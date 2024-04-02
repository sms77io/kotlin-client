package com.seven

abstract class BaseTest {
    protected val clientParams = ClientParams(
        apiKey = System.getenv("SEVEN_API_KEY_SANDBOX"),
        debug = true,
        dummy = true,
        sentWith = "Kotlin-Test",
        testing = true,
    )

    protected val client = getClient(clientParams)
}
