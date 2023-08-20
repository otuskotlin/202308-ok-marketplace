package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings

suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) : Unit =
    processV2<AdCreateRequest, AdCreateResponse>(appSettings, ApplicationCall::createAd::class, "ad-create")

suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) : Unit =
    processV2<AdReadRequest, AdReadResponse>(appSettings, ApplicationCall::readAd::class, "ad-read")

suspend fun ApplicationCall.updateAd(appSettings: MkplAppSettings) : Unit =
    processV2<AdUpdateRequest, AdUpdateResponse>(appSettings, ApplicationCall::updateAd::class, "ad-update")

suspend fun ApplicationCall.deleteAd(appSettings: MkplAppSettings) : Unit =
    processV2<AdDeleteRequest, AdDeleteResponse>(appSettings, ApplicationCall::deleteAd::class, "ad-delete")

suspend fun ApplicationCall.searchAd(appSettings: MkplAppSettings) : Unit =
    processV2<AdSearchRequest, AdSearchResponse>(appSettings, ApplicationCall::searchAd::class, "ad-search")
