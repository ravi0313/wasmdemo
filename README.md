This is a Kotlin Multiplatform project targeting Web.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:

- for the Wasm target (faster, modern browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
      ```
- for the JS target (slower, supports older browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:jsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
      ```

### Build for Production (GitHub Pages)

To build the production version for deployment:

```shell
.\gradlew.bat wasmJsBrowserDistribution
```

The production files will be in: `composeApp\build\dist\wasmJs\productionExecutable\`

### 🚀 Quick Deploy to GitHub Pages

**Option 1: Use the Quick Fix Script**
```shell
.\quick-fix-cache.bat
```

**Option 2: Manual Steps**
1. Build production: `.\gradlew.bat clean wasmJsBrowserDistribution`
2. Copy files from `composeApp\build\dist\wasmJs\productionExecutable\` to gh-pages branch
3. Commit and push to gh-pages branch
4. Wait 5-10 minutes, then hard refresh (Ctrl+F5)

**Option 3: Automatic Deploy with GitHub Actions**
- Push to main/master branch
- GitHub Actions will automatically build and deploy
- See `.github/workflows/deploy.yml`

### ⚠️ Cache Issues?

If you see old code after deploying:
1. The `index.html` now includes cache-busting with version parameters
2. Update the version in `composeApp/src/webMain/resources/index.html` before each deploy
3. Clear browser cache: Ctrl+Shift+Delete
4. Hard refresh: Ctrl+F5
5. Test in incognito mode

**See [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) for detailed instructions.**

### 📝 Login Credentials

- Username: `admin`
- Password: `admin`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).