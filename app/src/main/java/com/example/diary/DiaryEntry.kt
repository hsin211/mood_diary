package com.example.diary

import org.json.JSONObject

/**
 * 一筆心情日記
 *
 * @param date  時間字串 yyyy/MM/dd HH:mm
 * @param mood  心情表情符號 😊😢😡
 * @param note  備註文字
 */
data class DiaryEntry(
    val date: String,
    val mood: String,
    val note: String
) {

    /** 轉成 JSONObject，方便存進 JSONArray */
    fun toJson(): JSONObject = JSONObject().apply {
        put("date", date)
        put("mood", mood)
        put("note", note)
    }

    /** 取回顯示用的文字（可按需改） */
    fun moodText(): String = when (mood) {
        "😊" -> "開心"
        "😢" -> "難過"
        "😡" -> "生氣"
        else -> "其它"
    }

    companion object {
        /** 從 JSONObject 還原成 DiaryEntry */
        fun fromJson(obj: JSONObject): DiaryEntry = DiaryEntry(
            date = obj.optString("date"),
            mood = obj.optString("mood"),
            note = obj.optString("note")
        )
    }
}