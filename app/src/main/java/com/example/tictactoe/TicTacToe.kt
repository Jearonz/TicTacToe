import kotlin.random.Random


interface TicTacToe {                       // интерфейс игры
    val field: Field
    val isFinished: Boolean
    val winner: Boolean?
    fun actionPlayer(row: Int, col: Int): Boolean
    fun restart()
}

interface Field {                  // интерфейс поля игры (возможно задание любых размеров поля
    val size: Int
    fun get(row: Int, col: Int): Boolean?
    fun clearABoard()
}

interface MutableField : Field {  //интерфейс для предотвращения получения доступа к типу поля
    fun set(row: Int, col: Int, value: Boolean?)
}

class ArrayOfField(override val size: Int) : MutableField {
    // класс, необходимый для создания переменной поля
    private var pointsOfField: Array<Array<Boolean?>> = Array(size) { arrayOfNulls(size) }
    // заполнение поля нулями (пустыми клетками)

    override fun set(row: Int, col: Int, value: Boolean?) {     // функция установки знака
        pointsOfField[row][col] = value
    }

    override fun get(row: Int, col: Int): Boolean? = pointsOfField[row][col]    //функция получения знака
    override fun clearABoard() {
        pointsOfField = Array(size) { kotlin.arrayOfNulls(size) }
    }
}

class GameImp : TicTacToe {
    override val field: MutableField = ArrayOfField(3)
    override var isFinished: Boolean = false
    override var winner: Boolean? = null

    private var userSign = Random.nextBoolean()     //установка знака для пользователя

    init {
        if (!userSign) {                            //проверка хода игрока
            actionBot()
        }
    }

    override fun restart() {
        isFinished = false
        userSign = Random.nextBoolean()
        winner = null
        field.clearABoard()
        if (!userSign) {                            //проверка хода игрока
            actionBot()
        }
    }

    override fun actionPlayer(row: Int, col: Int): Boolean {       //ход пользователя по позиции ввода
        if (field.get(row, col) != null) {
            return false
        }
        field.set(row, col, userSign)                          //установка хода
        checkGame()
        if (!isFinished) {
            actionBot()
            checkGame()
        }
        return true
    }

    private fun checkGame() {                       //метод для проверки конца игры
        if (checkWin()) {
            isFinished = true

            return
        }

        repeat(field.size) { row ->
            repeat(field.size) { col ->
                if (field.get(row, col) == null) {
                    return
                }
            }
        }
        isFinished = true
    }

    private fun actionBot() {                      //ход бота
        var x: Int = -1
        var y: Int = -1
        var flag = true
        while (flag) {
            x = Random.nextInt(field.size)
            y = Random.nextInt(field.size)
            if (field.get(x, y) == null) {
                flag = false
            }
        }
        field.set(x, y, !userSign)
        return

    }

    private fun checkWin(): Boolean { //проверка победы игрока или бота
        return checkMainDiagonalWin() || checkHorizontalWin() || checkVerticalWin() || checkForceDiagonalWin()
    }

    private fun checkHorizontalWin(): Boolean { //проверка горизонтальной победы
        var countP = 0
        var countB = 0
        for (i in 0 until field.size) {
            for (j in 0 until field.size) {
                if (field.get(i, j) == userSign) {
                    countP++
                }
                if (field.get(i, j) == !userSign) {
                    countB++
                }
            }
            if (countB == field.size) {
                winner = !userSign
                return true
            }
            if (countP == field.size) {
                winner = userSign
                return true
            }
            countB = 0
            countP = 0
        }
        return false
    }

    private fun checkMainDiagonalWin(): Boolean { //проверка победы по главной диагонали
        var countP = 0
        var countB = 0
        for (i in 0 until field.size) {
            if (field.get(i, i) == userSign) {
                countP++
            }
            if (field.get(i, i) == !userSign) {
                countB++
            }

        }
        if (countB == field.size) {
            winner = !userSign
            return true
        }
        if (countP == field.size) {
            winner = userSign
            return true
        }
        return false
    }

    private fun checkForceDiagonalWin(): Boolean { //проверка победы по побочной диагонали
        var countB = 0
        var countP = 0
        for (i in 0 until field.size) {
            for (j in 0 until field.size) {
                if ((i + j) == (field.size - 1)) {
                    if (field.get(i, j) == userSign) countP++
                    if (field.get(i, j) == !userSign) countB++
                }
            }
        }
        if (countB == field.size) {
            winner = !userSign
            return true
        }
        if (countP == field.size) {
            winner = userSign
            return true
        }
        return false
    }

    private fun checkVerticalWin(): Boolean {   //проверка вертикальной победы
        var countP = 0
        var countB = 0
        for (j in 0 until field.size) {
            for (i in 0 until field.size) {
                if (field.get(i, j) == userSign) {
                    countP++
                }
                if (field.get(i, j) == !userSign) {
                    countB++
                }

            }
            if (countB == field.size) {
                winner = !userSign
                return true
            }
            if (countP == field.size) {
                winner = userSign
                return true
            }
            countB = 0
            countP = 0
        }
        return false
    }
}

//аналог оператора switch, используем его для присвоения булевым переменным знаков
fun Boolean?.toSign(): String = when (this) {
    true -> "X"
    false -> "O"
    null -> "-"
}

