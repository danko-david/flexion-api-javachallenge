package hu.dankodavid.flexion.javachallenge;

public class FlexionEnv
{
	public static String getApiRepositoryUrl()
	{
		return getVariable
		(
			"flexion.rest.repository_url",
			"FLEXION_REST_REPOSITORY_URL",
			"http://sandbox.flexionmobile.com/javachallenge/rest/developer/");
	}
	
	public static String getServiceDeveloperId()
	{
		return getVariable
		(
			"flexion.javachallenge.dev_id",
			"FLEXION_JAVACHALLENGE_DEV_ID",
			"test"
		);
	}
	
	public static String getVariable(String propVar, String envVar, String defVal)
	{
		String ret = System.getProperty(propVar);
		if(null != ret)
		{
			return ret;
		}
		
		ret = System.getenv(envVar);
		if(null != ret)
		{
			return ret;
		}
		
		return defVal;
	}
	
	public static PurchaseRestConnector getConfiguredPurchaseConnector()
	{
		return new PurchaseRestConnector(getServiceDeveloperId());
	}
}
