package helpers

/**
 * ������������� ����� ��� ��������� �����������. ��� ��������������� ��� ����� ��������� � ��������� �������.
 */
sealed class MyExceptions: Exception() {
    /**
     * ��������� ���������� ��� ���� �����. ������������ ������ ���.
     * @param message ���������, ������� ����� �������� � �����������.
     */
    class TextFieldException(override val message: String): MyExceptions()
}
