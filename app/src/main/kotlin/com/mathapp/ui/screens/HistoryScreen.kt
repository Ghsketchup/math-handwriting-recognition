package com.mathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HistoryItem(
    val id: String,
    val equation: String,
    val latex: String,
    val timestamp: String
)

@Composable
fun HistoryScreen() {
    var historyItems by remember { 
        mutableStateOf(listOf(
            HistoryItem("1", "5x + 3 = 0", "5x + 3 = 0", "2024-01-15 10:30"),
            HistoryItem("2", "x² - 4x + 3 = 0", "x^2 - 4x + 3 = 0", "2024-01-15 10:25"),
            HistoryItem("3", "sin(x) + cos(x) = 1", "\\sin(x) + \\cos(x) = 1", "2024-01-15 10:20")
        ))
    }
    var selectedItem by remember { mutableStateOf<HistoryItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title
        Text(
            "السجل",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 20.sp,
            color = Color.Black
        )

        // History List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historyItems) { item ->
                HistoryItemCard(
                    item = item,
                    onDelete = {
                        historyItems = historyItems.filter { it.id != item.id }
                    },
                    onSelect = {
                        selectedItem = item
                    }
                )
            }
        }

        // Action Buttons
        if (historyItems.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { historyItems = emptyList() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Icon(Icons.Filled.DeleteSweep, contentDescription = "Clear All", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("مسح الكل")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Export", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تصدير")
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    item: HistoryItem,
    onDelete: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(item.equation, fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.latex, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.timestamp, fontSize = 10.sp, color = Color.LightGray)
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onSelect) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color(0xFF2196F3))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFF44336))
                }
            }
        }
    }
}
