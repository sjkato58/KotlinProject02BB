package com.katofuji.kotlinproject02bb.daggerhilt

import android.content.Context
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterExtractor
import com.katofuji.kotlinproject02bb.data.characterlist.CharacterRepository
import com.katofuji.kotlinproject02bb.data.database.CharacterDao
import com.katofuji.kotlinproject02bb.data.database.CharacterDataBase
import com.katofuji.kotlinproject02bb.data.errors.DataErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRequestQueue(
        @ApplicationContext context: Context
    ): RequestQueue = Volley.newRequestQueue(context)

    @Singleton
    @Provides
    fun providesCharacterDatabase(
        @ApplicationContext context: Context
    ): CharacterDataBase = Room.databaseBuilder(
            context,
            CharacterDataBase::class.java,
            "charactermodels.db"
        )
        .build()

    @Singleton
    @Provides
    fun providesCharacterDao(
        characterDataBase: CharacterDataBase
    ): CharacterDao = characterDataBase.getCharacterDao()

    @Singleton
    @Provides
    fun providesCharacterExtractor(): CharacterExtractor = CharacterExtractor()

    @Singleton
    @Provides
    fun providesDataErrorHandler(
        @ApplicationContext context: Context
    ): DataErrorHandler = DataErrorHandler(context)

    @Singleton
    @Provides
    fun providesCharacterRepository(
        requestQueue: RequestQueue,
        characterExtractor: CharacterExtractor,
        dataErrorHandler: DataErrorHandler,
        characterDao: CharacterDao
    ) = CharacterRepository(requestQueue, characterExtractor, dataErrorHandler, characterDao)
}