import java.util.List;

class RedBlackTree {
	public RbtNodeFeatures root;
	public RbtNodeFeatures sentil;

	public RedBlackTree() {
		sentil = RbtNodeFeatures.sentinel;
		root = sentil;
		root.left = sentil;
		root.right = sentil;
	}

	//aVL LR rotation
	private RbtNodeFeatures avlRRRoatation(RbtNodeFeatures node) {
		RbtNodeFeatures actualNode = node.right; //node causing error
		RbtNodeFeatures nodeLeft = actualNode.left; //can be null
		//leftchild of node as the node
		node.right = nodeLeft;
		//parent node of earlier node now its parent
		if (nodeLeft != sentil)
			nodeLeft.parent = node;
		//grandparent as new parent
		actualNode.parent = node.parent;
		//case when gp is not present, rephrasing it
		if (node.parent == sentil)
			root = actualNode;
		else if (node == node.parent.left)
			node.parent.left = actualNode;
		else
			node.parent.right = actualNode;
		actualNode.left = node;
		node.parent = actualNode;
		return actualNode;
	}

	private void flipColor(RbtNodeFeatures parentNode, RbtNodeFeatures grandParent) {
        //black - false, red- true
        boolean swapColor = parentNode.color;
        parentNode.color = grandParent.color;
        grandParent.color = swapColor;
    }
	
    //insertion of node other than root
	private void notRootInsert(RbtNodeFeatures root, RbtNodeFeatures node) {
//		System.out.println("Root "+ root.buildingId);
//		System.out.println("Node "+ node.buildingId);
        //which part of the tree
		if (node.buildingId < root.buildingId) {
			if (root.left == sentil) {
				root.left = node;
				node.parent = root;
			} else {
				notRootInsert(root.left, node);
			}
		} else {
			if (root.right == sentil) {
				root.right = node;
				node.parent = root;
			} else {
				notRootInsert(root.right, node);
			}
		}
	}
	public void nodeInsert(RbtNodeFeatures node) {
		// true for red color node
		node.color = true;
		//first node
		if (root == sentil || root.buildingId == node.buildingId) {
			root = node;
			root.color = false;
			root.parent = sentil;
			return;
		}
		// if node inserted is not root
		notRootInsert(root, node);
		// #fix the tree if two red node back to back
		insertReBalancing(node);
	}

    //clockwise rotation
	private RbtNodeFeatures avlLLRotation(RbtNodeFeatures node){
		RbtNodeFeatures actualNode = node.left;
		RbtNodeFeatures nodeRight = actualNode.right;
        node.left = nodeRight;
        if(nodeRight != sentil)
            nodeRight.parent = node;
        actualNode.parent = node.parent;
        if (node.parent == sentil)
            root = actualNode;
        else if (node == node.parent.left)
            node.parent.left = actualNode;
        else
            node.parent.right = actualNode;
        actualNode.right = node;
        node.parent = actualNode;
        return actualNode;
    }
	
	private void insertReBalancing(RbtNodeFeatures node) {
		RbtNodeFeatures parentNode = sentil;
		RbtNodeFeatures grandParentNode = sentil;
		// root node is black
		if (node.buildingId == root.buildingId) {
			node.color = false;
			return;
		}
		while (node.buildingId != root.buildingId && node.color == true
				&& node.parent.color == true) {
			parentNode = node.parent;
			grandParentNode = parentNode.parent;
			// parent less than grandparent
			if (parentNode == grandParentNode.left) {
				RbtNodeFeatures rightChildGP = grandParentNode.right;
				// Case when XYr
				if (rightChildGP != sentil && rightChildGP.color == true) {
					grandParentNode.color = true;
					parentNode.color = false;
					rightChildGP.color = false;
					// repeat till root node is reached
					node = grandParentNode;
				} else {
					// right child of grandparent is black or null==black
					// will have to fix with respect to the parent node LRb Case
					if (parentNode.right == node) {
						parentNode = avlRRRoatation(parentNode);
						node = parentNode.left;
					}
					//common LLb Case
					avlLLRotation(grandParentNode);
					flipColor(parentNode, grandParentNode);
					node = parentNode;
				}
			}
			else if (parentNode == grandParentNode.right) {
				RbtNodeFeatures leftChildGP = grandParentNode.left;
				// Case when sibling of parent is red
				if (leftChildGP != sentil && leftChildGP.color == true) {
					grandParentNode.color = true;
					parentNode.color = false;
					leftChildGP.color = false;
					node = grandParentNode;
				} else {
					// RLb Case
					if (parentNode.left == node) {
						parentNode = avlLLRotation(parentNode);
						node = parentNode.right;
					}
					//common RRb Case
					avlRRRoatation(grandParentNode);
					flipColor(parentNode, grandParentNode);
					node = parentNode;
				}
			}
		}
		root.color = false;
	}

	private void deleteReBalancing(RbtNodeFeatures node){
//		System.out.println("Deleting node: "+node.buildingId);
		while(node!=root && node.color == false){
            if(node == node.parent.left){
                //v holds the ppy's right child
                RbtNodeFeatures tempNode = node.parent.right;

                //if v is red, left rotation is required
                if(tempNode.color == true){
                    tempNode.color = false;
                    node.parent.color = true;
                    avlRRRoatation(node.parent);
                    tempNode = node.parent.right;
                }
                //if v's both children are black, recolor
                if(tempNode.left.color == false && tempNode.right.color == false){
                    tempNode.color = true;
                    node = node.parent;
                    continue;
                }
                //if only right child is black, recolor and right rotate
                else if(tempNode.right.color == false){
                    tempNode.left.color = false;
                    tempNode.color = true;
                    avlLLRotation(tempNode);
                    tempNode = node.parent.right;
                }
                //if v's right child is red, recolor and left rotate
                if(tempNode.right.color == true){
                    tempNode.color = node.parent.color;
                    node.parent.color = false;
                    tempNode.right.color = false;
                    avlRRRoatation(node.parent);
                    node = root;
                }
            } 
            //Rb/Rr
            else {
                RbtNodeFeatures tempNode = node.parent.left;

                //Rb1
                if(tempNode.color == true){
                    tempNode.color = false;
                    node.parent.color = true;
                    avlLLRotation(node.parent);
                    tempNode = node.parent.left;
                }
                //replace two red 
                if(tempNode.right.color == false && tempNode.left.color == false){
                    tempNode.color = true;
                    node = node.parent;
                    continue;
                }
                //one child black
                else if(tempNode.left.color == false){
                    tempNode.right.color = false;
                    tempNode.color = true;
                    avlRRRoatation(tempNode);
                    tempNode = node.parent.left;
                }
                if(tempNode.left.color == true){
                    tempNode.color = node.parent.color;
                    node.parent.color = false;
                    tempNode.left.color = false;
                    avlLLRotation(node.parent);
                    node = root;
                }
            }
        }
        node.color = false;
    }
	
	public List<RbtNodeFeatures> intervalSearch(RbtNodeFeatures root, List<RbtNodeFeatures> list, int start, int end) {
        if (root == sentil)
            return list;
        if (start < root.buildingId)
        	intervalSearch(root.left, list, start, end);
        if (start <= root.buildingId && end >= root.buildingId)
            list.add(root);
        if (end > root.buildingId)
        	intervalSearch(root.right, list, start, end);
        return list;
    }
	
	//O(lgn)
	public RbtNodeFeatures search(RbtNodeFeatures root, int buildId) {
		if (root == sentil) {
			return null;
		}
		if (root.buildingId == buildId) {
			return root;
		} else if (buildId > root.buildingId) {
			return search(root.right, buildId);
		} else {
			return search(root.left, buildId);
		}
	}
	
public boolean delete(int key){
        RbtNodeFeatures deleteNode = search(root,key);
        if (deleteNode == null){
            return false;
        }
        RbtNodeFeatures temp = deleteNode;
        RbtNodeFeatures fixture;
        boolean presentColor = deleteNode.color;
        //deleting node decreases height at times
        if (deleteNode.left == sentil){
        	fixture = deleteNode.right;
            decreaseHeight(deleteNode, deleteNode.right);}
        else if (deleteNode.right == sentil){
        	fixture = deleteNode.left;
            decreaseHeight(deleteNode, deleteNode.left);}
        else {
        	temp = deleteNode.right;
        	while (temp.left != sentil)
                temp = temp.left;
            presentColor = temp.color;
            fixture = temp.right;
            //restructuring
            if (temp.parent != deleteNode) {
            	decreaseHeight(temp, temp.right);
                temp.right = deleteNode.right;
                temp.right.parent = temp;
            }else{
            	fixture.parent = temp;
            }
            decreaseHeight(deleteNode, temp);
            temp.left = deleteNode.left;
            temp.left.parent = temp;
            temp.color = deleteNode.color;
        }
        //fix when black node deleted
        if (presentColor == false) {
            deleteReBalancing(fixture);
        }
        return true;
    }
	
    //if the end of the tree leaves has a deletion of its left or right child
	 private void decreaseHeight(RbtNodeFeatures node, RbtNodeFeatures childNode){
		 //check if it was a root node
//		 System.out.print(node.parent.right);
	        if (node.parent == sentil)
	            root = childNode;
	        else if(node == node.parent.right)
	        	node.parent.right = childNode;
	        else 
	        	node.parent.left = childNode;
	        childNode.parent = node.parent;
	    }

}
