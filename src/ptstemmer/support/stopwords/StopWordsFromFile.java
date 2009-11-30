/**
 * PTStemmer - Java Stemming toolkit for the Portuguese language (C) 2008 Pedro Oliveira
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
package ptstemmer.support.stopwords;

import java.util.HashSet;

import ptstemmer.support.ReadFileToHashSet;

/**
 * Read the named stopword list from a file (one stopword per line)
 * @author Pedro Oliveira
 *
 */
public class StopWordsFromFile implements StopWordList {

	private HashSet<String> res;
	
	public StopWordsFromFile(String file)
	{
		res = ReadFileToHashSet.getInstance().fileToHash(file);
	}
	
	//@Override
	public boolean isStopWord(String word) {
		return res.contains(word);
	}

}
