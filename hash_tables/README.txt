# Hash Tables

#### Flow Id
Using random distinct values between 0 and 10,000 as flow ids.

#### Common methods in all hash tables implementation
1. run(numberOfFlows) - Run multi hash table for numberOfFlows distinct random flow ids
2. randomFlows(numberOfFlows) - Generates numberOfFlows distinct random flows with id
3. print - Prints number of flow in hash table in first line and flow ids for each index in next lines
4. createHashHelpers(numberOfHashFunctions) - Creates random integer values for helping in hash index calculation
5. insert(flow) - Insert flow in table, returns true if successful, otherwise false
6. hashValue(index, flow) - Calculates hash index for flow for index-th hash function

## Multi Hash table
Java File : [MultiHashTable.java](src/MultiHashTable.java)

Output File : [MultiHashTable.txt](outputs/MultiHashTable.txt)

## Cuckoo Hash Table
Java File : [CuckooHashTable.java](src/CuckooHashTable.java)

### Methods
1. move(index, cuckooSteps) - Try to move value current flow present at index with cuckooSteps nesting,
                              returns true if successful, otherwise false

Output File : [CuckooHashTable.txt](outputs/CuckooHashTable.txt)

## D-Left Hash Table
Java File : [DLeftHashTable.java](src/DLeftHashTable.java)

Output File : [DLeftHashTable.txt](outputs/DLeftHashTable.txt)

---
## Output :
Output files are in [outputs](outputs) folder.

### Output Format

- First line denotes number of flows in hash table
- Later lines denotes flow id present in hash table (index as _line_number - 1_). If it is present otherwise zero (0).