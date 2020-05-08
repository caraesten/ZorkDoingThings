package zorkreader

import models.ZorkData
import java.io.BufferedReader
import java.io.InputStreamReader

private const val FILE_DIRECTORY = "/zork-source"

class ZorkFileReader {
    private val stringRegex = Regex("\"([\\w\\s\\.\\,]{20,})\"", RegexOption.MULTILINE)

    fun readData(): ZorkData {
        val fileName = getSourceFiles().shuffled().first()
        return ZorkData(content = getRandomString(fileName), url = fileName)
    }

    private fun getSourceFiles(): List<String> {
        javaClass.getResourceAsStream(FILE_DIRECTORY).use {
            return it?.let { BufferedReader(InputStreamReader(it)).readLines() } ?: emptyList()
        }
    }

    private fun getRandomString(fileName: String): String {
        return stringRegex.findAll(readStringFromFile(fileName)).toList().shuffled().first().value
    }

    private fun readStringFromFile(fileName: String): String {
        return javaClass.getResource("$FILE_DIRECTORY/$fileName").readText()
    }
}