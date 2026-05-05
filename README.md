# RainIQ

> *"Every drop, counted."*

**RainIQ** is an Android app that transforms rainwater harvesting from a passive, untracked activity into a data-driven, measurable habit. It helps households log daily rainfall, calculate actual water harvested using a verified engineering formula, visualize savings through an animated water tank, and get AI-powered tips via Google Gemini.

---

## 📱 Screenshots & Features

### 🏠 Dashboard
- Animated water tank that fills up as you log rainfall
- Today's savings and all-time total with live counters
- Impact score — converts liters saved into household water days
- Monthly goal progress bar
- 7-day streak tracker

### 🌧️ Log Rainfall
- Log rainfall in millimetres with date, roof type, and optional notes
- Live harvest preview using the formula: `Area × Rainfall (mm) × 0.0929 × Runoff Coefficient`
- Input validation with helpful error messages
- Optional photo attachment (rain gauge capture)

### 📋 History
- Full log of all rainfall entries
- Search and filter by date range
- Swipe to delete with undo support

### 📈 Monthly Report
- Bar/line charts of monthly rainfall and water collected
- Total savings summary with environmental impact stats

### 🤖 JalBot — AI Assistant
- Powered by Google Gemini API
- Personalized water-saving tips based on your setup
- AI crop/garden planner — suggests what you can grow with your saved water
- Conversational chat interface

### 🌱 Goals & Garden
- Set monthly water collection targets
- Track progress toward your goals
- Garden planner with AI-generated suggestions

### 🏆 Achievements
- Badges and streaks for consistent logging
- Gamified conservation milestones

### 🌤️ Weather
- Live weather forecast via OpenWeatherMap API
- Upcoming rain predictions to help you prepare

### 👤 Profile & Settings
- Roof area, tank capacity, and material setup
- Measurement units, dark mode, rain alerts, daily reminders
- Export history, data privacy controls

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | XML Layouts + Material Design 3 |
| Architecture | MVVM (Model-View-ViewModel) |
| Local Database | Room DB (Jetpack) |
| State Management | LiveData + ViewModel |
| Background Tasks | WorkManager (Jetpack) |
| Charts | MPAndroidChart |
| AI | Google Gemini API |
| Weather | OpenWeatherMap API |
| Min SDK | API 26 (Android 8.0 Oreo) |
| Target SDK | API 35 (Android 15) |

---

## 🚀 Getting Started

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/RainIQ.git
   ```
2. Open in **Android Studio**
3. Add your API keys to `local.properties`:
   ```
   GEMINI_API_KEY=your_gemini_key_here
   WEATHER_API_KEY=your_openweathermap_key_here
   ```
4. Build and run on a device or emulator (Android 8.0+)

---

## 📐 Core Formula

```
Water Harvested (L) = Roof Area (sq ft) × Rainfall (mm) × 0.0929 × Runoff Coefficient
```

**Runoff Coefficients by roof type:**
| Roof Material | Coefficient |
|---------------|-------------|
| Metal / Tin | 0.90 |
| Concrete / RCC | 0.85 |
| Clay Tiles | 0.75 |
| Asphalt | 0.70 |
| Other | 0.65 |

---

## 📁 Project Structure

```
com.rainiq/
├── ui/
│   ├── dashboard/       → Home Dashboard
│   ├── entry/           → Add Rainfall Entry
│   ├── history/         → Rainfall History Log
│   ├── report/          → Monthly Report
│   ├── ai/              → JalBot AI Assistant
│   ├── features/        → Goals, Weather, Achievements, Garden
│   ├── onboarding/      → Splash + Setup screens
│   └── settings/        → Profile & Settings
├── data/
│   ├── db/              → Room DB, DAOs, Entities
│   ├── repository/      → Data repositories
│   └── preferences/     → SharedPreferences helpers
├── domain/
│   ├── models/          → Data models
│   └── calculator/      → Water calculation logic
├── network/
│   ├── gemini/          → Gemini API service
│   └── weather/         → OpenWeatherMap service
└── utils/               → Extensions, constants, formatters
```

---

## 📋 Requirements

- Android 8.0 (API 26) or higher
- Internet connection for AI tips and weather features
- Works fully offline for logging and history

---

*Designed and Developed by Rahul Mirji*
