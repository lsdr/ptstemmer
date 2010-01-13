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
using ptstemmer.support.datastructures;
using System.Collections.Generic;

namespace ptstemmer.implementations
{

	/// <summary>
	/// Porter Stemmer as defined in:
	/// http://snowball.tartarus.org/algorithms/portuguese/stemmer.html
	/// </summary>	
	public class PorterStemmer: Stemmer
	{
		
		public override String stemming(String word)
		{
			return algorithm(word);
		}
	
		private String algorithm(String st)
		{		
			st = processNasalidedVowels(st);
			String stem = st;
			String r1 = findR(stem);
			String r2 = findR(r1);
			String rv = findRV(stem);

			stem = step1(stem,r1,r2,rv);
		
			if(stem.Equals(st))
				stem = step2(stem,r1,r2,rv);
			else
			{
				r1 = findR(stem);
				r2 = findR(r1);
				rv = findRV(stem);
			}

			if(!stem.Equals(st))
			{
				r1 = findR(stem);
				r2 = findR(r1);
				rv = findRV(stem);
				stem = step3(stem,r1,r2,rv);
			}
			else
				stem = step4(stem,r1,r2,rv);		

			if(!stem.Equals(st))
			{
				r1 = findR(stem);
				r2 = findR(r1);
				rv = findRV(stem);
			}
			
			stem = step5(stem,r1,r2,rv);

			stem = deprocessNasalidedVowels(stem);
			
			return stem;
		}
		
		private String step1(String st, String r1, String r2, String rv)
		{	
			if((suffix = suffix1.getLongestSuffix(r2)).Length>0)	//Rule 1
				return st.Substring(0, st.Length-suffix.Length);
			
			if((suffix = suffix2.getLongestSuffix(r2)).Length>0)	//Rule 2
				return st.Substring(0, st.Length-suffix.Length)+"log";
			
			if((suffix = suffix3.getLongestSuffix(r2)).Length>0)	//Rule 3
				return st.Substring(0, st.Length-suffix.Length)+"u";

			if((suffix = suffix4.getLongestSuffix(r2)).Length>0)	//Rule 4
				return st.Substring(0, st.Length-suffix.Length)+"ente";
			
			if((suffix = suffix5.getLongestSuffix(r1)).Length>0)	//Rule 5
				{
					st = st.Substring(0, st.Length-suffix.Length);
					if(st.EndsWith("iv") && r2.EndsWith("iv"+suffix))
					{
						st = st.Substring(0,st.Length-2);
						if(st.EndsWith("at")&& r2.EndsWith("ativ"+suffix))
							st = st.Substring(0,st.Length-2);
					}
					else if(st.EndsWith("os") && r2.EndsWith("os"+suffix))
						st = st.Substring(0,st.Length-2);
					else if(st.EndsWith("ic") && r2.EndsWith("ic"+suffix))
						st = st.Substring(0,st.Length-2);
					else if(st.EndsWith("ad") && r2.EndsWith("ad"+suffix))
						st = st.Substring(0,st.Length-2);
					return st;
				}
			
			if((suffix = suffix6.getLongestSuffix(r2)).Length>0)	//Rule 6
				{
					st = st.Substring(0, st.Length-suffix.Length);
					if(st.EndsWith("ante") && r2.EndsWith("ante"+suffix))
						st = st.Substring(0,st.Length-4);
					else if(st.EndsWith("avel") && r2.EndsWith("avel"+suffix))
						st = st.Substring(0,st.Length-4);
					else if(st.EndsWith("ível") && r2.EndsWith("ível"+suffix))
						st = st.Substring(0,st.Length-4);
					return st;
				}
			
			if((suffix = suffix7.getLongestSuffix(r2)).Length>0)	//Rule 7
				{
					st = st.Substring(0, st.Length-suffix.Length);
					if(st.EndsWith("abil") && r2.EndsWith("abil"+suffix))
						st = st.Substring(0,st.Length-4);
					else if(st.EndsWith("ic") && r2.EndsWith("ic"+suffix))
						st = st.Substring(0,st.Length-2);
					else if(st.EndsWith("iv") && r2.EndsWith("iv"+suffix))
						st = st.Substring(0,st.Length-2);
					return st;
				}
			
			if((suffix = suffix8.getLongestSuffix(r2)).Length>0)	//Rule 8
				{
					st = st.Substring(0, st.Length-suffix.Length);
					if(st.EndsWith("at") && r2.EndsWith("at"+suffix))
						st = st.Substring(0,st.Length-2);
					return st;
				}
			
			if((suffix = suffix9.getLongestSuffix(rv)).Length>0)	//Rule 9
				{
					if(st.EndsWith("e"+suffix))
						st = st.Substring(0,st.Length-suffix.Length)+"ir";
					return st;
				}	
			return st;
		}
		
		private String step2(String st, String r1, String r2, String rv)
		{
			if((suffix = suffixv.getLongestSuffix(rv)).Length>0)	//Rule 1
				return st.Substring(0, st.Length-suffix.Length);
			return st;
		}
		
		private String step3(String st, String r1, String r2, String rv)
		{
			if(rv.EndsWith("i")&&st.EndsWith("ci")) 	//Rule 1
				return st.Substring(0, st.Length-1);
			else
				return st;
		}
		
		private String step4(String st, String r1, String r2, String rv)
		{
			if((suffix = suffixr.getLongestSuffix(rv)).Length>0)	//Rule 1
					return st.Substring(0,st.Length-suffix.Length);
			return st;
		}
		
		private String step5(String st, String r1, String r2, String rv)
		{
			if((suffix = suffixf.getLongestSuffix(rv)).Length>0)	//Rule 1
				{
					st = st.Substring(0,st.Length-suffix.Length);
					if(st.EndsWith("gu") && rv.EndsWith("u"+suffix))
						st = st.Substring(0,st.Length-1);
					else if(st.EndsWith("ci") && rv.EndsWith("i"+suffix))
						st = st.Substring(0,st.Length-1);
					return st;
				}

			if(st.EndsWith("ç"))
				st = st.Substring(0,st.Length-1)+"c";
			return st;
		}

		
		private String findR(String st)
		{
			for(int i = 0; i< st.Length-1; i++)
				if(vowels.Contains(st[i]))
					if(!vowels.Contains(st[i+1]))
						return st.Substring(i+2);		
			return "";
		}
		
		private String findRV(String st)
		{
			if(st.Length>2)
			{
				if(!vowels.Contains(st[1]))
				{
					for(int i=2; i<st.Length-1; i++)
						if(vowels.Contains(st[i]))
							return st.Substring(i+1);
				}
				else if(vowels.Contains(st[0]) && vowels.Contains(st[1]))
				{
					for(int i=2; i<st.Length-1; i++)
						if(!vowels.Contains(st[i]))
							return st.Substring(i+1);
				}
				else
					return st.Substring(2);
			}
			return "";
		}
		
		private String processNasalidedVowels(String st)
		{
			st = st.Replace("ã", "a~");
			st = st.Replace("õ", "o~");
			return st;
		}
		
		private String deprocessNasalidedVowels(String st)
		{
			st = st.Replace("a~", "ã");
			st = st.Replace("o~", "õ");
			return st;
		}
		
		private String suffix;	//auxiliary variable
		
		
		private readonly static HashSet<char> vowels = new HashSet<char>(new char[]{'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'â', 'ê', 'ô'});	
		
		private readonly static SuffixTree<bool> suffix1 = new SuffixTree<bool>(true,new String[]{"amentos", "imentos", "amento", "imento", "adoras", "adores", "aço~es", "ismos", "istas", "adora", "aça~o", "antes", "ância", "ezas", "icos", "icas", "ismo", "ável", "ível", "ista", "osos", "osas", "ador", "ante", "eza", "ico", "ica", "oso", "osa"});
		private readonly static SuffixTree<bool> suffix2 = new SuffixTree<bool>(true,new String[]{"logías", "logía"});
		private readonly static SuffixTree<bool> suffix3 = new SuffixTree<bool>(true,new String[]{"uciones", "ución"});
		private readonly static SuffixTree<bool> suffix4 = new SuffixTree<bool>(true,new String[]{"ências", "ência"});
		private readonly static SuffixTree<bool> suffix5 = new SuffixTree<bool>(true,new String[]{"amente"});
		private readonly static SuffixTree<bool> suffix6 = new SuffixTree<bool>(true,new String[]{"mente"});
		private readonly static SuffixTree<bool> suffix7 = new SuffixTree<bool>(true,new String[]{"idades", "idade"});
		private readonly static SuffixTree<bool> suffix8 = new SuffixTree<bool>(true,new String[]{"ivas", "ivos", "iva", "ivo"});
		private readonly static SuffixTree<bool> suffix9 = new SuffixTree<bool>(true,new String[]{"iras", "ira"});
		private readonly static SuffixTree<bool> suffixv = new SuffixTree<bool>(true,new String[]{"aríamos", "eríamos", "iríamos", "ássemos", "êssemos", "íssemos", "aríeis", "eríeis", "iríeis", "ásseis", "ésseis", "ísseis", "áramos", "éramos", "íramos", "ávamos", "aremos", "eremos", "iremos", "ariam", "eriam", "iriam", "assem", "essem", "issem", "ara~o", "era~o", "ira~o", "arias", "erias", "irias", "ardes", "erdes", "irdes", "asses", "esses", "isses", "astes", "estes", "istes", "áreis", "areis", "éreis", "ereis", "íreis", "ireis", "áveis", "íamos", "armos", "ermos", "irmos", "aria", "eria", "iria", "asse", "esse", "isse", "aste", "este", "iste", "arei", "erei", "irei", "aram", "eram", "iram", "avam", "arem", "erem", "irem", "ando", "endo", "indo", "adas", "idas", "arás", "aras", "erás", "eras", "irás", "avas", "ares", "eres", "ires", "íeis", "ados", "idos", "ámos", "amos", "emos", "imos", "iras", "ada", "ida", "ará", "ara", "erá", "era", "irá", "ava", "iam", "ado", "ido", "ias", "ais", "eis", "ira", "ia", "ei", "am", "em", "ar", "er", "ir", "as", "es", "is", "eu", "iu", "ou"});
		private readonly static SuffixTree<bool> suffixr = new SuffixTree<bool>(true,new String[]{"os", "a", "i", "o", "á", "í", "ó"});
		private readonly static SuffixTree<bool> suffixf = new SuffixTree<bool>(true,new String[]{"e", "é", "ê"});
	}
}
