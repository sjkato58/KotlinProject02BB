package com.katofuji.kotlinproject02bb.utils

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Configure CoroutineScope injection for production and testing.
 *
 * @receiver ViewModel provides viewModelScope for production
 * @param coroutineScope null for production, injects TestCoroutineScope for unit tests
 * @return CoroutineScope to launch coroutines on
 */
fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) = coroutineScope ?: this.viewModelScope

fun ImageView.setCharacterAvatar(
    glideRequests: GlideRequests,
    imageUrl: String
) {
    glideRequests
        .load(Uri.parse(imageUrl))
        .into(this)
}