# Heap_RedBlackTree

Wayne Enterprises is developing a new city. They are constructing many buildings and plan to use software to keep track of all buildings under construction in this new city. A building record has the following fields:
buildingNum: unique integer identifier for each building.
executed_time: total number of days spent so far on this building.
total_time: the total number of days needed to complete the construction of the building.

A min heap is used to store (buildingNums,executed_time,total_time) triplets ordered by executed_time. An RBT is used to store (buildingNums,executed_time,total_time) triplets ordered by buildingNum.
