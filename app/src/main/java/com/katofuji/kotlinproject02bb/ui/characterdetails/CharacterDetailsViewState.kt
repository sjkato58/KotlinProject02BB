package com.katofuji.kotlinproject02bb.ui.characterdetails

data class CharacterDetailsViewState(
    val name: String = "",
    val img: String = "",
    val occupation: String = "",
    val nickname: String = "",
    val status: String = "",
    val appearance: String = "",
    val showError: Boolean = false,
)