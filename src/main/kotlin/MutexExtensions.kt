import kotlinx.coroutines.sync.Mutex

/**
 * Unlocks the [Mutex] ignoring any exceptions.
 */
fun Mutex.tryUnlock() {
    try {
        this.unlock()
    } catch (e: Exception) {
        // ignore
    }
}