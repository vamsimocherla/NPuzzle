

public class Node {

	double infinity = Double.MAX_VALUE;			// Infinity
	public int N;								// Size of the Puzzle
	public int[][] blocks;				 		// Blocks on the Puzzle
	public String move;							// Move of the Child Node
	public double f = infinity;					// f-value of the Node
	
	// Make an empty Node
	public Node(int n) {
		this.N = n;
		this.blocks = new int[N][N];
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				blocks[i][j] = 0;
			}
		}
	}
	
	// Set Blocks of the Node
	public void setBlocks(int[][] arr) {
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				blocks[i][j] = arr[i][j];
			}
		}
	}
	
	// Compare Two Nodes
	public boolean equals(Node n) {
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(blocks[i][j] != n.blocks[i][j])
					return false;
			}
		}
		return true;
	}
	
	// Return a String representation of the Node
	public String toString() {
		
		String str = new String();
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				str += blocks[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}
}