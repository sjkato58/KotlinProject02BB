package com.katofuji.kotlinproject02bb.ui.characterselection

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import com.katofuji.kotlinproject02bb.databinding.ItemCharacterSelectionBinding
import com.katofuji.kotlinproject02bb.utils.GlideRequests
import com.katofuji.kotlinproject02bb.utils.setCharacterAvatar

class CharacterSelectionAdapter constructor(
    val glideRequests: GlideRequests,
    val characterClicked: (CharacterModel) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = mutableListOf<CharacterModel>()

    fun updateListContents(
        newList: List<CharacterModel>
    ) {
        list = mutableListOf()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCharacterSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterItemViewHolder(
            binding,
            characterClicked
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CharacterItemViewHolder) {
            holder.bindData(
                list[position],
                glideRequests
            )
        }
    }

    class CharacterItemViewHolder constructor(
        private val binding: ItemCharacterSelectionBinding,
        private val characterClicked: (CharacterModel) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            data: CharacterModel,
            glideRequests: GlideRequests
        ) {
            binding.tvCharacterSelectionItem.text = data.name

            binding.ivCharacterSelectionItem.setCharacterAvatar(
                glideRequests,
                data.img
            )

            binding.cvCharacterSelectionItem.setOnClickListener { characterClicked.invoke(data) }
        }
    }
}