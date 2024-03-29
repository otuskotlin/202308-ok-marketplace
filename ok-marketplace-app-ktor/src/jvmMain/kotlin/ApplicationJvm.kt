package ru.otus.otuskotlin.marketplace.app

import com.auth0.jwt.JWT
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.app.base.resolveAlgorithm
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.plugins.initAppSettings
import ru.otus.otuskotlin.marketplace.app.v1.WsHandlerV1
import ru.otus.otuskotlin.marketplace.app.v1.v1Ad
import ru.otus.otuskotlin.marketplace.app.v1.v1Offer
import ru.otus.otuskotlin.marketplace.app.v2.WsHandlerV2
import java.time.Duration
import ru.otus.otuskotlin.marketplace.app.module as commonModule

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(appSettings: MkplAppSettings = initAppSettings()) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    commonModule(appSettings)

    install(CallLogging) {
        level = Level.INFO
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    val handlerV1 = WsHandlerV1()
    val handlerV2 = WsHandlerV2()

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@moduleJvm.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }

            authenticate("auth-jwt") {
                v1Ad(appSettings)
                v1Offer(appSettings)
            }
        }

        webSocket("/ws/v1") {
            handlerV1.handle(this, appSettings)
        }
        webSocket("/ws/v2") {
            handlerV2.handle(this, appSettings)
        }

        static("static") {
            resources("static")
        }
    }
}
