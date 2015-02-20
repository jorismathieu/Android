package test.fr.petshop.entities;

public class CurrentUser
{
    private static CurrentUser mInstance = null;

    private int     idUser;
    private String  email;

    public static CurrentUser getmInstance()
    {
        if (mInstance == null)
            mInstance = new CurrentUser();
        return mInstance;
    }

    public int getIdUser()
    {
        return idUser;
    }

    public void setIdUser(int id_user)
    {
        this.idUser = id_user;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
