package pos.domainlayer;

public class TaxMasterAdapter implements ITaxCalculatorAdapter {

	public Money getTaxes(Money s) {
		double total = Double.parseDouble(s.toString());
		double result = total * 1.1;
		
		Money getT = new Money((int) result);
		return getT;
	}

}
