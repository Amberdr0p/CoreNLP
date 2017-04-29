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
public class RussianHeadFinder extends AbstractCollinsHeadFinder {

  public RussianHeadFinder() {
    this(new RussianTreebankLanguagePack());
  }


  public RussianHeadFinder(RussianTreebankLanguagePack tlp) {
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
    nonTerminalInfo.put(tlp.startSymbol(), new String[][]{{"left", "VP", "NP"}, {"left"}});
    nonTerminalInfo.put("SENT", new String[][]{{"left", "VP", "NP"}, {"left"}});
    
    // adjectival phrases
    nonTerminalInfo.put("AP", new String[][]{{"left", "A", "V"}, {"rightdis", "N", "ET"}, {"left"}});

    // adverbial phrases
    nonTerminalInfo.put("ADVP", new String[][]{{"left", "ADV"}, {"right"}});

    // coordinated phrases
    nonTerminalInfo.put("COORD", new String[][]{{"left", "CONJ"}, {"left"}});

    // noun phrases
    nonTerminalInfo.put("NP", new String[][]{{"left", "ADJ", "NOUN",  "PRON", "NP", "NUM"}, {"left"}});
    nonTerminalInfo.put("NP", new String[][]{{"right", "NOUN",  "PRON", "NP", "ADJ"}, {"right"}});
   

    // prepositional phrases
    // OK
    nonTerminalInfo.put("PP", new String[][]{{"left", "ADP", "NOUN"}, {"left"}); //"в течение"
    nonTerminalInfo.put("PP", new String[][]{{"left", "ADP"}, {"right"}}); //"в течение"

    // verbal nucleus
    nonTerminalInfo.put("VP", new String[][]{{"right", "VPinf", "VERB"}, {"right"}});
    nonTerminalInfo.put("VP", new String[][]{{"left", "VERB", "VPinf"}, {"left"}});

    // infinitive clauses
    // OK
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VERB"}, {"left"}}); // "хочется смеяться"
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VP", "VERB"}, {"left"}}); // "быстро решил вмешаться"
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VP"}, {"left"}}); // "решил не вмешиваться" VP -> PART  + VERB
    // https://nlp.stanford.edu/nlp/javadoc/javanlp-3.6.0/edu/stanford/nlp/trees/AbstractCollinsHeadFinder.html

    // nonfinite clauses
    nonTerminalInfo.put("VPpart", new String[][]{{"left", "VP", "V", "NOUN", "ADV", "ADJ"}, {"left"}});

    // relative clauses
    nonTerminalInfo.put("Srel", new String[][]{{"left", "CONJ"}, {"left"}});

    // subordinate clauses
    nonTerminalInfo.put("Ssub", new String[][]{{"left", "CONJ"}, {"left"}});

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
    final HeadFinder chf = new RussianHeadFinder();
    treebank.apply(pt -> {
      pt.percolateHeads(chf);
      pt.pennPrint();
      System.out.println();
    });
  }

  private static final long serialVersionUID = 8747319554557223422L;


}

