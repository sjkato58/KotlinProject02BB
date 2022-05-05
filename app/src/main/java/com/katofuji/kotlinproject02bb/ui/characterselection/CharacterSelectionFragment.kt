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
            viewModel::navigateToCharacterDetails
        )
    }

    override fun initObservers() {
        viewModel.characterList.observe(viewLifecycleOwner) { responseList ->
            binding.srlCharacterSelection.isRefreshing = when {
                (responseList[0].isLoading) -> {
                    true
                }
                (responseList[0].showError) -> {
                    Snackbar.make(
                        binding.foundationCharacterSelection,
                        when {
                            !responseList[0].errorMessage.isNullOrEmpty() -> {
                                responseList[0].errorMessage
                            }
                            else -> resources.getString(R.string.err_download_unknown) + resources.getString(R.string.err_please_try_again_later)
                        },
                        Snackbar.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> {
                    (binding.rvCharacterSelection.adapter as CharacterSelectionAdapter).updateListContents(responseList)
                    false
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
