package com.example.gymtracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymtracker.ui.theme.GymTrackerTheme
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseInformationField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportText: String = ""
) {
    if (supportText == "") {
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
            modifier = modifier.semantics { contentDescription = label }
        )
    } else {
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
            supportingText = {
                Text(
                    text = supportText,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = modifier.semantics { contentDescription = label }
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

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
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
fun Calendar(
    month: Int,
    year: Int,
    activeDays: List<Int>,
    dayFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        DaysOfWeek()
        CalendarMonth(
            month = month,
            year = year,
            activeDays = activeDays,
            dayFunction = dayFunction
        )
    }
}

@Composable
fun DaysOfWeek(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        for (day in DayOfWeek.values()) {
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@Composable
fun CalendarMonth(
    month: Int,
    year: Int,
    activeDays: List<Int>,
    dayFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = YearMonth.of(year, month)
    val days = daysInMonth.lengthOfMonth()
    val firstDay = daysInMonth.atDay(1).dayOfWeek
    val lastDay = daysInMonth.atDay(days).dayOfWeek
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        CalendarWeek(
            startDate = 1,
            activeDays = activeDays,
            dayFunction = dayFunction,
            startDayOfWeek = firstDay
        )
        var currentDay = 7 - firstDay.value + 2
        while (currentDay < days - 7) {
            CalendarWeek(
                startDate = currentDay,
                activeDays = activeDays,
                dayFunction = dayFunction
            )
            currentDay += 7
        }
        CalendarWeek(
            startDate = currentDay,
            activeDays = activeDays,
            dayFunction = dayFunction,
            endDayOfWeek = lastDay
        )
    }
}

@Composable
fun CalendarWeek(
    startDate: Int,
    activeDays: List<Int>,
    dayFunction: (Int) -> Unit,
    modifier: Modifier = Modifier,
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    endDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        var dayValue = 1
        for (day in 1..7) {
            if (day >= startDayOfWeek.value && day <= endDayOfWeek.value) {
                val dayOfMonth = startDate + dayValue - 1
                Button(
                    modifier = Modifier
                        .padding(0.dp)
                        .width(0.dp)
                        .weight(1F)
                        .aspectRatio(1f),
                    colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(0.dp),
                    enabled = activeDays.contains(dayOfMonth),
                    onClick = { dayFunction(dayOfMonth) }
                ) {
                    Text(
                        text = dayOfMonth.toString(),
                        textAlign = TextAlign.Center
                    )
                }
                dayValue++
            } else {
                Spacer(modifier = Modifier.weight(1F))
            }
        }
    }
}

@Composable
fun MonthPicker(
    yearMonthValue: YearMonth,
    yearMonthValueOnChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    var pickMonth by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = yearMonthValue.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = yearMonthValue.year.toString())
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { pickMonth = true }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Change Month")
            }
        }
        if (pickMonth) {
            YearMonthOptions(
                yearMonthValue = yearMonthValue,
                yearMonthValueOnChange = yearMonthValueOnChange,
                closeFunction = { pickMonth = false }
            )
        }
    }
}

@Composable
fun YearMonthOptions(
    yearMonthValue: YearMonth,
    yearMonthValueOnChange: (YearMonth) -> Unit,
    closeFunction: () -> Unit
) {
    var year by remember { mutableStateOf(yearMonthValue.year) }
    val currentTime = YearMonth.now()
    Box {
        Card {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { year-- }) {
                        Icon(Icons.Default.ArrowBack, "Previous Year")
                    }
                    Text(text = year.toString())
                    IconButton(
                        onClick = { year++ },
                        enabled = year < YearMonth.now().year
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next Year",
                            modifier = Modifier.alpha(if (year < YearMonth.now().year) 1F else 0F)
                        )
                    }

                }
                for (monthRow in 1..10 step 3) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (monthColumn in 0..2) {
                            val month = Month.of(monthRow + monthColumn)
                            Button(
                                onClick = {
                                    yearMonthValueOnChange(YearMonth.of(year, month.value))
                                    closeFunction()
                                },
                                enabled = !(yearMonthValue.year == year && yearMonthValue.month == month)
                                        && currentTime.isAfter(YearMonth.of(year, month)),
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = if (yearMonthValue.year == year && yearMonthValue.month == month) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                        0.5F
                                    )
                                ),
                            ) {
                                Text(
                                    text = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                )
                            }
                        }
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-8).dp, 8.dp),
            onClick = { closeFunction() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
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
fun CalendarPreview() {
    GymTrackerTheme(darkTheme = false) {
        Calendar(
            month = 10,
            year = 2023,
            activeDays = listOf(),
            dayFunction = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MonthPickerPreview() {
    GymTrackerTheme(darkTheme = false) {
        MonthPicker(
            yearMonthValue = YearMonth.now(),
            yearMonthValueOnChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YearMonthOptionsPreview() {
    GymTrackerTheme(darkTheme = false) {
        YearMonthOptions(
            yearMonthValue = YearMonth.now(),
            yearMonthValueOnChange = { },
            closeFunction = { }
        )
    }
}
