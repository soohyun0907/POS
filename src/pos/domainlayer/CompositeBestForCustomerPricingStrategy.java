package pos.domainlayer;

import java.util.Iterator;

public class CompositeBestForCustomerPricingStrategy extends CompositePricingStrategy implements ISalePricingStrategy{

	@Override
	public Money getTotal(Sale sale) {
		Money lowestTotal = new Money(Integer.MAX_VALUE);
		
		Iterator<ISalePricingStrategy> i = strategies.iterator();
		while(i.hasNext()) {
			ISalePricingStrategy strategy = i.next();
			
			Money total = strategy.getTotal(sale);
			
			lowestTotal = total.min(lowestTotal);
		}
		
		return lowestTotal;
	}

}
