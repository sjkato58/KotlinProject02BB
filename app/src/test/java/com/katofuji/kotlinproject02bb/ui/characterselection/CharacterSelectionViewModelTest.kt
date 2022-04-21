package com.katofuji.kotlinproject02bb.ui.characterselection

import androidx.lifecycle.Observer
import com.katofuji.kotlinproject02bb.InstantExecutorExtension
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterListDownloadLoadStatus
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterRepository
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CharacterSelectionViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: CharacterSelectionViewModel

    private val characterRepository = mockk<CharacterRepository>()

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
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = CharacterSelectionViewModel(
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
    fun `when attempting to download the character list then confirm that the repository attempts to download the list`() {
        coEvery {
            characterRepository.downloadCharacterList()
        } returns ApiResponse.Success(exampleCharacterList)

        runTest {
            viewModel.downloadCharacterList(CharacterListDownloadLoadStatus.Initial)
        }

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.downloadCharacterList()
        }
    }

    @Test
    fun `when attempting to download the character list then receives a complete character list`() {
        val slot = slot<ApiResponse<List<CharacterModel>>>()
        val responsesList = mutableListOf<ApiResponse<List<CharacterModel>>>()
        val observer = mockk<Observer<ApiResponse<List<CharacterModel>>>>()
        coEvery {
            characterRepository.downloadCharacterList()
        } returns ApiResponse.Success(exampleCharacterList)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.characterList.observeForever(observer)

        runTest {
            viewModel.downloadCharacterList(CharacterListDownloadLoadStatus.Initial)
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.downloadCharacterList()
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0] is ApiResponse.Loading)
        assertTrue(responsesList[1] is ApiResponse.Success)
        assertTrue((responsesList[1] as ApiResponse.Success).data != null)
        assertTrue((responsesList[1] as ApiResponse.Success).data!![0].name == EXAMPLE_NAME)

        viewModel.characterList.removeObserver(observer)
    }

    @Test
    fun `when attempting to download the character list but an error occurs then the error should be sent back`() {
        val slot = slot<ApiResponse<List<CharacterModel>>>()
        val responsesList = mutableListOf<ApiResponse<List<CharacterModel>>>()
        val observer = mockk<Observer<ApiResponse<List<CharacterModel>>>>()
        coEvery {
            characterRepository.downloadCharacterList()
        } returns ApiResponse.Error(EXAMPLE_ERROR_RESPONSE)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.characterList.observeForever(observer)

        runTest {
            viewModel.downloadCharacterList(CharacterListDownloadLoadStatus.Initial)
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.downloadCharacterList()
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0] is ApiResponse.Loading)
        assertTrue(responsesList[1] is ApiResponse.Error)
        assertFalse((responsesList[1] as ApiResponse.Error).message.isNullOrEmpty())
        assertTrue((responsesList[1] as ApiResponse.Error).message == EXAMPLE_ERROR_RESPONSE)

        viewModel.characterList.removeObserver(observer)
    }

    @Test
    fun `when attempting to save the character list to memory then confirm that the save method is being called once`() {
        coEvery {
            characterRepository.downloadCharacterList()
        } returns ApiResponse.Success(exampleCharacterList)

        runTest {
            viewModel.downloadCharacterList(CharacterListDownloadLoadStatus.Initial)
        }

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.downloadCharacterList()
        }

        coEvery {
            characterRepository.saveCharacterListToDatabase(exampleCharacterList)
        } returns Unit

        runTest {
            viewModel.saveCharacterList()
        }

        coVerify(exactly = VALUE_ONCE) {
            characterRepository.saveCharacterListToDatabase(exampleCharacterList)
        }
    }
}