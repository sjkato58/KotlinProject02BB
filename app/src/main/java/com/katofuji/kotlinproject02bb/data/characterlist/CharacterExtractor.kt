package com.katofuji.kotlinproject02bb.data.characterlist

import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel
import org.json.JSONArray
import org.json.JSONObject

class CharacterExtractor {

    fun extractCharacterList(
        resultJson: JSONArray?
    ): List<CharacterModel> {
        val characterList = mutableListOf<CharacterModel>()
        resultJson?.let { jsonArray ->
            val length = jsonArray.length()
            for (i in 0 until length) {
                extractIndividualCharacter(jsonArray.optJSONObject(i))?.let {
                    characterList.add(it)
                }
            }
        }
        return characterList
    }

    fun extractIndividualCharacter(
        jsonObject: JSONObject?
    ): CharacterModel? = jsonObject?.let { characterJSON ->
        val characterModel = CharacterModel()
        characterModel.charid = characterJSON.optInt(CHARACTER_CHAR_ID)
        characterModel.name = characterJSON.optString(CHARACTER_NAME)
        characterModel.birthday = characterJSON.optString(CHARACTER_BIRTHDAY)
        characterModel.occupation = extractStringList(characterJSON.optJSONArray(
            CHARACTER_OCCUPATION
        ))
        characterModel.img = characterJSON.optString(CHARACTER_IMAGE)
        characterModel.status = characterJSON.optString(CHARACTER_STATUS)
        characterModel.nickname = characterJSON.optString(CHARACTER_NICKNAME)
        characterModel.appearance = extractIntegerList(characterJSON.optJSONArray(
            CHARACTER_APPEARANCE
        ))
        characterModel.portrayed = characterJSON.optString(CHARACTER_PORTRAYED)
        characterModel.category = characterJSON.optString(CHARACTER_CATEGORY)
        characterModel.bettercallsaulappearance = extractIntegerList(characterJSON.optJSONArray(
            CHARACTER_BETTER_CALL_SAUL_APPEARANCE
        ))
        characterModel
    }

    fun extractStringList(
        resultJson: JSONArray?
    ): List<String> {
        val stringList = mutableListOf<String>()
        resultJson?.let { jsonArray ->
            val length = jsonArray.length()
            for (i in 0 until length) {
                stringList.add(jsonArray.optString(i))
            }
        }
        return stringList
    }

    fun extractIntegerList(
        resultJson: JSONArray?
    ): List<Int> {
        val integerList = mutableListOf<Int>()
        resultJson?.let { jsonArray ->
            val length = jsonArray.length()
            for (i in 0 until length) {
                integerList.add(jsonArray.optInt(i))
            }
        }
        return integerList
    }
}
