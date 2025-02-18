grammar Alloy;

alloyFile
    : paragraph*
    ;

paragraph       : moduleDecl
                | importDecl
                | sigDecl
                | factDecl 
                | predDecl 
                | assertDecl
                | commandDecl
                ;

moduleDecl      : 'module' qname ( '[' names ']' )? ;
importDecl      : 'open' qname ( '[' qname ( ',' qname )* ']' )? ( 'as' ID )? ;
sigDecl         : qualifier* 'sig' names ('extends' qname | 'in' qname ( '+' qname )*)? '{' sigFields? '}' block? ;
factDecl        : 'fact' ID? block ;
predDecl        : 'pred' ( qname '.' )? ID paraDecls? block ;
funDecl         : 'fun' ( qname '.' )? ID paraDecls? ':' value block;
assertDecl      : 'assert' ID? block ;
commandDecl     : ( ID ':' )? ( 'run' | 'check' ) ( qname | block ) scope? ;
macroDecl       : 'let' ID ( '[' names ']' )? '='? macro ;

sigFields       : sigFieldDecl ( ',' sigFieldDecl )* ','? ; 
sigFieldDecl    : 'var'? decl ;

decl            : 'disj'? names ':' 'disj'? ('lone' | 'one' | 'some' |  'set' )? value  ;


qualifier       : 'var'                                                             # var
                | 'abstract'                                                        # abstract
                | 'private'                                                         # private
                | 'lone'                                                            # lone
                | 'one'                                                             # one
                | 'some'                                                            # some
                ;

value           : ('~'|'^'|'*') value                                               # unaryOpValue
                | value '\''                                                        # primeValue
                | value '.' value                                                   # dotJoinValue
                | value '[' value ']'                                               # boxJoinOrFunctionValue
                | value ('<:'|':>') value                                           # restrictionValue
                | value arrowQuant '->' arrowQuant value                            # arrowValue
                | value '&' value                                                   # intersectionValue
                | value '++' value                                                  # relationOverrideValue
                | '#' value                                                         # cardinalityValue
                | value ('+'|'-') value                                             # unionDifferenceValue
                | '{' decl ( ',' decl )+ ( block | bar ) '}'                        # comprehensionValue
                | QNAME '[' value ( ',' value )*  ']'                               # funValue
                | value qname value                                                 # relationOverrideValue
                | 'sum' decl ( ',' decl )+ ( block | bar )                          # quantificationFormula        
                | '(' value ')'                                                     # parenthesisValue                
                | '@' ID                                                            # atnameValue
                | QNAME '$'                                                         # metaValue 
                | '-'? NUMBER                                                       # numberValue
                | 'none'                                                            # noneValue
                | 'univ'                                                            # univValue
                | 'iden'                                                            # idenValue
                | 'this'                                                            # thisValue
                | ID                                                                # nameValue
                | QNAME                                                             # nameValue
                ;
            
formula         : ('no' | 'lone' | 'one' | 'some' | 'set') value                    # multiplicityFormula
                | value neg? ('in' | '=' | '<' | '>' | '=<' | '>=') value           # comparisonFormula
                | ('!' | 'not' | 'always' | 'eventually' | 'after' | 'before'| 'historically' | 'once' ) formula  # unaryFormula
                | formula ( 'releases' | 'since' | 'triggered' ) formula            # binaryFormula
                | formula ('&&' | 'and')? formula                                   # andFormula
                | <assoc=right> formula ('=>'|'implies') formula ('else' formula)?  # ifThenElseFormula
                | formula ('<=>' | 'iff') formula                                   # iffFormula
                | formula ('||'|'or') formula                                       # orFormula
                | ( let | quantification )                                          # letOrQuantificationFormula
                | formula ';' formula                                               # sequenceFormula
                | '(' formula ')'                                                   # parenthesisFormula
                | block                                                             # blockFormula
                | QNAME ('[' value ( ',' value )*  ']' )?                           # predicateFormula
                ;
    
let             : 'let' ID '=' value ( ',' ID '=' value )+ ( block | bar ) ;
quantification  : ('all'|'no'|'lone'|'one'| 'some') decl ( ',' decl )+ ( block | bar )  ;

neg             :  '!' | 'not' ;

arrowQuant      : 'lone'                                                            # loneArrow 
                | 'one'                                                             # oneArrow
                | 'some'                                                            # someArrow
                | 'set'                                                             # setArrow
                | /*empty*/                                                         # setArrow
                ; 

paraDecls       : '(' ( decl ( ',' decl )* )? ')'
                | '[' ( decl ( ',' decl )* )? ']'
                ;
    
names           : ID ( ',' ID )* ;
qname           : ID | QNAME;
block           : '{' formula? '}' ;
bar             : '|' formula ;  

macro           : '{' macro '}'
                | value
                | formula
                ;
            
scope           : 'for' NUMBER ( 'but' typescope ( ',' typescope )* )?
                | 'for' typescope ( ',' typescope )+
                | /* empty */
                ;

typescope       : ('exactly')? NUMBER qname ;

fragment NAME   : [\p{L}\p{Lo}_%][\p{L}\p{Lo}_"0-9%]*;

ID              : [\p{L}\p{Lo}_][\p{L}\p{Lo}_"0-9]*;
QNAME           : NAME ( '/' NAME )+ ;
NUMBER          : [0-9]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
OPTION_COMMENT  : '--'~[0-9] ~[\r\n]* -> skip ; // 1--1 is not a comment
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;
