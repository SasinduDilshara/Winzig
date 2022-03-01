package com.cd.winzigcompiler.parser;

import com.cd.winzigcompiler.constants.ParserConstants;
import com.cd.winzigcompiler.exceptions.WinzigParserException;
import com.cd.winzigcompiler.scanner.LexicalAnalayer;
import com.cd.winzigcompiler.scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Parser {

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

    public void read(String token, Boolean astParentcondition, String type) throws WinzigParserException {
        if (!nextToken.getName().equals(token)) {
            throw new WinzigParserException(WinzigParserException.generateErrorMessage(token));
        } else {
            if (astParentcondition) {
                TreeNode parentNode = new TreeNode(type);
                if (type.contains("char")) {
                    token = "'" + token + "'";
                }
                parentNode.addChild(new TreeNode(token));
                treeStack.push(parentNode);
            }
            setNextToken(lexicalAnalayer.getNextToken());
        }
    }

    public void read(String token) throws WinzigParserException {
        read(token, false, null);
    }

    public void write(Token token, int depth) {
        TreeNode parentTreeNode = new TreeNode(token.getName());
        for (int i = 0; i < depth; i++) {
            parentTreeNode.addChild(treeStack.pop());
        }
    }

    public void buildAST(String root, int depth) {
        TreeNode parentTreeNode = new TreeNode(root);
        TreeNode[] childNodes = new TreeNode[depth];
        for (int i = 0; i < depth; i++) {
            childNodes[depth - 1 - i] = treeStack.pop();
        }
        parentTreeNode.setChildren(new ArrayList<>(Arrays.asList(childNodes)));
        treeStack.push(parentTreeNode);
    }

    private void generateParserError(String errorInput) throws WinzigParserException {
        throw new WinzigParserException(WinzigParserException.generateErrorMessage(errorInput));
    }

    /*
     * Winzig     -> 'program' Name ':' Consts Types Dclns SubProgs Body Name '.' => "program";
     * */
    public void winzigProcedure() throws WinzigParserException {
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
                buildAST("program", 7);
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }

    /*
    Consts -> 'const' Const list ',' ';'  => "consts"
           ->                             => "consts";
     */
    private void constsProcedure() throws WinzigParserException {
        int n = 0;
        switch (nextToken.getName()) {
            case "const":
                read("const");
                
                constProcedure();
                n++;
                while (nextToken.getName().equals(",")) {
                    read(",");
                    constProcedure();
                    n++;
                }
                read(";");
                break;
        }
        buildAST("consts", n);
    }

    /*
    Const -> Name '=' ConstValue
    */
    private void constProcedure() throws WinzigParserException {
        nameProcedure();
        read("=");
        constValueProcedure();
        buildAST("const", 2);
    }

    /*
    ConstValue -> '<integer>'
           -> '<char>'
           -> Name;
     */

    private void constValueProcedure() throws WinzigParserException {
        String type = "";
        if ((nextToken.getInteger() || nextToken.getChar()) || nextToken.getIdentifier()) {
            if (nextToken.getIdentifier()) {
                nameProcedure();
            } else {
                if (nextToken.getInteger()) {
                    type = "<integer>";
                    read(nextToken.getName(), true, type);
                } else if (nextToken.getChar()) {
                    type = "<char>";
                    read(nextToken.getName(), true, type);
                } else {
                    nameProcedure();
                }
            }
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }


    /*
    Types      -> 'type' (Type ';')+                      => "types"
           ->
     */
    private void typesProcedure() throws WinzigParserException {
        int n = 0;
        switch (nextToken.getName()) {
            case "type":
                read("type");
                typeProcedure();
                read(";");
                n++;
                while (nextToken.getIdentifier()) {
                    typeProcedure();
                    read(";");
                    n++;
                }
                break;
        }
        buildAST("types", n);
    }

    /*
    Type       -> Name '=' LitList
     */
    private void typeProcedure() throws WinzigParserException {
        nameProcedure();
        read("=");
        litListProcedure();
        buildAST("type", 2);
    }


    /*
        LitList    -> '(' Name list ',' ')'
     */
    private void litListProcedure() throws WinzigParserException {
        int n = 1;
        switch (nextToken.getName()) {
            case "(":
                read("(");
                
                nameProcedure();
                while (nextToken.getName().equals(",")) {
                    read(",");
                    nameProcedure();
                    n++;
                }
                read(")");
                buildAST("lit", n);
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }

    /*
        SubProgs   -> Fcn*
     */

    public void subProgsProcedure() throws WinzigParserException {
        int n = 0;
        while (nextToken.getName().equals("function")) {
            n++;
            fcnProcedure();
        }
        buildAST("subprogs", n);
    }

    /*
    Fcn        -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';'    => "fcn";
 */
    private void fcnProcedure() throws WinzigParserException {
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
                buildAST("fcn", 8);
                break;
            default:
                generateParserError(nextToken.getName());

        }
    }

        /*
        Params     -> Dcln list ';'
     */

    private void paramsProcedure() throws WinzigParserException {
        
        int n = 1;
        dclnProcedure();
        while (nextToken.getName().equals(";")) {
            read(";");
            dclnProcedure();
            n++;
        }
        buildAST("params", n);
    }


    /*
    Dclns      -> 'var' (Dcln ';')+                       => "dclns"
           ->
     */
    private void dclnsProcedure() throws WinzigParserException {
        int n = 0;
        switch (nextToken.getName()) {
            case "var":
                read("var");
                dclnProcedure();
                read(";");
                n++;
                while (nextToken.getIdentifier()) {
                    dclnProcedure();
                    read(";");
                    n++;
                }
        }
        buildAST("dclns", n);
    }

    /*
        Dcln       -> Name list ',' ':' Name
     */
    private void dclnProcedure() throws WinzigParserException {
        int n = 1;
        
        nameProcedure();
        while (nextToken.getName().equals(",")) {
            read(",");
            nameProcedure();
            n++;
        }
        read(":");
        nameProcedure();
        n++;
        buildAST("var", n);
    }


    /*
    Body       -> 'begin' Statement list ';' 'end'
     */
    private void bodyProcedure() throws WinzigParserException {
        int n = 1;
        switch (nextToken.getName()) {
            case "begin":
                read("begin");
                
                statementProcedure();
                while (nextToken.getName().equals(";")) {
                    read(";");
                    statementProcedure();
                    n++;
                }
                read("end");
                buildAST("block", n);
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
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        } else {
            int n;
            switch (nextToken.getName()) {
                case "output":
//                    'output' '(' OutExp list ',' ')'        => "output"
                    read("output");
                    read("(");
                    
                    outExpProcedure();
                    n = 1;
                    while (nextToken.getName().equals(",")) {
                        read(",");
                        outExpProcedure();
                        n++;
                    }
                    read(")");
                    buildAST("output", n);
                    break;
                case "if":
//                    'if' Expression 'then' Statement ('else' Statement)?
                    read("if");
                    expressionProcedure();
                    read("then");
                    statementProcedure();
                    n = 2;
                    if (nextToken.getName().equals("else")) {
                        read("else");
                        statementProcedure();
                        n = 3;
                    }
                    buildAST("if", n);
                    break;
                case "while":
//                    'while' Expression 'do' Statement       => "while"
                    read("while");
                    expressionProcedure();
                    read("do");
                    statementProcedure();
                    buildAST("while", 2);
                    break;
                case "repeat":
//                    'repeat' Statement list ';' 'until' Expression
                    read("repeat");
                    
                    statementProcedure();
                    n = 1;
                    while (nextToken.getName().equals(";")) {
                        read(";");
                        statementProcedure();
                        n++;
                    }
                    read("until");
                    expressionProcedure();
                    n += 1;
                    buildAST("repeat", n);
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
                    buildAST("for", 4);
                    break;
                case "loop":
                    n = 1;
//                    'loop' Statement list ';' 'pool'        => "loop"
                    read("loop");
                    
                    statementProcedure();
                    while (nextToken.getName().equals(";")) {
                        read(";");
                        statementProcedure();
                        n++;
                    }
                    read("pool");
                    buildAST("loop", n);
                    break;
                case "case":
//                    'case' Expression 'of' Caseclauses OtherwiseClause 'end'              => "case"
                    read("case");
                    expressionProcedure();
                    read("of");
                    int numberOfCaseClauses = caseClausesProcedure();
                    int isOtherClauseAvailable = otherwiseClauseProcedure();
                    read("end");
                    buildAST("case", numberOfCaseClauses + isOtherClauseAvailable + 1);
                    break;
                case "read":
//                    'loop' Statement list ';' 'pool'        => "loop"
                    n = 1;
                    read("read");
                    read("(");
                    
                    nameProcedure();
                    while (nextToken.getName().equals(",")) {
                        n++;
                        read(",");
                        nameProcedure();
                    }
                    read(")");
                    buildAST("read", n);
                    break;
                case "exit":
                    read("exit");
                    buildAST("exit", 0);
                    break;
                case "return":
                    read("return");
                    expressionProcedure();
                    buildAST("return", 1);
                    break;
                case "begin":
                    bodyProcedure();
                    break;
                default:
                    buildAST("<null>", 0);
            }

        }
    }

    /*
    OutExp     -> Expression                              => "integer"
            -> StringNode
*/
    private void outExpProcedure() throws WinzigParserException {
        if (nextToken.getString()) {
            stringNodeProcedure();
            buildAST("string", 1);
        } else {
            expressionProcedure();
            buildAST("integer", 1);
        }
    }
    /*
    StringNode -> '<string>';
     */
    private void stringNodeProcedure() throws WinzigParserException {
        if (nextToken.getString()) {
            read(nextToken.getName(), true, "<string>");
        } else {
            generateParserError(nextToken.getName());
        }
    }
    /*
     * Caseclauses-> (Caseclause ';')+;
     * */
    private int caseClausesProcedure() throws WinzigParserException {
        int n = 0;
        caseClauseProcedure();
        n++;
        read(";");
        while (nextToken.getIdentifier() || nextToken.getInteger() || nextToken.getChar()) {
            caseClauseProcedure();
            read(";");
            n++;
        }
        return n;
    }
    /*
    Caseclause -> CaseExpression list ',' ':' Statement => "case_clause";
    */
    private void caseClauseProcedure() throws WinzigParserException {
        int n = 1;
        
        caseExpressionProcedure();
        while (nextToken.getName().equals(",")) {
            read(",");
            caseExpressionProcedure();
            n++;
        }
        read(":");
        statementProcedure();
        n +=1;
        buildAST("case_clause", n);
    }
    /*
    CaseExpression -> ConstValue
               -> ConstValue '..' ConstValue
     */
    private void caseExpressionProcedure() throws WinzigParserException {
        constValueProcedure();
        if (nextToken.getName().equals("..")) {
            read("..");
            constValueProcedure();
            buildAST("..", 2);
        }
    }

/*
    OtherwiseClause -> 'otherwise' Statement                       => "otherwise"
            -> ;
*/
    private int otherwiseClauseProcedure() throws WinzigParserException {
        switch (nextToken.getName()) {
            case "otherwise":
                read("otherwise");
                statementProcedure();
                buildAST("otherwise", 1);
                return 1;
        }
        return 0;
    }

    /*
Assignment -> Name ':=' Expression                        => "assign"
           -> Name ':=:' Name
 */
    private void assignmentProcedure() throws WinzigParserException {
        nameProcedure();
        switch (nextToken.getName()) {
            case ":=":
                read(":=");
                expressionProcedure();
                buildAST("assign", 2);
                break;
            case ":=:":
                read(":=:");
                nameProcedure();
                buildAST("swap", 2);
                break;
            default:
                generateParserError(nextToken.getName());
        }
    }


    /*
ForStat    -> Assignment
           ->
 */
    private void forStatProcedure() throws WinzigParserException {
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        } else {
            buildAST("<null>", 0);
        }
    }

/*
ForExp     -> Expression
           ->
 */
    private void forExpProcedure() throws WinzigParserException {
        if (ParserConstants.primaryBegins.contains(nextToken.getName()) || nextToken.getIdentifier()
                || nextToken.getInteger() || nextToken.getChar()) {
            expressionProcedure();
        } else {
            buildAST("true", 0);
        }
    }

    /*
    Expression ->  Term
           ->  Term '<=' Term                             => "<="
           ->  Term '<' Term                              => "<"
           ->  Term '>=' Term                             => ">="
           ->  Term '>' Term                              => ">"
           ->  Term '=' Term                              => "="
           ->  Term '<>' Term                             => "<>"
     */
    private void expressionProcedure() throws WinzigParserException {
        termProcedure();
        if (ParserConstants.expressionSymbols.contains(nextToken.getName())) {
            String treeNodeName = nextToken.getName();
            read(treeNodeName);
            termProcedure();
            buildAST(treeNodeName, 2);
        }
    }

    /*
    Term   ->  Factor
           ->  Term '+' Factor                            => "+"
           ->  Term '-' Factor                            => "-"
           ->  Term 'or' Factor                           => "or";
     */
    private void termProcedure() throws WinzigParserException {
        factorProcedure();
        while (ParserConstants.termSymbols.contains(nextToken.getName())) {
            String treeNodeName = nextToken.getName();
            read(treeNodeName);
            factorProcedure();
            buildAST(treeNodeName, 2);
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
        primaryProcedure();
        while (ParserConstants.factorSymbols.contains(nextToken.getName())) {
            String treeNodeName = nextToken.getName();
            read(treeNodeName);
            primaryProcedure();
            buildAST(treeNodeName, 2);
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
        if (nextToken.getIdentifier()) {
            int n = 1;
            nameProcedure();
            if (nextToken.getName().equals("(")) {
                read("(");
                
                expressionProcedure();
                n++;
                while (nextToken.getName().equals(",")) {
                    read(",");
                    expressionProcedure();
                    n++;
                }
                read(")");
                buildAST("call", n);
            }
        } else if (nextToken.getInteger()){
            read(nextToken.getName(), true, "<integer>");
        } else if (nextToken.getChar()){
            read(nextToken.getName(), true, "<char>");
        } else {
            switch (nextToken.getName()) {
                case "-":
                    read(nextToken.getName());
                    primaryProcedure();
                    buildAST("-", 1);
                    break;
                case "not":
                    read(nextToken.getName());
                    primaryProcedure();
                    buildAST("not", 1);
                    break;
                case "+":
                    read(nextToken.getName());
                    primaryProcedure();
                    break;
                case "eof":
                    read("eof");
                    buildAST("eof", 0);
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
                    String treeNodeName = nextToken.getName();
                    read(treeNodeName);
                    read("(");
                    expressionProcedure();
                    read(")");
                    buildAST(treeNodeName, 1);
                    break;
                default:
                    generateParserError(nextToken.getName());
            }
        }
    }

    private void nameProcedure() throws WinzigParserException {
        if (nextToken.getIdentifier()) {
            read(nextToken.getName(), true, "<identifier>");
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }
}
