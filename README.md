# STRIDE Wear OS App

STRIDE is a Wear OS smartwatch application developed for a senior capstone project on rhythmic gait training for stroke rehabilitation. The app delivers a therapist-configured metronome, detects walking pole strikes from watch accelerometer data, compares each strike to the nearest metronome beat, and provides immediate visual, audio, and haptic feedback to help patients stay on rhythm while walking.

![STRIDE watch interface](./Watch%20Face.jpg)

## Project Overview

The app is designed to support rehabilitation sessions in which a patient walks with a prescribed cadence. During a session, STRIDE:

- plays a metronome at a selected beats-per-minute (BPM) rate,
- detects pole strikes using accelerometer data from the watch,
- computes whether each strike occurred on beat or off beat,
- provides multimodal feedback on the watch,
- saves session statistics for later review.

The repository currently contains the Wear OS app only.

## Core Features

- `Configurable metronome`: users can adjust BPM directly or set it through calibration workflows.
- `Calibration modes`: the app includes a tap-based BPM mode and a pole-strike calibration mode.
- `Pole strike detection`: accelerometer samples are processed to identify likely walking pole impacts.
- `Rhythm accuracy feedback`: each detected strike is compared to the nearest beat using a timing threshold.
- `Multimodal cues`: the app supports visual feedback on-screen, metronome audio, confirmation/error tones, and optional vibration.
- `Session history`: completed sessions are stored locally and can be reviewed on the watch.
- `Wear OS tile`: a tile provides quick access to launch the application.

## How It Works

1. A patient or therapist selects a cadence.
2. The metronome starts and maintains the selected BPM.
3. The watch listens to accelerometer data and detects pole strikes.
4. Each strike is compared against the nearest metronome beat.
5. The app classifies the strike as on beat or off beat.
6. STRIDE presents immediate feedback and updates session statistics.
7. At the end of the session, the app stores summary metrics locally.

## Session Metrics Recorded

For each saved session, the app stores:

- session timestamp,
- session duration,
- total pole strikes,
- percentage of strikes that were on beat,
- average absolute timing offset from the nearest beat in milliseconds.

## Tech Stack

- `Kotlin`
- `Jetpack Compose for Wear OS`
- `AndroidX Wear Navigation`
- `Room` for local session storage
- `DataStore` for user settings
- `Wear Tiles`
- Android accelerometer APIs for motion sensing

## Repository Structure

```text
app/
  src/main/java/com/example/stride/
    data/           Room database, DAO, entities, repository
    haptics/        Haptic feedback controller
    navigation/     Wear navigation graph and routes
    presentation/   Compose UI screens, themes, and view models
    sensors/        Accelerometer access and pole-strike detection
    tile/           Wear OS tile service
    timing/         Beat matching and timing statistics
```

## Build and Run

### Prerequisites

- Android Studio with Wear OS support
- JDK 11
- A Wear OS emulator or physical Wear OS watch
- Android SDK with `compileSdk 34`

### Open the Project

1. Clone the repository.
2. Open the project in Android Studio.
3. Let Gradle sync complete.

### Run the App

1. Start a Wear OS emulator or connect a Wear OS device.
2. Select the `app` run configuration in Android Studio.
3. Build and deploy the app to the watch or emulator.

### Command Line

```bash
./gradlew assembleDebug
./gradlew test
```

## Permissions and Device Requirements

The app requests the following Android permissions:

- `BODY_SENSORS`
- `ACTIVITY_RECOGNITION`
- `VIBRATE`
- `WAKE_LOCK`
- `BLUETOOTH`
- `BLUETOOTH_CONNECT`

The app is intended for Wear OS devices with accelerometer support. Heart rate support is declared as optional.

## Architecture Notes

- `MetronomeViewModel` manages BPM, beat timing, stopwatch state, and session persistence.
- `SettingsViewModel` stores default BPM and feedback mode preferences using `DataStore`.
- `PoleStrikeDetector` analyzes accelerometer data to emit strike events.
- `PoleStrikeTimingManager` compares strike timestamps to beat timing and computes timing accuracy statistics.
- `Room` stores completed session summaries for the past sessions screen.

## Current Behavior and Defaults

- Default BPM is `60`.
- Visual feedback is enabled by default.
- Audio feedback is enabled by default.
- Vibration feedback is disabled by default.
- A strike is considered on beat when it falls within `200 ms` of the nearest metronome beat.

## Known Limitations

- Session data is stored locally on-device and is not synced to a phone or cloud backend.
- The repository does not currently include automated documentation assets such as screenshots or demo videos.
- Strike detection thresholds are tuned in-app code and may need refinement for different users, walking styles, or hardware.
- The app targets a Wear OS watch workflow and is not a standalone phone application.

## Academic Context

This project was developed as part of a senior capstone project focused on stroke rehabilitation support through wearable technology. STRIDE is intended as a research and prototype application rather than a production medical device.
