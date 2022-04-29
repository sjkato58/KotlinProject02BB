package com.katofuji.kotlinproject02bb.ui.characterdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.katofuji.kotlinproject02bb.base.BaseFragment
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.databinding.FragmentCharacterDetailsBinding
import com.katofuji.kotlinproject02bb.utils.setCharacterAvatar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailsFragment: BaseFragment<FragmentCharacterDetailsBinding, CharacterDetailsViewModel>(
    FragmentCharacterDetailsBinding::inflate
) {

    override val viewModel: CharacterDetailsViewModel by viewModels()

    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun initViews() {
        binding.ivBackBtnCharacterDetails.setOnClickListener { findNavController().popBackStack() }
    }

    override fun initObservers() {
        viewModel.characterData.observe(viewLifecycleOwner) { uiState ->
            if (uiState.showError) {
                binding.svCharacterDetails.isVisible = false
            } else {
                binding.svCharacterDetails.isVisible = true
                binding.ivAvatarCharacterDetails.setCharacterAvatar(glideRequests, uiState.img)
                binding.tvNameCharacterDetails.text = uiState.name
                binding.tvOccupationCharacterDetails.text = uiState.occupation
                binding.tvNicknameCharacterDetails.text = uiState.nickname
                binding.tvStatusCharacterDetails.text = uiState.status
                binding.tvSeasonAppearanceCharacterDetails.text = uiState.appearance
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val charId = args.characterid

        viewModel.retrieveCharacterData(charId)

        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", charId)
    }
}
