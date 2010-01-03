package ptstemmer.support.datastructures;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Suffix Tree node
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
	
	public String toString()
	{
		return "["+value+"]";
	}
	
	
	public void printTrie()
	{
		Set<Entry<Character, SuffixTreeNode<T>>> entries = edges.entrySet();	
		for(Entry<Character, SuffixTreeNode<T>> entry: entries)
			System.out.print(entry.getKey().toString()+' ');
		System.out.println();
		for(Entry<Character, SuffixTreeNode<T>> entry: entries)
			entry.getValue().printTrie();
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}
}
