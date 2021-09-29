package org.jesperancinha.twitter.model.twitter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MessageTest {
    @Test
    @Throws(JsonProcessingException::class)
    fun testEquals_whenEquals_Ok() {
        val message1 = objectMapper.readValue(
            """{
  "created_at": "Mon Feb 13 12:15:04 +0000 2020",
  "id": 1216770650121084933,
  "id_str": "1216770650121084933",
  "text": "Message3",
  "user": {
    "id": 3024323693,
    "name": "Author2"  }}""", Message::class.java)
        val message2 = objectMapper.readValue(
            """{
  "created_at": "Mon Jan 13 17:15:04 +0000 2020",
  "id": 1216770650121084933,
  "id_str": "1216770650121084933",
  "text": "Message5",
  "user": {
    "id": 3024323693,
    "name": "Author3",
    "notifications": null
  }}""", Message::class.java)
        Assertions.assertThat(message1).isEqualTo(message2)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testHashCode_whenOther_NotSame() {
        val message1 = objectMapper.readValue(
            """{
  "created_at": "Mon Feb 13 12:15:04 +0000 2020",
  "id": 1216770650121084933,
  "id_str": "1216770650121084933",
  "text": "Message3",
  "user": {
    "id": 3024323693,
    "name": "Author2"  }}""", Message::class.java)
        val message2 = objectMapper.readValue(
            """{
  "created_at": "Mon Jan 13 17:15:04 +0000 2020",
  "id": 1216770650121084933,
  "id_str": "1216770650121084933",
  "text": "Message5",
  "user": {
    "id": 3024323693,
    "name": "Author3",
    "notifications": null
  }}""", Message::class.java)
        Assertions.assertThat(message1).isNotSameAs(message2)
        Assertions.assertThat(message1.hashCode()).isEqualTo(message2.hashCode())
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}