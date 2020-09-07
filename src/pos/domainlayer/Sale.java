package pos.domainlayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	
	private List<SalesLineItem> lineItems =  new ArrayList<SalesLineItem>(); //내용이 항상 같다면 특별한 것만 생성자로 관리하여 생성하고 초기값으로는 기본 설정이 더 유용.
	private boolean isComplete = false;
	private Payment payment;
	Sale s;
	ArrayList<PropertyListener> propertyListeners = new ArrayList<>();
	
	ISalePricingStrategy pricingStrategy;
	CompositePricingStrategy comPricingStrategy;
	Money total;
	
	ITaxCalculatorAdapter taxCalculatorAdapter;
	
	
	public void initialize() {
		taxCalculatorAdapter = ServicesFactory.getInstance().getTaxCalculatorAdapter();
	}
	
	public void addPropertyListener(PropertyListener lis) {
		propertyListeners.add(lis);
	}
	
	public void publishPropertyEvent(String name, Money value) {
		for(PropertyListener propertylistener : propertyListeners) {
			propertylistener.onPropertyEvent(s, name, value);
		}
	}
	
	public Money getBalance()	{ //잔돈 계산
		return payment.getAmount().minus(total);
	}
	
	public void becomeComplete(){ 
		isComplete = true; 
	}
	
	public boolean isComplete() {  
		return isComplete; 
	}
	
	public void makeLineItem(ProductDescription desc, int quantity)	{
		lineItems.add( new SalesLineItem(desc, quantity) ); //ArrayList에 추가.
	}
	
	public Money getTotal() { //합계 구하기 : 부분 합을 이용하여 총합 구하기
		Money subtotal = null;
		total = new Money();
		
		for(SalesLineItem lineItem : lineItems)
		{
			subtotal = lineItem.getSubtotal(); //부분 합.
			total.add(subtotal); //누적 합.
		}
		
		setTotal(total);
		
		return total;
	}
	
	public Money getTotal(Sale sale) {
		return pricingStrategy.getTotal(this);
	}
	
	public void setTotal(Money newTotal) { //새로운 합계
		total = newTotal;
		publishPropertyEvent("sale.total", total);
	}
	
	public Money getTotalWithTax() {
		total = taxCalculatorAdapter.getTaxes(total);
		return total;
	}
	
	public Money getPreDiscountTotal() {
		Money preDiscountTotal = total;
		return preDiscountTotal;
	}
	
	public void makePayment(Money cashTendered) {
		payment = new Payment(cashTendered);
	}
	
	public Money applyDiscount(CompositePricingStrategy comPricingStrategy, Sale s) {
		PercentDiscountPricingStrategy percentDiscount = new PercentDiscountPricingStrategy();
		AbsoluteDiscountOverThresholdPricingStrategy absoluteDicount = new AbsoluteDiscountOverThresholdPricingStrategy();
		comPricingStrategy.add(percentDiscount);
		comPricingStrategy.add(absoluteDicount);
		total = comPricingStrategy.getTotal(s);
		return total;
	}
}
