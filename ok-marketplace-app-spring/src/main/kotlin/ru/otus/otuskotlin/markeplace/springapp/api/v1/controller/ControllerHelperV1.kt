package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import ru.otus.otuskotlin.markeplace.springapp.fakeMkplPrincipal
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV1(
    appSettings: MkplAppSettings,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
): R = appSettings.processor.process(logger, logId,
    fromTransport = {
        fromTransport(request)
        principal = fakeMkplPrincipal()
    },
    sendResponse = { toTransportAd() as R }
)
