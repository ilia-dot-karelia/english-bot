package ru.tg.pawaptz.eng.core

class UserCommandRegistry : CommandRegistry {

    private val map: MutableMap<Command, (CommandContext) -> Unit> = mutableMapOf()

    override fun registerCommand(cmd: Command, t: (CommandContext) -> Unit) {
        map[cmd] = t
    }

    override fun handleCommand(ctx: CommandContext) {
        val f = map[ctx.cmd] ?: throw UnsupportedOperationException("Can not handle user command $ctx")
        f.invoke(ctx)
    }
}