package com.jslab.repository

import com.jslab.model.*

interface Repository {
    suspend fun add(userId: String, emojiValue: String, phraseValue: String): titleContent?
    suspend fun getItem(id: Int):titleContent?
    suspend fun getItem(id: String):titleContent?
    suspend fun getItems(): List<titleContent> // prev -> ArrayList<EmojiPhrase>
    suspend fun remove(id: Int): Boolean
    suspend fun remove(id: String): Boolean
    suspend fun clear()
    suspend fun user(userId: String, hash: String? = null): User?
    suspend fun userByEmail(email: String): User?
    suspend fun userById(userId: String): User?
    suspend fun createUser(user: User)
}