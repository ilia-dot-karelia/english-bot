package ru.tg.pawaptz.eng.core

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import ru.tg.api.generic.TgBot
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
class UserCommandHandler(
    private val tgBot: TgBot,
    private val console: InteractiveConsole,
    private val commandRegistry: CommandRegistry
) {
    companion object {
        const val CMD_MENU = "/menu"

        private val log = LoggerFactory.getLogger(UserCommandHandler::class.java)
    }

    init {
        commandRegistry.registerCommand(Command("/menu")) {
            console.sendMainMenu(it.chat)
        }
    }

    private lateinit var job: Job

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            log.info("Start listening to the users commands")
            val receiveChannel = tgBot.subscribe()
            while (isActive) {
                val upd = receiveChannel.receive()
                log.debug("Received an update: $upd")
                if (upd.isCommand()) {
                    log.info("Received an update $upd")
                    commandRegistry.handleCommand(
                        CommandContext(
                            upd.user(), upd.command(), upd.chatId()
                        )
                    )
                }
            }
            log.warn("Stop listening to the users commands")
        }
    }

    fun stop() {
        job.cancel(CancellationException("Stopped by outer call"))
    }

}