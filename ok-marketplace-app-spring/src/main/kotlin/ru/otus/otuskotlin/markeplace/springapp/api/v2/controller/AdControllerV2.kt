package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(private val appSettings: MkplAppSettings) {
    private val logger by lazy { appSettings.logger.logger(AdControllerV2::class) }
    @PostMapping("create")
    suspend fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse =
        processV2(appSettings, request, logger, "ad-create")

    @PostMapping("read")
    suspend fun  readAd(@RequestBody request: AdReadRequest): AdReadResponse =
        processV2(appSettings, request, logger, "ad-read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        processV2(appSettings, request, logger, "ad-update")

    @PostMapping("delete")
    suspend fun  deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        processV2(appSettings, request, logger, "ad-delete")

    @PostMapping("search")
    suspend fun  searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse =
        processV2(appSettings, request, logger, "ad-search")
}
