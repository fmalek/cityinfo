package edu.htwm.fmalek.cityinfo.services.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;







import edu.htwm.fmalek.cityinfo.services.core.impl.CityinfoServiceInMemory;


public class CityTest {
	CityinfoService cityService;

	@Before
	public void setUp() throws Exception 
	{
        cityService= new CityinfoServiceInMemory();
        
        
	}

	@Test
	public final void testcityerstellenundloeschen() 
	{
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		City testcity = cityService.createCity(expectedName,0,"");
		assertThat(testcity.getName(), is(expectedName));
		cityService.deleteCity(testcity.getId());
		City newtestcity = cityService.getCityById(testcity.getId());
		assertThat(newtestcity, is(nullValue()));
	}

	@Test
	public final void Testattraktionhinzufuegenundloeschen() {
		String cityname = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		String attraktionsname = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		City testcity = cityService.createCity(cityname,0,"");
		Attraktion testattraktion = new Attraktion(attraktionsname,1);
		testcity.addAttraktion(testattraktion);
		assertThat(testcity.getattraktion(attraktionsname), is(testattraktion));
		testcity.deleteAttraktion(attraktionsname);
		assertThat(testcity.getattraktion(attraktionsname), is(nullValue()));
	}



}
