package org.bbakaple.monsing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MonsingApplication

fun main(args: Array<String>) {
    runApplication<MonsingApplication>(*args)
}
