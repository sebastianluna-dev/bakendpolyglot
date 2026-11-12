package dev.shelfwise.http

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.shelfwise.domain.DomainException
import java.time.Year

/**
 * A JSON request body read field by field, so we can reject unknown fields and report every invalid
 * field at once (Chapter 13). Bean Validation on a DTO can't tell "absent" from "null" in a PATCH.
 */
class Body private constructor(private val fields: Map<String, JsonNode>) {
    private val errors = linkedMapOf<String, MutableList<String>>()

    val size get() = fields.size

    fun has(field: String) = field in fields

    fun fail(field: String, message: String) {
        errors.getOrPut(field) { mutableListOf() } += message
    }

    /** Throws a 422 validation problem if any field failed. */
    fun throwIfInvalid() {
        if (errors.isNotEmpty()) throw DomainException.validation(errors)
    }

    fun str(field: String, required: Boolean, range: IntRange, pattern: Regex? = null, patternMessage: String = ""): String? {
        val node = fields[field] ?: return null.also { if (required) fail(field, "is required") }
        if (!node.isTextual) return null.also { fail(field, "must be a string") }
        val s = node.textValue()
        if (s.codePointCount(0, s.length) !in range) {
            return null.also { fail(field, "must be ${range.first}–${range.last} characters") }
        }
        if (pattern != null && !pattern.matches(s)) return null.also { fail(field, patternMessage) }
        return s
    }

    /** An integer within [range]; nullable fields may be JSON null. */
    fun long(field: String, required: Boolean, nullable: Boolean, range: LongRange, maxMessage: String): Long? {
        val node = fields[field] ?: return null.also { if (required) fail(field, "is required") }
        if (node.isNull) return null.also { if (!nullable) fail(field, "cannot be null") }
        if (!node.isIntegralNumber || !node.canConvertToLong()) return null.also { fail(field, "must be an integer") }
        val n = node.longValue()
        return when {
            n < range.first -> null.also { fail(field, "must be at least ${range.first}") }
            n > range.last -> null.also { fail(field, maxMessage) }
            else -> n
        }
    }

    fun email(field: String): String? {
        val s = str(field, true, 3..254) ?: return null
        return s.takeIf { EMAIL.matches(it) } ?: null.also { fail(field, "must be a valid email address") }
    }

    companion object {
        val ISBN13 = Regex("^97[89][0-9]{10}$")
        private val EMAIL = Regex("^[^@\\s]+@[^@\\s]+$")

        fun thisYear() = Year.now().value.toLong()

        fun read(json: ObjectMapper, raw: String?, vararg allowed: String): Body {
            val root = try {
                raw?.takeIf { it.isNotBlank() }?.let { json.readTree(it) }
            } catch (e: JsonProcessingException) {
                null
            } ?: throw DomainException.invalidBody()

            if (!root.isObject) return Body(emptyMap()).apply { fail("body", "must be a JSON object") }
            val fields = root.properties().associate { (k, v) -> k to v }
            return Body(fields).apply {
                fields.keys.filter { it !in allowed }.forEach { fail(it, "is not allowed") }
            }
        }
    }
}
