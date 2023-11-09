package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		int i = locations.length - 1;
		int j = busStops.length - 1;
		int k = trainStations.length - 1;
		TNode walkNode = null;
		TNode busNode = null;
		TNode trainNode = null;
		do {
			walkNode = new TNode(locations[i], walkNode, null);
			if (j >= 0 && busStops[j] == locations[i]) {
				busNode = new TNode(busStops[j], busNode, walkNode);
				if (k >= 0 && trainStations[k] == busStops[j]) {
					trainNode = new TNode(trainStations[k], trainNode, busNode);
					k--;
				}
				j--;
			}
			i--;
		} while (i >= 0);
		walkNode = new TNode(0, walkNode, null);
		busNode = new TNode(0, busNode, walkNode);
		trainNode = new TNode(0, trainNode, busNode);
		trainZero = trainNode;
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		TNode current = trainZero;
		TNode prev = null;
		while (current != null) {
			if (station == current.getLocation()) {
				prev.setNext(current.getNext());
				return;
			}
			prev = current;
			current = current.getNext();
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
	    TNode currentWalk = trainZero.getDown().getDown();
		TNode prevBus = trainZero.getDown();
		while (currentWalk != null) {
			if (busStop == currentWalk.getLocation()) {
				if (prevBus.getNext() != null) {
					if (busStop != prevBus.getNext().getLocation()) {
						TNode n = new TNode(busStop, prevBus.getNext(), currentWalk);
						prevBus.setNext(n);
					}
				}
				else {
					TNode n = new TNode(busStop, null, currentWalk);
					prevBus.setNext(n);
				}
				return;
			}
			if (prevBus.getNext() != null && currentWalk.getLocation() == prevBus.getNext().getLocation()) {
				prevBus = prevBus.getNext();
			}
			currentWalk = currentWalk.getNext();
		}

	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		TNode current = trainZero;
		ArrayList<TNode> nodeList = new ArrayList<TNode>();
		while(current.getLocation() <= destination) {
			nodeList.add(current);
			if (current.getNext() != null) {
				if (current.getNext().getLocation() > destination) {
					if (current.getDown() != null) {
						current = current.getDown();
					}
					else {
						return nodeList;
					}
				}
				else {
					current = current.getNext();
				}
			}
			else {
				if (current.getDown() == null) {
					return nodeList;
				}
				else {
					current = current.getDown();
				}
			}
			
		}
	    return null;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {
		TNode currentWalk = trainZero.getDown().getDown().getNext();
		TNode currentBus = trainZero.getDown().getNext();
		TNode currentTrain = trainZero.getNext();
		TNode newWalk = new TNode(0, null, null);
		TNode newBus = new TNode(0, null, newWalk);
		TNode newTrain = new TNode(0, null, newBus);
		TNode tzNew = newTrain;
		while (currentWalk != null) {
			TNode prev = newWalk;
			newWalk = new TNode(currentWalk.getLocation(), null, null);
			prev.setNext(newWalk);
			if (currentBus != null && currentBus.getLocation() == currentWalk.getLocation()) {
				prev = newBus;
				newBus = new TNode(currentBus.getLocation(), null, newWalk);
				prev.setNext(newBus);
				if (currentTrain != null && currentTrain.getLocation() == currentBus.getLocation()) {
					prev = newTrain;
					newTrain = new TNode(currentTrain.getLocation(), null, newBus);
					prev.setNext(newTrain);
					currentTrain = currentTrain.getNext();
				}
				currentBus = currentBus.getNext();
			}
			currentWalk = currentWalk.getNext();
		}
	    return tzNew;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		TNode walk = trainZero.getDown().getDown();
		TNode scoot = new TNode(0, null, walk);
		TNode bus = trainZero.getDown();
		bus.setDown(scoot);
		bus = bus.getNext();
		int i = 0;
		while (walk != null) {
			if (i < scooterStops.length && scooterStops[i] == walk.getLocation()) {
				TNode prev = scoot;
				scoot = new TNode(walk.getLocation(), null, walk);
				prev.setNext(scoot);
				if (bus != null && bus.getLocation() == scoot.getLocation()) {
					bus.setDown(scoot);
					bus = bus.getNext();
				}
				i++;
			}
			walk = walk.getNext();
		}
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
