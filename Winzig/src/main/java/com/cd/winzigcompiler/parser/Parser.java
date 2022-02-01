package com.cd.winzigcompiler.parser;

import com.cd.winzigcompiler.constants.ParserConstants;
import com.cd.winzigcompiler.exceptions.WinzigParserException;
import com.cd.winzigcompiler.scanner.LexicalAnalayer;
import com.cd.winzigcompiler.scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    //TODO : ??
    private LexicalAnalayer lexicalAnalayer;
    private TreeStack treeStack;
    private Token nextToken;
    private Boolean isDerivationTreeEnable;
    //TODO Remove this
    private Stack<String> tempStack = new Stack<>();

    public Parser(LexicalAnalayer lexicalAnalayer) {
        this.lexicalAnalayer = lexicalAnalayer;
        this.treeStack = new TreeStack();
        this.isDerivationTreeEnable = false;
    }

    public Parser(LexicalAnalayer lexicalAnalayer, Boolean isDerivationTreeEnable) {
        this.lexicalAnalayer = lexicalAnalayer;
        this.isDerivationTreeEnable = isDerivationTreeEnable;
        this.treeStack = new TreeStack();
    }

    public Boolean getDerivationTreeEnable() {
        return isDerivationTreeEnable;
    }

    public void setDerivationTreeEnable(Boolean derivationTreeEnable) {
        isDerivationTreeEnable = derivationTreeEnable;
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
//        System.out.println(token + " - " + nextToken.getName() + "\n");
        if (!nextToken.getName().equals(token)) {
            throw new WinzigParserException(WinzigParserException.generateErrorMessage(token));
        } else {
//            if (getDerivationTreeEnable()) {
//                treeStack.push(new TreeNode(nextToken.getName()));
//            }
            if (astParentcondition) {
                tempStack.push(token);
                System.out.println(token + " added to stack : Stack :- " + tempStack);
//                treeStack.push(new TreeNode(token));
                TreeNode parentNode = new TreeNode(type);
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
        tempStack.push(token.getName());
    }

    public void buildAST(String root, int depth) {
        System.out.println("build AST for - " + root + " depth of " + depth + " ?<=" + tempStack.size() + " stack:- " + tempStack);
        TreeNode parentTreeNode = new TreeNode(root);
        ArrayList<TreeNode> children;
        children = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            tempStack.pop();
            children.add(treeStack.pop());
        }
        parentTreeNode.setChildren(children);
        treeStack.push(parentTreeNode);
        tempStack.push(parentTreeNode.getName());
        System.out.println("Pushed - " + parentTreeNode.getName() + " - " + tempStack);;
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
                //TODO Check
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
//        System.out.println("This is constsProcedure " + nextToken.getName());
        int n = 0;
        switch (nextToken.getName()) {
            case "const":
                read("const");
                //TODO Is list can be empty?
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
//        System.out.println("This is constProcedure " + nextToken.getName());
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
//        System.out.println("This is constValueProcedure " + nextToken.getName());
        String type = "";
        if ((nextToken.getInteger() || nextToken.getChar()) || nextToken.getIdentifier()) {
            if (nextToken.getIdentifier()) {
                type = "<identifier>";
            } else if (nextToken.getInteger()) {
                type = "<integer>";
            } else if (nextToken.getChar()) {
                type = "<char>";
            }
            read(nextToken.getName(), true, type);
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }


    /*
    Types      -> 'type' (Type ';')+                      => "types"
           ->
     */
    private void typesProcedure() throws WinzigParserException {
//        System.out.println("This is typesProcedure " + nextToken.getName());
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
//                constProcedure();
//                read(";");
//                buildAST("types", n);
                break;
        }
        buildAST("types", n);
    }

    /*
    Type       -> Name '=' LitList
     */
    private void typeProcedure() throws WinzigParserException {
//        System.out.println("This is typeProcedure " + nextToken.getName());
        nameProcedure();
        read("=");
        litListProcedure();
        buildAST("type", 2);
    }


    /*
        LitList    -> '(' Name list ',' ')'
     */
    private void litListProcedure() throws WinzigParserException {
//        System.out.println("This is litListProcedure " + nextToken.getName());
        int n = 1;
        switch (nextToken.getName()) {
            case "(":
                read("(");
                //TODO Is list can be empty?
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
//        System.out.println("This is subProgsProcedure " + nextToken.getName());
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
//        System.out.println("This is fcnProcedure " + nextToken.getName());
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
//        System.out.println("This is paramsProcedure " + nextToken.getName());
        //TODO Is list can be empty?
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
//        System.out.println("This is dclnsProcedure " + nextToken.getName());
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
//        System.out.println("This is dclnProcedure " + nextToken.getName());
        int n = 1;
        //TODO Is list can be empty?
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
//        System.out.println("This is bodyProcedure " + nextToken.getName());
        int n = 1;
        switch (nextToken.getName()) {
            case "begin":
                read("begin");
                //TODO Is list can be empty?
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
//        System.out.println("This is statementProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        } else {
            int n;
            switch (nextToken.getName()) {
                case "output":
//                    'output' '(' OutExp list ',' ')'        => "output"
                    read("output");
                    read("(");
                    //TODO Is list can be empty?
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
                    //TODO Is list can be empty?
                    statementProcedure();
                    n = 1;
                    while (nextToken.getName().equals(";")) {
                        read(";");
                        statementProcedure();
                        n++;
                    }
                    read("until");
                    expressionProcedure();
                    n +=1;
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
                    //TODO Is list can be empty?
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
                    caseClausesProcedure();
                    otherwiseClauseProcedure();
                    read("end");
                    buildAST("case", 3);
                    break;
                case "read":
//                    'loop' Statement list ';' 'pool'        => "loop"
                    n = 1;
                    read("read");
                    read("(");
                    //TODO Is list can be empty?
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
                    buildAST("null", 0);
            }
        }
    }

    /*
    OutExp     -> Expression                              => "integer"
            -> StringNode
*/
    private void outExpProcedure() throws WinzigParserException {
//        System.out.println("This is outExpProcedure " + nextToken.getName());
        if (nextToken.getString()) {
            stringNodeProcedure();
            buildAST("string", 0);
        } else {
            expressionProcedure();
            buildAST("integer", 0);
        }
    }
    /*
    StringNode -> '<string>';
     */
    private void stringNodeProcedure() throws WinzigParserException {
//        System.out.println("This is stringNodeProcedure " + nextToken.getName());
        if (nextToken.getString()) {
            read(nextToken.getName(), true, "<string>");
        } else {
            generateParserError(nextToken.getName());
        }
    }
    /*
     * Caseclauses-> (Caseclause ';')+;
     * */
    private void caseClausesProcedure() throws WinzigParserException {
//        System.out.println("This is caseClausesProcedure " + nextToken.getName());
        caseClauseProcedure();
        read(";");
        String type = "";
        while (nextToken.getIdentifier() || nextToken.getInteger() || nextToken.getChar()) {
            if (nextToken.getIdentifier()) {
                type = "<identifier>";
            } else if (nextToken.getInteger()) {
                type = "<integer>";
            } else if (nextToken.getChar()) {
                type = "<char>";
            }
            caseClauseProcedure();
            read(";", true, type);
        }
    }
    /*
    Caseclause -> CaseExpression list ',' ':' Statement => "case_clause";
    */
    private void caseClauseProcedure() throws WinzigParserException {
//        System.out.println("This is caseClauseProcedure " + nextToken.getName());
        int n = 1;
        //TODO Is list can be empty?
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
//        System.out.println("This is caseExpressionProcedure " + nextToken.getName());
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
    private void otherwiseClauseProcedure() throws WinzigParserException {
//        System.out.println("This is otherwiseClauseProcedure " + nextToken.getName());
        switch (nextToken.getName()) {
            case "otherwise":
                read("otherwise");
                statementProcedure();
                buildAST("otherwise", 1);
                break;
        }
    }

    /*
Assignment -> Name ':=' Expression                        => "assign"
           -> Name ':=:' Name
 */
    private void assignmentProcedure() throws WinzigParserException {
//        System.out.println("This is assignmentProcedure " + nextToken.getName());
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
//        System.out.println("This is forStatProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            assignmentProcedure();
        } else {
            buildAST("null", 0);
        }
    }

/*
ForExp     -> Expression
           ->
 */
    private void forExpProcedure() throws WinzigParserException {
//        System.out.println("This is forExpProcedure " + nextToken.getName());
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
           ->  Term '<>' Term
     */
    private void expressionProcedure() throws WinzigParserException {
//        System.out.println("This is expressionProcedure " + nextToken.getName());
        termProcedure();
        if (ParserConstants.expressionSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            termProcedure();
            buildAST(nextToken.getName(), 2);
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
//        System.out.println("This is termProcedure " + nextToken.getName());
        factorProcedure();
        while (ParserConstants.termSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            factorProcedure();
            buildAST(nextToken.getName(), 2);
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
//        System.out.println("This is factorProcedure " + nextToken.getName());
        primaryProcedure();
        while (ParserConstants.factorSymbols.contains(nextToken.getName())) {
            read(nextToken.getName());
            primaryProcedure();
            buildAST(nextToken.getName(), 2);
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
//        System.out.println("This is primaryProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            int n = 1;
            nameProcedure();
//            ->  Name '(' Expression list ',' ')'           => "call"
            if (nextToken.getName().equals("(")) {
                read("(");
                //TODO Is list can be empty?
                expressionProcedure();
                while (nextToken.getName().equals(",")) {
                    read(",");
                    expressionProcedure();
                    n++;
                }
                read(")");
                buildAST("call", n);
            }
        } else if (nextToken.getInteger()){
            read(nextToken.getName(), true, "integer");
        } else if (nextToken.getChar()){
            read(nextToken.getName(), true, "<char>");
        } else {
            switch (nextToken.getName()) {
                case "-":
                    read(nextToken.getName());
                    primaryProcedure();
                    buildAST(nextToken.getName(), 1);
                case "not":
                    read(nextToken.getName());
                    primaryProcedure();
                    buildAST(nextToken.getName(), 1);
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
                    read(nextToken.getName());
                    read("(");
                    expressionProcedure();
                    read(")");
                    buildAST(nextToken.getName(), 1);
                    break;
                default:
                    generateParserError(nextToken.getName());
            }
        }
    }

    private void nameProcedure() throws WinzigParserException {
//        System.out.println("This is nameProcedure " + nextToken.getName());
        if (nextToken.getIdentifier()) {
            read(nextToken.getName(), true, "<identifier>");
        } else {
            generateParserError(nextToken.getTokenType() + " :- " + nextToken.getName());
        }
    }
}
