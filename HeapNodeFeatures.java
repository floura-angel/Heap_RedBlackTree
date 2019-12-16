class HeapNodeFeatures {
	public int executedTime;
	public RbtNodeFeatures rbNeighbor;
	public int buildingId;
	public int totalTime;
	
    //constructor with input as execution time, object reference, building id, total time for execution
	public HeapNodeFeatures(int extime,RbtNodeFeatures temp,int id,int ttime){
		this.executedTime = extime;
		this.rbNeighbor = temp;
		this.buildingId = id;
		this.totalTime = ttime;
	}
}
