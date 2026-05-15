# RainIQ Tracker — Product Requirements Document (PRD)

**Version:** 1.0  
**Date:** April 25, 2026  
**Program:** MindMatrix VTU Internship Program — Project #86  
**Category:** Android App Development using GenAI (Natural Resources)  
**Platform:** Android (Minimum SDK 26 / Android 8.0+)  
**Language:** Kotlin  
**Developer:** Internship Student, HKBK College of Engineering  

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Problem Statement](#2-problem-statement)
3. [Vision & Goal](#3-vision--goal)
4. [Target Users](#4-target-users)
5. [App Name & Branding](#5-app-name--branding)
6. [Technical Architecture](#6-technical-architecture)
7. [Success Criteria](#7-success-criteria)
8. [Screen-by-Screen Specification](#8-screen-by-screen-specification)
   - Screen 1: Splash & Onboarding Setup
   - Screen 2: Home Dashboard
   - Screen 3: Add Rainfall Entry
   - Screen 4: Rainfall History Log
   - Screen 5: Monthly Report
   - Screen 6: AI Tips (Gemini)
   - Screen 7: Goals & Targets
   - Screen 8: Environmental Impact
   - Screen 9: Achievements & Badges
   - Screen 10: Weather Forecast
   - Screen 11: AI Crop/Garden Planner
   - Screen 12: Share & Export Report
   - Screen 13: Notifications & Reminders
   - Screen 14: Settings
9. [Navigation Flow](#9-navigation-flow)
10. [Database Schema](#10-database-schema)
11. [API Integrations](#11-api-integrations)
12. [Core Formula & Logic](#12-core-formula--logic)
13. [Non-Functional Requirements](#13-non-functional-requirements)
14. [Build Timeline](#14-build-timeline)
15. [Out of Scope](#15-out-of-scope)

---

## 1. Executive Summary

RainIQ Tracker is an Android application that transforms rainwater harvesting from a passive, untracked activity into a data-driven, measurable habit. The app allows households to log daily rainfall, calculates actual water harvested using a verified engineering formula, visualizes savings through an animated water tank, and uses Google Gemini AI to generate personalized water-saving tips and garden planning suggestions. The app targets rural and semi-urban Indian households that practice or want to practice rainwater harvesting.

---

## 2. Problem Statement

Many Indian households have rainwater harvesting infrastructure — rooftop gutters, storage tanks, sump pits — but have no way to quantify how effective their system is. Without data, conservation feels intangible, motivation decreases, and the full potential of the existing infrastructure is never realized. Citizens cannot answer basic questions like:

- "How many liters did I collect last monsoon?"
- "Is my roof size sufficient for my family's needs?"
- "What can I grow in my garden with the water I've saved?"

The absence of a simple, localized tracking tool means conservation remains aspirational rather than actionable.

---

## 3. Vision & Goal

**Vision:** Turn every Indian household's rooftop into a measurable, quantified water asset.

**Primary Goal:** Help users understand their water harvesting potential and track actual savings against that potential.

**Secondary Goal:** Use GenAI to make water conservation feel personal and rewarding, not like a chore.

**Internship Evaluation Goal:** Demonstrate proficiency in Android development (Room DB, RecyclerView, animations, WorkManager) combined with GenAI API integration (Gemini).

---

## 4. Target Users

| User Type | Description | Primary Need |
|-----------|-------------|--------------|
| **Primary** | Urban/semi-urban homeowner with existing rainwater harvesting setup | Track how much water they're actually saving |
| **Secondary** | Apartment resident curious about potential savings | Understand if rainwater harvesting is worth installing |
| **Tertiary** | Student / environmentally conscious user | Gamified conservation tracking |

**User Geography:** India (Karnataka focus, English UI with local context)  
**Device Target:** Mid-range Android (4GB RAM, Android 8.0+)  
**Connectivity:** Must work offline; AI features need internet  

---

## 5. App Name & Branding

| Property | Value |
|----------|-------|
| **App Name** | RainIQ Tracker |
| **Meaning** | *Jal* = Water (Hindi/Kannada), *Sanchay* = Collection/Savings |
| **Tagline** | *"Every drop, counted."* |
| **Primary Color** | Deep Teal `#01696F` (Water/Nature) |
| **Secondary Color** | Sky Blue `#4A90D9` (Rain) |
| **Accent Color** | Leaf Green `#437A22` (Nature/Growth) |
| **Background** | Off-white `#F7F6F2` |
| **Font** | Roboto (Body), Roboto Slab (Headings) |
| **App Icon** | Water drop falling into a tank outline |
| **Theme** | Material Design 3 with custom teal palette |

---

## 6. Technical Architecture

### Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin |
| **UI Framework** | XML Layouts + Material Design 3 |
| **Architecture Pattern** | MVVM (Model-View-ViewModel) |
| **Local Database** | Room DB (Jetpack) |
| **State Management** | LiveData + ViewModel |
| **Background Tasks** | WorkManager (Jetpack) |
| **Charts** | MPAndroidChart (open-source) |
| **AI Integration** | Google Gemini API (Free Tier) |
| **Weather API** | OpenWeatherMap API (Free Tier) |
| **Preferences** | SharedPreferences (user setup data) |
| **Minimum SDK** | API 26 (Android 8.0 Oreo) |
| **Target SDK** | API 35 (Android 15) |
| **Build Tool** | Gradle (Kotlin DSL) |
| **IDE** | Android Studio |

### Project Structure

```
com.jalasanchay.tracker/
├── ui/
│   ├── onboarding/       → Splash + Setup screens
│   ├── dashboard/        → Home Dashboard
│   ├── entry/            → Add Rainfall Entry
│   ├── history/          → Rainfall History Log
│   ├── report/           → Monthly Report
│   ├── ai/               → AI Tips + Crop Planner
│   ├── goals/            → Goals & Targets
│   ├── impact/           → Environmental Impact
│   ├── achievements/     → Badges screen
│   ├── weather/          → Weather Forecast
│   ├── share/            → Export Report
│   └── settings/         → Settings + Reminders
├── data/
│   ├── db/               → Room DB, DAOs, Entities
│   ├── repository/       → Data repositories
│   └── preferences/      → SharedPreferences helpers
├── domain/
│   ├── models/           → Data models
│   └── calculator/       → Water calculation logic
├── network/
│   ├── gemini/           → Gemini API service
│   └── weather/          → OpenWeatherMap service
└── utils/                → Extensions, constants, formatters
```

---

## 7. Success Criteria

The following are the mandatory success criteria from the project brief that must be met:

| # | Criterion | Implementation |
|---|-----------|---------------|
| 1 | Water Tank visual must fill up relative to data entered | Animated ProgressBar on Dashboard (Screen 2) |
| 2 | App must provide a monthly report of total water saved | Monthly Report screen with MPAndroidChart (Screen 5) |
| 3 | Math calculations must be validated (non-numeric inputs handled gracefully) | Input validation on Add Entry screen (Screen 3) |
| 4 | Project must include a Tips section for better water harvesting | AI Tips screen powered by Gemini (Screen 6) |

---

## 8. Screen-by-Screen Specification

---

### Screen 1: Splash & Onboarding Setup

**Type:** One-time setup (shown only on first launch)  
**Navigation trigger:** App first install / SharedPreferences flag not set  

#### Splash Sub-Screen (2 seconds)
- Full-screen teal background
- App logo (water drop animation — droplet falls into a tank)
- App name "RainIQ Tracker" in white Roboto Slab
- Tagline: *"Every drop, counted."*
- Auto-navigates to Onboarding Step 1 after 2 seconds

#### Onboarding Step 1 — Welcome
- Illustration: Animated rain falling on a house rooftop
- Heading: "Track your rainwater savings"
- Body: Brief 2-line description of what the app does
- "Get Started" button → navigates to Setup

#### Onboarding Step 2 — Roof Setup
- **Field 1:** Roof Area (sq ft) — NumberDecimal input
- **Field 2:** Tank Capacity (liters) — NumberDecimal input
- **Field 3:** Roof Material — Dropdown spinner
  - Options: Concrete/RCC, Clay Tiles, Metal/Tin, Asphalt, Other
  - Each option auto-sets the Runoff Coefficient (editable)
- **Field 4:** City/Location — Text input (used for Weather API)
- Validation: All fields mandatory, non-numeric input shows red error text
- "Save & Continue" button → saves to SharedPreferences, navigates to Dashboard

#### Data Stored (SharedPreferences)
```
KEY_ROOF_AREA          → Float
KEY_TANK_CAPACITY      → Float
KEY_ROOF_MATERIAL      → String
KEY_RUNOFF_COEFFICIENT → Float
KEY_CITY               → String
KEY_ONBOARDING_DONE    → Boolean (set to true after this screen)
```

---

### Screen 2: Home Dashboard

**Type:** Main screen (entry point after first launch)  
**Navigation:** Bottom Navigation Bar tab 1  

#### Top App Bar
- App logo (small) + "RainIQ" title
- Settings icon (top-right) → navigates to Settings (Screen 14)
- Notification bell icon → navigates to Notifications settings

#### Section A — Animated Water Tank
- Large animated vertical ProgressBar styled as a water tank
- Tank outline drawn using a custom drawable XML
- Water level fills from bottom to top as percentage of tank capacity
  - Formula: `(Total Liters Saved / Tank Capacity) × 100`
- Animated: When new entry is added, water level smoothly rises (300ms animation)
- Color gradient: Light blue (empty) → Deep teal (full)
- Tank label shows: `"XXX L / YYY L"` (current / capacity)
- Rain drop animation plays when water level increases

#### Section B — Today's Stats Cards (Horizontal ScrollView with 2 CardViews)
- **Card 1:** "Saved Today"
  - Large animated number counter showing liters saved today
  - Sub-label: "Liters harvested"
  - Teal icon: droplet
- **Card 2:** "Total Savings"
  - All-time total liters saved (animated counter on load)
  - Sub-label: "Since you started"
  - Green icon: chart upward

#### Section C — Impact Score
- Single card spanning full width
- Converts total liters into household water days
  - Formula: `Total Liters / 135` (135L = average Indian household per day per person, WHO standard)
- Display: "You've powered **X household days** of water 💧"
- Tappable → navigates to Environmental Impact screen (Screen 8)

#### Section D — Goal Progress (if goal set)
- Horizontal ProgressBar showing monthly goal progress
- Label: "Monthly Goal: XXX L / YYY L"
- Percentage shown on right
- "Set Goal" text link if no goal is set → navigates to Goals screen (Screen 7)

#### Section E — Quick Streak
- 7 small circle indicators for the last 7 days
- Filled teal circle = logged that day, empty = missed
- Label: "🔥 X day streak"

#### Floating Action Button (FAB)
- Position: Bottom-right
- Icon: `+` (add)
- Color: Teal
- Tap → navigates to Add Rainfall Entry (Screen 3)

#### Bottom Navigation Bar
- Tab 1: Home (Dashboard) — house icon
- Tab 2: History — list icon
- Tab 3: Report — bar chart icon
- Tab 4: AI Tips — sparkle/star icon
- Tab 5: More — grid icon (opens: Goals, Impact, Achievements, Weather, Settings)

---

### Screen 3: Add Rainfall Entry

**Type:** Data entry modal / full screen  
**Navigation trigger:** FAB on Dashboard, or "+" in History screen  

#### Top App Bar
- Back arrow (←) — returns to Dashboard without saving
- Title: "Log Rainfall"
- "Save" text button (top-right) — validates and saves

#### Form Fields

**Field 1: Date**
- Material DatePicker — defaults to today's date
- Tappable calendar icon opens DatePickerDialog
- Cannot select future dates (validation enforced)
- Display format: `DD MMM YYYY` (e.g., "25 Apr 2026")

**Field 2: Rainfall Amount (mm)**
- Label: "Rainfall (in mm)"
- Input type: NumberDecimal
- Hint: "Enter rainfall in millimetres"
- Validation:
  - Empty → "Please enter rainfall amount"
  - Non-numeric → "Only numbers allowed"
  - Zero or negative → "Value must be greater than 0"
  - Greater than 500 → Warning dialog "That's unusually high. Are you sure?"
- Helper text: "Check your local weather station or rain gauge"

**Field 3: Runoff Coefficient**
- Label: "Roof Type"
- Pre-filled from onboarding setup (editable)
- Dropdown spinner with coefficient shown next to each option:
  - Concrete/RCC — 0.85
  - Clay Tiles — 0.75
  - Metal/Tin — 0.90
  - Asphalt — 0.70
  - Other — 0.65 (manual override allowed)

**Field 4: Notes (Optional)**
- Label: "Notes (optional)"
- Multi-line text input
- Hint: "e.g., Heavy storm, partial collection..."
- Max 200 characters, character counter shown

#### Live Preview Card
- Updates in real-time as user types
- Shows: **"Estimated harvest: XXX.X liters"**
- Formula shown below in small text: `Area × Rainfall × 0.0929 × Coefficient`
- If fields incomplete → shows "Fill all fields to see estimate"
- Background: Light teal tint to make it visually distinct

#### Photo Attachment (Optional)
- Camera icon button at bottom
- Opens camera intent to capture rain gauge photo
- Photo stored locally (internal storage, not gallery)
- Thumbnail preview shown after capture
- "Remove photo" option if photo attached

#### Save Logic
- Validates all required fields
- On success: saves to Room DB, shows Snackbar "Entry saved! +XXX L added 💧"
- Returns to Dashboard, triggers water tank animation
- On failure: highlights error fields with red borders + error messages

---

### Screen 4: Rainfall History Log

**Type:** List screen  
**Navigation:** Bottom Navigation Bar tab 2  

#### Top App Bar
- Title: "History"
- Filter icon (top-right) → opens filter bottom sheet
- Search icon → enables search by date

#### Filter Bottom Sheet
- Filter by: This Week / This Month / Last 3 Months / All Time / Custom Range
- Sort by: Newest First / Oldest First / Highest Rainfall / Highest Harvest
- "Apply" button

#### RecyclerView — Entry Cards
Each entry card shows:
- **Left:** Date (large, bold) — `"25 Apr"` with year below
- **Center:** Rainfall amount `"XX mm"` and harvested amount `"XXX L"`
- **Right:** Small teal droplet icon
- **Bottom row:** Notes text (if any), photo thumbnail (if any)
- **Swipe left to delete:** Shows red delete background with trash icon + undo Snackbar (5 sec timer)
- **Tap to expand:** Opens detail bottom sheet with full info + edit option

#### Empty State
- Illustration: Empty rain gauge
- Text: "No entries yet"
- Sub-text: "Tap the + button on the dashboard to log your first rainfall"
- CTA Button: "Log First Entry" → navigates to Add Entry (Screen 3)

#### Summary Footer (sticky at bottom)
- Total entries count
- Total rainfall (mm) across filtered range
- Total harvest (liters) across filtered range

---

### Screen 5: Monthly Report

**Type:** Report/analytics screen  
**Navigation:** Bottom Navigation Bar tab 3  

#### Top App Bar
- Title: "Monthly Report"
- Share icon (top-right) → navigates to Share screen (Screen 12)
- Month selector arrows (← April 2026 →) to navigate between months

#### Section A — Month Summary Cards (2x2 grid)
- **Card 1:** Total Rain Logged (mm) this month
- **Card 2:** Total Water Harvested (L) this month
- **Card 3:** Best Single Day (highest harvest day)
- **Card 4:** Days Logged (out of days in month)

#### Section B — Bar Chart (MPAndroidChart)
- X-axis: Days of the month (1–31)
- Y-axis: Liters harvested
- Bar color: Teal gradient
- Tapping a bar shows tooltip: "Apr 12 — 45mm rain — 210L harvested"
- Toggle button: Switch between "Daily View" and "Monthly Trend" (shows last 6 months as bars)

#### Section C — Line Chart (toggle from bar chart)
- Shows cumulative savings over the month
- Line color: Teal
- Shaded area below line
- Milestone markers at 100L, 250L, 500L, 1000L

#### Section D — Monthly Insight Card
- Auto-generated text insight:
  - "This was your best month! You harvested 23% more than last month 📈"
  - "You missed 8 logging days. Try to log daily for better accuracy."
- Background: Light teal with quote icon

#### Section E — Yearly Summary (collapsible)
- 12-month bar chart showing total harvest per month
- Best month highlighted in darker teal
- Total year-to-date savings

---

### Screen 6: AI Tips (Gemini Integration)

**Type:** AI-powered content screen  
**Navigation:** Bottom Navigation Bar tab 4  

#### Top App Bar
- Title: "Water Wisdom"
- Gemini logo icon (small) + "Powered by Gemini" subtitle

#### Section A — Static Tips (Always Available / Offline)
- Curated list of 10 pre-loaded water harvesting tips stored locally in `strings.xml`
- Each tip shown as a Card with:
  - Icon (bucket, roof, garden, etc.)
  - Bold tip title
  - 2-line explanation
  - "Did you know?" badge on selected tips
- Horizontally scrollable chip filters: All / Roof Care / Storage / Monsoon / Dry Season

#### Section B — AI-Powered Personalized Tips
- Header: "Your Personalized Tips 🤖"
- Sub-text: "Based on your roof size and location"
- **"Get AI Tips" Button** (teal, full-width)
  - On tap: Shows loading shimmer animation
  - Calls Gemini API with context:
    ```
    Prompt: "I have a [X sq ft] [roof type] rooftop in [City], India.
    My rainwater tank capacity is [Y liters].
    I have collected [Z liters] total so far.
    Give me 5 specific, practical tips to improve my rainwater harvesting.
    Format as numbered list. Keep each tip under 50 words."
    ```
  - Response displayed as numbered tip cards with teal left accent
  - "Regenerate" icon button to get fresh tips
  - Error state: "Couldn't load AI tips. Check your internet connection." with retry button

#### Section C — Ask Jal-Bot (Conversational)
- Collapsible section, tap to expand
- Simple chat-style input field at bottom
- Pre-set question chips: "How to clean my tank?", "Best month to harvest?", "How much can I save yearly?"
- User types question → Gemini responds in chat bubble
- Limited to 5 questions per session (free tier consideration)
- Conversation history cleared on app restart (no persistent chat storage)

---

### Screen 7: Goals & Targets

**Type:** Goal-setting screen  
**Navigation:** More tab → Goals  

#### Top App Bar
- Title: "My Goals"
- Back arrow

#### Section A — Monthly Water Goal
- Large circular ProgressBar showing current month progress vs. goal
- Center text: `"XXX L / YYY L"`
- Percentage below: `"67% achieved"`
- Edit Goal button (pencil icon) → opens dialog to set new goal in liters

#### Section B — Set Goal Dialog
- Input: Target liters for the month
- Helper: "Recommended for your roof size: ~XXX L/month" (calculated from area × avg rainfall)
- Save / Cancel buttons

#### Section C — Goal History
- Past months list: Month | Goal | Achieved | Status (✅ Met / ❌ Missed / 🔄 In Progress)
- Color coding: Green for met, red for missed

#### Section D — Streak Goal
- Toggle: Enable "Log every day" streak goal
- Shows current streak: 🔥 X days
- Best streak ever shown below

---

### Screen 8: Environmental Impact

**Type:** Read-only impact visualization  
**Navigation:** Tap Impact Score on Dashboard, or More tab → Impact  

#### Top App Bar
- Title: "Your Impact"
- Back arrow

#### Hero Card
- Large animated number: "You've saved **XXX,XXX liters** of water"
- Confetti particle animation plays on first visit

#### Impact Conversion Cards (Grid layout)

| Card | Icon | Formula | Display Example |
|------|------|---------|-----------------|
| Household Days | 🏠 | Total L ÷ 135 | "Powered 47 household days" |
| Borewell Trips | 💧 | Total L ÷ 20 | "Saved 230 borewell trips" |
| Trees Supported | 🌳 | Total L ÷ 75 | "Supported 18 trees for a week" |
| Tanker Savings | 🚛 | Total L ÷ 5000 | "Replaced 0.3 water tankers" |
| Money Saved (INR) | 💰 | Total L × 0.05 (₹5/L avg) | "Saved approx. ₹1,200" |
| CO₂ Reduced | 🌿 | Total L × 0.001 kg | "Reduced 5.2 kg CO₂ (vs tanker)" |

#### Progress Timeline
- Line chart showing cumulative impact score over months
- "You're in the top X% of users" (calculated locally based on days active)

---

### Screen 9: Achievements & Badges

**Type:** Gamification screen  
**Navigation:** More tab → Achievements  

#### Top App Bar
- Title: "Achievements"
- Badge count chip: "X / 15 earned"

#### Badge Grid (3 columns)
Each badge card shows:
- Icon (colored if earned, grayscale if locked)
- Badge name
- "Earned on [date]" or "XX L more to go"

#### Full Badge List

| Badge | Icon | Condition | Type |
|-------|------|-----------|------|
| First Drop | 💧 | Log first entry | Milestone |
| Rain Watcher | 🌧️ | Log 7 consecutive days | Streak |
| Century | 💯 | Save 100 liters total | Volume |
| Rain Warrior | ⚔️ | Save 500 liters total | Volume |
| Water Guardian | 🛡️ | Save 1,000 liters total | Volume |
| Monsoon Master | 🌊 | Save 5,000 liters total | Volume |
| Consistent Logger | 📅 | Log for 30 days total | Habit |
| Streak Champion | 🔥 | 14-day logging streak | Streak |
| AI Explorer | 🤖 | Use AI Tips 3 times | Feature |
| Goal Crusher | 🎯 | Meet monthly goal 3 times | Goal |
| Impact Hero | 🌱 | Reach 100 household days | Impact |
| Sharer | 📤 | Share a monthly report | Social |
| Crop Planner | 🌾 | Use AI Crop Planner | Feature |
| Night Logger | 🌙 | Log an entry after 9 PM | Fun |
| Early Bird | 🐦 | Log an entry before 7 AM | Fun |

#### Newly Earned Badge Animation
- When a badge is earned: Full-screen overlay with badge animation, particle burst, and "Achievement Unlocked!" toast
- Haptic feedback (vibration)

---

### Screen 10: Weather Forecast

**Type:** Live data screen  
**Navigation:** More tab → Weather  

#### Top App Bar
- Title: "Rainfall Forecast"
- Location chip showing current city
- Refresh icon

#### Section A — Today's Weather
- Large weather icon (rain/cloud/sun)
- Current temperature
- "Rainfall expected today: XX mm" (or "No rain expected today")
- Humidity and wind speed
- Data from OpenWeatherMap API (free tier)

#### Section B — 5-Day Forecast
- Horizontal scroll of 5 day cards
- Each card: Day name, weather icon, expected rainfall (mm)
- Teal highlight on days with significant rainfall (>5mm)

#### Section C — Harvest Prediction
- Auto-calculated using forecast data + user's roof area
- "Based on this week's forecast, you could harvest approximately **XXX liters**"
- CTA: "Pre-fill tomorrow's entry with forecast data" → opens Add Entry screen with rainfall pre-filled

#### Error / Offline State
- "Weather data unavailable. Check your internet connection."
- Last fetched timestamp shown
- Cached last result shown in offline state

---

### Screen 11: AI Crop & Garden Planner

**Type:** AI-powered utility screen  
**Navigation:** More tab → Crop Planner  

#### Top App Bar
- Title: "Garden Planner"
- Gemini badge

#### Section A — Your Water Summary
- Read-only summary card:
  - Total water available this month: XXX L
  - Roof area: X sq ft
  - City: [City Name]

#### Section B — Planner Input
- **Field 1:** Garden/plot area (sq ft) — NumberDecimal
- **Field 2:** Season selector — Dropdown (Kharif/Rabi/Summer/All Year)
- **Field 3:** Preference — Chips (Vegetables / Flowers / Herbs / Fruits / Mixed)
- **"Plan My Garden" Button** (full-width, teal)

#### Section C — AI Response
- Loading: Shimmer animation with "Jal-Bot is planning your garden... 🌱"
- Gemini prompt sent:
  ```
  "I have [X liters] of rainwater available this month in [City], India.
  My garden area is [Y sq ft]. Season: [season]. Preference: [type].
  Suggest 5 crops/plants I can grow with this water. For each, mention:
  water requirement per week, expected yield, and one care tip.
  Format as a numbered list."
  ```
- Response displayed as crop cards:
  - Crop name + emoji
  - Water required per week
  - Expected yield
  - One quick care tip
  - "Add to my plan" button (saves locally as a note)

#### My Garden Plans (Saved)
- List of past AI-generated plans, saved locally
- Swipe to delete

---

### Screen 12: Share & Export Report

**Type:** Utility screen  
**Navigation:** Share icon in Monthly Report (Screen 5), or More tab → Share  

#### Top App Bar
- Title: "Share Report"
- Back arrow

#### Report Preview Card
- Visual shareable card rendered on a Canvas:
  - App logo + "RainIQ Tracker" header
  - Month + Year
  - Total harvest in large bold text
  - 3 impact metrics (household days, trees, money saved)
  - "Generated with RainIQ Tracker" footer
  - Teal + white color scheme

#### Export Options
- **Share as Image:** Saves Canvas as PNG → opens Android native Share sheet
  - Shareable to WhatsApp, Instagram, Gmail, etc.
- **Share as Text:** Generates summary text:
  ```
  🌧️ RainIQ Report — April 2026
  💧 Water Harvested: 430 liters
  🏠 Household Days Powered: 3.2 days
  🌳 Trees Supported: 5.7 weeks
  💰 Estimated Savings: ₹215
  Track yours at RainIQ Tracker 📱
  ```
- **Export as CSV:** All entries exported as `.csv` file via Android FileProvider (openable in Excel/Google Sheets)

---

### Screen 13: Notifications & Reminders

**Type:** Settings sub-screen  
**Navigation:** Settings (Screen 14) → Notifications  

#### Top App Bar
- Title: "Reminders"
- Back arrow

#### Section A — Daily Logging Reminder
- Toggle: Enable/Disable
- Time picker: Choose reminder time (default: 8:00 PM)
- Label: "Remind me to log today's rainfall"
- Implementation: WorkManager `PeriodicWorkRequest` (repeats daily)

#### Section B — Streak Nudge
- Toggle: Enable/Disable
- Message: "If you haven't logged in 2 days, send a reminder"
- Implementation: WorkManager checks last entry date, fires notification if gap > 2 days

#### Section C — Goal Alert
- Toggle: Enable/Disable
- Message: "Notify me when I reach 50%, 75%, and 100% of my monthly goal"

#### Notification Content (Examples)
- Daily: "🌧️ Did it rain today? Log your harvest in RainIQ!"
- Streak: "💧 You've missed 2 days. Don't break your streak — log now!"
- Goal: "🎯 You've hit 75% of your April goal! Keep going!"

---

### Screen 14: Settings

**Type:** Configuration screen  
**Navigation:** Settings icon on Dashboard top bar, or More tab → Settings  

#### Top App Bar
- Title: "Settings"
- Back arrow

#### Section A — My Setup
- Roof Area — editable (opens number input dialog)
- Tank Capacity — editable
- Roof Material — editable dropdown
- Runoff Coefficient — editable (advanced)
- City/Location — editable
- "Update Setup" button — saves changes to SharedPreferences

#### Section B — App Preferences
- Theme: Light / Dark / Follow System (toggle)
- Default chart view: Bar / Line
- First day of week: Sunday / Monday (for weekly streak display)

#### Section C — Notifications
- Shortcut button → navigates to Notifications screen (Screen 13)

#### Section D — Data Management
- "Export All Data as CSV" → triggers CSV export
- "Reset All Data" → confirmation dialog ("This will permanently delete all entries. This cannot be undone.") → clears Room DB, resets SharedPreferences, returns to Onboarding

#### Section E — About
- App version
- Developer name
- College + Program name
- "Powered by Google Gemini AI" credit
- "View on GitHub" link (if applicable)

---

## 9. Navigation Flow

```
App Launch
    │
    ├── [First time] → Splash (Screen 1) → Onboarding Setup → Dashboard
    │
    └── [Returning]  → Dashboard (Screen 2)
                              │
              ┌───────────────┼───────────────────┐
              ▼               ▼                   ▼
         History (4)      Report (5)          AI Tips (6)
              │               │                   │
              │           Share (12)          Crop Planner (11)
              │
          Add Entry (3)
              │
          [Save] → Dashboard (with animation)

    Dashboard → [FAB] → Add Entry (3)
    Dashboard → [Impact Card] → Environmental Impact (8)
    Dashboard → [Goal Bar] → Goals & Targets (7)
    Dashboard → [Settings Icon] → Settings (14)

    Bottom Nav "More" →
        ├── Goals (7)
        ├── Impact (8)
        ├── Achievements (9)
        ├── Weather (10)
        └── Settings (14)

    Settings (14) → Notifications (13)
    Report (5) → Share (12)
```

---

## 10. Database Schema

### Room DB: `JalSanchayDatabase`

#### Table 1: `rainfall_entries`

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique entry ID |
| `date` | TEXT | NOT NULL | Date in `YYYY-MM-DD` format |
| `rainfall_mm` | REAL | NOT NULL | Rainfall in millimeters |
| `roof_area_sqft` | REAL | NOT NULL | Roof area at time of entry |
| `runoff_coefficient` | REAL | NOT NULL | Coefficient used for this entry |
| `liters_harvested` | REAL | NOT NULL | Calculated liters (computed and stored) |
| `roof_material` | TEXT | | Material type label |
| `notes` | TEXT | | Optional notes |
| `photo_path` | TEXT | | Local path to photo, nullable |
| `created_at` | INTEGER | NOT NULL | Unix timestamp of entry creation |

#### Table 2: `monthly_goals`

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique ID |
| `month` | TEXT | NOT NULL UNIQUE | `YYYY-MM` format |
| `goal_liters` | REAL | NOT NULL | Target for this month |
| `created_at` | INTEGER | NOT NULL | Timestamp |

#### Table 3: `achievements`

| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| `badge_id` | TEXT | PRIMARY KEY | e.g., `"first_drop"` |
| `earned_date` | TEXT | | `YYYY-MM-DD`, null if not earned |
| `is_earned` | INTEGER | NOT NULL DEFAULT 0 | Boolean (0/1) |

### DAO Methods Required

```kotlin
// RainfallDao
fun getAllEntries(): LiveData<List<RainfallEntry>>
fun getEntriesForMonth(month: String): LiveData<List<RainfallEntry>>
fun getTotalLitersSaved(): LiveData<Float>
fun getLitersSavedToday(): LiveData<Float>
fun insertEntry(entry: RainfallEntry)
fun deleteEntry(entry: RainfallEntry)
fun getEntriesForDateRange(start: String, end: String): List<RainfallEntry>
fun getMonthlyTotals(): LiveData<List<MonthlyTotal>>
```

---

## 11. API Integrations

### Integration 1: Google Gemini API

| Property | Value |
|----------|-------|
| **Provider** | Google AI Studio |
| **API Key** | Free tier — obtain from aistudio.google.com |
| **Model** | `gemini-2.5-flash-lite` (recommended for free tier) |
| **Free Limit** | 1,000 requests/day, 15 requests/minute |
| **Endpoint** | `https://generativelanguage.googleapis.com/v1beta/models/` |
| **Auth** | API key in request header |
| **Used In** | Screen 6 (Tips), Screen 11 (Crop Planner) |

**Cost:** Free for this project. Billing never required at college project scale.

**Error Handling:**
- 429 (Rate limit): Show "Please wait a moment and try again"
- 503 (Service unavailable): Show "AI service is currently unavailable"
- No internet: Show "Connect to the internet to use AI features"

---

### Integration 2: OpenWeatherMap API

| Property | Value |
|----------|-------|
| **Provider** | OpenWeatherMap |
| **API Key** | Free tier — obtain from openweathermap.org |
| **Free Limit** | 1,000 calls/day |
| **Endpoints Used** | `/weather` (current), `/forecast` (5-day) |
| **Used In** | Screen 10 (Weather Forecast) |

**Cost:** Free for this project.

---

## 12. Core Formula & Logic

### Water Harvest Calculation

The core formula used throughout the app:

```
Liters Harvested = Roof Area (sq ft) × Rainfall (mm) × 0.0929 × Runoff Coefficient
```

**Where:**
- `0.0929` = conversion factor from sq ft to sq meters (1 sq ft = 0.0929 sq m)
- Runoff Coefficient accounts for evaporation, splash, and roof material absorption loss

**Example:**
```
Roof Area     = 1,000 sq ft
Rainfall      = 25 mm
Coefficient   = 0.85 (Concrete roof)

Liters = 1000 × 25 × 0.0929 × 0.85
       = 1974.25 liters
```

### Runoff Coefficient Reference Table

| Roof Material | Coefficient | Notes |
|--------------|-------------|-------|
| Metal/Tin | 0.90 | Least absorption |
| Concrete/RCC | 0.85 | Most common in India |
| Clay Tiles | 0.75 | Moderate absorption |
| Asphalt | 0.70 | |
| Other | 0.65 | Conservative default |

### Impact Conversion Formulas

| Metric | Formula |
|--------|---------|
| Household days | `Total Liters ÷ 135` |
| Borewell trips saved | `Total Liters ÷ 20` |
| Trees supported (weeks) | `Total Liters ÷ 75` |
| Money saved (INR) | `Total Liters × 5` |
| CO₂ reduced (kg) | `Total Liters × 0.001` |

---

## 13. Non-Functional Requirements

### Performance
- App launch time: Under 2 seconds (cold start)
- Dashboard animations: 60fps smooth rendering
- Room DB queries: Under 100ms for all read operations
- API response timeout: 10 seconds (show error after)

### Offline Behavior
- All core features (logging, history, dashboard, report) work fully offline
- AI Tips and Weather Forecast require internet; graceful error states shown
- Static pre-loaded tips always available on Screen 6

### Input Validation Rules
- All number inputs: reject non-numeric characters in real-time
- Rainfall range: 0.1mm — 500mm (warn above 300mm)
- Roof area range: 50 — 50,000 sq ft
- Tank capacity range: 100 — 1,000,000 liters
- Date: Cannot be in the future; cannot be before app install date

### Accessibility
- Minimum touch target: 48dp × 48dp for all buttons
- Content descriptions on all icons (for TalkBack)
- Text size respects system font size settings
- Color is never the sole indicator of state (use icons + text alongside color)

### Security
- Gemini and OpenWeatherMap API keys must NOT be hardcoded in source code
- Store API keys in `local.properties` and access via `BuildConfig`
- No user data transmitted to external servers (all data stored locally)

---

## 14. Build Timeline

| Week | Milestone | Screens Delivered |
|------|-----------|------------------|
| **Week 1** | Setup Android project, Room DB, MVVM architecture, Onboarding | Screen 1, 3, 4 |
| **Week 2** | Dashboard with animated tank, Goals, core formula logic | Screen 2, 7 |
| **Week 3** | Monthly Report with charts, Impact screen, Achievements | Screen 5, 8, 9 |
| **Week 4** | Gemini AI integration (Tips + Crop Planner), Weather API | Screen 6, 10, 11 |
| **Week 5** | Share/Export, Notifications/WorkManager, Settings | Screen 12, 13, 14 |
| **Week 6** | UI polish, bug fixes, animation refinement, testing on device, APK build | All screens QA |

**Minimum Viable Product (MVP) — End of Week 3:**
Screens 1, 2, 3, 4, 5, 7 — all success criteria met. Screens 6–14 are enhancements.

---

## 15. Out of Scope

The following features are explicitly excluded from this internship build to maintain focus:

- **Multi-user / household accounts** — single user only
- **Cloud sync or backend server** — all data is local on-device
- **Play Store publishing** — APK submission to evaluator only
- **iOS version** — Android only
- **Kannada localization** — English UI only (unlike Project #37)
- **Paid API tiers** — all integrations must remain within free limits
- **Bluetooth/IoT sensor integration** — manual rainfall input only
- **Social features or leaderboards** — achievements are local only

---

*Document prepared for MindMatrix VTU Internship Program — Project #86.*  
*All formulas referenced from standard rainwater harvesting engineering guidelines.*


<!-- Designed and Developed by Yuvraj Bhatt -->
