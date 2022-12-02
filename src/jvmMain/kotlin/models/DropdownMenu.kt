package models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import helpers.ProjectColors

/**
 * Класс для создания кастомной выпадающей менюшки
 * @param labelText подсказка о типе выбираемых элементов
 * @param items словарь объектов, который хотим отображать в менюшке
 * @param onItemClick лямбда выражение, вызываемое при выборе другого элемента меню
 */
class DropdownMenu(
    private val labelText: String,
    private val items: Map<String, Float>,
    private val onItemClick: (Float) -> Unit
): ItemUI() {

    var currentEntry: Map.Entry<String, Float> = items.entries.first()

    @Composable
    override fun draw() {

        var expanded by remember { mutableStateOf(false) }
        var dismissed by remember { mutableStateOf(true) }

        var selectedItem by remember { mutableStateOf("") }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val icon =
            if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown

        fun showOrHideMenu() {
            expanded = !expanded
            dismissed = false
        }

        Column(
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
        ) {
            OutlinedTextField(
                colors = ProjectColors.outlinedDropdownColors(),
                readOnly = true,
                enabled = false,
                value = selectedItem,
                onValueChange = { newValue ->
                    selectedItem = newValue
                    currentEntry = items.entries.find { it.key == selectedItem } ?: items.entries.first()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coords ->
                        textFieldSize = coords.size.toSize()
                    }
                    .focusRequester(focusRequester = focusRequester)
                    .clickable {
                        showOrHideMenu()
                    },
                label = {
                    Text(text = labelText)
                },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            showOrHideMenu()
                        },
                        tint = ProjectColors.ThemeColors.TEXT_PRIMARY.color
                    )
                }
            )

            DropdownMenu(
                expanded = expanded && !dismissed,
                onDismissRequest = {
                    dismissed = true
                },
                modifier = Modifier
                    .width(
                        with(LocalDensity.current) {
                            textFieldSize.width.toDp()
                        }
                    )
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onItemClick(item.value)
                            selectedItem = item.key
                            expanded = false
                            focusManager.clearFocus()
                        }
                    ) {
                        Text(
                            text = item.key
                        )
                    }
                }
            }
        }
    }
}