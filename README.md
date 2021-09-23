# Tries with frequencies in Java

## In brief

This repository provides a Java class / package for creation and manipulation of 
[Tries (Prefix trees)](https://en.wikipedia.org/wiki/Trie) 
with frequencies. 

The Java code was originally published at
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction)
-- see 
[Tries with frequencies in Java](https://github.com/antononcube/MathematicaForPrediction/tree/master/Java/TriesWithFrequencies).

### Related packages in other languages

- The Mathematica package 
[JavaTriesWithFrequencies.m](https://github.com/antononcube/MathematicaForPrediction/blob/master/JavaTriesWithFrequencies.m),
[AAp4],
uses this Java package. The corresponding unit tests are given in [AAp5].

- The Mathematica packages [AAp1, AAp2] provide Tries with frequencies functionalities using 
"native" Mathematica structures and code.

- The Raku package 
[ML::TriesWithFrequencies](https://github.com/antononcube/Raku-ML-TriesWithFrequencies), [AAp6],
is based on this Java implementation.

## Structure

The file ["src/Trie.java"](https://github.com/antononcube/MathematicaForPrediction/blob/master/Java/TriesWithFrequencies/src/Trie.java) 
contains the definition of the class `Trie`.

The file ["src/TrieFunctions.java"](https://github.com/antononcube/MathematicaForPrediction/blob/master/Java/TriesWithFrequencies/src/TrieFunctions.java)
has implementations of a variety of functions that can used over tries.

The file ["src/Experiments.java"](https://github.com/antononcube/MathematicaForPrediction/blob/master/Java/TriesWithFrequencies/src/Experiments.java)
is only used to do sanity check tests over the implementations. 

We call a trie "word" a list of strings.

## Use through a Mathematica package

The Mathematica package 
[JavaTriesWithFrequencies.m](https://github.com/antononcube/MathematicaForPrediction/blob/master/JavaTriesWithFrequencies.m)
provides functions for utilizing the implemented Java Trie functionalities. 

The test file 
[JavaTriesWithFrequencies-Unit-Tests.wlt](https://github.com/antononcube/MathematicaForPrediction/blob/master/UnitTests/JavaTriesWithFrequencies-Unit-Tests.wlt)
provides unit tests for
[JavaTriesWithFrequencies.m](https://github.com/antononcube/MathematicaForPrediction/blob/master/JavaTriesWithFrequencies.m).

In order to use the package the corresponding .jar file must be made -- see the next section.

## How to use in Mathematica directly

In order to use the defined Java functions in Mathematica the following steps have to be taken.

### Making a jar file

In the local directory "src" execute the following commands:

    src> mkdir build
    src> javac -d ./build *.java; cd build; jar cvf ../../TriesWithFrequencies.jar *; cd ../
    
(Skip the first line if you have the directory "src/build" already.)

### Mathematica JLink set-up

    $JavaTriesWithFrequenciesPath = "<<path>>/MathematicaForPrediction/Java/TriesWithFrequencies";

    Needs["JLink`"];
    AddToClassPath[$JavaTriesWithFrequenciesPath];
    ReinstallJava[JVMArguments->"-Xmx2g"]
    
    LoadJavaClass["java.util.Collections"];
    LoadJavaClass["java.util.Arrays"];
    
    LoadJavaClass["Trie"];
    LoadJavaClass["TrieFunctions"];
    
### Basic trie creation and retrieval
    
Get dictionary words starting with "b":
    
    dWords = DictionaryLookup["b*"];
    Length[dWords]
    (* 4724 *)
    
Create a trie with the words:
    
    Block[{},
      (* Make a list of words. *)
      jWords = MakeJavaObject[dWords];
      jWords = Arrays`asList[jWords];
      
      (* Make a string object (that represents a spliting regexp pattern). *)
      jSp = MakeJavaObject[""];
      
      (* Create the trie specifying the words to be split into characters. *)
      jTr = TrieFunctions`createBySplit[jWords, jSp];
      
      (* Optionally convert the node frequencies into probabilties. *)
      (*jTr=TrieFunctions`nodeProbabilities[jTr]*)
    ];
    
Get the sub-trie that corresponds to "bark":
    
    jSubTr = TrieFunctions`retrieve[jTr, Arrays`asList[MakeJavaObject[Characters["bark"]]]]
    (* JLink`Objects`vm4`JavaObject17330643155288065 *)     
    
    
Get JSON form of the sub-trie:
    
    ImportString[jSubTr@toJSON[], "JSON"]
    
    (* {"value" -> 10., "key" -> "k", 
     "children" -> {{"value" -> 1., "key" -> "s", 
        "children" -> {}}, {"value" -> 7., "key" -> "e", 
        "children" -> {{"value" -> 2., "key" -> "r", 
           "children" -> {{"value" -> 1., "key" -> "s", 
              "children" -> {}}}}, {"value" -> 1., "key" -> "d", 
           "children" -> {}}, {"value" -> 4., "key" -> "e", 
           "children" -> {{"value" -> 4., "key" -> "p", 
              "children" -> {{"value" -> 1., "key" -> "s", 
                 "children" -> {}}, {"value" -> 2., "key" -> "e", 
                 "children" -> {{"value" -> 2., "key" -> "r", 
                    "children" -> {{"value" -> 1., "key" -> "s", 
                       "children" -> {}}}}}}}}}}}}, {"value" -> 1., 
        "key" -> "i", 
        "children" -> {{"value" -> 1., "key" -> "n", 
           "children" -> {{"value" -> 1., "key" -> "g", 
              "children" -> {}}}}}}}} *)

If we load the package [TriesWithFrequencies.m](https://github.com/antononcube/MathematicaForPrediction/blob/master/TriesWithFrequencies.m) :

    Import["https://raw.githubusercontent.com/antononcube/MathematicaForPrediction/master/TriesWithFrequencies.m"]    

we can visualize the obtained sub-trie (Java object) using the function `ToTrieFromJSON` and `TrieForm`:

    TrieForm@ToTrieFromJSON@ImportString[jSubTr@toJSON[], "JSON"]    
    
!["SubTrie-of-dictionary-trie-by-bark"](http://i.imgur.com/sRlL357.png)


## References

### Articles

[AA1] Anton Antonov,
["Tries with frequencies for data mining"](https://mathematicaforprediction.wordpress.com/2013/12/06/tries-with-frequencies-for-data-mining/),
(2013),
[MathematicaForPrediction at WordPress](https://mathematicaforprediction.wordpress.com).

[AA2] Anton Antonov,
["Removal of sub-trees in tries"](https://mathematicaforprediction.wordpress.com/2014/10/12/removal-of-sub-trees-in-tries/),
(2013),
[MathematicaForPrediction at WordPress](https://mathematicaforprediction.wordpress.com).

[AA3] Anton Antonov,
["Tries with frequencies in Java"](https://mathematicaforprediction.wordpress.com/2017/01/31/tries-with-frequencies-in-java/)
(2017),
[MathematicaForPrediction at WordPress](https://mathematicaforprediction.wordpress.com).
[GitHub Markdown](https://github.com/antononcube/MathematicaForPrediction).


[WK1] Wikipedia entry, [Trie](https://en.wikipedia.org/wiki/Trie).

### Packages

[AAp1] Anton Antonov,
[Tries with frequencies Mathematica Version 9.0 package](https://github.com/antononcube/MathematicaForPrediction/blob/master/TriesWithFrequenciesV9.m),
(2013),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).

[AAp2] Anton Antonov,
[Tries with frequencies Mathematica package](https://github.com/antononcube/MathematicaForPrediction/blob/master/TriesWithFrequencies.m),
(2013-2018),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).

[AAp3] Anton Antonov,
[Tries with frequencies in Java](https://github.com/antononcube/MathematicaForPrediction/tree/master/Java/TriesWithFrequencies),
(2017),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).

[AAp4] Anton Antonov,
[Java tries with frequencies Mathematica package](https://github.com/antononcube/MathematicaForPrediction/blob/master/JavaTriesWithFrequencies.m),
(2017),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).

[AAp5] Anton Antonov,
[Java tries with frequencies Mathematica unit tests](https://github.com/antononcube/MathematicaForPrediction/blob/master/UnitTests/JavaTriesWithFrequencies-Unit-Tests.wlt),
(2017),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).

[AAp6] Anton Antonov,
[ML::TriesWithFrequencies Raku package](https://github.com/antononcube/Raku-ML-TriesWithFrequencies),
(2021),
[MathematicaForPrediction at GitHub](https://github.com/antononcube/MathematicaForPrediction).


### Videos

[AAv1] Anton Antonov,
["Prefix Trees with Frequencies for Data Analysis and Machine Learning"](https://www.youtube.com/watch?v=MdVp7t8xQbQ),
(2017),
Wolfram Technology Conference 2017,
[Wolfram channel at YouTube](https://www.youtube.com/channel/UCJekgf6k62CQHdENWf2NgAQ).

