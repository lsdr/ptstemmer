/**
 * PTStemmer - Java Stemming toolkit for the Portuguese language (C) 2008 Pedro Oliveira
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
package ptstemmer.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Create a HashSet from a text file (one element per line)
 * @author Pedro Oliveira
 *
 */
public class ReadFileToHashSet {

	private final static ReadFileToHashSet INSTANCE = new ReadFileToHashSet();

	public static ReadFileToHashSet getInstance()
	{
		return INSTANCE;
	}

	public HashSet<String> fileToHash(String filename)
	{
		HashSet<String> res = new HashSet<String>();
		String aux;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while((aux=in.readLine())!=null)
				res.add(aux.trim().toLowerCase());

		} catch (IOException e) {
			System.out.println("Problems opening file "+filename);
		}
		return res;
	}
}
