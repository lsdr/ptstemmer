/**
 * PTStemmer - A Stemming toolkit for the Portuguese language (C) 2008-2010 Pedro Oliveira
 * 
 * This file is part of PTStemmer.
 * PTStemmer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * PTStemmer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with PTStemmer. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ptstemmer;


import java.util.Collection;
import java.util.HashSet;

import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.implementations.OrengoStemmer;
import ptstemmer.implementations.PorterStemmer;
import ptstemmer.implementations.SavoyStemmer;
import ptstemmer.support.datastructures.LRUCache;

/**
 * Abstract class that provides the main features to all the stemmers
 * @author Pedro Oliveira
 *
 */
public abstract class Stemmer {

	private boolean cacheStems;
	private LRUCache lruCache;

	private HashSet<String> toIgnore = new HashSet<String>();

	public enum StemmerType{ORENGO, PORTER, SAVOY};

	/**
	 * Stemmer construction factory
	 * @param stype
	 * @return
	 * @throws PTStemmerException 
	 */
	public static Stemmer StemmerFactory(StemmerType stype) throws PTStemmerException
	{
		switch (stype) {
		case PORTER:
			return new PorterStemmer();
		case SAVOY:
			return new SavoyStemmer();			
		default:
			return new OrengoStemmer();
		}
	}

	/**
	 * Create a LRU Cache, caching the last <code>size</code> stems
	 * @param size
	 */
	public void enableCaching(int size)
	{
		cacheStems = true;
		lruCache = new LRUCache(size);
	}

	/**
	 * Disable and delete the LRU Cache
	 */
	public void disableCaching()
	{
		cacheStems = false;
		lruCache = null;
	}

	/**
	 * Check if LRU Cache is enabled
	 * @return
	 */
	public boolean isCachingEnabled()
	{
		return cacheStems;
	}

	/**
	 * Add list of words to ignore list
	 * @param words
	 */
	public void ignore(String... words)
	{
		for(String word: words)
			toIgnore.add(word);
	}

	/**
	 * Add Collection of words to ignore list
	 * @param words
	 */
	public void ignore(Collection<String> words)
	{
		toIgnore.addAll(words);
	}

	/**
	 * Clear the contents of the ignore list
	 */
	public void clearIgnoreList()
	{
		toIgnore.clear();
	}

	/**
	 * Performs stemming on the <code>phrase</code>, using a simple space tokenizer
	 * @param phrase
	 * @return
	 */
	public String[] getPhraseStems(String phrase)
	{
		String[] res = phrase.split(" ");
		for(int i=0; i<res.length; i++)
			res[i] = getWordStem(res[i]);
		return res;
	}

	/**
	 * Performs stemming on the <code>word</code>
	 * @param word
	 * @return
	 */
	public String getWordStem(String word)
	{
		String res;
		word = word.trim().toLowerCase();

		if(cacheStems)
			if(lruCache.containsKey(word))
				return lruCache.get(word);

		if(toIgnore.contains(word))
			return word;
		
		res = stemming(word);

		if(cacheStems)
			lruCache.put(word, res);
		return res;
	}

	protected abstract String stemming(String word);
}
