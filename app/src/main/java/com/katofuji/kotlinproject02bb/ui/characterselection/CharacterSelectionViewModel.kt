package com.katofuji.kotlinproject02bb.ui.characterselection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.katofuji.kotlinproject02bb.base.BaseViewModel
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterListDownloadLoadStatus
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterRepository
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import com.katofuji.kotlinproject02bb.data.database.CharacterDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterSelectionViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private val _characterList = MutableLiveData<ApiResponse<List<CharacterModel>>>()
    val characterList: LiveData<ApiResponse<List<CharacterModel>>> get() = _characterList

    var isInitialLoadNeeded = true

    private fun checkIfDownloadAllowed(
        downloadStatus: CharacterListDownloadLoadStatus
    ) = (downloadStatus == CharacterListDownloadLoadStatus.Refresh
            || (downloadStatus == CharacterListDownloadLoadStatus.Initial && isInitialLoadNeeded))

    fun downloadCharacterList(
        downloadStatus: CharacterListDownloadLoadStatus
    ) {
        if (checkIfDownloadAllowed(downloadStatus)) {
            _characterList.postValue(ApiResponse.Loading())
            viewModelScope.launch {
                _characterList.value = characterRepository.downloadCharacterList()
                isInitialLoadNeeded = false
            }
        }
    }

    fun saveCharacterList() {
        viewModelScope.launch {
            (_characterList.value as ApiResponse.Success<List<CharacterModel>>).data?.let {
                characterRepository.saveCharacterListToDatabase(it)
            }
        }
    }

    fun navigateToCharacterDetails(
        data: CharacterModel
    ) {
        val destination = CharacterSelectionFragmentDirections.actionCharacterSelectionFragmentToCharacterDetailsFragment(data.charid)
        navigateInDirection(destination)
    }
}
