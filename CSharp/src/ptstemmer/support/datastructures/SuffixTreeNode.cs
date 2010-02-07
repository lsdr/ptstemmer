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
	/// Object-oriented Suffix Tree node
	/// </summary>
	public class SuffixTreeNode<T>
	{		
		private readonly Dictionary<char, SuffixTreeNode<T>> edges;
		private T val;
		private bool isNull;
		
		public SuffixTreeNode()
		{
			edges = new Dictionary<char,SuffixTreeNode<T>>();
		}
		
		public SuffixTreeNode(T val): this()
		{
			this.val = val;
		}
		
		private SuffixTreeNode<T> addEdge(char c, T val, bool isNull)
		{
			SuffixTreeNode<T> node;
			if(!edges.TryGetValue(c, out node))
			{
				node = new SuffixTreeNode<T>(val);
				edges.Add(c, node);
				if(isNull)
					node.isNull = true;
			}
			return node;
		}
		
		public SuffixTreeNode<T> addEdge(char c, T val)
		{
			return addEdge(c,val,false);
		}
		
		public SuffixTreeNode<T> addEdge(char c)
		{
			return addEdge(c,default (T), true);
		}
	
		public SuffixTreeNode<T> this[char c]
		{
			get {
				SuffixTreeNode<T> node;
				edges.TryGetValue(c, out node);
				return node;
				}
		}

		public T Value
		{
			get { return this.val; }
			set { this.val = value; }
		}
		
		public bool ValueIsNull
		{
			get {return this.isNull;}
			set {this.isNull = value; }
		}
	}
}
