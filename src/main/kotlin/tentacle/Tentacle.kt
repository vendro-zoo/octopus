package tentacle

import kotlinx.coroutines.sync.Mutex
import tryUnlock
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A thread-safe queue that can process different type of [Sucker]s.
 */
class Tentacle {
    private var started = false
    private val executionMutex: Mutex = Mutex(true)
    private val callMutexes = ConcurrentLinkedQueue<DelayedSucker>()

    /**
     * Starts the queue and processes the [Sucker]s.
     *
     * The process pauses if there are no [Sucker]s in the queue.
     * When a [Sucker] is added to the queue, the process will resume until the queue is empty.
     *
     * @throws IllegalStateException if the [Tentacle] is already started.
     */
    suspend fun run() {
        if (started) throw IllegalStateException("Queue already started")
        started = true

        while (true) {
            executionMutex.lock()
            callMutexes.poll()?.let { dCall ->
                dCall.sucker()
                dCall.callMutex.unlock()
                executionMutex.tryUnlock()
            }
        }
    }

    /**
     * Puts the [Sucker] into the queue and waits for it to be executed.
     *
     * @param sucker The [Sucker] to be executed.
     */
    suspend fun queueCall(sucker: Sucker) {
        val mutex = Mutex(true)
        callMutexes.add(DelayedSucker(mutex, sucker))
        executionMutex.tryUnlock()
        mutex.lock()
    }
}