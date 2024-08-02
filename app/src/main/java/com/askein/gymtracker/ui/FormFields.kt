package com.askein.gymtracker.ui

import android.icu.text.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.details.toDate
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun FormInformationField(
    @StringRes label: Int,
    value: String,
    onChange: (String) -> Unit,
    formType: FormTypes,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    @StringRes errorMessage: Int = R.string.default_error
) {
    val labelString = stringResource(id = label)
    val decimalSeparator = stringResource(id = R.string.decimal_separator)
    TextField(
        value = if (formType == FormTypes.DOUBLE) value.replace(",", decimalSeparator) else value,
        onValueChange = { newValue ->
            when (formType) {
                FormTypes.STRING -> {
                    onChange(newValue)
                }

                FormTypes.DOUBLE -> {
                    if (newValue.matches(Regex(formType.regexPattern))) {
                        onChange(newValue)
                    }
                }

                else -> {
                    if (newValue.matches(Regex(formType.regexPattern))) {
                        onChange(newValue.replace(",", "."))
                    }
                }
            }
        },
        label = {
            Text(text = labelString)
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary
        ),
        isError = error,
        supportingText = {
            if (error) {
                Text(
                    text = stringResource(id = errorMessage),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = formType.keyboardOptions,
        modifier = modifier.semantics { contentDescription = labelString }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInformationFieldWithSuggestions(
    @StringRes label: Int,
    value: TextFieldValue,
    onChange: (TextFieldValue) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val labelString = stringResource(id = label)
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
                Text(text = labelString)
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier
                .semantics { contentDescription = labelString }
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
        FormInformationField(
            label = R.string.minutes,
            value = minutes,
            onChange = minutesOnChange,
            formType = FormTypes.INTEGER,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
        )
        FormInformationField(
            label = R.string.seconds,
            value = seconds,
            onChange = secondsOnChange,
            formType = FormTypes.INTEGER,
            error = secondsError,
            errorMessage = R.string.seconds_error,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    @StringRes options: List<Int>,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selected: Int? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableIntStateOf(selected ?: options.first()) }
    LaunchedEffect(selected) {
        selected?.let { newValue ->
            selectedOption = newValue
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = stringResource(id = selectedOption),
            onValueChange = { onChange(selectedOption) },
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary
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
                            text = stringResource(id = item),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    options: Map<DistanceUnits, Int>,
    onChange: (DistanceUnits) -> Unit,
    modifier: Modifier = Modifier,
    selected: DistanceUnits? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(selected ?: DistanceUnits.KILOMETERS) }
    LaunchedEffect(selected) {
        selected?.let { newValue ->
            selectedOption = newValue
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = stringResource(id = options[selectedOption]!!),
            onValueChange = { onChange(selectedOption) },
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary
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
                            text = stringResource(id = item.value),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    onClick = {
                        selectedOption = item.key
                        expanded = false
                        onChange(item.key)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    options: Map<WeightUnits, Int>,
    onChange: (WeightUnits) -> Unit,
    modifier: Modifier = Modifier,
    selected: WeightUnits? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(selected ?: WeightUnits.KILOGRAMS) }
    LaunchedEffect(selected) {
        selected?.let { newValue ->
            selectedOption = newValue
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            value = stringResource(id = options[selectedOption]!!),
            onValueChange = { onChange(selectedOption) },
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary
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
                            text = stringResource(id = item.value),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    onClick = {
                        selectedOption = item.key
                        expanded = false
                        onChange(item.key)
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
    Box {
        Card(
            modifier = modifier
                .padding(vertical = 10.dp, horizontal = 10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
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
                        Text(text = stringResource(id = R.string.yes))
                    }
                    Button(
                        onClick = { cancelFunction() }
                    ) {
                        Text(text = stringResource(id = R.string.no))
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
                contentDescription = stringResource(id = R.string.close)
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val customFormatter = DateFormat.getDateInstance(DateFormat.SHORT, LocalConfiguration.current.locales[0])

    Box(contentAlignment = Alignment.Center) {
        Button(onClick = { showDatePicker = true }) {
            Text(text = customFormatter.format(date.toDate()))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            date = date,
            onDateChange = onDateChange,
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.toEpochMillis(),
        selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis < LocalDate.now().plusDays(1).toEpochMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    } ?: LocalDate.now()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateChange(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

fun LocalDate.toEpochMillis(): Long {
    return this.toEpochDay() * 86400000L
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
            label = R.string.minutes,
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
