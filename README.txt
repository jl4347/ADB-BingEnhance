Name: Mingfei Ge(mg3534), Jialun Liu(jl4347)

Bing Search Account Key: W5lSDB4TuFJk9OTI55RhqDa74QLT+NphlYDDUlJrJSY

=================================================================================
How to run our program
=================================================================================
change directory to ADB
execute run.sh

cd ADB/
./run.sh <bing account key> <precision> <query>


=================================================================================
List of files we submit
=================================================================================
Extra jar we used in our program:
./bin/json.org.jar
./bin/org-apache-commons-codec.jar

Source code:
./src/WeightCalculation/CreateVector.java
./src/testBing/FeedBack.java
./src/testBing/QueryExpand.java
./src/util/Query.java
./src/util/Doc.java
./src/util/BingData.java


=================================================================================
Internal design of our project
=================================================================================
On the top level, this java program contains three packages: testBing, util and WeightCalculation. "testBing" package
contains the main method to execute the program, and "QueryExpand" class for query expansion; "util" package has "BingData"
to interact with Bing API, "Doc" and "Query" class to store the query and documents information; "WeightCalculation" package
contains "CreateVector" to compute query and document vector.

Feedback.java contains the main function to start running the program. It will first verify the user input before
start the query. Then, it will catch returned JSON data from BingSearch and parse it for us to analyze the data and 
store the documents as a list of DOC objects. During parsing, it will first get rid of all the punctuations since
they don't contribute to the precision of the search.

The program would then print the articles rettrived from the Bing Search and ask user to judge whether the document
is relevant or not. After the human relevance is done, the program then send the list of DOC ojects, tokenized query
array and list of tokenized documents to the CalcWeight method in the CreateVector class to compute the vobulary list,
document and query vectors for query expansion later on.

After we computed the weight vectors by using the "tf-idf", the program sends the query and document weight vector to 
computeNewWeight method in the QueryExpand class. This method is using the Rocchio's algorithm to compute the optimal 
query vector.

The last step of the program is to augment the current query based on the optimal query we computed from the previous
step. It first builds a hashmap to contain the index of the current query tokens. Then, it will find two tokens that have
the greatest weight which are not the one of the current query tokens. Finally, the program finds the next two words and
build the new query and send it to the Bing Search API for the next round until it reaches the user specfied precision.


=================================================================================
Query Modification Method
=================================================================================
As mentioned in the previous section, the CalcWeight method computes the weight vectors of query and documents based on the "tf-idf" method and then compute the optimal query vector using the Rocchio's algorithm. Finally, we just need to pick the two
entries of the vector which has the highest weight. Because that means they are the most relevant words.


=================================================================================
Addional effort to Query Modification Method
=================================================================================
Besides the "tf-idf", we have also tested some other methods to compute the weight vectors in order to increase the performance
of the program.

A)
Since the tokens in the document is too sparse, we think it may introduce noise in the "tf-idf" calculation. Instead, we tried
two different ways:
	-1. Simply using (tf)/(df)
	-2. Still using (tf)/(df), but count the human relevance judgement to compute the df, that is, if the document is relevant,
		df of each token in this document will be halved, otherwise use the normal counting. In this way, we are aiming at
		increase the weight score of tokens in the relevant document.

However, both these methods give even worse performance than the "tf-idf".

B)
We have also tried to implement the "wtf-idf" mentioned in the Chapter 6 of the Book - Information Retrieval. Basically, it
just changes tf to (1+log(tf)).

After test, it showed worse performance than tf-idf.


