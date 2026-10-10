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

@Composable
fun SettingsScreen() {
    var brightness by remember { mutableStateOf(0.5f) }
    var brushSize by remember { mutableStateOf(3f) }
    var enableDarkMode by remember { mutableStateOf(false) }
    var autoSave by remember { mutableStateOf(true) }
    var showGridByDefault by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            "الإعدادات",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 20.sp,
            color = Color.Black
        )

        // Display Settings
        SettingsSection(title = "العرض") {
            SettingItem(
                title = "السطوع",
                icon = Icons.Filled.Brightness4
            ) {
                Slider(
                    value = brightness,
                    onValueChange = { brightness = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SettingToggle(
                title = "الوضع الليلي",
                checked = enableDarkMode,
                onCheckedChange = { enableDarkMode = it },
                icon = Icons.Filled.DarkMode
            )
        }

        // Drawing Settings
        SettingsSection(title = "الرسم") {
            SettingItem(
                title = "حجم الفرشاة",
                icon = Icons.Filled.Edit
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = brushSize,
                        onValueChange = { brushSize = it },
                        valueRange = 1f..10f,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "${brushSize.toInt()}",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // General Settings
        SettingsSection(title = "عام") {
            SettingToggle(
                title = "الحفظ التلقائي",
                checked = autoSave,
                onCheckedChange = { autoSave = it },
                icon = Icons.Filled.SaveAs
            )

            SettingToggle(
                title = "إظهار الشبكة افتراضياً",
                checked = showGridByDefault,
                onCheckedChange = { showGridByDefault = it },
                icon = Icons.Filled.GridView
            )
        }

        // About Section
        SettingsSection(title = "حول التطبيق") {
            SettingItem(
                title = "الإصدار",
                icon = Icons.Filled.Info,
                value = "1.0.0"
            ) {}

            SettingItem(
                title = "حقوق الطبع والنشر",
                icon = Icons.Filled.Copyright,
                value = "2024 © جميع الحقوق محفوظة"
            ) {}
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            title,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: androidx.compose.material.icons.materialIcon = Icons.Filled.Settings,
    value: String = "",
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = title, tint = Color(0xFF2196F3))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, color = Color.Black)
                if (value.isNotEmpty()) {
                    Text(value, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        if (value.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: androidx.compose.material.icons.materialIcon
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = title, tint = Color(0xFF2196F3))
            Text(title, fontSize = 14.sp, color = Color.Black)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
