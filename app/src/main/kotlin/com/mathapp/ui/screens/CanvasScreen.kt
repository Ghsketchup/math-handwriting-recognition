package com.mathapp.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mathapp.math.MathParser
import com.mathapp.math.LaTeXGenerator
import kotlinx.coroutines.launch

data class Stroke(
    val points: MutableList<Offset> = mutableListOf(),
    val paint: Paint = Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 3f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
)

@Composable
fun CanvasScreen(navController: NavController) {
    var strokes by remember { mutableStateOf(listOf<Stroke>()) }
    var currentStroke by remember { mutableStateOf<Stroke?>(null) }
    var recognizedText by remember { mutableStateOf("") }
    var latex by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title
        Text(
            "رسم المعادلة الرياضية",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 20.sp,
            color = Color.Black
        )

        // Canvas Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color(0xFFF5F5F5))
                .border(2.dp, Color.Gray)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke = Stroke(mutableListOf(offset))
                        },
                        onDrag = { change: PointerInputChange, dragAmount ->
                            change.consume()
                            currentStroke?.let {
                                it.points.add(change.position)
                            }
                        },
                        onDragEnd = {
                            currentStroke?.let {
                                strokes = strokes + it
                                currentStroke = null
                            }
                        }
                    )
                }
                .drawBehind {
                    drawStrokes(strokes, currentStroke)
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Results Section
        if (recognizedText.isNotEmpty() || latex.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("النتيجة المعترف بها:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(recognizedText, fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("صيغة LaTeX:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(latex, fontSize = 14.sp, color = Color(0xFF2196F3))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    strokes = emptyList()
                    currentStroke = null
                    recognizedText = ""
                    latex = ""
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("مسح")
            }

            Button(
                onClick = {
                    if (strokes.isNotEmpty()) {
                        isProcessing = true
                        scope.launch {
                            try {
                                val parser = MathParser()
                                val generator = LaTeXGenerator()
                                
                                // Simulate recognition
                                recognizedText = "5x + 3 = 0"
                                latex = "5x + 3 = 0"
                                
                            } catch (e: Exception) {
                                recognizedText = "خطأ: ${e.message}"
                            } finally {
                                isProcessing = false
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = strokes.isNotEmpty() && !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.Check, contentDescription = "Recognize", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("التعرف")
                }
            }

            Button(
                onClick = {
                    // TODO: Save to graph screen
                },
                modifier = Modifier.weight(1f),
                enabled = latex.isNotEmpty()
            ) {
                Icon(Icons.Filled.Bookmark, contentDescription = "Save", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("حفظ")
            }
        }
    }
}

fun DrawScope.drawStrokes(strokes: List<Stroke>, currentStroke: Stroke?) {
    // Draw all completed strokes
    strokes.forEach { stroke ->
        for (i in 0 until stroke.points.size - 1) {
            val start = stroke.points[i]
            val end = stroke.points[i + 1]
            drawLine(
                color = Color.Black,
                start = start,
                end = end,
                strokeWidth = 3.dp.toPx()
            )
        }
    }

    // Draw current stroke being drawn
    currentStroke?.let { stroke ->
        for (i in 0 until stroke.points.size - 1) {
            val start = stroke.points[i]
            val end = stroke.points[i + 1]
            drawLine(
                color = Color(0xFF2196F3),
                start = start,
                end = end,
                strokeWidth = 3.dp.toPx()
            )
        }
    }
}

fun Modifier.border(width: dp, color: Color) = this.then(
    Modifier.drawBehind {
        val strokeWidth = width.toPx()
        drawRect(
            color = color,
            style = Stroke(strokeWidth),
            size = size
        )
    }
)
