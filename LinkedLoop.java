///////////////////////////////////////////////////////////////////////////////
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoop.java
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
 * A linked loop implementation which uses a doubly-linked chain of nodes.
 * 
 * <p>
 * Bugs: none known
 * 
 * @author Peter Collins
 */
public class LinkedLoop<E> implements LoopADT<E>, Iterable<E> {
	/**
	 * A private doubly-linked node containing the data of the current object
	 * represented
	 */
	private DblListnode<E> currNode;
	/**
	 * A private representation of the total number of nodes currently in the
	 * loop
	 */
	private int numNodes;

	/**
	 * Constructs an linked loop
	 */
	public LinkedLoop() {
		currNode = null;
		numNodes = 0;
	}

	/**
	 * Add a new object to the linked loop by prepending it to the list
	 * 
	 * @param object
	 *            to add to the loop
	 */
	@Override
	public void add(E item) {
		if (currNode == null) {
			// create the first node if there isn't one
			currNode = new DblListnode<E>(item);
			// link it to itself
			currNode.setNext(currNode);
			currNode.setPrev(currNode);
		} else {
			// create a new node to hold given object as data
			DblListnode<E> newNode = new DblListnode<E>(item);
			// link it to siblings
			newNode.setNext(currNode);
			newNode.setPrev(currNode.getPrev());
			// link it siblings to itself
			currNode.getPrev().setNext(newNode);
			currNode.setPrev(newNode);
			// make the new node current
			currNode = newNode;
		}
		// increment total number of nodes
		numNodes++;
	}

	/**
	 * Return the current object from the loop
	 * 
	 * @return current object
	 */
	@Override
	public E getCurrent() throws EmptyLoopException {
		// throw exception if empty
		if (currNode == null) {
			throw new EmptyLoopException();
		}
		return currNode.getData();
	}

	/**
	 * Return the current object from the loop and remove. Make the one after
	 * current.
	 * 
	 * @return removed current object
	 */
	@Override
	public E removeCurrent() throws EmptyLoopException {
		// throw exception if empty
		if (currNode == null) {
			throw new EmptyLoopException();
		}
		// store node to return
		DblListnode<E> returnNode = currNode;
		if (numNodes == 1) {
			// set to current node to null if the loop is empty
			currNode = null;
		} else {
			// link next node with other nodes if there are more nodes in the
			// loop
			currNode = returnNode.getNext();
			currNode.setPrev(returnNode.getPrev());
			returnNode.getPrev().setNext(currNode);
		}
		numNodes--; // decrement the total number of nodes in the loop
		return returnNode.getData(); // return object of removed node
	}

	/**
	 * Make the next node the current node
	 */
	@Override
	public void next() {
		currNode = currNode.getNext();
	}

	/**
	 * Make the previous node the current node
	 */
	@Override
	public void previous() {
		currNode = currNode.getPrev();
	}

	/**
	 * Determine if the loop is empty
	 * 
	 * @return true if the loop is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		if (numNodes == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Return the total number objects in the loop
	 * 
	 * @return total number of objects in the loop
	 */
	@Override
	public int size() {
		return numNodes;
	}

	/**
	 * Return a iterator to iterate over the loop without affect the current
	 * node
	 * 
	 * @return iterator to iterate over the loop
	 */
	@Override
	public Iterator<E> iterator() {
		return new LinkedLoopIterator<E>(currNode, numNodes);
	}

}
