package com.katofuji.kotlinproject02bb.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class CharacterDaoTest {

    lateinit var characterDataBase: CharacterDataBase
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

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        characterDataBase = Room.inMemoryDatabaseBuilder(
            context,
            CharacterDataBase::class.java
        ).allowMainThreadQueries().build()
        characterDao = characterDataBase.getCharacterDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        characterDataBase.close()
    }

    @Test
    @Throws(Exception::class)
    fun verifyInsertFunctionWorks() = runBlocking {
        characterDao.insert(exampleCharacter)

        val characterList = characterDao.getAllCharacters()

        assertFalse(characterList.isEmpty())
        assertTrue(characterList[0].charid == exampleCharacter.charid)
        assertTrue(characterList[0].name == exampleCharacter.name)
        assertTrue(characterList[0].nickname == exampleCharacter.nickname)
    }

    @Test
    @Throws(Exception::class)
    fun canObtainCharacterList() = runBlocking {
        exampleCharacterList.forEach { characterModel ->
            characterDao.upsert(characterModel)
        }

        val characterList = characterDao.getAllCharacters()

        assertFalse(characterList.isEmpty())
        assertTrue(characterList[0].charid == exampleCharacter.charid)
        assertTrue(characterList[1].charid == exampleCharacter2.charid)
        assertTrue(characterList[2].charid == exampleCharacter3.charid)
    }

    @Test
    @Throws(Exception::class)
    fun canObtainSpecificCharacter() = runBlocking {
        exampleCharacterList.forEach { characterModel ->
            characterDao.upsert(characterModel)
        }

        val characterModel = characterDao.getCharacterById(exampleCharacter2.charid)

        assertTrue(characterModel != null)
        assertTrue(characterModel!!.charid == exampleCharacter2.charid)
        assertTrue(characterModel.nickname == exampleCharacter2.nickname)
    }

    @Test
    @Throws(Exception::class)
    fun canDeleteIndividualFromCharacterList() = runBlocking {
        exampleCharacterList.forEach { characterModel ->
            characterDao.upsert(characterModel)
        }

        characterDao.delete(exampleCharacter2)

        val characterList = characterDao.getAllCharacters()

        assertFalse(characterList.isEmpty())
        assertTrue(characterList.size == 2)
        assertTrue(characterList[0].charid == exampleCharacter.charid)
        assertTrue(characterList[1].charid == exampleCharacter3.charid)
    }
}