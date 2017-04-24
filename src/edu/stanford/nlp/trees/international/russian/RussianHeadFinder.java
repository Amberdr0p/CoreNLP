package edu.stanford.nlp.trees.international.french;

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
    nonTerminalInfo.put(tlp.startSymbol(), new String[][]{{"left", "VN", "NP"}, {"left"}});
    nonTerminalInfo.put("SENT", new String[][]{{"left", "VN", "NP"}, {"left"}});
    
    // adjectival phrases
    nonTerminalInfo.put("AP", new String[][]{{"left", "A", "V"}, {"rightdis", "N", "ET"}, {"left"}});

    // adverbial phrases
    nonTerminalInfo.put("AdP", new String[][]{{"right", "ADV"}, {"left", "N"}, {"right"}});

    // coordinated phrases
    nonTerminalInfo.put("COORD", new String[][]{{"leftdis", "C", "CC", "ADV", "PP", "P"}, {"left"}});

    // noun phrases
    nonTerminalInfo.put("NP", new String[][]{{"rightdis", "N", "PRO", "NP", "A"}, {"right", "ET"}, {"right"}});

    // prepositional phrases
    nonTerminalInfo.put("PP", new String[][]{{"left", "P", "PRO", "A", "NP", "V", "PP", "ADV"}, {"left"}});

    // verbal nucleus
    nonTerminalInfo.put("VN", new String[][]{{"right", "V", "VN"}, {"right"}});

    // infinitive clauses
    // OK
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VERB"}, {"left"}}); // "хочется смеяться"
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VP", "VERB"}, {"left"}}); // "быстро решил вмешаться"
    nonTerminalInfo.put("VPinf", new String[][]{{"left", "VERB", "VP"}, {"left"}}); // "решил не вмешиваться" VP -> PART  + VERB
    // https://nlp.stanford.edu/nlp/javadoc/javanlp-3.6.0/edu/stanford/nlp/trees/AbstractCollinsHeadFinder.html

    // nonfinite clauses
    nonTerminalInfo.put("VPpart", new String[][]{{"left", "VN", "V", "AP", "A", "AdP", "VPpart"}, {"left"}});

    // relative clauses
    nonTerminalInfo.put("Srel", new String[][]{{"left", "NP", "PRO", "PP", "C", "ADV"}});

    // subordinate clauses
    nonTerminalInfo.put("Ssub", new String[][]{{"left", "C", "PC", "ADV", "P", "PP"}, {"left"}});

    // parenthetical clauses
    nonTerminalInfo.put("Sint", new String[][]{{"left", "VN", "V", "NP", "Sint", "Ssub", "PP"}, {"left"}});

    // adverbes
    nonTerminalInfo.put("ADV", new String[][] {{"left", "ADV", "PP", "P"}});

    // compound categories: start with MW: D, A, C, N, ADV, V, P, PRO, CL
    nonTerminalInfo.put("MWD", new String[][] {{"left", "D"}, {"left"}});
    nonTerminalInfo.put("MWA", new String[][] {{"left", "P"}, {"left", "N"}, {"right", "A"}, {"right"}});
    nonTerminalInfo.put("MWC", new String[][] {{"left", "C", "CS"}, {"left"}});
    nonTerminalInfo.put("MWN", new String[][] {{"right", "N", "ET"}, {"right"}});
    nonTerminalInfo.put("MWV", new String[][] {{"left", "V"}, {"left"}});
    nonTerminalInfo.put("MWP", new String[][] {{"left", "P", "ADV", "PRO"}, {"left"}});
    nonTerminalInfo.put("MWPRO", new String[][] {{"left", "PRO", "CL", "N", "A"}, {"left"}});
    nonTerminalInfo.put("MWCL", new String[][] {{"left", "CL"}, {"right"}});
    nonTerminalInfo.put("MWADV", new String[][] {{"left", "P", "ADV"}, {"left"}});

    nonTerminalInfo.put("MWI", new String[][] {{"left", "N", "ADV", "P"}, {"left"}});
    nonTerminalInfo.put("MWET", new String[][] {{"left", "ET", "N"}, {"left"}});

    //TODO: wsg2011: For phrasal nodes that lacked a label.
    nonTerminalInfo.put(FrenchXMLTreeReader.MISSING_PHRASAL, new String[][]{{"left"}});
    
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

