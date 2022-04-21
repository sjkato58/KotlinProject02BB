package com.katofuji.kotlinproject02bb.ui.characterselection

import android.content.res.Configuration
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.katofuji.kotlinproject02bb.ISDEBUG
import com.katofuji.kotlinproject02bb.R
import com.katofuji.kotlinproject02bb.TAG_CORE
import com.katofuji.kotlinproject02bb.base.BaseFragment
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterListDownloadLoadStatus.Initial
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterListDownloadLoadStatus.Refresh
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import com.katofuji.kotlinproject02bb.databinding.FragmentCharacterSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterSelectionFragment: BaseFragment<FragmentCharacterSelectionBinding, CharacterSelectionViewModel>(
    FragmentCharacterSelectionBinding::inflate
) {

    override val viewModel: CharacterSelectionViewModel by viewModels()

    private val characterClicked: (CharacterModel) -> Unit = { characterModel ->
        viewModel.navigateToCharacterDetails(characterModel)
    }

    override fun initViews() {
        binding.srlCharacterSelection.setOnRefreshListener {
            viewModel.downloadCharacterList(Refresh)
        }
        binding.rvCharacterSelection.layoutManager = GridLayoutManager(
            context,
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> 3
                else -> 2
            }
        )
        binding.rvCharacterSelection.adapter = CharacterSelectionAdapter(
            glideRequests,
            characterClicked
        )
    }

    override fun initObservers() {
        viewModel.characterList.observe(viewLifecycleOwner) { response ->
            binding.srlCharacterSelection.isRefreshing = when {
                (response is ApiResponse.Success && !response.data.isNullOrEmpty()) -> {
                    (binding.rvCharacterSelection.adapter as CharacterSelectionAdapter).updateListContents(response.data)
                    viewModel.saveCharacterList()
                    false
                }
                (response is ApiResponse.Error) -> {
                    Snackbar.make(
                        binding.foundationCharacterSelection,
                        when {
                            !response.message.isNullOrEmpty() -> {
                                response.message
                            }
                            else -> resources.getString(R.string.err_download_unknown) + resources.getString(R.string.err_please_try_again_later)
                        },
                        Snackbar.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> {
                    true
                }
            }
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("key")?.observe(
            viewLifecycleOwner) { result ->
                if (ISDEBUG) Log.d(TAG_CORE, "Previously Visited: $result")
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>("key")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadCharacterList(Initial)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
