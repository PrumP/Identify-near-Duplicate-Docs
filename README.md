# Identify-near-Duplicate-Docs

We can use Jaccard similarity as matrix to detemine how much two documents are similar. Here I am
identifying near-duplicate documents using Jaccard similarity by minhash and locality sensitive hashing.

MinHash Matrix Construction

* collect all terms of the documents
* coding terms to integer
* generate random permutations

Locality Sensitive Hashing

* use MinHash matrix
* create bands
* identify near duplicates that are "s" similar
