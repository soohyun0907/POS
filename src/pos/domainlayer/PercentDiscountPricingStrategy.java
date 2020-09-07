package pos.domainlayer;

public class PercentDiscountPricingStrategy implements ISalePricingStrategy{
	float percentage = (float) 0.9;
	
	@Override
	public Money getTotal(Sale s) {
		
		float total = Float.parseFloat(s.getPreDiscountTotal().toString());
		total = total*percentage;
		Money percentDiscount = new Money((int) total);
		
		return percentDiscount;
	}

}
