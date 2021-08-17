package ru.tg.pawaptz.eng.core

import org.slf4j.LoggerFactory
import ru.tg.api.generic.TgBot
import ru.tg.api.inlined.TgChatId
import ru.tg.api.inlined.TgText
import kotlin.time.ExperimentalTime

@ExperimentalTime
class InteractiveConsoleImpl(
    private val tgBot: TgBot
) : InteractiveConsole {
    companion object {
        private const val HELLO_MESSAGE = "Hey! Whats up? What we gonna do today?"

        private const val OPT1 = "Teach me grammar"
        private const val OPT2 = "Ready for a test"
        private const val OPT3 = "Let`s learn the words"

        private val log = LoggerFactory.getLogger(InteractiveConsole::class.java)
    }

    override suspend fun sendMainMenu(chat: TgChatId) {
        val sentMessage = tgBot.sendMessage(chat, TgText(HELLO_MESSAGE)) {
            inlineKeyBoard {
                createNewLine {
                    addButton(OPT1, "grammar")
                    addButton(OPT2, "test")
                    addButton(OPT3, "words")
                }
            }
        }
        log.debug("Sent message to chat $chat, message returned: $sentMessage")
    }


}