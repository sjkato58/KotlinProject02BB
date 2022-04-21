package com.katofuji.kotlinproject02bb.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharacterDataBaseTest {

    lateinit var characterDao: CharacterDao

    private val exampleCharacter = CharacterModel(charid = EXAMPLE_CHARID, name = EXAMPLE_NAME, nickname = EXAMPLE_NICKNAME)
    private val exampleCharacter2 = CharacterModel(charid = EXAMPLE_CHARID2, name = EXAMPLE_NAME2, nickname = EXAMPLE_NICKNAME2)
    private val exampleCharacter3 = CharacterModel(charid = EXAMPLE_CHARID3, name = EXAMPLE_NAME3, nickname = EXAMPLE_NICKNAME3)
    private val exampleCharacterList = listOf(exampleCharacter, exampleCharacter2, exampleCharacter3)

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_CHARID = 0
        const val EXAMPLE_NAME = "Sid"
        const val EXAMPLE_NICKNAME = "James"
        const val EXAMPLE_CHARID2 = 1
        const val EXAMPLE_NAME2 = "Kenneth"
        const val EXAMPLE_NICKNAME2 = "Williams"
        const val EXAMPLE_CHARID3 = 2
        const val EXAMPLE_NAME3 = "Joan"
        const val EXAMPLE_NICKNAME3 = "Sims"
        const val EXAMPLE_ERROR_RESPONSE = "An unknown error has occurred, please try again later"
    }

    @BeforeEach
    fun setUp() {
        val characterDataBase = Room.inMemoryDatabaseBuilder(
                //InstrumentationRegistry.getInstrumentation().targetContext,
                ApplicationProvider.getApplicationContext(),
            CharacterDataBase::class.java
            )
            .allowMainThreadQueries()
            .build()
        characterDao = characterDataBase.getCharacterDao()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `when then`() {
        characterDao.insert(exampleCharacter)

        val result = characterDao.getCharacterById(exampleCharacter.charid)

        assertTrue(result != null)
        assertTrue(result!!.name == exampleCharacter.name)
    }
}