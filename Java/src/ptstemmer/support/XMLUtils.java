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
package ptstemmer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ptstemmer.support.datastructures.SuffixTree;

public abstract class XMLUtils {
	
	public static void setProperty(SuffixTree<?> tree, String propertyName, Integer defaultValue, Element element)
	{
		try
		{
			int value = Integer.parseInt(element.getAttribute(propertyName));
			tree.setProperty(propertyName, value);
		}catch (Exception e) {
			tree.setProperty(propertyName, defaultValue);
		}
	}

	public static Collection<Element> getChilds(Element e)
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
}
