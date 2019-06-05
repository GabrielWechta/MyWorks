
public class BinaryTree <U extends Comparable<U>> {

	Node <U> root;
	
	private U findSmallestValue(Node <U> root) { 
	    return root.left == null ? root.value : findSmallestValue(root.left);
	}
	
	private Node <U> addRecursive (Node <U> current, U value) {
		if(current == null) return new Node <U>(value);
		
		if(value.compareTo(current.value) < 0) {
			current.left = addRecursive(current.left, value);
		}
		else if (value.compareTo(current.value) > 0) {
			current.right = addRecursive(current.right, value);
		}
		else {
			System.out.println("Value " + current.value + " already existed in the tree");
			return current;
		}
		
		return current;
	}
		
	private Node <U> deleteRecursive(Node <U> current, U value) {
	    if (current == null) {
	        return null;
	    }
	 
	    if (value.equals( current.value)) {
	    	if (current.left == null && current.right == null) { //no child scenerio
	    	    return null;
	    	}
	    	
	    	if (current.right == null) { // one child (left) scenario
	    	    return current.left;
	    	}
	    	 
	    	if (current.left == null) { // one child (right) scenario
	    	    return current.right;
	    	} 
	    	
	    	// two children scenario
	    	U smallestValue = findSmallestValue(current.right);
	    	current.value = smallestValue;
	    	current.right = deleteRecursive(current.right, smallestValue);
	    	return current;	
	    } 

	    if (value.compareTo(current.value) < 0) {
	        current.left = deleteRecursive(current.left, value);
	        return current;
	    }
	    current.right = deleteRecursive(current.right, value);
	    return current;
	}
	
	private boolean containsNodeRecursive(Node <U> current, U value) {
	    if (current == null) {
	        return false;
	    } 
	    if (value.equals(current.value)) {
	        return true;
	    } 
	    return value.compareTo(current.value) < 0 ? containsNodeRecursive(current.left, value) : containsNodeRecursive(current.right, value);
	}
	
	private String printInOrderRecursive(Node <U> node) {
		String drawing = "";
	    if (node != null) {
	    	drawing += node.value + "(";
	    	drawing += printInOrderRecursive(node.left);
	    	drawing += ", ";
	        drawing += printInOrderRecursive(node.right);
	        drawing += ")";
	    }
	    else drawing += "X";
	    return drawing;
	}
	
	public  void insert(U value) {
		this.root = addRecursive(this.root, value);
	}
	
	public boolean search(U value) {
	    return containsNodeRecursive(root, value);
	}
	
	public void delete(U value) {
	    root = deleteRecursive(root, value);
	}
	
	public String draw() {
		String treeDrawing = "";
		System.out.println();
		treeDrawing = printInOrderRecursive(this.root);
		System.out.println(treeDrawing);
		return treeDrawing;
	}
}
