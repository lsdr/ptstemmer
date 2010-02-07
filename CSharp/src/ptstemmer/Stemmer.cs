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

using System;
using System.Collections.Generic;

using ptstemmer.implementations;
using ptstemmer.support.datastructures;

namespace ptstemmer
{
	 /// <summary>
	 /// Abstract class that provides the main features to all the stemmers
	 /// @author Pedro Oliveira
	 /// </summary>
	public abstract class Stemmer
	{
		
		private bool cacheStems;
		private LRUCache<String, String> lruCache;
		private HashSet<String> toIgnore = new HashSet<String>();

		public enum StemmerType{ORENGO=0, PORTER=1,SAVOY=2};

		/// <summary>
		/// Stemmer construction factory
		/// </summary>
		/// <param name="stype">
		/// A <see cref="StemmerType"/>
		/// </param>
		/// <returns>
		/// A <see cref="Stemmer"/>
		/// </returns>
		public static Stemmer StemmerFactory(StemmerType stype)
		{
			if(stype == StemmerType.PORTER)
				return new PorterStemmer();
			else if(stype == StemmerType.SAVOY)
				return new SavoyStemmer();
			else
				return new OrengoStemmer();
		}

		/// <summary>
		/// Create a LRU Cache, caching the last <code>size</code> stems
		/// </summary>
		/// <param name="size">
		/// A <see cref="System.Int32"/>
		/// </param>
		public void enableCaching(int size)
		{
			cacheStems = true;
			lruCache = new LRUCache<string,string>(size);
		}

		/// <summary>
		/// Disable and deletes the LRU Cache
		/// </summary>
		public void disableCaching()
		{
			cacheStems = false;
			lruCache = null;
		}

		/// <summary>
		/// Check if LRU Cache is enabled
		/// </summary>
		/// <returns>
		/// A <see cref="System.Boolean"/>
		/// </returns>
		public bool isCachingEnabled()
		{
			return cacheStems;
		}

		/// <summary>
		/// Add list of words to ignore list
		/// </summary>
		/// <param name="words">
		/// A <see cref="String"/>
		/// </param>
		public void ignore(String[] words)
		{
			foreach(String word in words)
				toIgnore.Add(word);
		}
		
		/// <summary>
		/// Add Collection of words to ignore list
		/// </summary>
		/// <param name="words">
		/// A <see cref="ICollection`1"/>
		/// </param>
		public void ignore(ICollection<String> words)
		{
			toIgnore.AddRange(words);
		}

		/// <summary>
		/// Clear the contents of the ignore list
		/// </summary>
		public void clearIgnoreList()
		{
			toIgnore.Clear();
		}

		/// <summary>
		/// Performs stemming on the <code>phrase</code>, using a simple space tokenizer
		/// </summary>
		/// <param name="phrase">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="String"/>
		/// </returns>
		public String[] getPhraseStems(String phrase)
		{
			string[] res = phrase.Split(' ');
			for(int i=0; i<res.Length; i++)
				res[i] = getWordStem(res[i]);
			return res;
		}

		/// <summary>
		/// Performs stemming on the <code>word</code>
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="String"/>
		/// </returns>
		public String getWordStem(String word)
		{
			String res;
			word = word.Trim().ToLower();

			if(cacheStems)
				if(lruCache.TryGetValue(word,out res))
					return res;

			if(toIgnore.Contains(word))
				return word;
			
			res = stemming(word);

			if(cacheStems)
				lruCache.Add(word,res);
			return res;
		}

		public abstract String stemming(String word);
	}
}
