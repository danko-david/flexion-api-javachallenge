package hu.dankodavid.flexion.javachallenge;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.flexionmobile.codingchallenge.integration.Purchase;

public class PurchaseItem implements Purchase
{
	protected final String id;
	protected final boolean consumed;
	protected final String itemId;
	
	public PurchaseItem(String id, boolean consumed, String itemId)
	{
		this.id = id;
		this.consumed = consumed;
		this.itemId = itemId;
	}
	
	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public boolean getConsumed()
	{
		return consumed;
	}

	@Override
	public String getItemId()
	{
		return itemId;
	}
	
	@Override
	public String toString()
	{
		return "PurchaseItem: {id=\""+id+"\", consumed="+consumed+", itemId=\""+itemId+"\"}";
	}
	
	public static PurchaseItem parse(JSONObject obj)
	{
		return new PurchaseItem(obj.getString("id"), obj.getBoolean("consumed"), URLDecoder.decode(obj.getString("itemId"), Charset.defaultCharset()));
	}
	
	public static List<PurchaseItem> parseAll(JSONArray arr)
	{
		List<PurchaseItem> ret = new ArrayList<>();
		for(int i=0;i<arr.length();++i)
		{
			ret.add(parse(arr.getJSONObject(i)));
		}
		return ret;
	}
	
	public static <T extends Purchase > T findById(List<T> purchases, String id)
	{
		for(T p:purchases)
		{
			if(id.equals(p.getId()))
			{
				return p;
			}
		}
		
		return null;
	}
}
