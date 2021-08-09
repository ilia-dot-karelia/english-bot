package ru.tg.pawaptz.eng.core

interface CommandRegistry {

    fun registerCommand(cmd: Command, t: (CommandContext) -> Unit)

    fun handleCommand(ctx: CommandContext)
}