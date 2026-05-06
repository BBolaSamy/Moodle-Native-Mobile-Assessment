# Moodle Assessment Android (Jetpack Compose)

Native Android implementation of the Moodle technical assessment.

## Requirements
- Android Studio (latest stable)
- JDK 17
- Android SDK with emulator or physical device

## Tech Stack
- Kotlin
- Jetpack Compose + Material 3
- Retrofit + OkHttp + Moshi
- Coil
- Coroutines + StateFlow

## Configuration
The app uses token-only authentication (no login screen).

Values are configured in:
- `app/src/main/java/com/itcorner/moodleandroid/data/network/MoodleConfig.kt`

Configured endpoints:
- Base URL: `https://moodle.itcorner.qzz.io`
- REST endpoint: `/webservice/rest/server.php`
- User id: `1003`

## Architecture
- `domain`
- Models: `Course`, `CourseSection`, `GradeItem`
- Repository contract: `CourseRepository`
- Screen state: `ViewState` (`Idle`, `Loading`, `Success`, `Empty`, `Error`)
- `data`
- `network`: Retrofit service + request/query construction
- `dto`: API response models
- `mappers`: DTO to domain mapping
- `repository`: Moodle API orchestration with retry/backoff for transient 5xx failures
- `presentation`
- ViewModels and Compose screens for Courses, Details, and Grades

## Features Implemented
- Courses screen (`core_enrol_get_users_courses`)
- Course details screen (`core_course_get_contents`)
- Grades screen (`gradereport_user_get_grade_items`)
- Pull-to-refresh on Courses
- Loading/empty/error/retry states on required screens

## Run
1. Open the `android` folder in Android Studio.
2. Sync Gradle.
3. Run on emulator or device.
