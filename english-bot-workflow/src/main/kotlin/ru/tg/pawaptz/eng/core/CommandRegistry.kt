package ru.tg.pawaptz.eng.core

interface CommandRegistry {

    fun registerCommand(cmd: Command, t: suspend (CommandContext) -> Unit)

    suspend fun handleCommand(ctx: CommandContext)
}