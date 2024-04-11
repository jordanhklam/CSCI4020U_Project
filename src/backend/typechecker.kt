package backend

class TypeChecker {
    fun check(expr: Expr) {
        checkExpr(expr)
    }

    private fun checkExpr(expr: Expr): Data {
        return when (expr) {
            is IntegerExpr -> Integer(0)
            is StringExpr -> StringData("")
            is BooleanExpr -> BooleanData(false)
            is VariableExpr -> None
            is AssignmentExpr -> {
                checkExpr(expr.expr)
                None
            }
            is PrintExpr -> {
                checkExpr(expr.expr)
                None
            }
            is ConcatExpr -> {
                checkExpr(expr.expr1)
                checkExpr(expr.expr2)
                StringData("")
            }
            is MultiplyExpr -> {
                checkExpr(expr.expr1)
                checkExpr(expr.expr2)
                Integer(0)
            }
            is IfElseExpr -> {
                checkExpr(expr.condition)
                checkExpr(expr.ifBody)
                checkExpr(expr.elseBody)
            }
            is FunctionDefExpr -> None // No type checking needed for function definitions
            is FunctionCallExpr -> {
                expr.args.forEach { checkExpr(it) }
                None
            }
            else -> throw RuntimeException("Unknown expression type")
        }
    }
}
