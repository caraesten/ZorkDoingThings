package web

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.mustache.Mustache
import io.ktor.mustache.MustacheContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import models.ZorkData
import twitter.ZorkTwitterClient
import zorkreader.ZorkFileReader

private const val PARAM_STRING = "string_data"
private const val PARAM_URL = "url_data"
private const val GITHUB_BASE_URL = "https://github.com/historicalsource/zork-1977-source/blob/master/zork/lcf/"

fun Application.main() {
    val twitterClient = ZorkTwitterClient()

    install(DefaultHeaders)
    install(CallLogging)
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }

    install(Routing) {
        get("/") {
            val dataReader = ZorkFileReader()
            call.respond(MustacheContent("main.html", dataReader.readData()))
        }
        post("/postTweet") {
            val postParameters = call.receiveParameters()
            val zorkData = ZorkData(
                content = postParameters[PARAM_STRING].orEmpty(),
                url = GITHUB_BASE_URL + postParameters[PARAM_URL].orEmpty()
            )
            twitterClient.postTweet(zorkData)
            call.respondRedirect("/")
        }
    }
}
