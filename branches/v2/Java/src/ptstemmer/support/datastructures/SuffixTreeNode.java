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

import java.util.HashMap;

/**
 * Object-oriented Suffix Tree node
 * @author Pedro Oliveira
 *
 * @param <T>
 */
public class SuffixTreeNode<T> {

	private HashMap<Character, SuffixTreeNode<T>> edges;
	private T value;
	
	public SuffixTreeNode()
	{
		edges = new HashMap<Character, SuffixTreeNode<T>>(26);
	}
	
	public SuffixTreeNode(T value)
	{
		edges = new HashMap<Character, SuffixTreeNode<T>>(26);
		this.value = value;
	}
	
	/**
	 * Add edge to node
	 * @param c
	 * @param value
	 * @return
	 */
	public SuffixTreeNode<T> addEdge(char c, T value)
	{
		SuffixTreeNode<T> node = edges.get(c);
		if(node == null)
		{
			node = new SuffixTreeNode<T>(value);
			edges.put(c, node);
		}
		return node;
	}
	
	public SuffixTreeNode<T> getEdge(char c)
	{
		return edges.get(c);
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}	
	
	public String toString()
	{
		return "["+value+"]";
	}
}
