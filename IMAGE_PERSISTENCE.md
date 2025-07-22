# Image Persistence in FiestaApp

This document outlines the changes made to ensure that custom images uploaded by users persist even after the app is closed and reopened.

## Problem

The original implementation had an issue where custom images uploaded by users would not persist after the app was closed and reopened. This was because:

1. While the app correctly saved the custom image URI to SharedPreferences, it wasn't properly reloading this data when the app restarted.
2. The holidays were only loaded once when the MainScreen was first composed, and subsequent updates were only made to the in-memory list.

## Solution

The following changes were made to ensure that custom images persist:

1. **Added DisposableEffect to MainScreen**:
   - Added a DisposableEffect that reloads holidays from the repository whenever the component is composed or the app is restarted.
   - This ensures that the latest data, including custom image URIs, is always loaded from SharedPreferences.

2. **Created a loadHolidays Function**:
   - Extracted the holiday loading logic into a separate function that can be called whenever we need to refresh the data.
   - This function loads holidays from the repository with the latest custom image URIs.

3. **Updated the onSave Callback**:
   - Modified the onSave callback in the EditHolidayDialog to call the loadHolidays function after saving the updated holiday.
   - This ensures that the UI immediately reflects the changes, including any new custom images.

## Implementation Details

The key changes were made in the following files:

1. **MainScreen.kt**:
   - Added DisposableEffect to reload holidays when the app is restarted
   - Created a loadHolidays function to centralize the loading logic
   - Updated the onSave callback to reload holidays after saving

## Benefits

- **Persistent Custom Images**: Custom images now persist even after the app is closed and reopened.
- **Consistent Data**: The app always displays the latest data from SharedPreferences.
- **Improved User Experience**: Users can upload custom images and see them consistently across app sessions.

## Technical Details

The implementation uses Android's SharedPreferences for storing the custom image URIs. When a user selects a custom image:

1. The URI of the selected image is saved to SharedPreferences using the HolidayPreferences class.
2. The HolidayImage component checks if a custom image URI exists and displays it using the Coil library's AsyncImage.
3. When the app is restarted, the DisposableEffect in MainScreen ensures that the holidays are reloaded from SharedPreferences, including any custom image URIs.

This approach ensures that custom images persist across app sessions while maintaining a clean separation of concerns between the UI and data layers.