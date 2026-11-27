# Building this branch

Minecraft 26.1.2, Kaleidoscope Cookery 1.7.0. Use JDK 25.

Place these compile-time integration libraries in `libs/`. Versions and filenames match `build.gradle`.

- `FarmersDelight-26.1.2-1.3.3.jar`
- `jade-mc26.1-neoforge-26.1.8.jar`
- `jei-26.1.2-neoforge-29.21.0.66.jar`
- `kubejs-neoforge-26.1.2-8.0.4.jar`
- `rhino-2101.2.8-build.91.jar`

Gradle resolves repository dependencies from the repositories configured in `build.gradle`.
Run `./gradlew build` on Linux or `gradlew.bat build` on Windows once the local libraries are in place.
The output is in `build-local/libs/`. The local libraries are separate from the mod JAR.
GitHub Actions runs the same build and needs the same local dependencies.
