package com.example

import com.example.data.model.GoldKaratRegistry
import com.example.data.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun goldKaratRegistry_containsAllStandardKarats() {
        val k24 = GoldKaratRegistry.findByNumber("999")
        assertNotNull(k24)
        assertEquals("24K", k24?.karat)
        assertEquals(99.9, k24?.purityPercentage ?: 0.0, 0.01)

        val k22 = GoldKaratRegistry.findByNumber("916")
        assertNotNull(k22)
        assertEquals("22K", k22?.karat)
        assertEquals(91.6, k22?.purityPercentage ?: 0.0, 0.01)

        val k21 = GoldKaratRegistry.findByNumber("875")
        assertNotNull(k21)
        assertEquals("21K", k21?.karat)
        assertEquals(87.5, k21?.purityPercentage ?: 0.0, 0.01)

        val k18 = GoldKaratRegistry.findByNumber("750")
        assertNotNull(k18)
        assertEquals("18K", k18?.karat)
        assertEquals(75.0, k18?.purityPercentage ?: 0.0, 0.01)

        val k14 = GoldKaratRegistry.findByNumber("585")
        assertNotNull(k14)
        assertEquals("14K", k14?.karat)
        assertEquals(58.5, k14?.purityPercentage ?: 0.0, 0.01)

        val k10 = GoldKaratRegistry.findByNumber("417")
        assertNotNull(k10)
        assertEquals("10K", k10?.karat)
        assertEquals(41.7, k10?.purityPercentage ?: 0.0, 0.01)

        val k9 = GoldKaratRegistry.findByNumber("375")
        assertNotNull(k9)
        assertEquals("9K", k9?.karat)
        assertEquals(37.5, k9?.purityPercentage ?: 0.0, 0.01)
    }

    @Test
    fun goldKaratRegistry_findByKaratString() {
        val k21 = GoldKaratRegistry.findByNumber("21K")
        assertNotNull(k21)
        assertEquals("875", k21?.hallmarkNumber)

        val k18 = GoldKaratRegistry.findByNumber("18")
        assertNotNull(k18)
        assertEquals("750", k18?.hallmarkNumber)
    }

    @Test
    fun verificationStatus_containsExpectedStatuses() {
        assertEquals(3, VerificationStatus.values().size)
        assertEquals(VerificationStatus.GREEN, VerificationStatus.valueOf("GREEN"))
        assertEquals(VerificationStatus.YELLOW, VerificationStatus.valueOf("YELLOW"))
        assertEquals(VerificationStatus.RED, VerificationStatus.valueOf("RED"))
    }
}
