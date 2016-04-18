# PTStemmer - A Stemming toolkit for the Portuguese language #

## FEATURES ##
  * **Java**, **Python**, and **.NET C#** implementations of **Orengo**, **Porter**, and **Savoy** stemmers

  * **Fast**: can stem more than **1.5M** words/second on a normal desktop

  * Least Recently Used (**LRU**) stem cache

  * Support for lists of words to ignore (useful for stopword and named entity removal)


## MINIMUM REQUIREMENTS ##
  * **Java**
    * Java 1.5
  * **Python**
    * Python 2.5
  * **.NET**
    * .NET Framework 2.0 or Mono 2.0


## USAGE EXAMPLES ##
More usage examples on the wiki: [UsageExamples](UsageExamples.md)
### Java ###
```
Stemmer stemmer = new OrengoStemmer();
stemmer.enableCaching(1000);   //Optional
stemmer.ignore(PTStemmerUtilities.fileToSet("data/namedEntities.txt"));  //Optional
System.out.println(stemmer.getWordStem("extremamente"));
```
### Python ###
```
stemmer = OrengoStemmer()
stemmer.enableCaching(1000)   #Optional
stemmer.ignore(PTStemmerUtilities.fileToSet("data/namedEntities.txt"))   #Optional
print stemmer.getWordStem("extremamente")
```
### C# ###
```
Stemmer stemmer = new OrengoStemmer();
stemmer.enableCaching(1000);   //Optional
stemmer.ignore(PTStemmerUtilities.fileToList("data/namedEntities.txt"));   //Optional
Console.WriteLine(stemmer.getWordStem("extremamente"));
```


## TODO ##
  * Add more programming languages (PHP, Ruby)
  * Implement a portuguese language specific tokenizer


## DEVELOPED & SUPPORTED ##
  * Pedro Oliveira
    * http://www.cpdomina.net