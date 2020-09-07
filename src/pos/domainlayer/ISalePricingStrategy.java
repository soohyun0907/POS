package pos.domainlayer;

public interface ISalePricingStrategy {
	public Money getTotal(Sale s);
}
