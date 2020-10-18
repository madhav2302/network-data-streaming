# Bloom Filters

#### Element Id
Using random distinct values.

#### Common methods in all bloom filter implementation
1. encode - Sets bits to 1 for hash index for element or increase counter related to bloom filter
2. lookup - Look ups if element is available in bloom filter
3. createHashHelpers - Creates random integer values for helping in hash index calculation
4. randomElements - Generates numberOfElemetns distinct random elements as integer value

---

## Bloom Filter

Java File : [BloomFilter.java](src/BloomFilter.java)

Output File : [BloomFilter.txt](outputs/BloomFilter.txt)

---

## Counting Bloom Filter

Java File : [CountingBloomFilter.java](src/CountingBloomFilter.java)

Output File : [CountingBloomFilter.txt](outputs/CountingBloomFilter.txt)

---

## Coded Bloom Filter

Java File : [CodedBloomFilter.java](src/CodedBloomFilter.java)

Output File : [CodedBloomFilter.txt](outputs/CodedBloomFilter.txt)
