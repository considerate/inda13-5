import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.After;

import org.junit.rules.ExpectedException;

@RunWith(JUnit4.class)
public class TestBinaryTreeSet {
	private BinaryTreeSet<Integer> treeSet;

	@Before
	public void setUp() {
		treeSet = new BinaryTreeSet<Integer>();
	}

	@After
	public void tearDown() {
		treeSet = null;
	}

	@Test
	public void testAdd() {
		boolean added;
		added = treeSet.add(2);
		assertEquals(true, added);
		System.out.println(treeSet.toString());
	}

	@Test
	public void testRemove() {
		boolean removed;

	}

	@Test
	public void testContains() {	
		boolean contains;

	}
}