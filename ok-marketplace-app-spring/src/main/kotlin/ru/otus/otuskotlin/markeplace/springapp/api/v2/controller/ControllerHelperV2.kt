package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import kotlinx.serialization.decodeFromString
import ru.otus.otuskotlin.markeplace.springapp.fakeMkplPrincipal
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV2(
    appSettings: MkplAppSettings,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
): R = appSettings.processor.process(logger, logId,
    fromTransport = {
        fromTransport(request)
        principal = fakeMkplPrincipal()
    },
    sendResponse = {  toTransportAd() as R }
)
