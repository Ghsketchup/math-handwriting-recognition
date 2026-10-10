# TensorFlow Lite
-keep class org.tensorflow.** { *; }
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.**

# ONNX Runtime
-keep class com.microsoft.onnxruntime.** { *; }
-dontwarn com.microsoft.onnxruntime.**

# OpenCV
-keep class org.opencv.** { *; }
-dontwarn org.opencv.**

# Hilt
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Kotlin
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep your app classes
-keep class com.mathapp.** { *; }
