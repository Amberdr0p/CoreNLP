package edu.stanford.nlp.trees.international.russian;

import edu.stanford.nlp.ling.CategoryWordTag;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.Generics;


/**
 * TODO wsg2010: Compare these head finding rules to those found in Arun Abishek's 
 * master's thesis.
 * 
 * @author mcdm
 */
public class RussianHeadFinder2 extends AbstractCollinsHeadFinder {

  public RussianHeadFinder2() {
    this(new RussianTreebankLanguagePack());
  }


  public RussianHeadFinder2(RussianTreebankLanguagePack tlp) {
    super(tlp);


    //Russian POS (UD POS-tags):
    // ADJ: adjective, ADP: adposition, ADV: adverb, AUX: auxiliary
    // CCONJ: coordinating conjunction, DET: determiner, INTJ: interjection
    // NOUN: noun, NUM: numeral, PART: particle, PRON: pronoun, PROPN: proper noun, PUNCT: punctuation
    // SCONJ: subordinating conjunction, SYM: symbol, VERB: verb, X: other

    // A (adjective), ADV (adverb), C (conjunction and subordinating conjunction), CL (clitics),
    // CS (subordinating conjunction) but occurs only once!,
    // D (determiner), ET (foreign word), I (interjection), N (noun),
    // P (preposition), PREF (prefix), PRO (strong pronoun -- very confusing), V (verb), PUNC (punctuation)

    nonTerminalInfo = Generics.newHashMap();

    // "sentence"
    // OK
    nonTerminalInfo.put(tlp.startSymbol(), new String[][]{{"leftdis", "VERB", "NP"}});
    //nonTerminalInfo.put("ROOT", new String[][]{{"left", "VERB"}, {"left"}});
    //nonTerminalInfo.put("SENT", new String[][]{{"right", "VP"}, {"right"}});
    
   
    // adverbial phrases
    nonTerminalInfo.put("ADVP", new String[][]{{"leftdis", "ADV"}, {"left"}});
    nonTerminalInfo.put("ADVP", new String[][]{{"rightdis", "ADV"}, {"right"}});

    // coordinated phrases
    nonTerminalInfo.put("COORD", new String[][]{{"left", "CONJ"}, {"left"}});

    // noun phrases
    nonTerminalInfo.put("NP", new String[][]{{"leftdis", "NOUN", "PRON", "NP"}});
    nonTerminalInfo.put("NP", new String[][]{{"right", "NP"}});
   

    // prepositional phrases
    // OK
    //nonTerminalInfo.put("PP", new String[][]{{"left", "ADP", "NOUN"}, {"left"}}); //"в течение"
    nonTerminalInfo.put("PP", new String[][]{{"leftdis", "ADP"}}); // правое ветвление, "в лесу"

    // verbal nucleus
    nonTerminalInfo.put("VP", new String[][]{{"left", "VERB", "VP"}});
    nonTerminalInfo.put("VP", new String[][]{{"right", "VERB", "VP"}});

    // infinitive clauses
    // OK
    //nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VERB"}, {"left"}}); // "хочется смеяться"
    //nonTerminalInfo.put("VPinf", new String[][]{{"left", "VP", "VERB"}, {"left"}}); // "быстро решил вмешаться"
    //nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VP"}, {"left"}}); // "решил не вмешиваться" VP -> PART  + VERB
    // https://nlp.stanford.edu/nlp/javadoc/javanlp-3.6.0/edu/stanford/nlp/trees/AbstractCollinsHeadFinder.html

    // nonfinite clauses
    //nonTerminalInfo.put("VPpart", new String[][]{{"left", "VP", "V", "NOUN", "ADV", "ADJ"}, {"left"}});

    // relative clauses
    nonTerminalInfo.put("Srel", new String[][]{{"left", "CONJ"}});

    // subordinate clauses
    nonTerminalInfo.put("Ssub", new String[][]{{"left", "CONJ"}});

    // parenthetical clauses
    //nonTerminalInfo.put("Sint", new String[][]{{"left", "VN", "V", "NP", "Sint", "Ssub", "PP"}, {"left"}});

    // adverbes
    //nonTerminalInfo.put("ADV", new String[][] {{"left", "ADV", "PP", "P"}});

    // compound categories: start with MW: D, A, C, N, ADV, V, P, PRO, CL
                                             
    //TODO: wsg2011: For phrasal nodes that lacked a label.
    //nonTerminalInfo.put(FrenchXMLTreeReader.MISSING_PHRASAL, new String[][]{{"left"}});
    
  }


  /**
   * Go through trees and determine their heads and print them.
   * Just for debugging. <br>
   * Usage: <code>
   * java edu.stanford.nlp.trees.FrenchHeadFinder treebankFilePath
   * </code>
   *
   * @param args The treebankFilePath
   */
  public static void main(String[] args) {
    Treebank treebank = new DiskTreebank();
    CategoryWordTag.suppressTerminalDetails = true;
    treebank.loadPath(args[0]);
    final HeadFinder chf = new RussianHeadFinder2();
    treebank.apply(pt -> {
      pt.percolateHeads(chf);
      pt.pennPrint();
      System.out.println();
    });
  }

  private static final long serialVersionUID = 8747319554557223422L;


}

