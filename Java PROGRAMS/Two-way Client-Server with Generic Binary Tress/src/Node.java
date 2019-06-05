

public class Node <U> {
		public U value;
		public Node <U> left;
		public Node <U> right;
		
		Node(U value) {
			this.value = value;
			this.left = null;
			this.right = null;
		}
	}
	