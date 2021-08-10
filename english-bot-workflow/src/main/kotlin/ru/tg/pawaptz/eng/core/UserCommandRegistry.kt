package ru.tg.pawaptz.eng.core

object UserCommandRegistry : CommandRegistry {

    private val map: MutableMap<Command, suspend (CommandContext) -> Unit> = mutableMapOf()

    override fun registerCommand(cmd: Command, t: suspend (CommandContext) -> Unit) {
        map[cmd] = t
    }

    override suspend fun handleCommand(ctx: CommandContext) {
        val f = map[ctx.cmd] ?: throw UnsupportedOperationException("Can not handle user command $ctx")
        f.invoke(ctx)
    }
}