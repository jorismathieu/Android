package qualia.fr.petshop.dao;

import java.util.List;

public interface IMyPetDAO<T>
{
    public List<T>  read(Integer id_user);
    public void     delete(T object);
    public void     update(T object);
    public void     create(T object);
}
