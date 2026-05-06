# MoodleAssessmentApp (iOS)

Native iOS implementation of the Moodle LMS technical assessment using SwiftUI.

## Requirements
- Xcode 15+
- iOS 17.0+

## Configuration
The app is preconfigured for token-only access (no login screen).

Configuration file:
- `MoodleAssessmentApp/Config/MoodleConfig.swift`

Configured values:
- Base URL: `https://moodle.itcorner.qzz.io`
- REST endpoint: `/webservice/rest/server.php`
- User id: `1003`

## Architecture
MVVM + Repository with clear layers:
- `Domain`: entities, state model, repository protocol
- `Data`: API client, DTOs, mapping, repository implementation
- `Presentation`: SwiftUI screens and ViewModels
- `App`: dependency container and root composition

## Features Implemented
- Courses screen (`core_enrol_get_users_courses`)
- Course details screen (`core_course_get_contents`)
- Grades screen (`gradereport_user_get_grade_items`)
- Loading/empty/error/retry states for required screens

## Run
1. Open `ios/MoodleAssessmentApp/MoodleAssessmentApp.xcodeproj` in Xcode.
2. Select the `MoodleAssessmentApp` scheme.
3. Run on an iOS simulator or device.

## Tests
Included test coverage:
- API request builder tests
- DTO-to-domain mapper tests
- ViewModel state transition tests

## Notes on implementation decisions
- The app uses token-only authentication for this submission to keep access aligned with the provided assessment credentials.
- MVVM + Repository architecture is used to keep feature logic testable and consistent with the Android implementation.
- State-driven UI (`loading`, `success`, `empty`, `error`) is applied across required screens for predictable UX.
- Scope is intentionally limited to required endpoints and screens; offline cache and extended Moodle modules are excluded in this phase.
