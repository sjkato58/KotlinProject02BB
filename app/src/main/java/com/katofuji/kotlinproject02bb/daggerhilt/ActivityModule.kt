package com.katofuji.kotlinproject02bb.daggerhilt

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    /*@ActivityContext
    @Provides
    @Named("TestString3")
    fun provideTestString3(
        @ApplicationContext context: Context,
        @Named("TestString2") testString2: String
    ):String = "${context.getString(R.string.d_teststring)} & $testString2"*/
}