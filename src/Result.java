
public class Result {

	double infinity = Double.MAX_VALUE;		// Infinity
	public Node n;							// Node
	public double f_limit;					// f_limit
	
	public Result(int N) {
		this.n = new Node(N);
		this.f_limit = infinity;
	}
	
	public Result(Node n, double f_limit) {
		this.n = n;
		this.f_limit = f_limit;
	}
}
