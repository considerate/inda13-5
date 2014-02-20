import java.util.Random;

public class TreapSet<T extends Comparable<T>> extends BinaryTreeSet<T> {
	private static final Random random = new Random();

	protected static class Node<T> extends BinaryTreeSet.Node<T> {
		public double prio;

		public Node(T data) {
			super(data);
			this.prio = random.nextDouble();
		}

		//Initialize with a binary tree set node.
		public Node(BinaryTreeSet.Node<T> node) {
			this(node.data);
		}
	}

	/**
	 * Adds an element with of a given type T to the set.
	 * If the element already exists in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 * @param data The data of the element to add.
	 * @return true if the data was added.
	 */
	@Override
	public boolean add(T data) {
		if(data == null) {
			return false;
		}
		//Mutable boolean
		boolean[] added = {false};
		this.root = addRecursive(data, this.root, added);
		this.height = calculateHeight(this.root);
		return added[0];
	}

	/**
	 * Remove the element with the a given type T from the set.
	 * If the element does not already exist in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 * @param data The data of the element to find and remove.
	 * @return true if the data was removed.
	 */
	@Override
	public boolean remove(T data) {
		if(data == null) {
			return false;
		}
		//Mutable boolean
		boolean[] removed = {false};
		this.root = removeRecursive(data, this.root, removed);
		setParent(this.root, null);
		this.height = calculateHeight(this.root);
		return removed[0];
	}

	/**
	 * Removes a node with the element data from the set.
	 * The tree is then rotated to ensure the random priority given to each node is
	 * in the correct order (higher values higher up the tree).
	 */
	protected BinaryTreeSet.Node<T> removeRecursive(T data, BinaryTreeSet.Node<T> node, boolean[] removed) {
		if(node == null) {
			removed[0] = false;
			return node;
		}

		int comparator = data.compareTo(node.data);
		if(comparator == 0) {
			removed[0] = true;
			this.size -= 1;

			int childCount = childCount(node);
			if(childCount == 0) {
				//No child, remove by setting this node to null.
				return null;
			}
			if(childCount == 1) {
				if(node.left != null) {
					setParent(node.left, node);
					return node.left;
				} else {
					setParent(node.right, node);
					return node.right;
				}
			}
			if(childCount == 2) {
				double leftPrio = ((Node<T>) node.left).prio;
				double rightPrio = ((Node<T>) node.right).prio;
				if(leftPrio > rightPrio) {
					node.left.right = node.right;
					setParent(node.left.right, node.left);
					setParent(node.left, node);
					return node.left;
				} else {
					node.right.left = node.left;
					setParent(node.right.left, node.right);
					setParent(node.right, node);
					return node.right;
				}
			}
		} else if(comparator < 0) {
			node.left = removeRecursive(data, node.left, removed);
			setParent(node.left, node);
			return node;
		} else if(comparator > 0) {
			node.right = removeRecursive(data, node.right, removed);
			setParent(node.right, node);
			return node;
		} else {
			assert false;
		}
		return node;
	}

	private int childCount(BinaryTreeSet.Node<T> node) {
		int count = 0;
		if(node.left != null) {
			count += 1;
		}
		if(node.right != null) {
			count += 1;
		}	
		return count;	
	}

	/**
	 * Adds a node with the element data to the set.
	 * The tree is then rotated to ensure the random priority given to each node is
	 * in the correct order (higher values higher up the tree).
	 */
	protected BinaryTreeSet.Node<T> addRecursive(T data, BinaryTreeSet.Node<T> node, boolean[] added) {
		if(node == null) {
			added[0] = true;
			if(!isLeaf(node)) {
				this.leaves += 1;
			}
			this.size += 1;
			return new Node<T>(data);
		}
		int comparator = data.compareTo(node.data);
		if(comparator < 0) {
			node.left = addRecursive(data, node.left, added);
			setParent(node.left, node);
			Node<T> leftNode = (Node<T>) node.left;
			Node<T> thisNode = (Node<T>) node;
			if(thisNode.prio > leftNode.prio) {
				node = rotateRight(node);
			}

		} else if(comparator > 0) {
			node.right = addRecursive(data, node.right, added);
			setParent(node.right, node);
			Node<T> rightNode = (Node<T>) node.right;
			Node<T> thisNode = (Node<T>) node;
			if(thisNode.prio > rightNode.prio) {
				node = rotateLeft(node);
			}
		}
		return node;
	}

	@Override
	protected BinaryTreeSet.Node<T> createLeaf(T data, BinaryTreeSet.Node<T> node) {
		Node<T> addedNode = new Node<T>(super.createLeaf(data,node));
		return addedNode;
	}

	/**
	 * Rotates a subtree left about a sub-tree root node.
	 * The node C takes the place of A.
	 * Only nodes A and C are known to be non-null.
	 *   A          C
	 *  / \   =>   / \
	 * B   C      A   E
	 *    / \    / \  
	 *   D   E  B   D
	 */
	protected BinaryTreeSet.Node<T> rotateLeft(BinaryTreeSet.Node<T> node) {
		BinaryTreeSet.Node<T> a = node;
		BinaryTreeSet.Node<T> b = node.left;
		BinaryTreeSet.Node<T> c = node.right;
		setParent(c, a.parent);
		a.right = c.left;
		setParent(a.right, a);
		c.left = a;
		setParent(a, c);
		setParent(b, a);
		return c;
	}

	/**
	 * Rotates a subtree right about a sub-tree root node.
	 * The node B takes the place of A.
	 * Only nodes A and B are known to be non-null.
	 *     A          B
	 *    / \   =>   / \
	 *   B   C      D   A
	 *  / \            / \
	 * D   E          E   C
	 */
	protected BinaryTreeSet.Node<T> rotateRight(BinaryTreeSet.Node<T> node) {
		BinaryTreeSet.Node<T> a = node;
		BinaryTreeSet.Node<T> b = node.left;
		BinaryTreeSet.Node<T> c = node.right;
		setParent(b, a.parent);
		a.left = b.right;
		setParent(a.left, a);
		b.right = a;
		setParent(a, b);
		setParent(c, a);
		return b;
	}

	@Override
	protected String toStringData(BinaryTreeSet.Node<T> node) {
		if(node instanceof TreapSet.Node) {
			Node<T> treapNode = (Node<T>) node;
			int priority = (int) Math.floor(treapNode.prio*10000);
			String parentString = "";
			if(node.parent != null) {
				parentString = "P("+node.parent.data.toString()+")";
			}
			String output = "("+ treapNode.data.toString()+") : "+priority+parentString;
			return output;
		}
		return "("+ node.data.toString()+")";
	}

	public static void main(String[] args) {
		TreapSet<Integer> set = new TreapSet<>();
		set.add(10);
		set.add(9);
		set.add(7);
		set.add(6);
		set.add(3);
		set.add(5);
		set.add(2);
		set.add(1);
		set.add(4);
		set.add(3);
		set.add(2);
		set.add(65);
		System.out.println(set.size());
		set.remove(65);
		System.out.println(set.size());
		System.out.println(set);
	}
}