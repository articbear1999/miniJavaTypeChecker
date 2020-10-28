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

public class ClassVisitor extends GJDepthFirst<MyWrapper,MyWrapper> {
    //set up easy exit Sequence

    public MyWrapper visit(NodeList n, MyWrapper argu) {
        int _count = 0;

        for(Enumeration e = n.elements(); e.hasMoreElements(); ++_count) {
            argu = ((Node)e.nextElement()).accept(this, argu);
        }

        return argu;
    }

    public MyWrapper visit(NodeListOptional n, MyWrapper argu) {
        if (!n.present()) {
            return argu;
        } else {
            int _count = 0;

            for(Enumeration e = n.elements(); e.hasMoreElements(); ++_count) {

                argu = ((Node)e.nextElement()).accept(this, argu);
            }

            return argu;
        }
    }

    public MyWrapper visit(NodeOptional n, MyWrapper argu) {
        return n.present() ? n.node.accept(this, argu) : argu;
    }

    public MyWrapper visit(NodeSequence n, MyWrapper argu) {
        int _count = 0;

        for(Enumeration e = n.elements(); e.hasMoreElements(); ++_count) {
            argu = ((Node)e.nextElement()).accept(this, argu);
        }

        return argu;
    }

    public MyWrapper visit(NodeToken n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(Goal n, MyWrapper argu) {
        argu = n.f0.accept(this, argu);
        argu = n.f1.accept(this, argu);
        argu = n.f2.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(MainClass n, MyWrapper argu) {
        String className = n.f1.f0.toString();
        argu.addClass(className);
        argu.addExtendClass(className, "");
        String id = n.f11.f0.toString();
        argu.addId(id,"String[]");
        argu = n.f14.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(TypeDeclaration n, MyWrapper argu) {
        argu = n.f0.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(ClassDeclaration n, MyWrapper argu) {
        //reset methods
        argu.methodName = "";
        argu.methodType = "";
        String className = n.f1.f0.toString();
        argu.addClass(className);
        argu.addExtendClass(className, "");
        argu = n.f3.accept(this, argu);
        argu = n.f4.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(ClassExtendsDeclaration n, MyWrapper argu) {
        argu.methodName = "";
        argu.methodType = "";
        String className = n.f1.f0.toString();
        argu.addClass(className);
        String parentName = n.f3.f0.toString();
        argu.addExtendClass(className,parentName);
        argu = n.f3.accept(this, argu);
        argu = n.f5.accept(this, argu);
        argu = n.f6.accept(this, argu);
        return argu;
    }

    //add this to the closest thing, so method, or class
    public MyWrapper visit(VarDeclaration n, MyWrapper argu) {
        String idName = n.f1.f0.toString();
        //should change temp to some type of identifier
        argu = n.f0.accept(this, argu);
        //this will add the identifier and type to the class
        argu.addId(idName,argu.typeName);
        return argu;
    }

    public MyWrapper visit(MethodDeclaration n, MyWrapper argu) {
        //add this type to the method thing
        n.f1.accept(this,argu);
        //add name to the method thing
        String methodName = n.f2.f0.toString();
        argu.addMethod(methodName, argu.typeName);
        argu.methodType = argu.typeName;
        argu.methodName = methodName;
        //formal parameter list, add this to the class
        //variables, have to add to method not class so what do we do
        //just add them to both, this will run it twice, but this time methodName is defined
        //what will happen is it'll add it to both the list outside to check if its distinct,
        //then it'll add it to the inside so variables inside methods can't be duplicated
        //shouldn't be a problem considering it'll always looking at the closest scope first
        argu = n.f4.accept(this,argu);
        argu = n.f7.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(FormalParameterList n, MyWrapper argu) {
        argu = n.f0.accept(this, argu);
        argu = n.f1.accept(this, argu);
        return argu;
    }

    //add this to the id list corresponding to the class
    public MyWrapper visit(FormalParameter n, MyWrapper argu) {
        String className = argu.className;
        String idName = n.f1.f0.toString();
        //the bottom here should tell the identifier to get us the type by putting it into argu, it'll only do that
        //here cause if we need it as a variable, we just to string it
        argu = n.f0.accept(this, argu);
        argu.addFormal(idName,argu.typeName);
        return argu;
    }

    public MyWrapper visit(FormalParameterRest n, MyWrapper argu) {
        argu = n.f1.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(Type n, MyWrapper argu) {
        n.f0.accept(this, argu);
        return argu;
    }

    public MyWrapper visit(ArrayType n, MyWrapper argu) {
        argu.setType("int[]");
        return argu;
    }

    public MyWrapper visit(BooleanType n, MyWrapper argu) {
        argu.setType("boolean");
        return argu;
    }

    public MyWrapper visit(IntegerType n, MyWrapper argu) {
        argu.setType("int");
        return argu;
    }
    public MyWrapper visit(Statement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(Block n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(AssignmentStatement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ArrayAssignmentStatement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(IfStatement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(WhileStatement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(PrintStatement n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(Expression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(AndExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(CompareExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(PlusExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(MinusExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(TimesExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ArrayLookup n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ArrayLength n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(MessageSend n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ExpressionList n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ExpressionRest n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(PrimaryExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(IntegerLiteral n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(TrueLiteral n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(FalseLiteral n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(Identifier n, MyWrapper argu) {
        argu.setType(n.f0.toString());
        return argu;
    }

    public MyWrapper visit(ThisExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(ArrayAllocationExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(AllocationExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(NotExpression n, MyWrapper argu) {
        return argu;
    }

    public MyWrapper visit(BracketExpression n, MyWrapper argu) {
        return argu;
    }
}
