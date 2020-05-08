package twitter

import models.ZorkData
import twitter4j.Status
import twitter4j.StatusUpdate
import twitter4j.TwitterFactory

private const val TWEET_LENGTH = 280
private const val LINK_CHARACTER_COUNT = 21

class ZorkTwitterClient {
    val twitter = TwitterFactory.getSingleton()

    fun postTweet(data: ZorkData) {
        if (data.content.length < TWEET_LENGTH - LINK_CHARACTER_COUNT - 1) {
            val status: Status = twitter.updateStatus("${data.content} ${data.url}")
        } else {
            var lastInReplyTo = 0L
            chunkText(data.content).map {
                val status = twitter.updateStatus(StatusUpdate(it).apply {
                    if (lastInReplyTo != 0L) { inReplyToStatusId = lastInReplyTo }
                })
                lastInReplyTo = status.id
            }
            twitter.updateStatus(StatusUpdate(data.url).apply {
                inReplyToStatusId = lastInReplyTo
            })
        }
    }

    private fun chunkText(string: String): List<String> {
        // TODO: do a non-iterative solution
        val list = mutableListOf<String>()
        while (string.isNotEmpty()) {
            val c = getNextChunk(string)
            list += c
            string.replace(c, "")
        }
        return list
    }

    private fun getNextChunk(remainder: String): String {
        return remainder.fold("", { acc, c ->
            val charsToNextSpace = remainder.replace(acc, "").indexOfFirst { it == ' ' }
            return@fold if (acc.length + charsToNextSpace < TWEET_LENGTH) {
                acc + c
            } else {
                acc
            }
        })
    }
}