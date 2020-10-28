import cs132.minijava.syntaxtree.AllocationExpression;
import cs132.minijava.syntaxtree.AndExpression;
import cs132.minijava.syntaxtree.ArrayAllocationExpression;
import cs132.minijava.syntaxtree.ArrayAssignmentStatement;
import cs132.minijava.syntaxtree.ArrayLength;
import cs132.minijava.syntaxtree.ArrayLookup;
import cs132.minijava.syntaxtree.ArrayType;
import cs132.minijava.syntaxtree.AssignmentStatement;
import cs132.minijava.syntaxtree.Block;
import cs132.minijava.syntaxtree.BooleanType;
import cs132.minijava.syntaxtree.BracketExpression;
import cs132.minijava.syntaxtree.ClassDeclaration;
import cs132.minijava.syntaxtree.ClassExtendsDeclaration;
import cs132.minijava.syntaxtree.CompareExpression;
import cs132.minijava.syntaxtree.Expression;
import cs132.minijava.syntaxtree.ExpressionList;
import cs132.minijava.syntaxtree.ExpressionRest;
import cs132.minijava.syntaxtree.FalseLiteral;
import cs132.minijava.syntaxtree.FormalParameter;
import cs132.minijava.syntaxtree.FormalParameterList;
import cs132.minijava.syntaxtree.FormalParameterRest;
import cs132.minijava.syntaxtree.Goal;
import cs132.minijava.syntaxtree.Identifier;
import cs132.minijava.syntaxtree.IfStatement;
import cs132.minijava.syntaxtree.IntegerLiteral;
import cs132.minijava.syntaxtree.IntegerType;
import cs132.minijava.syntaxtree.MainClass;
import cs132.minijava.syntaxtree.MessageSend;
import cs132.minijava.syntaxtree.MethodDeclaration;
import cs132.minijava.syntaxtree.MinusExpression;
import cs132.minijava.syntaxtree.Node;
import cs132.minijava.syntaxtree.NodeList;
import cs132.minijava.syntaxtree.NodeListOptional;
import cs132.minijava.syntaxtree.NodeOptional;
import cs132.minijava.syntaxtree.NodeSequence;
import cs132.minijava.syntaxtree.NodeToken;
import cs132.minijava.syntaxtree.NotExpression;
import cs132.minijava.syntaxtree.PlusExpression;
import cs132.minijava.syntaxtree.PrimaryExpression;
import cs132.minijava.syntaxtree.PrintStatement;
import cs132.minijava.syntaxtree.Statement;
import cs132.minijava.syntaxtree.ThisExpression;
import cs132.minijava.syntaxtree.TimesExpression;
import cs132.minijava.syntaxtree.TrueLiteral;
import cs132.minijava.syntaxtree.Type;
import cs132.minijava.syntaxtree.TypeDeclaration;
import cs132.minijava.syntaxtree.VarDeclaration;
import cs132.minijava.syntaxtree.WhileStatement;
import cs132.minijava.visitor.GJDepthFirst;
import cs132.minijava.visitor.DepthFirstVisitor;

import java.lang.reflect.TypeVariable;
import java.util.Enumeration;

public class MyVisitor extends GJDepthFirst<TypeValue,MyWrapper> {
    //set up easy exit Sequence
    public static void exitSequence(String debug)
    {
        //System.err.println(debug);
        System.out.println("Type error");
        System.exit(0);
    }

    //Type check for MainClass
    public TypeValue visit(MainClass n, MyWrapper argu) {
        argu.changeCurScope(n.f1.f0.toString(),"","");
        argu.resetNonCurScope();
        //We've done all the type checking before this, so just check the statement
        n.f15.accept(this, argu);
        return null;
    }

    //Specify which class you're in do nothing else
    public TypeValue visit(ClassDeclaration n, MyWrapper argu) {
        argu.changeCurScope(n.f1.f0.toString(),"","");
        argu.resetNonCurScope();
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return null;
    }

    //Type check for class extensions
    public TypeValue visit(ClassExtendsDeclaration n, MyWrapper argu) {
        argu.changeCurScope(n.f1.f0.toString(),"","");
        argu.resetNonCurScope();
        //just check that the extended class exists
        String parent = n.f3.f0.toString();
        if(!(argu.classExists(parent)))
            exitSequence("ClassExtendsDeclaration");
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return null;
    }

    //Type check for VarDeclaration
    public TypeValue visit(VarDeclaration n, MyWrapper argu) {
        //check if this type is valid in the argu
        n.f0.accept(this, argu);
        //store this identifier into the argu list, shouldn't visit this
        //n.f1.accept(this, argu);
        return null;
    }

    //No overloading here, might be able to code it separately
    //Set the method name and type so when we add id's in like allocation statements we can
    public TypeValue visit(MethodDeclaration n, MyWrapper argu) {
        argu.changeCurScope(argu.className, n.f2.f0.toString(), argu.getMethodType(argu.className, n.f2.f0.toString()));
        //Only need to type check statements
        n.f8.accept(this, argu);
        //and expressions
        if(!argu.methodType.equals(n.f10.accept(this, argu).type)) {
            exitSequence("Method Declaration");
        }
        return null;
    }

    //Type check for Formal Parameter List
    public TypeValue visit(FormalParameterList n, MyWrapper argu) {
        //check that the format parameter type checks
        //n.f0.accept(this, argu);
        //check that the rest of the formal parameter returns
        //n.f1.accept(this, argu);
        return null;
    }

    //Type check for formal parameter
    public TypeValue visit(FormalParameter n, MyWrapper argu) {
        //Check that n.f0.accept.type actually exists with the arguments list
        //n.f0.accept(this, argu);
        //add this to the argument list, but check if it existed before in the same scope
        //n.f1.accept(this, argu);
        return null;
    }

    //Type check for rest of the formal parameter,
    public TypeValue visit(FormalParameterRest n, MyWrapper argu) {
        //Type check the formal parameter
        //n.f1.accept(this, argu);
        return null;
    }

    //Type check for statements
    public TypeValue visit(Statement n, MyWrapper argu) {
        //Make sure whatever statement you have type checks
        n.f0.accept(this, argu);
        return null;
    }

    //Type check for blocks
    public TypeValue visit(Block n, MyWrapper argu) {
        //As long as the list of statements type checks we're good
        n.f1.accept(this, argu);
        return null;
    }

    //Type check for assignment statement
    public TypeValue visit(AssignmentStatement n, MyWrapper argu) {
        //If there types are different, exit. You check this by checking the left hand side if it exists in argu
        //check that the left hand size exists in the argument
        String id = n.f0.f0.toString();
        if(!(argu.idExistsCurScope(id))) {
            exitSequence("AssignmentStatement1");
        }
        //if the right hand side is a subtype of the left hand side
        //parents on the left hand side children on the right
        String idType = argu.idTypeCurScope(id);
        if(!argu.isSubType(idType,n.f2.accept(this,argu).type)) {
            exitSequence("AssignmentStatement2");
        }
        return null;
        //So this might have to change, we might have to get the type first, pass it in and then
        //tell our wrapper function to check that if its a subtype, it keeps going til it finds one.
    }

    //Type check for array assignment statement
    public TypeValue visit(ArrayAssignmentStatement n, MyWrapper argu) {
        // check that this is valid in argu since it needs to exist as int[]
        String id = n.f0.f0.toString();
        if(!argu.idExistsCurScope(id))
        {
            exitSequence("ArrayAssignmentStatement1");
        }
        if(argu.idTypeCurScope(id) != "int[]") {
            exitSequence("ArrayAssignmentStatement2");
        }

        //Index must be of type int
        if(n.f2.accept(this, argu).type != "int") {
            exitSequence("ArrayAssignmentStatement3");
        }
        //inserted item must be of type int as well
        if(n.f5.accept(this, argu).type != "int")
        {
            exitSequence("ArrayAssignmentStatement4");
        }
        return null;
    }

    //Return for if statements
    public TypeValue visit(IfStatement n, MyWrapper argu) {
        //The expression must be boolean
        if(n.f2.accept(this, argu).type != "boolean")
            exitSequence("IfStatement");
        //Go through the if statement to make sure it type checks
        n.f4.accept(this, argu);

        //Go through the else statement to make sure it type checks
        n.f6.accept(this, argu);
        return null;
    }

    //Return for while statement
    public TypeValue visit(WhileStatement n, MyWrapper argu) {
        //Expression must be a boolean
        if(n.f2.accept(this, argu).type != "boolean") {
            exitSequence("WhileStatement");
        }
        //Type check statement to make sure it doesn't fail
        n.f4.accept(this, argu);
        return null;
    }

    //Return for Print statement
    public TypeValue visit(PrintStatement n, MyWrapper argu) {
        if(n.f2.accept(this, argu).type != "int") {
            exitSequence("PrintStatement");
        }
        return null;
    }

    //Return for expression statement
    public TypeValue visit(Expression n, MyWrapper argu) {
        TypeValue _ret = n.f0.accept(this, argu);
        return _ret;
    }

    // Return for &&
    public TypeValue visit(AndExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("boolean");
        if(n.f0.accept(this, argu).type != "boolean")
            exitSequence("AndExpression1");
        if(n.f2.accept(this, argu).type != "boolean")
            exitSequence("AndExpression2");
        return _ret;
    }
    //Return for < operator, return boolean
    public TypeValue visit(CompareExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("boolean");
        //both must be ints or else return false
        if(n.f0.accept(this, argu).type != "int")
            exitSequence("CompareExpression1");
        if(n.f2.accept(this, argu).type != "int")
            exitSequence("CompareExpression2");
        return _ret;
    }

    //Return int for plus expression
    public TypeValue visit(PlusExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        //if the first expression is not an int, return type error
        if(n.f0.accept(this,argu).type != "int")
            exitSequence("PlusExpression1");
        //if the second expression is not an int return type error
        if(n.f2.accept(this,argu).type != "int")
            exitSequence("PlusExpression2");
        return _ret;
    }

    //Return int for -
    public TypeValue visit(MinusExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        //if the first expression is not an int, return type error
        if(n.f0.accept(this,argu).type != "int") {
            exitSequence("MinusExpression1");
        }
        //if the second expression is not an int return type error
        if(n.f2.accept(this,argu).type != "int")
        {
            exitSequence("MinusExpression2");
        }
        return _ret;
    }

    //Return int for *
    public TypeValue visit(TimesExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        //if the first expression is not an int, return type error
        if(n.f0.accept(this,argu).type != "int") {
            exitSequence("TimesExpression1");
        }
        //if the second expression is not an int return type error
        if(n.f2.accept(this,argu).type != "int") {
            exitSequence("TimesExpression2");
        }
        return _ret;
    }

    //Return for ArrayLookup
    public TypeValue visit(ArrayLookup n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        //must be of type array
        if(n.f0.accept(this, argu).type != "int[]") {
            exitSequence("ArrayLookup1");
        }
        //must be of type int
        if(n.f2.accept(this, argu).type != "int") {
            exitSequence("ArrayLookup2");
        }
        return _ret;
    }

    //Return for arraylength
    public TypeValue visit(ArrayLength n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        if(n.f0.accept(this, argu).type != "int[]")
            exitSequence("ArrayLength");
        return _ret;
    }

    //For method calls
    public TypeValue visit(MessageSend n, MyWrapper argu) {
        //MessageSend 	::= 	PrimaryExpression "." Identifier "(" ( ExpressionList )? ")"
        //Identifier has to be an actual method in the Class type of primary expression. The
        //Expression list has to be match the formal parameters. Each type has to be a subtype of the formal
        //parameters
        //check that the method actually exists in the class and then check that the parameters are correct,subtyping and check
        //that the return types match
        String className = n.f0.accept(this, argu).type; //This gets the classtype of primary expression
        String methodName = n.f2.f0.toString(); //This gets the method name

        //if method doesn't exist in current or super
        boolean inCurScope = argu.methodExists(className, methodName);
        boolean inSuperScope = argu.methodExistsSuper(className, methodName);
        if(!inCurScope && !inSuperScope) //Find the method within the class of the primaryexpression
            exitSequence("MessageSend1");

        if(inSuperScope)
        {
            className = argu.getMethodClass(className,methodName);
        }
        //get back the return val
        TypeValue returnVal = new TypeValue(argu.getMethodType(className, methodName)); //get the return value of method
        String methType = returnVal.type;
        //Must reset old types
        String oldClassName = argu.nonScopeClassName;
        String oldMethodName = argu.nonScopeMethodName;
        String oldMethType = argu.nonScopeMethodType;
        argu.changeNonCurScope(className, methodName, methType);
        argu.setIndex(0);
        //get back parameters, check each argument is the subtype of each argument;
        //check if length's are equal though if it provides no arguments

        //Check that the expressions match correctly, and everything belongs to the right class
        int paramLength = argu.getParamLength(className, methodName, methType);
        if(n.f4.accept(this, argu)!=null) {
            if (paramLength != 0) {
                exitSequence("MessageSend2");
            }
        }
        else if(paramLength - 1 != argu.getIndex()) {
            exitSequence("MessageSend3");
        }
        argu.changeNonCurScope(oldClassName,oldMethodName,oldMethType);
        return returnVal;
    }

    //
    public TypeValue visit(NodeOptional n, MyWrapper argu) {
        return n.present() ? n.node.accept(this, argu) : new TypeValue("empty");
    }

    //Gotta figure out how to check params against each other
    public TypeValue visit(ExpressionList n, MyWrapper argu) {
        TypeValue expr = n.f0.accept(this, argu);
        if(!argu.paramValid(expr.type)) {
            exitSequence("ExpressionList");
        }
        n.f1.accept(this, argu);
        return null;
    }

    //check the formal params
    public TypeValue visit(ExpressionRest n, MyWrapper argu) {
        argu.iterateIndex();
        TypeValue _ret = null;
        TypeValue expr = n.f1.accept(this, argu);
        if(!argu.paramValid(expr.type)) {
            exitSequence("ExpressionRest");
        }
        return null;
    }
    //Return for primary expression, just return whatever type the expression is
    public TypeValue visit(PrimaryExpression n, MyWrapper argu) {
        return n.f0.accept(this, argu);
    }

    //Return for ints
    public TypeValue visit(IntegerLiteral n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int");
        n.f0.accept(this, argu);
        return _ret;
    }

    //Return for true
    public TypeValue visit(TrueLiteral n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("boolean");
        n.f0.accept(this, argu);
        return _ret;
    }

    //Return for false
    public TypeValue visit(FalseLiteral n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("boolean");
        n.f0.accept(this, argu);
        return _ret;
    }

    //Return for identifier, find it in class list and return it, never go to identifier if it is newly allocated
    public TypeValue visit(Identifier n, MyWrapper argu) {
        TypeValue _ret = null;
        //return its type from the arguments, if not found, return null
        if(argu.idExistsCurScope(n.f0.toString()))
            _ret = new TypeValue(argu.idTypeCurScope(n.f0.toString()));
        return _ret;
    }

    //Return the current class
    public TypeValue visit(ThisExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue(argu.className);
        return _ret;
    }

    //Return for array allocation expression is int[]
    public TypeValue visit(ArrayAllocationExpression n, MyWrapper argu) {
        TypeValue _ret = new TypeValue("int[]");
        if (n.f3.accept(this, argu).type != "int")
            exitSequence("ArrayAllocationExpression");
        return _ret;
    }

    //Return for allocation expression, must check if the variable/identifier is in the class list then return it
    public TypeValue visit(AllocationExpression n, MyWrapper argu) {
        //so you have to check if we have this type
        String id = n.f1.f0.toString();
        TypeValue _ret = new TypeValue(id);
        if(!(argu.classExists(id)))
            exitSequence("AllocationExpression");
        //return the type
        return _ret;
    }

    //Return boolean for ! expression
    public TypeValue visit(NotExpression n, MyWrapper argu) {
        TypeValue _ret = n.f1.accept(this, argu);
        if(_ret.type != "boolean")
            exitSequence("NotExpression");
        return _ret;
    }

    //Return expression for bracket expression
    public TypeValue visit(BracketExpression n, MyWrapper argu) {
        return n.f1.accept(this, argu);
    }
}
//Statement
//IfStatement
//WhileStatement
//PrintStatement
//Expression
//AndExpression
//CompareExpression
//PlusExpression
//MinusExpression
//TimesExpression
//ArrayLookup
//ArrayLength
//PrimaryExpression
//IntegerLiteral
//TrueLiteral
//FalseLiteral
//ArrayAllocationExpression
//NotExpression
//BracketExpression

/*sorta needs work on
MainClass a bunch of issues from wild cards to adding tot he argulist
VarDeclaration needs to check if Type os in argu and then add identifier into the list
Block might be a problem because it has a wildcard, check nodeListOptional for DFS
AllocationExpression, must check that the identifier is part of an actual class
FormalParameter check that type is available in the current context. This means we need to have a list of variable and classes
AssignmentStatement, must check the list if identifier is of valid type or exists
ArrayAssignment Statement, must check if identifier was also in list
 */

//main idea to implement is environments which is tied to classes, subtype

//Statement needs to be checked,
//Expression need to be checked
//Message.send needs to be checked
//Expression list, just check all expressions
//Primary expressions should be checked
//this