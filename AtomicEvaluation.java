import java.util.ArrayList;
import java.util.LinkedList;


public class AtomicEvaluation {
	public static int[] dotProduct(int[] vector, ArrayList<LinkedList<Pair<Integer,Integer>>> matrix){ // dot product of a sparse matrix and a vector of the same length 
		int[] product = new int[vector.length];
		for(int i = 0; i < matrix.size(); i++){ // for each state
			int row = 0;
			LinkedList<Pair<Integer, Integer>> list = matrix.get(i);
			for(Pair<Integer, Integer> pair : list){ // for each edge from a particular state. 
				row += vector[pair.getE()];
			}
			if(row >= 1){
				product[i] = 1;
			}else{
				product[i] = 0;
			}
		}
		/*for(int x : product){
			System.out.print(x);
		}*/
		return product;
	}
	
	public static int[] createVectorFromProposition(String proposition, FiniteStateMachine machine){ // given a proposition that is a string, creates a boolean vector corresponding to whether or not a state has that specific proposition.
		int[] vector = new int[machine.getStates()];
		for(int i = 0; i < vector.length; i++){
			if(machine.getPropositions().get(i).contains(proposition)){
				vector[i] = 1; // that state has the particular proposition
			}else{
				vector[i] = 0; // it doens't have it
			}
		}
		/*for(int x : vector){
			System.out.print(x);
		}*/
		return vector;
	}
	
	public static int[] disjunction(int[] x, int[] y){ // disjunction of two vectors of the same size, essentially ors corresponding elements together
		if(x.length != y.length){ // something went wrong...
			return null;
		}
		int[] product = new int[x.length];
		for(int i = 0; i < x.length; i++){
			if(x[i] >= 1 || y[i] >= 1){
				product[i] = 1;
			}else{
				product[i] = 0;
			}
		}
		return product;
	}
	
	public static int[] conjunction(int[] x, int[] y){ // conjunction of two vectors of the same size, essentially ands corresponding elements together
		if(x.length != y.length){ // something went wrong...
			return null;
		}
		int[] product = new int[x.length];
		for(int i = 0; i < x.length; i++){
			if(x[i] >= 1 && y[i] >= 1){
				product[i] = 1;
			}else{
				product[i] = 0;
			}
		}
		return product;
	}
	
	public static int[] invert(int[] x){ // inverts a vector, flipping the values from 1 to 0 or 0 to 1.
		int[] product = new int[x.length];
		for(int i = 0; i < x.length; i++){
			if(x[i] >= 1){
				x[i] = 0;
			}else{
				x[i] = 1;
			}
		}
		return product;
	}
	
	public static int[] until(int[] g, int [] f, FiniteStateMachine machine){
		return untilHelper(f, g, machine);
	}
	
	private static int[] untilHelper(int[] h_n, int[] g, FiniteStateMachine machine){
		int[] h_next = disjunction(h_n, conjunction(dotProduct(h_n, machine.getSparseMatrix().getPreImageMatrix()), g));
		if(arrayEquality(h_n, h_next)){
			/*for(int x : h_next){
				System.out.print("k" + x);
			}*/
			return h_next;
		}else{
			return untilHelper(h_next, g, machine);
			
		}
	}
	
	public static int[] global(int[] f, FiniteStateMachine machine){
		int[] h_0 = f;
		return globalHelper(conjunction(h_0, h_0), h_0, machine); // conjunction returns a deep copy
	}
	
	private static int[] globalHelper(int[] f, int[] h_n, FiniteStateMachine machine){
		int[] h_next = conjunction(dotProduct(h_n, machine.getSparseMatrix().getPreImageMatrix()), f);
		/*for(int x : h_next){
			System.out.print(x);
		}*/
		if(arrayEquality(h_next, h_n)){
			return h_next;
		}else{
			return globalHelper(f, h_next, machine);
		}
	}
	
	private static boolean arrayEquality(int[] a, int[] b){
		if (a.length != b.length){
			return false;
		}
		for(int i = 0; i < a.length; i ++){
			if(a[i] != b[i]){
				return false;
			}
		}
		return true;
	}
	
}
