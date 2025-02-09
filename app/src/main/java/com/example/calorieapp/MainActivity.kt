package com.example.calorieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.tooling.preview.Preview
import com.example.calorieapp.ui.theme.CalorieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalorieApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalorieApp(modifier: Modifier = Modifier) {
    var weightInput by remember { mutableStateOf("") }
    val weight = weightInput.toIntOrNull() ?: 0
    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableFloatStateOf(1.3f) }
    var result by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading("Calories")
        WeightField(weightInput = weightInput, onValueChange = { weightInput = it })
        GenderChoices(male, setGenderMale = { male = it })
        IntensityList(onClick = { intensityValue: Float -> intensity = intensityValue })
        Text(
            text = "Calories: $result kcal",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Calculation(
            male = male,
            weight = weight,
            intensity = intensity,
            setResult = { result = it }
        )
    }
}

@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun WeightField(weightInput: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter weight (kg)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GenderChoices(male: Boolean, setGenderMale: (Boolean) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = male, onClick = { setGenderMale(true) })
            Text(text = "Male", modifier = Modifier.padding(start = 8.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = !male, onClick = { setGenderMale(false) })
            Text(text = "Female", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")

    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() },
            label = { Text("Select intensity") },
            trailingIcon = {
                Icon(imageVector = icon, contentDescription = "Select intensity", Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.forEach { item ->
                val intensity = when (item) {
                    "Light" -> 1.3f
                    "Usual" -> 1.5f
                    "Moderate" -> 1.7f
                    "Hard" -> 2.0f
                    "Very hard" -> 2.2f
                    else -> 0.0f
                }
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onClick(intensity)
                        selectedText = item
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun Calculation(male: Boolean, weight: Int, intensity: Float, setResult: (Int) -> Unit) {
    Button(
        onClick = {
            val calories = if (male) {
                ((879 + 10.2 * weight) * intensity).toInt()
            } else {
                ((795 + 7.18 * weight) * intensity).toInt()
            }
            setResult(calories)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "CALCULATE")
    }
}

@Preview(showBackground = true)
@Composable
fun CalorieAppPreview() {
    CalorieAppTheme {
        CalorieApp()
    }
}
