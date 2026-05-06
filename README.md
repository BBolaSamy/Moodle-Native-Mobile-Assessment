# Moodle Native Mobile Assessment

This repository contains native implementations for the Moodle LMS technical assessment:
- iOS app built with SwiftUI
- Android app built with Jetpack Compose

## Repository Structure
- `ios/MoodleAssessmentApp` iOS project
- `android` Android project

## Assessment Scope Covered
- Courses: `core_enrol_get_users_courses`
- Course Details: `core_course_get_contents`
- Grades: `gradereport_user_get_grade_items`

## API Configuration
- Base URL: `https://moodle.itcorner.qzz.io`
- REST endpoint: `/webservice/rest/server.php`
- Auth flow used in this submission: token-only (no login screen)
- Default user id: `1003`

## Architecture Overview
Both apps follow equivalent architecture:
- MVVM presentation layer
- Repository abstraction for data access
- DTO -> Domain mapper layer
- Shared UI state modeling (`loading`, `success`, `empty`, `error`)

## Build and Run
See platform-specific setup and run steps in:
- `ios/MoodleAssessmentApp/README.md`
- `android/README.md`

## Notes on implementation decisions
- Focus is limited to the requested assessment scope.
- Loading, empty, error, and retry behaviors are implemented across required screens.
- Android includes retry/backoff for transient server failures.
