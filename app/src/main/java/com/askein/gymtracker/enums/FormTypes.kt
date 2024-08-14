package com.askein.gymtracker.enums

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

enum class FormTypes(val keyboardOptions: KeyboardOptions, val regexPattern: String) {
    STRING(KeyboardOptions.Default, ""),
    INTEGER(KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), """^-?\d*$"""),
    DOUBLE(KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal), """^-?\d*([\.,]\d?)?${'$'}""")
}
