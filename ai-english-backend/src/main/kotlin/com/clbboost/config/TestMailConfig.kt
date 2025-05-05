package com.clbboost.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
@Profile("test") // ✅ Only active for "test" profile
class TestMailConfig {

    @Bean
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl() // Dummy sender that won't really send
    }
}
