import cs132.minijava.MiniJavaParser;
import cs132.minijava.ParseException;
import cs132.minijava.syntaxtree.Node;

import java.io.InputStream;

public class Typecheck {
    public static void main(String [] args) throws ParseException {
        try {
            String j = "ok";
            InputStream in = System.in;
            //This builds the AST
            Node root = new MiniJavaParser(in).Goal();
            MyWrapper wrapper = new MyWrapper();
            ClassVisitor firstPass = new ClassVisitor();
            root.accept(firstPass,wrapper);
            //debug
            //wrapper.print();
            //Check if it is acyclic
            wrapper.myClass.isAcyclic();
            //check if functions overload
            wrapper.myClass.overLoads();

            MyVisitor visitor = new MyVisitor();
            root.accept(visitor, wrapper);
        }
        catch(RuntimeException e)
        {
            System.out.println("Type error");
            System.exit(0);
            return;
        }
        System.out.println("Program type checked successfully");
    }
}
