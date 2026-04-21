<div align="center">

# 🔥 EV Battery Fire Prevention System

### An AI-powered Android application for real-time EV battery monitoring and fire hazard prevention

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%2F%20Java-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![IDE](https://img.shields.io/badge/IDE-Android%20Studio-3DDC84?style=flat-square&logo=androidstudio&logoColor=white)](https://developer.android.com/studio)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success?style=flat-square)]()

<br/>

> Detects early signs of EV battery overheating and prevents fire hazards using intelligent real-time monitoring and predictive analysis — before they become critical failures.

<br/>

<!-- Replace with actual screenshot/demo GIF -->
![App Demo](https://via.placeholder.com/800x400?text=Snacker+App+Screenshot)

</div>

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [How It Works](#-how-it-works)
- [Use Cases](#-use-cases)
- [Future Enhancements](#-future-enhancements)
- [What I Learned](#-what-i-learned)
- [Author](#-author)

---

## 🚀 Overview

Electric vehicle battery fires are a growing safety concern — they ignite rapidly and are notoriously difficult to extinguish. This project addresses that problem by building an Android application that continuously monitors battery parameters and uses AI-based logic to detect anomalies **before** they escalate.

The system identifies early warning signs such as abnormal temperature spikes, sends real-time alerts to the user, and provides a data-driven safety layer that can integrate with IoT sensors and cloud platforms in future iterations.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🌡️ Real-time Monitoring | Continuous tracking of battery temperature and key parameters |
| ⚠️ Early Warning System | Alerts triggered before temperature reaches critical thresholds |
| 🧠 AI Prediction Engine | Machine learning logic to predict thermal runaway risk |
| 📊 Data Analysis Dashboard | Visual representation of battery health trends |
| 🔔 Push Notifications | Instant alerts for abnormal conditions |
| 📱 Mobile-first UI | Clean, user-friendly interface built with XML layouts |

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin / Java |
| UI Design | XML (Android Layouts) |
| IDE | Android Studio |
| Data Storage | Firebase / Local Storage |
| ML / AI | Machine Learning concepts for predictive logic |
| Build System | Gradle (Kotlin DSL) |

---

## 📂 Project Structure

```
EVFirePreventionApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/          # Kotlin/Java source files
│   │   │   │   ├── model/     # Data models (BatteryData, Alert, etc.)
│   │   │   │   ├── ui/        # Activities and Fragments
│   │   │   │   ├── service/   # Background monitoring services
│   │   │   │   └── utils/     # Helper classes and ML logic
│   │   │   ├── res/
│   │   │   │   ├── layout/    # XML UI layouts
│   │   │   │   ├── drawable/  # Icons and graphics
│   │   │   │   └── values/    # Colors, strings, themes
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

---

## ⚙️ Getting Started

### Prerequisites

- Android Studio (Flamingo or later recommended)
- JDK 11+
- Android SDK (API level 24+)
- A physical Android device or emulator

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/karthik8310/ev-battery-fire-prevention.git
   cd ev-battery-fire-prevention
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the project folder
   ```

3. **Sync Gradle dependencies**
   ```
   Android Studio will prompt to sync — click "Sync Now"
   ```

4. **Run the app**
   ```
   Click the ▶ Run button or press Shift + F10
   ```

> If you're using Firebase, add your `google-services.json` file inside the `app/` directory before running.

---

## 🧠 How It Works

```
Battery Sensor Data
        │
        ▼
┌─────────────────────┐
│  Data Collection    │  ← Reads temperature, voltage, current
└─────────────────────┘
        │
        ▼
┌─────────────────────┐
│  AI Analysis Layer  │  ← Compares against safe thresholds + ML prediction
└─────────────────────┘
        │
     ┌──┴──┐
   Safe   Anomaly
     │       │
     ▼       ▼
  Continue  Alert
  Monitor   System  ← Push notification + UI warning
```

The monitoring service runs continuously in the background. When battery parameters cross defined thresholds or the ML model detects a high-risk pattern, the system immediately triggers a notification and logs the event for review.

---

## 🎯 Use Cases

- **Electric Vehicles** — proactive battery safety during charging and driving
- **Smart Fleet Management** — monitor multiple EVs simultaneously
- **Automotive Safety Research** — dataset collection for thermal runaway studies
- **EV Manufacturing QA** — battery stress testing and anomaly logging

---

## 🔮 Future Enhancements

- [ ] IoT sensor integration for hardware-level real-time data
- [ ] Cloud-based monitoring dashboard (Firebase / AWS)
- [ ] Advanced ML models (LSTM / time-series prediction) for higher accuracy
- [ ] Live vehicle GPS tracking integration
- [ ] Emergency SOS alert system (SMS / call to contacts)
- [ ] Multi-vehicle support for fleet monitoring
- [ ] Wear OS companion app for wrist alerts

---

## 📚 What I Learned

- Android application development using **Kotlin and Java**
- Managing Android project structure with **Gradle Kotlin DSL**
- Implementing **background services** for continuous monitoring
- Applying **AI/ML concepts** to real-world safety problems
- Understanding **EV battery chemistry** and thermal runaway risks
- Building **notification systems** and real-time data pipelines

---

## 🤝 Contributing

Contributions are welcome! If you have ideas for improvements:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push and open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 👨‍💻 Author

**Karthik Kumar**

[![GitHub](https://img.shields.io/badge/GitHub-karthik8310-181717?style=flat-square&logo=github)](https://github.com/karthik8310)
[![Email](https://img.shields.io/badge/Email-KK1981060%40gmail.com-D14836?style=flat-square&logo=gmail&logoColor=white)](mailto:KK1981060@gmail.com)

---

<div align="center">

⭐ If you found this project useful, please consider giving it a star!

</div>
