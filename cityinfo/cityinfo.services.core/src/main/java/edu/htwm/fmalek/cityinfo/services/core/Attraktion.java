package edu.htwm.fmalek.cityinfo.services.core;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "attraktionen")
@XmlType(propOrder = {"name", "lage","typ","beschreibung"})
public class Attraktion 
{


	private String name;
	private String lage;
	private int typ;
	private String beschreibung;

	
	public Attraktion(String name, int typ) 
	{
		this(name,"",typ,"");
	}
	public Attraktion()
	{
		
	}
	public Attraktion(String name, String lage, int typ, String beschreibung) {
		this.name = name;
		this.lage = lage;
		this.typ = typ;
		this.beschreibung = beschreibung;
	}
	
	public String getLage() {
		return lage;
	}
	public void setLage(String lage) {
		this.lage = lage;
	}
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
