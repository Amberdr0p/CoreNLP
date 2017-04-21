package edu.stanford.nlp.trees.international.russian;

import edu.stanford.nlp.international.french.FrenchMorphoFeatureSpecification;
import edu.stanford.nlp.international.morph.MorphoFeatureSpecification;
import edu.stanford.nlp.trees.AbstractTreebankLanguagePack;
import edu.stanford.nlp.trees.HeadFinder;

public class RussianTreebankLanguagePack extends AbstractTreebankLanguagePack  {

  public static final String[] pennPunctTags = {"''", "``", ".", ":", ",", "PUNCT", "Пункт"}; // LRB RRB

  private static final String[] pennSFPunctTags = {".", "PUNCT", "Пункт"};

  private static final String[] pennPunctWords = {"=","*","/","\\","]","[","\"","''", "'", "``", "`", ".", "?", "!", ",", ":", "-", "--", "...", ";", "&quot;"};

  private static final String[] pennSFPunctWords = {".", "!", "?", "?!", "...", "!?"};
  
  private static final String[] frenchStartSymbols = {"ROOT"};

  private static final char[] annotationIntroducingChars = {'=', '|', '#', '_'};
  
  // public static final String RU_ENCODING = "UTF-8";
  
  @Override
  public String[] sentenceFinalPunctuationWords() {
    return pennSFPunctWords;
  }

  @Override
  public String treebankFileExtension() {
    return "tree";
  }

  @Override
  public HeadFinder headFinder() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public HeadFinder typedDependencyHeadFinder() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] punctuationTags() {
    return pennPunctTags;
  }

  @Override
  public String[] punctuationWords() {
    return pennPunctWords;
  }

  @Override
  public String[] sentenceFinalPunctuationTags() {
    // TODO Auto-generated method stub
    return pennSFPunctTags;
  }

  @Override
  public String[] startSymbols() {
    return frenchStartSymbols;
  }
  
  @Override
  public char[] labelAnnotationIntroducingCharacters() {
    return annotationIntroducingChars;
  }
  
  @Override
  public MorphoFeatureSpecification morphFeatureSpec() {
    return new FrenchMorphoFeatureSpecification();
  }

}
