package com.afterlight.core.security

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PartyKeyCodecTest {

    @Test
    fun generate_produces32ByteKeys() {
        val key = PartyKeyCodec.generate()
        assertEquals(PartyKeyCodec.KEY_SIZE_BYTES, key.size)
    }

    @Test
    fun generate_isUnique() {
        val first = PartyKeyCodec.generate()
        val second = PartyKeyCodec.generate()
        assertNotEquals(first.toList(), second.toList())
    }

    @Test
    fun encodeDecode_roundTrips() {
        val original = PartyKeyCodec.generate()
        val encoded = PartyKeyCodec.encode(original)
        val decoded = PartyKeyCodec.decode(encoded)
        assertArrayEquals(original, decoded)
    }

    @Test(expected = IllegalArgumentException::class)
    fun encode_rejectsWrongLength() {
        PartyKeyCodec.encode(ByteArray(16))
    }

    @Test(expected = IllegalArgumentException::class)
    fun decode_rejectsWrongLength() {
        PartyKeyCodec.decode(java.util.Base64.getEncoder().encodeToString(ByteArray(8)))
    }
}
