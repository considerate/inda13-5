import java.util.Iterator;

public class BinaryTreeSet<T extends Comparable<T>> implements Set<T>, Iterable<T> {
	protected static class Node<T> {
		public Node<T> left; //null
		public Node<T> right; //null
		public Node<T> parent; //null
		protected T data;
		public Node(T data) {
			this(data, null);
		}
		public Node(T data, Node<T> parent) {
			this.parent = parent;
			this.data = data;
		}
	}

	protected Node<T> root; //null
	protected int leaves; //0
	protected int height; //0
	protected int size; //0
	private int numattop; //0
	private final boolean treeOutput;

	public BinaryTreeSet() {
		this(false);
	}

	public BinaryTreeSet(boolean treeOutput) {
		this.treeOutput = treeOutput;
	}
	/**
	 * Adds an element with of a given type T to the set.
	 * If the element already exists in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 * @param data The data of the element to add.
	 * @return true if the data was added.
	 */
	public boolean add(T data) {
		if(data == null) {
			return false;
		}

		boolean addedNode = addIterative(data);
		return addedNode;
	}

	/**
	 * Remove the element with the a given type T from the set.
	 * If the element does not already exist in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 * @param data The data of the element to find and remove.
	 * @return true if the data was removed.
	 */
	public boolean remove(T data) {
		if(data == null) {
			return false;
		}
		return removeIterative(data);
	}
	/**
	 * Checks if an element of a given type T exists in the set.
	 * Required expected time: O(log n) 
	 * @return true if the data was in the set.
	 */
	public boolean contains(T data) {
		if(data == null) {
			return false;
		}
		return containsIterative(data);
	}
	/**
	 * Returns the height of the tree.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * A string representation of the tree.
	 */
	public String toString() {
		if(treeOutput) {
			String str = toString(this.root);
			//remove last \n.
			if(str.length() > 0) {
				str = str.substring(0, str.length()-1);
			}
			return str;
		} else {
			StringBuilder output = new StringBuilder();
			toStringSorted(this.root, output);
			return "["+output.toString()+"]";
		}
	}

	@Override
    public Iterator<T> iterator() {
    	Node<T> rootNode = this.root;
        Iterator<T> it = new Iterator<T>() {
        	private Node<T> currentNode = getLeft(rootNode);

            @Override
            public boolean hasNext() {
                if(currentNode != null) {
                	return true;
                } else {
                	return false;
                }
            }

            @Override
            public T next() {
            	Node<T> node = currentNode;
            	if(node.right != null) {
            		//Next node is far left of right.
            		currentNode = getLeft(currentNode.right);
            		return node.data;
            	} else {
            		//Move up along right side.
            		while(currentNode.parent != null && currentNode.parent.right == currentNode) {
            			currentNode = currentNode.parent;
            		}
            		//One step up
            		currentNode = currentNode.parent;
            		return node.data;
            	}
            }

            private Node<T> getLeft(Node<T> node) {
            	if(node == null) {
            		return node;
            	}
            	if(node.left == null) {
            		return node;
            	}
            	return getLeft(node.left);
            }

            @Override
            public void remove() {
                
            }
        };
        return it;
    }

    private Node<T> getLeft(Node<T> node) {
    	if(node == null) {
    		return node;
    	}
    	if(node.left == null) {
    		return node;
    	}
    	return getLeft(node.left);
    }

	private Node<T> getRight(Node<T> node) {
    	if(node == null) {
    		return node;
    	}
    	if(node.right == null) {
    		return node;
    	}
    	return getRight(node.right);
    }

	/**
	 * Clears the tree of all elements.
	 */
	public void clear() {
		this.root = null;
	}
	/**
	 * Number of elements in the tree.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Number of leaf nodes in the tree.
	 */
	public int getLeaves() {
		return this.leaves;
	}

	protected boolean addIterative(T data) {
		if(this.root == null) { //Create root node
			this.height = 1;
			this.root = createLeaf(data,null);
			return true;
		}

		Node<T> currentNode = this.root;
		
		int depth = 0;
		while(true) {
			int comparator = data.compareTo(currentNode.data);
			if(comparator == 0) {
				return false;
			}
			depth += 1;
			if(comparator < 0) {
				if(currentNode.left == null) {
					currentNode.left = createLeaf(data, currentNode);
					updateHeight(depth+1);
					assert this.leaves == calculateLeaves(this.root);
					return true;
				} else {
					currentNode = currentNode.left;
					continue;
				}
			} else if(comparator > 0) {
				if(currentNode.right == null) {
					currentNode.right = createLeaf(data, currentNode);
					updateHeight(depth+1);
					assert this.leaves == calculateLeaves(this.root);
					return true;
				} else {
					currentNode = currentNode.right;
					continue;
				}
			}
		}
	}

	/**
	 * Updates the height of the tree if the given depth is greater than the trees depth.
	 */
	private void updateHeight(int depth) {
		if(depth > this.height) {
			this.height = depth;
		}		
	}
	/**
	 * Returns the number leaf nodes in the tree.
	 */
	private int calculateLeaves(Node<T> node) {
		if(node == null) {
			return 0;
		}
		if(isLeaf(node)) {
			return 1;
		}
		return calculateLeaves(node.left) + calculateLeaves(node.right);
	}

	/**
	 * Returns the height of the tree.
	 */
	protected int calculateHeight(Node<T> node) {
		if(node == null) {
			return 0;
		}
		int leftHeight = calculateHeight(node.left);
		int rightHeight = calculateHeight(node.right);
		int height = 1+Math.max(leftHeight, rightHeight);
		return height;
	}

	/**
	 * Checks if an element with the given data exists in the tree.
	 */
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

	/**
	 * Removes an element with given data.
	 */
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
				parent.left = removeNode(parent.left);
				setParent(parent.left, parent);
			} else if(compareParent > 0) {
				parent.right = removeNode(parent.right);
				setParent(parent.right, parent);
			}
			//Did the parent now become a leaf?
			if(isLeaf(parent)) {
				//It did :( *so sad* All the children died. wow. such sad.
				this.leaves += 1;
			}
		}
		else {
			this.root = removeNode(this.root);
			setParent(this.root, null);
		}

		//May need to recalculate height of tree.
		if(depth == this.height) {
			this.height = calculateHeight(this.root);
		}
		assert this.leaves == calculateLeaves(this.root);
		return true;		
	}

	private Node<T> removeNode(Node<T> node) {
		int numchildren = 0;
		if(node.left != null) {
			numchildren += 1;
		}
		if(node.right != null) {
			numchildren += 1;
		}

		if(numchildren == 0) {
			return null;
		} else if(numchildren == 1) {
			if(node.left != null) {
				return node.left;
			} else {
				return node.right;
			}
		} else if(numchildren == 2) {
			Node<T> nodeToReplace = getRight(node.left);
			if(isLeaf(nodeToReplace)) {
				this.leaves -= 1;
			}
			nodeToReplace.right = node.right;
			setParent(nodeToReplace.left, nodeToReplace);
			setParent(nodeToReplace.right, nodeToReplace);
			nodeToReplace.parent.right = nodeToReplace.left;
			if(isLeaf(nodeToReplace.parent)) {
				this.leaves += 1;
			}
			if(node.left != nodeToReplace) {
				nodeToReplace.left = node.left;
			}
			return nodeToReplace;
		}		
		return null;
	}

	/**
	 * Returns the root node of the tree. Null if there is no root.
	 */
	protected Node<T> getRoot() {
		return this.root;
	}

	protected void setParent(Node<T> node, Node<T> parent) {
		if(node != null) {
			node.parent = parent;
		}
	}

	/**
	 * Returns true if the given node is a leaf node.
	 */
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

	/**
	 * Creates a leaf node. Adjusts leaf
	 */
	protected Node<T> createLeaf(T data, Node<T> parent) {
		this.size += 1;
		if(!isLeaf(parent)) {
			this.leaves += 1;
		}
		return new Node<T>(data, parent);
	}

	protected void toStringSorted(Node<T> node, StringBuilder builder) {
		if(node != null) {
			if(node.left != null) {
				toStringSorted(node.left, builder);
				builder.append(", ");
			}
			builder.append(node.data.toString());
			if(node.right != null) {
				builder.append(", ");
				toStringSorted(node.right,builder);
			}
		}
	}

	protected String toStringData(Node<T> node) {
		if(node.data == null) {
			return "(null)";
		}
		return "("+ node.data.toString()+")";
	}

	protected String toString(Node<T> node) {
		StringBuilder ouput = new StringBuilder();
		toString(node, ouput, 0, "", false);
		return ouput.toString();
	}

	protected void toString(Node<T> node, StringBuilder output, int level, String prefix, boolean bar) {
		if(node == null) {
			return;
		}
		if(level > 0) {
			if(bar) {
				prefix += "│   ";
			} else {
				prefix += "    ";	
			}
		}
		output.append(toStringData(node)+"\n");
		int numchildren = 0;
		if(node.left != null) {
			numchildren += 1;
		}
		if(node.right != null) {
			numchildren += 1;
		}
		if(numchildren == 2) {
			output.append(prefix);
			output.append("├── R");
			toString(node.right,output, 1, prefix, true);
			output.append(prefix);
			output.append("└── L");
			toString(node.left,output,1, prefix, false);

		} else if(numchildren == 1) {
			if(node.right == null) {
				output.append(prefix);
				output.append("│   \n");	
				output.append(prefix);
				output.append("└── L");
				toString(node.left,output,1, prefix, false);
			} else {
				output.append(prefix);
				output.append("└── R");
				toString(node.right,output,1, prefix, false);
			}
		}
	}

	public static void main(String[] args) {
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

		System.out.println("Iterate:");
		for(int x: treeSet) {
			System.out.println(x);
		}

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
}