package pos.domainlayer;

import java.util.ArrayList;

import presentationlayer.PersistentStorage;

public class Store {
	private ProductCatalog catalog;
	private Register register; //컨트롤러인 register객체 생성.
	

	public Store(String dbFileName) {
		// TODO Auto-generated constructor stub
		PersistentStorage ps = new PersistentStorage(dbFileName);
		ArrayList<ItemID> idList = ps.loadItemIds();
		ArrayList<Money> priceList = ps.loadPriceList();
		ArrayList<String> descList = ps.loadDescList();
		catalog = new ProductCatalog(idList, priceList, descList);
		register = new Register(catalog);
	}

	public Register getRegister() { 
		return register; 
	}
}
