package com.englishprep.auth.service

import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    @Value("\${spring.mail.from}")
    lateinit var fromEmail: String

    fun sendMagicLinkEmail(email: String, magicLinkUrl: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setFrom(fromEmail, "AI English Test Preparation")
        helper.setSubject("🚀 Your Magic Link to Sign In - AI English Test Prep")

        helper.setText(
            """
            <html>
            <body style="font-family:Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
                <div style="max-width:600px; margin:0 auto; background:white; padding:30px; border-radius:8px; box-shadow:0 2px 6px rgba(0,0,0,0.1);">
                    <h2 style="color:#4f46e5;">🎯 Welcome Back!</h2>
                    <p style="font-size:16px; color:#333;">You're one click away from signing in to your account.</p>
                    <div style="text-align:center; margin:30px 0;">
                        <a href="$magicLinkUrl" style="padding:12px 24px; background-color:#4f46e5; color:white; text-decoration:none; border-radius:6px; font-size:16px;">
                            🔑 Sign In Now
                        </a>
                    </div>
                    <p style="font-size:14px; color:#666;">This link will expire in 15 minutes for your security. If you didn't request this, please ignore this email. 🛡️</p>
                    <p style="font-size:14px; color:#666;">Thank you,<br><strong>AI English Test Preparation Team</strong> 📚</p>
                </div>
            </body>
            </html>
            """.trimIndent(),
            true
        )

        mailSender.send(message)
    }
}