package hu.dankodavid.flexion.javachallenge;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;

public class PurchaseRestConnector implements Integration
{
	protected final String restEndpoint;
	protected final String developerId;
	
	public PurchaseRestConnector(String restEndpoint, String developerId)
	{
		this.restEndpoint = restEndpoint;
		this.developerId = developerId;
	}
	
	public PurchaseRestConnector(String developerId)
	{
		this(FlexionEnv.getApiRepositoryUrl(), developerId);
	}
	
	@Override
	public Purchase buy(String itemId)
	{
		Assert.notNull(itemId, "ItemId may not null");
		return PurchaseItem.parse(RestTools.transactionJsonObject(restEndpoint+"/"+developerId+"/buy/"+URLEncoder.encode(itemId, Charset.defaultCharset()), HttpMethod.POST));
	}

	@Override
	public List<Purchase> getPurchases()
	{
		return (List) PurchaseItem.parseAll
		(
			RestTools.transactionJsonObject(restEndpoint+"/"+developerId+"/all", HttpMethod.GET)
				.getJSONArray("purchases")
		);
	}

	@Override
	public void consume(Purchase var1)
	{
		RestTools.transactionRaw(restEndpoint+"/"+developerId+"/consume/"+var1.getId(), HttpMethod.POST);
	}
	
	public Purchase getFreshPurchase(Purchase p)
	{
		return PurchaseItem.findById(getPurchases(), p.getId());
	}
}
