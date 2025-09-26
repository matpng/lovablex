# Employee Tracker Android App

A full-featured native Android application for tracking employee movements using real-time location services.

## Features

1. **Employee Authentication**
   - Email/password login system (local-only storage)
   - Admin and regular employee roles
   - Demo credentials pre-configured for testing

2. **Real-time Location Tracking**
   - Uses FusedLocationProviderClient for accurate location
   - Foreground service for background tracking
   - Location updates every 30 seconds
   - Privacy notice and permission prompts

3. **Check-in/Check-out System**
   - Start/stop tracking with dedicated buttons
   - Session management with timestamps
   - Visual status indicators

4. **Local Data Storage**
   - Room database for offline operation
   - Employee profiles and credentials
   - Location history with timestamps
   - Tracking session records

5. **Admin Dashboard**
   - View all employee locations
   - Real-time status updates
   - Location history for each employee
   - Pull-to-refresh functionality

6. **Modern Android Architecture**
   - MVVM pattern with ViewModels and LiveData
   - Repository pattern for data management
   - Coroutines for async operations
   - Material Design 3 components

7. **Background Operation**
   - Persistent notification while tracking
   - Foreground service for location updates
   - Background location permissions handling

## Demo Credentials

The app comes pre-configured with demo accounts:

### Regular Employees:
- **Email:** john@company.com **Password:** password123
- **Email:** jane@company.com **Password:** password123

### Admin Account:
- **Email:** admin@company.com **Password:** admin123

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK with minimum API level 24 (Android 7.0)
- JDK 8 or later
- Gradle 8.0 or later

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/matpng/lovablex.git
   cd lovablex
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory and select it
   - Wait for Gradle sync to complete

3. **Build the Project**
   ```bash
   ./gradlew build
   ```

4. **Run on Device/Emulator**
   - Connect an Android device with USB debugging enabled, or
   - Create an Android Virtual Device (AVD) in Android Studio
   - Click "Run" button or use:
   ```bash
   ./gradlew installDebug
   ```

### Location Permissions Setup

The app requires location permissions to function properly:

1. **Fine Location Permission** - For accurate GPS tracking
2. **Background Location Permission** - For tracking when app is closed
3. **Notification Permission** - For foreground service notifications

**Important:** On Android 10+ (API 29+), background location permission must be granted separately through device settings.

## Project Structure

```
app/src/main/java/com/example/employeetracker/
├── data/
│   ├── database/          # Room database entities and DAOs
│   └── repository/        # Repository pattern implementation
├── services/
│   └── LocationTrackingService.kt  # Foreground location service
└── ui/
    ├── login/            # Login screen and ViewModel
    ├── tracker/          # Employee tracking screen
    └── admin/            # Admin dashboard
```

## Key Dependencies

- **AndroidX Core & AppCompat** - Core Android libraries
- **Material Design Components** - Modern UI components
- **Room Database** - Local data persistence
- **Location Services** - Google Play Services location APIs
- **Lifecycle Components** - ViewModel and LiveData
- **Coroutines** - Asynchronous programming

## Architecture Overview

### MVVM Pattern
- **View**: Activities and Fragments with ViewBinding
- **ViewModel**: Business logic and UI state management
- **Model**: Repository pattern with Room database

### Data Flow
1. User interactions trigger ViewModel methods
2. ViewModel calls Repository for data operations
3. Repository manages database operations via DAOs
4. LiveData updates trigger UI refreshes

### Location Tracking Flow
1. User grants location permissions
2. Check-in starts a new tracking session
3. LocationTrackingService runs in foreground
4. Location updates stored in database every 30 seconds
5. Admin dashboard displays real-time employee locations

## Privacy and Security

- All data stored locally on device
- Location data collection clearly disclosed to users
- Privacy notice displayed before first use
- Appropriate Android permissions requested
- Background location usage explained to users

## Testing

### Manual Testing Steps

1. **Login Flow**
   - Test with valid credentials
   - Test with invalid credentials
   - Test admin vs employee login

2. **Location Tracking**
   - Grant location permissions
   - Start tracking and verify notification appears
   - Check location updates in admin dashboard
   - Test stop tracking functionality

3. **Admin Dashboard**
   - Login as admin user
   - Verify employee list displays correctly
   - Check location data updates
   - Test pull-to-refresh

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

## Future Enhancements

The codebase is prepared for future REST API integration:

1. **Server Synchronization**
   - Repository pattern ready for network calls
   - Local data can be synced to remote server
   - Offline-first architecture supports sync when online

2. **Additional Features**
   - Geofencing for work locations
   - Route tracking and optimization
   - Reporting and analytics
   - Push notifications for admin

## Troubleshooting

### Common Issues

1. **Location Not Updating**
   - Ensure location permissions are granted
   - Check device location settings are enabled
   - Verify GPS signal availability

2. **Background Tracking Stops**
   - Grant background location permission
   - Disable battery optimization for the app
   - Ensure notification permission is granted

3. **Build Errors**
   - Clean and rebuild project: `./gradlew clean build`
   - Invalidate caches in Android Studio
   - Check Android SDK and build tools are up to date

### Permissions Guide

For Android 10+ devices:
1. Initial location permission popup grants foreground access
2. Background location requires additional permission
3. Go to Settings > Apps > Employee Tracker > Permissions > Location
4. Select "Allow all the time" for background tracking

## Support

For issues and questions:
- Check the [Issues](https://github.com/matpng/lovablex/issues) section
- Review Android documentation for location services
- Test on different Android versions and devices

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Package Name:** com.example.employeetracker
**Minimum SDK:** 24 (Android 7.0)
**Target SDK:** 34 (Android 14)
**Build Tools:** Gradle 8.0+