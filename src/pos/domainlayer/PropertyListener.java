package pos.domainlayer;

public interface PropertyListener {
	public void onPropertyEvent(Sale s, String name, Money value);
}
