package models

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import helpers.ProjectColors

/**
 * Простой класс заголовка
 * @param value текст, который необходимо отобразить в заголовке
 */
class Header(val value: String) : ItemUI() {

    @Composable
    override fun draw() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .wrapContentHeight(),
            backgroundColor = ProjectColors.ThemeColors.SECONDARY_BACKGROUND.color
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = ProjectColors.ThemeColors.TEXT_PRIMARY.color
            )
        }
    }

}