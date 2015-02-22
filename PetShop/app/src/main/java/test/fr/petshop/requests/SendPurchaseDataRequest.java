package test.fr.petshop.requests;

public class SendPurchaseDataRequest extends MyPetRequest
{

    public SendPurchaseDataRequest()
    {
        scriptName = "synchroPurchases.php";
    }

    @Override
    protected String doInBackground(String... params)
    {
        return null;
    }
}
