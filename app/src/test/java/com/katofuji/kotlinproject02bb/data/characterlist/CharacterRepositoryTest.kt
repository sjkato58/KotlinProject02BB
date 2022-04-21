package com.katofuji.kotlinproject02bb.data.characterlist

import com.android.volley.RequestQueue
import com.katofuji.kotlinproject02bb.InstantExecutorExtension
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import com.katofuji.kotlinproject02bb.data.database.CharacterDao
import com.katofuji.kotlinproject02bb.data.errors.DataErrorHandler
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CharacterRepositoryTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    private val requestQueue = mockk<RequestQueue>()
    private val characterExtractor = mockk<CharacterExtractor>()
    private val dataErrorHandler = mockk<DataErrorHandler>()
    private val characterDao = mockk<CharacterDao>()

    lateinit var characterRepository: CharacterRepository

    private val exampleCharacter = CharacterModel(charid = EXAMPLE_CHARID, name = EXAMPLE_NAME, nickname = EXAMPLE_NICKNAME)
    private val exampleCharacter2 = CharacterModel(charid = EXAMPLE_CHARID2, name = EXAMPLE_NAME2, nickname = EXAMPLE_NICKNAME2)
    private val exampleCharacter3 = CharacterModel(charid = EXAMPLE_CHARID3, name = EXAMPLE_NAME3, nickname = EXAMPLE_NICKNAME3)
    private val exampleCharacterList = listOf(exampleCharacter, exampleCharacter2, exampleCharacter3)

    companion object {
        const val VALUE_ONCE = 1
        const val VALUE_THREE_TIMES = 3

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
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        characterRepository = CharacterRepository(
            requestQueue,
            characterExtractor,
            dataErrorHandler,
            characterDao
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when attempting to save a character list then the character dao attempts to save the characters`() {
        coEvery {
            characterDao.upsert(exampleCharacter)
        } returns Unit
        coEvery {
            characterDao.upsert(exampleCharacter2)
        } returns Unit
        coEvery {
            characterDao.upsert(exampleCharacter3)
        } returns Unit

        runTest {
            characterRepository.saveCharacterListToDatabase(
                exampleCharacterList
            )
        }

        coVerify(exactly = VALUE_THREE_TIMES) {
            characterDao.upsert(any())
        }
    }

    @Test
    fun `when attempting to save a character then the character dao attempts to save the character`() {
        coEvery {
            characterDao.upsert(exampleCharacter)
        } returns Unit

        runTest {
            characterRepository.saveCharacterToDatabase(
                exampleCharacter
            )
        }

        coVerify(exactly = VALUE_ONCE) {
            characterDao.upsert(exampleCharacter)
        }
    }
}