package backend

abstract class Data

object None:Data() {
    override fun toString() = "None"
}

data class Integer(val value: Int) : Data() {
    override fun toString() = value.toString()
}

data class StringData(val value: String) : Data() {
    override fun toString() = value
}

data class BooleanData(val value: Boolean) : Data() {
    override fun toString() = value.toString()
}

data class FunctionData(val params: List<String>, val body: Expr) : Data() {
    override fun toString() = "Function"
}