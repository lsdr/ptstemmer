# Java #

**Simple**
```
Stemmer stemmer = new OrengoStemmer();
System.out.println(stemmer.getWordStem("extremamente"));
```

**Complete**
```
Stemmer stemmer = Stemmer.StemmerFactory(StemmerType.ORENGO);
stemmer.enableCaching(1000);
stemmer.ignore(PTStemmerUtilities.fileToSet("data/stopwords.txt"));
stemmer.ignore(PTStemmerUtilities.fileToSet("data/namedEntities.txt"));		
String stem = stemmer.getWordStem("ciências");		
System.out.println(PTStemmerUtilities.removeDiacritics(stem));
```


---

# C# #

**Simple**
```
Stemmer stemmer = new OrengoStemmer();
Console.WriteLine(stemmer.getWordStem("extremamente"));
```

**Complete**
```
Stemmer stemmer = Stemmer.StemmerFactory(Stemmer.StemmerType.ORENGO);
stemmer.enableCaching(1000);
stemmer.ignore(PTStemmerUtilities.fileToList("data/stopwords.txt"));
stemmer.ignore(PTStemmerUtilities.fileToList("data/namedEntities.txt"));		
string stem = stemmer.getWordStem("ciências");		
Console.WriteLine(PTStemmerUtilities.removeDiacritics(stem));
```


---

# Python #

**Simple**
```
stemmer = OrengoStemmer()
print stemmer.getWordStem("extremamente")
```

**Complete**
```
stemmer = OrengoStemmer() #or PorterStemmer() or SavoyStemmer()
stemmer.enableCaching(1000)
stemmer.ignore(PTStemmerUtilities.fileToSet("data/stopwords.txt"))
stemmer.ignore(PTStemmerUtilities.fileToSet("data/namedEntities.txt"))		
stem = stemmer.getWordStem("ciências")
print PTStemmerUtilities.removeDiacritics(stem)
```
