package edu.htwm.fmalek.cityinfo.services.rest;

import edu.htwm.fmalek.cityinfo.services.core.Attraktion;
import edu.htwm.fmalek.cityinfo.services.core.impl.CityinfoServiceInMemory;
import edu.htwm.fmalek.cityinfo.services.core.City;
import edu.htwm.fmalek.cityinfo.services.core.CityinfoService;
import edu.htwm.fmalek.cityinfo.services.rest.impl.CityinfoResourceImpl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Diese Klasse testet die Funktionalität der REST-Methoden unabhängig vom 
 * Web-Server (Whitebox-Testing). Das Controller-Objekt phonebookService 
 * wird manuell instanziert und der Instanz von PhonebookResourcImpl
 * übergeben
 *
 * @author adopleb / mbenndor
 */
public class CityinfoResourceImplTest {

	private CityinfoResourceImpl cityinfoResource;
	private UriInfo uriInfo;
	private CityinfoService cityinfoService;
    private String EMPTYSTRING;

    /**
	 * initialize components
	 */
	@Before
	public void prepareResourcesToTest() {
		cityinfoService = new CityinfoServiceInMemory();
		cityinfoResource = new CityinfoResourceImpl();
		cityinfoResource.setCityinfoService(cityinfoService);
        EMPTYSTRING = "";
    }

	/**
	 * Hilfsmethode (Mock-Objekt), welche die URI-Informationen bereitstellt 
	 * (Wird sonst vom Web-Server instanziert)
	 */
	@Before
	public void mockUriInfo() {

		uriInfo = mock(UriInfo.class);
		final UriBuilder fromResource = UriBuilder.fromResource(CityinfoResource.class);
		when(uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
			@Override
			public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
				return fromResource;
			}
		});
	}

	/**
	 * Testet, ob sich eine neue City anlegt werden kann (Statuscode 201 (created))
	 * Anschließend wird getestet, ob die City existiert (Statuscode
	 * 200 (OK))
	 * Daraufhin wird geprüft ob der zurückgegebene City identisch mit dem erzeugten ist
	 *
	 * @throws IllegalArgumentException
	 * @throws javax.ws.rs.core.UriBuilderException
	 * @throws java.net.URISyntaxException
	 * @throws java.net.MalformedURLException
	 */
	@Test
	public void createCitySuccess() throws IllegalArgumentException, UriBuilderException, URISyntaxException, MalformedURLException {
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		Response createCityResponse = cityinfoResource.createCity(uriInfo, expectedName,0,"");

		City createdCity = (City) createCityResponse.getEntity();

		// check correct response code 
		assertThat(createCityResponse.getStatus(), is(Status.CREATED.getStatusCode()));

		// get City 
		Response fetchUserResponse = cityinfoResource.getCity(createdCity.getId());
		// check correct response code 
		assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));
        // deserialize City
		City fetchedCity = (City) fetchUserResponse.getEntity();
		assertThat(fetchedCity, is(createdCity));
	}

	/**
	 * Anlegen eines City ohne Namen
	 */
	@Test
	public void createCityFails() {
		String name = EMPTYSTRING;
		Response createCityResponse = cityinfoResource.createCity(uriInfo, name,0,"");
        //check correct response code (bad request because of no name)
		assertThat(createCityResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	/**
	 * GET einen nicht existierenden User
	 */
	@Test
	public void fetchNotExistingCity() {
		Response fetchCityResponse = cityinfoResource.getCity(Integer.MAX_VALUE);
        //check correct response code
		assertThat(fetchCityResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}


	/**
	 * Methode testet, ob sich eine Telefonnummer für einen Nutzer anlegen und
	 * löschen lässt
	 */
	@Test
	public void createAndDeleteNumber() {
		//CREATE random PhoneUser and PhoneNumber
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
        City randomcity = cityinfoService.createCity(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1),RandomUtils.nextInt(99999),RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        
        Attraktion randomattraktion = new Attraktion(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), (RandomUtils.nextInt(3) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
		// add Number
		Response addAttraktionResponse = cityinfoResource.addattraktion(randomcity.getId(),randomattraktion.getName(), randomattraktion.getLage(),randomattraktion.getTyp(),randomattraktion.getBeschreibung());
        //check correct response code (201)
        assertThat(addAttraktionResponse.getStatus(), is(Status.CREATED.getStatusCode()));

        // GET User
		City fetchedCityWithNumber = (City) cityinfoResource.getCity(randomcity.getId()).getEntity();
        // number with caption exists
        assertThat(fetchedCityWithNumber.containsAttraktion(randomattraktion.getName()), is(true));
        // get Number
        Attraktion tempattraktion = fetchedCityWithNumber.getattraktion(randomattraktion.getName());
        //same number?
        assertThat(tempattraktion.getName(), is(randomattraktion.getName()));


        // DELETE PhoneNumber
		Response deleteattraktionResponse = cityinfoResource.deleteattraktion(randomcity.getId(), randomattraktion.getName());
        //check correct response code (200)
        assertThat(deleteattraktionResponse.getStatus(), is(Status.OK.getStatusCode()));

		// get User
		City fetchedattraktionWithoutname = (City) cityinfoResource.getCity(randomcity.getId()).getEntity();
        // check Number is deleted
        assertThat(fetchedattraktionWithoutname.getAttraktionen().contains(randomattraktion), is(false));
	}


    /**
	 * Attraktion einer nicht existierenden City hinzufügen
	 */
	//@Test
	public void checkAddNumberToNotExistingUser() {
		//remove all data
		this.prepareResourcesToTest();
        //create random data;
		int randomID = RandomUtils.nextInt(10) + 13;
		Attraktion attraktion = new Attraktion(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), (RandomUtils.nextInt(3) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
		///add number to not existing user
		Response addattraktionResponse = cityinfoResource.addattraktion(randomID, attraktion.getName(), attraktion.getLage(),attraktion.getTyp(),attraktion.getBeschreibung());
        //check correct response code (404)
        assertThat(addattraktionResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

    /**
     * Leere Attraktion hinzufügen
     */
    @Test
    public void checkAddEmptyNumber(){
        //add user
        City randomcity = cityinfoService.createCity(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1),RandomUtils.nextInt(99999),RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        //add empty Strings
        Response addattraktionResponse = cityinfoResource.addattraktion(randomcity.getId(), EMPTYSTRING, EMPTYSTRING, 0,EMPTYSTRING);
        assertThat(addattraktionResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
    }


	/**
	 * Testet, dass deleteattraktion 404 zurückgibt, wenn keine City gefunden wurde
	 * bzw. zu einer City die Attraktion nicht existiert
	 */
	@Test
	public void checkDeleteNumber() {
		//create random data;
        City randomcity = cityinfoService.createCity(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1),RandomUtils.nextInt(99999),RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        Attraktion randomattraktion = new Attraktion(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1), (RandomUtils.nextInt(3) + 1), RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
		
		//test delete from user without number
		Response deleteattraktionResponse = cityinfoResource.deleteattraktion(randomcity.getId(), randomattraktion.getName());
		assertThat(deleteattraktionResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

		// delete all
		this.prepareResourcesToTest();

		//test user not found
		deleteattraktionResponse = cityinfoResource.deleteattraktion(randomcity.getId(), randomattraktion.getName());
		assertThat(deleteattraktionResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

	/**
	 * Löschen einer existierenden und einer nicht existierenden City
	 */
	@Test
	public void checkDeleteUser() {
        // create randomUser
        City randomcity = cityinfoService.createCity(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1),RandomUtils.nextInt(99999),RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        //user deleted?
		Response deleteCityResponse = cityinfoResource.deleteCity(randomcity.getId());
		assertThat(deleteCityResponse.getStatus(), is(Status.OK.getStatusCode()));
		assertThat(cityinfoService.fetchAllCities().contains(randomcity), is(false));
		//retry delete allready deleted user
		deleteCityResponse = cityinfoResource.deleteCity(randomcity.getId());
        //check correct response code (404)
		assertThat(deleteCityResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}

}
