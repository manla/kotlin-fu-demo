package com.maranin.kotlinfundemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
class KotlinFunDemoApplication

fun main(args: Array<String>) {
    runApplication<KotlinFunDemoApplication>(*args)
}
