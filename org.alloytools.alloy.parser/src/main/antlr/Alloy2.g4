grammar Alloy;

alloyFile
    : paragraph*
    ;

paragraph
    : moduleDecl
    | importDecl
    | sigDecl
    | factDecl 
    | predDecl 
    | assertDecl
    | commandDecl
    ;
    
moduleDecl  : 'module' qualifiedName ( '[' names ']' )? ;
importDecl  : 'open' qualifiedName ( '[' qualifiedName ( ',' qualifiedName )* ']' )? ( 'as' name )? ;
sigDecl     : qualifier* 'sig' names (sigExt | sigIn)? '{' fieldDecl* '}' block? ;
factDecl    : 'fact' name? block ;
predDecl    : 'pred' ( qualifiedName '.' )? name paraDecls? block ;
funDecl     : 'fun' ( qualifiedName '.' )? name paraDecls? ':' expr block;
assertDecl  : 'assert' name? block ;
commandDecl : ( name ':' )? ( 'run' | 'check' ) ( qualifiedName | block ) scope? ;

sigExt      : 'extends' qualifiedName  ;
sigIn       :  'in' qualifiedName ( '+' qualifiedName )* ;
fieldDecl   : ('var')? decl ;
decl        : ('disj')? names ':' ('disj')? expr  ;


qualifier   : 'var'             # qualifier_var
            | 'abstract'        # qualifier_abstract
            | 'private'         # qualifier_private
            | 'lone'            # qualifier_lone
            | 'one'             # qualifier_one
            | 'some'            # qualifier_some
            ;

expr
            : '~' expr                                          # transposeExpr
            | '^' expr                                          # transClosureExpr
            | '*' expr                                          # reflectiveTransClosureExpr
            
            | expr '.' expr                                     # joinExpr
            | expr '[' ( expr ( ',' expr )* )? ']'              # boxJoinExpr
            | expr '<:' expr                                    # domainExpr
            | expr ':>' expr                                    # rangeExpr
            | expr arrowQuant '->' arrowQuant expr              # arrowExpr
            | expr '&' expr                                     # intersectionExpr
            | expr '++' expr                                    # relationOverrideExpr
            | '#' expr                                          # cardinalityExpr
            | expr '+' expr                                     # unionExpr
            | expr '-' expr                                     # differenceExpr
            | quant decl ( ',' decl )+ ( block | bar )          # quantificationExpr
            | 'no' expr                                         # noExpr
            | 'lone' expr                                       # loneExpr
            | 'one' expr                                        # oneExpr
            | 'some' expr                                       # someExpr
            | 'set' expr                                        # setExpr
            | expr 'in' expr                                    # inExpr
            | expr neg 'in' expr                                # notInExpr
            | expr '=' expr                                     # eqExpr
            | expr neg '=' expr                                 # notEqExpr
            | expr '<' expr                                     # ltExpr
            | expr neg '<' expr                                 # notLtExpr
            | expr '>' expr                                     # gtExpr
            | expr neg '>' expr                                 # notGtExpr
            | expr '=<' expr                                    # leExpr
            | expr neg '=<' expr                                # notLeExpr
            | expr '>=' expr                                    # geExpr
            | expr neg '>=' expr                                # notGeExpr
            | 'not' expr                                        # notExpr
            | '!' expr                                          # notExpr
            | expr '&&' expr                                    # andExpr
            | expr 'and' expr                                   # andExpr
            | expr expr                                         # andExpr
            | <assoc=right> expr '=>' expr 'else' expr          # ifThenElseExpr
            | <assoc=right> expr 'implies' expr 'else' expr     # ifThenElseExpr
            | <assoc=right> expr  '=>'  expr                    # impliesExpr
            | <assoc=right> expr 'implies' expr                 # impliesExpr
            | expr '<=>' expr                                   # iffExpr
            | expr 'iff' expr                                   # iffExpr
            | expr '||' expr                                    # orExpr
            | expr 'or' expr                                    # orExpr    
            | 'let' letDecl ( ',' letDecl )+ ( block | bar )    # letExpr
            | quant decl ( ',' decl )+ ( block | bar )          # quantificationExpr        
            | '-'? NUMBER                                       # numberExpr
            | 'none'                                            # noneExpr
            | 'univ'                                            # univExpr
            | 'iden'                                            # idenExpr
            | '@' name                                          # atnameExpr
            | qualifiedName                                     # nameExpr
            | 'this'                                            # thisExpr
            | expr '\''                                         # primeExpr    
            | 'always' expr                                     # alwaysExpr
            | 'eventually' expr                                 # eventuallyExpr
            | 'after' expr                                      # afterExpr
            | 'before' expr                                     # beforeExpr
            | 'historically' expr                               # historicallyExpr
            | 'once' expr                                       # onceExpr
            | expr 'until' expr                                 # untilExpr
            | expr 'releases' expr                              # releasesExpr
            | expr 'since' expr                                 # sinceExpr
            | expr 'triggered' expr                             # triggeredExpr
            | expr ';' expr                                     # afterPrevExpr
            | '{' decl ( ',' decl )+ ( block | bar ) '}'        # comprehensionExpr
            | '(' expr ')'                                      # parenthesisExpr
            | block                                             # blockExpr
            ;
    
neg : ( '!' | 'not' ) ;

arrowQuant  : 'lone'    # arrowQuant_lone 
            | 'one'     # arrowQuant_one
            | 'some'    # arrowQuant_some
            | 'set'     # arrowQuant_set
            | /*empty*/ # arrowQuant_set 
            ; 

paraDecls
    : '(' ( decl ( ',' decl )* )? ')'
    | '[' ( decl ( ',' decl )* )? ']'
    ;



block       : '{' expr* '}' ;
bar         : '|' expr ;  

scope
    : 'for' NUMBER ( 'but' typescope ( ',' typescope )* )?
    | 'for' typescope ( ',' typescope )+
    | /* empty */
    ;

typescope   : ('exactly')? NUMBER qualifiedName ;
qualifiedName    : ( 'this' '/' )? ( name '/' )* name ;
names       : name ( ',' name )* ;
name        : ID ;



letDecl     : name '=' expr ;


quant 
    : 'all'             # quant_all
    | 'no'              # quant_no
    | 'sum'             # quant_sum
    | 'lone'            # quant_lone
    | 'one'             # quant_one
    | 'some'            # quant_some
    ;

ID              : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER          : [0-9]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
OPTION_COMMENT  : '--' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;
