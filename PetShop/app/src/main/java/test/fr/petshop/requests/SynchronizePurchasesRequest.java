package test.fr.petshop.requests;

public class SynchronizePurchasesRequest extends MyPetRequest
{

    public SynchronizePurchasesRequest()
    {
        scriptName = "synchroPurchases.php";
    }

    @Override
    protected String doInBackground(String... params)
    {
        return null;
    }
}
