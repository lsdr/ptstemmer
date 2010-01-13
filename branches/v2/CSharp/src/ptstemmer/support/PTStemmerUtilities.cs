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
using System.Text;
using System.Globalization;
using System.IO;
using System.Collections.Generic;

namespace ptstemmer.support
{
	
	
	public abstract class PTStemmerUtilities
	{
		/// <summary>
		/// Parse text file (one word per line) to List 
		/// </summary>
		/// <param name="filename">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="List`1"/>
		/// </returns>
		public static List<String> fileToList(String filename)
		{
			List<String> res = new List<String>();			
			try
			{
				using (TextReader reader = new StreamReader(filename))
				{
					String line;
					while((line = reader.ReadLine()) != null)
						res.Add(line.Trim().ToLower());
				}
			}catch(Exception e){
				Console.WriteLine(e.Message);
			}
			return res;
		}
		
		/// <summary>
		/// Remove diacritics (i.e., accents) from String
		/// </summary>
		/// <param name="word">
		/// A <see cref="String"/>
		/// </param>
		/// <returns>
		/// A <see cref="String"/>
		/// </returns>
		public static String removeDiacritics(String word)
		{
			String kdform =  word.Normalize(NormalizationForm.FormKD);
			StringBuilder sb = new StringBuilder();

			for(int i = 0; i < kdform.Length; i++) {
				UnicodeCategory uc = CharUnicodeInfo.GetUnicodeCategory(kdform[i]);
				if(uc != UnicodeCategory.NonSpacingMark)
					sb.Append(kdform[i]);
			}
			return sb.ToString();
		}
	}
}
