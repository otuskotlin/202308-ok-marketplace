package coroutinescope

import kotlinx.coroutines.*

fun main() = runBlocking(CoroutineName("1")) {
//    coroutineScope {
//        val job = launch { 1/0 }
//        job.join()
//        println("finished не попадем сюда")
//    }
    coroutineScope {
        val job = launch() {
            supervisorScope {
                launch {  1/0 }
            }
        }
        job.join()
        println("finished должны попасть")
    }
}
