package com.katofuji.kotlinproject02bb.ui.characterdetails

import androidx.lifecycle.Observer
import com.katofuji.kotlinproject02bb.InstantExecutorExtension
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterRepository
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CharacterDetailsViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: CharacterDetailsViewModel

    private val characterRepository = mockk<CharacterRepository>()

    private val exampleCharacter = CharacterModel()

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_CHARID = 1
        const val EXAMPLE_NAME = "Sid"
        const val EXAMPLE_NICKNAME = "James"
        const val EXAMPLE_ERROR_RESPONSE = "An unknown error has occurred, please try again later"

    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        exampleCharacter.charid = EXAMPLE_CHARID
        exampleCharacter.name = EXAMPLE_NAME
        exampleCharacter.nickname = EXAMPLE_NICKNAME

        viewModel = CharacterDetailsViewModel(
            characterRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when attempting to get character data then repository will attempt to call the correct method once`() {
        coEvery {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        } returns ApiResponse.Success(exampleCharacter)

        runTest {
            viewModel.retrieveCharacterData(EXAMPLE_CHARID)
        }

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        }
    }

    @Test
    fun `when attempting to get character data then will receive the appropriate data from the repository`() {
        val slot = slot<CharacterDetailsViewState>()
        val observer = mockk<Observer<CharacterDetailsViewState>>()
        coEvery {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        } returns ApiResponse.Success(exampleCharacter)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.characterData.observeForever(observer)

        runTest {
            viewModel.retrieveCharacterData(EXAMPLE_CHARID)
        }

        print("This is the slot: ${slot.captured}")

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        }
        assertFalse(slot.captured.name.isEmpty())
        assertTrue(slot.captured.name == EXAMPLE_NAME)

        viewModel.characterData.removeObserver(observer)
    }

    @Test
    fun `when attempting to get character data but an error occurs then an error will be supplied by the repository`() {
        val slot = slot<CharacterDetailsViewState>()
        val observer = mockk<Observer<CharacterDetailsViewState>>()
        coEvery {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        } returns ApiResponse.Error(EXAMPLE_ERROR_RESPONSE)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.characterData.observeForever(observer)

        runTest {
            viewModel.retrieveCharacterData(EXAMPLE_CHARID)
        }

        print("This is the slot: ${slot.captured}")

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.retrieveCharacterFromDatabase(EXAMPLE_CHARID)
        }
        assertTrue(slot.captured.showError)
        assertFalse(slot.captured.errorMessage.isEmpty())
        assertTrue(slot.captured.errorMessage == EXAMPLE_ERROR_RESPONSE)

        viewModel.characterData.removeObserver(observer)
    }
}