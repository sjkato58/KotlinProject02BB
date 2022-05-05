package com.katofuji.kotlinproject02bb.ui.characterselection

data class CharacterSelectionViewState(
    val charid : Int = -1,
    val name: String = "",
    val img: String = "",
    val appearance: List<Int>? = null,
    val isLoading: Boolean = false,
    val isFilter: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String = "",
)