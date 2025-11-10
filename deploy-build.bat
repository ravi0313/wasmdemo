@echo off
setlocal enabledelayedexpansion

echo ========================================
echo WasmDemo Deployment Build Script
echo ========================================
echo.

:: Get current date for version
for /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
echo Current date version: %mydate%
echo.

echo Step 1: Cleaning previous build...
call gradlew.bat clean
if errorlevel 1 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)
echo Clean complete.
echo.

echo Step 2: Building production distribution...
call gradlew.bat wasmJsBrowserDistribution
if errorlevel 1 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)
echo Build complete!
echo.

echo ========================================
echo Build Successful!
echo ========================================
echo.
echo Distribution files are located at:
echo   composeApp\build\dist\wasmJs\productionExecutable\
echo.
echo IMPORTANT NEXT STEPS:
echo ========================================
echo 1. Update version in index.html to: ?v=%mydate%
echo    Location: composeApp\src\webMain\resources\index.html
echo.
echo 2. Rebuild after updating version:
echo    gradlew.bat wasmJsBrowserDistribution
echo.
echo 3. Copy ALL files from productionExecutable\ to your gh-pages branch:
echo    - index.html
echo    - composeApp.js
echo    - *.wasm files
echo    - styles.css
echo    - composeResources\ folder
echo    - .nojekyll file
echo.
echo 4. Commit and push to gh-pages branch:
echo    git checkout gh-pages
echo    git add .
echo    git commit -m "Deploy: Update %mydate%"
echo    git push origin gh-pages
echo.
echo 5. Wait 5-10 minutes for GitHub Pages to update
echo.
echo 6. Hard refresh browser: Ctrl+F5 or Cmd+Shift+R
echo.
echo ========================================

pause

