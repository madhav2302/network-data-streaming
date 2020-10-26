### Project 3



#### Count Min

Java File : [CountMin.java](src/CountMin.java)

Output File : [CountMin.txt](outputs/CountMin.txt)

##### Methods

1. record : Record size of flow in the counters
2. query : Fetch minimum size of all the counters for the flow
3. hashIndex : Hash Index for the counter number index

##### Output Format
(1) the first line of the output: the average error among all flows

(2) the next 100 lines: the flows of the largest estimated sizes, each for a line,
including the flow id, its estimated size and its true size

#### Counter Sketch

Java File : [CounterSketch.java](src/CounterSketch.java)

Output File : [CounterSketch.txt](outputs/CounterSketch.txt)

##### Methods
1. record : Record size of flow in the counter depending on the most significant bit i.e.
            Positive if most significant bit is 1, otherwise negative
2. median : Calculate median of the array
3. query : Fetch median estimated size of all the counters for the flow
4. hashValue : Hash Value of the flow for respective hash function
5. hashIndex : Hash index of the flow

##### Output Format
(1) the first line of the output: the average error among all flows,

(2) the next 100 lines: the flows of the largest estimated sizes, each for a line,
including the flow id, its estimated size and its true size


#### Active Counter

Java File : [ActiveCounter.java](src/ActiveCounter.java)

Output File : [ActiveCounter.txt](outputs/ActiveCounter.txt)

##### Methods
1. run : Runs the active counter n times
2. activeIncrease : Increase n, if overflow, then increase e and right shift n by 1

##### Output Format

Final value of the active counter in decimal