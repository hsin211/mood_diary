package com.example.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class MeditationFragment : Fragment() {

    // 1. 先把所有變數型別都寫清楚
    private lateinit var tvStep: TextView       // 顯示「吸氣…／停留…／吐氣…」
    private lateinit var tvCountdown: TextView  // 顯示剩餘秒數，格式 mm:ss
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private val handler: Handler = Handler(Looper.getMainLooper())

    // 三個步驟的持續時間（秒）
    private val stepDurationsSec: List<Int> = listOf(4, 2, 6)
    // 三個步驟的顯示文字
    private val stepLabels: List<String> = listOf("吸氣", "停留", "吐氣")

    private var stepIndex: Int = 0            // 目前在第幾個步驟 (0~2)
    private var remainingSec: Int = 0         // 倒數剩餘秒數
    private var isBreathing: Boolean = false  // 標記目前是否處於呼吸循環中

    // 2. 把 countdownRunnable 也明確寫成 Runnable 型別
    private val countdownRunnable: Runnable = object : Runnable {
        override fun run() {
            if (!isBreathing) {
                // 已經暫停，就停止一切
                return
            }
            if (remainingSec >= 0) {
                // 計算 mm:ss 格式
                val minutes: Int = remainingSec / 60
                val seconds: Int = remainingSec % 60
                tvCountdown.text = String.format("%02d:%02d", minutes, seconds)

                if (remainingSec > 0) {
                    remainingSec--
                    handler.postDelayed(this, 1000L)
                } else {
                    // 秒數倒完了，就進入下一個步驟
                    breathingRunnable.run()
                }
            }
        }
    }

    // 3. 同樣把 breathingRunnable 明確寫成 Runnable 型別
    private val breathingRunnable: Runnable = object : Runnable {
        override fun run() {
            if (!isBreathing) {
                // 已經暫停，就不繼續下一步
                return
            }

            // 取得當前步驟 index，然後顯示文字、設定剩餘秒數
            val idx: Int = stepIndex % stepLabels.size
            tvStep.text = stepLabels[idx]
            remainingSec = stepDurationsSec[idx]

            // 啟動倒數
            handler.post(countdownRunnable)

            // 下一個步驟的 index
            stepIndex++
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_meditation, container, false)

        tvStep = view.findViewById(R.id.tvStep)
        tvCountdown = view.findViewById(R.id.tvCountdown)
        btnStart = view.findViewById(R.id.btnStart)
        btnPause = view.findViewById(R.id.btnPause)
        btnReset = view.findViewById(R.id.btnReset)

        btnStart.setOnClickListener { startBreathing() }
        btnPause.setOnClickListener { pauseBreathing() }
        btnReset.setOnClickListener { resetBreathing() }

        // 初始顯示
        tvStep.text = "準備中"
        tvCountdown.text = "00:00"

        return view
    }

    /** 開始三步驟呼吸循環 */
    private fun startBreathing() {
        if (isBreathing) return  // 如果已經在跑，就不再重複啟動
        isBreathing = true

        stepIndex = 0
        breathingRunnable.run()  // 直接啟動第一個步驟
    }

    /** 暫停：馬上移除所有排程，秒數立刻停止 */
    private fun pauseBreathing() {
        isBreathing = false
        handler.removeCallbacks(countdownRunnable)
        handler.removeCallbacks(breathingRunnable)
    }

    /** 重置到初始狀態 */
    private fun resetBreathing() {
        isBreathing = false
        handler.removeCallbacks(countdownRunnable)
        handler.removeCallbacks(breathingRunnable)

        stepIndex = 0
        remainingSec = 0

        tvStep.text = "準備中"
        tvCountdown.text = "00:00"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
