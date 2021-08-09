package ru.tg.pawaptz.eng.core

import ru.tg.api.inlined.TgChatId

interface InteractiveConsole {

    fun sendMainMenu(chat: TgChatId)
}