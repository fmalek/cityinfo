package edu.htwm.fmalek.cityinfo.services.core;

import java.util.List;

public interface CityinfoService 
{
	public City createCity(String name,int einwohner,String plz);
	public City getCityById(int cityId);
	public List<City> fetchAllCities();
	public void deleteCity(int cityId);
}
