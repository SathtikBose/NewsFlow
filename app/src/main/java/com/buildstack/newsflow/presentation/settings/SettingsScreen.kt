package com.buildstack.newsflow.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buildstack.newsflow.ui.components.glass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 24.dp, top = 24.dp)
        )

        // Theme Mode Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .glass()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Theme Mode",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val themeOptions = listOf("SYSTEM", "LIGHT", "DARK")
                themeOptions.forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.themeMode == mode,
                            onClick = { viewModel.setThemeMode(mode) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(
                            text = mode.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Font Size Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .glass()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Text Scale",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "${(uiState.fontScale * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Slider(
                    value = uiState.fontScale,
                    onValueChange = { viewModel.setFontScale(it) },
                    valueRange = 0.8f..1.4f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Smaller", style = MaterialTheme.typography.labelSmall)
                    Text("Larger", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // App Info
        Text(
            text = "NewsFlow v1.0.0",
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )
    }
}
