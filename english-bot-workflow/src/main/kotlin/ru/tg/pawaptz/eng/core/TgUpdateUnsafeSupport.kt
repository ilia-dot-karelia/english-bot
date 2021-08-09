package ru.tg.pawaptz.eng.core

import ru.tg.api.inlined.TgChatId
import ru.tg.api.transport.TgUpdate
import ru.tg.api.transport.TgUser

fun TgUpdate.user(): TgUser {
    return this.message?.from ?: throw IllegalStateException("check the update $this")
}

fun TgUpdate.chatId(): TgChatId {
    return TgChatId(this.message?.chat?.id ?: throw java.lang.IllegalStateException("check the update $this"))
}

fun TgUpdate.command(): Command {
    return Command(this.message?.text ?: throw IllegalStateException("check the update $this"))
}

fun TgUpdate.isCommand() : Boolean {
    return this.message?.text?.startsWith("/") == true
}