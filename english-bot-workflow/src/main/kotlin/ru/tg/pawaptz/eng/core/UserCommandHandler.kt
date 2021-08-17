package ru.tg.pawaptz.eng.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import ru.tg.pawaptz.eng.messaging.MessageReactor
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
class UserCommandHandler(
    private val messageReactor: MessageReactor,
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

    fun init() = runBlocking {
        messageReactor.register({ it.isCommand() }) {
            commandRegistry.handleCommand(
                CommandContext(
                    it.user(), it.command(), it.chatId()
                )
            )
        }
    }

    fun stop() {
        job.cancel(CancellationException("Stopped by outer call"))
    }

}