// MainActivity.kt
package com.example.projetoconversor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoconversor.ui.theme.ProjetoconversorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetoconversorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Conversor()
                }
            }
        }
    }
}

@Composable
fun Conversor() {
    var value by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Metros") }
    var toUnit by remember { mutableStateOf("Quilômetros") }

    val units = listOf("Metros", "Quilômetros", "Milhas", "Jardas")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Conversor de Unidades", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        UnitDropdownMenu(
            label = "De",
            selectedUnit = fromUnit,
            onUnitSelected = { fromUnit = it },
            units = units
        )

        Spacer(modifier = Modifier.height(8.dp))

        UnitDropdownMenu(
            label = "Para",
            selectedUnit = toUnit,
            onUnitSelected = { toUnit = it },
            units = units
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Digite o valor") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            result = convertLength(value.toDoubleOrNull(), fromUnit, toUnit).toString()
        }) {
            Text("Converter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Resultado: $result", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun UnitDropdownMenu(
    label: String,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit,
    units: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedUnit)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(text = unit) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertLength(value: Double?, fromUnit: String, toUnit: String): Double {
    if (value == null) return 0.0

    val meters = when (fromUnit) {
        "Metros" -> value
        "Quilômetros" -> value * 1000
        "Milhas" -> value * 1609.34
        "Jardas" -> value * 0.9144
        else -> value
    }

    return when (toUnit) {
        "Metros" -> meters
        "Quilômetros" -> meters / 1000
        "Milhas" -> meters / 1609.34
        "Jardas" -> meters / 0.9144
        else -> meters
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversor() {
    ProjetoconversorTheme {
        Conversor()
    }
}
