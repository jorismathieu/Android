package qualia.fr.petshop.entities;

public class Purchase
{
    private Integer id;
    private Integer idUser;
    private Integer idAnimal;

    public Purchase(Integer nId, Integer nIdUser, Integer nIdAnimal)
    {
        id = nId;
        idUser = nIdUser;
        idAnimal = nIdAnimal;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getIdUser()
    {
        return idUser;
    }

    public void setIdUser(Integer idUser)
    {
        this.idUser = idUser;
    }

    public Integer getIdAnimal()
    {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal)
    {
        this.idAnimal = idAnimal;
    }
}
