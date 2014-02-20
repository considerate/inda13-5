import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.After;

import org.junit.rules.ExpectedException;

import java.util.Iterator;
import java.util.Random;

@RunWith(JUnit4.class)
public class TestTreapSet {
	private TreapSet<Integer> treeSet;
	private TreapSet<String> stringSet;
	@Before
	public void setUp() {
		treeSet = new TreapSet<Integer>();
		stringSet = new TreapSet<String>();
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
	}

	@Test
	public void testAdd2() {
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
		assertEquals(11, treeSet.size());
		assertEquals(true, TestTreapSet.assertOrder(treeSet));
	}

	@Test
	public void testAddRandom() {
		Random random = new Random();
		for(int i = 100; i < 1000; i += random.nextInt(40)) {
			int count = random.nextInt(10);
			TreapSet<Integer> randomSet = new TreapSet<Integer>();
			for(int j = 0; j < count; j++) {
				int value = random.nextInt();
				randomSet.add(value);
			}
			assertEquals(true, TestTreapSet.assertOrder(randomSet));
		}
	}

	@Test
	public void testRemoveRandom() {
		Random random = new Random();
		for(int i = 100; i < 10000; i += random.nextInt(40)) {
			int count = random.nextInt(10);
			TreapSet<Integer> randomSet = new TreapSet<Integer>();
			for(int j = 0; j < count; j++) {
				int value = random.nextInt();
				randomSet.add(value);
			}
			int index = random.nextInt(count+1);
			int currentIndex = 0;
			int val = 0;
			String beforeRemove = randomSet.toString();
			for(int value: randomSet) {
				if(currentIndex == index) {
					val = value;
					randomSet.remove(value);
					break;
				}
				currentIndex++;
			}
			boolean inOrder = TestTreapSet.assertOrder(randomSet);
			if(!inOrder) {
				System.out.println(beforeRemove);
				System.out.println("Removed "+val);
				System.out.println(randomSet);
			}
			assertEquals(true, inOrder);
		}
	}

	@Test
	public void testToString() {
		treeSet.add(9);
		treeSet.add(6);
		treeSet.add(4);	
		assertEquals("[4, 6, 9]", treeSet.toString());
	}

	@Test
	public void testRemove() {
		boolean removed;
	}

	@Test
	public void testContains() {	
		boolean contains;
		stringSet.add("Hello");
		stringSet.add("World!");
		stringSet.add("How are you?");
		stringSet.add("I'm fine, thank you.");
		stringSet.add("Asd");
		assertEquals(true, stringSet.contains("Hello"));
		assertEquals(false, stringSet.contains("you?"));
	}


	private static <T extends Comparable<T>> boolean assertOrder(TreapSet<T> set) {
		Iterator<T> it = set.iterator();
		T a = null;
		T b;
		if(it.hasNext()) {
			a = it.next();
		}
		while(it.hasNext()) {
			b = it.next();
			if(a.compareTo(b) > 0) {
				System.out.println(a.toString() +" > "+ b.toString() );
				return false;
			}
			a = b;
		}
		return true;
	}
}