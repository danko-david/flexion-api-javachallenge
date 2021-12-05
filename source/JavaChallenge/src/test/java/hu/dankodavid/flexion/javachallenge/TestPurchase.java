package hu.dankodavid.flexion.javachallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClientResponseException.Conflict;

import com.flexionmobile.codingchallenge.integration.IntegrationTestRunner;
import com.flexionmobile.codingchallenge.integration.Purchase;

/**
 * Test purchase related functions.
 */
@TestInstance(Lifecycle.PER_METHOD)
public class TestPurchase
{
	/**
	 * Runs the integration tests, provided with the documentation.
	 * */
	@Test
	public void testExternalIntegration()
	{
		new IntegrationTestRunner().runTests(FlexionEnv.getConfiguredPurchaseConnector());
	}

	/**
	 * Negative tests where trying consume an item we don't bought 
	 * and ultimately fail.
	 * 
	 * Note:
	 * Failure tests for bad API usage.
	 * In this scenario, I thinking with a mind of a tester, who should test an API
	 * connector written by someone else.
	 * I saw many time such implementation:
	 * <code>
	 * 	try
	 * 	{
	 * 		return doQuerySuffs();
	 * 	}
	 * 	catch(Throwable e)
	 * 	{
	 * 		logger.trace("An exception occurred: ", e);
	 * 	}
	 * </code>
	 * (Note that logging is on trace level, and Exceptions.propagateAnyway() not called)
	 * 
	 * This can happen easily when the interface do not enable to throw any exception
	 * but implementation code does, and IDE automatic try-catch wrapping block created.
	 * 
	 * If the application is written well, and issues are rare, it might hard to catch
	 * the issue. Method returns silently as it like to succeed, but it don't. 
	 * */
	@Test
	public void test_fail_consumeNonexistingItem()
	{
		assertThrows(Conflict.class, ()->
		{
			PurchaseItem item = new PurchaseItem("nonexisting_item", false, "nonexisting_id");
			FlexionEnv.getConfiguredPurchaseConnector().consume(item);
		});
	}

	/**
	 * Test item names that might cause technical problems.
	 * 
	 * Note:
	 * There's not enough information what subject the API is about to manage.
	 * So I assume the "worst" case, when item name can be a user provided content. 
	 * Eg.: Users can sell custom items (for other users) in an in-game workshop
	 * and therefore can specify custom names.
	 * */
	@ParameterizedTest
	@ValueSource(strings = {"item/useful", "Árvíztűrő tükörfúrógép", "Grandma's legendary pie 😁"})
	public void test_pass_unusualItemName(String itemId)
	{
		Purchase p = FlexionEnv.getConfiguredPurchaseConnector().buy(itemId);
		assertEquals(p.getItemId(), itemId);
	}
	
	@Test
	public void test_fail_buyNullItem()
	{
		assertThrows(IllegalArgumentException.class, ()->
		{
			FlexionEnv.getConfiguredPurchaseConnector().buy(null);
		});
	}
	
	@Test
	public void test_fail_doubleConsume()
	{
		PurchaseRestConnector conn = FlexionEnv.getConfiguredPurchaseConnector();
		
		Purchase item = conn.buy("testItem");
		assertFalse(item.getConsumed());
		conn.consume(item);
		
		Purchase refreshed = conn.getFreshPurchase(item);
		assertTrue(refreshed.getConsumed());
		
		assertThrows(Conflict.class, ()->
		{
			conn.consume(refreshed);
		});
	}
}
