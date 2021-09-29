package org.jesperancinha.twitter.examples

open class AppleService {
    // TODO: Example of method not implemented yet.
    open val appleFromFrige: Int
        get() {
            // TODO: Example of method not implemented yet.
            throw RuntimeException("Method not implemented yet!")
        }

    // TODO: Example of method not implemented yet.
    open val appleFromSupermarket: Int
        get() {
            // TODO: Example of method not implemented yet.
            throw RuntimeException("Method not implemented yet!")
        }
    val totalApples: Int
        get() = appleFromFrige + appleFromSupermarket
}