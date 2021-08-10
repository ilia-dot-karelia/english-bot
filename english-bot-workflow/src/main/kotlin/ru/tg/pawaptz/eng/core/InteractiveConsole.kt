package ru.tg.pawaptz.eng.core

import ru.tg.api.inlined.TgChatId

interface InteractiveConsole {

    suspend fun sendMainMenu(chat: TgChatId)
}