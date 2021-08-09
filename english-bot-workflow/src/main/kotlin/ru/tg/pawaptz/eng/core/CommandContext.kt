package ru.tg.pawaptz.eng.core

import ru.tg.api.inlined.TgChatId
import ru.tg.api.transport.TgUser

data class CommandContext(
    val user: TgUser,
    val cmd: Command,
    val chat: TgChatId
)