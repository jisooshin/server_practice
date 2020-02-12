package com.jslab.webapp

import com.jslab.model.*
import com.jslab.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*


const val ABOUT = "/about"

@Location(ABOUT)
class About

fun Route.about(db: Repository) {
    get <About> {
        val user = call.sessions.get<dataViewSession>()?.let { db.user(it.userId) }
        call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}