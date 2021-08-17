package ru.tg.pawaptz.eng.messaging

import ru.tg.api.transport.TgUpdate
import kotlin.time.ExperimentalTime

interface MessageReactor {

    suspend fun register(selector: (TgUpdate) -> Boolean,
                         handler: suspend (TgUpdate) -> Unit)
}