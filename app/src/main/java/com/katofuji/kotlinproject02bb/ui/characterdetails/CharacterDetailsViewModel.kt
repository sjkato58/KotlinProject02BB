package com.katofuji.kotlinproject02bb.ui.characterdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.katofuji.kotlinproject02bb.base.BaseViewModel
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterRepository
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private val _characterData = MutableLiveData<CharacterDetailsViewState>()
    val characterData: LiveData<CharacterDetailsViewState> get() = _characterData

    fun retrieveCharacterData(
        charId: Int,
    ) {
        viewModelScope.launch {
            when (val apiResponse = characterRepository.retrieveCharacterFromDatabase(charId)) {
                is ApiResponse.Success -> publishCharacterDetailsViewState(apiResponse)
                is ApiResponse.Error -> publishCharacterDetailsErrorViewState(apiResponse)
            }
        }
    }

    private fun publishCharacterDetailsErrorViewState(apiResponse: ApiResponse.Error<CharacterModel>) {
        //TODO consider writing better more detail error handling.
        _characterData.value = CharacterDetailsViewState(showError = true, errorMessage = apiResponse.message ?: "")
    }

    private fun publishCharacterDetailsViewState(apiResponse: ApiResponse.Success<CharacterModel>) {
        apiResponse.data?.let {
            _characterData.value = CharacterDetailsViewState(
                name = it.name,
                img = it.img,
                occupation = it.occupation?.joinToString()?.replace(",", ",\n")?: "",
                nickname = it.nickname,
                status = it.status,
                appearance = it.appearance?.joinToString()?: ""
            )
        }
    }
}
