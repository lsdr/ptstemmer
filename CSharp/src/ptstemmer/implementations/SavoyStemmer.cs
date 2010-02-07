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
using System.Xml;
using System.IO;
using System.Collections.Generic;
using ptstemmer.support.datastructures;
using ptstemmer.exceptions;
using ptstemmer;

namespace ptstemmer.implementations
{
	/// <summary>
	/// Savoy Stemmer as defined in:
	/// J. Savoy, "Light stemming approaches for the French, Portuguese, German and Hungarian languages," Proceedings of the 2006 ACM symposium on Applied computing,  Dijon, France: ACM, 2006, pp. 1031-1035.
	/// Implementation based on:
	/// http://members.unine.ch/jacques.savoy/clef/index.html
	/// </summary>
	public class SavoyStemmer: Stemmer
	{
		
		public SavoyStemmer()
		{
			readRulesFromXML();
		}
		
		public override String stemming(String word)
		{
			return algorithm(word);
		}

		private String algorithm(String word)
		{
			int length = word.Length-1;
		
			if(length> 2)
			{
				word = applyRules(word, pluralreductionrules);
				length = word.Length-1;
				
				if(length>5 && word[length]=='a')
					word = applyRules(word, femininereductionrules);
				
				length = word.Length-1;
				char lastChar = word[length];
				if(length> 3 && (lastChar=='a' || lastChar=='e' || lastChar=='o'))
					word = word.Substring(0,length);
				//word = applyRules(word, finalvowel);	//It's easier, simpler (and probably faster) to apply the rule without using the SuffixTree
				//word = removeDiacritics(word);
			}
			return word;
		}
		
		private String applyRules(String st, SuffixTree<SavoyRule> rules)
		{
			int length = st.Length-1;
			if(length < rules.Properties["size"])	//If the word is smaller than the minimum stemming size of this step, ignore it
				return st;		
			
			List<Pair<String, SavoyRule>> res = rules.getLongestSuffixesAndValues(st);
			for(int i=res.Count-1; i>=0; i--)
			{
				Pair<String, SavoyRule> r = res[i];
				String suffix = r.First;
				SavoyRule rule = r.Second;
				if(length > rule.size)
					return st.Substring(0, st.Length-suffix.Length)+rule.replacement;
			}
			return st;
		}
		
		public void readRulesFromXML()
		{
			XmlDocument doc = new XmlDocument();
			try{
				doc.Load(System.Reflection.Assembly.GetExecutingAssembly().GetManifestResourceStream("SavoyStemmerRules.xml"));
			}catch(Exception e) {
				throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file.",e);}
			
			XmlElement root = doc.DocumentElement;
			XmlAttribute val,val2,val3;
			
			foreach (XmlNode step in root.ChildNodes)
			{
				val = step.Attributes["name"];
				if(val == null)
					throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Invalid step.");
				String stepName = val.Value;
				SuffixTree<SavoyRule> suffixes = new SuffixTree<SavoyRule>();
				setProperty(suffixes,"size",0,step);
				
				foreach (XmlNode rule in step.ChildNodes)
				{
					val = rule.Attributes["suffix"];
					val2 = rule.Attributes["replacement"];
					val3 = rule.Attributes["size"];
					
					if(val == null || val2 == null || val3 == null)
						throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Invalid rule in "+stepName+".");
					
					String suffix = val.Value;				
					String replacement = val2.Value;
					int size = 0;
					try{
						size = Convert.ToInt32(val3.Value);
					}catch(Exception e) {throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Missing or invalid rules properties on step "+stepName+".", e);}

					SavoyRule r = new SavoyRule(size,replacement);
					suffixes.addSuffix(suffix,r);
				}

				if(stepName.Equals("pluralreduction"))
					pluralreductionrules = suffixes;
				else if(stepName.Equals("femininereduction"))
					femininereductionrules = suffixes;
				else if(stepName.Equals("finalvowel"))
					finalvowel = suffixes;
					
			}
			
			if(pluralreductionrules == null || femininereductionrules == null || finalvowel == null)
				throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Missing steps.");	
		}
		
		private void setProperty(SuffixTree<SavoyRule> tree, String propertyName, int defaultValue, XmlNode node)
		{
			XmlAttribute val = node.Attributes[propertyName];
			if(val != null)
				tree.Properties[propertyName] = Convert.ToInt32(val.Value);
			else
				tree.Properties[propertyName] = defaultValue;
		}
		
		private SuffixTree<SavoyRule> pluralreductionrules;
		private SuffixTree<SavoyRule> femininereductionrules;
		private SuffixTree<SavoyRule> finalvowel;
	}
	
	public class SavoyRule
	{
		public int size;
		public String replacement;

		public SavoyRule(int size, String replacement){
			this.size = size;
			this.replacement = replacement;
		}	
	}
}
