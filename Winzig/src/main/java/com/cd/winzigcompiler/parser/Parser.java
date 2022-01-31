package com.cd.winzigcompiler.parser;

import com.cd.winzigcompiler.constants.ParserConstants;
import com.cd.winzigcompiler.exceptions.WinzigParserException;
import com.cd.winzigcompiler.scanner.LexicalAnalayer;
import com.cd.winzigcompiler.scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;

public class Parser {

    //TODO : ??
    private LexicalAnalayer lexicalAnalayer;
    private TreeStack treeStack;
    private Token nextToken;

    public Parser(LexicalAnalayer lexicalAnalayer) {
        this.lexicalAnalayer = lexicalAnalayer;
        this.treeStack = new TreeStack();
    }

    public LexicalAnalayer getLexicalAnalayer() {
        return lexicalAnalayer;
    }

    public void setLexicalAnalayer(LexicalAnalayer lexicalAnalayer) {
        this.lexicalAnalayer = lexicalAnalayer;
    }

    public TreeStack getTreeStack() {
        return treeStack;
    }

    public void setTreeStack(TreeStack treeStack) {
        this.treeStack = treeStack;
    }

    public Token getNextToken() {
        return nextToken;
    }

    public void setNextToken(Token nextToken) {
        this.nextToken = nextToken;
    }

    public void read(String token, Boolean condition) throws WinzigParserException {
        System.out.println(token + " - " + nextToken.getName() + "\n");
        if (!condition || !nextToken.getName().equals(token)) {
            throw new WinzigParserException(WinzigParserException.generateErrorMessage(token));
        } else {
            treeStack.push(new TreeNode(nextToken.getName()));
            setNextToken(lexicalAnalayer.getNextToken());
        }
    }

    public void read(String token) throws WinzigParserException {
        read(token, true);
    }

    public void write(Token token, int depth) {
        TreeNode parentTreeNode = new TreeNode(token.getName());
        for (int i = 0; i < depth; i++) {
            parentTreeNode.addChild(treeStack.pop());
        }
        treeStack.push(parentTreeNode);
    }


    private void generateParserError(String errorInput) throws WinzigParserException {
        throw new WinzigParserException(WinzigParserException.generateErrorMessage(errorInput));
    }

    /*
     * Winzig     -> 'program' Name ':' Consts Types Dclns SubProgs Body Name '.' => "program";
     * */
    public void winzigProcedure() throws WinzigParserException {
//        System.out.println("This is winzigProcedure " + nextToken.getName());
        setNextToken(lexicalAnalayer.getNextToken());
        switch (nextToken.getName()) {
            case "program":
                read("program");
                nameProcedure();
                read(":");
                constsProcedure();
                typesProcedure();
                dclnsProcedure();
                subProgsProcedure();
                bodyProcedure();
                nameProcedure();
                read(".");
                if (nextToken != null) {
                    generateParserError(nextToken.getName());
                }
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }

    /*
    Body       -> 'begin' Statement list ';' 'end'
     */
    private void bodyProcedure() throws WinzigParserException {
        System.out.println("This is bodyProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "begin":
                read("begin");
                //TODO Is list can be empty?
                statementProcedure();
                while (nextToken.getName().equals(";")) {
                    read(";");
                    statementProcedure();
                }
                read("end");
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }
/*
    Statement  -> Assignment
           -> 'output' '(' OutExp list ',' ')'        => "output"
            -> 'if' Expression 'then' Statement ('else' Statement)?       => "if"
            -> 'while' Expression 'do' Statement       => "while"
            -> 'repeat' Statement list ';' 'until' Expression                            => "repeat"
            -> 'for' '(' ForStat ';' ForExp ';' ForStat ')' Statement                 => "for"
            -> 'loop' Statement list ';' 'pool'        => "loop"
            -> 'case' Expression 'of' Caseclauses OtherwiseClause 'end'              => "case"
            -> 'read' '(' Name list ',' ')'            => "read"
            -> 'exit'                                  => "exit"
            -> 'return' Expression                     => "return"
            -> Body
           ->
*/
    private void statementProcedure() throws WinzigParserException {
        System.out.println("This is statementProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        } else {
            switch (nextToken.getName()) {
                case "output":
//                    'output' '(' OutExp list ',' ')'        => "output"
                    read("output");
                    read("(");
                    //TODO Is list can be empty?
                    outExpProcedure();
                    while (nextToken.getName().equals(",")) {
                        read(",");
                        outExpProcedure();
                    }
                    read(")");
                    break;
                case "if":
//                    'if' Expression 'then' Statement ('else' Statement)?
                    read("if");
                    expressionProcedure();
                    read("then");
                    statementProcedure();
                    if (nextToken.getName().equals("else")) {
                        read("else");
                        statementProcedure();
                    }
                    break;
                case "while":
//                    'while' Expression 'do' Statement       => "while"
                    read("while");
                    expressionProcedure();
                    read("do");
                    statementProcedure();
                    break;
                case "repeat":
//                    'repeat' Statement list ';' 'until' Expression
                    read("repeat");
                    //TODO Is list can be empty?
                    statementProcedure();
                    while (nextToken.getName().equals(";")) {
                        read(";");
                        statementProcedure();
                    }
                    read("until");
                    expressionProcedure();
                    break;
                case "for":
//                    'for' '(' ForStat ';' ForExp ';' ForStat ')' Statement                 => "for"
                    read("for");
                    read("(");
                    forStatProcedure();
                    read(";");
                    forExpProcedure();
                    read(";");
                    forStatProcedure();
                    read(")");
                    statementProcedure();
                    break;
                case "loop":
//                    'loop' Statement list ';' 'pool'        => "loop"
                    read("loop");
                    //TODO Is list can be empty?
                    statementProcedure();
                    while (nextToken.getName().equals(";")) {
                        read(";");
                        statementProcedure();
                    }
                    read("pool");
                    break;
                case "case":
//                    'case' Expression 'of' Caseclauses OtherwiseClause 'end'              => "case"
                    read("case");
                    expressionProcedure();
                    read("of");
                    caseClausesProcedure();
                    otherwiseClauseProcedure();
                    read("end");
                    break;
                case "read":
//                    'loop' Statement list ';' 'pool'        => "loop"
                    read("read");
                    read("(");
                    //TODO Is list can be empty?
                    nameProcedure();
                    while (nextToken.getName().equals(",")) {
                        read(",");
                        nameProcedure();
                    }
                    read(")");
                    break;
                case "exit":
                    read("exit");
                    break;
                case "return":
                    read("return");
                    expressionProcedure();
                    break;
                case "begin":
                    bodyProcedure();
                    break;
            }
        }
    }
/*
    OtherwiseClause -> 'otherwise' Statement                       => "otherwise"
            -> ;
*/
    private void otherwiseClauseProcedure() throws WinzigParserException {
        System.out.println("This is otherwiseClauseProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "otherwise":
                read("otherwise");
                statementProcedure();
                break;
        }
    }
/*
ForExp     -> Expression
           ->
 */
    private void forExpProcedure() throws WinzigParserException {
        System.out.println("This is forExpProcedure " + nextToken.getName());
        if (ParserConstants.primaryBegins.contains(nextToken.getName()) || nextToken.getIdentifier()
                || nextToken.getInteger() || nextToken.getChar()) {
            expressionProcedure();
        }
    }
    /*
    ForStat    -> Assignment
               ->
     */
    private void forStatProcedure() throws WinzigParserException {
        System.out.println("This is forStatProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        }
    }

    /*
    Expression ->  Term
           ->  Term '<=' Term                             => "<="
           ->  Term '<' Term                              => "<"
           ->  Term '>=' Term                             => ">="
           ->  Term '>' Term                              => ">"
           ->  Term '=' Term                              => "="
           ->  Term '<>' Term
     */
    private void expressionProcedure() throws WinzigParserException {
        System.out.println("This is expressionProcedure " + nextToken.getName());
        termProcedure();
        if (ParserConstants.expressionSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            termProcedure();
        }
    }

    /*
    Term       ->  Factor
               ->  Term '+' Factor                            => "+"
               ->  Term '-' Factor                            => "-"
               ->  Term 'or' Factor
     */
    //TODO: Double Check
    private void termProcedure() throws WinzigParserException {
        System.out.println("This is termProcedure " + nextToken.getName());
        factorProcedure();
        while (ParserConstants.termSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            factorProcedure();
        }
    }

    /*
    Factor     ->  Factor '*' Primary                         => "*"
               ->  Factor '/' Primary                         => "/"
               ->  Factor 'and' Primary                       => "and"
               ->  Factor 'mod' Primary                       => "mod"
               ->  Primary;
     */
    private void  factorProcedure() throws WinzigParserException {
        System.out.println("This is factorProcedure " + nextToken.getName());
        primaryProcedure();
        while (ParserConstants.factorSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            primaryProcedure();
        }
    }
    /*
    Primary    ->  '-' Primary                                => "-"
               ->  '+' Primary
               ->  'not' Primary                              => "not"
               ->  'eof'                                      => "eof"
               ->  Name
               ->  '<integer>'
               ->  '<char>'
               ->  Name '(' Expression list ',' ')'           => "call"
               ->  '(' Expression ')'
               ->  'succ' '(' Expression ')'                  => "succ"
               ->  'pred' '(' Expression ')'                  => "pred"
               ->  'chr' '(' Expression ')'                   => "chr"
               -> 'ord' '(' Expression ')' => "ord";
     */
    public void primaryProcedure() throws WinzigParserException {
//        setNextToken(lexicalAnalayer.getNextToken());
        System.out.println("This is primaryProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            nameProcedure();
//            ->  Name '(' Expression list ',' ')'           => "call"
            if (nextToken.getName().equals("(")) {
                System.out.println("AAA1");
                read("(");
                //TODO Is list can be empty?
                expressionProcedure();
                while (nextToken.getName().equals(",")) {
                    read(",");
                    expressionProcedure();
                }
                System.out.println("AAA2");
                read(")");
            }
        } else if (nextToken.getInteger()){
            read(nextToken.getName());
        } else if (nextToken.getChar()){
            read(nextToken.getName());
        } else {
            switch (nextToken.getName()) {
                case "-":
                case "+":
                case "not":
                    read(nextToken.getName());
                    primaryProcedure();
                    break;
                case "eof":
                    read("eof");
                    break;
                case "(":
                    read("(");
                    expressionProcedure();
                    read(")");
                    break;
                case "succ":
                case "pred":
                case "chr":
                case "ord":
                    read(nextToken.getName());
                    read("(");
                    expressionProcedure();
                    read(")");
                    break;
                default:
                    generateParserError(nextToken.getName());
            }
        }
    }

    /*
    Assignment -> Name ':=' Expression                        => "assign"
               -> Name ':=:' Name
     */
    private void assignmentProcedure() throws WinzigParserException {
        System.out.println("This is assignmentProcedure " + nextToken.getName());
        nameProcedure();
        switch (nextToken.getName()) {
            case ":=":
                read(":=");
                expressionProcedure();
                break;
            case ":=:":
                read(":=:");
                nameProcedure();
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }
/*
    OutExp     -> Expression                              => "integer"
            -> StringNode
*/
    private void outExpProcedure() throws WinzigParserException {
        System.out.println("This is outExpProcedure " + nextToken.getName());
        if (nextToken.getString()) {
            stringNodeProcedure();
        } else {
            expressionProcedure();
        }
    }
    /*
    StringNode -> '<string>';
     */
    private void stringNodeProcedure() throws WinzigParserException {
        System.out.println("This is stringNodeProcedure " + nextToken.getName());
        if (nextToken.getString()) {
            read(nextToken.getName());
        } else {
            generateParserError(nextToken.getName());
        }
    }
    /*
    * Caseclauses-> (Caseclause ';')+;
    * */
    private void caseClausesProcedure() throws WinzigParserException {
        System.out.println("This is caseClausesProcedure " + nextToken.getName());
        caseClauseProcedure();
        read(";");
        while (nextToken.getIdentifier() || nextToken.getInteger() || nextToken.getChar()) {
            caseClauseProcedure();
            read(";");
        }
    }
    /*
    Caseclause -> CaseExpression list ',' ':' Statement => "case_clause";
    */
    private void caseClauseProcedure() throws WinzigParserException {
        System.out.println("This is caseClauseProcedure " + nextToken.getName());
        //TODO Is list can be empty?
        caseExpressionProcedure();
        while (nextToken.getName().equals(",")) {
            read(",");
            caseExpressionProcedure();
        }
        read(":");
        statementProcedure();
    }
/*
CaseExpression -> ConstValue
           -> ConstValue '..' ConstValue
 */
    private void caseExpressionProcedure() throws WinzigParserException {
        System.out.println("This is caseExpressionProcedure " + nextToken.getName());
        constValueProcedure();
        if (nextToken.getName().equals("..")) {
            read("..");
            constValueProcedure();
        }
    }

    /*
    Types      -> 'type' (Type ';')+                      => "types"
           ->
     */
    private void typesProcedure() throws WinzigParserException {
        System.out.println("This is typesProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "type":
                read("type");
                typeProcedure();
                read(";");
                while (nextToken.getIdentifier()) {
                    typeProcedure();
                    read(";");
                }
//                constProcedure();
//                read(";");
                break;
        }
    }

    /*
    Type       -> Name '=' LitList
     */
    private void typeProcedure() throws WinzigParserException {
        System.out.println("This is typeProcedure " + nextToken.getName());
        nameProcedure();
        read("=");
        litListProcedure();
    }

    /*
        LitList    -> '(' Name list ',' ')'
     */
    private void litListProcedure() throws WinzigParserException {
        System.out.println("This is litListProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "(":
                read("(");
                //TODO Is list can be empty?
                nameProcedure();
                while (nextToken.getName().equals(",")) {
                    read(",");
                    nameProcedure();
                }
                read(")");
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }

    /*
        SubProgs   -> Fcn*
     */

    public void subProgsProcedure() throws WinzigParserException {
        System.out.println("This is subProgsProcedure " + nextToken.getName());
        while (nextToken.getName().equals("function")) {
            fcnProcedure();
        }
    }
/*
    Fcn        -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';'    => "fcn";
 */
    private void fcnProcedure() throws WinzigParserException {
        System.out.println("This is fcnProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "function":
                read("function");
                nameProcedure();
                read("(");
                paramsProcedure();
                read(")");
                read(":");
                nameProcedure();
                read(";");
                constsProcedure();
                typesProcedure();
                dclnsProcedure();
                bodyProcedure();
                nameProcedure();
                read(";");
                break;
            default:
                generateParserError(nextToken.getName());

        }
    }

    /*
        Params     -> Dcln list ';'
     */

    private void paramsProcedure() throws WinzigParserException {
        System.out.println("This is paramsProcedure " + nextToken.getName());
        //TODO Is list can be empty?
        dclnProcedure();
        while (nextToken.getName().equals(";")) {
            read(";");
            dclnProcedure();
        }
    }

    /*
    Dclns      -> 'var' (Dcln ';')+                       => "dclns"
           ->
     */
    private void dclnsProcedure() throws WinzigParserException {
        System.out.println("This is dclnsProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "var":
                read("var");
                dclnProcedure();
                read(";");
                while (nextToken.getIdentifier()) {
                    dclnProcedure();
                    read(";");
                }
        }
    }

    /*
        Dcln       -> Name list ',' ':' Name
     */
    private void dclnProcedure() throws WinzigParserException {
        System.out.println("This is dclnProcedure " + nextToken.getName());
        //TODO Is list can be empty?
        nameProcedure();
        while (nextToken.getName().equals(",")) {
            read(",");
            nameProcedure();
        }
        read(":");
        nameProcedure();
    }

    /*
    Consts -> 'const' Const list ',' ';'  => "consts"
           ->                             => "consts";
     */
    private void constsProcedure() throws WinzigParserException {
        System.out.println("This is constsProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "const":
                read("const");
                //TODO Is list can be empty?
                constProcedure();
                while (nextToken.getName().equals(",")) {
                    read(",");
                    constProcedure();
                }
                read(";");
                break;
        }
    }

    /*
        Const -> Name '=' ConstValue
    */
    private void constProcedure() throws WinzigParserException {
        System.out.println("This is constProcedure " + nextToken.getName());
        nameProcedure();
        read("=");
        constValueProcedure();
    }

    private void constValueProcedure() throws WinzigParserException {
        System.out.println("This is constValueProcedure " + nextToken.getName());
        if ((nextToken.getInteger() || nextToken.getChar()) || nextToken.getIdentifier()) {
            read(nextToken.getName());
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }

    private void nameProcedure() throws WinzigParserException {
        System.out.println("This is nameProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            read(nextToken.getName());
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }
}
