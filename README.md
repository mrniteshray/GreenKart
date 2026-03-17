# Greenkart 🍏🛒

**Greenkart** is a premium, Zomato-inspired grocery delivery application built using modern Android development practices. It offers a seamless and visually stunning experience for browsing and purchasing fresh vegetables.

![Greenkart Logo](app/src/main/res/drawable/logo.png)

## ✨ Key Features

- **🚀 Premium Splash Screen**: A beautifully animated entry with scaling and fading effects, featuring smart routing based on authentication status.
- **📱 Zomato-style Home Screen**: A rich, dynamic interface with interactive categories, "Top Picks" for users, and working real-time search.
- **🛡️ Secure Authentication**: Full integration with Firebase Auth for secure Email/Password login and signup, including robust input validation.
- **🧺 Smart Cart System**: 
  - Persistent local storage using **Room Database**.
  - Zomato-style **Floating Cart Bar** showing real-time item counts and total price.
  - Smooth checkout flow with order confirmation dialogs.
- **❤️ Favorites (My List)**: Save your favorite vegetables locally and access them quickly from a dedicated "My List" screen.
- **👤 Profile Management**: Real-time Firestore sync for user details, allowing users to edit their address and contact information.
- **🎨 Premium UI/UX**:
  - Material 3 Design with a vibrant Green/White palette.
  - Custom dialogs for logout and order placement.
  - Smooth image loading with **Coil**.
  - Consistent and non-intrusive toast feedback.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Local Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Backend/Auth**: [Firebase Auth](https://firebase.google.com/docs/auth) & [Cloud Firestore](https://firebase.google.com/docs/firestore)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Architecture**: MVVM with **Clean Architecture** principles.
- **Async Operations**: Coroutines & Flow

## 🏗️ Architecture

The project follows **Clean Architecture** to ensure highly maintainable, testable, and scalable code.

- **`domain`**: Contains the core business logic, entities (`Vegetable`, `CartItem`, `User`), and repository interfaces.
- **`data`**: Implements repository interfaces, handles local Room DB operations, JSON parsing (`vegetables.json`), and Firebase integration.
- **`presentation`**: UI components, Screens, and ViewModels.
- **`di`**: Koin modules for dependency injection.

## 📁 Project Structure

```text
app/src/main/java/com/greenkart/
├── data/           # Repositories & Local/Remote Data Sources
│   ├── local/      # Room DB, DAOs, TypeConverters
│   └── repository/ # Repository implementations
├── domain/         # Business Logic & Entities
│   ├── model/      # Data Classes
│   └── repository/ # Interfaces
├── presentation/   # UI Layer (Compose)
│   ├── account/    # Profile & Settings
│   ├── auth/       # Login & Signup
│   ├── cart/       # Cart logic & Screen
│   ├── components/ # Shared UI Components (VegetableCard, etc.)
│   ├── detail/     # Product Details
│   ├── favorite/   # My List Screen
│   ├── home/       # Home Screen & Search
│   ├── navigation/ # App Navigation & Routes
│   └── splash/     # Animated Splash Screen
└── di/             # Koin Dependency Modules
```

## 🚀 Getting Started

1. **Clone the repository**.
2. **Setup Firebase**: Add your `google-services.json` to the `app/` directory.
3. **Run the app**: Build and run on an Android device or emulator (API 24+).

---

Developed with ❤️ for a premium grocery experience.
