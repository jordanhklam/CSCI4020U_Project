package backend

abstract class Expr {
    abstract fun eval(runtime: Runtime): Data
}

class NoneExpr : Expr() {
    override fun eval(runtime: Runtime): Data = None
}

class IntegerExpr(val value: Int) : Expr() {
    override fun eval(runtime: Runtime): Data = Integer(value)
}

class StringExpr(val value: String) : Expr() {
    override fun eval(runtime: Runtime): Data = StringData(value)
}

class BooleanExpr(val value: Boolean) : Expr() {
    override fun eval(runtime: Runtime): Data = BooleanData(value)
}

class VariableExpr(val name: String) : Expr() {
    override fun eval(runtime: Runtime): Data {
        return runtime.symbolTable[name] ?: throw RuntimeException("Variable $name not found")
    }
}

class AssignmentExpr(val name: String, val expr: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value = expr.eval(runtime)
        runtime.symbolTable[name] = value
        return value
    }
}

class PrintExpr(val expr: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value = expr.eval(runtime)
        println(value)
        return None
    }
}

class ConcatExpr(val expr1: Expr, val expr2: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value1 = expr1.eval(runtime)
        val value2 = expr2.eval(runtime)
        return StringData(value1.toString() + value2.toString())
    }
}

class MultiplyExpr(val expr1: Expr, val expr2: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val value1 = expr1.eval(runtime) as Integer
        val value2 = expr2.eval(runtime) as Integer
        return Integer(value1.value * value2.value)
    }
}

class IfElseExpr(val condition: Expr, val ifBody: Expr, val elseBody: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val conditionValue = (condition.eval(runtime) as BooleanData).value
        return if (conditionValue) ifBody.eval(runtime) else elseBody.eval(runtime)
    }
}

class FunctionDefExpr(val name: String, val params: List<String>, val body: Expr) : Expr() {
    override fun eval(runtime: Runtime): Data {
        runtime.symbolTable[name] = FunctionData(params, body)
        return None
    }
}

class FunctionCallExpr(val name: String, val args: List<Expr>) : Expr() {
    override fun eval(runtime: Runtime): Data {
        val function = runtime.symbolTable[name] as? FunctionData
            ?: throw RuntimeException("Function $name not found")

        if (args.size != function.params.size) {
            throw RuntimeException("Function $name expects ${function.params.size} arguments, ${args.size} provided")
        }

        val bindings = function.params.zip(args.map { it.eval(runtime) }).toMap()
        val subscopeRuntime = runtime.subscope(bindings)

        return function.body.eval(subscopeRuntime)
    }
}