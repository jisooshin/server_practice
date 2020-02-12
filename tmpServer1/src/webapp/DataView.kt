package com.jslab.webapp

import com.jslab.*
import com.jslab.model.*
import com.jslab.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.sessions.*

const val DATAVIEW = "/dataview"

@Location(DATAVIEW)
class dataView

fun Route.dataView(db: Repository, hashFuntion: (String) -> String) {
    get<dataView> {
        val user = call.sessions.get<dataViewSession>()?.let { db.user(it.userId) }
        if (user == null) {
            call.redirect(Signin())
        } else {
            val date = System.currentTimeMillis()
            val code = call.securityCode(date, user, hashFuntion)
            val data = db.getItems()
            call.respond(
                FreeMarkerContent(
                    "data_view.ftl",
                    mapOf(
                        "displayName" to user.displayName,
                        "data" to data,
                        "user" to user,
                        "date" to date,
                        "code" to code),
                    user.userId
                )
            )
        }
    }
    post<dataView> {
        val user = call.sessions.get<dataViewSession>()?.let { db.user(it.userId) }
        val param = call.receiveParameters()
        val date = param["date"]?.toLongOrNull() ?: return@post call.redirect( it )
        val code = param["code"] ?: return@post call.redirect( it )
        val action = param["action"] ?: throw IllegalArgumentException("Missing parameter: action")

        if (user == null || !call.verifyCode(date, user, code, hashFuntion)) {
            call.redirect(Signin())
        }
        when (action) {
            "delete" -> {
                val id = param["id"] ?: throw IllegalArgumentException("Mission parameter: id")
                db.remove(id)
            }
            "add" -> {
                val title = param["title"] ?: throw IllegalArgumentException("Missing parameter: title")
                val content = param["content"] ?: throw IllegalArgumentException("Missing parameter: content")
                db.add(user!!.userId, title, content)
            }
        }
        call.redirect(dataView())
    }

}