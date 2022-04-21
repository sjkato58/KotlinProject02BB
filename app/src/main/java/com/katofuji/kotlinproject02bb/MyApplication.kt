package com.katofuji.kotlinproject02bb

import android.app.Application
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    var tempHolder: CharacterModel? = null
}