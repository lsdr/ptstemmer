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
using System.Collections;
using System.Collections.Generic;


namespace ptstemmer.support.datastructures
{
	/// <summary>
	/// Simple HashSet Implementation
	/// </summary>
	public class HashSet<T>: ICollection<T>
	{
		private readonly Dictionary<T, object> dict;
		
		public HashSet()
		{
			dict = new Dictionary<T,object>();
		}
		
		public HashSet(IEnumerable<T> items) : this()
	    {
	        if (items == null){
	            return;
	        }

	        foreach (T item in items){
	            Add(item);
	        }
	    }
		
		public void Add(T item)
		{
	        if (item == null){
	            throw new ArgumentNullException();
	        }

	        dict[item] = null;
		}
		
		public void AddRange(IEnumerable<T> items)
		{
	        if (items == null){
	            throw new ArgumentNullException();
	        }
			foreach(T item in items)
				Add(item);
		}
		
		public bool Contains(T item)
		{
			return dict.ContainsKey(item);
		}
		
		public void Clear()
	    {
	        dict.Clear();
	    }
		
		public bool Remove(T item)
	    {
	        return dict.Remove(item);
	    }

		public int Count
	    {
	        get { return dict.Count; }
	    }
				
		public bool IsReadOnly
	    {
	        get{ return false; }
	    }	
		
		public List<T> ToList()
	    {
	        return new List<T>(this);
	    }

		public void CopyTo(T[] array, int arrayIndex)
	    {
	        if (array == null) 
				throw new ArgumentNullException();
	        if (arrayIndex < 0 || arrayIndex >= array.Length || arrayIndex >= Count){
	            throw new ArgumentOutOfRangeException();
	        }
	        dict.Keys.CopyTo(array, arrayIndex);
	    }	
				
		public IEnumerator<T> GetEnumerator()
		{
			return dict.Keys.GetEnumerator();
		}
		
		IEnumerator IEnumerable.GetEnumerator()
	    {
	        return GetEnumerator();
	    }
	}
}
