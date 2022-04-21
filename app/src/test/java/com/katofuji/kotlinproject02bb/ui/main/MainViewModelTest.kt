package com.katofuji.kotlinproject02bb.ui.main

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.katofuji.kotlinproject02bb.InstantExecutorExtension
import com.katofuji.kotlinproject02bb.ui.characterselection.CharacterSelectionFragmentDirections
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class MainViewModelTest {

    lateinit var viewModel: MainViewModel

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_CHARID = 1
        const val EXAMPLE_NAME = "Sid"
        const val EXAMPLE_NICKNAME = "James"
        const val EXAMPLE_ERROR_RESPONSE = "An unknown error has occurred, please try again later"

    }

    @BeforeEach
    fun setUp() {

        viewModel = MainViewModel()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `when attempting to navigate to a direction then the event will be fired off appropriately`() {
        val slot = slot<NavController.() -> Any>()
        val observer = mockk<Observer<NavController.() -> Any>>()
        every {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.navigationEvent.observeForever(observer)

        viewModel.navigateInDirection(CharacterSelectionFragmentDirections.actionCharacterSelectionFragmentToCharacterDetailsFragment(0))

        print("This is the slot: ${slot.captured}")

        assertTrue(slot.captured is NavController.() -> Any)
    }
}