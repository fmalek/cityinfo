package edu.htwm.fmalek.cityinfo.services.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import edu.htwm.fmalek.cityinfo.services.core.CityinfoService;
import edu.htwm.fmalek.cityinfo.services.core.impl.CityinfoServiceInMemory;
import edu.htwm.fmalek.cityinfo.services.rest.impl.CityinfoResourceImpl;

public class CityinfoApplication extends Application 
{
	private final CityinfoService cityinfoService;
	private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
	private final Set<Object> singletons = new HashSet<Object>();
	
	public CityinfoApplication()
	{
		cityinfoService = new CityinfoServiceInMemory();
		SingletonTypeInjectableProvider<Context,CityinfoService> CityinfoServiceProvider = 
				new SingletonTypeInjectableProvider<Context,CityinfoService>(CityinfoService.class,cityinfoService){};
				
		getSingletons().add(CityinfoServiceProvider);
		getClasses().add(CityinfoResourceImpl.class);
	}
	
	@Override
	public final Set<Class<?>> getClasses()
	{
		return resourcesToRegister;
	}
	
	@Override
	public final Set<Object> getSingletons()
	{
		return singletons;
	}
	
	
}
