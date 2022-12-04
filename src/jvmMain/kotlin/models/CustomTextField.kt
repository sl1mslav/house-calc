package models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpers.MyExceptions
import helpers.ProjectColors
import helpers.Strings
import helpers.Strings.COMMA_AFTER_ZERO
import helpers.Strings.FIELD_ERROR_NAN

/**
 * Класс для создания "красивого" объекта поля ввода
 * @param label подсказка для ввода
 * @param onValueChange лямбда-функция для обработки событий изменения значения поля ввода
 */
class CustomTextField(
    private val label: String,
    private val onValueChange: (String) -> Unit
): ItemUI() {

    @Composable
    override fun draw() {
        var inputText by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf(FIELD_ERROR_NAN) }

        Column {
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                singleLine = true,
                isError = error,
                trailingIcon = {
                    if (error) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Error",
                            tint = ProjectColors.ThemeColors.ERROR.color
                        )
                    }
                },
                value = inputText,
                onValueChange = { changedValue ->
                    try {
                        inputText = compareInput(inputText, changedValue)
                        error = false
                    } catch (exception: MyExceptions.TextFieldException) {
                        error = true
                        errorMessage = exception.message
                    }
                    onValueChange(inputText)
                },
                colors = ProjectColors.outlinedTextFieldColors(),
                label = {
                    Text(
                        color = if (error)
                            ProjectColors.ThemeColors.ERROR.color
                        else
                            ProjectColors.ThemeColors.TEXT_SECONDARY.color,
                        text = label
                    )
                }
            )
            if (error) {
                Text(
                    text = errorMessage,
                    color = ProjectColors.ThemeColors.ERROR.color,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp, top = 3.dp)
                )
            }
        }
    }

    /**
     * Дикая функция для проверки ввода пользователя, поскольку на клавиатуре нельзя ограничить тип ввода
     * @param oldValue значение поля ввода до добавления нового символа
     * @param newValue значение поля ввода после добавления нового символа
     */
    private fun compareInput(oldValue: String, newValue: String): String {

        when {
            newValue.endsWith(",") -> {
                return if (newValue.length <= oldValue.length)
                    newValue
                else if (oldValue.contains(","))
                    throw MyExceptions.TextFieldException(message = Strings.FIELD_ERROR_EXTRA_COMMA)
                else if (oldValue.isEmpty())
                    throw MyExceptions.TextFieldException(message = Strings.FIELD_ERROR_COMMA_START)
                else
                    oldValue + newValue.last()
            }

            newValue.length <= oldValue.length -> {
                return newValue
            }

            newValue.length == 2 && newValue.first().toString() == "0" && newValue[1].toString() != "," ->
                throw MyExceptions.TextFieldException(message = COMMA_AFTER_ZERO)

            (newValue.lastOrNull() ?: throw MyExceptions.TextFieldException(message = FIELD_ERROR_NAN)).isDigit() -> {
                return oldValue + newValue.last()
            }

            else -> {
                throw MyExceptions.TextFieldException(
                    message = FIELD_ERROR_NAN
                )
            }
        }
    }
}