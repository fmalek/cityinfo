package edu.htwm.fmalek.cityinfo.services.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htwm.fmalek.cityinfo.services.core.City;
import edu.htwm.fmalek.cityinfo.services.core.CityinfoService;


public class CityinfoServiceInMemory implements CityinfoService
{
    private final Map<Integer, City> cities;
    private int lastId=0;

    public CityinfoServiceInMemory() 
    {
        this.cities = new HashMap<Integer, City>();
    }	
    @Override
	public City createCity(String name,int einwohner, String plz)
	{
        City city = new City(++lastId,name,einwohner,plz);
        cities.put(city.getId(),city);
        return city;
	}
	@Override
	public City getCityById(int cityId)
	{
		if(cities.containsKey(cityId))
			return cities.get(cityId);
		else 
			return null;
	}
	@Override
	public List<City> fetchAllCities()
	{
		return new ArrayList<City>(cities.values());
		
	}
	@Override
	public void deleteCity(int cityid)
		{
			cities.remove(cityid);
		}
}
