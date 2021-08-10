package ru.tg.pawaptz.eng.core

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.tg.api.generic.TgBot
import ru.tg.api.inlined.ChatType
import ru.tg.api.inlined.FirstName
import ru.tg.api.inlined.TgChatId
import ru.tg.api.inlined.TgMessageId
import ru.tg.api.transport.TgChat
import ru.tg.api.transport.TgMessage
import ru.tg.api.transport.TgUpdate
import ru.tg.api.transport.TgUser
import ru.tg.pawaptz.eng.core.UserCommandHandler.Companion.CMD_MENU

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal class UserCommandHandlerTest {

    private val console: InteractiveConsole = mockk()
    private val tgBot: TgBot = mockk()
    private val registry = UserCommandRegistry
    private val handler = UserCommandHandler(tgBot, console, registry)
    private val broadcastChannel = BroadcastChannel<TgUpdate>(Channel.BUFFERED)
    private val receiveChannel = broadcastChannel.openSubscription()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { tgBot.subscribe() } returns receiveChannel
        handler.start()
    }

    @AfterEach
    internal fun tearDown() {
        handler.stop()
    }

    @Test
    fun whenUserPressesMenuThenShowTheMainMenu() = runBlocking {
        val chat = TgChat(1001, ChatType("regular"))
        broadcastChannel.send(
            TgUpdate(
                666, TgMessage(
                    TgMessageId(100), TgUser(12, false, FirstName("user")), chat, 100500, chat, CMD_MENU, null,
                ), null, null
            )
        )

        coVerify(timeout = 300) { console.sendMainMenu(TgChatId(1001)) }
    }
}