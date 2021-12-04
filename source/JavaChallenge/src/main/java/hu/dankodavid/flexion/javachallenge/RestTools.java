package hu.dankodavid.flexion.javachallenge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

public class RestTools
{
	public static String transactionRaw(String url, HttpMethod method)
	{
		WebClient cli = WebClient.builder().baseUrl(url).build();
		
		RequestHeadersUriSpec<?> ret = cli.method(method);
		return ret.retrieve().bodyToMono(String.class).block();
	}
	

	public static JSONObject transactionJsonObject(String url, HttpMethod method)
	{
		return new JSONObject(transactionRaw(url, method));
	}

	public static JSONArray transactionGetJsonArray(String url, HttpMethod method)
	{
		return new JSONArray(transactionRaw(url, method));
	}
}
