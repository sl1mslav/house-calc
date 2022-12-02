package models

import androidx.compose.runtime.Composable

/**
 * Изолированный абстрактный родительский класс отрисовываемых элементов
 */
sealed class ItemUI {

    /**
     * Переопределяемая функция отрисовки элемента.
     */
    @Composable
    abstract fun draw()
}
