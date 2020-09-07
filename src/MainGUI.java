import pos.domainlayer.Register;
import pos.domainlayer.Sale;
import pos.domainlayer.Store;
import presentationlayer.ProcessSaleJFrame;

public class MainGUI {

	public static void main(String[] args) {
		if(args.length==1) {
			String dbFileName = args[0];
			
			Store store = new Store(dbFileName);
			Register register = store.getRegister();
			Sale sale = new Sale();
			
			new ProcessSaleJFrame(register, dbFileName, sale);
		}
		else {
			System.out.println(
					"Usage: java POS databaseDriver databaseURL" );
		}
	}

}
