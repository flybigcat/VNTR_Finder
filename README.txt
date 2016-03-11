Author: Huijuan (Ann) Huang
Time: 03/11/2016

What Is This?
-------------

This is a project find all the VNTR from input string

Language and Tools:
algorithm: 
(IDE: Eclipse)

Background
-----------------------
1. what is VNTR:
wiki:
https://en.wikipedia.org/wiki/Variable_number_tandem_repeat 

2. algorithms
1). brute force one using 3 for loops with time O(n^3), space O(n); not efficient
2). Some other algorithms like, time O(n^2) (dynamic programming), time O(nlogn) suffix tree, which are Ok, but the most optimized one is linear time 
There are lots of articles/reference talking about many different ways chasing efficient approach:

In "Simple and flexible detection of contiguous repeats using a suffix tree", Stoye et. al made use of suffix tree and devise a searching algorithm running through the suffix tree to detect all tandem repeat. The time complexity is O(nlog(n)).

In "Computation of Squares in a String", Kosaraju used Winner's method to construct a suffix tree which add suffix one after another. In this process, the author put forward a recursive algorithm to detect tandem repeat in each round of adding a suffix. The time complexity is O(n)

In "Linear time algorithms for finding and representing all the tandem repeats in a string", Gusfield et. al made use of suffix tree and combined several algorithm including Crochemore’s algorithm for detecting squarefree suffix tree, Lempel-Ziv (LZ) decomposition,  ongest common extension queries to detect tandem repeat. The time complexity is O(n).

3). Here, an interesting and very efficient method written by X. Guan is implemented by Java with time O(n), space O(n)

The algorithm is use hashMap with unique key (formula in the article for each 5 length string)

literature reference: 
A Fast Look-Up Algorithm for Detecting Repetitive DNA SequencesX. Guan and E. C. UberbacherComputer Science and Mathematics DivisionOak Ridge National Laboratory

sudo code:
doc/ folder







