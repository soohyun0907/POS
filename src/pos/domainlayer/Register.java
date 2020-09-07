package pos.domainlayer;

public class Register {
	private ProductCatalog catalog;
	private Sale currentSale;
	
	public Register(ProductCatalog catalog){ //변수 가시성을 유지.
		this.catalog = catalog;
	}
	
	public void endSale()
	{
		currentSale.becomeComplete();
	}
	
	public void enterItem(ItemID id, int quantity) //아이템 저장.
	{
		ProductDescription desc = catalog.getProductDescription(id);
		currentSale.makeLineItem(desc, quantity);
	}
	
	public Sale makeNewSale()
	{
		currentSale = new Sale();
		return currentSale;
	}
	
	public void makePayment(Money cashTendered) //System operation.
	{
		currentSale.makePayment(cashTendered);
	}
}
