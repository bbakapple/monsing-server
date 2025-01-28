package org.monsing.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.NoIvGenerator
import org.jasypt.salt.RandomSaltGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableEncryptableProperties
@Configuration
class JasyptConfig(
    @Value("\${jasypt.encryptor.password}") private val password: String
) {

    @Bean
    fun jasyptStringEncryptor() = StandardPBEStringEncryptor().apply {
        setPassword(password)
        setSaltGenerator(RandomSaltGenerator())
        setIvGenerator(NoIvGenerator())
    }
}
