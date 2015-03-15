package edu.htwm.fmalek.cityinfo.services.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("cities")
public interface CityinfoResource 
{
	
	
	@POST
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response createCity(
			@Context UriInfo uriInfo, 
			@FormParam("name") String name,
			@DefaultValue("0") @FormParam("einwohner") int einwohner,
			@DefaultValue("") @FormParam("plz") String plz);
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("{cityid}")
	public Response getCity(
			@PathParam("cityid") int cityId);
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/typ/"+"{typ}")
	public Response getTypCity(
			@PathParam("typ") String typ);
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("{cityid}"+"/typ/"+"{typ}")
	public Response getTypAttr(
			@PathParam("cityid") int cityId,
			@PathParam("typ") String typ);
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/empf/")
	public Response getempfcity();
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("{cityid}"+"/empf/")
	public Response getempfattr(
			@PathParam("cityid") int cityId);
	
	
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PUT
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("{cityid}"+"/attr/")
	public Response addattraktion(
			@PathParam("cityid") int cityid,
			@DefaultValue("") @QueryParam("name") String aname,
			@DefaultValue("") @QueryParam("lage") String alage,
			@QueryParam("typ") int atyp,
			@DefaultValue("") @QueryParam("besch") String abeschreib);
	
	@DELETE
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("{cityid}"+"/attr/"+"{aname}")
	public Response deleteattraktion(
			@PathParam("cityid")int cityid,
			@PathParam("aname")String aname);
	@DELETE
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})	
	@Path("{cityid}")
	public Response deleteCity(
			@PathParam("cityid") int cityid);
	
	
}
