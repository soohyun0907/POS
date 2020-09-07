package pos.domainlayer;

import java.util.List;

public class GoodAsGoldTaxProAdapter implements ITaxCalculatorAdapter {

	public Money getTaxes(Money s) {
		double total = Double.parseDouble(s.toString());
		double result = total * 1.2;
		
		Money getT = new Money((int) result);
		return getT;
	}

}
