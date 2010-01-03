/**
 * PTStemmer - Java Stemming toolkit for the Portuguese leanguage (C) 2008 Pedro Oliveira
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

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ptstemmer.Stemmer;
import ptstemmer.exceptions.PTStemmerException;
import ptstemmer.support.datastructures.Pair;
import ptstemmer.support.datastructures.SuffixTree;


/**
 * Orengo Stemmer as defined in:<br>
 * V. Orengo and C. Huyck, "A stemming algorithm for the portuguese language," String Processing and Information Retrieval, 2001. SPIRE 2001. Proceedings.Eighth International Symposium on, 2001, pp. 186-193.<br>
 * Augmented using the rules present in:<br>
 * http://www.inf.ufrgs.br/%7Earcoelho/rslp/integrando_rslp.html
 * @author Pedro Oliveira
 *
 */
public class OrengoStemmer extends Stemmer{

	private class Rule
	{
		public int size;
		public String replacement;
		public SuffixTree<Boolean> exceptions;

		public Rule(int size, String replacement, String[] exceptions){
			this.size = size;
			if(replacement != null)
				this.replacement = replacement;
			else
				this.replacement = "";
			if(exceptions == null)
				this.exceptions  = new SuffixTree<Boolean>();
			else
				this.exceptions = new SuffixTree<Boolean>(true, exceptions);
		}	
	}

	public OrengoStemmer() throws PTStemmerException
	{
		readRulesFromXML();
	}

	@Override
	protected String stemming(String word)
	{		
		return algorithm(word);
	}

	private String algorithm(String st)
	{
		String aux, stem = st;
		char end = stem.charAt(stem.length()-1);	
		if(end == 's')
			stem = applyRules(stem, pluralreductionrules);
		end = stem.charAt(stem.length()-1);
		if(end == 'a' || end == 'ã')
			stem = applyRules(stem, femininereductionrules);
		stem = applyRules(stem, augmentativediminutivereductionrules);
		stem = applyRules(stem, adverbreductionrules);
		aux = stem;
		stem = applyRules(stem, nounreductionrules);
		if(aux.compareTo(stem)==0)
		{
			aux = stem;
			stem = applyRules(stem, verbreductionrules);
			if(aux.compareTo(stem)==0)
				stem = applyRules(stem, vowelremovalrules);
		}
		//stem = removeDiacritics(stem);		
		return stem;
	}

	private String applyRules(String st, SuffixTree<Rule> rules)
	{
		if(st.length() < rules.getProperty("size"))	//If the word is smaller than the minimum stemming size of this step, ignore it
			return st;
		
		List<Pair<String, Rule>> res = rules.getLongestSuffixesAndValues(st);

		for(int i=res.size()-1; i>=0; i--)
		{
			Pair<String, Rule> r = res.get(i);
			String suffix = r.a;
			Rule rule = r.b;

			//System.out.println(st+"\t"+suffix+"\t"+rule.replacement+"\t"+rule.size+"\t"+rule.exceptions);

			if(rules.getProperty("exceptions") == 1){	//Compare entire word with exceptions
				if(rule.exceptions.contains(st))
					break;
			}else{	//Compare only the longest suffix
				if(rule.exceptions.getLongestSuffixValue(st) != null)
					break;				
			}
			if(st.length() >= suffix.length()+rule.size)
				return st.substring(0, st.length()-suffix.length())+rule.replacement;
		}
		return st;
	}

	private String removeDiacritics(String st)
	{
		String res = st;
		res = Normalizer.normalize(res, Normalizer.Form.NFD);
		res = res.replaceAll("[^\\p{ASCII}]","");
		return res;
	}

	public void readRulesFromXML() throws PTStemmerException
	{
		DocumentBuilder builder;
		Document document;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(new File("OrengoStemmerRules.xml"));
		} catch (Exception e) {
			throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file.", e);
		}

		Element root = document.getDocumentElement();

		for(Element step: getChilds(root))
		{
			if(!step.hasAttribute("name"))
				throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid step.");

			String stepName = step.getAttribute("name");
			SuffixTree<Rule> suffixes = new SuffixTree<Rule>();
			setProperty(suffixes, "size", 0, step);
			setProperty(suffixes, "exceptions", 0, step);

			for(Element rule: getChilds(step))
			{
				if(!rule.hasAttribute("size") || !rule.hasAttribute("replacement") || !rule.hasAttribute("suffix"))
					throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid rule in "+stepName+".");


				String suffix = rule.getAttribute("suffix");

				List<String> exceptions = new ArrayList<String>();
				for(Element exception: getChilds(rule))
				{

					if(! exception.getTagName().equals("exception") || !exception.hasChildNodes())
						throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Invalid exception in step "+stepName+", rule "+suffix+".");
					exceptions.add(exception.getChildNodes().item(0).getNodeValue());
				}
				Rule r;
				try
				{
					r = new Rule(Integer.parseInt(rule.getAttribute("size")), rule.getAttribute("replacement"), exceptions.toArray(new String[exceptions.size()]));
				}catch (NumberFormatException e) {
					throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Missing or invalid rules properties on step "+stepName+".", e);
				}
				suffixes.addSuffix(suffix, r);
			}

			if(stepName.equals("pluralreduction"))
				pluralreductionrules = suffixes;
			else if(stepName.equals("femininereduction"))
				femininereductionrules = suffixes;
			else if(stepName.equals("adverbreduction"))
				adverbreductionrules = suffixes;
			else if(stepName.equals("augmentativediminutivereduction"))
				augmentativediminutivereductionrules = suffixes;
			else if(stepName.equals("nounreduction"))
				nounreductionrules = suffixes;
			else if(stepName.equals("verbreduction"))
				verbreductionrules = suffixes;
			else if(stepName.equals("vowelremoval"))
				vowelremovalrules = suffixes;
		}

		if(pluralreductionrules == null || femininereductionrules == null || adverbreductionrules == null ||
				augmentativediminutivereductionrules == null || nounreductionrules == null || verbreductionrules == null ||
				vowelremovalrules == null)
			throw new PTStemmerException("Problem while parsing Orengo's XML stemming rules file: Missing steps.");
	}

	private void setProperty(SuffixTree<?> tree, String propertyName, Integer defaultValue, Element element)
	{
		if(element.hasAttribute(propertyName))
		{
			try
			{
				int value = Integer.parseInt(element.getAttribute(propertyName));
				tree.setProperty(propertyName, value);
			}catch (NumberFormatException e) {
				tree.setProperty(propertyName, defaultValue);
			}
		}
		else
			tree.setProperty(propertyName, defaultValue);
	}

	private Collection<Element> getChilds(Element e)
	{
		List<Element> childs = new ArrayList<Element>();
		if(e == null)
			return childs;

		NodeList cnodes = e.getChildNodes();
		for(int i=0; i< cnodes.getLength(); i++)
		{
			Node cnode = cnodes.item(i);
			if(cnode instanceof Element)
			{
				Element child = (Element)cnode;
				childs.add(child);
			}
		}
		return childs;
	}

	private SuffixTree<Rule> pluralreductionrules;
	private SuffixTree<Rule> femininereductionrules;
	private SuffixTree<Rule> adverbreductionrules;
	private SuffixTree<Rule> augmentativediminutivereductionrules;
	private SuffixTree<Rule> nounreductionrules;
	private SuffixTree<Rule> verbreductionrules;
	private SuffixTree<Rule> vowelremovalrules;

}
