package edu.htwm.fmalek.cityinfo.services.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "city")
public class City 
{

	private int id;
	private String name;
	private int einwohnerzahl;
	private List<Attraktion> attraktionen;
	private String plz;
	private int typ;

	
	
	public City(int id, String name, int einwohnerzahl,
			List<Attraktion> attraktionen,String plz,int typ) 
	{
		this.id = id;
		this.name = name;
		this.einwohnerzahl = einwohnerzahl;
		this.attraktionen = attraktionen;	
		this.plz = plz;
		this.typ=typ;
	}
	public City(int id,String name,int einwohner, String plz)
	{
		this(id,name,einwohner,new ArrayList<Attraktion>(),plz,0);
	}
	public City() 
	{
		attraktionen = new ArrayList<Attraktion>();
	}
	@XmlAttribute
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getEinwohnerzahl() 
	{
		return einwohnerzahl;
	}
	public void setEinwohnerzahl(int einwohnerzahl) 
	{
		this.einwohnerzahl = einwohnerzahl;
	}
	@XmlElement(name = "attraktion")
	@XmlElementWrapper(name = "attraktionen")
	public List<Attraktion> getAttraktionen() 
	{
		return attraktionen;
	}
	public void setAttraktionen(List<Attraktion> attraktionen) 
	{
		this.attraktionen = attraktionen;
	}
	public void addAttraktion(Attraktion newattraktion)
	{
		attraktionen.add(newattraktion);
		calctyp();
	}
	public void deleteAttraktion(String name)
	{
		Attraktion attraktion = getattraktion(name);
			if(attraktion!= null)
				attraktionen.remove(attraktion);
		calctyp();
	}
	public Attraktion getattraktion(String name)
	{
		Attraktion attraktion = null;
		for(Attraktion temp: attraktionen)
		{
			if(temp.getName().equals(name))
				return temp;
		}
		return attraktion;
	}
	public String getplz() {
		return plz;
	}
	public void setplz(String plz) {
		this.plz = plz;
	}
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}
	public void calctyp()
	{
		int sommer=0;
		int winter=0;
		int ganzjaehrig=0;
		for(Attraktion temp: attraktionen)
		{
			if(temp.getTyp()==1)
				sommer++;
			if(temp.getTyp()==2)
				winter++;
			if(temp.getTyp()==3)
				ganzjaehrig++;
		}
		if(sommer>winter)
			this.typ=1;
		if(winter>sommer)
			this.typ=2;
		if(ganzjaehrig>=winter+sommer||winter==sommer)
			this.typ=3;
	}
	public List<Attraktion> getspezialAttraktionen(int typ)
	{
		ArrayList<Attraktion> typattraktionen = new ArrayList<Attraktion>();
		for(Attraktion temp: attraktionen)
		{
			if(temp.getTyp()==typ)
				typattraktionen.add(temp);
		}
		return typattraktionen;
	}
	public List<Attraktion> getspezialandganzAttraktionen(int typ)
	{
		ArrayList<Attraktion> typattraktionen = new ArrayList<Attraktion>();
		for(Attraktion temp: attraktionen)
		{
			if(temp.getTyp()==typ||temp.getTyp()==3)
				typattraktionen.add(temp);
		}
		return typattraktionen;
	}
    public boolean containsAttraktion(String name)
    {
    	Attraktion attraktion = getattraktion(name);
    	if(attraktion!=null)
    		return true;
    	else
    		return false;
    }
	
}
