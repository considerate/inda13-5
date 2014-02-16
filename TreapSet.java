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

	@Override
	public boolean add(T data) {
		//Mutable boolean
		boolean[] added = {false};
		this.root = addRecursive(data, this.root, added);
		this.height = calculateHeight(this.root);
		return added[0];
	}

	@Override
	public boolean remove(T data) {
		//Mutable boolean
		boolean[] removed = {false};
		//this.root = removeRecursive(data, this.root, removed);
		this.height = calculateHeight(this.root);
		return removed[0];
	}


	protected BinaryTreeSet.Node<T> removeRecursive(T data, BinaryTreeSet.Node<T> node, boolean[] removed) {
		if(node == null) {
			removed[0] = false;
			return node;
		}

		int comparator = data.compareTo(node.data);
		if(comparator == 0) {
			int childCount = 0;
			removed[0] = true;
			this.size -= 1;
			if(node.left != null) {
				childCount += 1;
			}
			if(node.right != null) {
				childCount += 1;
			}
			if(childCount == 0) {
				return null;
			}
			if(childCount == 1) {
				if(node.left != null) {
					return node.left;
				} else {
					return node.right;
				}
			}
			if(childCount == 2) {
				double leftPrio = ((Node<T>) node.left).prio;
				double rightPrio = ((Node<T>) node.right).prio;
				if(leftPrio > rightPrio) {
					node.left.right = node.right;
					return node.left;
				} else {
					node.right.left = node.left;
					return node.right;
				}
			}
		} else if(comparator < 0) {
			node.left = removeRecursive(data, node.left, removed);
			return node;
		} else if(comparator > 0) {
			node.right = removeRecursive(data, node.right, removed);
			return node;
		} else {
			assert false;
		}
		return node;
	}

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
			Node<T> leftNode = (Node<T>) node.left;
			Node<T> thisNode = (Node<T>) node;
			if(thisNode.prio > leftNode.prio) {
				node = rotateRight(node);
			}
		} else if(comparator > 0) {
			node.right = addRecursive(data, node.right, added);
			Node<T> rightNode = (Node<T>) node.right;
			Node<T> thisNode = (Node<T>) node;
			if(thisNode.prio > rightNode.prio) {
				node = rotateLeft(node);
			}
		}
		return node;
	}

	@Override
	protected BinaryTreeSet.Node<T> addLeaf(T data, BinaryTreeSet.Node<T> node) {
		Node<T> addedNode = new Node<T>(super.addLeaf(data,node));
		return addedNode;
	}

	/**
	 * Rotates a subtree left about a sub-tree root node.
	 *   A          C
	 *  / \   =>   / \
	 * B   C      A   B
	 */
	protected BinaryTreeSet.Node<T> rotateLeft(BinaryTreeSet.Node<T> node) {
		BinaryTreeSet.Node<T> c = node.right;
		BinaryTreeSet.Node<T> a = node;
		BinaryTreeSet.Node<T> b = node.left;
		a.right = b;
		c.left = a;
		return c;
	}

	/**
	 * Rotates a subtree right about a sub-tree root node.
	 *   A          B
	 *  / \   =>   / \
	 * B   C      C   A
	 */
	protected BinaryTreeSet.Node<T> rotateRight(BinaryTreeSet.Node<T> node) {
		BinaryTreeSet.Node<T> a = node;
		BinaryTreeSet.Node<T> b = node.left;
		BinaryTreeSet.Node<T> c = node.right;
		a.left = c;
		b.right = a;
		return b;
	}
	@Override
	protected String toStringData(BinaryTreeSet.Node<T> node) {
		if(node instanceof TreapSet.Node) {
			Node<T> treapNode = (Node<T>) node;
			int priority = (int) Math.floor(treapNode.prio*10000);
			return "("+ treapNode.data.toString()+") : "+priority+"";
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
		set.remove(65);
		System.out.println(set.size());
		System.out.println(set);
	}
}