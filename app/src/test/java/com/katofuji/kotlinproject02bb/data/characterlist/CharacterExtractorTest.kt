package com.katofuji.kotlinproject02bb.data.characterlist

import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class CharacterExtractorTest {

    lateinit var characterExtractor: CharacterExtractor

    companion object {
        const val testCharacter = "{\"char_id\":1,\"name\":\"Walter White\",\"birthday\":\"09-07-1958\",\"occupation\":[\"High School Chemistry Teacher\",\"Meth King Pin\"],\"img\":\"https://images.amcnetworks.com/amc.com/wp-content/uploads/2015/04/cast_bb_700x1000_walter-white-lg.jpg\",\"status\":\"Presumed dead\",\"nickname\":\"Heisenberg\",\"appearance\":[1,2,3,4,5],\"portrayed\":\"Bryan Cranston\",\"category\":\"Breaking Bad\",\"better_call_saul_appearance\":[]}"
        const val validStringList = "[\"High School Chemistry Teacher\",\"Meth King Pin\"]"
        const val validIntList = "[1,2,3,4,5]"
        const val emptyList = "[]"
    }

    @BeforeEach
    fun setUp() {

        characterExtractor = CharacterExtractor()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when providing the test feed example then a complete character model should return`() {
        val demoFeed = JSONObject(testCharacter)
        val result = characterExtractor.extractIndividualCharacter(demoFeed)

        assertTrue(result != null)
        assertTrue(result!!.name == "Walter White")
        assertTrue(result.charid == 1)
        assertTrue(result.occupation != null)
        assertTrue(result.occupation?.size == 2)
    }

    @Test
    fun `when providing an invalid jsonobject then a null response should be returned`() {
        val result = characterExtractor.extractIndividualCharacter(null)
        assertTrue(result == null)
    }

    @Test
    fun `when providing a valid string list then a list of strings should be returned`() {
        val demoFeed = JSONArray(validStringList)
        val result = characterExtractor.extractStringList(demoFeed)
        assertTrue(!result.isNullOrEmpty())
        assertTrue(result[0] == "High School Chemistry Teacher")
    }

    @Test
    fun `when providing an invalid string list then an empty list should be returned`() {
        val demoFeed = JSONArray(emptyList)
        val result = characterExtractor.extractStringList(demoFeed)
        assertTrue(result.isNullOrEmpty())

        val result2 = characterExtractor.extractStringList(null)
        assertTrue(result2.isNullOrEmpty())
    }

    @Test
    fun `when providing a valid integer list then a list of integers should be returned`() {
        val demoFeed = JSONArray(validIntList)
        val result = characterExtractor.extractIntegerList(demoFeed)
        assertTrue(!result.isNullOrEmpty())
        assertTrue(result[0] == 1)
    }

    @Test
    fun `when providing an invalid integer list then an empty list should be returned`() {
        val demoFeed = JSONArray(emptyList)
        val result = characterExtractor.extractIntegerList(demoFeed)
        assertTrue(result.isNullOrEmpty())

        val result2 = characterExtractor.extractIntegerList(null)
        assertTrue(result2.isNullOrEmpty())
    }
}