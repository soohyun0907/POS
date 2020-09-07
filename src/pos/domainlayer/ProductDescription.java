package pos.domainlayer;

public class ProductDescription {
	private ItemID id;
	private Money price;
	private String description;
	// 생성자
	public ProductDescription(ItemID id, Money price, String description) {
		this.id = id;
		this.price = price;
		this.description = description;
	}

	public ItemID getItemID() {
		return id;
	}

	public Money getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}
}
