>  **Refactoring in Progress**
> 
> The project is currently being refactored to improve architecture, scalability, and code quality.
> Additional features and performance improvements are actively being implemented.
> Some functionality may change during this transition.

# GJU Events

**GJU Events** is an Android application developed for students and staff at the German Jordanian University (GJU). The app enables users to view and manage university-related events through a modern and user-friendly interface. It supports features like user authentication, event listing, and interaction through Jetpack Compose.

---

##  Features

-  User sign-up and login (Firebase Authentication)
-  Browse university events
-  Mark attendance or view event details
-  MVVM architecture with Jetpack Compose
-  Firebase integration (Firestore, Auth)

---

##  Tech Stack

- **Kotlin**
- **Jetpack Compose** for UI
- **MVVM architecture**
- **Firebase** (Authentication & Firestore)
- **Android Studio (Kotlin DSL)**

---

##  Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yazanalkherret/GJU-Events.git
cd GJU-Events
```

### 2. Open in Android Studio

- Launch **Android Studio**.
- Open the cloned folder (`GJUEvents/`) as a project.

### 3. Configure Firebase

- This project uses Firebase — make sure you:
    - Create a Firebase project at [https://console.firebase.google.com](https://console.firebase.google.com)
    - Download the `google-services.json` file
    - Place it in:
      ```
      app/google-services.json
      ```

### 4. Sync and Build the Project

- Let Android Studio sync Gradle.
- Run the app on an emulator or physical device (API 24+ recommended).

---

##  Project Structure

```
GjuEvents/
├── app/
│   ├── src/main/java/com/example/myapplication/
│   │   ├── screens/         # Jetpack Compose screens
│   │   ├── viewmodels/      # MVVM ViewModels
│   │   └── MainActivity.kt  # Entry point
│   └── AndroidManifest.xml
├── build.gradle.kts
└── settings.gradle.kts
```

