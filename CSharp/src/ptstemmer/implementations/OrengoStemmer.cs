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
	/// Orengo Stemmer as defined in:
	/// V. Orengo and C. Huyck, "A stemming algorithm for the portuguese language," String Processing and Information Retrieval, 2001. SPIRE 2001. Proceedings.Eighth International Symposium on, 2001, pp. 186-193.
	/// Added extra stemming rules and exceptions found in:<br>
	/// http://www.inf.ufrgs.br/%7Earcoelho/rslp/integrando_rslp.html
	/// @author Pedro Oliveira
	/// </summary>
	public class OrengoStemmer: Stemmer
	{
		public OrengoStemmer()
		{
			readRulesFromXML();
		}

		public override String stemming(String word)
		{
			return algorithm(word);
		}

		private String algorithm(String st)
		{
			String aux, stem = st;
			char end = stem[stem.Length-1];	
			if(end == 's')
				stem = applyRules(stem, pluralreductionrules);
			end = stem[stem.Length-1];
			if(end == 'a' || end == 'ã')
				stem = applyRules(stem, femininereductionrules);
			stem = applyRules(stem, augmentativediminutivereductionrules);
			stem = applyRules(stem, adverbreductionrules);
			aux = stem;
			stem = applyRules(stem, nounreductionrules);
			if(aux.Equals(stem))
			{
				aux = stem;
				stem = applyRules(stem, verbreductionrules);
				if(aux.Equals(stem))
					stem = applyRules(stem, vowelremovalrules);
			}
			//stem = removeDiacritics(stem);		
			return stem;
		}

		private String applyRules(String st, SuffixTree<Rule> rules)
		{
			if(st.Length < rules.Properties["size"])	//If the word is smaller than the minimum stemming size of this step, ignore it
				return st;		
			
			List<Pair<String, Rule>> res = rules.getLongestSuffixesAndValues(st);
			for(int i=res.Count-1; i>=0; i--)
			{
				Pair<String, Rule> r = res[i];
				String suffix = r.First;
				Rule rule = r.Second;

				if(rules.Properties["exceptions"] == 1){	//Compare entire word with exceptions
					if(rule.exceptions.contains(st))
						break;
				}else{	//Compare only the longest suffix
					if(rule.exceptions.getLongestSuffixValue(st) == true)
						break;				
				}
				if(st.Length >= suffix.Length+rule.size)
					return st.Substring(0, st.Length-suffix.Length)+rule.replacement;
			}
			return st;
		}

		public void readRulesFromXML()
		{
			XmlDocument doc = new XmlDocument();
			try{
				doc.Load(System.Reflection.Assembly.GetExecutingAssembly().GetManifestResourceStream("OrengoStemmerRules.xml"));
			}catch(Exception e) {
				throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file.",e);}
			
			XmlElement root = doc.DocumentElement;
			XmlAttribute val,val2,val3;
			
			foreach (XmlNode step in root.ChildNodes)
			{
				val = step.Attributes["name"];
				if(val == null)
					throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid step.");
				String stepName = val.Value;
				SuffixTree<Rule> suffixes = new SuffixTree<Rule>();
				setProperty(suffixes,"size",0,step);
				setProperty(suffixes,"exceptions",0,step);
				
				foreach (XmlNode rule in step.ChildNodes)
				{
					val = rule.Attributes["suffix"];
					val2 = rule.Attributes["replacement"];
					val3 = rule.Attributes["size"];
					
					if(val == null || val2 == null || val3 == null)
						throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid rule in "+stepName+".");
					
					String suffix = val.Value;				
					String replacement = val2.Value;
					int size = 0;
					try{
						size = Convert.ToInt32(val3.Value);
					}catch(Exception e) {throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Missing or invalid rules properties on step "+stepName+".", e);}
					
					List<String> exceptions = new List<String>();
					
					foreach (XmlNode exception in rule.ChildNodes)
					{
						if(exception.LocalName.Equals("exception") && exception.HasChildNodes)
							exceptions.Add(exception.FirstChild.Value);
						else
							throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid exception in step "+stepName+", rule "+suffix+".");
					}
					
					Rule r = new Rule(size,replacement,exceptions.ToArray());
					suffixes.addSuffix(suffix,r);

				}

				if(stepName.Equals("pluralreduction"))
					pluralreductionrules = suffixes;
				else if(stepName.Equals("femininereduction"))
					femininereductionrules = suffixes;
				else if(stepName.Equals("adverbreduction"))
					adverbreductionrules = suffixes;
				else if(stepName.Equals("augmentativediminutivereduction"))
					augmentativediminutivereductionrules = suffixes;
				else if(stepName.Equals("nounreduction"))
					nounreductionrules = suffixes;
				else if(stepName.Equals("verbreduction"))
					verbreductionrules = suffixes;
				else if(stepName.Equals("vowelremoval"))
					vowelremovalrules = suffixes;
			}
			
			if(pluralreductionrules == null || femininereductionrules == null || adverbreductionrules == null ||
			augmentativediminutivereductionrules == null || nounreductionrules == null || verbreductionrules == null ||
			vowelremovalrules == null)
				throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Missing steps.");	
		}
		
		private void setProperty(SuffixTree<Rule> tree, String propertyName, int defaultValue, XmlNode node)
		{
			XmlAttribute val = node.Attributes[propertyName];
			if(val != null)
				tree.Properties[propertyName] = Convert.ToInt32(val.Value);
			else
				tree.Properties[propertyName] = defaultValue;
		}
	
		private SuffixTree<Rule> pluralreductionrules;
		private SuffixTree<Rule> femininereductionrules;
		private SuffixTree<Rule> adverbreductionrules;
		private SuffixTree<Rule> augmentativediminutivereductionrules;
		private SuffixTree<Rule> nounreductionrules;
		private SuffixTree<Rule> verbreductionrules;
		private SuffixTree<Rule> vowelremovalrules;
		
	}
	
	public class Rule
	{
		public int size;
		public String replacement;
		public SuffixTree<bool> exceptions;

		public Rule(int size, String replacement, String[] exceptions){
			this.size = size;
			if(replacement != null)
				this.replacement = replacement;
			else
				this.replacement = "";
			if(exceptions.Length == 0)
				this.exceptions  = new SuffixTree<bool>();
			else
				this.exceptions = new SuffixTree<bool>(true, exceptions);
		}	
	}
}
