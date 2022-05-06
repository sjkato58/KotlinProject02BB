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
import com.katofuji.kotlinproject02bb.ui.characterdetails.CharacterDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterSelectionViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseViewModel() {

    private val _characterList = MutableLiveData<List<CharacterSelectionViewState>>()
    val characterList: LiveData<List<CharacterSelectionViewState>> get() = _characterList

    var isInitialLoadNeeded = true

    private fun checkIfDownloadAllowed(
        downloadStatus: CharacterListDownloadLoadStatus
    ) = (downloadStatus == CharacterListDownloadLoadStatus.Refresh
            || (downloadStatus == CharacterListDownloadLoadStatus.Initial && isInitialLoadNeeded))

    fun downloadCharacterList(
        downloadStatus: CharacterListDownloadLoadStatus
    ) {
        if (checkIfDownloadAllowed(downloadStatus)) {
            _characterList.postValue(listOf(CharacterSelectionViewState(isLoading = true)))
            viewModelScope.launch {
                when (val apiResponse = characterRepository.downloadCharacterList()) {
                    is ApiResponse.Success -> {
                        apiResponse.data?.let { responseList ->
                            publishCharacterSelectionViewState(responseList)
                            saveCharacterList(responseList)
                        }
                    }
                    is ApiResponse.Error -> publishCharacterSelectionErrorViewState(apiResponse)
                }
                isInitialLoadNeeded = false
            }
        }
    }

    private fun publishCharacterSelectionViewState(responseList: List<CharacterModel>) {
        _characterList.value = responseList.map {
            CharacterSelectionViewState(
                charid = it.charid,
                name = it.name,
                img = it.img,
                appearance = it.appearance
            )
        }
    }

    private fun publishCharacterSelectionErrorViewState(apiResponse: ApiResponse.Error<List<CharacterModel>>) {
        _characterList.value = listOf(CharacterSelectionViewState(showError = true, errorMessage = apiResponse.message ?: ""))
    }

    private fun saveCharacterList(responseList: List<CharacterModel>) {
        viewModelScope.launch {
            characterRepository.saveCharacterListToDatabase(responseList)
        }
    }

    fun navigateToCharacterDetails(
        data: CharacterSelectionViewState
    ) {
        val destination = CharacterSelectionFragmentDirections.actionCharacterSelectionFragmentToCharacterDetailsFragment(data.charid)
        navigateInDirection(destination)
    }
}
