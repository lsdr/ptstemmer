package ptstemmer.support.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Suffix Tree
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
		root = new SuffixTreeNode<T>();
		properties = new HashMap<String, Integer>(2);
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
		return true;
	}
	
	/**
	 * Get value saved on the longest suffix of the word
	 * @param word
	 * @return
	 */
	public T getLongestSuffixValue(String word)
	{
		SuffixTreeNode<T> cnode = root;
		T valueToReturn = null;
		char c;
		for(int i=word.length()-1; i>=0; i--)
		{
			c = word.charAt(i);
			cnode = cnode.getEdge(c);
			if(cnode != null)
			{
				if(cnode.getValue() != null)
					valueToReturn = cnode.getValue();
			}
			else
				break;
		}
		return valueToReturn;
	}
	
	public String getLongestSuffix(String word)
	{
		SuffixTreeNode<T> cnode = root;
		int longestSuffixIndex = -1;
		char c;
		for(int i=word.length()-1; i>=0; i--)
		{
			c = word.charAt(i);
			cnode = cnode.getEdge(c);
			if(cnode != null)
			{
				if(cnode.getValue() != null)
					longestSuffixIndex = i;
			}
			else
				break;
		}
		if(longestSuffixIndex != -1)
			return word.substring(longestSuffixIndex);
		return "";
	}
	
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
	
	public void printTrie()
	{
		root.printTrie();
	}
}
