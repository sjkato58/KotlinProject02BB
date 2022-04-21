package com.katofuji.kotlinproject02bb.ui.characterdetails

import android.os.Bundle
import android.view.View
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

    val args: CharacterDetailsFragmentArgs by navArgs()

    override fun initViews() {
        binding.ivBackBtnCharacterDetails.setOnClickListener { findNavController().popBackStack() }
    }

    override fun initObservers() {
        viewModel.characterData.observe(viewLifecycleOwner) { response ->
            if (response is ApiResponse.Success && response.data != null) {
                val data = response.data

                binding.ivAvatarCharacterDetails.setCharacterAvatar(
                    glideRequests,
                    data.img
                )

                binding.tvNameCharacterDetails.text = data.name
                data.occupation?.let {
                    binding.tvOccupationCharacterDetails.text = it.joinToString().replace(",", ",\n")
                }
                binding.tvNicknameCharacterDetails.text = data.nickname
                binding.tvStatusCharacterDetails.text = data.status
                data.appearance?.let {
                    binding.tvSeasonAppearanceCharacterDetails.text = it.joinToString()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val charId = args.characterid

        viewModel.retrieveCharacterData(charId)

        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", charId)
    }
}
