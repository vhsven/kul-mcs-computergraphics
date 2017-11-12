package matrices;

import java.util.Stack;

@SuppressWarnings("serial")
public class MatrixStack extends Stack<Matrix4f> implements Cloneable {	
	public Matrix4f getMatrix()
	{
		MatrixStack temp = this.clone();
		Matrix4f result = IdentityMatrix.getInstance();
		while(!temp.empty())
			result = temp.pop().multiplyRightWith(result); //popn * ... * pop2 * pop 1 * I
		
		return result;
	}
	
	public Matrix4f getInverseMatrix()
	{
		MatrixStack temp = this.clone();
		Matrix4f result = IdentityMatrix.getInstance();
		while(!temp.empty())
			result = result.multiplyRightWith(temp.pop().inv());
		
		return result;
	}
	
	/*
	 * Because the inv() function of many subclasses of Matrix4f is heavily optimized,
	 * it's often better to calculate those separately and multiply them in reverse order
	 * to get the complete inverse matrix.
	 *  
	 * 				[A]
	 * 				 |
	 * 				 |
	 * 				/ \
	 * 			   /   ...
	 * 			  [B]
	 * 
	 * Stack:
	 * 
	 * | [B] |
	 * | [A] |
	 * +-----+
	 * 
	 * getTotalMatrix = [A] * [B]
	 * getTotalInverseMatrix = inv([A]*[B]) = inv([B]) * inv([A]) 
	 */
	
	@Override
	public MatrixStack clone() {
		return (MatrixStack) super.clone(); //new stack with same matrice references
	}
}
