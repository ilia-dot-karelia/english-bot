package ru.tg.pawaptz.eng.messaging

import anyMessage
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.tg.api.generic.TgBot
import ru.tg.api.transport.TgUpdate

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal class MessageReactorImplTest {
    private val channel = BroadcastChannel<TgUpdate>(100)

    @MockK
    private lateinit var tgBot: TgBot

    @InjectMockKs
    private lateinit var msgReactor: MessageReactorImpl

    @MockK
    private lateinit var handlerMock : (TgUpdate) -> Unit

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        tgBot.also {
            every { it.subscribe() } returns channel.openSubscription()
        }
        msgReactor.start()
    }

    @AfterEach
    internal fun tearDown() {
        msgReactor.stop()
    }

    @Test
    fun whenHandlerRegisteredAndSelectorMatchesThenCallAsync() = runBlocking{
        handlerMock.also {
            every { handlerMock.invoke(any()) } returns Unit
        }
        msgReactor.register({upd -> upd.updateId == 123L}){
            handlerMock.invoke(it)
        }
        assertThat(channel.trySend(TgUpdate(123)).isSuccess).isTrue
        verify(timeout = 500) { handlerMock.invoke(TgUpdate(123)) }

        confirmVerified(handlerMock)
    }
}