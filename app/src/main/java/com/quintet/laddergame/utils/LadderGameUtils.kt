package com.quintet.laddergame.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Ladder Game Utils
 *
 * 2024-07-07 Created by sy.lee
 */
object LadderGameUtils {

    private var gson: Gson = GsonBuilder().setLenient().create()

    /**
     * 해당객체를 JSON 형태로 변환한다.
     *
     * @param obj 변환하고자하는 Object
     * @return JSON 형태로 변환된 문자열
     */
    fun convertObjToJSON(obj: Any?): String? {
        return try {
            gson.toJson(obj)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * JSON String 을 T 형태로 변환환다.
     *
     * TypeToken 은 다음과 같이 생성한다.
     * e.g) val type = object: TypeToken<DTO>() {}.type
     *
     * @param jsonString JSON String
     * @param type       TypeToken 으로 생성한 Type
     * @param <T>        변환할 Object 형식
     * @return 변환된 Object
     */
    fun <T> convertJSONToObj(jsonString: String?, type: Type): T? {
        return try {
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            Log.e("[convertJSONToObj]", e.message ?: "")
            null
        }
    }

    /**
     * JsonElement 를 T 형태로 변환한다.
     *
     * @param element               JsonElement
     * @param classOfT              Class
     * @return 변환된 Object
     */
    fun <T> convertJSONToObj(element: JsonElement, classOfT: Class<T>): T? {
        return try {
            gson.fromJson(element, classOfT)
        } catch (e: Exception) {
            Log.e("[convertJSONToObj]", e.message ?: "")
            null
        }
    }
}