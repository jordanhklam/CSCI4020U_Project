grammar PL;

@header {
import backend.*;
}

@members {
}

program returns [Expr expr]
    : statements EOF { $expr = $statements.result; }
    ;

statements returns [Expr result]
    : statement ( ';' statement )* 
    ;

statement returns [Expr result]
    : assignmentStmt
    | printStmt
    ;

assignmentStmt returns [Expr result]
    : ID '=' expression { $result = new AssignmentExpr($ID.text, $expression.result); }
    ;

printStmt returns [Expr result]
    : 'print' '(' expression ')' { $result = new PrintExpr($expression.result); }
    ;

expression returns [Expr result]
    : stringLiteral 
    | ID 
    | expression '++' expression 
    | expression '*' expression 
    | '(' expression ')' 
    | INT 
    ;

stringLiteral returns [Expr result]
    : STRING 
    ;

ID : [a-zA-Z]+;
STRING : '"' ~[\r\n]* '"';
INT : [0-9]+;
WHITESPACE : [ \t\r\n] -> skip;
