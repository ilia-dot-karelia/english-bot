package ru.tg.pawaptz.eng.core

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.tg.api.inlined.FirstName
import ru.tg.api.inlined.TgChatId
import ru.tg.api.transport.TgUser


internal class UserCommandRegistryImplTest {

    @MockK
    lateinit var console: InteractiveConsole

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    private val handler = UserCommandRegistry()

    @Test
    fun whenUserCommandRegisteredAndInvokedThenCall(){
        handler.registerCommand(Command("/start")){
            console.sendMainMenu(it.chat)
        }
        handler.handleCommand(CommandContext(TgUser(125, true, FirstName("name")), Command("/start"), TgChatId(123)))
        verify { console.sendMainMenu(TgChatId(123)) }
    }
}