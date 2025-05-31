package com.example.diary.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val content: String,
    val imagePath: String? = null, // 對應到心情表情的類型
    val moodType: Int,
    val note: CharSequence,
    val mood: String
)