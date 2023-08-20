package ru.otus.otuskotlin.marketplace.app.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit


class WsHandlerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, processor: MkplAdProcessor) {
        mutex.withLock {
            sessions.add(session)
        }

        // Handle init request
        val ctx = MkplContext()
        ctx.workMode = MkplWorkMode.STUB
        processor.exec(ctx)

        val init = apiV1Mapper.writeValueAsString(ctx.toTransportInit())
        session.outgoing.send(Frame.Text(init))

        // Handle flow
        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()
            val context = MkplContext()

            // Handle without flow destruction
            try {
                val request = apiV1Mapper.readValue<IRequest>(jsonStr)
                context.fromTransport(request)
                processor.exec(context)

                val result = apiV1Mapper.writeValueAsString(context.toTransportAd())

                // If change request, response is sent to everyone
                if (context.isUpdatableCommand()) {
                    mutex.withLock {
                        sessions.forEach {
                            it.send(Frame.Text(result))
                        }
                    }
                } else {
                    session.outgoing.send(Frame.Text(result))
                }
            } catch (_: ClosedReceiveChannelException) {
                mutex.withLock {
                    sessions.clear()
                }
            } catch (t: Throwable) {
                context.addError(t.asMkplError())

                val result = apiV1Mapper.writeValueAsString(context.toTransportInit())
                session.outgoing.send(Frame.Text(result))
            }
        }.collect()
    }
}
