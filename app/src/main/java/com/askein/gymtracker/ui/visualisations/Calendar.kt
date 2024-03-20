package com.askein.gymtracker.ui.visualisations

import android.icu.text.DateFormat
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.theme.GymTrackerTheme
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date


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
                text = day.getDisplayName(TextStyle.SHORT, LocalConfiguration.current.locales[0]),
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
            val monthEndDate = Date.from(yearMonthValue.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant())
            val monthYearString = DateFormat.getPatternInstance(DateFormat.YEAR_MONTH, LocalConfiguration.current.locales[0]).format(monthEndDate)
            Text(text = monthYearString)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = { pickMonth = !pickMonth }) {
                Icon(
                    imageVector = if (pickMonth) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.change_month)
                )
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
    var year by remember { mutableIntStateOf(yearMonthValue.year) }
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
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.previous_year)
                        )
                    }
                    Text(text = year.toString())
                    IconButton(
                        onClick = { year++ },
                        enabled = year < YearMonth.now().year
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.next_year),
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
                            val yearMonthOption = YearMonth.of(year, monthRow + monthColumn)
                            val currentlySelected = yearMonthValue == yearMonthOption
                            Button(
                                onClick = {
                                    yearMonthValueOnChange(yearMonthOption)
                                    closeFunction()
                                },
                                enabled = !currentlySelected
                                        && currentTime >= yearMonthOption,
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = if (currentlySelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                        0.5F
                                    )
                                ),
                            ) {
                                Text(
                                    text = yearMonthOption.month.getDisplayName(TextStyle.SHORT, LocalConfiguration.current.locales[0])
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
                contentDescription = stringResource(id = R.string.close)
            )
        }
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

@Preview(locale = "es", showBackground = true)
@Composable
fun CalendarPreviewSpanish() {
    GymTrackerTheme(darkTheme = false) {
        Calendar(
            month = 10,
            year = 2023,
            activeDays = listOf(),
            dayFunction = { }
        )
    }
}

@Preview(locale = "en", showBackground = true)
@Composable
fun MonthPickerPreview() {
    GymTrackerTheme(darkTheme = false) {
        MonthPicker(
            yearMonthValue = YearMonth.now(),
            yearMonthValueOnChange = { }
        )
    }
}

@Preview(locale = "de", showBackground = true)
@Composable
fun MonthPickerPreviewGerman() {
    GymTrackerTheme(darkTheme = false) {
        MonthPicker(
            yearMonthValue = YearMonth.now(),
            yearMonthValueOnChange = { }
        )
    }
}

@Preview(locale = "en", showBackground = true)
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

@Preview(locale = "fr", showBackground = true)
@Composable
fun YearMonthOptionsPreviewFrench() {
    GymTrackerTheme(darkTheme = false) {
        YearMonthOptions(
            yearMonthValue = YearMonth.now(),
            yearMonthValueOnChange = { },
            closeFunction = { }
        )
    }
}
