//
// Generated by JTB 1.3.2
//

package syntaxtree;
import visitor.Visitor;

/**
 * Grammar production:
 * f0 -> <INTEGER_LITERAL>
 */
public class IntegerLiteral implements Node {
   public NodeToken f0;

   public IntegerLiteral(NodeToken n0) {
      f0 = n0;
   }

   public void accept(Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}
