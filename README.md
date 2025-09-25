# 📝 Diary App

一個以心理健康為核心的 Android 應用程式，提供心情日記、呼吸冥想、壓力量表與緊急支援專線功能，幫助使用者照顧自己的心理狀態。

## 📖 功能介紹
- **歡迎頁面 (WelcomeActivity)**  
  - 進入應用的起始頁，點擊「進入」按鈕即可進入主頁。

- **主頁 (MainActivity)**  
  - 採用 **側邊選單 (Navigation Drawer)** 切換功能模組。
  - 包含以下功能：
    -  **心情日記**：記錄日常心情與想法  
    -  **呼吸冥想**：提供科學化的壓力舒緩技巧 
    -  **壓力指數量表**：自我檢測心理壓力狀態  
    -  **緊急支援專線**：快速取得心理協助管道  

## 🛠️ 技術細節
- **語言**: Kotlin  
- **架構**: Android Fragment + Navigation Drawer  
- **UI 元件**: Material Design (Toolbar, DrawerLayout, NavigationView)  
- **ViewBinding**: 用於安全地操作 UI 元件  

## 專案結構
```bash
app/src/main/java/com/example/diary/ui/
├── MainActivity.kt        # 主頁，包含側邊導覽
├── WelcomeActivity.kt     # 歡迎頁面
├── HotlineFragment.kt     # 緊急支援專線 Fragment
├── DiaryFragment.kt       # 心情日記 Fragment 
├── MeditationFragment.kt  # 呼吸冥想 Fragment 
├── StressFragment.kt      # 壓力量表 Fragment 


### 下載專案
```bash
git clone https://github.com/hsin211/mood_diary.git
cd diary-app
