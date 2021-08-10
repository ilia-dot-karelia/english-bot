package ru.tg.pawaptz.eng.core

import ru.tg.api.generic.TgBot
import ru.tg.api.inlined.TgChatId
import ru.tg.api.inlined.TgText

class InteractiveConsoleImpl(private val tgBot: TgBot) : InteractiveConsole {
    companion object {
        private const val HELLO_MESSAGE = "Hey! Whats up? What we gonna do today?"

        private const val OPT1 = "Teach me grammar"
        private const val OPT2 = "Ready for a test"
        private const val OPT3 = "Let`s learn the words"
    }

    override suspend fun sendMainMenu(chat: TgChatId) {
        tgBot.sendMessage(chat, TgText(HELLO_MESSAGE)){
            inlineKeyBoard {
                createNewLine {
                    addButton(OPT1, "grammar")
                    addButton(OPT2, "test")
                    addButton(OPT3, "words")
                }
            }
        }
    }
}