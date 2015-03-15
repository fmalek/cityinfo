/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.htwm.fmalek.cityinfo.services.core;

import edu.htwm.fmalek.cityinfo.services.core.City;
import edu.htwm.fmalek.cityinfo.services.core.Attraktion;
import edu.htwm.fmalek.cityinfo.services.core.impl.CityinfoServiceInMemory;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author adopleb , mbenndor
 * 
 * Testet, ob die JAXB-Annotationen korrekt gesetzt sind und das das XML-Mapping
 * fehlerfrei funktioniert
 */
public class XmlMappingTest {
    private File testfileLoad;
    private File testfileSave;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
	CityinfoService cityService;

    @Before
    public void prepareXmlMarshalling() throws JAXBException {
	testfileLoad= new File(XmlMappingTest.class.getResource("/mittweida.xml").getPath());
        testfileSave = new File(XmlMappingTest.class.getResource("/random.xml").getPath());
        /*
         * Code Duplizierung vermeiden
         * (DRY -> http://de.wikipedia.org/wiki/Don%E2%80%99t_repeat_yourself) und
         * aufwändige, immer wieder verwendete Objekte nur einmal erzeugen
         *
         */
        JAXBContext context = JAXBContext.newInstance(City.class, Attraktion.class);

        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        unmarshaller = context.createUnmarshaller();
    }
    @Before
	public void setUp() throws Exception 
	{
        cityService= new CityinfoServiceInMemory();
        
        
	}
    /**
     * Testet ob eine XML-Datei korrekt angelegt wird
     */
    @Test
    public void createValidXmlFile() throws IOException, JAXBException {

        // Lösche Datei falls schon vorhanden
        if (testfileSave.exists()) {
            testfileSave.delete();
        }

        /*
         * Erzeugt einen Nutzer mit einem zufälligen Namen und einer zufälligen Telefonnummer.
         */
		String cityname = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		String attraktionsname = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		City testcity = cityService.createCity(cityname,0,"");
		Attraktion testattraktion = new Attraktion(attraktionsname,"asqweq",1,"1234q");
        //City newUser = createRandomUser(phoneService);
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        //Attraktion newNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);
        testcity.getAttraktionen().add(testattraktion);



        // XML-Datei schreiben
        marshaller.marshal(testcity, new FileWriter(testfileSave));
        // Teste ob Datei existiert und lesbar ist
        assertThat(testfileSave.exists(), is(true));
        assertThat(testfileSave.isFile(), is(true));

        // deserialize
        City deserializedUser = (City) unmarshaller.unmarshal(testfileSave);

        // check deserialization creates new object
        assertThat(deserializedUser, is(not(sameInstance(testcity))));
        assertThat(deserializedUser.getName(), is(testcity.getName()));
    }

    /**
     * liest aus XML-Datei *
     */
    @Test
    public void loadFromXmlFile() throws IOException, JAXBException {

        City loadedCity = (City) unmarshaller.unmarshal(new FileReader(testfileLoad));

        Collection Attraktionen = loadedCity.getAttraktionen();

        // prüft, ob die beiden Telefonnummern enthalten sind
        assertThat(Attraktionen.size(), is(2));
        // prüft, ob der Name erkannt wurde
        assertThat(loadedCity.getName(), is("mittweida"));
        // prüft, dass die ID stimmt
        assertThat(loadedCity.getId(), is(1));
    }
}
