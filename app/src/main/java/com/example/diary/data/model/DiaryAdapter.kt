package com.example.diary.data.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.DiaryEntry
import com.example.diary.R


class DiaryAdapter(
    private val items: MutableList<DiaryEntry>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Int) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTime: TextView = v.findViewById(R.id.text_date)
        val tvMood: TextView = v.findViewById(R.id.text_mood)
        val tvNote: TextView = v.findViewById(R.id.text_content)  // ← 修正
        val btnDel: ImageButton    = v.findViewById(R.id.btn_delete)
        val btnEdit: ImageButton = v.findViewById(R.id.btn_edit)     // 新增
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = items[position]
        holder.tvTime.text = e.date
        holder.tvMood.text = "${e.mood.emojiToText()} ${e.mood}"
        holder.tvNote.text = e.note
        holder.btnDel.setOnClickListener { onDelete(position) }
        holder.btnEdit.setOnClickListener { onEdit(position) }
        holder.itemView.setOnClickListener { onEdit(position) } // 整行點也可編輯
    }
    fun String.emojiToText(): String = when (this) {
        "😊" -> "開心"; "😢" -> "難過"; "😡" -> "生氣"; else -> "其它"
    }
}