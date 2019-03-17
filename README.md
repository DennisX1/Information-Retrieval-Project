# Information-Retrieval-Project

# Protokoll (erstmal hier, muss noch verschoben werden)

Wir fragen am Montag den prof ob subset vom gesamten gesamten dataset oder von einer kategorie. Für den Moment für die Entwicklung nutzen wir ein Kateogie set mit geringer größe.

Und wegen labels der nodes (=overall?)

Dennis macht TF-IDF fertig

Art des Graphen implementieren

Imput data aufräumen

```
Review Klasse

String text;
int id;  // fortlaufend (1,...,n)
double label; //(?)
```

```
Utils
/**
Computes all similarites between the given reviews. The ordering stays the same: result[0][1] is the similarity between reviews[0] and reviews[1].
*/
public static double[][] computeSimilarity(Review[] reviews){…}
```

Jeder einen eigenen Branch und mergen wenn soweit

Roman schreibt import für json und Page rank 

Dennis weiter similiarity measures

Nadja HITS, graphen klasse, latex overleaf
