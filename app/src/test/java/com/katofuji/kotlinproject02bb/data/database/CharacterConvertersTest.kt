package com.katofuji.kotlinproject02bb.data.database

import com.katofuji.kotlinproject02bb.InstantExecutorExtension
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.json.JSONArray
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantExecutorExtension::class)
class CharacterConvertersTest {

    lateinit var characterConverters: CharacterConverters

    companion object {


    }

    val exampleListString = listOf("High School Chemistry Teacher", "Meth King Pin", "Husband")
    val exampleJSONString = "[\"High School Chemistry Teacher\",\"Meth King Pin\",\"Husband\"]"
    val exampleListInteger = listOf(1, 2, 3)
    val exampleJSONInteger = "[1,2,3]"

    @BeforeEach
    fun setUp() {

        characterConverters = CharacterConverters()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when converting a string list into a json then should return appropriate json array`() {
        val result = characterConverters.stringListToJson(exampleListString)
        println("result1 is: $result")
        println("result2 is: $exampleJSONString")
        assertTrue(result == exampleJSONString)
    }

    @Test
    fun `when converting a json array of strings into a string list then should return the string list`() {
        val result = characterConverters.jsonToStringList(exampleJSONString)
        println("result1 is: $result")
        println("result2 is: $exampleListString")
        assertTrue(result == exampleListString)
    }

    @Test
    fun `when converting a integer list into a json then should return appropriate json array`() {
        val result = characterConverters.integerListToJson(exampleListInteger)
        println("result1 is: $result")
        println("result2 is: $exampleJSONInteger")
        assertTrue(result == exampleJSONInteger)
    }

    @Test
    fun `when converting a json array of integers into a string list then should return the string list`() {
        val result = characterConverters.jsonToIntegerList(exampleJSONInteger)
        println("result1 is: $result")
        println("result2 is: $exampleListInteger")
        assertTrue(result == exampleListInteger)
    }
}