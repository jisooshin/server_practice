package com.jslab

import io.ktor.util.*
import io.ktor.utils.io.charsets.Charsets
import javax.crypto.*
import javax.crypto.spec.*


const val MIN_USER_ID_LENGTH = 4
const val MIN_PASSWORD_LENGTH = 6

// val hashKey = hex(System.getenv("SECRET_KEY"))
val hashKey = hex("6819b57a326945c1968f45236589")

val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

private val userIdPattern = "[a-zA-Z0-9\\.]+".toRegex()

internal fun userNameValid(userId: String) = userId.matches(userIdPattern)
