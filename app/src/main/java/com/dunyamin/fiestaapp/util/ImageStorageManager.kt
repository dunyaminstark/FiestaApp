package com.dunyamin.fiestaapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

/**
 * Utility class to handle image storage operations.
 * This class provides methods for saving images to internal storage and retrieving them.
 */
class ImageStorageManager(private val context: Context) {

    companion object {
        private const val TAG = "ImageStorageManager"
        private const val IMAGE_DIRECTORY = "holiday_images"
    }

    /**
     * Save an image from a content URI to internal storage.
     *
     * @param sourceUri The content URI of the image to save
     * @return The URI of the saved image in internal storage, or null if saving failed
     */
    fun saveImageToInternalStorage(sourceUri: Uri): Uri? {
        try {
            // Create a unique filename for the image
            val filename = "holiday_image_${UUID.randomUUID()}.jpg"
            
            // Get the input stream from the content URI
            val inputStream = context.contentResolver.openInputStream(sourceUri)
            
            // Decode the input stream to a bitmap
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            // Create the directory if it doesn't exist
            val directory = File(context.filesDir, IMAGE_DIRECTORY)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            
            // Create the output file
            val file = File(directory, filename)
            
            // Write the bitmap to the output file
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            
            Log.d(TAG, "Image saved to internal storage: ${file.absolutePath}")
            
            // Return the URI of the saved file
            return Uri.fromFile(file)
        } catch (e: IOException) {
            Log.e(TAG, "Error saving image to internal storage", e)
            return null
        }
    }

    /**
     * Delete an image from internal storage.
     *
     * @param imageUri The URI of the image to delete
     * @return True if the image was deleted successfully, false otherwise
     */
    fun deleteImageFromInternalStorage(imageUri: Uri): Boolean {
        try {
            val path = imageUri.path ?: return false
            val file = File(path)
            
            if (file.exists()) {
                val deleted = file.delete()
                Log.d(TAG, "Image deleted from internal storage: $deleted")
                return deleted
            }
            
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image from internal storage", e)
            return false
        }
    }

    /**
     * Check if a URI points to an image in internal storage.
     *
     * @param imageUri The URI to check
     * @return True if the URI points to an image in internal storage, false otherwise
     */
    fun isInternalStorageUri(imageUri: Uri): Boolean {
        val path = imageUri.path ?: return false
        val directory = File(context.filesDir, IMAGE_DIRECTORY).absolutePath
        return path.startsWith(directory)
    }
}

/**
 * Composable function to get the ImageStorageManager instance.
 */
@Composable
fun rememberImageStorageManager(): ImageStorageManager {
    val context = LocalContext.current
    return ImageStorageManager(context)
}