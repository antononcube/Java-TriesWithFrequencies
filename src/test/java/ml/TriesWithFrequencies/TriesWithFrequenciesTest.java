package ml.TriesWithFrequencies;

//import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TriesWithFrequenciesTest {
    @Test
    public void basicCreation() {

//		List<String> sampleSeq = new ArrayList<String>() {{
//			add("arm"); add("arms"); add("arc"); add("bar"); add("bark"); add("barman"); add("arcola"); }};

        List<String> sampleSeq = new ArrayList<String>() {{
            add("a;r;m");
            add("a;r;m;s");
            add("a;r;c");
            add("b;a;r");
            add("b;a;r;k");
            add("b;a;r;m;a;n");
            add("a;r;c;o;l;a");
        }};


//		List< List<String> > sampleSeqList = new ArrayList<>();
//
//        for ( String s : sampleSeq ) {
//            sampleSeqList.add( Arrays.asList( s.split("") ) );
//        }
//
//        System.out.println( sampleSeqList );
//
//        Trie strie = TrieFunctions.create( sampleSeqList );

        Trie strie = TrieFunctions.createBySplit(sampleSeq, ";");

        System.out.println("strie = ");
        System.out.println(strie);
        System.out.println();

        Boolean tRes = TrieFunctions.contains(strie, new ArrayList<>(Arrays.asList("arms".split(""))));
        Assertions.assertTrue(tRes);
    }

    @Test
    public void shrinking() {
//        sampleSeq = new ArrayList<String>() {{
//            add("bar"); add("barks"); add("barkeep"); add("barn"); add("balm");
//            add("car"); add("care"); add("caress"); add("card"); add("cold"); add("colder"); }};

        //assertTrue(refSize - 1 <= path.size() && path.size() <= refSize + 1);

        List<String> sampleSeq = new ArrayList<>() {{
            add("barks");
            add("barkers");
            add("barked");
            add("barkeeps");
            add("barkeepers");
            add("barking");
            add("balm");
            add("barman");
        }};

        Trie strie = TrieFunctions.createBySplit(sampleSeq, "");

        Boolean tRes = TrieFunctions.contains(strie, new ArrayList<>(Arrays.asList("barkeeps".split(""))));
        Assertions.assertTrue(tRes);

        System.out.println("shrink trie:");
        System.out.println(TrieFunctions.shrink(strie, ":"));
        System.out.println();

        System.out.println("shrink internal nodes only trie:");
        System.out.println(TrieFunctions.shrinkInternalNodes(strie, ":", 1.0));
        System.out.println();
    }

    @Test
    public void retrieval() {

        List<String> sampleSeq = new ArrayList<>() {{
            add("barks");
            add("barkers");
            add("barked");
            add("barkeeps");
            add("barkeepers");
            add("barking");
            add("balm");
            add("barman");
        }};

        Trie strie = TrieFunctions.createBySplit(sampleSeq, "");
        List<String> sword = new ArrayList<>() {{
            add("b");
            add("a");
            add("r");
        }};
        System.out.println("For " + sword);
        System.out.println("contains: " + TrieFunctions.contains(strie, sword));
        System.out.println("position: " + TrieFunctions.position(strie, sword));
        System.out.println("complete match: " + TrieFunctions.hasCompleteMatch(strie, sword));
        System.out.println();

        sword = new ArrayList<>() {{
            add("b");
            add("a");
        }};

        System.out.println("For " + sword);
        System.out.println("contains: " + TrieFunctions.contains(strie, sword));
        System.out.println("position: " + TrieFunctions.position(strie, sword));
        System.out.println("complete match: " + TrieFunctions.hasCompleteMatch(strie, sword));
        System.out.println();

        Trie ftrie = TrieFunctions.retrieve(strie, sword);
        System.out.println("ftrie = ");
        System.out.println(ftrie + "\n");

        Trie pstrie = TrieFunctions.nodeProbabilities(strie);

        System.out.println("pstrie = ");
        System.out.println(pstrie + "\n");


        System.out.println("strie = ");
        System.out.println(strie + "\n");

        sword = new ArrayList<>() {{
            add("a");
            add("r");
        }};
        System.out.println("words for:" + sword);
        System.out.println(TrieFunctions.getWords(strie, sword));
        System.out.println();

//        System.out.println( "paths to JSON:");
//        System.out.println( TrieFunctions.pathsToJSON( TrieFunctions.rootToLeafPaths( strie) ) );
//        System.out.println();

        System.out.println("paths strie:");
        System.out.println(TrieFunctions.rootToLeafPaths(strie));
        System.out.println();

        System.out.println("paths pstrie:");
        System.out.println(TrieFunctions.rootToLeafPaths(pstrie));
        System.out.println();


        System.out.println("node counts:");
        System.out.println(TrieFunctions.nodeCounts(strie));
        System.out.println();

        System.out.println("leaf probabilities :");
        System.out.println(TrieFunctions.leafProbabilities(pstrie));
        System.out.println();
        System.out.println(TrieFunctions.leafProbabilitiesJSON(pstrie));
        System.out.println();

        System.out.println("randomChoice :");
        System.out.println(TrieFunctions.randomChoice(pstrie, true));
        System.out.println();
        System.out.println(TrieFunctions.randomChoice(pstrie, false));
        System.out.println();


        System.out.println("Top 3 paths :");
        System.out.println(TrieFunctions.topRootToLeafPaths(pstrie, 3));
        System.out.println();
    }

}
