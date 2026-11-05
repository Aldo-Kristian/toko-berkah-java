# Toko Sembako "Berkah Jaya" — Group Project Scaffold

Android + API starter for the Chapter 10 group assignment.

**This repository is scaffolding only.** It builds and runs, but dashboard, product /
category / supplier CRUD, search & filter, and low-stock warnings are intentionally not
implemented — the team owns those features.

## What is in the repo

| Path | Description |
|---|---|
| `sembakoapp/` | Android app, **Java**, classic Views, ViewBinding |
| `sembakoapi/` | API server, **Java**, Spring Boot 4 + SQLite |
| `gradle/libs.versions.toml` | Shared dependency versions |
| `README.md` | This guide |

Dependencies already wired:

- **Android:** AppCompat, Material 3, RecyclerView, ConstraintLayout, SwipeRefreshLayout,
  Navigation Component, Lifecycle ViewModel/LiveData, Retrofit 3, converter-gson, Gson,
  OkHttp, ViewBinding, `BuildConfig.API_BASE_URL`.
- **API:** Spring Web, Spring JDBC, Actuator, Validation, SQLite JDBC.
- **Tooling:** Gradle wrapper 9.7.1, Java 21 toolchain, Proto pin for Temurin 21,
  `.gitignore` for Android/SQLite/IDE files.

Spring Boot is used instead of Ktor because the server must be Java; Ktor's routing DSL is
Kotlin-first. The REST/SQLite role is unchanged.

## Requirements

- JDK 21 (the repo pins `temurin-21` in `.prototools`). If it is missing, Gradle downloads
  Temurin 21 automatically through the Foojay resolver on the first sync (network required).
- Android Studio (latest stable) with Android SDK 37.
- An emulator or a USB-connected Android device.
- Optional, if you use [Proto](https://moonrepo.dev/proto): `proto install` in this directory.

> Android Studio does not use Proto. Set **Gradle JDK** to 21:
> *Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK*.

## Run both processes (they must run at the same time)

### 1. Start the API — terminal 1

```bash
./gradlew :sembakoapi:bootRun
```

Verify it is up:

```bash
curl http://localhost:8080/actuator/health
# {"status":"UP"}
```

The SQLite file is created at `sembakoapi/sembako.db`. Delete it to start fresh.

### 2. Run the Android app — terminal 2 or Android Studio

**Android Studio (recommended for the team):**

1. Open the repository root (`toko-berkah-java`) in Android Studio.
2. Wait for Gradle sync; set Gradle JDK to 21 if prompted.
3. Select the **sembakoapp** run configuration.
4. Pick an emulator/device and press **Run**.

**Or from the terminal:**

```bash
./gradlew :sembakoapp:installDebug
adb shell am start -n com.example.sembakoapp/.MainActivity
```

## API URL: emulator vs physical device

| Target | API URL | Extra step |
|---|---|---|
| Android emulator | `http://10.0.2.2:8080/` (default) | none |
| USB device | `http://127.0.0.1:8080/` | `adb reverse tcp:8080 tcp:8080` |
| Wi-Fi device | `http://<your-computer-ip>:8080/` | same network, firewall open |

The default is in `gradle.properties` (`apiBaseUrl=http://10.0.2.2:8080/`).

For a **physical USB device**, add this to your **user-level** Gradle properties
(`~/.gradle/gradle.properties` on Linux/macOS, `%USERPROFILE%\.gradle\gradle.properties` on
Windows), then sync/run in Android Studio:

```properties
apiBaseUrl=http://127.0.0.1:8080/
```

Also run once per connection:

```bash
adb reverse tcp:8080 tcp:8080
```

Command-line alternative without editing user properties:

```bash
adb reverse tcp:8080 tcp:8080
./gradlew :sembakoapp:installDebug -PapiBaseUrl=http://127.0.0.1:8080/
```

`BuildConfig.API_BASE_URL` is generated from this value.

## API endpoints

No feature endpoints exist yet; the team will add them. Only Actuator is exposed:

- `GET /actuator/health`
- `GET /actuator/info`

Add controllers under `sembakoapi/src/main/java/com/example/sembakoapi/`.

## Database

- SQLite file: `sembakoapi/sembako.db` (gitignored).
- Connection settings: `sembakoapi/src/main/resources/application.properties`.
- No schema is created yet. The team can add `schema.sql` and set
  `spring.sql.init.mode=always` when the product/category/supplier tables are designed.

## Where the team adds code

```
sembakoapp/src/main/java/com/example/sembakoapp/     Android data/, network/, ui/
sembakoapp/src/main/res/layout/                     screens and list items
sembakoapi/src/main/java/com/example/sembakoapi/     controllers/, services/, repositories/
sembakoapi/src/main/resources/                       application.properties, schema.sql
```

Suggested split for the six features:

1. Dashboard summary (total products, low stock, total categories)
2. Product CRUD (+ optional image)
3. Category CRUD
4. Supplier CRUD
5. Product search & filter (category, supplier, stock range)
6. Low-stock warning

## Team workflow

- Create a branch per feature (`feat/products`, `feat/categories`, ...), open a PR.
- Never commit `local.properties`, `sembakoapi/*.db`, or IDE files — `.gitignore` covers them.
- Run `./gradlew :sembakoapi:build :sembakoapp:assembleDebug` before opening a PR.
- Keep the Android app in **Java**; the API is **Java/Spring Boot**.

## Troubleshooting

- **Port 8080 already in use:** stop the other process or change `server.port` in
  `application.properties` and `apiBaseUrl` to match.
- **App shows 404/connection error:** confirm the API is running and the URL matches the
  target (emulator vs device) as described above.
- **Gradle sync uses the wrong JDK / “Project source sets cannot be resolved”:** the project
  requests a Java 21 toolchain. The Foojay resolver downloads Temurin 21 automatically on sync;
  make sure the machine is online for the first sync. Alternatively set *Gradle JDK* to 21 in
  Android Studio.
- **Physical device cannot connect:** run `adb reverse tcp:8080 tcp:8080` again after
  reconnecting the cable.
