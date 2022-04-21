package com.katofuji.kotlinproject02bb

import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.unmockkAll

import org.junit.Assert.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExtendWith(InstantExecutorExtension::class)
class ExampleUnitTest {

    @BeforeEach
    fun setUp() {

    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `when demoing a test then use this as a base`() {
        val mutableLiveData = MutableLiveData<String>()

        mutableLiveData.postValue("test")

        Assertions.assertEquals("test", mutableLiveData.value)
    }
}