package com.jslab.webapp

import com.jslab.*
import com.jslab.model.*
import com.jslab.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNUP = "/signup"

@Location(SIGNUP)
data class Signup(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val error: String = ""
)

fun Route.signup(db: Repository, hashFunction: (String) -> String) {
    post<Signup> {
        val user = call.sessions.get<dataViewSession>()?.let { db.user(it.userId) }
        if (user != null) return@post call.redirect(dataView())

        val signupParameters = call.receive<Parameters>()
        val userId = signupParameters["userId"]?: return@post call.redirect(it)
        val password = signupParameters["password"]?: return@post call.redirect(it)
        val displayName = signupParameters["displayName"]?: return@post call.redirect(it)
        val email = signupParameters["email"]?: return@post call.redirect(it)

        val signUpError = Signup(userId, displayName, email)

        when {
            password.length < MIN_PASSWORD_LENGTH
                -> call.redirect(signUpError.copy(error = "Password should be at least $MIN_PASSWORD_LENGTH"))
            userId.length < MIN_USER_ID_LENGTH
                -> call.redirect(signUpError.copy(error = "User id should be at least $MIN_USER_ID_LENGTH"))
            !userNameValid(userId)
                -> call.redirect(signUpError.copy(error = "User id isn't follow the regex."))
            db.user(userId) != null
                -> call.redirect(signUpError.copy(error = "User already exists."))
            else -> {
                val hash = hashFunction(password)
                val newUser = User(userId, email, displayName, hash)
                try {
                    db.createUser(newUser)
                } catch(e: Throwable) {
                    when {
                        db.user(userId) != null
                            -> call.redirect(signUpError.copy(error = "User with the following username is already exists."))
                        db.userByEmail(userId) != null
                            -> call.redirect(signUpError.copy(error = "User with the following email $email is already exists."))
                        else -> {
                            application.log.error("Failed to registre user", e)
                            call.redirect(signUpError.copy(error = "Failed to register."))
                        }
                    }
                }
                call.sessions.set(dataViewSession(newUser.userId))
                call.redirect(dataView())
            }
        }
    }

    get<Signup> {
        val user = call.sessions.get<dataViewSession>()?.let { db.user(it.userId) }
        if (user != null) {
            call.redirect(dataView())
        } else {
            call.respond(FreeMarkerContent("signup.ftl", mapOf("error" to it.error)))
        }
    }
}