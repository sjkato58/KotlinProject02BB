package com.katofuji.kotlinproject02bb.ui.characterselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katofuji.kotlinproject02bb.databinding.ItemCharacterSelectionBinding
import com.katofuji.kotlinproject02bb.utils.GlideRequests
import com.katofuji.kotlinproject02bb.utils.setCharacterAvatar

class CharacterSelectionAdapter constructor(
    private val glideRequests: GlideRequests,
    private val characterClicked: (CharacterSelectionViewState) -> Unit
) : RecyclerView.Adapter<CharacterSelectionAdapter.CharacterItemViewHolder>() {

    private var list = mutableListOf<CharacterSelectionViewState>()

    fun updateListContents(
        newList: List<CharacterSelectionViewState>
    ) {
        list = mutableListOf()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        val binding = ItemCharacterSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterItemViewHolder(
            binding,
            characterClicked
        )
    }

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        holder.bindData(
            list[position],
            glideRequests
        )
    }

    class CharacterItemViewHolder constructor(
        private val binding: ItemCharacterSelectionBinding,
        private val characterClicked: (CharacterSelectionViewState) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            data: CharacterSelectionViewState,
            glideRequests: GlideRequests
        ) {
            binding.tvCharacterSelectionItem.text = data.name

            binding.ivCharacterSelectionItem.setCharacterAvatar(
                glideRequests,
                data.img
            )

            binding.cvCharacterSelectionItem.setOnClickListener { characterClicked(data) }
        }
    }
}