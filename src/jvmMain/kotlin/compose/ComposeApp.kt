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

    // ������� ������� ���������� ����������
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
                    // ������ ���������
                    Header(value = HEADER_TEXT).draw()

                    // ��������� � ������ ������� ���������
                    var surfaceValue by remember { mutableStateOf<Float?>(null) }
                    InputContainer(
                        labelText = "������ ������� ����",
                        modifier = Modifier.fillMaxWidth(),
                        outputValue = if (surfaceValue == null) "-" else surfaceValue.toString().replace(".", ","),
                        outputLabel = "������� ��������� (�2)",
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
                            labelText = "�������� ��� ����",
                            items = mapOf(
                                "�����������" to 1f,
                                "���������������" to 1.5f,
                                "�����������" to 2f
                            ),
                            onItemClick = {
                                inputs[0] = it
                                surfaceValue = multiplyInputs(inputs)
                            }
                        )

                        val lengthField = CustomTextField(
                            label = "����� (�)",
                            onValueChange = {
                                inputs[1] = it.replace(",", ".").toFloatOrNull()
                                surfaceValue = multiplyInputs(inputs)
                            }
                        )

                        val widthField = CustomTextField(
                            label = "������ (�)",
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

                    // ����� ���� �������� ����������, ��������� �������������
                    var inputSum by remember { mutableStateOf<Float?>(null) }

                    // ����� ����, ������������� ���� ��������� ��� ����
                    var cost by remember { mutableStateOf("-") }
                    cost = if (inputSum == null || surfaceValue == null)
                        "-"
                    else
                        (surfaceValue!! + inputSum!!).toString().replace(".", ",")

                    // ��������� � �������� ��������� ������������� ���� �� �������
                    InputContainer(
                        labelText = "������ ��������� ������������� ���� �� ������� ",
                        modifier = Modifier.fillMaxWidth(),
                        cost,
                        "����� ���� �������"
                    ) {
                        val inputs by remember { mutableStateOf(MutableList<Float?>(4) { null }) }

                        fun sumInputs() {
                            inputSum = if (inputs.any { it == null }) {
                                null
                            } else
                                inputs.reduce { acc, float -> acc?.plus(float ?: 0f) }
                        }

                        val houseFoundation = DropdownMenu(
                            labelText = "�������� ��������� ����",
                            items = mapOf(
                                "������-������������" to 1f,
                                "���������� �����" to 2f,
                                "���������� �����" to 3f
                            ),
                            onItemClick = {
                                inputs[0] = it
                                sumInputs()
                            }
                        )

                        val bearingWalls = DropdownMenu(
                            labelText = "�������� ��� ������� ����",
                            items = mapOf(
                                "������������ �����" to 1f,
                                "������������ �����" to 2f,
                                "���������� ����������" to 3f,
                                "���������� ������" to 4f
                            ),
                            onItemClick = {
                                inputs[1] = it
                                sumInputs()
                            }
                        )

                        val facadeCladding = DropdownMenu(
                            labelText = "�������� ��������� ������",
                            items = mapOf(
                                "������������ ������" to 1f,
                                "���������� ������������" to 2f,
                                "������������� �������" to 3f
                            ),
                            onItemClick = {
                                inputs[2] = it
                                sumInputs()
                            }
                        )

                        val roofing = DropdownMenu(
                            labelText = "�������� ��� ������",
                            items = mapOf(
                                "���������������" to 1f,
                                "�������� ��������" to 2f,
                                "����������� ��������" to 3f
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
     * ������� ��������� ���������� (��������), ������� ����� ���������
     * ���������� ������. �� ���� ����� ����� � ����� ������ ItemUI
     * @param labelText ��������� ����������
     * @param modifier ��������� ���������� ������������ Compose
     * @param outputValue ��������, ������� ������� ��� ����������� � ����������� ���������� ����������.
     * @param outputLabel ��������� ������������� ���� ������ ����������
     * @param content ���� ��������� Composable �������, ����� ����� ���� ��������������� ���������� ����������.
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