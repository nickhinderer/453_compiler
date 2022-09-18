package translate.vapor;

import symboltable.Symbol;
import symboltable.SymbolTable;
import type.VaporClassType;
import typecheck.TypeCheckVisitor;
import syntaxtree.*;
import syntaxtree.ArrayType;
import syntaxtree.Type;
import visitor.GJDepthFirst;
import type.ClassType;

import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class VaporVisitor extends GJDepthFirst<String[], SymbolTable> {
    SymbolTable symbolTable;

    public VaporVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        initialize();
    }

    public void initialize() {
        symbolTable.classes().forEach((classID, classType) -> VaporClassType.create(classID, classType, symbolTable));
//        symbolTable.classes().forEach(VaporClassType::create);
//        Map<Symbol, VaporClassType> classes = new HashMap<>();
//        for (var entry : symbolTable.classes().entrySet())
//            classes.put(entry.getKey(), VaporClassType.create(entry.getValue())); //new VaporClassType(entry.getValue(), false));
    }

    //
    // Auto class visitors--probably don't need to be overridden.
    //
    public String[] visit(NodeList n, SymbolTable st) {
        String[] _ret = new String[n.nodes.size()];
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            String[] statement = e.nextElement().accept(this, st);
            _ret[_ret.length - 1 - _count] = statement[1] + "\n" + statement[0];
            _count++;
        }
        return _ret;
    }

    public String[] visit(NodeListOptional n, SymbolTable st) {
        if (n.present()) {
            String[] _ret = new String[n.nodes.size()];
            int _count = 0;
            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                String[] statement = e.nextElement().accept(this, st);
                _ret[_count] = statement[1] + "\n" + statement[0];
                _count++;
            }
            return _ret;
        }
        return null;
    }

    public String[] visit(NodeOptional n, SymbolTable st) {
        if (n.present())
            return n.node.accept(this, st);
        else
            return null;
    }

    public String[] visit(NodeSequence n, SymbolTable st) {
        String[] _ret = null;
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this, st);
            _count++;
        }
        return _ret;
    }

    public String[] visit(NodeToken n, SymbolTable st) {
        return null;
    }
    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public String[] visit(Goal n, SymbolTable st) {
        String[] _ret = null;
        st = symbolTable;
        n.f0.accept(this, st);
        if (n.f1.present()) {
            for (Enumeration<Node> e = n.f1.nodes.elements(); e.hasMoreElements(); ) {
                e.nextElement().accept(this, st);
            }
        }
//        for (var entry : st.classes().entrySet()) {
//            if (!entry.getValue().isMain())
//                for (var method : entry.getValue().getMethods().entrySet()) {
//                    System.out.println(method.getValue().vapor.getSignature());
//                    System.out.println(method.getValue().vapor.statements());
//                }
//        }
        st.printVapor();
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public String[] visit(MainClass n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        st.state.methodID = "main";
        String[] statements = n.f15.accept(this, st);
        if (statements != null) {
            for (int i = 0; i < statements.length; ++i) {
                if (statements[i] == null) {
                    statements[i - 1] += "\nret";
                    break;
                }
                if (i == statements.length - 1) {
                    statements[i] += "\nret";
                    break;
                }
            }
        } else {
            statements = new String[]{"\nret"};
        }
        StringBuilder combinedStatements = new StringBuilder();
        for (String statement : statements)
            combinedStatements.append(removeEmptyLines(statement));
        String classID = n.f1.f0.tokenImage;
        st.typeM(classID, "main").vapor.setStatements(combinedStatements.toString());
        return null;
    }

    /**
     * f0 -> ClassDeclaration()
     * | ClassExtendsDeclaration()
     */
    public String[] visit(TypeDeclaration n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String[] visit(ClassDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        return n.f4.accept(this, st);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public String[] visit(ClassExtendsDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        n.f6.accept(this, st);
        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String[] visit(VarDeclaration n, SymbolTable st) {
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public String[] visit(MethodDeclaration n, SymbolTable st) {
        st.state.methodID = n.f2.f0.tokenImage;
        String[] statements = n.f8.accept(this, st);
        StringBuilder methodStatements = new StringBuilder();
        if (statements != null)
            for (String statement : statements) {
                methodStatements.append(removeEmptyLines(statement));
            }
        String[] returnExpression = n.f10.accept(this, st);
        if (needsTemp(returnExpression))
            returnExpression = wrap(returnExpression, st);
        methodStatements.append(returnExpression[1]).append("ret ").append(returnExpression[0]);
        st.typeM(st.state.classID, st.state.methodID).vapor.setStatements(removeEmptyLines(methodStatements.toString()));
        return new String[]{"", ""};
    }

    public static String removeEmptyLines(String text) {
        final String[] strings = text.split("\n");
        StringBuilder result = new StringBuilder();
        for (String str : strings) {
            if (str.isEmpty()) continue;
            result.append(str);
            result.append("\n");
        }
//        for (int i = 0, stringsLength = strings.length; i < stringsLength; i++) {
//            String str = strings[i];
//            if (str.isEmpty()) continue;
//            result.append(str);
//            result.append("\n");
//        }
        return result.toString();
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String[] visit(FormalParameterList n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String[] visit(FormalParameter n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String[] visit(FormalParameterRest n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     * | BooleanType()
     * | IntegerType()
     * | Identifier()
     */
    public String[] visit(Type n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String[] visit(ArrayType n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        n.f2.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public String[] visit(BooleanType n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public String[] visit(IntegerType n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> Block()
     * | AssignmentStatement()
     * | ArrayAssignmentStatement()
     * | IfStatement()
     * | WhileStatement()
     * | PrintStatement()
     */
    public String[] visit(Statement n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public String[] visit(Block n, SymbolTable st) {
        if (n.f1.present()) {
            String[] statements = n.f1.accept(this, st);
            int count = 0;
            StringBuilder block = new StringBuilder();
            for (Enumeration<Node> e = n.f1.elements(); e.hasMoreElements(); ) {
                String[] s = e.nextElement().accept(this, st);
                block.append(s[1]).append("\n").append(s[0]);
                count++;
            }
            return new String[]{block.toString(), ""};
        } else return new String[]{"", ""};
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String[] visit(AssignmentStatement n, SymbolTable st) {
        String[] _ret = null;
        String[] identifier = n.f0.accept(this, st);
        String[] expression = n.f2.accept(this, st);
        if (needsTemp(expression))
            expression = wrap(expression, st);
        String assignment = identifier[0] + " = " + expression[0] + "\n";
        String[] ret = new String[]{assignment, identifier[1] + "\n" + expression[1]};
        String statement = ret[1] + "\n" + ret[0];
        return ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public String[] visit(ArrayAssignmentStatement n, SymbolTable st) {
        String[] _ret = null;
        String[] array = n.f0.accept(this, st);
        String[] index = n.f2.accept(this, st);
        if (needsTemp(index))
            index = wrap(index, st);
        String temporary1 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String temporary2 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String lookup = temporary1 + " = " + array[0] + "\n" +
                temporary2 + " = MulS(" + index[0] + " 4)\n" +
                temporary2 + " = Add(" + temporary1 + " " + temporary2 + ")\n";
        String elementSetup = array[1] + "\n" + index[1] + "\n" + lookup;
        String element = "[" + temporary2 + "+4]";
        String[] value = n.f5.accept(this, st);
        if (needsTemp(value))
            value = wrap(value, st);
        String assignment = elementSetup + "\n" + value[1] + "\n" + element + " = " + value[0] + "\n";
        return new String[]{assignment, ""};
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public String[] visit(IfStatement n, SymbolTable st) {
        String[] _ret = null;
        String[] condition = n.f2.accept(this, st);
        if (needsTemp(condition)) {
            condition = wrap(condition, st);
        }
        int ifCount = st.getIfCounter();
        String[] ifBody = n.f4.accept(this, st);
        String ifBodyStatements = ifBody[1] + '\n' + ifBody[0];
        String[] elseBody = n.f6.accept(this, st);
        String elseBodyStatements = elseBody[1] + '\n' + elseBody[0];
        String ifStatement = condition[1] + "\nif0 " + condition[0] + " goto :if" + ifCount + "_else" +
                ifBodyStatements + "\ngoto :if" + ifCount + "_end\n" +
                "if" + ifCount + "_else:\n" +
                elseBodyStatements + "\nif" + ifCount + "_end:\n";
        return new String[]{ifStatement, ""};
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String[] visit(WhileStatement n, SymbolTable st) {
        String[] _ret = null;
        String[] expression = n.f2.accept(this, st);
        if (needsTemp(expression))
            expression = wrap(expression, st);
        int whileCount = st.getWhileCounter();
        String[] body = n.f4.accept(this, st);
        String loop = "while" + whileCount + "_top:\n" +
                expression[1] + "\n" +
                "if0 " + expression[0] + " goto :while" + whileCount + "_end\n" +
                body[1] + "\n" + body[0] + "\n" +
                "goto :while" + whileCount + "_top\n" +
                "while" + whileCount + "_end:\n";
        return new String[]{loop, ""};
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String[] visit(PrintStatement n, SymbolTable st) {
        String[] expression = n.f2.accept(this, st);
        if (needsTemp(expression))
            expression = wrap(expression, st);
        String print = "PrintIntS(" + expression[0] + ")\n";
        return new String[]{print, expression[1]};
    }

    /**
     * f0 -> AndExpression()
     * | CompareExpression()
     * | PlusExpression()
     * | MinusExpression()
     * | TimesExpression()
     * | ArrayLookup()
     * | ArrayLength()
     * | MessageSend()
     * | PrimaryExpression()
     */
    public String[] visit(Expression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public String[] visit(AndExpression n, SymbolTable st) {
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        int andCount = st.getAndCounter();
        String[] primaryExpression1 = n.f0.accept(this, st);
        if (needsTemp(primaryExpression1))
            primaryExpression1 = wrap(primaryExpression1, st);
        String[] primaryExpression2 = n.f2.accept(this, st);
        if (needsTemp(primaryExpression2))
            primaryExpression2 = wrap(primaryExpression2, st);
        String and = temporary + " = 0\n" +
                "if0 " + primaryExpression1[0] + " goto :and" + andCount + "_end\n" +
                "\tif0 " + primaryExpression2[0] + " goto :and" + andCount + "_end\n" +
                "\t\t" + temporary + " = 1\n" +
                "and" + andCount + "_end:\n";
        return new String[]{temporary, primaryExpression1[1] + "\n" + primaryExpression2[1] + "\n" + and};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String[] visit(CompareExpression n, SymbolTable st) {
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String[] primaryExpression1 = n.f0.accept(this, st);
        if (needsTemp(primaryExpression1))
            primaryExpression1 = wrap(primaryExpression1, st);
        String[] primaryExpression2 = n.f2.accept(this, st);
        if (needsTemp(primaryExpression2))
            primaryExpression2 = wrap(primaryExpression2, st);
        String compare = "LtS(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
        return new String[]{compare, primaryExpression1[1] + "\n" + primaryExpression2[1] + "\n"};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public String[] visit(PlusExpression n, SymbolTable st) {
        String[] primaryExpression1 = n.f0.accept(this, st);
        if (needsTemp(primaryExpression1))
            primaryExpression1 = wrap(primaryExpression1, st);
        String[] primaryExpression2 = n.f2.accept(this, st);
        if (needsTemp(primaryExpression2))
            primaryExpression2 = wrap(primaryExpression2, st);
        String plus = "Add(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String[] visit(MinusExpression n, SymbolTable st) {
        String[] primaryExpression1 = n.f0.accept(this, st);
        if (needsTemp(primaryExpression1))
            primaryExpression1 = wrap(primaryExpression1, st);
        String[] primaryExpression2 = n.f2.accept(this, st);
        if (needsTemp(primaryExpression2))
            primaryExpression2 = wrap(primaryExpression2, st);
        String plus = "Sub(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String[] visit(TimesExpression n, SymbolTable st) {
        String[] primaryExpression1 = n.f0.accept(this, st);
        if (needsTemp(primaryExpression1))
            primaryExpression1 = wrap(primaryExpression1, st);
        String[] primaryExpression2 = n.f2.accept(this, st);
        if (needsTemp(primaryExpression2))
            primaryExpression2 = wrap(primaryExpression2, st);
        String plus = "MulS(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String[] visit(ArrayLookup n, SymbolTable st) {
        String[] _ret = null;
        String[] array = n.f0.accept(this, st);
        String[] index = n.f2.accept(this, st);
        if (needsTemp(index))
            index = wrap(index, st);
        String temporary1 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String temporary2 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String lookup = temporary1 + " = " + array[0] + "\n" +
                temporary2 + " = MulS(" + index[0] + " 4)\n" +
                temporary2 + " = Add(" + temporary1 + " " + temporary2 + ")\n";
        return new String[]{"[" + temporary2 + "+4]", array[1] + "\n" + index[1] + "\n" + lookup};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String[] visit(ArrayLength n, SymbolTable st) {
        String[] array = n.f0.accept(this, st);
        if (needsTemp(array))
            array = wrap(array, st);
        String length = array[0];
        return new String[]{"[" + length + "]", array[1] + "\n"};
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public String[] visit(MessageSend n, SymbolTable st) {
        String[] primaryExpression = n.f0.accept(this, st);
        if (needsTemp(primaryExpression)) {
            primaryExpression = wrap(primaryExpression, st);
        }
        String argsSetup = "";
        ClassType type = null;
        switch (n.f0.f0.which) {
            case 3: {
                String id = ((Identifier) n.f0.f0.choice).f0.tokenImage;
                boolean local = st.isPV(st.state.classID, st.state.methodID, id);
                if (!local) {
                    type = (ClassType) st.typeF(st.state.classID, id);
                } else {
                    type = (ClassType) st.typePV(st.state.classID, st.state.methodID, id);
                }
                break;
            }
            case 4: {
                type = st.typeC(st.state.classID);
                break;
            }
            case 6: {
                String classID = ((AllocationExpression) n.f0.f0.choice).f1.f0.tokenImage;
                type = st.typeC(classID);
                break;
            }
            case 8: {
                BracketExpression be = (BracketExpression) n.f0.f0.choice;
                Expression exp = be.f1;
                if (exp.f0.which == 7) {
                    TypeCheckVisitor tcv = new TypeCheckVisitor();
                    type = (ClassType) exp.f0.accept(tcv, st);
                    String[] messageSend = exp.f0.accept(this, st);
                    argsSetup += messageSend[1];//setup
                    argsSetup += messageSend[0];//call
                } else {
                    PrimaryExpression pe = (PrimaryExpression) exp.f0.choice; //it is type checked, so don't even bother checking if 'which' is 8, it must be a primary expression ('which' == 8, which is confusing because the primaryexpresssion.nodechoice.which is also 8 for bracket expressions, they probably did this on purpose fucking dickheads
                    while (pe.f0.which == 8) {
                        be = (BracketExpression) pe.f0.choice;
                        exp = be.f1;
                        pe = (PrimaryExpression) exp.f0.choice; //again, same as above, it has type checked, so its not going to be addition or anything other than an object nested in brackets which you are un-nesting here
                    }
                    int i = pe.f0.which;
                    switch (i) {
                        case 3: {
                            String id = ((Identifier) pe.f0.choice).f0.tokenImage;
                            boolean local = st.isPV(st.state.classID, st.state.methodID, id);
                            if (!local) {
                                type = (ClassType) st.typeF(st.state.classID, id);
                            } else {
                                type = (ClassType) st.typePV(st.state.classID, st.state.methodID, id);
                            }
                        }
                        case 4: {
                            type = st.typeC(st.state.classID);
                            break;
                        }
                        case 6: {
                            String classID = ((AllocationExpression) pe.f0.choice).f1.f0.tokenImage;
                            type = st.typeC(classID);
                            break;
                        }
                    }
                }
                break;
            }
        }
        String argsList = "";
        if (n.f4.present()) {
            ArrayList<Expression> args = new ArrayList<>();
            ExpressionList n2 = (ExpressionList) n.f4.node;
            args.add(n2.f0);
            if (n2.f1.present()) {
                for (Enumeration<Node> e = n2.f1.elements(); e.hasMoreElements(); )
                    args.add(((ExpressionRest) e.nextElement()).f1);
            }
            StringBuilder argsSetupB = new StringBuilder();
            StringBuilder argsListB = new StringBuilder();
            for (Expression e : args) {
                String[] stmts = e.accept(this, st);
                if (needsTemp(stmts))
                    stmts = wrap(stmts, st);
                argsSetupB.append(stmts[1]).append("\n");
                argsListB.append(' ').append(stmts[0]);
            }
            argsSetup = argsSetupB.toString();
            argsList = argsListB.toString();
        }
        ClassType c = st.table.classes.get(Symbol.symbol(type.classID()));
        VaporClassType vm = c.vapor;
        int methodOffset = vm.getMethodOffset(n.f2.f0.tokenImage);
//        int methodOffset = st.typeM(type.classID(), n.f2.f0.tokenImage).vapor.getOffset();
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String methodSetup = temporary + " = [" + primaryExpression[0] + "]\n" +
                temporary + " = [" + temporary + "+" + methodOffset + "]\n";
        String call = "call " + temporary + "(" + primaryExpression[0] + argsList + ")\n";
        return new String[]{call, primaryExpression[1] + argsSetup + "\n" + methodSetup + "\n"};
//        return new String[]{call, primaryExpression[1] + "\n" + methodSetup + "\n" + argsSetup};
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public String[] visit(ExpressionList n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String[] visit(ExpressionRest n, SymbolTable st) {
        String[] _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     * | TrueLiteral()
     * | FalseLiteral()
     * | Identifier()
     * | ThisExpression()
     * | ArrayAllocationExpression()
     * | AllocationExpression()
     * | NotExpression()
     * | BracketExpression()
     */
    public String[] visit(PrimaryExpression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String[] visit(IntegerLiteral n, SymbolTable st) {
        return new String[]{n.f0.tokenImage, ""};
    }

    /**
     * f0 -> "true"
     */
    public String[] visit(TrueLiteral n, SymbolTable st) {
        return new String[]{"1", ""};
    }

    /**
     * f0 -> "false"
     */
    public String[] visit(FalseLiteral n, SymbolTable st) {
        return new String[]{"0", ""};
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String[] visit(Identifier n, SymbolTable st) {
        boolean b = st.isPV(st.state.classID, st.state.methodID, n.f0.tokenImage);
        if (b)
            return new String[]{n.f0.tokenImage, ""};
        return new String[]{"[this+" + st.typeC(st.state.classID).vapor.fieldOffset(n.f0.tokenImage) * 4 + "]", ""};
    }

    /**
     * f0 -> "this"
     */
    public String[] visit(ThisExpression n, SymbolTable st) {
        return new String[]{n.f0.tokenImage, ""};
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String[] visit(ArrayAllocationExpression n, SymbolTable st) {
        String[] size = n.f3.accept(this, st);
        if (needsTemp(size)) {
            size = wrap(size, st);
        }
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String allocation = temporary + " = call :AllocArray(" + size[0] + ")\n";
        return new String[]{temporary, size[1] + "\n" + allocation};
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String[] visit(AllocationExpression n, SymbolTable st) {
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String allocation = temporary + " = HeapAllocZ(" +
                st.typeC(n.f1.f0.tokenImage).vapor.allocSize() + ")\n" +
                "[" + temporary + "] = :vmt_" + n.f1.f0.tokenImage + "\n";
        //n.f1.accept(this, st);
        return new String[]{temporary, allocation};
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String[] visit(NotExpression n, SymbolTable st) {
        String[] expression = n.f1.accept(this, st);
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        int ifCount = st.getIfCounter();
        if (needsTemp(expression)) {
            expression = wrap(expression, st);
        }
        String not = temporary + " = 1\n" +
                "if0 " + expression[0] + " goto :if" + ifCount + "_end\n" +
                "\t" + temporary + " = 0\n" +
                "if" + ifCount + "_end:\n";
        String setup = expression[1] + not;
        return new String[]{temporary, setup};
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String[] visit(BracketExpression n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

    public static String parseLastLineVariable(String expression) {
        String[] lines = expression.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            if (!lines[i].isBlank()) {
                return expression.split(" ", 2)[0];
            }
        }
        return null;
    }

    public static String[] wrap(String[] expression, SymbolTable st) {
        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
        String bind = temporary + " = " + expression[0] + "\n";
        return new String[]{temporary, expression[1] + "\n" + bind};
    }

    public static boolean needsTemp(String[] expression) {
        if (expression[0].charAt(0) == '[' && expression[0].charAt(expression[0].length() - 1) == ']')
            return true;
        if (expression[0].contains("call "))
            return true;
        return expression[0].contains("LtS(") || expression[0].contains("MulS(") || expression[0].contains("Sub(") || expression[0].contains("Add(");
    }
}
