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
	/// Simple Least Recently Used (LRU) Cache implementation
	/// </summary>
	public class LRUCache<K,V>
	{
		private int capacity;
		private Dictionary<K,V> cache;
		private LinkedList<K> lru;
		
		public LRUCache(int capacity)
		{
			this.capacity = capacity;
			this.cache = new Dictionary<K,V>(capacity);
			this.lru = new LinkedList<K>();
		}
		
		public void Add(K key, V val)
		{
			if(cache.ContainsKey(key))
				lru.Remove(key);
			else
			{
				if(lru.Count == capacity)
				{
					cache.Remove(lru.Last.Value);
					lru.RemoveLast();
				}
			}	
			cache[key] = val;
			lru.AddFirst(key);
		}
		
		public bool TryGetValue(K key, out V val)
		{
			if(cache.TryGetValue(key, out val))
			{
				lru.Remove(key);
				lru.AddFirst(key);
				return true;
			}
			return false;
		}	
	}		
}
