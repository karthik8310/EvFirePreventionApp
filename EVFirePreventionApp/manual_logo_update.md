# Manual App Logo Update Instructions

Since the automated update failed, follow these steps to manually set your new app logo.

## Prerequisites
You have the new logo file at:
`d:\mm\EVFirePreventionApp\app_logo_no_text.png`

## Steps

1.  **Open File Explorer** and navigate to your project's resource folder:
    `d:\mm\EVFirePreventionApp\app\src\main\res\`

2.  **Delete Adaptive Icons** (Important):
    *   Look for a folder named `mipmap-anydpi-v26` or `mipmap-anydpi`.
    *   **Delete this folder**.
    *   *Why?* This folder contains the default Android "adaptive icon" XML files. If you don't delete it, Android will keep showing the default green Android robot background even if you change the PNG files.

3.  **Replace Icon Files**:
    *   Copy your `app_logo_no_text.png` file.
    *   Go into **EACH** of the following folders inside `res`:
        *   `mipmap-mdpi`
        *   `mipmap-hdpi`
        *   `mipmap-xhdpi`
        *   `mipmap-xxhdpi`
        *   `mipmap-xxxhdpi`
    *   In **each** folder:
        1.  Paste `app_logo_no_text.png`.
        2.  Delete the existing `ic_launcher.png` and `ic_launcher_round.png`.
        3.  Rename `app_logo_no_text.png` to `ic_launcher.png`.
        4.  Paste `app_logo_no_text.png` again.
        5.  Rename this second copy to `ic_launcher_round.png`.

## Verification
1.  Rebuild your app in Android Studio (`Build > Rebuild Project`).
2.  Run the app on your emulator or device.
3.  You should now see your new blue shield logo!
