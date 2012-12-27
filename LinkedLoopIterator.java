///////////////////////////////////////////////////////////////////////////////
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoopIterator.java
// Semester:         Fall 2011
//
// Author:           Peter Collins pmcollins2@wisc.edu
// CS Login:         pcollins
// Lecturer's Name:  Beck Hasti
// Lab Section:      NA
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Iterator;

/**
 * An iterator to manipulate specifically the linked loop implementation in this
 * package.
 * 
 * <p>
 * Bugs: none known
 * 
 * @author Peter Collins
 */
public class LinkedLoopIterator<E> implements Iterator<E> {
	/** A private doubly-linked node linked into the image loop */
	private DblListnode<E> currNode;
	/** Number of nodes in the loop */
	private int numNodes;
	/** Current position based on the total number of nodes */
	private int currPos;

	/**
	 * Package access constructor that constructs new linked loop iterator
	 * 
	 * @param currNode
	 *            current node in the linked loop
	 * @param numNodes
	 *            total number of nodes in the linked loop
	 */
	LinkedLoopIterator(DblListnode<E> currNode, int numNodes) {
		// instantiate the variables
		this.currNode = currNode;
		this.numNodes = numNodes;
		this.currPos = 0;
	}

	/**
	 * Determines if we have more nodes to iterate over
	 * 
	 * @return true if there are more, or false if we have gone through the
	 *         entire loop
	 */
	@Override
	public boolean hasNext() {
		return this.currPos < this.numNodes;
	}

	/**
	 * Increments the iterator and returns the data of the next node
	 * 
	 * @return the data of the next node
	 */
	@Override
	public E next() {
		E data = currNode.getData();
		currNode = currNode.getNext();
		currPos++;
		return data;
	}

	/**
	 * Unimplemented remove method
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}