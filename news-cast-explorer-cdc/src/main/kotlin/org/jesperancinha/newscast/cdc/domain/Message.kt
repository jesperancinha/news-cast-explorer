package org.jesperancinha.newscast.cdc.domain

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by jofisaes on 08/10/2021
 */
@Entity
@Table(name = "message", schema = "eventuate")
data class Message(
    @Id
    val id: String? = null,
    val destination: String? = null,
    val headers: String? = null,
    val payload: String? = null,
    val creation_time: Long? = null,
    val published: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Message

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , destination = $destination , headers = $headers , payload = $payload , creation_time = $creation_time , published = $published )"
    }
}