package test.fr.petshop.entities;


public class Animal
{
    private Integer id;
    private String  name;
    private String  description;
    private String  type;
    private String  address;
    private String  coordinates;

    public Animal(Integer nId, String nName, String nDescription, String nType, String nAddress, String nCoordinates)
    {
        id = nId;
        name = nName;
        description = nDescription;
        type  = nType;
        address = nAddress;
        coordinates = nCoordinates;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAdress(String adress)
    {
        this.address = adress;
    }

    public String getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }
}
