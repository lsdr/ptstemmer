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

namespace ptstemmer.support.datastructures
{

	/// <summary>
	/// Object-oriented Suffix Tree implementation
	/// @author Pedro Oliveira
	/// </summary>
	public class SuffixTree<T>
	{
		private SuffixTreeNode<T> root;	
		private Dictionary<String, int> properties;
		
		public SuffixTree()
		{
			root = new SuffixTreeNode<T>();
			properties = new Dictionary<String, int>(2);
		}
		
		public SuffixTree(T val, String[] suffixes): this()
		{
			foreach(String suffix in suffixes)
				addSuffix(suffix, val);
		}
		
		public Dictionary<String, int> Properties
		{
			get {return properties;}
			set {properties = value;}
		}
		
		public bool containsProperty(String property)
		{
			return properties.ContainsKey(property);
		}
		
		/// <summary>
		/// Add suffix to the Suffix Tree
		/// </summary>
		/// <param name="suffix">
		/// A <see cref="String"/>
		/// </param>
		/// <param name="val">
		/// A <see cref="T"/>
		/// </param>
		public void addSuffix(String suffix, T val)
		{
			SuffixTreeNode<T> node = root;
			char c;
			for(int i=suffix.Length-1; i>=0; i--)
			{
				c = suffix[i];
				node = node.addEdge(c);
			}
			node.ValueIsNull = false;
			node.Value = val;
		}

		/// <summary>
		/// Checks if Suffix Tree contains word
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="System.Boolean"/>
		/// </returns>
		public bool contains(String word)
		{
			SuffixTreeNode<T> cnode = root;
			char c;
			for(int i=word.Length-1; i>=0; i--)
			{
				c = word[i];
				cnode = cnode[c];
				if(cnode == null)
					return false;
			}
			if(cnode != null && !cnode.ValueIsNull)
				return true;
			return false;
		}
		
		/// <summary>
		/// Get value saved on the longest suffix of the word
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="T"/>
		/// </returns>
		public T getLongestSuffixValue(String word)
		{
			Pair<String, T> res = getLongestSuffixAndValue(word);
			return res == null? default(T): res.Second;
		}
	
		/// <summary>
		/// Get word's longest suffix present in the tree
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="String"/>
		/// </returns>
		public String getLongestSuffix(String word)
		{
			Pair<String, T> res = getLongestSuffixAndValue(word);
			return res == null? "": res.First;
		}
		
		/// <summary>
		/// Get word's longest suffix and value
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="Pair`2"/>
		/// </returns>
		public Pair<String, T> getLongestSuffixAndValue(String word)
		{
			SuffixTreeNode<T> cnode = root;
			int longestSuffixIndex = -1;
			T valueToReturn = default (T);
			char c;
			for(int i=word.Length-1; i>=0; i--)
			{
				c = word[i];
				cnode = cnode[c];
				if(cnode != null)
				{
					if(!cnode.ValueIsNull)
					{
						longestSuffixIndex = i;
						valueToReturn = cnode.Value;
					}
				}
				else
					break;
			}
			if(longestSuffixIndex != -1)
				return new Pair<String, T>(word.Substring(longestSuffixIndex), valueToReturn);
			return null;
		}

		/// <summary>
		/// Get all the suffixes in the word and their values
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="List`1"/>
		/// </returns>
		public List<Pair<String, T>> getLongestSuffixesAndValues(String word)
		{
			SuffixTreeNode<T> cnode = root;
			char c;		
			List<Pair<String, T>> res = new List<Pair<String,T>>();
			
			for(int i=word.Length-1; i>=0; i--)
			{
				c = word[i];
				cnode = cnode[c];
				if(cnode != null)
				{
					if(!cnode.ValueIsNull)
							res.Add(new Pair<String, T>(word.Substring(i), cnode.Value));
				}
				else
					break;
			}
			return res;
		}
	}
}
