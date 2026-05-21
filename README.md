# WeatherSnap

Android intern assignment for Trakzio — search live weather, capture a field photo, compress it, add notes, and save a report locally.

---

## Setup & Run

**Requirements:** Android Studio Hedgehog or newer, JDK 17, Android SDK 26+.

```bash
git clone https://github.com/jarvis1704/WeatherSnap.git
cd WeatherSnap
```

1. Open in Android Studio.
2. Let Gradle sync finish.
3. Run on a physical device or emulator with API 26+.
4. No API keys needed — uses Open-Meteo (free, no auth).

> Camera features require a physical device or an emulator with a virtual camera configured.

---

## Screens

| Screen | Description |
|---|---|
| Weather | Search city, autocomplete suggestions, live weather fetch |
| Create Report | Weather snapshot + photo capture + notes + save |
| Custom Camera | CameraX live preview, capture, image compression |
| Saved Reports | All saved reports from Room DB with image/weather/notes |

---

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, ViewModel, StateFlow |
| DI | Hilt (KSP) |
| Navigation | Navigation Compose 2.9.0 |
| Network | Retrofit 2.11.0, OkHttp 4.12.0, Gson |
| Local DB | Room 2.7.1 (KSP) |
| Camera | CameraX 1.5.0 |
| Image loading | Coil 3.2.0 |
| Async | Coroutines 1.10.2 |
| Language | Kotlin 2.2.10 |

**APIs:**
- Geocoding: `https://geocoding-api.open-meteo.com/v1/search`
- Forecast: `https://api.open-meteo.com/v1/forecast`

---

## Architecture

Single-module MVVM. No use-cases layer — kept flat intentionally for assignment scope.

```
data/
  remote/api/        ← GeocodingApiService, WeatherApiService (two @Named Retrofit instances)
  remote/dto/        ← GeocodingResponseDto, WeatherResponseDto
  local/db/          ← WeatherSnapDatabase, ReportDao
  local/entity/      ← ReportEntity (flat fields, no TypeConverters needed)
  repository/        ← WeatherRepositoryImpl (mappers private here)
domain/
  model/             ← City, Weather (WMO code→condition map), Report
  repository/        ← WeatherRepository interface (Kotlin stdlib Result<T>)
di/                  ← NetworkModule, DatabaseModule, RepositoryModule
feature/
  weather/           ← WeatherScreen, WeatherViewModel, WeatherUiState
  createreport/      ← CreateReportScreen, CreateReportViewModel
  camera/            ← CameraScreen, CameraViewModel
  savedreports/      ← SavedReportsScreen, SavedReportsViewModel
navigation/          ← AppNavGraph, Screen sealed class
ui/theme/            ← Dark-only theme, custom palette (DarkBackground, LimeAccent)
```

---

## Developer Judgment Challenge

**Problem:** User selects weather → navigates to Create Report → captures photo → enters notes → rotates device or app is backgrounded/killed. All in-progress state must survive without saving a duplicate report.

**Solution: `SavedStateHandle` as draft store in `CreateReportViewModel`**

When `CreateReportScreen` is entered, the weather snapshot is passed as a URL-encoded JSON navigation argument and immediately written into `SavedStateHandle` under the key `draft_weather`. As the user captures a photo or types notes, `draft_image_uri` and `draft_notes` are also written to `SavedStateHandle` on every change.

`SavedStateHandle` survives:
- configuration changes (rotation)
- process death with app in background (Android restores the back stack and re-delivers the bundle via `SavedStateHandle`)

The weather data is **frozen at navigation time** — it is never re-fetched. The exact snapshot the user saw when they tapped "Create Report" is what ends up in the saved report, regardless of what the weather API would return later.

**Tradeoffs:**

| Concern | Decision |
|---|---|
| Weather freshness | Intentionally stale — assignment requires exact snapshot, not current weather |
| Draft persistence scope | Lost on explicit back/discard; only survives lifecycle events, not deliberate exits |
| Temp image files | CameraX writes to app-internal storage; on save the URI is moved to the Room entity; on discard the ViewModel `onCleared` deletes the temp file |
| Duplicate reports | `SavedStateHandle` is tied to the back-stack entry, not created fresh on re-entry, so restoring never triggers a second save |

No database draft table, no WorkManager, no additional complexity — `SavedStateHandle` is the right primitive for this problem at this scope.

---

## UI Notes

- Dark-only theme. No dynamic color.
- Custom palette: `DarkBackground` `#0D1208`, `LimeAccent` `#BDCC5A`, olive→mint gradient header.
- All 4 weather states implemented: empty, suggestions, loading, success.
- Skeleton shimmer on weather load.
- Animated city suggestion list (slide + fade).
- Smooth screen transitions via Navigation Compose enter/exit animations.
