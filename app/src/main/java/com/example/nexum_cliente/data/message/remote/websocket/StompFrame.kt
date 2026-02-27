package com.example.nexum_cliente.data.message.remote.websocket


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/14/2026
 * @version 1.0
 */
data class StompFrame(
    val command: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String = ""
) {
    fun toRawString(): String {
        val sb = StringBuilder()
        sb.append(command).append("\n")
        headers.forEach { (key, value) ->
            sb.append("$key:$value\n")
        }
        sb.append("\n")
        sb.append(body)
        sb.append("\u0000") // NULL terminator
        return sb.toString()
    }

    companion object {
        fun fromRawString(raw: String): StompFrame? {
            val lines = raw.split("\n")
            if (lines.isEmpty()) return null

            val command = lines[0]
            val headers = mutableMapOf<String, String>()
            var bodyStartIndex = 1

            for (i in 1 until lines.size) {
                if (lines[i].isEmpty()) {
                    bodyStartIndex = i + 1
                    break
                }
                val parts = lines[i].split(":", limit = 2)
                if (parts.size == 2) {
                    headers[parts[0].trim()] = parts[1].trim()
                }
            }

            val body = if (bodyStartIndex < lines.size) {
                lines.subList(bodyStartIndex, lines.size)
                    .joinToString("\n")
                    .replace("\u0000", "")
                    .trim()
            } else ""

            return StompFrame(command, headers, body)
        }
    }
}