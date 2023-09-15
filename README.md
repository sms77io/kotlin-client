![](https://www.seven.io/wp-content/uploads/Logo.svg "seven Logo")

# Official Kotlin Client for the seven.io SMS Gateway API

## Installation

*build.gradle.kts:*

```kotlin
dependencies {
    implementation("com.seven:client")
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

Need help? Feel free to [contact us](https://www.seven.io/en/company/contact/).

[![MIT](https://img.shields.io/badge/License-MIT-teal.svg)](LICENSE)
