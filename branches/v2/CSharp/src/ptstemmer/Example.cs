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

namespace ptstemmer
{
	
	public class Example
	{		
		public static void Main(string[] args)
		{
			Stemmer s = Stemmer.StemmerFactory(Stemmer.StemmerType.ORENGO);
			s.enableCaching(1000);
			s.ignore(new string[]{"a","e"});
			Console.WriteLine(s.getWordStem("extremamente"));
		}
	}
}
