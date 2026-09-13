package com.afterlight.core.security

import java.security.SecureRandom
import java.util.Base64

/**
 * Codec for the shared per-party AES-256 media key.
 * The key itself is random; this only handles generate/encode/decode.
 */
object PartyKeyCodec {
    const val KEY_SIZE_BYTES = 32

    fun generate(): ByteArray {
        val key = ByteArray(KEY_SIZE_BYTES)
        SecureRandom().nextBytes(key)
        return key
    }

    fun encode(key: ByteArray): String {
        require(key.size == KEY_SIZE_BYTES) { "Party media key must be $KEY_SIZE_BYTES bytes" }
        return Base64.getEncoder().encodeToString(key)
    }

    fun decode(encoded: String): ByteArray {
        val key = Base64.getDecoder().decode(encoded)
        require(key.size == KEY_SIZE_BYTES) { "Party media key must be $KEY_SIZE_BYTES bytes" }
        return key
    }
}
