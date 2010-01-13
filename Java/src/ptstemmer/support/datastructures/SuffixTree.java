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
package ptstemmer.support.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Object-oriented Suffix Tree implementation
 * @author Pedro Oliveira
 *
 * @param <T>
 */
public class SuffixTree<T> {

	private SuffixTreeNode<T> root;	
	private HashMap<String, Integer> properties;
	
	public SuffixTree()
	{
		root = new SuffixTreeNode<T>();
		properties = new HashMap<String, Integer>(2);
	}
	
	public SuffixTree(T value, String... suffixes)
	{
		this();
		for(String suffix: suffixes)
			addSuffix(suffix, value);
	}
	
	public void setProperty(String property, Integer value)
	{
		properties.put(property, value);
	}
	
	public Integer getProperty(String property)
	{
		return properties.get(property);
	}
	
	public boolean containsProperty(String property)
	{
		return properties.containsKey(property);
	}
	
	/**
	 * Add suffix to the Suffix Tree
	 * @param suffix
	 * @param value
	 */
	public void addSuffix(String suffix, T value)
	{
		SuffixTreeNode<T> node = root;
		char c;
		for(int i=suffix.length()-1; i>=0; i--)
		{
			c = suffix.charAt(i);
			node = node.addEdge(c, null);
		}
		node.setValue(value);
	}

	
	/**
	 * Checks if Suffix Tree contains word
	 * @param word
	 * @return
	 */
	public boolean contains(String word)
	{
		SuffixTreeNode<T> cnode = root;
		char c;
		for(int i=word.length()-1; i>=0; i--)
		{
			c = word.charAt(i);
			cnode = cnode.getEdge(c);
			if(cnode == null)
				return false;
		}
		
		if(cnode != null && cnode.getValue() != null)
			return true;
		return false;
	}
	
	/**
	 * Get value saved on the longest suffix of the word
	 * @param word
	 * @return
	 */
	public T getLongestSuffixValue(String word)
	{
		Pair<String, T> res = getLongestSuffixAndValue(word);
		return res != null? res.b: null;
	}
	
	/**
	 * Get word's longest suffix present in the tree
	 * @param word
	 * @return
	 */
	public String getLongestSuffix(String word)
	{
		Pair<String, T> res = getLongestSuffixAndValue(word);
		return res != null? res.a: "";
	}
	
	/**
	 * Get word's longest suffix and value
	 * @param word
	 * @return
	 */
	public Pair<String, T> getLongestSuffixAndValue(String word)
	{
		SuffixTreeNode<T> cnode = root;
		int longestSuffixIndex = -1;
		T valueToReturn = null;
		char c;
		for(int i=word.length()-1; i>=0; i--)
		{
			c = word.charAt(i);
			cnode = cnode.getEdge(c);
			if(cnode != null)
			{
				if(cnode.getValue() != null)
				{
					longestSuffixIndex = i;
					valueToReturn = cnode.getValue();
				}
			}
			else
				break;
		}
		if(longestSuffixIndex != -1)
			return new Pair<String, T>(word.substring(longestSuffixIndex), valueToReturn);
		return null;
	}

	/**
	 * Get all the suffixes in the word and their values
	 * @param word
	 * @return
	 */
	public List<Pair<String, T>> getLongestSuffixesAndValues(String word)
	{
		SuffixTreeNode<T> cnode = root;
		char c;		
		List<Pair<String, T>> res = new ArrayList<Pair<String,T>>();
		
		for(int i=word.length()-1; i>=0; i--)
		{
			c = word.charAt(i);
			cnode = cnode.getEdge(c);
			if(cnode != null)
			{
					if(cnode.getValue() != null)
						res.add(new Pair<String, T>(word.substring(i), cnode.getValue()));
			}
			else
				break;
		}
		return res;
	}
}
