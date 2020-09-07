package pos.domainlayer;

public class SalesLineItem {
	
	private int quantity; //수량
	private ProductDescription description; //한번 내용을 찾으면 계속 활용. 필요할때마다 다시 찾지 않아도 됨.
	
	public SalesLineItem(ProductDescription desc, int quantity)
	{
		this.description = desc;
		this.quantity= quantity;
	}
	
	public Money getSubtotal() //부분 합을 구하는 메소드.
	{
		return description.getPrice().times(quantity); //Money타입에 곱셈 연산을 하도록 추가.
	}
}
