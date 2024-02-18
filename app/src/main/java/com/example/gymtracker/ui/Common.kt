package com.example.gymtracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.gymtracker.ui.theme.GymTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInformationField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    errorMessage: String = ""
) {
    TextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(text = label)
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        isError = error,
        supportingText = {
            if (error) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier.semantics { contentDescription = label }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInformationFieldWithSuggestions(
    label: String,
    value: TextFieldValue,
    onChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onChange,
            label = {
                Text(text = label)
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .semantics { contentDescription = label }
                .menuAnchor()
        )
        val valueText = value.text
        if (valueText.length >= 2) {
            val possible =
                suggestions.sorted().filter { it.startsWith(valueText, ignoreCase = true) }
            if (possible.isNotEmpty()) {
                DropdownMenu(
                    expanded = expanded,
                    properties = PopupProperties(focusable = false),
                    onDismissRequest = { expanded = false }
                ) {
                    possible.take(3).forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                            onClick = {
                                onChange(
                                    TextFieldValue(
                                        text = item,
                                        selection = TextRange(item.length)
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTimeField(
    minutes: String,
    seconds: String,
    minutesOnChange: (String) -> Unit,
    secondsOnChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val secondsError = seconds != "" && seconds.toInt() >= 60
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        TextField(
            value = minutes,
            onValueChange = minutesOnChange,
            label = {
                Text(text = "Minutes")
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.semantics { contentDescription = "Minutes" }
                .weight(1f)
                .padding(0.dp)
        )
        TextField(
            value = seconds,
            onValueChange = secondsOnChange,
            label = {
                Text(text = "Seconds")
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = secondsError,
            supportingText = {
                if (secondsError) {
                    Text(
                        text = "Value must be between 0 and 59",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.semantics { contentDescription = "Seconds" }
                .weight(1f)
                .padding(0.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    options: List<String>,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { onChange(selectedOption) },
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    onClick = {
                        selectedOption = item
                        expanded = false
                        onChange(item)
                    }
                )
            }
        }
    }
}

@Composable
fun ActionConfirmation(
    actionTitle: String,
    confirmFunction: () -> Unit,
    cancelFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customCardElevation = CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = customCardElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = actionTitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { confirmFunction() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Yes")
                    }
                    Button(
                        onClick = { cancelFunction() }
                    ) {
                        Text(text = "No")
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, 8.dp),
            onClick = { cancelFunction() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}

@Composable
fun customCardElevation(): CardElevation {
    return CardDefaults.cardElevation(
        defaultElevation = 16.dp
    )
}

@Preview(showBackground = true)
@Composable
fun ActionConfirmationPreview() {
    GymTrackerTheme(darkTheme = false) {
        ActionConfirmation(
            actionTitle = "Delete Exercise?",
            confirmFunction = { },
            cancelFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormInformationFieldWithSuggestionsPreview() {
    GymTrackerTheme(darkTheme = false) {
        FormInformationFieldWithSuggestions(
            label = "Test Field",
            value = TextFieldValue(text = "Bi"),
            onChange = { },
            suggestions = listOf("Biceps", "Bicycle", "Bismuth")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormTimeFieldPreview() {
    GymTrackerTheme(darkTheme = false) {
        FormTimeField(
            minutes = "",
            seconds = "",
            minutesOnChange = { },
            secondsOnChange = { }
        )
    }
}
