package edu.htwm.fmalek.cityinfo.services.rest.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import edu.htwm.fmalek.cityinfo.services.core.Attraktion;
import edu.htwm.fmalek.cityinfo.services.core.City;

public class Client 
{
	private HttpClient httpClient;
	private String baseUri;
	private final String xml = "application/xml";
	private final String json ="application/json";
	
	public Client(String baseUri)
	{
		this.baseUri = baseUri;
		this.httpClient = new HttpClient();
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) 
	{
		this.baseUri = baseUri;
	}

	private Object unmarshallfromXML(Reader in,JAXBContext jaxbContext) throws JAXBException
	{
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller.unmarshal(in);
	}
	
	public City addCity(String name) throws HttpException, IOException, JAXBException
	{
		PostMethod postMethod = new PostMethod(getBaseUri()+"/cities");
		postMethod.addParameter("name",name);
		postMethod.setRequestHeader("Accept",xml);

		int responseCode = httpClient.executeMethod(postMethod);
		System.out.println("schaun wir mal in den Responsecode:" +responseCode);
		String response = postMethod.getResponseBodyAsString();
		System.out.println("schaun wir mal in den Response String:" +response);
		JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
		return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
	}
	
	
	public City getCity(int cityId) throws HttpException, IOException, JAXBException
	{
		GetMethod getMethod = new GetMethod(getBaseUri()+"/cities/"+cityId);
		getMethod.setRequestHeader("Accept",xml);
		int responseCode = httpClient.executeMethod(getMethod);
		String response = getMethod.getResponseBodyAsString();
		JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
		return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
		
	}
	
	public int deleteCity(int cityid) throws HttpException, IOException 
	{
		DeleteMethod deleteMethod = new DeleteMethod(getBaseUri() + "/cities/" + cityid);
		deleteMethod.setRequestHeader("Accepct",xml);
		int responseCode = this.httpClient.executeMethod(deleteMethod);

		return responseCode;
	}

	public Attraktion addAttraktion(int cityId,Attraktion Attraktion) throws HttpException, IOException, JAXBException
	{
		PutMethod putMethod = new PutMethod(getBaseUri()+"/cities/"+cityId+"/attr/?");
		putMethod.setQueryString("name="+Attraktion.getName()+"&lage="+Attraktion.getLage()+"&typ="+Attraktion.getTyp()+"&besch="+Attraktion.getBeschreibung());
		int responseCode = this.httpClient.executeMethod(putMethod);
		System.out.println("schaun wir mal in den Responsecode:" +responseCode);
		String response = putMethod.getResponseBodyAsString();
		System.out.println("schaun wir mal in den Response String:" +response);
		JAXBContext jaxbContext = JAXBContext.newInstance(Attraktion.class);
		return (Attraktion)unmarshallfromXML(new StringReader((response)),jaxbContext);

	}
	
	public int deleteAttraktion(int cityId, String aname) throws HttpException, IOException
	{
		DeleteMethod deleteMethod = new DeleteMethod(getBaseUri() + "/cities/" + cityId + "/attr/" + aname);
		deleteMethod.setRequestHeader("Accepct",xml);
		int responseCode = this.httpClient.executeMethod(deleteMethod);
		return responseCode;
	}
	public City getTypCity(String typ) throws HttpException, IOException, JAXBException 
	{
		GetMethod getMethod = new GetMethod(getBaseUri()+"/cities/"+"typ/"+typ);
		getMethod.setRequestHeader("Accept",xml);
		int responseCode = httpClient.executeMethod(getMethod);
		String response = getMethod.getResponseBodyAsString();
		JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
		return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
	}
		public City getTypAttr(int cityId,String typ) throws IOException, JAXBException
		{
			GetMethod getMethod = new GetMethod(getBaseUri()+"/cities/"+cityId+"/typ/"+typ);
			getMethod.setRequestHeader("Accept",xml);
			int responseCode = httpClient.executeMethod(getMethod);
			String response = getMethod.getResponseBodyAsString();
			JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
			return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
		}
		public City getempfcity() throws JAXBException, IOException
		{
			GetMethod getMethod = new GetMethod(getBaseUri()+"/cities/"+"empf/");
			getMethod.setRequestHeader("Accept",xml);
			int responseCode = httpClient.executeMethod(getMethod);
			String response = getMethod.getResponseBodyAsString();
			JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
			return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
		}
		public City getempfattr(int cityId) throws HttpException, IOException, JAXBException 
		{
			GetMethod getMethod = new GetMethod(getBaseUri()+"/cities/"+cityId+"/empf/");
			getMethod.setRequestHeader("Accept",xml);
			int responseCode = httpClient.executeMethod(getMethod);
			String response = getMethod.getResponseBodyAsString();
			JAXBContext jaxbContext = JAXBContext.newInstance(City.class);
			return (City)unmarshallfromXML(new StringReader((response)),jaxbContext);
		}
	
}
