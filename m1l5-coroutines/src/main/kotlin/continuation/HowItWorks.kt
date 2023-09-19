package continuation

import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

fun main() = runBlocking(Dispatchers.Default) {
    println("Start")
//    launch {
//        delay(1000)
//        repeat(5) {
//            delay(500)
//            println("Working....")
//        }
//        println("Done")
//    }
//
//    suspendCoroutine<Unit> { continuation ->
////        continuation.resume(Unit)
//    }

    println("Finish")
}
