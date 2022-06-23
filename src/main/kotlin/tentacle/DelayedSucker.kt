package tentacle

import kotlinx.coroutines.sync.Mutex

/**
 * A [Sucker] with a [Mutex] used to wait for the async action to complete.
 */
data class DelayedSucker (
    val callMutex: Mutex,
    val sucker: Sucker,
)