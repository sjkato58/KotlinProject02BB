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
): BaseViewModel() {

    private val _characterData = MutableLiveData<ApiResponse<CharacterModel>>()
    val characterData: LiveData<ApiResponse<CharacterModel>> get() = _characterData

    fun retrieveCharacterData(
        charId: Int
    ) {
        viewModelScope.launch {
            _characterData.value = characterRepository.retrieveCharacterFromDatabase(charId)
        }
    }
}
