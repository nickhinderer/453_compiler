//package vapor;
//
//import Type.ClassType;
//import Type.PrimitiveType;
//import SymbolTable.Symbol;
//import SymbolTable.SymbolTable;
//import syntaxtree.*;
//import visitor.GJDepthFirst;
//import visitor.GJVisitor;
//
//import java.util.*;
//
///**
// * Provides default methods which visit each node in the tree in depth-first
// * order.  Your visitors may extend this class.
// */
//
//public class VVisitor extends GJDepthFirst<String[], SymbolTable> {
//    SymbolTable symbolTable;
//
//    public VVisitor(SymbolTable symbolTable) {
//        this.symbolTable = symbolTable;
//        initialize();
//    }
//
//    public void initialize() {
//        Map<Symbol, VaporClassType> classes = new HashMap<>();
//        for (var entry : symbolTable.classes().entrySet())
//            if (!entry.getValue().isMain())
//                classes.put(entry.getKey(), new VaporClassType(entry.getValue()));
////        Map<Symbol, VaporClassType> functions = new HashMap();
//    }
//
//
//    //
//    // Auto class visitors--probably don't need to be overridden.
//    //
//    public String[] visit(NodeList n, SymbolTable st) {
//        String[] _ret = new String[n.nodes.size()];
//        int _count = 0;
//        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//            String[] statement = e.nextElement().accept(this, st);
//            _ret[_ret.length - 1 - _count] = statement[1] + "\n" + statement[0];
//            _count++;
//        }
//        return _ret;
//    }
//
//    public String[] visit(NodeListOptional n, SymbolTable st) {
////        if (n.present()) {
////            String[] _ret = null;
////            int _count = 0;
////            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
////                String[] s = e.nextElement().accept(this, st);
////                _count++;
////            }
////            return _ret;
////        } else
////            return null;
//        if (n.present()) {
//            String[] _ret = new String[n.nodes.size()];
//            int _count = 0;
//            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//                String[] statement = e.nextElement().accept(this, st);
//                _ret[_count] = statement[1] + "\n" + statement[0];
//                _count++;
//            }
//            return _ret;
//        }
//        return null;
//
//    }
//
//    public String[] visit(NodeOptional n, SymbolTable st) {
//        if (n.present())
//            return n.node.accept(this, st);
//        else
//            return null;
//    }
//
//    public String[] visit(NodeSequence n, SymbolTable st) {
//        String[] _ret = null;
//        int _count = 0;
//        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//            e.nextElement().accept(this, st);
//            _count++;
//        }
//        return _ret;
//    }
//
//    public String[] visit(NodeToken n, SymbolTable st) {
//        return null;
//    }
//
//    //
//    // User-generated visitor methods below
//    //
//
//    /**
//     * f0 -> MainClass()
//     * f1 -> ( TypeDeclaration() )*
//     * f2 -> <EOF>
//     */
//    public String[] visit(Goal n, SymbolTable st) {
//        String[] _ret = null;
//        st = symbolTable;
//        //n.f0.accept(this, st);
//
//        if (n.f1.present()) {
//            for (Enumeration<Node> e = n.f1.nodes.elements(); e.hasMoreElements(); ) {
//                e.nextElement().accept(this, st);
//            }
//        }
//
////        n.f1.accept(this, st);
//        for (var entry : st.classes().entrySet()) {
//            if (!entry.getValue().isMain())
//                for (var method : entry.getValue().getMethods().entrySet()) {
//                    System.out.println(method.getValue().vapor.getSignature());
//                    System.out.println(method.getValue().vapor.statements());
//                }
////            entry.getValue().getMethods().forEach((key, value) -> {
////                System.out.println(value.vapor.getSignature());
////                System.out.println(value.vapor.statements());
////            });
//        }
//        return _ret;
//    }
//
//    /**
//     * f0 -> "class"
//     * f1 -> Identifier()
//     * f2 -> "{"
//     * f3 -> "public"
//     * f4 -> "static"
//     * f5 -> "void"
//     * f6 -> "main"
//     * f7 -> "("
//     * f8 -> "String"
//     * f9 -> "["
//     * f10 -> "]"
//     * f11 -> Identifier()
//     * f12 -> ")"
//     * f13 -> "{"
//     * f14 -> ( VarDeclaration() )*
//     * f15 -> ( Statement() )*
//     * f16 -> "}"
//     * f17 -> "}"
//     */
//    public String[] visit(MainClass n, SymbolTable st) {
//        String[] _ret = null;
////        n.f1.accept(this, st);
//        n.f15.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> ClassDeclaration()
//     * | ClassExtendsDeclaration()
//     */
//    public String[] visit(TypeDeclaration n, SymbolTable st) {
//        return n.f0.accept(this, st);
//    }
//
//    /**
//     * f0 -> "class"
//     * f1 -> Identifier()
//     * f2 -> "{"
//     * f3 -> ( VarDeclaration() )*
//     * f4 -> ( MethodDeclaration() )*
//     * f5 -> "}"
//     */
//    public String[] visit(ClassDeclaration n, SymbolTable st) {
//        String[] _ret = null;
////        n.f1.accept(this, st);
////        n.f2.accept(this, st);
//        st.state.classID = n.f1.f0.tokenImage;
//        return n.f4.accept(this, st);
//    }
//
//    /**
//     * f0 -> "class"
//     * f1 -> Identifier()
//     * f2 -> "extends"
//     * f3 -> Identifier()
//     * f4 -> "{"
//     * f5 -> ( VarDeclaration() )*
//     * f6 -> ( MethodDeclaration() )*
//     * f7 -> "}"
//     */
//    public String[] visit(ClassExtendsDeclaration n, SymbolTable st) {
//        String[] _ret = null;
////        n.f1.accept(this, st);
////        n.f3.accept(this, st);
////        n.f5.accept(this, st);
//        st.state.classID = n.f1.f0.tokenImage;
//        n.f6.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> Type()
//     * f1 -> Identifier()
//     * f2 -> ";"
//     */
//    public String[] visit(VarDeclaration n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> "public"
//     * f1 -> Type()
//     * f2 -> Identifier()
//     * f3 -> "("
//     * f4 -> ( FormalParameterList() )?
//     * f5 -> ")"
//     * f6 -> "{"
//     * f7 -> ( VarDeclaration() )*
//     * f8 -> ( Statement() )*
//     * f9 -> "return"
//     * f10 -> Expression()
//     * f11 -> ";"
//     * f12 -> "}"
//     */
//    public String[] visit(MethodDeclaration n, SymbolTable st) {
//        String[] _ret = null;
////        n.f1.accept(this, st);
////        n.f2.accept(this, st);
////        n.f4.accept(this, st);
////        n.f7.accept(this, st);
//        st.state.methodID = n.f2.f0.tokenImage;
//        String[] statements = n.f8.accept(this, st);
//        StringBuilder methodStatements = new StringBuilder();
//        if (statements != null)
//            for (String statement : statements) {
//                methodStatements.append(removeEmptyLines(statement));
////            methodStatements.append(removeEmptyLines(statement));
////            methodStatements.append(statement);
//            }
////        methodStatements.reverse();
//        String[] returnExpression = n.f10.accept(this, st);
//        if (naked(returnExpression))
//            returnExpression = wrap(returnExpression, st);
//        methodStatements.append(returnExpression[1]).append('\n').append("return ").append(returnExpression[0]);
//        st.typeM(st.state.classID, st.state.methodID).vapor.setStatements(methodStatements.toString());
//        return new String[]{"", ""};
//    }
//
//    private static String removeEmptyLines(String text) {
//        final String[] strings = text.split("\n");
//        StringBuilder result = new StringBuilder();
//        for (int i = 0, stringsLength = strings.length; i < stringsLength; i++) {
//            String str = strings[i];
//            if (str.isEmpty()) continue;
//            result.append(str);
////            if (i + 1 == stringsLength) continue;
//            result.append("\n");
//        }
//        return result.toString();
//    }
//
//    /**
//     * f0 -> FormalParameter()
//     * f1 -> ( FormalParameterRest() )*
//     */
//    public String[] visit(FormalParameterList n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> Type()
//     * f1 -> Identifier()
//     */
//    public String[] visit(FormalParameter n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> ","
//     * f1 -> FormalParameter()
//     */
//    public String[] visit(FormalParameterRest n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> ArrayType()
//     * | BooleanType()
//     * | IntegerType()
//     * | Identifier()
//     */
//    public String[] visit(Type n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> "int"
//     * f1 -> "["
//     * f2 -> "]"
//     */
//    public String[] visit(ArrayType n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> "boolean"
//     */
//    public String[] visit(BooleanType n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> "int"
//     */
//    public String[] visit(IntegerType n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> Block()
//     * | AssignmentStatement()
//     * | ArrayAssignmentStatement()
//     * | IfStatement()
//     * | WhileStatement()
//     * | PrintStatement()
//     */
//    public String[] visit(Statement n, SymbolTable st) {
//        return n.f0.accept(this, st);
//    }
//
//    /**
//     * f0 -> "{"
//     * f1 -> ( Statement() )*
//     * f2 -> "}"
//     */
//    public String[] visit(Block n, SymbolTable st) {
//        if (n.f1.present()) {
//            //        if (n.present()) {
////            String[] _ret = null;
////            int _count = 0;
////            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
////                String[] s = e.nextElement().accept(this, st);
////                _count++;
////            }
////            return _ret;
////        } else
////            return null;
//
//
//            String[] statements = n.f1.accept(this, st);
//            int count = 0;
//            String block = "";
//            for (Enumeration<Node> e = n.f1.elements(); e.hasMoreElements(); ) {
//                String[] s = e.nextElement().accept(this, st);
//                block += s[1] + "\n" + s[0];
//                count++;
//            }
//            return new String[]{block, ""};
//        } else return new String[]{"", ""};
////        if (block.length > 1)
////            return new String[]{block[1] + "\n" + block[0], ""};
////        return new String[]{block[0], ""};
//    }
//
//    /**
//     * f0 -> Identifier()
//     * f1 -> "="
//     * f2 -> Expression()
//     * f3 -> ";"
//     */
//    public String[] visit(AssignmentStatement n, SymbolTable st) {
//        String[] _ret = null;
//        String[] identifier = n.f0.accept(this, st);
//        String[] expression = n.f2.accept(this, st);
////        if (naked(expression))
////            expression = wrap(expression, st);
//        String assignment = identifier[0] + " = " + expression[0] + "\n";
//        String[] ret = new String[]{assignment, identifier[1] + "\n" + expression[1]};
//        String statement = ret[1] + "\n" + ret[0];
//        return ret;
//    }
//
//    /**
//     * f0 -> Identifier()
//     * f1 -> "["
//     * f2 -> Expression()
//     * f3 -> "]"
//     * f4 -> "="
//     * f5 -> Expression()
//     * f6 -> ";"
//     */
//    public String[] visit(ArrayAssignmentStatement n, SymbolTable st) {
//        String[] _ret = null;
//        String[] array = n.f0.accept(this, st);
//        String[] index = n.f2.accept(this, st);
//        if (naked(index))
//            index = wrap(index, st);
//        String temporary1 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String temporary2 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String lookup = temporary1 + " = " + array[0] + "\n" +
////                temporary2 + " = [" + index[0] + "]\n" +
//                //error checking, index checking, null checking, print to error
//                temporary2 + " = MulS(" + index[0] + " 4)\n" +
//                temporary2 + " = Add(" + temporary1 + " " + temporary2 + ")\n";
//
//        String elementSetup = array[1] + "\n" + index[1] + "\n" + lookup;
//        String element = "[" + temporary2 + "+4]";
//
//        String[] value = n.f5.accept(this, st);
//        if (naked(value))
//            value = wrap(value, st);
//
//        String assignment = elementSetup + "\n" + value[1] + "\n" + element + " = " + value[0] + "\n";
//        return new String[]{assignment, ""};
//    }
//
//    /**
//     * f0 -> "if"
//     * f1 -> "("
//     * f2 -> Expression()
//     * f3 -> ")"
//     * f4 -> Statement()
//     * f5 -> "else"
//     * f6 -> Statement()
//     */
//    public String[] visit(IfStatement n, SymbolTable st) {
//        String[] _ret = null;
//        String[] condition = n.f2.accept(this, st);
//        if (naked(condition)) {
//            condition = wrap(condition, st);
//        }
//        int ifCount = st.getIfCounter();
//        String[] ifBody = n.f4.accept(this, st);
//        String ifBodyStatements = ifBody[1] + '\n' + ifBody[0];
//        String[] elseBody = n.f6.accept(this, st);
////        StringBuilder elseBodyStatements = new StringBuilder();
////        Scanner scanner = new Scanner(elseBody[1]);
////        while (scanner.hasNextLine()) {
////            String line = scanner.nextLine();
////            elseBodyStatements.append('\t').append(line);
////            // process the line
////        }
////        elseBodyStatements.append('\t').append(elseBody[0]);
////        StringBuilder ifBodyStatements = new StringBuilder();
////        Scanner scanner2 = new Scanner(ifBody[1]);
////        while (scanner2.hasNextLine()) {
////            String line = scanner2.nextLine();
////            ifBodyStatements.append('\t').append(line);
////            // process the line
////        }
////        ifBodyStatements.append('\t').append(ifBody[0]);
////        scanner.close();
//        String elseBodyStatements = elseBody[1] + '\n' + elseBody[0];
//        String ifStatement = condition[1] + "\nif0 " + condition[0] + " goto :if" + ifCount + "_else" +
//                ifBodyStatements + "\ngoto :if" + ifCount + "_end\n" +
//                "if" + ifCount + "_else:\n" +
//                elseBodyStatements + "\nif" + ifCount + "_end:\n";
//        return new String[]{ifStatement, ""};
//    }
//
//    /**
//     * f0 -> "while"
//     * f1 -> "("
//     * f2 -> Expression()
//     * f3 -> ")"
//     * f4 -> Statement()
//     */
//    public String[] visit(WhileStatement n, SymbolTable st) {
//        String[] _ret = null;
//
//        String[] expression = n.f2.accept(this, st);
//        if (naked(expression))
//            expression = wrap(expression, st);
//        int whileCount = st.getWhileCounter();
//        String[] body = n.f4.accept(this, st);
//        String loop = "while" + whileCount + "_top:\n" +
//                expression[1] + "\n" +
//                "if0 " + expression[0] + " goto :while" + whileCount + "_end\n" +
//                body[1] + "\n" + body[0] + "\n" +
//                "goto :while" + whileCount + "_top\n" +
//                "while" + whileCount + "_end:\n";
//        return new String[]{loop, ""};
//    }
//
//    /**
//     * f0 -> "System.out.println"
//     * f1 -> "("
//     * f2 -> Expression()
//     * f3 -> ")"
//     * f4 -> ";"
//     */
//    public String[] visit(PrintStatement n, SymbolTable st) {
//        String[] expression = n.f2.accept(this, st);
//        if (naked(expression))
//            expression = wrap(expression, st);
//        String print = "PrintIntS(" + expression[0] + ")\n";
//        return new String[]{print, expression[1]};
//    }
//
//    /**
//     * f0 -> AndExpression()
//     * | CompareExpression()
//     * | PlusExpression()
//     * | MinusExpression()
//     * | TimesExpression()
//     * | ArrayLookup()
//     * | ArrayLength()
//     * | MessageSend()
//     * | PrimaryExpression()
//     */
//    public String[] visit(Expression n, SymbolTable st) {
//        return n.f0.accept(this, st);
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "&&"
//     * f2 -> PrimaryExpression()
//     */
//    public String[] visit(AndExpression n, SymbolTable st) {
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        int andCount = st.andCounter;
//        String[] primaryExpression1 = n.f0.accept(this, st);
//        if (naked(primaryExpression1))
//            primaryExpression1 = wrap(primaryExpression1, st);
//        String[] primaryExpression2 = n.f2.accept(this, st);
//        if (naked(primaryExpression2))
//            primaryExpression2 = wrap(primaryExpression2, st);
//
//        String and = temporary + " = 0\n" +
//                "if0 " + primaryExpression1[0] + " goto :and" + andCount + "_end\n" +
//                "\tif0 " + primaryExpression2[0] + " goto :and" + andCount + "_end\n" +
//                "\t\t" + temporary + " = 1\n" +
//                "and" + andCount + "_end\n";
//        return new String[]{temporary, primaryExpression1[1] + "\n" + primaryExpression2[1] + "\n" + and};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "<"
//     * f2 -> PrimaryExpression()
//     */
//    public String[] visit(CompareExpression n, SymbolTable st) {
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String[] primaryExpression1 = n.f0.accept(this, st);
//        if (naked(primaryExpression1))
//            primaryExpression1 = wrap(primaryExpression1, st);
//        String[] primaryExpression2 = n.f2.accept(this, st);
//        if (naked(primaryExpression2))
//            primaryExpression2 = wrap(primaryExpression2, st);
//        String compare = "LtS(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
//        return new String[]{compare, primaryExpression1[1] + "\n" + primaryExpression2[1] + "\n"};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "+"
//     * f2 -> PrimaryExpression()
//     */
//    public String[] visit(PlusExpression n, SymbolTable st) {
//        String[] primaryExpression1 = n.f0.accept(this, st);
//        if (naked(primaryExpression1))
//            primaryExpression1 = wrap(primaryExpression1, st);
//        String[] primaryExpression2 = n.f2.accept(this, st);
//        if (naked(primaryExpression2))
//            primaryExpression2 = wrap(primaryExpression2, st);
//        String plus = "Add(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
//        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "-"
//     * f2 -> PrimaryExpression()
//     */
//    public String[] visit(MinusExpression n, SymbolTable st) {
//        String[] primaryExpression1 = n.f0.accept(this, st);
//        if (naked(primaryExpression1))
//            primaryExpression1 = wrap(primaryExpression1, st);
//        String[] primaryExpression2 = n.f2.accept(this, st);
//        if (naked(primaryExpression2))
//            primaryExpression2 = wrap(primaryExpression2, st);
//        String plus = "Sub(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
//        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "*"
//     * f2 -> PrimaryExpression()
//     */
//    public String[] visit(TimesExpression n, SymbolTable st) {
//        String[] primaryExpression1 = n.f0.accept(this, st);
//        if (naked(primaryExpression1))
//            primaryExpression1 = wrap(primaryExpression1, st);
//        String[] primaryExpression2 = n.f2.accept(this, st);
//        if (naked(primaryExpression2))
//            primaryExpression2 = wrap(primaryExpression2, st);
//        String plus = "MulS(" + primaryExpression1[0] + " " + primaryExpression2[0] + ")\n";
//        return new String[]{plus, primaryExpression1[1] + "\n" + primaryExpression2[1]};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "["
//     * f2 -> PrimaryExpression()
//     * f3 -> "]"
//     */
//    public String[] visit(ArrayLookup n, SymbolTable st) {
//        String[] _ret = null;
//        String[] array = n.f0.accept(this, st);
//        String[] index = n.f2.accept(this, st);
//        if (naked(index))
//            index = wrap(index, st);
//        String temporary1 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String temporary2 = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String lookup = temporary1 + " = " + array[0] + "\n" +
////                temporary2 + " = [" + index[0] + "]\n" +
//                //error checking, index checking, null checking, print to error
//                temporary2 + " = MulS(" + index[0] + " 4)\n" +
//                temporary2 + " = Add(" + temporary1 + " " + temporary2 + ")\n";
//
//        return new String[]{"[" + temporary2 + "+4]", array[1] + "\n" + index[1] + "\n" + lookup};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "."
//     * f2 -> "length"
//     */
//    public String[] visit(ArrayLength n, SymbolTable st) {
//        String[] array = n.f0.accept(this, st);
////        if (naked(array)) {
////            array = wrap(array, st);
////
////      }
//        String length = array[0];
////        if (!naked(array))
////            length = "[" + length + "]";
//        return new String[]{"[" + length + "]", array[1] + "\n"};
//    }
//
//    /**
//     * f0 -> PrimaryExpression()
//     * f1 -> "."
//     * f2 -> Identifier()
//     * f3 -> "("
//     * f4 -> ( ExpressionList() )?
//     * f5 -> ")"
//     */
//    public String[] visit(MessageSend n, SymbolTable st) {
//        String[] _ret = null;
//        String[] primaryExpression = n.f0.accept(this, st);
//        if (naked(primaryExpression)) {
//            primaryExpression = wrap(primaryExpression, st);
//        }
//        ClassType type = null;
//        switch (n.f0.f0.which) {
//            case 3: {
//                String id = ((Identifier) n.f0.f0.choice).f0.tokenImage;
//                boolean local = st.isPV(st.state.classID, st.state.methodID, id);
//                if (!local) {
//                    type = (ClassType) st.typeF(st.state.classID, id);
//                } else {
//                    type = (ClassType) st.typePV(st.state.classID, st.state.methodID, id);
//                }
//            }
//            //identifier
//            break;
//            case 4: {
//                type = st.typeC(st.state.classID);
////                id = "this";
//            }
//            break;
//            case 6: {
//                String classID = ((AllocationExpression) n.f0.f0.choice).f1.f0.tokenImage;
////                String allocation = n.f0.f0.choice.accept(this, st);
////                ret += allocation;
////                id = parseLastLineVariable(allocation);
//                type = st.typeC(classID);
//                //allocation
//            }
//            break;
//            case 8: {
//                BracketExpression be = (BracketExpression) n.f0.f0.choice;
//                Expression exp = be.f1;
//                PrimaryExpression pe = (PrimaryExpression) exp.f0.choice; //it is type checked, so don't even bother checking if 'which' is 8, it must be a primary expression ('which' == 8, which is confusing because the primaryexpresssion.nodechoice.which is also 8 for bracket expressions, they probably did this on purpose fucking dickheads
//                while (pe.f0.which == 8) {
//                    be = (BracketExpression) pe.f0.choice;
//                    exp = be.f1;
//                    pe = (PrimaryExpression) exp.f0.choice; //again, same as above, it has type checked, so its not going to be addition or anything other than an object nested in brackets which you are un-nesting here
//                }
//
//
//                int i = pe.f0.which;
//                switch (i) {
//                    case 3: {
//                        String id = ((Identifier) pe.f0.choice).f0.tokenImage;
//                        boolean local = st.isPV(st.state.classID, st.state.methodID, id);
//                        if (!local) {
//                            type = (ClassType) st.typeF(st.state.classID, id);
//                        } else {
//                            type = (ClassType) st.typePV(st.state.classID, st.state.methodID, id);
//                        }
//                    }
//                    case 4: {
//                        type = st.typeC(st.state.classID);
//                        break;
//                    }
//                    case 6: {
//                        String classID = ((AllocationExpression) pe.f0.choice).f1.f0.tokenImage;
//                        type = st.typeC(classID);
//                        break;
//                    }
//
//                }
//
//                System.out.println();
//
////                Expression exp = ((BracketExpression) n.f0.f0.choice).f1;
////                PrimaryExpression pe;
////                while (exp.f0.which == 8) {
////                     pe = (PrimaryExpression) exp.f0.choice;
////                    if (pe.f0.which != 8)
////                        break;
////                    exp = ((BracketExpression) pe.f0.choice).f1;
////                }
////
////                System.out.println();
////                NodeChoice childExpression = ((BracketExpression) n.f0.f0.choice).f1.f0;
////                int i = childExpression.which;
//
//
////                while (((PrimaryExpression) childExpression.choice).f0.which == 8)
////                    childExpression = ((BracketExpression) childExpression.choice).f1.f0;
//
//
////                switch (childExpression.which) {
////                    case 3:
////                        String id1 = ((Identifier) childExpression.choice).f0.tokenImage;
////                        boolean local1 = st.isPV(st.state.classID, st.state.methodID, id1);
////                        if (!local1) {
////                            type = (ClassType) st.typeF(st.state.classID, id1);
////                        } else {
////                            type = (ClassType) st.typePV(st.state.classID, st.state.methodID, id1);
////                        }
////                        break;
////                    case 4:
////                        type = st.typeC(st.state.classID);
////                        break;
////                    case 6:
////                        String classID1 = ((AllocationExpression) childExpression.choice).f1.f0.tokenImage;
////                        type = st.typeC(classID1);
////                        break;
////                }
//
//
////                String[] expression = ((BracketExpression) n.f0.f0.choice).f1.accept(this, st);
////                String retID = parseLastLineVariable(expression);
////                if (isLocal(retID)) {
////                    type = (ClassType) st.typePV(st.state.classID, st.state.methodID, retID);
////                    id = retID;
////                    break;
////                }
//                break;
//            }
//        }
//        String argsSetup = "";
//        String argsList = "";
//        if (n.f4.present()) {
//            ArrayList<Expression> args = new ArrayList<>();
//            ExpressionList n2 = (ExpressionList) n.f4.node;
//            args.add(n2.f0);
//            if (n2.f1.present()) {
//                for (Enumeration<Node> e = n2.f1.elements(); e.hasMoreElements(); )
//                    args.add(((ExpressionRest) e.nextElement()).f1);
//            }
//            StringBuilder argsSetupB = new StringBuilder();
//            StringBuilder argsListB = new StringBuilder();
//            for (Expression e : args) {
//                String[] stmts = e.accept(this, st);
//                if (naked(stmts))
//                    stmts = wrap(stmts, st);
//                argsSetupB.append(stmts[1]).append("\n");
//                argsListB.append(' ').append(stmts[0]);
//            }
//            argsSetup = argsSetupB.toString();
//            argsList = argsListB.toString();
//        }
//        int methodOffset = st.typeM(type.classID(), n.f2.f0.tokenImage).vapor.getOffset();
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String methodSetup = temporary + " = [" + primaryExpression[0] + "]\n" +
//                temporary + " = [" + temporary + "+" + methodOffset + "]\n";
//        // now get method offset
//        // set up argument list
//        // set up method call
//        // return [0]: call offset_temp( primaryExp[0], arglist) [1]: primaryExp[1]
////        if (naked)
//        String call = "call " + temporary + "(" + primaryExpression[0] + argsList + ")\n";
////        n.f2.accept(this, st);
////        n.f4.accept(this, st);
//        return new String[]{call, primaryExpression[1] + "\n" + methodSetup + "\n" + argsSetup};
//    }
//
//    /**
//     * f0 -> Expression()
//     * f1 -> ( ExpressionRest() )*
//     */
//    public String[] visit(ExpressionList n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> ","
//     * f1 -> Expression()
//     */
//    public String[] visit(ExpressionRest n, SymbolTable st) {
//        String[] _ret = null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        return _ret;
//    }
//
//    /**
//     * f0 -> IntegerLiteral()
//     * | TrueLiteral()
//     * | FalseLiteral()
//     * | Identifier()
//     * | ThisExpression()
//     * | ArrayAllocationExpression()
//     * | AllocationExpression()
//     * | NotExpression()
//     * | BracketExpression()
//     */
//    public String[] visit(PrimaryExpression n, SymbolTable st) {
//        return n.f0.accept(this, st);
//    }
//
//    /**
//     * f0 -> <INTEGER_LITERAL>
//     */
//    public String[] visit(IntegerLiteral n, SymbolTable st) {
//        return new String[]{n.f0.tokenImage, ""};
//    }
//
//    /**
//     * f0 -> "true"
//     */
//    public String[] visit(TrueLiteral n, SymbolTable st) {
//        return new String[]{"1", ""};
//    }
//
//    /**
//     * f0 -> "false"
//     */
//    public String[] visit(FalseLiteral n, SymbolTable st) {
//        return new String[]{"0", ""};
//    }
//
//    /**
//     * f0 -> <IDENTIFIER>
//     */
//    public String[] visit(Identifier n, SymbolTable st) {
//        boolean b = st.isPV(st.state.classID, st.state.methodID, n.f0.tokenImage);
//        if (b)
//            return new String[]{n.f0.tokenImage, ""};
//
//        return new String[]{"[this+" + st.typeC(st.state.classID).vapor.fieldOffset(n.f0.tokenImage) * 4 + "]", ""};
//    }
//
//    /**
//     * f0 -> "this"
//     */
//    public String[] visit(ThisExpression n, SymbolTable st) {
//        return new String[]{n.f0.tokenImage, ""};
//    }
//
//    /**
//     * f0 -> "new"
//     * f1 -> "int"
//     * f2 -> "["
//     * f3 -> Expression()
//     * f4 -> "]"
//     */
//    public String[] visit(ArrayAllocationExpression n, SymbolTable st) {
//        String[] _ret = null;
//        String[] size = n.f3.accept(this, st);
//        if (naked(size)) {
//            size = wrap(size, st);
//        }
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String allocation = temporary + " = :AllocArray(" + size[0] + ")\n";
//        return new String[]{temporary, size[1] + "\n" + allocation};
//    }
//
//    /**
//     * f0 -> "new"
//     * f1 -> Identifier()
//     * f2 -> "("
//     * f3 -> ")"
//     */
//    public String[] visit(AllocationExpression n, SymbolTable st) {
//        String[] _ret = null;
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String allocation = temporary + " = HeapAllocZ(" +
//                st.typeC(n.f1.f0.tokenImage).vapor.allocSize() + ")\n" +
//                "[" + temporary + "] = :vmt_" + n.f1.f0.tokenImage + "\n";
//        //n.f1.accept(this, st);
//        return new String[]{temporary, allocation};
//    }
//
//    /**
//     * f0 -> "!"
//     * f1 -> Expression()
//     */
//    public String[] visit(NotExpression n, SymbolTable st) {
//        String[] _ret = null;
//        //check if expression is a literal or a local variable (parameter/variable)
//        //check if expression is a field
//        //check if expression is a
//
//        String[] expression = n.f1.accept(this, st);
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        int ifCount = st.getIfCounter();
//        if (naked(expression)) {
//            expression = wrap(expression, st);
//        }
//        String not = temporary + " = 1\n" +
//                "if0 " + expression[0] + " goto if" + ifCount + "_end\n" +
//                "\t" + temporary + " = 0\n" +
//                "if" + ifCount + "_end\n";
//        String setup = expression[1] + not;
//        return new String[]{temporary, setup};
//    }
//
//    /**
//     * f0 -> "("
//     * f1 -> Expression()
//     * f2 -> ")"
//     */
//    public String[] visit(BracketExpression n, SymbolTable st) {
//        return n.f1.accept(this, st);
//    }
//
////    private static String[] wrap(String expression) {
//////            n
////        return null;
////    }
//
//    private static String parseLastLineVariable(String expression) {
//        String[] lines = expression.split("\n");
//        for (int i = lines.length - 1; i >= 0; i--) {
//            if (!lines[i].isBlank()) {
//                return expression.split(" ", 2)[0];
//            }
//        }
//        return null;
//    }
//
////    private static boolean isLocal(String id, SymbolTable st) {
////        if (st.isPV(st.state.classID, st.state.methodID, id))
////        String[] lines = expression.split(" ", 2);
////        return lines[1].isBlank();
////
////    }
//
//    private static String[] wrap(String[] expression, SymbolTable st) {
//        String temporary = st.typeM(st.state.classID, st.state.methodID).vapor.getTemp();
//        String bind = temporary + " = " + expression[0] + "\n";
//        return new String[]{temporary, expression[1] + "\n" + bind};
//    }
//
//    private static boolean naked(String[] expression) {
//        if (expression[0].charAt(0) == '[' && expression[0].charAt(expression[0].length() - 1) == ']')
//            return true;
//        if (expression[0].contains("call "))
//            return true;
//        if (expression[0].contains("LtS(") || expression[0].contains("MulS(") || expression[0].contains("Sub(") || expression[0].contains("Add("))
//            return true;
//        //array lookup, times expression, and expression, message send, array legnth (don't wrap when returning because in the case of assignment statements, you'll need to not have it wrapped so as to not introduce unneeded temporary)
//        return false;
//    }
//}
