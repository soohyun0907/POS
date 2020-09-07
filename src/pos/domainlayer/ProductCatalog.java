package pos.domainlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import presentationlayer.PersistentStorage;

public class ProductCatalog { //다수의 product description을 카탈로그에 저장하여 관리.
	
	private Map<String, ProductDescription> descriptions = new HashMap<String, ProductDescription>(); //프로그램의 확장성을 고려하여 hashmap의 인터페이스인 map을 생성.
																									  //다른 형제 map들과의 유연성 확보가 가능함.
																									  //HashMap<String, ProductDescription> 이 자체가 하나의 타입.
	
	public ProductCatalog(ArrayList<ItemID> idList, ArrayList<Money> priceList, ArrayList<String> descList) {
		for(int i = 0; i < 5; i++) {
			ProductDescription desc = new ProductDescription(idList.get(i), priceList.get(i), descList.get(i));
			descriptions.put(idList.get(i).toString(), desc);
		}
	}

	public ProductDescription getProductDescription(ItemID id){
		return descriptions.get(id.toString()); //상품 정보 반환.
		}
}
