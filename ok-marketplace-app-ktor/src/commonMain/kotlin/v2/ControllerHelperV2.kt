package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: MkplAppSettings,
    klass: KClass<*>,
    logId: String,
) {
    appSettings.processor.process(appSettings.logger.logger(klass), logId,
        fromTransport = {
            val request = receive<Q>()
            principal = mkplPrincipal(appSettings)
            fromTransport(request)
        },
        sendResponse = { respond(toTransportAd()) }
    )
}

// TODO: костыль для решения проблемы отсутствия jwt в native
@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")
fun ApplicationCall.mkplPrincipal(appSettings: MkplAppSettings): MkplPrincipalModel = MkplPrincipalModel(
    id = MkplUserId("user-1"),
    fname = "Ivan",
    mname = "Ivanovich",
    lname = "Ivanov",
    groups = setOf(MkplUserGroups.TEST, MkplUserGroups.USER),
)
