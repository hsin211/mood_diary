package com.example.diary

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.data.model.DiaryAdapter
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class DiaryFragment : Fragment() {

    private lateinit var etNote: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DiaryAdapter
    private val entries = mutableListOf<DiaryEntry>()
    private var selectedMood = ""

    private val prefs by lazy {
        requireContext().getSharedPreferences("diary_prefs", Context.MODE_PRIVATE)
    }

    companion object { private const val KEY_HISTORY = "history_json" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_diary, container, false)

        etNote = v.findViewById(R.id.etMoodNote)
        recyclerView = v.findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = DiaryAdapter(
            items   = entries,
            onDelete = { idx -> confirmDelete(idx) },
            onEdit   = { idx -> showEditDialog(idx) }
        )
        recyclerView.adapter = adapter

        v.findViewById<Button>(R.id.btnHappy).setOnClickListener { selectedMood = "😊" }
        v.findViewById<Button>(R.id.btnSad).setOnClickListener   { selectedMood = "😢" }
        v.findViewById<Button>(R.id.btnAngry).setOnClickListener { selectedMood = "😡" }
        v.findViewById<Button>(R.id.btnSend).setOnClickListener { addEntry() }

        loadHistory()
        return v
    }

    /** 新增日記 */
    private fun addEntry() {
        val note = etNote.text.toString().trim()
        if (note.isBlank() || selectedMood.isEmpty()) {
            Toast.makeText(requireContext(), "請選心情並輸入內容", Toast.LENGTH_SHORT).show(); return
        }
        val ts = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
        entries.add(0, DiaryEntry(ts, selectedMood, note))
        adapter.notifyItemInserted(0); recyclerView.scrollToPosition(0)
        saveHistory(); etNote.text.clear(); selectedMood = ""
    }

    /** 刪除日記 */
    private fun removeEntry(idx: Int) {
        entries.removeAt(idx)
        adapter.notifyItemRemoved(idx)
        saveHistory()
    }
    private fun confirmDelete(idx: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("確定要刪除嗎？")
            .setPositiveButton("是") { _, _ -> removeEntry(idx) }
            .setNegativeButton("否", null)
            .show()
    }


    /** 編輯日記對話框 */
    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(idx: Int) {
        val e = entries[idx]
        val dlgView = layoutInflater.inflate(R.layout.dialog_edit_diary, null)
        val et = dlgView.findViewById<TextInputEditText>(R.id.etEditNote)
        val tv = dlgView.findViewById<TextView>(R.id.tvEditMood)
        et.setText(e.note)
        tv.text = e.mood

        // 正確綁定三顆按鈕
        dlgView.findViewById<Button>(R.id.btnHappyEdit).setOnClickListener { tv.text = "😊" }
        dlgView.findViewById<Button>(R.id.btnSadEdit).setOnClickListener   { tv.text = "😢" }
        dlgView.findViewById<Button>(R.id.btnAngryEdit).setOnClickListener { tv.text = "😡" }

        AlertDialog.Builder(requireContext())
            .setTitle("編輯日記")
            .setView(dlgView)
            .setPositiveButton("儲存") { _, _ ->
                entries[idx] = e.copy(
                    mood = tv.text.toString(),
                    note = et.text.toString()
                )
                adapter.notifyItemChanged(idx)
                saveHistory()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /** 存歷史 */
    private fun saveHistory() {
        val ja = JSONArray().apply { entries.forEach { put(it.toJson()) } }
        prefs.edit().putString(KEY_HISTORY, ja.toString()).apply()
    }

    /** 讀歷史 */
    private fun loadHistory() {
        if (prefs.all[KEY_HISTORY] is Int) prefs.edit().remove(KEY_HISTORY).apply()

        entries.clear()
        try {
            JSONArray(prefs.getString(KEY_HISTORY, "[]")).let { arr ->
                for (i in 0 until arr.length())
                    entries.add(DiaryEntry.fromJson(arr.getJSONObject(i)))
            }
        } catch (_: JSONException) { prefs.edit().remove(KEY_HISTORY).apply() }
        adapter.notifyDataSetChanged()

    }
}
