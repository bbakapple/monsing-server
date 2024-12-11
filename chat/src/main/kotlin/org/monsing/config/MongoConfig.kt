package org.monsing.config

import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConfig {

    fun mongoTemplate() = MongoTemplate(mongoClient(), "test")

    fun mongoClient() = MongoClients.create()
}
