package com.mathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun GraphScreen() {
    var equation by remember { mutableStateOf("sin(x)") }
    var minX by remember { mutableStateOf("-2π") }
    var maxX by remember { mutableStateOf("2π") }
    var showGrid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            "رسم الدوال",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 20.sp,
            color = Color.Black
        )

        // Input Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("إدخال الدالة", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = equation,
                    onValueChange = { equation = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("الدالة: f(x) = ") },
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = minX,
                        onValueChange = { minX = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("من") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = maxX,
                        onValueChange = { maxX = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("إلى") },
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = showGrid,
                        onCheckedChange = { showGrid = it }
                    )
                    Text("إظهار الشبكة")
                }
            }
        }

        // Graph Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            GraphCanvas(
                equation = equation,
                showGrid = showGrid,
                minX = -2 * PI,
                maxX = 2 * PI,
                minY = -2f,
                maxY = 2f
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Analysis Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("تحليل الدالة", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                
                AnalysisItem("المجال", "(-∞, ∞)")
                AnalysisItem("المدى", "[-1, 1]")
                AnalysisItem("الدورة", "2π")
                AnalysisItem("النوع", "دالة دورية")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Download, contentDescription = "Export", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("تصدير")
            }

            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Analyze", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("تحليل")
            }
        }
    }
}

@Composable
fun AnalysisItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun GraphCanvas(
    equation: String,
    showGrid: Boolean,
    minX: Double,
    maxX: Double,
    minY: Float,
    maxY: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Simple graph representation
        Text(
            "رسم الدالة: $equation",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
