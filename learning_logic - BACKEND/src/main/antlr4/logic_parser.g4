parser grammar logic_parser;

options { tokenVocab = pl_lexer; }

start
    : expression EOF
    ;

expression
    : atom
    | bidirectional
    ;

bidirectional
    : bidirectional EQUIV disjunction
    | bidirectional IMPLIES disjunction
    | disjunction
    ;

disjunction
    : disjunction OR conjunction
    | conjunction
    ;

conjunction
    : conjunction AND atom
    | atom
    ;

atom
    : NOT atom
    | LPAREN expression RPAREN
    | variable
    ;

variable
    : LETTER
    ;