package org.jesperancinha.twitter.model.twitter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UserTest {
    private val objectMapper = ObjectMapper()
    @Test
    @Throws(JsonProcessingException::class)
    fun testEquals_whenEquals_Ok() {
        val user1 = objectMapper.readValue(
            """{
      "id": 988105075267637248,
      "id_str": "988105075267637248",
      "name": "Climate Alliance Switzerland",
      "screen_name": "climalliancech",
      "created_at": "Sun Apr 22 17:19:42 +0000 2018"
    }""", User::class.java)
        val user2 = objectMapper.readValue(
            """{
      "id": 988105075267637248,
      "id_str": "988105075267637248",
      "name": "Climate BALALLA Switzerland",
      "screen_name": "UUUU",
      "created_at": "Sun Jan 22 17:19:42 +0000 2018"
    }""", User::class.java)
        Assertions.assertThat(user1).isEqualTo(user2)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testHashCode_whenOther_NotSame() {
        val user1 = objectMapper.readValue(
            """{
      "id": 988105075267637248,
      "id_str": "988105075267637248",
      "name": "Climate Alliance Switzerland",
      "screen_name": "climalliancech",
      "created_at": "Sun Apr 22 17:19:42 +0000 2018"
    }""", User::class.java)
        val user2 = objectMapper.readValue(
            """{
      "id": 988105075267637248,
      "id_str": "988105075267637248",
      "name": "Climate BALALLA Switzerland",
      "screen_name": "UUUU",
      "created_at": "Sun Jan 22 17:19:42 +0000 2018"
    }""", User::class.java)
        Assertions.assertThat(user1).isNotSameAs(user2)
        Assertions.assertThat(user1.hashCode()).isEqualTo(user2.hashCode())
    }
}