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

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ptstemmer.Stemmer;
import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.support.XMLUtils;
import ptstemmer.support.datastructures.Pair;
import ptstemmer.support.datastructures.SuffixTree;

/**
 * Savoy Stemmer as defined in:<br>
 * J. Savoy, "Light stemming approaches for the French, Portuguese, German and Hungarian languages," Proceedings of the 2006 ACM symposium on Applied computing,  Dijon, France: ACM, 2006, pp. 1031-1035<br>
 * Implementation based on:<br>
 * http://members.unine.ch/jacques.savoy/clef/index.html
 * @author Pedro Oliveira
 *
 */
public class SavoyStemmer extends Stemmer {

	private class Rule
	{
		public int size;
		public String replacement;
		public Rule(int size, String replacement) {
			this.size = size;
			this.replacement = replacement;
		}
	}

	public SavoyStemmer() throws PTStemmerException
	{
		parseXML();
	}

	@Override
	protected String stemming(String word) {
		return algorithm(word);
	}

	private String algorithm(String word)
	{
		int length = word.length()-1;

		if(length> 2)
		{
			word = applyRules(word, pluralreductionrules);
			length = word.length()-1;
			if(length>5 && word.charAt(length)=='a')
				word = applyRules(word, femininereductionrules);

			length = word.length()-1;
			char lastChar = word.charAt(length);
			if(length> 3 && (lastChar=='a' || lastChar=='e' || lastChar=='o'))
				word = word.substring(0,length);
			//word = applyRules(word, finalvowel);	//It's easier, simpler (and probably faster) to apply the rule without using the SuffixTree
			//word = removeDiacritics(word);
		}
		return word;
	}

	private String applyRules(String st, SuffixTree<Rule> rules)
	{
		int length = st.length()-1;
		if(length < rules.getProperty("size"))	//If the word is smaller than the minimum stemming size of this step, ignore it
			return st;

		List<Pair<String, Rule>> res = rules.getLongestSuffixesAndValues(st);

		for(int i=res.size()-1; i>=0; i--)
		{
			Pair<String, Rule> r = res.get(i);
			String suffix = r.a;
			Rule rule = r.b;

			if(length > rule.size)
				return st.substring(0, st.length()-suffix.length())+rule.replacement;
		}
		return st;
	}

	private void parseXML() throws PTStemmerException
	{
		DocumentBuilder builder;
		Document document;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(SavoyStemmer.class.getResourceAsStream("SavoyStemmerRules.xml"));
		} catch (Exception e) {
			throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file.", e);
		}

		Element root = document.getDocumentElement();

		for(Element step: XMLUtils.getChilds(root))
		{
			if(!step.hasAttribute("name"))
				throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Invalid step.");

			String stepName = step.getAttribute("name");
			SuffixTree<Rule> suffixes = new SuffixTree<Rule>();
			XMLUtils.setProperty(suffixes, "size", 0, step);

			for(Element rule: XMLUtils.getChilds(step))
			{
				if(!rule.hasAttribute("size") || !rule.hasAttribute("replacement") || !rule.hasAttribute("suffix"))
					throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Invalid rule in "+stepName+".");


				String suffix = rule.getAttribute("suffix");
				Rule r;
				try
				{
					r = new Rule(Integer.parseInt(rule.getAttribute("size")), rule.getAttribute("replacement"));
				}catch (NumberFormatException e) {
					throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Missing or invalid rules properties on step "+stepName+".", e);
				}
				suffixes.addSuffix(suffix, r);
			}

			if(stepName.equals("pluralreduction"))
				pluralreductionrules = suffixes;
			else if(stepName.equals("femininereduction"))
				femininereductionrules = suffixes;
			else if(stepName.equals("finalvowel"))
				finalvowel = suffixes;
		}
		if(pluralreductionrules == null || femininereductionrules == null || finalvowel == null)
			throw new PTStemmerException("Problem while parsing Savoy's XML stemming rules file: Missing steps.");
	}

	private SuffixTree<Rule> pluralreductionrules;
	private SuffixTree<Rule> femininereductionrules;
	private SuffixTree<Rule> finalvowel;
}
