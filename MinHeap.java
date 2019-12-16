class MinHeap {
	public int size;
	public HeapNodeFeatures[] nodeArray;

	private int maxsize = 2000;

	public MinHeap() {
		this.size = 0;
		nodeArray = new HeapNodeFeatures[maxsize];
		// nodeArray[0].executedTime = Integer.MIN_VALUE;
	}

	public void heapify(int index) {
		int leftChild = 2 * index + 1;
		int rightChild = 2 * index + 2;
		// System.out.println("LeftChild: "+nodeArray[leftChild].buildingId + ""
		// +
		// "RightChild: "+nodeArray[rightChild].buildingId);
		int leftChildID, rightChildID, indexID = 0;
		boolean condition = false;
		// to ensure one with less building number and executed time is used
		int smallest = index;
		indexID = nodeArray[index].rbNeighbor.buildingId;
		if (leftChild < size) {
			if (nodeArray[leftChild].executedTime < nodeArray[index].executedTime) {
				smallest = leftChild;
				// condition = true;
			}
			if (nodeArray[leftChild].executedTime == nodeArray[index].executedTime) {
				leftChildID = nodeArray[leftChild].rbNeighbor.buildingId;
				if (indexID < leftChildID) {
					smallest = index;
				} else {
					smallest = leftChild;
				}
			}
		}
		if (rightChild < size) {
			if (nodeArray[rightChild].executedTime < nodeArray[smallest].executedTime) {
				smallest = rightChild;
				// System.out.println("right small");
				// condition = true;
			}
			if (nodeArray[rightChild].executedTime == nodeArray[smallest].executedTime) {
				rightChildID = nodeArray[rightChild].rbNeighbor.buildingId;
				if (nodeArray[smallest].rbNeighbor.buildingId > rightChildID) {
					smallest = rightChild;
				}
			}
		}

		// System.out.println("Smallest node: " +
		// nodeArray[smallest].rbNeighbor.buildingId);
		if (smallest != index
				&& nodeArray[index].executedTime >= nodeArray[smallest].executedTime)
			if (nodeArray[index].executedTime > nodeArray[smallest].executedTime
					|| nodeArray[index].rbNeighbor.buildingId > nodeArray[smallest].rbNeighbor.buildingId) {
				swapNodes(index, smallest);
				heapify(smallest);// till we find the one
			}
	}

	public HeapNodeFeatures deleteMin() {
		int index = 0;
		HeapNodeFeatures minNode = nodeArray[index];
		if (size == 1) {
			nodeArray[index] = null;
			size--;
			return minNode;
		}
		// last node replaced to heapify
		nodeArray[index] = nodeArray[size - 1];
		// System.out.println("index: "+index +
		// " Value: "+nodeArray[index].buildingId);
		nodeArray[size - 1] = null;
		size--;
		heapify(index);
		return minNode;
	}

	private int parentIndex(int i) {
		return (i - 1) / 2;
	}

	private void swapNodes(int i, int j) {
		HeapNodeFeatures temp = nodeArray[i];
		nodeArray[i] = nodeArray[j];
		nodeArray[j] = temp;
	}

	public void nodeInsert(HeapNodeFeatures p) {
		int index = size;
		size++;
		nodeArray[index] = p;
		// index = size;
		// System.out.println("Inserting "+ p.buildingId + " Parent: "+
		// nodeArray[parentIndex(index)].buildingId
		// + " execution so far: "+ nodeArray[index].executedTime);
		// heapify(0);
		// for(int i=0;i<size-1;i++)
		// System.out.print("Node Array: " + nodeArray[i].buildingId + " " +
		// size + " ");
		// System.out.println();
		// while (index != 0&& nodeArray[parentIndex(index)].executedTime ==
		// nodeArray[index].executedTime &&
		// nodeArray[parentIndex(index)].buildingId >
		// nodeArray[index].buildingId){
		// swapNodes(index, parentIndex(index));
		// index = parentIndex(index);
		// }
		while (index != 0
				&& nodeArray[parentIndex(index)].executedTime >= nodeArray[index].executedTime) {
			if (nodeArray[parentIndex(index)].executedTime > nodeArray[index].executedTime
					|| nodeArray[parentIndex(index)].rbNeighbor.buildingId > nodeArray[index].rbNeighbor.buildingId) {
				swapNodes(index, parentIndex(index));
				// heapify(0);
				index = parentIndex(index);
			} else {
				break;
			}
			// System.out.println("Node at index: " +
			// nodeArray[index].buildingId);
		}
	}

}
