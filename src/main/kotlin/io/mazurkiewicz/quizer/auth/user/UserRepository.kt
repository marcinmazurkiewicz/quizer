package io.mazurkiewicz.quizer.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun findByUserPublicId(userPublicId: UUID): User?
}