package helpers

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Объект для хранения кастомных тем приложения.
 */
object ProjectColors {
    enum class ThemeColors(val color: Color) {
        PRIMARY_BACKGROUND(Color.DarkGray),
        SECONDARY_BACKGROUND(Color(0xFF646464)),
        TERTIARY_BACKGROUND(Color(0xFF363232)),
        TEXT_PRIMARY(Color.White),
        TEXT_SECONDARY(Color.LightGray),
        ERROR(Color(0xFFFF4D4D))
    }

    @Composable
    fun outlinedDropdownColors() = TextFieldDefaults.outlinedTextFieldColors(
        unfocusedBorderColor = ThemeColors.TEXT_SECONDARY.color,
        focusedBorderColor = ThemeColors.TEXT_SECONDARY.color,
        textColor = ThemeColors.TEXT_PRIMARY.color,
        disabledTextColor = ThemeColors.TEXT_PRIMARY.color,
        disabledLabelColor = ThemeColors.TEXT_SECONDARY.color
    )

    @Composable
    fun outlinedTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
        textColor = ThemeColors.TEXT_PRIMARY.color,
        unfocusedBorderColor = ThemeColors.TEXT_SECONDARY.color,
        focusedBorderColor = ThemeColors.TEXT_SECONDARY.color,
        unfocusedLabelColor = ThemeColors.SECONDARY_BACKGROUND.color,
        focusedLabelColor = ThemeColors.TEXT_SECONDARY.color,
        cursorColor = ThemeColors.TEXT_PRIMARY.color,
        errorBorderColor = ThemeColors.ERROR.color,
        errorLabelColor = ThemeColors.ERROR.color
    )
}