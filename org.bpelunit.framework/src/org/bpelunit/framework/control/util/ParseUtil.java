/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package org.bpelunit.framework.control.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;

public class ParseUtil {

	private static Map<String, Document> fParsedDocuments = new HashMap<String, Document>();
	private static Map<String, Definition> fParsedDefinitions = new HashMap<String, Definition>();

	/**
	 * Gets a JDOM Document from the given XML file. The returned Document
	 * object is not cached for future requests. Instead a new Document will be
	 * created for each request. Document will not be looked up in cached
	 * Documents as well.
	 * 
	 * @param filename
	 *            Absolute path of the XML file.
	 * @throws IOException
	 *             Encapsulates JDOMException
	 * @see #getJDOMDocument(String, boolean, boolean)
	 */
	public static Document getJDOMDocument(String filename) throws IOException {
		Document document;
		FileInputStream is = null;
		File file = new File(filename);

		try {
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(file);
			document = builder.build(is);
		} catch (JDOMException e) {
			throw new IOException("Error while reading document from file \""
					+ file.getAbsolutePath() + "\".", e);
		}

		return document;
	}

	/**
	 * Gets a JDOM Document from the given XML file.
	 * 
	 * @param filename
	 *            Absolute path of the XML file.
	 * @param searchCache
	 *            Searches the cache for Document. If found returns cached
	 *            Document if this flag is set to true.
	 * @param toCache
	 *            If sets to true put the the created Document object in the
	 *            cache for future lookups.
	 * @throws IOException
	 *             Encapsulates JDOMException
	 * @see #getJDOMDocument(String)
	 */
	public static Document getJDOMDocument(String filename,
			boolean searchCache, boolean toCache) throws IOException {
		Document document;
		FileInputStream is = null;
		File file = new File(filename);

		if (searchCache && (document = fParsedDocuments.get(filename)) != null) {
			return document;
		}

		try {
			SAXBuilder builder = new SAXBuilder();
			is = new FileInputStream(file);
			document = builder.build(is);
		} catch (JDOMException e) {
			throw new IOException("Error while reading document from file \""
					+ file.getAbsolutePath() + "\".", e);
		}

		if (toCache) {
			fParsedDocuments.put(filename, document);
		}

		return document;
	}

	/**
	 * Gets a WSDL Definition (WSDL4J API) from the given WSDL file.
	 * 
	 * @param filename
	 *            Absolute path of the WSDL file.
	 * @param searchCache
	 *            Searches the cache for Definition. If found returns cached
	 *            Definition if this flag is set to true.
	 * @param toCache
	 *            If set to true put the the created Definition object in the
	 *            cache for future lookups.
	 * @throws IOException
	 *             Encapsulates WSDLException, FileNotFoundException
	 * @see #getWsdlDefinition(String)
	 */
	public static Definition getWsdlDefinition(String filename,
			boolean searchCache, boolean toCache) throws IOException {
		File wsdl = new File(filename);

		Definition definition;
		if (searchCache
				&& (definition = fParsedDefinitions.get(filename)) != null) {
			return definition;
		}

		try {
			WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();

			InputSource source = new InputSource(new FileInputStream(wsdl));
			definition = reader.readWSDL(filename, source);

		} catch (WSDLException e) {
			throw new IOException(
					"Error while reading definition from WSDL file \""
							+ wsdl.getAbsolutePath() + "\".", e);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}

		if (toCache) {
			fParsedDefinitions.put(filename, definition);
		}

		return definition;
	}

	/**
	 * Gets a WSDL Definition (WSDL4J API) from the given WSDL file. The
	 * returned Definition object is not cached for future requests. Instead a
	 * new Definition will be created for each request. Definition will not be
	 * looked up in cached Definitions as well.
	 * 
	 * @param filename
	 *            Absolute path of the WSDL file.
	 * @throws IOException
	 *             Encapsulates WSDLException, FileNotFoundException
	 * @see #getWsdlDefinition(String, boolean, boolean)
	 */
	public static Definition getWsdlDefinition(String filename)
			throws IOException {

		File wsdl = new File(filename);
		Definition def;

		try {
			WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();

			InputSource source = new InputSource(new FileInputStream(wsdl));
			def = reader.readWSDL(filename, source);

		} catch (WSDLException e) {
			throw new IOException(
					"Error while reading definition from WSDL file \""
							+ wsdl.getAbsolutePath() + "\".", e);
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}

		return def;
	}
}
