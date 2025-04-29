package com.englishprep

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication
class DemoApplication {

	private val logger = LoggerFactory.getLogger(javaClass)

	@Bean
	fun init(env: Environment) = CommandLineRunner {
		val activeProfiles = env.activeProfiles.joinToString(", ")
		logger.info("Application started with active profiles: $activeProfiles")
		logger.info("Server is running on port: ${env.getProperty("server.port")}")
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

