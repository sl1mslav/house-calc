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
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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

                    // ============= Общие переменные в контексте всего окна приложения ===================

                    // Списки объектов, хрянящих выбранные пользователем значения (размерности / материалов)
                    val sizeInputs by remember { mutableStateOf(MutableList<Float?>(6) { null }) }
                    val materialInputs by remember { mutableStateOf(MutableList<Float?>(4) { null }) }

                    // Общая цена, высчитывается если заполнены все поля
                    var cost by remember { mutableStateOf<String?>("-") }

                    // ====================================================================================

                    // Контейнер с счётом размерности помещения
                    var surfaceValue by remember { mutableStateOf<Float?>(null) }
                    surfaceValue =
                        if (sizeInputs[1] == null || sizeInputs[2] == null)
                            null
                        else sizeInputs[1]!! * sizeInputs[2]!!

                    var perimeterValue by remember { mutableStateOf<Float?>(null) }
                    var volumeValue by remember { mutableStateOf<Float?>(null) }
                    var foundationHeight by remember { mutableStateOf<Float?>(null) }

                    fun calculateCost() {
                        val toCalculate = listOf(
                            foundationHeight,
                            surfaceValue,
                            materialInputs[0],
                            materialInputs[1],
                            materialInputs[2],
                            materialInputs[3],
                            volumeValue
                        )
                        cost = if (toCalculate.all { it != null }) {
                            val result = (foundationHeight!! * surfaceValue!! * materialInputs[0]!! +
                                    volumeValue!! * (materialInputs[1]!! + materialInputs[2]!!) + surfaceValue!! * materialInputs[3]!!)
                            BigDecimal(result.toDouble()).toCoherentNumber()
                        } else null
                    }

                    InputContainer(
                        labelText = "Расчёт площади дома, объёма стен",
                        modifier = Modifier.fillMaxWidth(),
                        outputs = mapOf(
                            "Периметр дома (м)" to perimeterValue?.toString(),
                            "Площадь дома (м2)" to surfaceValue?.toString(),
                            "Суммарный объём стен (м3)" to volumeValue?.toString()
                        )
                    ) {

                        fun calcPerimeter(length: Float?, width: Float?): Float? {
                            return ((length ?: return null) + (width ?: return null)) * 2
                        }

                        fun calcWallsVolume(): Float? {
                            val toMultiplyList = listOf(
                                perimeterValue,
                                sizeInputs[0],
                                sizeInputs[3],
                                sizeInputs[4]
                            )
                            return if (toMultiplyList.all { it != null }) {
                                toMultiplyList.reduce { acc, element ->
                                    acc!! * element!!
                                }?.times(0.01f) // умножаем на 0.01 поскольку толщина в см
                            } else null
                        }

                        val dropdownMenu = DropdownMenu(
                            labelText = "Выберите тип дома",
                            items = mapOf(
                                "Одноэтажный" to 1f,
                                "Полутораэтажный" to 1.5f,
                                "Двухэтажный" to 2f
                            ),
                            onItemClick = {
                                sizeInputs[0] = it
                                calculateCost()
                            }
                        )

                        val lengthField = CustomTextField(
                            label = "Длина (м)",
                            onValueChange = {
                                sizeInputs[1] = it.replace(",", ".").toFloatOrNull()
                                perimeterValue = calcPerimeter(sizeInputs[1], sizeInputs[2])
                                calculateCost()
                            }
                        )

                        val widthField = CustomTextField(
                            label = "Ширина (м)",
                            onValueChange = {
                                sizeInputs[2] = it.replace(",", ".").toFloatOrNull()
                                perimeterValue = calcPerimeter(sizeInputs[1], sizeInputs[2])
                                calculateCost()
                            }
                        )

                        val heightField = CustomTextField(
                            label = "Высота этажа (м)",
                            onValueChange = {
                                sizeInputs[3] = it.replace(",", ".").toFloatOrNull()
                                volumeValue = calcWallsVolume()
                                calculateCost()
                            }
                        )

                        val wallThicknessField = CustomTextField(
                            label = "Толщина стены (см)",
                            onValueChange = {
                                sizeInputs[4] = it.replace(",", ".").toFloatOrNull()
                                volumeValue = calcWallsVolume()
                                calculateCost()
                            }
                        )

                        val foundationHeightField = CustomTextField(
                            label = "Высота фундамента (м)",
                            onValueChange = {
                                sizeInputs[5] = it.replace(",", ".").toFloatOrNull()
                                foundationHeight = sizeInputs[5]
                                calculateCost()
                            }
                        )

                        dropdownMenu.draw()
                        lengthField.draw()
                        widthField.draw()
                        heightField.draw()
                        wallThicknessField.draw()
                        foundationHeightField.draw()
                    }

                    // Сумма всех значений материалов, выбранных пользователем

                    // Контейнер с расчётом стоимости строительства дома по проекту
                    InputContainer(
                        labelText = "Расчёт стоимости строительства дома по проекту ",
                        modifier = Modifier.fillMaxWidth(),
                        outputs = mapOf(
                            "Общая цена проекта (₽)" to cost
                        )
                    ) {

                        val houseFoundation = DropdownMenu(
                            labelText = "Выберите фундамент дома",
                            items = mapOf(
                                "Монолитная лента" to 6500f,
                                "Монолитная плита" to 15_000f
                            ),
                            onItemClick = {
                                materialInputs[0] = it
                                calculateCost()
                            }
                        )

                        val bearingWalls = DropdownMenu(
                            labelText = "Выберите тип несущих стен",
                            items = mapOf(
                                "Газобетонные блоки" to 6_570f,
                                "Керамические блоки" to 9_850f,
                                "Монолитная технология" to 5_000f,
                                "Деревянный каркас" to 4_000f
                            ),
                            onItemClick = {
                                materialInputs[1] = it
                                calculateCost()
                            }
                        )

                        val facadeCladding = DropdownMenu(
                            labelText = "Выберите облицовку фасада",
                            items = mapOf(
                                "Облицовочный кирпич" to 1_000f,
                                "Штукатурка декоративная" to 300f,
                                "Металлический сайдинг" to 700f
                            ),
                            onItemClick = {
                                materialInputs[2] = it
                                calculateCost()
                            }
                        )

                        val roofing = DropdownMenu(
                            labelText = "Выберите тип кровли",
                            items = mapOf(
                                "Металлочерепица" to 500f,
                                "Битумная черепица" to 1_000f,
                                "Композитная черепица" to 750f
                            ),
                            onItemClick = {
                                materialInputs[3] = it
                                calculateCost()
                            }
                        )

                        houseFoundation.draw()
                        bearingWalls.draw()
                        facadeCladding.draw()
                        roofing.draw()
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
     * @param outputs словарь формы Заголовок - Значение, которое передаём для запоминания и отображения результата вычислений
     * @param content сюда передаётся Composable функция, чтобы можно было кастомизировать наполнение контейнера.
     */
    @Composable
    fun InputContainer(
        labelText: String,
        modifier: Modifier,
        outputs: Map<String, String?>,
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
                    fontSize = (21.5f).sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProjectColors.ThemeColors.TEXT_PRIMARY.color,
                    textAlign = TextAlign.Start
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, true).padding(top = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        content()
                    }
                    Spacer(modifier = Modifier.weight(0.15f, true))
                    Column(
                        modifier = Modifier.weight(1f, true).align(Alignment.Bottom),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (output in outputs) {
                            OutputField(
                                modifier = Modifier,
                                labelText = output.key,
                                value = output.value?.replace(".", ",") ?: "-"
                            ).draw()
                        }
                    }
                }
            }
        }
    }

    private fun BigDecimal.toCoherentNumber(): String {
        val decimalFormatSymbols = DecimalFormatSymbols().apply {
            decimalSeparator = ','
            groupingSeparator = ' '
        }
        return DecimalFormat("#,##0.00", decimalFormatSymbols).format(this)
    }
}