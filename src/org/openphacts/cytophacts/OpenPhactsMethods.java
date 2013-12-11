package org.openphacts.cytophacts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import org.codehaus.jackson.map.ObjectMapper;

public class OpenPhactsMethods 
{
	/**
	 * @param args
	 * @throws IOException 
	 */

	OpenPhactsMethods()
	{
	}
	
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException 
	{
		new OpenPhactsMethods().run();
	}
	
	/**
	 * Get the SMILES string for a compound by name
	 * @param chemicalName
	 * @return SMILES string.
	 * @throws IOException 
	 */
	
	public List<String> getChemicalFromText(String compoundName) throws IOException{
		URL url = new URL("https://beta.openphacts.org/1.3/search/byTag?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&q=lactate&uuid=07a84994-e464-4bbf-812a-a4b96fa3d197&_format=tsv");
		return getOpenPhacts(url);
	}
	
	public List<String> getConceptWikiUUID(String uri) throws IOException, ParserConfigurationException, SAXException
	{
		List<String> result = new ArrayList<String>();
		try
		{
			URL url = new URL("http://openphacts.cs.man.ac.uk:9092/QueryExpander/mapUri?Uri="+URLEncoder.encode(uri, "UTF-8")+"&format=application/xml&targetUriPattern=http://www.conceptwiki.org/concept/$id");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document results = db.parse(url.openStream());
			NodeList nl = results.getElementsByTagName("targetUri");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				Node n = nl.item(i);
				String id = n.getTextContent();
				result.add (id);
			}
			return result;
		}
		catch (Exception e)
		{
			//TODO: better error handling.
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	
	public String getSMILES (String chemicalUrl) throws IOException
	{
		URL url = new URL("https://beta.openphacts.org/1.3/compound?uri="+URLEncoder.encode(chemicalUrl, "UTF-8")+"&app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&_format=tsv");
		return getOpenPhacts(url).get(1).split("\t")[20];
	}
	
	private static class StructureSearchResult implements Comparable<StructureSearchResult>
	{
		String id;
		Number relevance;
		
		@Override
		public int compareTo(StructureSearchResult o) 
		{
			return Double.compare(o.relevance.doubleValue(), relevance.doubleValue()); 
		}

	}
	
	/**
	 * Use substructure search to get a list of similar compounds.
	 * @param SMILES, for example "CC(=O)Oc1ccccc1C(=O)O"
	 * @return list of compounds IDS, sorted by relevance, most relevant first
	 * @throws IOException 
	 */
	public List<String> getSubStructureSearch(String SMILES) throws IOException
	{
		URL url = new URL("https://beta.openphacts.org/1.3/structure/substructure?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&searchOptions.Molecule="+URLEncoder.encode(SMILES, "UTF-8")+"&_format=json");
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		InputStream is = url.openStream();
		Map<String, ?> result = mapper.readValue(is, Map.class);
		
		List<Map<String, ?>> searchResults = (List<Map<String, ?>>)((Map<?, ?>)((Map<?, ?>)result.get("result")).get("primaryTopic")).get("result");
		
		List<StructureSearchResult> searchResult = new ArrayList<StructureSearchResult>();
		for (Map<String, ?> o : searchResults)
		{
			StructureSearchResult ssr = new StructureSearchResult();
			ssr.id = (String)o.get("_about");
			ssr.relevance = (Number)o.get("relevance");
			searchResult.add (ssr);
		}
		// Sort by relevance
		Collections.sort (searchResult);
		
		List<String> ids = new ArrayList<String>();
		for (StructureSearchResult ssr : searchResult)
		{
			System.out.println (ssr.id + " " + ssr.relevance);
			ids.add (ssr.id);
		}
		
		return ids;
	}
	
	/**
	 * Get a list of pathways for a given organism
	 * @param organismName
	 * @return pathway ids.
	 * @throws IOException 
	 */
	public List<String> getOpenPhacts(URL url) throws IOException{
		List<String> result = new ArrayList<String>();
		
		InputStream is;
		try
		{
		URLConnection con = url.openConnection();
		is = con.getInputStream();
		}
		catch (FileNotFoundException ex)
		{
			// 404 response is equivalent to 0 results
			// OpenPhacts API does not distinguish between this and other error conditions.
			return result;
		}
		
		InputStreamReader inStream = new InputStreamReader(is); 
		BufferedReader buff= new BufferedReader(inStream);
		String nextLine;
		// Read and print the lines from index.html
		while (true){
			nextLine = buff.readLine();  
			if (nextLine !=null){
				result.add(nextLine); 
			}
			else{
				break;
			} 
		}
		
		return result;
	}
	
	public List<String> getPathways(String organismName) throws IOException
	{
		URL url = new URL("http://ops2.few.vu.nl/pathways?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism="+URLEncoder.encode(organismName, "UTF-8")+"&_format=tsv");
		return getOpenPhacts(url);
	}

	/**
	 * Get a list of pathways for a given species that contain a given compound.
	 * @param compoundID
	 * @param organismName
	 * @return a list of pathway identifiers.
	 * @throws IOException 
	 */
	public List<String> getPathwaysForCompound (String compoundURI, String organismName) throws IOException
	{
		URL url = new URL("https://beta.openphacts.org/1.3/pathways/byCompound?uri="+URLEncoder.encode(compoundURI, "UTF-8")+"&app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism="+URLEncoder.encode(organismName, "UTF-8")+"&_format=tsv");
		return getOpenPhacts(url);
	}

	public void run() throws IOException, ParserConfigurationException, SAXException
	{
		/*URL url = new URL("http://ops2.few.vu.nl/pathways?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism=Homo+sapiens&_format=tsv");
		InputStream is = url.openStream();
		InputStreamReader inStream = new InputStreamReader(is); 
		BufferedReader buff= new BufferedReader(inStream);
		String nextLine;
		// Read and print the lines from index.html
		while (true){
			nextLine =buff.readLine();  
			if (nextLine !=null){
				System.out.println(nextLine); 
			}
			else{
				break;
			} 
		}*/
//		List<String> pathwayList = getPathways("Homo sapiens");
//		for (String pwy : pathwayList)
//		{
//			System.out.println(pwy);
//		}
//		List<String> substructureList = getSubStructureSearch("CC(=O)Oc1ccccc1C(=O)O");
//		for (String ssl : substructureList)
//		{
//			System.out.println(ssl);
//		}
//		
//		List<String> pwyByCompound = getPathwaysForCompound("http://ops.rsc.org/OPS1536399", "Homo sapiens");
//		for (String x : pwyByCompound)
//		{
//			System.out.println(x); 
//		}
//		
		List<String> mapIds = getConceptWikiUUID("http://ops.rsc.org/OPS1536399");
		for (String x : mapIds)
		{
			System.out.println(x); 
		}
	}

}
