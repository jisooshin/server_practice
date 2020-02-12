package com.jslab.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import java.io.Serializable

data class titleContent(
    val userId: String,
    val id: Int,
    val title: String,
    val content: String): Serializable

object titleContents: IntIdTable() {
    val user: Column<String> = varchar("user_id", 20).index()
    val title: Column<String> = varchar("title", 255)
    val content: Column<String> = varchar("content", 255)
}
