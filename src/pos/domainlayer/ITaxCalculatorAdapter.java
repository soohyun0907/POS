package pos.domainlayer;

public interface ITaxCalculatorAdapter {
	
	public Money getTaxes(Money total);
}
