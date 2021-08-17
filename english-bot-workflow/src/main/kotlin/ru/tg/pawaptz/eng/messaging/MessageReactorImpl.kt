package ru.tg.pawaptz.eng.messaging

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import ru.tg.api.generic.TgBot
import ru.tg.api.transport.TgUpdate
import java.util.concurrent.Executors
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class MessageReactorImpl(private val tgBot: TgBot) : MessageReactor {
    companion object {
        private val log = LoggerFactory.getLogger(MessageReactorImpl::class.java)
    }
    private val mtx = Mutex()
    private val handlers: MutableList<UpdateHandler> = mutableListOf()
    private lateinit var job: Job
    private val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
    private val reactorScope = CoroutineScope(dispatcher + SupervisorJob() + CoroutineName("reactor"))

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            log.info("Start listening to the users commands")
            val receiveChannel = tgBot.subscribe()
            while (isActive) {
                val upd = receiveChannel.receive()
                log.debug("Received an update: $upd")
                callHandlers(upd)
            }
            log.warn("Stop listening to the users commands")
        }
    }

    fun stop() {
        job.cancel(CancellationException("Stopped by outer call"))
    }

    private fun callHandlers(upd: TgUpdate) {
        for (handler in handlers) {
            kotlin.runCatching { handler.handleIfMatch(upd) }
                .onFailure { log.error(it.message, it) }
        }
    }

    override suspend fun register(selector: (TgUpdate) -> Boolean,
                          handler: suspend (TgUpdate) -> Unit) {
        mtx.withLock {
            handlers.add(UpdateHandler(selector, handler))
        }
    }

    private inner class UpdateHandler(
        private val predicate: (TgUpdate) -> Boolean,
        private val handler: suspend (TgUpdate) -> Unit
    ) {

        fun handleIfMatch(upd: TgUpdate) {
            if (predicate.invoke(upd)) {
                reactorScope.launch {
                    withContext(MDCContext(mapOf("reactor-msg" to upd.updateId.toString()))) {
                        log.info("[reactor] Handling update")
                        handler.invoke(upd)
                        log.info("[reactor] Handling update")
                    }
                }
            }
        }
    }
}