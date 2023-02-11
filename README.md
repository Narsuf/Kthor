# ktor-webservice
[![CI](https://github.com/Narsuf/ktor-webservice/actions/workflows/kotlin.yml/badge.svg)](https://github.com/Narsuf/ktor-webservice/actions/workflows/kotlin.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Web service based on Ktor designed to access a database and retrieve information from
the elections.

## Quick start
In order to make it work you will need to add an `application.conf` file inside 
`src/main/resources`.

```
ktor {
    deployment {
        port = 1234
        port = ${?PORT}
        dbDriver = "your.db.driver"
        dbUrl = "jdbc:yourdb://localhost:5678/dbname"
        user = "user"
        password = "password"
        build = "debug"
    }

    application {
        modules = [ com.jorgedguezm.ApplicationKt.module ]
    }
}
```

No need to say you will need a database running that you can access to.

## Documentation
More information about the expected responses from this web service 
[here](https://github.com/Narsuf/ktor-webservice/tree/main/docs).
