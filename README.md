![Sms77.io Logo](https://www.sms77.io/wp-content/uploads/2019/07/sms77-Logo-400x79.png "Sms77.io Logo")

# Official Kotlin Client for the Sms77.io SMS Gateway API

## Installation

*build.gradle.kts:*

```kotlin
dependencies {
    implementation("com.sms77:client")
}
```

### Usage

```kotlin
    fun main() {
        runBlocking {
            val client = getClient()
            println("balance: ${getBalance(client)}")
        }
    }
```


##### Support

Need help? Feel free to send us an <a href='mailto: support@sms77.io'>email</a>.
