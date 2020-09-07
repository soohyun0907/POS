package pos.domainlayer;

public class AbsoluteDiscountOverThresholdPricingStrategy implements ISalePricingStrategy{
	
	Money discount;
	Money threshold = new Money(20000);
	
	@Override
	public Money getTotal(Sale s) {
		
		Money pdt = new Money(Integer.parseInt(s.getPreDiscountTotal().toString()));
		double disc = Double.parseDouble(pdt.toString()) * 0.2;
		discount = new Money((int) disc);
		
		if(Integer.parseInt(pdt.toString()) < Integer.parseInt(threshold.toString()))
			return pdt;
		else
			return pdt.minus(discount);
	}

}
