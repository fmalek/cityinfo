package edu.htwm.fmalek.cityinfo.services.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import edu.htwm.fmalek.cityinfo.services.core.Attraktion;
import edu.htwm.fmalek.cityinfo.services.core.City;

public class ClientTest  {

	private String baseUri = "http://localhost:8080";
	/*
	 * Jetty Server muss für Client Tests gestartet sein.
	 */
	@Test
	public final void CityanlegenGettenLoeschen() throws HttpException, IOException, JAXBException 
	{
		Client Client = new Client(baseUri);
		City addcity = Client.addCity("newcity");
		assertNotNull(addcity);
		City getcity = Client.getCity(addcity.getId());
		assertNotNull(getcity);
		int responseasd = Client.deleteCity(addcity.getId());
		assertEquals(200,responseasd);
		
	}

	@Test
	public final void Attraktionhinzufuegenundloeschen() throws HttpException, IOException, JAXBException 
	{
		Client Client = new Client(baseUri);
		City addcity = Client.addCity("newcity");
		Attraktion newAttraktion = new Attraktion("asd","asd",3,"asd");
		Attraktion testAttraktion = Client.addAttraktion(addcity.getId(), newAttraktion);
		assertEquals(newAttraktion.getName(),testAttraktion.getName());
		assertEquals(newAttraktion.getLage(),testAttraktion.getLage());
		assertEquals(newAttraktion.getTyp(),testAttraktion.getTyp());
		assertEquals(newAttraktion.getBeschreibung(),testAttraktion.getBeschreibung());
		int response = Client.deleteAttraktion(addcity.getId(), newAttraktion.getName());
		assertEquals(200,response);
	}

}
