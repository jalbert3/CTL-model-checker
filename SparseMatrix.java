import java.util.ArrayList;
import java.util.LinkedList;


public class SparseMatrix {
	private ArrayList<LinkedList<Pair<Integer,Integer>>> postImageMatrix;
	private ArrayList<LinkedList<Pair<Integer,Integer>>> preImageMatrix;
	
	SparseMatrix(int states){
		postImageMatrix = new ArrayList<LinkedList<Pair<Integer,Integer>>>(states);
		for(int i = 0; i < states; i ++){
			postImageMatrix.add(new LinkedList<Pair<Integer, Integer>>());
		}
		preImageMatrix = new ArrayList<LinkedList<Pair<Integer,Integer>>>(states);
		for(int i = 0; i < states; i ++){
			preImageMatrix.add(new LinkedList<Pair<Integer, Integer>>());
		}
	}
	
	public void addEdgeToSparseMatrix(int from, int to, int weight){
		preImageMatrix.get(from).add(new Pair<Integer, Integer>(to, weight));
		postImageMatrix.get(to).add(new Pair<Integer, Integer>(from, weight));
	}
	
	public ArrayList<LinkedList<Pair<Integer, Integer>>> getPostImageMatrix() {
		return postImageMatrix;
	}

	public void setPostImageMatrix(
			ArrayList<LinkedList<Pair<Integer, Integer>>> postImageMatrix) {
		this.postImageMatrix = postImageMatrix;
	}

	public ArrayList<LinkedList<Pair<Integer, Integer>>> getPreImageMatrix() {
		return preImageMatrix;
	}

	public void setPreImageMatrix(
			ArrayList<LinkedList<Pair<Integer, Integer>>> preImageMatrix) {
		this.preImageMatrix = preImageMatrix;
	}

	public LinkedList<Pair<Integer, Integer>> getEdges(int state){
		return postImageMatrix.get(state);
		
	}
}
