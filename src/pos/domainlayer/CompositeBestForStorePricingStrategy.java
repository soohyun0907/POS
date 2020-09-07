package pos.domainlayer;

import java.util.Iterator;

public class CompositeBestForStorePricingStrategy extends CompositePricingStrategy implements ISalePricingStrategy{

	@Override
	public Money getTotal(Sale sale) {
		Money highTotal = new Money(Integer.MIN_VALUE);
		
		Iterator<ISalePricingStrategy> i = strategies.iterator();
		while(i.hasNext()) {
			ISalePricingStrategy strategy = i.next();
			
			Money total = strategy.getTotal(sale);
			
			highTotal = total.max(highTotal);
		}
		
		return highTotal;
	}

}
