public class BinaryTreeSet<T extends Comparable<T>> implements Set<T> {
	protected static class Node<T> {
		public Node<T> left; //null
		public Node<T> right; //null
		protected T data;
		public Node(T data) {
			this.data = data;
		}
	}

	protected Node<T> root; //null
	protected int leaves; //0
	protected int height; //0
	protected int size; //0
	private int numattop; //0
	private final boolean treeOutput = true;

	public BinaryTreeSet() {
		//use default values only, no work needed here.
	}

	public static void main(String [] args) {
		BinaryTreeSet<Integer> treeSet = new BinaryTreeSet<Integer>();
		treeSet.add(9);
		treeSet.add(6);
		treeSet.add(4);
		treeSet.add(5);
		treeSet.add(14);
		treeSet.add(11);
		treeSet.add(22);
		treeSet.add(8);

		treeSet.add(12);
		treeSet.add(95);
		treeSet.add(2);
		treeSet.add(null);
		System.out.println(treeSet);
		System.out.println("leaves:" +treeSet.getLeaves());
		treeSet.remove(11);
		System.out.println(treeSet);
		System.out.println("leaves:" +treeSet.getLeaves());
		treeSet.remove(5);
		System.out.println(treeSet);
		System.out.println("leaves:" +treeSet.getLeaves());

		BinaryTreeSet<String> stringSet = new BinaryTreeSet<String>();
		stringSet.add("Hello");
		stringSet.add("World!");
		stringSet.add("How are you?");
		stringSet.add("I'm fine, thank you.");
		stringSet.add("Asd");
		System.out.println(stringSet);

	}

	protected Node<T> getRoot() {
		return this.root;
	}

	public boolean add(T data) {
		if(data == null) {
			return false;
		}

		boolean addedNode = addIterative(data);
		return addedNode;
	}

	protected boolean addIterative(T data) {
		if(this.root == null) { //Create root node
			this.height = 1;
			this.root = addLeaf(data,null);
			return true;
		}

		Node<T> currentNode = this.root;
		int comparator;
		int depth = 0;
		while((comparator = data.compareTo(currentNode.data)) != 0) {
			depth += 1;
			if(comparator < 0) {
				if(currentNode.left == null) {
					currentNode.left = addLeaf(data, currentNode);
					updateHeight(depth+1);
					assert this.leaves == calculateLeaves(this.root);
					return true;
				} else {
					currentNode = currentNode.left;
					continue;
				}
			} else if(comparator > 0) {
				if(currentNode.right == null) {
					currentNode.right = addLeaf(data, currentNode);
					updateHeight(depth+1);
					assert this.leaves == calculateLeaves(this.root);
					return true;
				} else {
					currentNode = currentNode.right;
					continue;
				}
			}
		}
		return false;
	}

	private void updateHeight(int depth) {
		if(depth > this.height) {
			this.height = depth;
		}		
	}

	private int calculateLeaves(Node<T> node) {
		if(node == null) {
			return 0;
		}
		if(isLeaf(node)) {
			return 1;
		}
		return calculateLeaves(node.left) + calculateLeaves(node.right);
	}

	protected int calculateHeight(Node<T> node) {
		if(node == null) {
			return 0;
		}
		int leftHeight = calculateHeight(node.left);
		int rightHeight = calculateHeight(node.right);
		int height = 1+Math.max(leftHeight, rightHeight);
		return height;
	}

	private boolean containsIterative(T data) {
		Node<T> currentNode = this.root;
		int comparator;
		int depth = 0;
		while((comparator = data.compareTo(currentNode.data)) != 0) {
			depth += 1;
			if(comparator < 0) {
				if(currentNode.left == null) {
					return false;
				} else {
					currentNode = currentNode.left;
					continue;
				}
			} else if(comparator > 0) {
				if(currentNode.right == null) {
					return false;
				} else {
					currentNode = currentNode.right;
					continue;
				}
			}
		}	
		assert comparator == 0;
		return true;	
	}

	public void clear() {
		this.root = null;
	}

	protected boolean removeIterative(T data) {
		Node<T> currentNode = this.root;
		Node<T> previousNode = null;
		int comparator;
		int depth = 0;
		while((comparator = data.compareTo(currentNode.data)) != 0) {
			depth += 1;
			previousNode = currentNode;
			if(comparator < 0) {
				if(currentNode.left == null) {
					return false;
				} else {
					currentNode = currentNode.left;
					continue;
				}
			} else if(comparator > 0) {
				if(currentNode.right == null) {
					return false;
				} else {
					currentNode = currentNode.right;
					continue;
				}
			}
		}
		assert comparator == 0;
		depth += 1;
		if(isLeaf(currentNode)) {
			this.leaves -= 1;
		}

		if(currentNode != this.root) {
			Node<T> parent = previousNode;
			int compareParent = data.compareTo(parent.data);
			if(compareParent < 0) {
				parent.left = null;
			} else if(compareParent > 0) {
				parent.right = null;
			}
			//Did the parent now become a leaf?
			if(isLeaf(parent)) {
				//It did :( *so sad*
				this.leaves += 1;
			}
			//May need to recalculate height of tree.
			if(depth == this.height) {
				this.height = calculateHeight(this.root);
			}
		}
		else {
			this.root = null;
		}
		//assert this.leaves == calculateLeaves(this.root);
		return true;		
	}

	/*private boolean addToNode(T data, Node<T> node) {
		if(data == null) {
			return false;
		}		

		if(data.compareTo(node.data) == 0) { //equals
			return false;
		}
		if(data.compareTo(node.data) < 0) { // <
			if(node.left == null) {
				addLeftLeaf(node, data);
				return true;
			}
			return addToNode(data, node.left);
		}
		if(data.compareTo(node.data) > 0) { // >
			if(node.right == null) {
				addRightLeaf(node, data);
				return true;
			}
			return addToNode(data, node.right);
		}

		return false;
	}*/

	protected boolean isLeaf(Node<T> node) {
		if(node == null) {
			return false;
		}
		if(node.left == null && node.right == null) {
			return true;
		} else {
			return false;
		}
	}

	/*protected Node<T> addLeftLeaf(Node<T> node, T data) {
		node.left = addLeaf(data, node);
		return node;
	}

	protected Node<T> addRightLeaf(Node<T> node, T data) {
		node.right = addLeaf(data, node);
		return node;
	}*/

	protected Node<T> addLeaf(T data, Node<T> node) {
		this.size += 1;
		if(!isLeaf(node)) {
			this.leaves += 1;
		}
		return new Node<T>(data);
	}

	public boolean remove(T data) {
		if(data == null) {
			return false;
		}
		return removeIterative(data);
	}

	public boolean contains(T data) {
		if(data == null) {
			return false;
		}
		return containsIterative(data);
	}

	public int size() {
		return this.size;
	}

	public int getLeaves() {
		return this.leaves;
	}

	public int getHeight() {
		return this.height;
	}

	public String toString() {
		if(treeOutput) {
			String str = toString(this.root);
			//remove last \n.
			str = str.substring(0, str.length()-1);
			return str;
		} else {
			return "["+toStringSorted(this.root)+"]";
		}
	}

	public String toStringSorted(Node<T> node) {
		if(node != null) {
			StringBuilder builder = new StringBuilder();
			if(node.left != null) {
				builder.append(toStringSorted(node.left));
				builder.append(", ");
			}
			builder.append(node.data.toString());
			if(node.right != null) {
				builder.append(", ");
				builder.append(toStringSorted(node.right));
			}
			return builder.toString();
		}
		return "";
	}

	protected String toStringData(Node<T> node) {
		return "("+ node.data.toString()+")";
	}

	protected String toString(Node<T> node) {
		return toString(node,0,"", false);
	}

	protected String toString(Node<T> node, int level, String prefix, boolean bar) {
		String pre;
		if(level > 0) {
			StringBuilder prefixBuilder = new StringBuilder(level*4);
			prefixBuilder.append(prefix);
			if(bar) {
				prefixBuilder.append("│   ");
			} else {
				prefixBuilder.append("    ");	
			}
			pre = prefixBuilder.toString();
		} else {
			pre = prefix;
		}
		String string = toStringData(node)+"\n";
		int numchildren = 0;
		if(node.left != null) {
			numchildren += 1;
		}
		if(node.right != null) {
			numchildren += 1;
		}
		if(numchildren == 2) {
			string += pre + "├── L"+ toString(node.left,1, pre, true);
			string += pre + "└── R"+ toString(node.right,1, pre, false);

		} else if(numchildren == 1) {
			if(node.left == null) {
				string += pre + "│   \n";	
				string += pre + "└── R"+ toString(node.right,1, pre, false);
			} else {
				string += pre + "└── L"+ toString(node.left,1, pre, false);
			}
		}

		return string;
	}
}