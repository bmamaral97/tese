parser grammar pl_parser;

options { tokenVocab = pl_lexer; }

proposition
   : expression EOF
   ;

expression
   : relExpression ((AND | OR) relExpression)*
   ;

relExpression
   : atom
   | equiv
   | implies
   ;

atom
   : variable
   | NOT atom
   | LPAREN expression RPAREN
   ;

equiv
   : atom EQUIV atom
   ;

implies
   : atom IMPLIES atom
   ;

variable
   : LETTER
   ;
