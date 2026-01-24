# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Documentation

Refer to the following documents only when needed:

| File | Purpose | When to Reference |
|------|---------|-------------------|
| `@docs/architecture.md` | Module structure, dependencies, implementation patterns | When implementing new features |
| `@docs/coding-conventions.md` | Naming conventions, Compose guidelines | When writing code |
| `@docs/testing.md` | Unit Test, Screenshot Test guidelines | When writing tests |

## Project Overview

SunsetScrob is a Last.fm client Android application built with modern Android development practices. It's a modular Kotlin app using Jetpack Compose, Clean Architecture, and feature-based modules.

## Setup Requirements

Before building, create `local.properties` with Last.fm API credentials:
```
API_KEY=YOUR_LAST_FM_API_KEY
SHARED_SECRET=YOUR_LAST_FM_SHARED_SECRET
```

## Common Development Commands

### Build Commands
- `./gradlew installDebug` - Install debug version on device
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew bundleRelease` - Build release AAB
- `make generate_compose_reports` - Generate Compose compiler reports

### Fastlane Commands
- `fastlane build_debug` - Build debug version
- `fastlane test` - Run unit tests
- `fastlane screenshot_test` - Run Roborazzi screenshot tests
- `fastlane android_test` - Run instrumentation tests

### Testing
- **Unit Tests**: Kotest with MockK, run via `fastlane test`
- **Screenshot Tests**: Roborazzi framework, run via `fastlane screenshot_test`

## Architecture Overview

### Module Structure
- **`app/`** - Main application module with navigation and Hilt setup
- **`core/`** - Shared entities and utilities
- **`data/`** - Data layer split into `api/`, `db/`, and `repository/` modules
- **`feature/`** - Feature modules: `account/`, `album/`, `artist/`, `auth/`, `discover/`, `home/`, `scrobble/`
- **`ui_common/`** - Shared UI components and theming
- **`build-logic/`** - Custom Gradle convention plugins

### Key Architectural Patterns
- **Clean Architecture** with clear separation of concerns
- **MVVM** with Compose UI and ViewModels
- **Repository Pattern** for data access abstraction
- **Dependency Injection** via Dagger Hilt
- **Modular Design** with feature-based modules

### Technology Stack
- **UI**: Jetpack Compose with Material 3
- **Networking**: Ktor client for Last.fm API
- **Database**: SQLDelight for local storage
- **Async**: Kotlin Coroutines and Flow
- **Image Loading**: Coil
- **Build**: Gradle with Kotlin DSL and custom convention plugins

## Development Notes

### Gradle Configuration
- Uses version catalog (`gradle/libs.versions.toml`) for dependency management
- Custom convention plugins in `build-logic/` for consistent module setup
- 29 custom lint rules defined in `app/lint-checks.gradle`

### Testing Framework
- Unit tests use Kotest with JUnit 5
- UI tests use Roborazzi for screenshot testing
- MockK for mocking, Turbine for Flow testing
- Custom test helper modules for shared testing utilities

### Code Quality
- Linting via Android Lint with custom rules
- Licensee plugin for dependency license validation
- Proguard configuration for release builds
