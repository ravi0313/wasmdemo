@echo off
echo ========================================
echo QUICK FIX: Cache Busting Update
echo ========================================
echo.
echo This script will:
echo 1. Clean the build
echo 2. Rebuild with cache-busting enabled
echo 3. Prepare files for deployment
echo.
pause

:: Clean build
echo Cleaning previous build...
call gradlew.bat clean

:: Rebuild
echo Building production distribution...
call gradlew.bat wasmJsBrowserDistribution

echo.
echo ========================================
echo SUCCESS!
echo ========================================
echo.
echo Your updated files are ready in:
echo   composeApp\build\dist\wasmJs\productionExecutable\
echo.
echo The index.html now includes:
echo   - Cache control headers
echo   - Version query parameters: ?v=20251110
echo   - .nojekyll file for GitHub Pages
echo.
echo NEXT: Copy these files to your GitHub Pages branch
echo.
echo Quick Deploy Commands:
echo ----------------------------------------
echo cd composeApp\build\dist\wasmJs\productionExecutable
echo git init
echo git add .
echo git commit -m "Deploy with cache fix"
echo git push -f origin gh-pages
echo ----------------------------------------
echo.
echo After deploying:
echo 1. Wait 5-10 minutes for GitHub Pages CDN
echo 2. Clear your browser cache (Ctrl+Shift+Delete)
echo 3. Hard refresh the page (Ctrl+F5)
echo 4. Or test in incognito mode
echo.
pause

