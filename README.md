# RainIQ 🌧️
> **Every drop, counted.**

**RainIQ** is an advanced Android application designed to transform rainwater harvesting from a passive activity into a data-driven, measurable habit. By leveraging verified engineering formulas and AI-powered insights, RainIQ helps households log rainfall, calculate water savings, and optimize their water conservation efforts.

---

## 📱 Screenshots

<p align="center">
  <img src="screenshots/dashboard.jpg" width="30%" alt="Dashboard" />
  <img src="screenshots/setup.jpg" width="30%" alt="Onboarding Setup" />
</p>

---

## ✨ Key Features

### 🏠 Intelligent Dashboard
- **Animated Water Tank**: A visual representation of your harvested water that fills dynamically.
- **Live Statistics**: Real-time tracking of today's savings and all-time totals.
- **Impact Score**: Understand your conservation in practical terms (e.g., "Enough for 3 washing machine cycles").
- **Goal Tracking**: Stay motivated with monthly progress bars and a 7-day streak tracker.

### 🌧️ Precision Logging
- **Engineering-Grade Calculations**: Uses the formula: `Area × Rainfall (mm) × 0.0929 × Runoff Coefficient`.
- **Roof Profiles**: Support for various materials (Metal, Concrete, Clay Tiles, etc.) with verified runoff coefficients.
- **Validation**: Smart input validation to ensure accurate data entry.

### 🤖 JalBot — AI Assistant
- **Powered by Google Gemini**: Personalized conservation tips and garden planning.
- **Crop Planner**: AI-driven suggestions on what to grow based on your harvested water volume.
- **Conversational UI**: Easy-to-use chat interface for all your water-related queries.

### 📈 Reports & Analytics
- **Visual Insights**: Detailed bar and line charts for rainfall and collection trends.
- **Historical Logs**: Comprehensive history with search, filter, and swipe-to-delete functionality.

---

## 🛠️ Technical Specifications

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin |
| **UI Framework** | XML + Material Design 3 (M3) |
| **Architecture** | MVVM + Clean Architecture principles |
| **Database** | Room DB (Persistent Storage) |
| **Concurrency** | Coroutines + Flow |
| **Animations** | Lottie + Custom View Transitions |
| **AI Integration** | Google Generative AI (Gemini Pro) |
| **Charts** | MPAndroidChart |
| **Minimum SDK** | API 26 (Android 8.0) |
| **Target SDK** | API 35 (Android 15) |

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug (2024.2.1) or newer recommended.
- **JDK**: Version 17.
- **Android SDK**: API 35 (Android 15).

### Installation Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Yuvrajbhattb/RainIQ-App.git
   ```

2. **Open Project**:
   - Launch Android Studio.
   - Select **File > Open** and navigate to the cloned `RainIQ` directory.
   - Wait for the **Gradle Sync** to complete.

3. **Configure API Keys**:
   RainIQ requires external API keys for full functionality. Create a `local.properties` file in the root directory (if not already present) and add:
   ```properties
   GEMINI_API_KEY=your_gemini_api_key_here
   WEATHER_API_KEY=your_openweathermap_key_here
   ```
   *Note: You can get a Gemini key from [Google AI Studio](https://aistudio.google.com/) and a Weather key from [OpenWeatherMap](https://openweathermap.org/api).*

4. **Build & Run**:
   - Connect an Android device (API 26+) or launch an Emulator.
   - Click the **'Run'** (Green Play) button in the toolbar or press `Shift + F10`.

---

## 📐 The Science Behind RainIQ

The app calculates water harvest using the standard engineering formula for rooftop rainwater harvesting:

```text
Water Harvested (L) = Roof Area (sq ft) × Rainfall (mm) × 0.0929 × Runoff Coefficient
```

### Runoff Coefficients
| Roof Material | Coefficient |
|---------------|-------------|
| Metal / Tin | 0.90 |
| Concrete / RCC | 0.85 |
| Clay Tiles | 0.75 |
| Asphalt | 0.70 |
| Other | 0.65 |

---

## 📂 Repository Structure

```text
com.rainiq/
├── ui/              # Fragments, ViewModels, and UI Logic
├── data/            # Room DB, Repositories, and Preferences
├── domain/          # Business logic and Math Calculators
├── network/         # Retrofit services for Gemini & Weather
└── utils/           # Extension functions and Constants
```

---

## 🔒 Privacy & Security
- All rainfall data is stored **locally** on your device using Room DB.
- No personal data is uploaded without user consent.
- API keys are managed securely via `local.properties` and are excluded from version control.

---

**Developed with ❤️ by [Yuvraj Bhatt](https://github.com/Yuvrajbhattb)**
