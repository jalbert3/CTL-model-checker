import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class FiniteStateMachine {
	private final int states; // how many states do we have? 
	private SparseMatrix matrix; // matrix representation of the graph
	private int[] initialStates; // starting states of the matrix
	private ArrayList<LinkedList<String>> propositions; // each index of the arraylist corresponds with the index's state and the linked list holds the propositions of each state
	private LinkedList<String> properties; // properties that must be tested
	private HashSet<String> propositionSet; // hashset of all propositions in the graph.
	private Stack<Character> operator; // stack for the operator
	private Stack<ArrayList<Integer>> operand; // stack for operands in evaluation of properties
	
	FiniteStateMachine(int states){
		this.states = states;
		this.matrix = new SparseMatrix(this.states);
		propositions = new ArrayList<LinkedList<String>>(this.states);
		propositionSet = new HashSet<String>();
		propositionSet.add("t");
		operator = new Stack<Character>();
		operand = new Stack<ArrayList<Integer>>();
		
		for(int i = 0; i < this.states; i++){
			this.propositions.add(new LinkedList<String>());
			this.propositions.get(i).add("t");
		}
		this.properties = new LinkedList<String>(); 
	}
	
	public static FiniteStateMachine readIn(){
		java.util.Scanner scan  = new java.util.Scanner(System.in);
		if(!scan.nextLine().equals("FSM")){ // is it an FSM file?
			System.out.println("Invalid input");
			scan.close();
			return null;
		}
		int states = Integer.parseInt(scan.nextLine().split(" ")[1]); // number of states
		FiniteStateMachine fsm = new FiniteStateMachine(states);
		scan.nextLine(); //skips past INIT
		String line;
		fsm.initialStates = new int[fsm.states];
		while(!(line = scan.nextLine()).split(" ")[0].equals("ARCS")){ // while we are still listing initial states and haven't started on ARCS
			String[] splitLine = line.split(" ");
			fsm.initialStates[Integer.parseInt(splitLine[0])] = Integer.parseInt(splitLine[2]);
		}
		int edges = Integer.parseInt(line.split(" ")[2]); // extract the number of arcs 
		for(int i = 0; i < edges; i ++){
			line = scan.nextLine();
			String[] splitLine = line.split(" ");
			
			int from  = Integer.parseInt(splitLine[0]); // get
			int to = Integer.parseInt(splitLine[2]); // arc
			int weight = Integer.parseInt(splitLine[4]); // information
			fsm.matrix.addEdgeToSparseMatrix(from, to, weight);
		}
		if(!scan.nextLine().equals("END")){ //sanity check
			System.out.println("Invalid input");
			scan.close();
			return null;
		}
		while(!(line = scan.nextLine()).equals("PROPERTIES")){ // assign propositions to each necessary state
			String propositions = line.split(" ")[0];
			fsm.propositionSet.add(propositions);
			String nextLine;
			while(!(nextLine = scan.nextLine()).equals("end_" + propositions)){
				int state = Integer.parseInt(nextLine.split(" ")[1]);
				fsm.propositions.get(state).add(propositions);
			}
		}
		while(scan.hasNextLine()){
			fsm.properties.add(scan.nextLine());
		}
		scan.close(); // done reading in the file
		SparseMatrix matrixCheck = fsm.getSparseMatrix();
		for(int i = 0; i < fsm.states; i ++){
			if(matrixCheck.getEdges(i).isEmpty()){
				matrixCheck.addEdgeToSparseMatrix(i, i, 1); // need to self loop if no other outgoing edges.
			}
		}
		
		return fsm;
	}
	
	public void checkProperties(){ // whether the properties hold for this specific state machine.
		int [] vector;
		for(String property : this.properties){
			vector  = check(property);
			/*for(int x : vector){
				System.out.print(x);
			}*/
			int[] x = AtomicEvaluation.conjunction(vector, this.getInitialStates());
			boolean flag = false; // if there's a one then it means that it holds for at least one starting state
			for(int j = 0; j < x.length; j++){
				if(x[j] == 1){
					flag = true;
					break;
				}
			}
			System.out.println(property + " evaluates to: " + flag); // print out true or false and the whole vector
			System.out.print("[");
			for(int i = 0; i < x.length; i++){
				System.out.print(vector[i] + ",");
			}
			System.out.println("]\n");
		}
	}
	public int[] check(String property){
		property = property.replace(" ", "");
		for(int i = 0; i < property.length(); i++){
			/*System.out.println((propositionSet.contains(Character.toString('p'))));
			System.out.println(operand.size() + " " + operator.size());*/
			if(property.charAt(i) == '('){
			}else if(property.charAt(i) == ')'){ // pop off the next operator
				computeOperator();
			}else if(propositionSet.contains((Character.toString(property.charAt(i))))){ // push a proposition onto the operand stack
				operand.push(arrayToList(AtomicEvaluation.createVectorFromProposition(Character.toString(property.charAt(i)), this))); // create a vector for this new proposition and push onto the operand stack for later use
			}else{
				operator.push(property.charAt(i)); // must be an operator
			}	
		}
		return listToArray(operand.pop()); // why is it empty?
	}
	
	private void computeOperator(){
		int[] a = listToArray(operand.pop());
		int[] b;
		
		String token = "";
		while(!token.equals("XE") && !token.equals("UE") && !token.equals("GE") && !token.equals("dna") && !token.equals("ro") && !token.equals("~")){
			token += operator.pop();
			//System.out.println(token);
		}
		
		switch(token){
			case "XE":
				a = AtomicEvaluation.dotProduct(a, this.getSparseMatrix().getPreImageMatrix());
				break;
			case "UE":
				b = listToArray(operand.pop());
				a = AtomicEvaluation.until(b, a, this);
				break;
			case "GE":
				a = AtomicEvaluation.global(a, this);
				break;
			case "dna":
				b = listToArray(operand.pop());
				a = AtomicEvaluation.conjunction(a, b);
				break;
			case "ro":
				b = listToArray(operand.pop());
				a = AtomicEvaluation.disjunction(a, b);
				break;
			case "~":
				a = AtomicEvaluation.invert(a);
				break;
			}
		operand.push(arrayToList(a));
	}

	public HashSet<String> getPropositionSet() {
		return propositionSet;
	}

	public void setPropositionSet(HashSet<String> propositionSet) {
		this.propositionSet = propositionSet;
	}

	public int getStates() {
		return states;
	}

	public int[] getInitialStates() {
		return initialStates;
	}

	public void setInitialStates(int[] initialStates) {
		this.initialStates = initialStates;
	}

	public ArrayList<LinkedList<String>> getPropositions() {
		return propositions;
	}

	public void setPropositions(ArrayList<LinkedList<String>> propositions) {
		this.propositions = propositions;
	}

	public LinkedList<String> getProperties() {
		return properties;
	}

	public void setProperties(LinkedList<String> properties) {
		this.properties = properties;
	}
	
	public SparseMatrix getSparseMatrix() {
		return matrix;
	}

	public void setSparseMatrix(SparseMatrix matrix) {
		this.matrix = matrix;
	}
	
	public static ArrayList<Integer> arrayToList(int[] x){
		ArrayList<Integer> list = new ArrayList<Integer>(x.length);
		for(int i = 0; i < x.length; i++){
			list.add(x[i]);
		}
		return list;
	}
	
	public static int[] listToArray(ArrayList<Integer> x){
		int[] array = new int[x.size()];
		for(int i = 0; i < x.size(); i++){
			array[i] = x.get(i);
		}
		return array;
	}
}
