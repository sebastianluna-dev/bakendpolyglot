package dev.shelfwise.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.shelfwise.domain.Book
import dev.shelfwise.domain.DomainException
import dev.shelfwise.domain.Page
import java.util.Base64

// Top-level functions: Kotlin needs no "Utils" class to hold them.

fun parseId(raw: String): Long =
    raw.takeIf { it.matches(Regex("[0-9]{1,18}")) }?.toLong()?.takeIf { it >= 1 } ?: throw DomainException.invalidId(raw)

fun parseLimit(raw: String?): Int {
    if (raw.isNullOrEmpty()) return 20
    return raw.toIntOrNull()?.takeIf { it in 1..100 }
        ?: throw DomainException.validation("limit", "must be an integer between 1 and 100")
}

val Book.etag get() = "\"book-$id-v$version\""

private val ifMatch = Regex("^\"book-(\\d+)-v(\\d+)\"$")

/** Parses If-Match; a header naming another book never matches (-1). */
fun expectedVersion(header: String?, id: Long): Int? {
    if (header.isNullOrEmpty()) return null
    val m = ifMatch.matchEntire(header.trim()) ?: return -1
    return if (m.groupValues[1] == id.toString()) m.groupValues[2].toIntOrNull() ?: -1 else -1
}

/** Trims the extra row fetched to detect a next page and builds the cursor from the last item. */
fun <T> page(rows: List<T>, limit: Int, cursorOf: (T) -> String): Page<T> =
    if (rows.size <= limit) Page(rows, null, limit)
    else rows.take(limit).let { Page(it, cursorOf(it.last()), limit) }

/** Opaque base64url cursors (Chapter 12). */
fun ObjectMapper.encodeCursor(value: Any): String =
    Base64.getUrlEncoder().withoutPadding().encodeToString(writeValueAsBytes(value))

inline fun <reified T : Any> ObjectMapper.decodeCursor(raw: String, valid: (T) -> Boolean): T =
    runCatching { readValue<T>(Base64.getUrlDecoder().decode(raw)) }.getOrNull()?.takeIf(valid)
        ?: throw DomainException.badCursor()
