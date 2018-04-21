/**
 * Expression grammar
 */

grammar Expression;
@header { package antlr.parse; }

parse: expr EOF;

expr :<assoc=right> left=expr '^' right=expr    #pow
     | op=(ADD | SUB) value=expr                #unary
     | left=expr (MUL | DIV | MOD) right=expr   #mulDivMod
     | left=expr (ADD | SUB) right=expr         #addSub
     | value=NUMBER                             #number
     | name=NAME                                #name
     | '(' expr ')'                             #paren
     ;

ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;
MOD : '%' ;

NUMBER
    : INT [eE] [+-]? INT
    | INT '.' DIGIT*
    | '.' DIGIT+
    | INT ;

NAME : LETTER (LETTER | DIGIT)*;

WHITESPACE : [\t\n\r ] -> skip ;

fragment INT : '0' | [1-9] DIGIT* ;
fragment DIGIT : [0-9] ;
fragment LETTER : [a-zA-Z] ;
