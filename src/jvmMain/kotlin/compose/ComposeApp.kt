package compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpers.ProjectColors
import helpers.Strings.HEADER_TEXT
import models.CustomTextField
import models.DropdownMenu
import models.Header
import models.OutputField

object ComposeApp {

    // Главная функция композиции приложения
    @Composable
    fun MainApp() {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ProjectColors.ThemeColors.PRIMARY_BACKGROUND.color)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // Рисуем заголовок
                    Header(value = HEADER_TEXT).draw()

                    // Контейнер с счётом площади помещения
                    var surfaceValue by remember { mutableStateOf<Float?>(null) }
                    InputContainer(
                        labelText = "Расчёт площади дома",
                        modifier = Modifier.fillMaxWidth(),
                        outputValue = if (surfaceValue == null) "-" else surfaceValue.toString().replace(".", ","),
                        outputLabel = "Площадь помещений (м2)",
                    ) {
                        val inputs by remember {
                            mutableStateOf(
                                MutableList<Float?>(3) { null }
                            )
                        }

                        fun multiplyInputs(items: MutableList<Float?>): Float? {
                            val result = items.reduce { acc, item ->
                                (acc ?: return null) * (item ?: return null)
                            }
                            return result
                        }

                        val dropdownMenu = DropdownMenu(
                            labelText = "Выберите тип дома",
                            items = mapOf(
                                "Одноэтажный" to 1f,
                                "Полутораэтажный" to 1.5f,
                                "Двухэтажный" to 2f
                            ),
                            onItemClick = {
                                inputs[0] = it
                                surfaceValue = multiplyInputs(inputs)
                            }
                        )

                        val lengthField = CustomTextField(
                            label = "Длина (м)",
                            onValueChange = {
                                inputs[1] = it.replace(",", ".").toFloatOrNull()
                                surfaceValue = multiplyInputs(inputs)
                            }
                        )

                        val widthField = CustomTextField(
                            label = "Ширина (м)",
                            onValueChange = {
                                inputs[2] = it.replace(",", ".").toFloatOrNull()
                                surfaceValue = multiplyInputs(inputs)
                            }
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(0.45f)
                        ) {
                            dropdownMenu.draw()
                            lengthField.draw()
                            widthField.draw()
                        }
                    }

                    // Сумма всех значений материалов, выбранных пользователем
                    var inputSum by remember { mutableStateOf<Float?>(null) }

                    // Общая цена, высчитывается если заполнены все поля
                    var cost by remember { mutableStateOf("-") }
                    cost = if (inputSum == null || surfaceValue == null)
                        "-"
                    else
                        (surfaceValue!! + inputSum!!).toString().replace(".", ",")

                    // Контейнер с расчётом стоимости строительства дома по проекту
                    InputContainer(
                        labelText = "Расчёт стоимости строительства дома по проекту ",
                        modifier = Modifier.fillMaxWidth(),
                        cost,
                        "Общая цена проекта"
                    ) {
                        val inputs by remember { mutableStateOf(MutableList<Float?>(4) { null }) }

                        fun sumInputs() {
                            inputSum = if (inputs.any { it == null }) {
                                null
                            } else
                                inputs.reduce { acc, float -> acc?.plus(float ?: 0f) }
                        }

                        val houseFoundation = DropdownMenu(
                            labelText = "Выберите фундамент дома",
                            items = mapOf(
                                "Свайно-ростверковый" to 1f,
                                "Монолитная лента" to 2f,
                                "Монолитная плита" to 3f
                            ),
                            onItemClick = {
                                inputs[0] = it
                                sumInputs()
                            }
                        )

                        val bearingWalls = DropdownMenu(
                            labelText = "Выберите тип несущих стен",
                            items = mapOf(
                                "Газобетонные блоки" to 1f,
                                "Керамические блоки" to 2f,
                                "Монолитная технология" to 3f,
                                "Деревянный каркас" to 4f
                            ),
                            onItemClick = {
                                inputs[1] = it
                                sumInputs()
                            }
                        )

                        val facadeCladding = DropdownMenu(
                            labelText = "Выберите облицовку фасада",
                            items = mapOf(
                                "Облицовочный кирпич" to 1f,
                                "Штукатурка декоративная" to 2f,
                                "Металлический сайдинг" to 3f
                            ),
                            onItemClick = {
                                inputs[2] = it
                                sumInputs()
                            }
                        )

                        val roofing = DropdownMenu(
                            labelText = "Выберите тип кровли",
                            items = mapOf(
                                "Металлочерепица" to 1f,
                                "Битумная черепица" to 2f,
                                "Композитная черепица" to 3f
                            ),
                            onItemClick = {
                                inputs[3] = it
                                sumInputs()
                            }
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(0.45f)
                        ) {
                            houseFoundation.draw()
                            bearingWalls.draw()
                            facadeCladding.draw()
                            roofing.draw()
                        }
                    }
                }
            }
        }
    }

    /**
     * Функция отрисовки контейнера (карточки), который можно заполнять
     * различными полями. Их типы можно найти в детях класса ItemUI
     * @param labelText заголовок контейнера
     * @param modifier параметры заполнения пространства Compose
     * @param outputValue значение, которое передаём для запоминания и отображения результата вычислений.
     * @param outputLabel заголовок обязательного поля вывода информации
     * @param content сюда передаётся Composable функция, чтобы можно было кастомизировать наполнение контейнера.
     */
    @Composable
    fun InputContainer(
        labelText: String,
        modifier: Modifier,
        outputValue: String,
        outputLabel: String,
        content: @Composable () -> Unit
    ) {

        Card(
            modifier = modifier.padding(20.dp),
            backgroundColor = ProjectColors.ThemeColors.SECONDARY_BACKGROUND.color
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(15.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = labelText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProjectColors.ThemeColors.TEXT_PRIMARY.color,
                    textAlign = TextAlign.Start
                )
                Box(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                ) {
                    content()
                    OutputField(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .fillMaxWidth(0.4f)
                            .wrapContentHeight(),
                        labelText = outputLabel,
                        value = outputValue
                    ).draw()
                }
            }
        }
    }
}