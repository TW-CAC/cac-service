package com.cac.service.config

import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory


@Configuration
class MongoConfig {
    @Value("\${mongo.connection.string}")
    private lateinit var mongoConnectionString: String
    /*@Value("\${mongo.userName}")
    private val userName: String? = null
    @Value("\${mongo.password}")
    private val password: String? = null*/
    @Value("\${mongo.dbname}")
    private lateinit var dbName: String

    @Bean
    fun mongoDbFactory() =
        SimpleMongoClientDbFactory(MongoClients.create(mongoConnectionString), dbName)
}