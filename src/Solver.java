import java.util.ArrayList;
import java.util.List;


public class Solver {

	double infinity = Double.MAX_VALUE;		// Infinity
	int N;									// Size of the Puzzle
	Result solution = new Result(N);		// Solution
	Node goal;								// Goal Node
	Node failure;							// Failure Node
	String[] path = new String[100];		// Path of Graph
	int[][] goalState;						// Goal State of the Puzzle

	public Solver(int n) {
		
		this.N = n;
		goal = new Node(N);
		failure = new Node(N);
		goalState = new int[N][N];
		
		// Set the Goal Node
		int cnt = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				goalState[i][j] = cnt++;
			}
		}
		goal.setBlocks(goalState);
	}
	
	// Make a new Problem Node
	public Node makeNode(int[][] problem) {
		Node n = new Node(problem.length);
		n.setBlocks(problem);
		return n;
	}
	
	// Heuristic Function - Tiles Out of Place
	public int h(Node n) {
		int H = 0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(n.blocks[i][j] != goal.blocks[i][j]) {
					H++;
				}
			}
		}
		return H;
	}
	
	// Returns a Solution or a Failure
	public void RecursiveBestFirstSearch(int[][] problem) {
		// return RBFS(problem, MAKE-NODE(problem.INITIAL-STATE), infinity)
		Node n = makeNode(problem);
		n.f = h(n);
		solution = RBFS(problem, n, infinity, 0, new Node(N));
	}
	
	public void printSuccessors(List<Node> list) {
		System.out.println("Successors:");
		int size = list.size();
		for(int i=0; i<size; i++) {
			System.out.println(list.get(i).move);
			System.out.println(list.get(i).toString());
		}
		System.out.println("---------------------------------------------");
	}
	
	// Returns a Solution or a Failure and f_cost limit
	public Result RBFS(int[][] problem, Node node, double f_limit, int moves, Node prev) {
		
		// Store the Path to reach the Goal
		path[moves] = node.move;
		
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if(node.equals(goal)) {
			
			// Print the Path to reach the Goal
			for(int i=1;i<=moves;i++) {
				System.out.println(path[i]);
			}
			
			// Print the Path Cost to reach the Goal
			System.out.println("PATH COST: " + moves);
			
			// Return the Solution
			return new Result(node, f_limit);
		}
		
		// successors <- []
		// for each action in problem.ACTION(node.STATE) do
		// add CHILD-NODE(problem, node, action) into successors
		List<Node> successors = getSuccessors(node, prev);
		
		// Save the previous node
		prev = node;
		
		// if successors is empty then return failure, infinity
		if(successors.size() == 0)
			return new Result(failure, infinity);
		
		// for each s in successors do - update f with value from previous search, if any
		int size = successors.size();
		for(int i=0; i<size; i++) {
			// s.f <- max(s.g + s.h, node.f)
			Node s = successors.get(i);
			s.f = Math.max(moves + h(successors.get(i)), node.f);
		}
		
		// repeat
		while(true) {
			// best <- the lowest f-value node in successors
			Node best = getBestNode(successors);
			
			// if best.f > f_limit then return failure, best.f
			if(best.f > f_limit)
				return new Result(failure, best.f);
			
			// alternative <- the second lowest f-value node in successors
			Node altNode = getNextBestNode(successors, best);
			
			// result, best.f <- RBFS(problem, best, min(f_limit, alternative))
			Result res = RBFS(problem, best, Math.min(f_limit, altNode.f), moves+1, prev);
			Node result = res.n;
			best.f = res.f_limit;
			
			// if result != failure then return result
			if(!result.equals(failure))
				return new Result(result, f_limit);
		}
	}
	
	// Get Best Node
	public Node getBestNode(List<Node> list) {
		int l = list.size();
		int ind = 0;
		double minVal = infinity;
		for(int i=0; i<l; i++) {
			if(list.get(i).f < minVal) {
				minVal = list.get(i).f;
				ind = i;
			}
		}
		return list.get(ind);
	}
	
	// Get Next Best Node
	public Node getNextBestNode(List<Node> list, Node n) {
		int l = list.size();
		int ind = 0;
		double minVal = infinity;
		for(int i=0; i<l; i++) {
			if(!list.get(i).equals(n) && list.get(i).f < minVal) {
				minVal = list.get(i).f;
				ind = i;
			}
		}
		return list.get(ind);
	}
	
	// Get Successors to Current Node
	public List<Node> getSuccessors(Node n, Node prev) {
		
		// Initialize an empty list
		List<Node> successors = new ArrayList<Node>();
		
		// Get the Position of the empty block
		int i = 0;
		int j = 0;
		boolean flag = false;
		for(i=0; i<N; i++) {
			for(j=0; j<N; j++) {
				if(n.blocks[i][j] == 0) {
					flag = true;
					break;
				}
			}
			if(flag)
				break;
		}
		
		// Add the possible moves only if not the previous node
		Node tmp;
		if(i-1 >= 0) {
			tmp = childNode(i, j, i-1, j, n, "UP");
			if(!tmp.equals(prev))
				successors.add(tmp);
		}
		if(i+1 < N)  {
			tmp = childNode(i, j, i+1, j, n, "DOWN");
			if(!tmp.equals(prev))
				successors.add(tmp);
		}
		if(j-1 >= 0) {
			tmp = childNode(i, j, i, j-1, n, "LEFT");
			if(!tmp.equals(prev))
				successors.add(tmp);
		}
		if(j+1 < N)  {
			tmp = childNode(i, j, i, j+1, n, "RIGHT");
			if(!tmp.equals(prev))
				successors.add(tmp);
		}
		return successors;
	}
	
	// Check if Node is present in a List
	public boolean check(Node n, List<Node> list) {
		int size = list.size();
		for(int i=0; i<size; i++) {
			if(n.equals(list.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	// Child Node to Current Node
	public Node childNode(int p, int q, int r, int s, Node n, String move) {
		
		// Create a temporary node
		Node tmp = new Node(N);
		tmp.setBlocks(n.blocks);
		tmp.move = move;
		
		// Move the empty block from [p,q] to [r,s]
		tmp.blocks[p][q] = tmp.blocks[r][s];
		tmp.blocks[r][s] = 0;
		
		return tmp;
	}
	
	// Return a String representation of the Solution
	public String solution() {
		return solution.n.toString();
	}
}
