package com.jslab.webapp

import com.jslab.*
import com.jslab.model.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNOUT = "/signout"

@Location(SIGNOUT)
class Signout

fun Route.signout() {
    get<Signout> {
        call.sessions.clear<dataViewSession>()
        call.redirect(Signin())
    }
}