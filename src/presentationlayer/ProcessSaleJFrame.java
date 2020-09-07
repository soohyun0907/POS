package presentationlayer;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pos.domainlayer.CompositeBestForCustomerPricingStrategy;
import pos.domainlayer.CompositeBestForStorePricingStrategy;
import pos.domainlayer.CompositePricingStrategy;
import pos.domainlayer.ITaxCalculatorAdapter;
import pos.domainlayer.ItemID;
import pos.domainlayer.Money;
import pos.domainlayer.PropertyListener;
import pos.domainlayer.Register;
import pos.domainlayer.Sale;
import pos.domainlayer.SalesLineItem;

public class ProcessSaleJFrame extends JFrame implements PropertyListener{
	
	Register register;
	Sale sale;
	Money money;
	PersistentStorage ps;
	SalesLineItem lineItem;
	
	ITaxCalculatorAdapter taxCalculatorAdapter;
	
	CompositePricingStrategy cps;
	CompositeBestForCustomerPricingStrategy bestForCustomer;
	CompositeBestForStorePricingStrategy bestForStore;
	
	ArrayList<ItemID> idsList = new ArrayList<ItemID>();
	
	private PropertyListener pl;
	
	private JButton b_makeNewSale;
	private JButton b_enterItem;
	private JButton b_endSale;
	private JButton b_calculateTax;
	private JButton b_applyDiscount;
	private JButton b_makePayment;
	
	private JComboBox c_itemID;
	
	private JLabel l_itemID;
	private JLabel l_quantity;
	private JLabel l_desc;
	private JLabel l_currentTotal;
	private JLabel l_totalWithTax;
	private JLabel l_totalAfterDiscount;
	private JLabel l_amount;
	private JLabel l_balance;
	
	ButtonGroup bg1 = new ButtonGroup();
	private JRadioButton r_taxMaster;
	private JRadioButton r_goodAsGoldTaxPro;
	ButtonGroup bg2 = new ButtonGroup();
	private JRadioButton r_bestForCustomer;
	private JRadioButton r_bestForStore;
	
	private JTextArea messageJTextArea;
	
	private JTextField t_quantity;
	private JTextField t_desc;
	private JTextField t_currentTotal;
	private JTextField t_totalWithTax;
	private JTextField t_totalAfterDiscount;
	private JTextField t_amount;
	private JTextField t_balance;
	
	public ProcessSaleJFrame(Register register, String dbFileName, Sale sale) {
		super("POS System v0.2");
		
		this.register = register;
		this.sale = sale;
		
		ps = new PersistentStorage(dbFileName);
		
		//화면 구성
		buildGUI();
		registerEventHandler();
		
		//Observer패턴 initialize(sale) 구현
		sale.addPropertyListener(this);
	}
	
	private void buildGUI() {
		Container cp = this.getContentPane();
		cp.setLayout(null);
		
		
		messageJTextArea = new JTextArea();
		messageJTextArea.setBounds(280, 30, 310, 600);
		messageJTextArea.setBorder(
				BorderFactory.createLoweredBevelBorder());
		messageJTextArea.setEditable(false);
		messageJTextArea.setText("판매를 시작하려면 makeNewSale( )버튼을 눌러주세요."); 
		cp.add(messageJTextArea);
		
		b_makeNewSale = new JButton();
		b_makeNewSale.setBounds(30, 30, 240, 35);
		b_makeNewSale.setText("1. makeNewSale( )");
		b_makeNewSale.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_makeNewSale);
		
		l_itemID = new JLabel();
		l_itemID.setBounds(35, 70, 80, 35);
		l_itemID.setText("Item ID :");
		cp.add(l_itemID);
		
		c_itemID = new JComboBox();
		c_itemID.setBounds(130, 70, 140, 30);
		c_itemID.addItem("");
		idsList = ps.loadItemIds();
		for(int i = 0; i < idsList.size(); i++) {
			c_itemID.addItem(idsList.get(i));;
		}
		c_itemID.setSelectedIndex(0);
		cp.add(c_itemID);
		c_itemID.addItemListener(
				new ItemListener() 
				{
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						// TODO Auto-generated method stub
						c_itemIDItemstateChanged(e);
					}
				}
		);
		
		l_quantity = new JLabel();
		l_quantity.setBounds(35, 110, 80, 35);
		l_quantity.setText("Quantity :");
		cp.add(l_quantity);
		
		t_quantity = new JTextField();
		t_quantity.setBounds(130, 110, 140, 30);
		cp.add(t_quantity);
		
		l_desc = new JLabel();
		l_desc.setBounds(35, 150, 80, 35);
		l_desc.setText("Description :");
		cp.add(l_desc);
		
		t_desc = new JTextField();
		t_desc.setBounds(130, 150, 140, 30);
		t_desc.setEditable(false);
		cp.add(t_desc);
		
		b_enterItem = new JButton();
		b_enterItem.setBounds(30, 185, 240, 35);
		b_enterItem.setText("2. enterItem( )(반복)");
		b_enterItem.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_enterItem);
		
		l_currentTotal = new JLabel();
		l_currentTotal.setBounds(35, 225, 80, 35);
		l_currentTotal.setText("Current Total :");
		cp.add(l_currentTotal);
		
		t_currentTotal = new JTextField();
		t_currentTotal.setBounds(130, 225, 140, 30);
		t_currentTotal.setEditable(false);
		cp.add(t_currentTotal);
		
		b_endSale = new JButton();
		b_endSale.setBounds(30, 260, 240, 35);
		b_endSale.setText("3. endSale( )");
		b_endSale.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_endSale);
		
		r_taxMaster = new JRadioButton("TaxMaster");
		r_taxMaster.setBounds(30, 300, 100, 35);
		cp.add(r_taxMaster);
		bg1.add(r_taxMaster);
		r_taxMaster.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						System.clearProperty("taxcalculator.class.name");
						System.setProperty("taxcalculator.class.name", "pos.domainlayer.TaxMasterAdapter");
					}
				}
				);
		
		r_goodAsGoldTaxPro = new JRadioButton("GoodAsGoldTaxPro");
		r_goodAsGoldTaxPro.setBounds(130, 300, 140, 35);
		cp.add(r_goodAsGoldTaxPro);
		bg1.add(r_goodAsGoldTaxPro);
		r_goodAsGoldTaxPro.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						System.clearProperty("taxcalculator.class.name");
						System.setProperty("taxcalculator.class.name", "pos.domainlayer.GoodAsGoldTaxProAdapter");
					}
				}
				);
		
		b_calculateTax = new JButton();
		b_calculateTax.setBounds(30, 335, 240, 35);
		b_calculateTax.setText("4. calculateTax( )");
		b_calculateTax.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_calculateTax);
		
		l_totalWithTax = new JLabel();
		l_totalWithTax.setBounds(35, 375, 100, 35);
		l_totalWithTax.setText("Total With Tax :");
		cp.add(l_totalWithTax);
		
		t_totalWithTax = new JTextField();
		t_totalWithTax.setBounds(130, 375, 140, 30);
		t_totalWithTax.setEditable(false);
		cp.add(t_totalWithTax);

		r_bestForCustomer = new JRadioButton();
		r_bestForCustomer.setBounds(30, 405, 130, 35);
		r_bestForCustomer.setText("BestForCustomer");
		cp.add(r_bestForCustomer);
		bg2.add(r_bestForCustomer);
		r_bestForCustomer.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						bestForCustomer = new CompositeBestForCustomerPricingStrategy();
					}
				}
				);
		
		r_bestForStore = new JRadioButton();
		r_bestForStore.setBounds(160, 405, 100, 35);
		r_bestForStore.setText("BestForStore");
		cp.add(r_bestForStore);
		bg2.add(r_bestForStore);
		r_bestForStore.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						bestForStore = new CompositeBestForStorePricingStrategy();
					}
				}
				);
		
		b_applyDiscount = new JButton();
		b_applyDiscount.setBounds(30, 440, 240, 35);
		b_applyDiscount.setText("5. applyDiscount( )");
		b_applyDiscount.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_applyDiscount);
		
		l_totalAfterDiscount = new JLabel();
		l_totalAfterDiscount.setBounds(10, 480, 120, 35);
		l_totalAfterDiscount.setText("Total After Discount :");
		cp.add(l_totalAfterDiscount);
		
		t_totalAfterDiscount = new JTextField();
		t_totalAfterDiscount.setBounds(130, 480, 140, 30);
		t_totalAfterDiscount.setEditable(false);
		cp.add(t_totalAfterDiscount);
		
		l_amount = new JLabel();
		l_amount.setBounds(35, 520, 80, 35);
		l_amount.setText("Amount :");
		cp.add(l_amount);
		
		t_amount = new JTextField();
		t_amount.setBounds(130, 520, 140, 30);
		cp.add(t_amount);
		
		b_makePayment = new JButton();
		b_makePayment.setBounds(30, 555, 240, 35);
		b_makePayment.setText("6. makePayment( )");
		b_makePayment.setBorder(BorderFactory.createRaisedBevelBorder() );
		cp.add(b_makePayment);
		
		l_balance = new JLabel();
		l_balance.setBounds(35, 595, 80, 35);
		l_balance.setText("Balance :");
		cp.add(l_balance);
		
		t_balance = new JTextField();
		t_balance.setBounds(130, 600, 140, 30);
		t_balance.setEditable(false);
		cp.add(t_balance);
		
		//default 화면 enabled
		b_makeNewSale.setEnabled(true);
		c_itemID.setEnabled(false);
		t_quantity.setEnabled(false);
		t_desc.setEnabled(false);
		b_enterItem.setEnabled(false);
		t_currentTotal.setEnabled(false);
		b_endSale.setEnabled(false);
		r_taxMaster.setEnabled(false);
		r_goodAsGoldTaxPro.setEnabled(false);
		b_calculateTax.setEnabled(false);
		t_totalWithTax.setEnabled(false);
		r_bestForCustomer.setEnabled(false);
		r_bestForStore.setEnabled(false);
		b_applyDiscount.setEnabled(false);
		t_totalAfterDiscount.setEnabled(false);
		t_amount.setEnabled(false);
		b_makePayment.setEnabled(false);
		t_balance.setEnabled(false);
		
		
		setTitle("POS System (학번:20141118 이름:전수현)");
		setSize(650,700);
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

	private void registerEventHandler() {
		b_makeNewSale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				messageJTextArea.setText("");
				sale = register.makeNewSale();

				b_makeNewSale.setEnabled(false);
				
				c_itemID.setEnabled(true);
				t_quantity.setEnabled(true);
				t_desc.setEnabled(true);
				b_enterItem.setEnabled(true);
				t_currentTotal.setEnabled(true);
				
				b_endSale.setEnabled(false);
				r_taxMaster.setEnabled(false);
				r_goodAsGoldTaxPro.setEnabled(false);
				b_calculateTax.setEnabled(false);
				t_totalWithTax.setEnabled(false);
				r_bestForCustomer.setEnabled(false);
				r_bestForStore.setEnabled(false);
				b_applyDiscount.setEnabled(false);
				t_totalAfterDiscount.setEnabled(false);
				t_amount.setEnabled(false);
				b_makePayment.setEnabled(false);
				t_balance.setEnabled(false);
				
				t_currentTotal.setText("");
				t_amount.setText("");
				t_balance.setText("");
				messageJTextArea.setText( "새 판매가 시작되었습니다." 
						+ "\nItemID----Description-----Price------Quantity----SubTotal");
			}
			
		});
		
		b_enterItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					ItemID id = new ItemID(c_itemID.getSelectedItem().toString());
					int q = Integer.parseInt(t_quantity.getText());
					Money price = ps.getPrice(id.toString());
					int subTotal = Integer.parseInt(price.toString())*q;
					
					register.enterItem(id, q); //send message to controller
					
					messageJTextArea.append( "\n" + id.getId() + "----" + ps.getDesc(id.toString())
						+ "-----" + price + "------" + q + "----" + subTotal);
					onPropertyEvent(sale, "sale.total", sale.getTotal());
				} catch (Exception ex) {
					
					JOptionPane.showMessageDialog(null, "숫자를 입력해주세요", "WARNING", JOptionPane.ERROR_MESSAGE);
					
				}
				
				t_quantity.setText("");
				
				b_makeNewSale.setEnabled(false);
				
				c_itemID.setEnabled(true);
				t_quantity.setEnabled(true);
				t_desc.setEnabled(true);
				b_enterItem.setEnabled(true);
				t_currentTotal.setEnabled(true);
				b_endSale.setEnabled(true);
				
				r_taxMaster.setEnabled(false);
				r_goodAsGoldTaxPro.setEnabled(false);
				b_calculateTax.setEnabled(false);
				t_totalWithTax.setEnabled(false);
				r_bestForCustomer.setEnabled(false);
				r_bestForStore.setEnabled(false);
				b_applyDiscount.setEnabled(false);
				t_totalAfterDiscount.setEnabled(false);
				t_amount.setEnabled(false);
				b_makePayment.setEnabled(false);
				t_balance.setEnabled(false);
			}
		});
		
		b_endSale.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t_quantity.setText("");
				t_desc.setText("");
				c_itemID.setSelectedIndex( 0 );
				
				b_makeNewSale.setEnabled(false);
				c_itemID.setEnabled(false);
				t_quantity.setEnabled(false);
				t_desc.setEnabled(false);
				b_enterItem.setEnabled(false);
				t_currentTotal.setEnabled(false);
				b_endSale.setEnabled(false);
				
				r_taxMaster.setEnabled(true);
				r_goodAsGoldTaxPro.setEnabled(true);
				b_calculateTax.setEnabled(true);
				
				t_totalWithTax.setEnabled(false);
				r_bestForCustomer.setEnabled(false);
				r_bestForStore.setEnabled(false);
				b_applyDiscount.setEnabled(false);
				t_totalAfterDiscount.setEnabled(false);
				t_amount.setEnabled(false);
				b_makePayment.setEnabled(false);
				t_balance.setEnabled(false);
				
				t_amount.setText("");
				messageJTextArea.append( "\n------------------------------------------총액 : " + sale.getTotal() 
						+ "\n상품 입력이 끝났습니다."
						+ "\n세금 계산 방법을 고르고 calculateTax()버튼을 눌러주세요.");
				register.endSale();
			}
		});
		
		b_calculateTax.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				b_makeNewSale.setEnabled(false);
				c_itemID.setEnabled(false);
				t_quantity.setEnabled(false);
				t_desc.setEnabled(false);
				b_enterItem.setEnabled(false);
				t_currentTotal.setEnabled(false);
				b_endSale.setEnabled(false);
				r_taxMaster.setEnabled(false);
				r_goodAsGoldTaxPro.setEnabled(false);
				b_calculateTax.setEnabled(false);
						
				t_totalWithTax.setEnabled(true);
				r_bestForCustomer.setEnabled(true);
				r_bestForStore.setEnabled(true);
				b_applyDiscount.setEnabled(true);
						
				t_totalAfterDiscount.setEnabled(false);
				t_amount.setEnabled(false);
				b_makePayment.setEnabled(false);
				t_balance.setEnabled(false);
				
				sale.initialize();
				
				if(r_taxMaster.isSelected()) {
					t_totalWithTax.setText(sale.getTotalWithTax().toString());
				}
				else{
					t_totalWithTax.setText(sale.getTotalWithTax().toString());
				}
				
				messageJTextArea.append("\n할인 방법을 고른 후 applyDiscount( )버튼을 눌러주세요.");
			}
		});
		
		b_applyDiscount.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				b_makeNewSale.setEnabled(false);
				c_itemID.setEnabled(false);
				t_quantity.setEnabled(false);
				t_desc.setEnabled(false);
				b_enterItem.setEnabled(false);
				t_currentTotal.setEnabled(false);
				b_endSale.setEnabled(false);
				r_taxMaster.setEnabled(false);
				r_goodAsGoldTaxPro.setEnabled(false);
				b_calculateTax.setEnabled(false);
				t_totalWithTax.setEnabled(false);
				r_bestForCustomer.setEnabled(false);
				r_bestForStore.setEnabled(false);
				b_applyDiscount.setEnabled(false);
				
				t_totalAfterDiscount.setEnabled(true);
				t_amount.setEnabled(true);
				b_makePayment.setEnabled(true);
				
				t_balance.setEnabled(false);
				
				if(r_bestForCustomer.isSelected()) {
					t_totalAfterDiscount.setText(sale.applyDiscount(bestForCustomer, sale).toString());
				}
				else {
					t_totalAfterDiscount.setText(sale.applyDiscount(bestForStore, sale).toString());
				}
				
				messageJTextArea.append("\n받은 돈을 입력한 후 makePayment( )버튼을 눌러주세요.");
			}
		});
		b_makePayment.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Money payment = new Money(Integer.parseInt(t_amount.getText().toString()));
				
				register.makePayment(payment);
				
				b_makeNewSale.setEnabled(true);
				
				c_itemID.setEnabled(false);
				t_quantity.setEnabled(false);
				t_desc.setEnabled(false);
				b_enterItem.setEnabled(false);
				t_currentTotal.setEnabled(false);
				b_endSale.setEnabled(false);
				r_taxMaster.setEnabled(false);
				bg1.clearSelection();
				r_goodAsGoldTaxPro.setEnabled(false);
				r_goodAsGoldTaxPro.setSelected(false);
				b_calculateTax.setEnabled(false);
				t_totalWithTax.setEnabled(false);
				r_bestForCustomer.setEnabled(false);
				r_bestForCustomer.setSelected(false);
				r_bestForStore.setEnabled(false);
				r_bestForStore.setSelected(false);
				bg2.clearSelection();
				b_applyDiscount.setEnabled(false);
				t_totalAfterDiscount.setEnabled(false);
				t_amount.setEnabled(false);
				b_makePayment.setEnabled(false);
				
				t_balance.setEnabled(true);
				
				t_totalWithTax.setText("");
				t_totalAfterDiscount.setText("");
				messageJTextArea.append("\n판매가 종료되었습니다."
						+ "\n새로운 판매를 시작하시려면 makeNewSale( )버튼을 눌러주세요.");

				t_balance.setText(sale.getBalance().toString());
			}
		});
	}
	
	private void c_itemIDItemstateChanged(ItemEvent event) {
		if((event.getStateChange() == ItemEvent.SELECTED) &&
				c_itemID.getSelectedIndex() != 0) 
		{
				t_desc.setText(ps.getDesc(c_itemID.getSelectedItem().toString()));	
		}
	}
	
	@Override
	public void onPropertyEvent(Sale s, String name, Money value) {
		if(name.equals("sale.total"))
			t_currentTotal.setText(value.toString());
	}
}
