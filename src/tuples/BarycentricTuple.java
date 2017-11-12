package tuples;

public class BarycentricTuple {
	public final double alpha, beta, gamma;

	public BarycentricTuple(double alpha, double beta, double gamma) {
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
	}
	
	public boolean isVertex()
	{
		return 	alpha == 1 ||
				beta == 1 ||
				gamma == 1;
	}
	
	public boolean isOnEdge()
	{
		return 	alpha == 0 ||
				beta == 0 ||
				gamma == 0;
	}
	
	public double getAlpha()
	{
		return alpha;
	}
	
	public double getBeta()
	{
		return beta;
	}
	
	public double getGamma()
	{
		return gamma;
	}
	
	public double getWeightedAverage(float v1, float v2, float v3)
	{
		return (alpha*v1 + beta*v2 + gamma*v3);
	}

	@Override
	public Object clone() {
		return new BarycentricTuple(alpha, beta, gamma);
	}
}
