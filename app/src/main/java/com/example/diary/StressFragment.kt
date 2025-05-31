package com.example.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val questions = listOf(
        "1.您最近是否經常感到緊張，覺得工作總是做不完？",
        "2.您最近是否老是睡不好，常常失眠或睡眠品質不佳？",
        "3.您最近是否經常有情緒低落、焦慮、煩躁的情況？",
        "4.您最近是否經常忘東忘西、變得很健忘？",
        "5.您最近是否經常覺得胃口不好？或胃口特別好？",
        "6.您最近六個月內是否生病不只一次了？",
        "7.您最近是否經常覺得很累，假日都在睡覺？",
        "8.您最近是否經常覺得頭痛、腰痠背痛？",
        "9.您最近是否經常意見和別人不同？",
        "10.您最近是否注意力經常難以集中？",
        "11.您最近是否經常覺得未來充滿不確定感？恐懼感？",
        "12.有人說您最近氣色不太好嗎？"
    )

    private var yesCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<LinearLayout>(R.id.question_container)
        val inflater = LayoutInflater.from(requireContext())

        val selectedAnswers = mutableMapOf<Int, Boolean?>()
        val radioGroupList = mutableListOf<RadioGroup>() // ✅ 新增記錄 RadioGroup

        questions.forEachIndexed { index, questionText ->
            val cardView = CardView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }
                radius = 16f
                cardElevation = 8f
                setContentPadding(24, 24, 24, 24)
            }

            val innerLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
            }

            val questionTextView = TextView(requireContext()).apply {
                text = questionText
                textSize = 18f
            }

            val radioGroup = RadioGroup(requireContext()).apply {
                orientation = RadioGroup.HORIZONTAL
            }

            val yesButton = RadioButton(requireContext()).apply {
                text = "是"
            }

            val noButton = RadioButton(requireContext()).apply {
                text = "否"
            }

            radioGroup.addView(yesButton)
            radioGroup.addView(noButton)

            // ✅ 記錄這個 RadioGroup
            radioGroupList.add(radioGroup)

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                selectedAnswers[index] = (checkedId == yesButton.id)
            }

            innerLayout.addView(questionTextView)
            innerLayout.addView(radioGroup)
            cardView.addView(innerLayout)
            container.addView(cardView)
        }

        // 提交按鈕（已改為 MaterialButton）
        val submitButton = MaterialButton(ContextThemeWrapper(requireContext(), R.style.CapsuleSubmitButton)).apply {
            text = "提交"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 24
            }

            setOnClickListener {
                // 先檢查是否所有題目都有作答
                val unansweredQuestions = questions.indices.filter { selectedAnswers[it] == null }

                if (unansweredQuestions.isNotEmpty()) {
                    val questionNumbers = unansweredQuestions.joinToString(", ") { (it + 1).toString() }
                    AlertDialog.Builder(requireContext())
                        .setTitle("未完成")
                        .setMessage("請完成所有題目再提交。\n尚未作答的題號：$questionNumbers")
                        .setPositiveButton("確定", null)
                        .show()
                    return@setOnClickListener
                }

                // 若全部題目皆已回答，才繼續處理
                val yesCount = selectedAnswers.values.count { it == true }
                showAdvice(yesCount)

                // 清除所有選項
                radioGroupList.forEach { it.clearCheck() }
                selectedAnswers.clear()
            }
        }

        container.addView(submitButton)
    }

    private fun showAdvice(yesCount: Int) {
        val message = when {
            yesCount <= 2 -> "你的壓力狀況穩定，繼續保持良好生活習慣。"
            yesCount in 3..5 -> "你可能有中度壓力，建議適度休息和調整。"
            else -> "你的壓力指數偏高，建議尋求專業諮詢。"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("心理建議")
            .setMessage("你回答了 $yesCount 題「是」。\n\n$message")
            .setPositiveButton("知道了", null)
            .show()
    }

}