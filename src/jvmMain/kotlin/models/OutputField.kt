package models

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpers.ProjectColors

/**
 * Класс карточки, отображающей результат каких-либо вычислений.
 * @param modifier особенности отображения в Compose
 * @param labelText заголовок результатной строки
 * @param value изменяемое значение строки, которое должно уметь реагировать на собственное переопределение новой отрисовкой.
 */
class OutputField(
    private val modifier: Modifier,
    private val labelText: String,
    private val value: String
): ItemUI() {

    @Composable
    override fun draw() {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.End,
                text = labelText,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = ProjectColors.ThemeColors.TEXT_PRIMARY.color,
            )
            Card(
                backgroundColor = ProjectColors.ThemeColors.TERTIARY_BACKGROUND.color,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.wrapContentHeight().fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(13.dp),
                    textAlign = TextAlign.End,
                    text = value,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProjectColors.ThemeColors.TEXT_PRIMARY.color
                )
            }
        }
    }
}