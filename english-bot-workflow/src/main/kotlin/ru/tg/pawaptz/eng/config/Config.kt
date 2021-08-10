package ru.tg.pawaptz.eng.config

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import ru.tg.api.generic.TgBot
import ru.tg.api.generic.TgBotImpl
import ru.tg.pawaptz.eng.core.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@SuppressWarnings("unused")
@SpringBootConfiguration
@PropertySource("file:private/secrets.properties")
class Config {

    @Bean
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun tgBot(@Value("\${tg.bot.token}") token: String): TgBot {
        return TgBotImpl.create(token)
    }

    @Bean
    fun userInteractiveConsole(tgBot: TgBot) : InteractiveConsole {
        return InteractiveConsoleImpl(tgBot)
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun userCommandHandler(tgBot: TgBot, userConsole: InteractiveConsole): UserCommandHandler {
        return UserCommandHandler(tgBot, userConsole, UserCommandRegistry)
    }
}