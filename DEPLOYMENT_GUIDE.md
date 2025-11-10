# GitHub Pages Deployment Guide for WasmDemo

## Issue: Old Code Showing After Deploy

This guide explains how to properly deploy updates to GitHub Pages and ensure the latest changes are visible.

## Root Causes
1. **Browser Caching**: Browsers cache JavaScript and WASM files aggressively
2. **GitHub Pages Caching**: GitHub's CDN caches static files
3. **Service Workers**: If you had a service worker, it might cache old files

## Solution Implemented

### 1. Cache-Busting in index.html
The `index.html` now includes:
- Cache control meta tags to prevent browser caching
- Version query parameters on JS/CSS files (update the version number with each deploy)

### 2. .nojekyll File
Added `.nojekyll` file to prevent GitHub Pages from ignoring certain files.

## Deployment Steps

### Step 1: Build Production Files
```bash
# Clean previous builds
gradlew.bat clean

# Build production distribution
gradlew.bat wasmJsBrowserDistribution
```

### Step 2: Update Version Number (IMPORTANT!)
Before each deployment, update the version in `composeApp/src/webMain/resources/index.html`:
```html
<link type="text/css" rel="stylesheet" href="styles.css?v=YYYYMMDD">
<script type="application/javascript" src="composeApp.js?v=YYYYMMDD"></script>
```
Change `YYYYMMDD` to current date or increment a version number.

### Step 3: Copy Distribution Files
```bash
# The production files are in:
composeApp\build\dist\wasmJs\productionExecutable\

# Copy ALL files from this directory to your gh-pages branch or docs folder
```

### Step 4: Commit and Push
```bash
# If using gh-pages branch:
git checkout gh-pages
# Copy files to gh-pages branch
git add .
git commit -m "Deploy: Updated with latest changes - [DATE]"
git push origin gh-pages

# If using docs folder in main:
git add docs/
git commit -m "Deploy: Updated with latest changes - [DATE]"
git push origin main
```

### Step 5: Force Refresh on GitHub Pages
1. Go to your repository on GitHub
2. Navigate to Settings → Pages
3. Temporarily change the source branch to "None"
4. Save and wait 1 minute
5. Change it back to your deployment branch
6. Save again

### Step 6: Clear Browser Cache
After deployment, users need to clear cache:
- **Hard Refresh**: Ctrl + F5 (Windows) or Cmd + Shift + R (Mac)
- **Clear Cache**: Browser settings → Clear browsing data → Cached images and files
- **Incognito/Private Window**: Test in a new incognito window

## Quick Fix Commands

### Full Rebuild and Prepare for Deploy
```bash
# Clean everything
gradlew.bat clean

# Build production
gradlew.bat wasmJsBrowserDistribution

# The files will be in:
# composeApp\build\dist\wasmJs\productionExecutable\
```

### For Future Deployments

Create a deployment script `deploy.bat`:
```batch
@echo off
echo Cleaning previous build...
call gradlew.bat clean

echo Building production distribution...
call gradlew.bat wasmJsBrowserDistribution

echo Build complete!
echo Files are in: composeApp\build\dist\wasmJs\productionExecutable\
echo.
echo IMPORTANT: Update version in index.html before copying to gh-pages!
echo Current version format: ?v=20251110
echo.
pause
```

## Verification

After deployment, verify the changes:

1. **Check Build Date**: Add a console log or version display in your app
2. **Inspect Network**: Open Browser DevTools → Network tab → Check if new files are loaded
3. **Check Response Headers**: Verify cache headers are set correctly
4. **Test Incognito**: Always test in an incognito/private window first

## Alternative: Using GitHub Actions for Auto-Deploy

Create `.github/workflows/deploy.yml`:
```yaml
name: Deploy to GitHub Pages

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'
          
      - name: Build
        run: ./gradlew wasmJsBrowserDistribution
        
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./composeApp/build/dist/wasmJs/productionExecutable
          force_orphan: true
```

## Troubleshooting

### Still Seeing Old Code?

1. **Wait 10-15 minutes**: GitHub Pages CDN takes time to update
2. **Check commit hash**: Verify your latest commit is on the deployment branch
3. **Verify files**: Check if the files in gh-pages branch are actually updated
4. **CDN Purge**: Try accessing `https://yourusername.github.io/wasmdemo/?nocache=1`
5. **Different Browser**: Test in a completely different browser

### JavaScript Errors?

1. Check browser console for errors
2. Verify all WASM files were copied correctly
3. Ensure file paths are correct in index.html

## Best Practices

1. **Always increment version**: Change `?v=YYYYMMDD` with each deploy
2. **Test locally first**: Run `wasmJsBrowserDevelopmentRun` before deploying
3. **Use git tags**: Tag releases for easy rollback
4. **Keep gh-pages clean**: Only commit distribution files, no source code

## Current Status

- ✅ Cache control meta tags added
- ✅ .nojekyll file created
- ✅ Version query parameters added to resources
- ⚠️ You need to: Rebuild, update version, and redeploy

## Next Steps for You

1. Run: `gradlew.bat clean`
2. Run: `gradlew.bat wasmJsBrowserDistribution`
3. Update version in index.html (already set to ?v=20251110, increment for next deploy)
4. Copy files from `composeApp\build\dist\wasmJs\productionExecutable\` to gh-pages branch
5. Commit and push to GitHub
6. Wait 5-10 minutes, then hard refresh your browser (Ctrl+F5)

