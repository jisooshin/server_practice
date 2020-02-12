package com.jslab.api

import com.jslab.*
import com.jslab.api.requests.*
import com.jslab.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val DATAVIEW_ENDPOINT = "$API_VERSION/dataview"

@Location(DATAVIEW_ENDPOINT)
class dataViewApi

fun Route.dataViewApi(db: Repository) {
    authenticate("jwt") {
        get<dataViewApi> {
            call.respond(db.getItems())
        }

        post<dataViewApi> {
            val user = call.apiUser!!
            try {
                val request = call.receive<dataViewApiRequest>()
                val data = db.add(user.userId, request.title, request.content)
                if (data != null) {
                    call.respond(data)
                } else {
                    call.respondText("Invalid data received.", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Throwable) {
                call.respondText("Invalid data received.", status = HttpStatusCode.BadRequest)
            }

        }
    }
}