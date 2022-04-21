package com.katofuji.kotlinproject02bb.data.characterlist

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.katofuji.kotlinproject02bb.data.ApiResponse
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import com.katofuji.kotlinproject02bb.data.database.CharacterDao
import com.katofuji.kotlinproject02bb.data.errors.DataErrorHandler
import com.katofuji.kotlinproject02bb.data.errors.ErrorType
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ActivityScoped
class CharacterRepository @Inject constructor(
    private val requestQueue: RequestQueue,
    private val characterExtractor: CharacterExtractor,
    private val dataErrorHandler: DataErrorHandler,
    private val characterDao: CharacterDao,
    private val iODispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun downloadCharacterList(): ApiResponse<List<CharacterModel>> = suspendCoroutine { cont ->
        val url = "https://breakingbadapi.com/api/characters"
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val result = characterExtractor.extractCharacterList(JSONArray(response))
                if (result.isNotEmpty()) {
                    cont.resume(ApiResponse.Success(result))
                } else {
                    cont.resume(ApiResponse.Error(dataErrorHandler.sortDownloadError(ErrorType.UNKNOWN)))
                }
            },
            { error ->
                cont.resume(ApiResponse.Error(dataErrorHandler.sortVolleyError(error), null))
            }
        )
        requestQueue.add(stringRequest)
    }

    suspend fun saveCharacterListToDatabase(
        characterList: List<CharacterModel>
    ) = withContext(iODispatcher) {
        characterList.forEach {
            saveCharacterToDatabase(it)
        }
    }

    suspend fun saveCharacterToDatabase(
        charactermodel: CharacterModel
    ) = withContext(iODispatcher) {
        characterDao.upsert(charactermodel)
    }

    suspend fun retrieveCharacterFromDatabase(
        characterId: Int
    ): ApiResponse<CharacterModel> = withContext(iODispatcher) {
        val character = characterDao.getCharacterById(characterId)
        if (character != null) {
            ApiResponse.Success(character)
        } else {
            ApiResponse.Error(dataErrorHandler.sortDownloadError(ErrorType.UNKNOWN))
        }
    }
}