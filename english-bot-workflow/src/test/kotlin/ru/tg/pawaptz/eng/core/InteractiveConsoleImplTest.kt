package ru.tg.pawaptz.eng.core

import anyMessage
import anyUser
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.tg.api.generic.TgBot
import ru.tg.api.inlined.TgChatId
import ru.tg.api.inlined.TgText
import ru.tg.api.transport.TgCallbackQuery
import ru.tg.api.transport.TgUpdate
import ru.tg.pawaptz.eng.messaging.MessageReactor
import kotlin.time.ExperimentalTime

@ExperimentalTime
class InteractiveConsoleImplTest {

    @MockK
    lateinit var bot: TgBot

    @MockK
    lateinit var updateSelector: MessageReactor

    @InjectMockKs
    lateinit var console: InteractiveConsoleImpl

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun whenSentMainMenuThenBotSendMessageIsCalled() = runBlocking {
        val chatId = TgChatId(123)
        coEvery { bot.sendMessage(TgChatId(any()), TgText(any()), any()) } returns anyMessage()
        console.sendMainMenu(chatId)

        coVerify { bot.sendMessage(chatId, TgText(any()), any()) }
        confirmVerified(bot)
    }
}