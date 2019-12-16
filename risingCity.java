import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class risingCity {
	public int globalCounter = 0;
	private int timeInterval = 0;
	private int buildingCompleteTime = 0;
	private RedBlackTree redTree = new RedBlackTree();
	private MinHeap minHeap = new MinHeap();
	private HeapNodeFeatures presentNode = null;
	private FileWriter writer;
	public int inputTime;
	public boolean status = false;
	public String operation;

	public int incrementCounter() {
		if (presentNode != null) {
			presentNode.executedTime += 1;
		}
		globalCounter = globalCounter + 1;
		return globalCounter;
	}

	public void insertBuilding(int id, int totaltime) {
		RbtNodeFeatures rbNodeFeatures = new RbtNodeFeatures(id, totaltime);
		HeapNodeFeatures heapNodeFeatures = new HeapNodeFeatures(0,
				rbNodeFeatures, id, totaltime);
		rbNodeFeatures.hpNeighbor = heapNodeFeatures;
		redTree.nodeInsert(rbNodeFeatures);
		minHeap.nodeInsert(heapNodeFeatures);
		// System.out.println("Insert");
	}

	public int getCounter() {
		return globalCounter;
	}

	private void continueBuilding(String[] range) throws IOException {
		status = false;
		// System.out.println("hi");
		if (presentNode != null) {
			// System.out.println("Execution time of "
			// + presentNode.rbNeighbor.buildingId + " "
			// + presentNode.executedTime +" " + getCounter());
			// System.out.println("Time Interval: "+timeInterval);
			if (buildingCompleteTime <= timeInterval) {
				if (globalCounter == buildingCompleteTime) {
					// incrementCounter();
					if (globalCounter == inputTime - 1) {
						if (operation.equals("PrintBuilding"))
							printBuilding(range);
						// status = true;
					}
					// System.out.println("GlobalCounter: "+getCounter() +
					// " BuildingComplete: "+buildingCompleteTime);
					writer.write("(" + presentNode.rbNeighbor.buildingId + ","
							+ getCounter() + ")\n");
					redTree.delete(presentNode.rbNeighbor.buildingId);
					presentNode = null;
					timeInterval = 0;
					buildingCompleteTime = 0;
					continueBuilding(range);
					status = true;
				}
			} else {
				if (globalCounter == timeInterval) {
					// time limit over
					// System.out.println("Present node id and executed so far and tota : "
					// +
					// presentNode.buildingId + " "+ presentNode.totalTime + " "
					// +
					// presentNode.executedTime);
					minHeap.nodeInsert(presentNode);
					// minHeap.heapify(0);
					presentNode = null;
					timeInterval = 0;
					buildingCompleteTime = 0;
					continueBuilding(range);
					// status = false;
				}
			}
		} else {
			if (minHeap.size == 0) {
				// status = false;
				return;
			} else {
				presentNode = minHeap.deleteMin();
				// System.out.println(presentNode.buildingId);
				timeInterval = globalCounter + 5;
				buildingCompleteTime = globalCounter
						+ presentNode.rbNeighbor.totalTime
						- presentNode.executedTime;
				// if(buildingCompleteTime <= timeInterval)
				// System.out.println(timeInterval + " " +
				// buildingCompleteTime);
				// status = false;
			}
		}
	}

	public void printBuilding(String[] range) throws IOException {
		// if(globalCounter == inputTime)
		// return;
		// System.out.println("Length: " +range.length);
		if (range.length == 1) {
			RbtNodeFeatures node = redTree.search(redTree.root,
					Integer.parseInt(range[0]));
			if (node == null)
				writer.write("(0,0,0)\n");
			else {
				if (node.buildingId == presentNode.rbNeighbor.buildingId) {
					// System.out.println("output write");
					writer.write("(" + node.buildingId + ","
							+ presentNode.executedTime + "," + node.totalTime
							+ ")\n");
				} else {
					writer.write("(" + node.buildingId + ","
							+ node.hpNeighbor.executedTime + ","
							+ node.totalTime + ")\n");
				}
			}
		} else if (range.length == 2) {
			List<RbtNodeFeatures> list1 = new LinkedList<RbtNodeFeatures>();
			List<RbtNodeFeatures> listNodes = redTree.intervalSearch(
					redTree.root, list1, Integer.parseInt(range[0]),
					Integer.parseInt(range[1]));
			if (listNodes.isEmpty())
				writer.write("(0,0,0)\n");
			else {
				StringBuilder stringWriter = new StringBuilder();           
                int index = 0;
				for (RbtNodeFeatures node : listNodes) {
                    if(index != listNodes.size()-1){
					    stringWriter.append("(" + node.buildingId + ","
							    + node.hpNeighbor.executedTime + ","
							    + node.totalTime + ")" + ",");
                    }else{
                        stringWriter.append("(" + node.buildingId + ","
							    + node.hpNeighbor.executedTime + ","
							    + node.totalTime + ")");
                    }
                    index++;
				}
				stringWriter.append("\n");
				// System.out.println("writer: "+stringWriter.toString());
				writer.write(stringWriter.toString());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		risingCity objClass = new risingCity();
		BufferedReader reader = null;
		String inputString;
		String[] range = {};
        String inputFile = args[0];
		objClass.writer = new FileWriter(
				"output.txt");
		try {
			reader = new BufferedReader(
					new FileReader(
							inputFile));
			while ((inputString = reader.readLine()) != null) {
				Pattern toMatchWith = Pattern
						.compile("(^\\d+): ([a-zA-Z]+)\\((.+)\\)");
				Matcher matcher = toMatchWith.matcher(inputString);
				if (matcher.find()) {
					range = matcher.group(3).split(",");
					// time of data in
					objClass.operation = matcher.group(2);
					objClass.inputTime = Integer.parseInt(matcher.group(1));
					while (objClass.inputTime != objClass.getCounter()) {
						objClass.continueBuilding(range);
						objClass.incrementCounter();
					}
					// System.out.println(objClass.getCounter());
					switch (matcher.group(2)) {
					case "Insert": {
						// insert in heap
						objClass.insertBuilding(Integer.parseInt(range[0]),
								Integer.parseInt(range[1]));
						break;
					}
					case "PrintBuilding": {
						// print from rbt
						// System.out.println(objClass.getCounter());
						if (objClass.status == false)
							objClass.printBuilding(range);
						break;
					}
					}
				}
				objClass.continueBuilding(range);
				objClass.incrementCounter();
			}
			while (objClass.presentNode != null) {
				objClass.continueBuilding(range);
				objClass.incrementCounter();
			}
		} catch (IOException e) {
			System.out.println("IO Exception");
		} finally {
			try {
				reader.close();
				objClass.writer.close();
			} catch (IOException ex) {
				System.out.println("IO Exception");
			}
		}
	}

}
