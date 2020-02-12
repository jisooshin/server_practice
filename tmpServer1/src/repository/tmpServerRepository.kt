package com.jslab.repository

import com.jslab.model.*
import com.jslab.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class tmpServerRepository: Repository {
    override suspend fun add(userId: String, titleValue: String, contentValue: String) =
        dbQuery {
            val insertStatement = titleContents.insert {
                it[user] = userId
                it[title] = titleValue
                it[content] = contentValue
            }
            val result = insertStatement.resultedValues?.get(0)
            if (result != null) {
                toTitleContent(result)
            } else {
                null
            }
        }


    override suspend fun getItem(id: Int): titleContent? = dbQuery {
        titleContents.select {
            (titleContents.id eq id)
        }.mapNotNull { toTitleContent(it) }.singleOrNull()
    }

    override suspend fun getItem(id: String): titleContent? {
        return getItem(id.toInt())
    }

    override suspend fun getItems(): List<titleContent> = dbQuery {
        titleContents.selectAll().map { toTitleContent(it) }
    }

    override suspend fun remove(id: Int): Boolean {
        if (getItem(id) == null) {
            throw IllegalArgumentException("No data found for id $id")
        }
        return dbQuery {
            titleContents.deleteWhere { titleContents.id eq id } > 0
        }
    }

    override suspend fun remove(id: String): Boolean {
        return remove(id.toInt())
    }

    override suspend fun clear() {
        titleContents.deleteAll()
    }

    override suspend fun user(userId: String, hash: String?): User? {
        val user = dbQuery {
            Users.select {
                (Users.id eq userId)
            }.mapNotNull { toUser(it) }.singleOrNull()
        }
        return when {
            user == null -> null
            hash == null -> user
            user.passwordHash == hash -> user
            else -> null
        }
    }

    override suspend fun userByEmail(email: String) = dbQuery {
        Users.select {Users.email eq email}
            .map {User(
                it[Users.id],
                email ,
                it[Users.displayName] ,
                it[Users.passwordHash])
        }.singleOrNull()
    }

    override suspend fun userById(userId: String) = dbQuery {
        Users.select { Users.id.eq(userId) }
            .map { User(userId, it[Users.email], it[Users.displayName], it[Users.passwordHash]) }.singleOrNull()
    }

    override suspend fun createUser(user: User) = dbQuery {
        Users.insert {
            it[id] = user.userId
            it[email] = user.email
            it[displayName] = user.displayName
            it[passwordHash] = user.passwordHash
        }
        Unit
    }

    private fun toTitleContent(row: ResultRow): titleContent =
        titleContent(
            id = row[titleContents.id].value,
            userId = row[titleContents.user],
            title = row[titleContents.title],
            content = row[titleContents.content])

    private fun toUser(row: ResultRow): User =
        User(
            userId = row[Users.id],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordHash = row[Users.passwordHash]
        )


}