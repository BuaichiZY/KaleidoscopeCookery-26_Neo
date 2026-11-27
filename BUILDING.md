# Building this branch

Minecraft 26.3, Kaleidoscope Cookery 1.7.0. Use JDK 25.

Place these compile-time integration libraries in `libs/`. Versions and filenames match `build.gradle`.

- `FarmersDelight-26.3-1.3.3-fix4.jar`
- `Jade-mc26.3-NeoForge-26.3.1.jar`
- `jei-26.3-neoforge-31.0.0.5.jar`

Gradle resolves repository dependencies from the repositories configured in `build.gradle`.
Run `./gradlew build` on Linux or `gradlew.bat build` on Windows once the local libraries are in place.
The output is in `build-local/libs/`. The local libraries are separate from the mod JAR.
GitHub Actions runs the same build and needs the same local dependencies.
