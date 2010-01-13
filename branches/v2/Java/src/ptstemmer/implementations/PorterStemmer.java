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
package ptstemmer.implementations;

import java.util.Arrays;
import java.util.HashSet;

import ptstemmer.Stemmer;
import ptstemmer.support.datastructures.SuffixTree;


/**
 * Porter Stemmer as defined in:<br>
 * http://snowball.tartarus.org/algorithms/portuguese/stemmer.html
 * @author Pedro Oliveira
 *
 */
public class PorterStemmer extends Stemmer{

	@Override
	public String stemming(String word)
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
		if(stem.compareTo(st)==0)
			stem = step2(stem,r1,r2,rv);
		else
		{
			r1 = findR(stem);
			r2 = findR(r1);
			rv = findRV(stem);
		}

		if(stem.compareTo(st)!=0)
		{
			r1 = findR(stem);
			r2 = findR(r1);
			rv = findRV(stem);
			stem = step3(stem,r1,r2,rv);
		}
		else
			stem = step4(stem,r1,r2,rv);		
		if(stem.compareTo(st)!=0)
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
		if(!(suffix = suffix1.getLongestSuffix(r2)).isEmpty())	//Rule 1
			return st.substring(0, st.length()-suffix.length());

		if(!(suffix = suffix2.getLongestSuffix(r2)).isEmpty())	//Rule 2
			return st.substring(0, st.length()-suffix.length())+"log";

		if(!(suffix = suffix3.getLongestSuffix(r2)).isEmpty())	//Rule 3
			return st.substring(0, st.length()-suffix.length())+"u";

		if(!(suffix = suffix4.getLongestSuffix(r2)).isEmpty())	//Rule 4
			return st.substring(0, st.length()-suffix.length())+"ente";

		if(!(suffix = suffix5.getLongestSuffix(r1)).isEmpty())	//Rule 5
		{
			st = st.substring(0, st.length()-suffix.length());
			if(st.endsWith("iv") && r2.endsWith("iv"+suffix))
			{
				st = st.substring(0,st.length()-2);
				if(st.endsWith("at")&& r2.endsWith("ativ"+suffix))
					st = st.substring(0,st.length()-2);
			}
			else if(st.endsWith("os") && r2.endsWith("os"+suffix))
				st = st.substring(0,st.length()-2);
			else if(st.endsWith("ic") && r2.endsWith("ic"+suffix))
				st = st.substring(0,st.length()-2);
			else if(st.endsWith("ad") && r2.endsWith("ad"+suffix))
				st = st.substring(0,st.length()-2);
			return st;
		}

		if(!(suffix = suffix6.getLongestSuffix(r2)).isEmpty())	//Rule 6
		{
			st = st.substring(0, st.length()-suffix.length());
			if(st.endsWith("ante") && r2.endsWith("ante"+suffix))
				st = st.substring(0,st.length()-4);
			else if(st.endsWith("avel") && r2.endsWith("avel"+suffix))
				st = st.substring(0,st.length()-4);
			else if(st.endsWith("ível") && r2.endsWith("ível"+suffix))
				st = st.substring(0,st.length()-4);
			return st;
		}

		if(!(suffix = suffix7.getLongestSuffix(r2)).isEmpty())	//Rule 7
		{
			st = st.substring(0, st.length()-suffix.length());
			if(st.endsWith("abil") && r2.endsWith("abil"+suffix))
				st = st.substring(0,st.length()-4);
			else if(st.endsWith("ic") && r2.endsWith("ic"+suffix))
				st = st.substring(0,st.length()-2);
			else if(st.endsWith("iv") && r2.endsWith("iv"+suffix))
				st = st.substring(0,st.length()-2);
			return st;
		}

		if(!(suffix = suffix8.getLongestSuffix(r2)).isEmpty())	//Rule 8
		{
			st = st.substring(0, st.length()-suffix.length());
			if(st.endsWith("at") && r2.endsWith("at"+suffix))
				st = st.substring(0,st.length()-2);
			return st;
		}

		if(!(suffix = suffix9.getLongestSuffix(rv)).isEmpty())	//Rule 9
		{
			if(st.endsWith("e"+suffix))
				st = st.substring(0,st.length()-suffix.length())+"ir";
			return st;
		}	
		return st;
	}

	private String step2(String st, String r1, String r2, String rv)
	{
		if(!(suffix = suffixv.getLongestSuffix(rv)).isEmpty())	//Rule 1
			return st.substring(0, st.length()-suffix.length());
		return st;
	}

	private String step3(String st, String r1, String r2, String rv)
	{
		if(rv.endsWith("i")&&st.endsWith("ci")) 	//Rule 1
			return st.substring(0, st.length()-1);
		else
			return st;
	}

	private String step4(String st, String r1, String r2, String rv)
	{
		if(!(suffix = suffixr.getLongestSuffix(rv)).isEmpty())	//Rule 1
			return st.substring(0,st.length()-suffix.length());
		return st;
	}

	private String step5(String st, String r1, String r2, String rv)
	{
		if(!(suffix = suffixf.getLongestSuffix(rv)).isEmpty())	//Rule 1
		{
			st = st.substring(0,st.length()-suffix.length());
			if(st.endsWith("gu") && rv.endsWith("u"+suffix))
				st = st.substring(0,st.length()-1);
			else if(st.endsWith("ci") && rv.endsWith("i"+suffix))
				st = st.substring(0,st.length()-1);
			return st;
		}

		if(st.endsWith("ç"))
			st = st.substring(0,st.length()-1)+"c";
		return st;
	}


	private String findR(String st)
	{
		for(int i = 0; i< st.length()-1; i++)
			if(vowels.contains(st.charAt(i)))
				if(!vowels.contains(st.charAt(i+1)))
					return st.substring(i+2);		
		return "";
	}

	private String findRV(String st)
	{
		if(st.length()>2)
		{
			if(!vowels.contains(st.charAt(1)))
			{
				for(int i=2; i<st.length()-1; i++)
					if(vowels.contains(st.charAt(i)))
						return st.substring(i+1);
			}
			else if(vowels.contains(st.charAt(0)) && vowels.contains(st.charAt(1)))
			{
				for(int i=2; i<st.length()-1; i++)
					if(!vowels.contains(st.charAt(i)))
						return st.substring(i+1);
			}
			else
				return st.substring(2);	//TODO Fazer alteracao em C#
		}
		return "";
	}

	private String processNasalidedVowels(String st)
	{
		st = st.replaceAll("ã", "a~");
		st = st.replaceAll("õ", "o~");
		return st;
	}

	private String deprocessNasalidedVowels(String st)
	{
		st = st.replaceAll("a~", "ã");
		st = st.replaceAll("o~", "õ");
		return st;
	}

	private String suffix;	//auxiliary variable

	private final static HashSet<Character> vowels = new HashSet<Character>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'â', 'ê', 'ô'));	

	private final static SuffixTree<Boolean> suffix1 = new SuffixTree<Boolean>(true,"amentos", "imentos", "amento", "imento", "adoras", "adores", "aço~es", "ismos", "istas", "adora", "aça~o", "antes", "ância", "ezas", "icos", "icas", "ismo", "ável", "ível", "ista", "osos", "osas", "ador", "ante", "eza", "ico", "ica", "oso", "osa");
	private final static SuffixTree<Boolean> suffix2 = new SuffixTree<Boolean>(true,"logías", "logía");
	private final static SuffixTree<Boolean> suffix3 = new SuffixTree<Boolean>(true,"uciones", "ución");
	private final static SuffixTree<Boolean> suffix4 = new SuffixTree<Boolean>(true,"ências", "ência");
	private final static SuffixTree<Boolean> suffix5 = new SuffixTree<Boolean>(true,"amente");
	private final static SuffixTree<Boolean> suffix6 = new SuffixTree<Boolean>(true,"mente");
	private final static SuffixTree<Boolean> suffix7 = new SuffixTree<Boolean>(true,"idades", "idade");
	private final static SuffixTree<Boolean> suffix8 = new SuffixTree<Boolean>(true,"ivas", "ivos", "iva", "ivo");
	private final static SuffixTree<Boolean> suffix9 = new SuffixTree<Boolean>(true,"iras", "ira");
	private final static SuffixTree<Boolean> suffixv = new SuffixTree<Boolean>(true,"aríamos", "eríamos", "iríamos", "ássemos", "êssemos", "íssemos", "aríeis", "eríeis", "iríeis", "ásseis", "ésseis", "ísseis", "áramos", "éramos", "íramos", "ávamos", "aremos", "eremos", "iremos", "ariam", "eriam", "iriam", "assem", "essem", "issem", "ara~o", "era~o", "ira~o", "arias", "erias", "irias", "ardes", "erdes", "irdes", "asses", "esses", "isses", "astes", "estes", "istes", "áreis", "areis", "éreis", "ereis", "íreis", "ireis", "áveis", "íamos", "armos", "ermos", "irmos", "aria", "eria", "iria", "asse", "esse", "isse", "aste", "este", "iste", "arei", "erei", "irei", "aram", "eram", "iram", "avam", "arem", "erem", "irem", "ando", "endo", "indo", "adas", "idas", "arás", "aras", "erás", "eras", "irás", "avas", "ares", "eres", "ires", "íeis", "ados", "idos", "ámos", "amos", "emos", "imos", "iras", "ada", "ida", "ará", "ara", "erá", "era", "irá", "ava", "iam", "ado", "ido", "ias", "ais", "eis", "ira", "ia", "ei", "am", "em", "ar", "er", "ir", "as", "es", "is", "eu", "iu", "ou");
	private final static SuffixTree<Boolean> suffixr = new SuffixTree<Boolean>(true,"os", "a", "i", "o", "á", "í", "ó");
	private final static SuffixTree<Boolean> suffixf = new SuffixTree<Boolean>(true,"e", "é", "ê");
}
