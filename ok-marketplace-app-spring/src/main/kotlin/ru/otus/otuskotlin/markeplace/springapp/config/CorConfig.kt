package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

@Configuration
class CorConfig {
    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun processor() = MkplAdProcessor(corSettings())

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
        repoStub = AdRepoStub(),
        repoProd = AdRepoInMemory(),
        repoTest = AdRepoInMemory(),
    )

    @Bean
    fun appSettings(corSettings: MkplCorSettings) = MkplAppSettings(
        processor = processor(),
        logger = loggerProvider(),
        corSettings = corSettings
    )
}
