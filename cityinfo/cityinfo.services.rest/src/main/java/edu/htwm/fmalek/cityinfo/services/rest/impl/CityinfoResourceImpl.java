package edu.htwm.fmalek.cityinfo.services.rest.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import edu.htwm.fmalek.cityinfo.services.core.Attraktion;
import edu.htwm.fmalek.cityinfo.services.core.City;
import edu.htwm.fmalek.cityinfo.services.core.CityinfoService;
import edu.htwm.fmalek.cityinfo.services.rest.CityinfoResource;

import java.util.Random;


public class CityinfoResourceImpl implements CityinfoResource
{
	@Context private CityinfoService cityinfoService;

	public CityinfoService getCityinfoService() 
	{
		return cityinfoService;
	}

	public void setCityinfoService(CityinfoService cityService) 
	{
		this.cityinfoService = cityService;
	}
	@Override
	public Response createCity(UriInfo uriinfo, String name, int einwohner, String plz)
	{
		if(name==null||name.isEmpty())		
			return Response.status(Status.BAD_REQUEST).build();
		else
		{
			City newcity = cityinfoService.createCity(name,einwohner,plz);
			return Response.status(Status.CREATED).entity(newcity).build();
		}
		
	}

	@Override
	public Response getCity(int cityId) 
	{
		if(getCityinfoService().getCityById(cityId)==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		else
		{
			City foundcity = getCityinfoService().getCityById(cityId);
			return Response.status(Status.OK).entity(foundcity).build(); 
		}
		
	}

	@Override
	public Response addattraktion(int cityid, String aname,
			String alage, int atyp, String abeschreib) 
	{
		if(getCityinfoService().getCityById(cityid)==null)
			return Response.status(Status.NOT_FOUND).build();
		else if(aname==null||aname.isEmpty()==true)		
			return Response.status(Status.BAD_REQUEST).build();
		else if(atyp>=4||atyp<0)
			return Response.status(Status.BAD_REQUEST).build();
		else
		{
			City newcity = getCityinfoService().getCityById(cityid);
			Attraktion newattraktion = new Attraktion(aname,alage,atyp,abeschreib);
			newcity.addAttraktion(newattraktion);
			return Response.status(Status.CREATED).entity(newattraktion).build();
		}
	}

	@Override
	public Response deleteattraktion(int cityid, String aname) 
	{
		if(getCityinfoService().getCityById(cityid)==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		else if (getCityinfoService().getCityById(cityid).getattraktion(aname)==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		{
			cityinfoService.getCityById(cityid).deleteAttraktion(aname);
			return Response.status(Status.OK).build();
		}
	}

	@Override
	public Response deleteCity(int cityid) 
	{
		if(getCityinfoService().getCityById(cityid)==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		else
		{
			getCityinfoService().deleteCity(cityid);
			return Response.status(Status.OK).build(); 
		}
	}
	/*
	 * (non-Javadoc)
	 * @see edu.htwm.fmalek.cityinfo.services.rest.CityinfoResource#getTypCity(java.lang.String)
	 * die Methode getTypCity dient zur Ausgabe einer Zufälligen gewünschten Typstadt
	 * Wählbar ist eine Sommer, eine Winter oder eine Stadt die als ganzjährig eingestuft wurde.
	 * 
	 */
	@Override
	public Response getTypCity(String typ) 
	{
		int inttyp = 0;
		if(typ.equals("sommer"))
			inttyp=1;
		if(typ.equals("winter"))
			inttyp=2;
		if(typ.equals("ganzjahr"))
			inttyp=3;
		
		if(inttyp==0)
		{
			return Response.status(Status.BAD_REQUEST).build();
		}
		ArrayList<City> typcities = new ArrayList<City>();
		for(City temp: getCityinfoService().fetchAllCities())
			{
			if(temp.getTyp()==inttyp)
				typcities.add(temp);
			}
		if(typcities.size()==0)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		Random random = new Random();
		City auswahl = typcities.get(random.nextInt(typcities.size()));
		if(auswahl==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		else
		{
			return Response.status(Status.OK).entity(auswahl).build(); 
		}
		
	}

	@Override
	public Response getTypAttr(int cityId,String typ) {
		if(getCityinfoService().getCityById(cityId)==null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		int inttyp = 0;
		if(typ.equals("sommer"))
			inttyp=1;
		if(typ.equals("winter"))
			inttyp=2;
		if(typ.equals("ganzjahr"))
			inttyp=3;
		if(inttyp==0)
		{
			return Response.status(Status.BAD_REQUEST).build();
		}
		//List<Attraktion> oldAttraktion = getCityinfoService().getCityById(cityId).getAttraktionen();
		City oldcity = getCityinfoService().getCityById(cityId);
		City uebergabe = new City(oldcity.getId(),oldcity.getName(),oldcity.getEinwohnerzahl(),oldcity.getspezialAttraktionen(inttyp),oldcity.getplz(),oldcity.getTyp());
		return Response.status(Status.OK).entity(uebergabe).build(); 

	}

	@Override
	public Response getempfcity() {
		int calweek = Integer.valueOf(new SimpleDateFormat("w").format(new java.util.Date()));
		if(calweek>12&&calweek<40)
			return getTypCity("sommer");
		else
			return getTypCity("winter");
	
	}

	@Override
	public Response getempfattr(int cityId) 
		{
		int calweek = Integer.valueOf(new SimpleDateFormat("w").format(new java.util.Date()));
		int inttyp;
		if(calweek>12&&calweek<40)
			inttyp=1;
		else
			inttyp=2;
		City oldcity = getCityinfoService().getCityById(cityId);
		City uebergabe = new City(oldcity.getId(),oldcity.getName(),oldcity.getEinwohnerzahl(),oldcity.getspezialandganzAttraktionen(inttyp),oldcity.getplz(),oldcity.getTyp());
		return Response.status(Status.OK).entity(uebergabe).build(); 

	}
	
	
	
}
