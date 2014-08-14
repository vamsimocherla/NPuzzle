import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {

	public static int N = 3;							// Size of Puzzle
	public static String fileName = "src/input.txt";	// Path to Input File
	public static Solver s = new Solver(N);				// Initialize a new Solver
	public static int[][] p = new int[N][N];			// Initialize a new Problem
	
	public static boolean isSolvable(int[][] p) {
		
		// Row of the Empty Block
		int emptyRow = 0;
		
		// Create a 1D array of the blocks
		int[] arr = new int[N*N];
		int k = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(p[i][j] == 0)
					emptyRow = i;
				arr[k++] = p[i][j];
			}
		}
		
		// Count number of inversions
		int inv = 0;
		for(int i=0; i<N*N-1; i++)
			for(int j=i+1; j<N*N; j++)
				if(arr[j] > 0 && arr[i] > arr[j])
					inv++;
		
		// Return False if Odd Board Size and Number of Inversions is Odd
		if((N % 2 == 1) && (inv % 2 == 1))
			return false;
		
		// Return False if Even Board Size and Number of (Inversions+EmptyRow) is Odd
		else if((N % 2 == 0) && (inv+emptyRow % 2 == 1))
			return false;
		
		// Return True if Number of Inversions is Even
		return true;
	}
	
	// Read Input File
	public static void readFile(String file) {
		Scanner in = null;
		try {
			in = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.getMessage();
		}
		int i = 0;
		int j = 0;
		while(in.hasNextInt()) {
			p[i][j++] = in.nextInt();
			if(j == N) {
				i++;
				j = 0;
			}
		}
	}
	
	public static void main(String args[]) {

		// Read the Problem
		readFile(fileName);
		
		// Check if problem is Solvable
		if(!isSolvable(p)) {
			System.out.println("Problem is not Solvable");
			return;
		}
		
		// Print the Initial State
		System.out.println("START");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				System.out.print(p[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

		// Start Time
		long start = System.currentTimeMillis();
		
		// Get the Solution to Problem p
		s.RecursiveBestFirstSearch(p);
		
		// End Time
		long end = System.currentTimeMillis();
		
		// Print the Execution Time
		System.out.println("TIME ELAPSED: " + (end-start)/1000.0 + " SECONDS");
	}
}