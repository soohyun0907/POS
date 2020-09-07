package pos.domainlayer;

import java.util.ArrayList;
import java.util.List;

// 추상 클래스 : BestForCustomer와 BestForStore에 동일한 인터페이스를 제공하기 위함.
public abstract class CompositePricingStrategy implements ISalePricingStrategy {
	
	protected List strategies = new ArrayList();
	
	public void add(ISalePricingStrategy s) {
		strategies.add(s);
	}
	
	public abstract Money getTotal(Sale sale);
}
