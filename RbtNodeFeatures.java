class RbtNodeFeatures {
	public static final RbtNodeFeatures sentinel = new RbtNodeFeatures(-999, -1);
	
	public HeapNodeFeatures hpNeighbor;
	public int buildingId;
	public int totalTime;
	public boolean color;
	public RbtNodeFeatures left = sentinel;
	public RbtNodeFeatures right = sentinel;
	public RbtNodeFeatures parent = sentinel;
	
     //constructor with input as execution time, object reference, building id, total time for execution
	public RbtNodeFeatures(HeapNodeFeatures temp,int id,int ttime){
		this.hpNeighbor = temp;
		this.buildingId = id;
		this.totalTime = ttime;
	}
	
 //intial color as false
	public RbtNodeFeatures(int id,int ttime){
		if(ttime == -1)
			this.color = false;
		this.buildingId = id;
		this.totalTime = ttime;
	}
}
