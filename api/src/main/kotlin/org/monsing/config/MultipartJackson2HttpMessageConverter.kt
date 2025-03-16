package org.monsing.config

import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.reflect.Type
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.stereotype.Component

@Component
class MultipartJackson2HttpMessageConverter(objectMapper: ObjectMapper) : AbstractJackson2HttpMessageConverter(
    objectMapper, MediaType.APPLICATION_OCTET_STREAM
) {

    override fun canWrite(mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }

    override fun canWrite(type: Type?, clazz: Class<*>, mediaType: MediaType?): Boolean {
        return false
    }
}
