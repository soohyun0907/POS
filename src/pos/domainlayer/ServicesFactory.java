package pos.domainlayer;

public class ServicesFactory {
	
	private static ServicesFactory instance;
	ITaxCalculatorAdapter taxCalculatorAdapter;
	
	public static synchronized ServicesFactory getInstance() {
		if (instance == null) {
			instance = new ServicesFactory();
		}
		return instance;
	}
	
	public ITaxCalculatorAdapter getTaxCalculatorAdapter() {	
			try {
				String className = System.getProperty("taxcalculator.class.name");
				taxCalculatorAdapter = (ITaxCalculatorAdapter) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return taxCalculatorAdapter;
	}
}
