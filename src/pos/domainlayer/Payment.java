package pos.domainlayer;

public class Payment {
	private Money amount;

	public Payment(Money cachTendered) {
		amount = cachTendered;
	}

	public Money getAmount() {
		return amount;
	}
}
