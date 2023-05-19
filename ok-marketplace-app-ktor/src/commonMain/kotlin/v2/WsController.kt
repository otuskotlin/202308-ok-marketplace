package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

class WsHandlerV2 {
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

        val init = apiV2ResponseSerialize(ctx.toTransportInit())
        session.outgoing.send(Frame.Text(init))

        // Handle flow
        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()
            val context = MkplContext()

            // Handle without flow destruction
            try {
                val request = apiV2Mapper.decodeFromString<IRequest>(jsonStr)
                context.fromTransport(request)
                processor.exec(context)

                val result = apiV2ResponseSerialize(context.toTransportAd())

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

                val result = apiV2ResponseSerialize(context.toTransportInit())
                session.outgoing.send(Frame.Text(result))
            }
        }.collect()
    }
}
