package helpers

/**
 * Изолированный класс для кастомных исключнений. При масштабировании его можно расширять и дополнять другими.
 */
sealed class MyExceptions: Exception() {
    /**
     * Кастомное исключение для поля ввода. Использовать только там.
     * @param message сообщение, которое хотим передать с исключением.
     */
    class TextFieldException(override val message: String): MyExceptions()
}
