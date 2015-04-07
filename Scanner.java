import java.util.LinkedList;


public class Scanner {
	public static void main(String args[]){
		FiniteStateMachine fsm = FiniteStateMachine.readIn();
		fsm.checkProperties();
		/*evaluationTest(fsm);
		dataTest(fsm);*/
	}
	
	public static void dataTest(FiniteStateMachine fsm){ // tests to make sure the machine has read in and constructed the proper data structure from input
		int i = 0;
		for(LinkedList<Pair<Integer, Integer>> list : fsm.getSparseMatrix().getPreImageMatrix()){
			System.out.println(i++);
			for(Pair<Integer, Integer> pair : list){
				pair.print();
			}
		}
		for(String x : fsm.getProperties()){
			System.out.println(x);
		}
		for(int x : fsm.getInitialStates()){
			System.out.println(x);
		}
		i = 0;
		for(LinkedList<String> list : fsm.getPropositions()){
			System.out.println(i++);
			for(String s : list){
				System.out.println(s);
			}
		}	
	}
	
	public static void evaluationTest(FiniteStateMachine machine){ // tests vector construction, dot product and dis/con-junction
		int[] x = AtomicEvaluation.createVectorFromProposition("q", machine);
		System.out.println(x.length);
		for(int a : x){
			System.out.println(a);
		}System.out.println("");
		
		int[] y = AtomicEvaluation.dotProduct(x, machine.getSparseMatrix().getPostImageMatrix());
		for(int a : y){
			System.out.println(a);
		}
		
		int[] test = {0,1,0};
		int[] z = AtomicEvaluation.conjunction(x, test);
		for(int a : z){
			System.out.println(a);
		}
		
		int[] w = AtomicEvaluation.disjunction(x, test);
		for(int a : w){
			System.out.println(a);
		}
		
	}
}
