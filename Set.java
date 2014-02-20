interface Set<T> {
	/**
	 * Adds an element with of a given type T to the set.
	 * If the element already exists in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 */
	public boolean add(T data);
	/**
	 * Remove the element with the a given type T from the set.
	 * If the element does not already exist in the set <code>false</code> is
	 * returned.
	 * Required expected time: O(log n) 
	 */
	public boolean remove(T data);
	/**
	 * Checks if an element of a given type T exists in the set.
	 * Required expected time: O(log n) 
	 */
	public boolean contains(T data);
}