package pos.domainlayer;

public class ItemID {
	String id;
	//문자열을 위한 생성자
	public ItemID(String id) {
		this.id = id;
	}
	
	//int형을 위한 생성자
	public ItemID(int id) {
		this.id = String.valueOf(id); //String값을 변환.
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
