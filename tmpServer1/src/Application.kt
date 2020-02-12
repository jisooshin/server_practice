package com.jslab

import com.fasterxml.jackson.module.kotlin.*
import com.jslab.api.*
import com.jslab.model.*
import com.jslab.repository.*
import com.jslab.webapp.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.net.*
import java.util.concurrent.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> {e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        jackson {
            jacksonObjectMapper()
        }
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    install(Sessions) {
        cookie<dataViewSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    val hashFunction = {s: String -> hash(s)}

    DatabaseFactory.init()

    val db = tmpServerRepository()
    val jwtService = JwtService()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "tmpServer1"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asString()
                val user = db.userById(claimString)
                user
            }
        }
    }

    routing {
        static("/static") {
            resources("images")
        }
        home(db)
        about(db)
        dataView(db, hashFunction)

        signin(db, hashFunction)
        signup(db, hashFunction)
        signout()

        //API
        dataViewApi(db)
        login(db, jwtService)
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) =
    hashFunction("$date:${user.userId}:${request.host()}:${refererHost()}")

fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code &&
            (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }

val ApplicationCall.apiUser get() = authentication.principal<User>()
