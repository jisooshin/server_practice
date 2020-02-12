package com.jslab

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.jslab.model.*
import java.util.*

class JwtService {
    private val issuer = "emojiphrases"
    private val jwtSecret = "kZnzR7iw463VyysmS9qln"//System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.userId)
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    private fun expiresAt() = Date(System.currentTimeMillis() + 1_000_000)
}