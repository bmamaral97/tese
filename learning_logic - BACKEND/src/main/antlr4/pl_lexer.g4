lexer grammar pl_lexer;

AND: '&'
    ;

OR: '|'
    ;

NOT: '~'
    ;

IMPLIES: '=>'
    ;

EQUIV: '<=>'
    ;

LPAREN: '('
    ;

RPAREN: ')'
    ;

LETTER: ('a' .. 'z') | ('A' .. 'Z')
    ;

WS: [ \r\n\t] + -> skip
    ;

