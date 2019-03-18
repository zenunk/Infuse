To run the experiments, perform the following steps:
1.	Create feature vectors running FVvector.java (Feature vectors -> src)
2.	Compute aggregated score composed of semantic and contextual information running Similarity.java (Semantic &context -> src)
3.	Compute Leveshtein distance between terms obtained in step 2 and concepts of the ontology using Lucene fuzzy search
4.	 Run python scripts located in folder named scripts to simulate different representation techniques and classifiers.
