package javaWizard.javaWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
*	Sample test program to show 13 frames using this Wizard
*	component.
*
*/

public class WizardFrame extends JFrame {
	public WizardFrame() {
		try {
			
			setSize(800, 600);
			show();
			WizardModal wm = new WizardModal();
			wm.setSize(400, 300);
			wm.setLocation(200, 100);
			TestFrame tf1 = new TestFrame("One");
			tf1.setSize(100, 100);
			TestFrame tf2 = new TestFrame("Two");
			tf2.setSize(100, 100);
			TestFrame1 tf3 = new TestFrame1();
			tf3.setSize(100, 100);
			TestFrame tf4 = new TestFrame("Four");
			tf4.setSize(300, 400);
			TestFrame tf5 = new TestFrame("Five");
			tf5.setSize(300, 400);
			TestFrame tf6 = new TestFrame("Six");
			tf6.setSize(300, 400);
			TestFrame tf7 = new TestFrame("Seven");
			tf7.setSize(300, 400);
			TestFrame tf8 = new TestFrame("Eight");
			tf8.setSize(300, 400);
			TestFrame tf9 = new TestFrame("Nine");
			tf9.setSize(300, 400);
			TestFrame tf10 = new TestFrame("Ten");
			tf10.setSize(300, 400);
			TestFrame tf11 = new TestFrame("Eleven");
			tf11.setSize(300, 400);
			TestFrame tf12 = new TestFrame("Twelve");
			tf12.setSize(300, 400);
			TestFrame tf13 = new TestFrame("Thirteen");
			tf13.setSize(300, 400);

			Object[] obj = new Object[13];
			obj[0] = tf1;
			obj[1] = tf2;
			obj[2] = tf3;
			obj[3] = tf4;
			obj[4] = tf5;
			obj[5] = tf6;
			obj[6] = tf7;
			obj[7] = tf8;
			obj[8] = tf9;
			obj[9] = tf10;
			obj[10] = tf11;
			obj[11] = tf12;
			obj[12] = tf13;

			wm.addPages(obj);
			wm.show();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					System.exit(0);
				}
			});
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new WizardFrame();
	}
}

/**
*	This is the modal dialog box that contains the Wizard screen.
*
*/

class WizardModal extends JDialog {
	java.util.Vector pages = new java.util.Vector();
	static JPanel jp = new JPanel();
	static JPanel jp1 = new JPanel();
	JButton jbNext;
	JButton jbPrevious;
	JButton jbFinish;

	int pageCounter = 0;
	static WizardModal wm;
	boolean nextStatus = true;
	boolean previousStatus = true;
	boolean finishStatus = false;

	JPanel jp2;

	public WizardModal() {
		setModal(true);
		jbNext = new JButton("Next");
		jbPrevious = new JButton("Previous");
		jbFinish = new JButton("Finish");
		jp.add(jbNext);
		jp.add(jbPrevious);
		jp.add(jbFinish);
		this.getContentPane().add(jp, BorderLayout.SOUTH);
		this.getContentPane().add(jp1, BorderLayout.CENTER);
		validateButtons(pageCounter);
		wm = this;
		jbNext.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getSource() == jbNext && nextStatus) {
					if (jp2 instanceof TestFrame1) {
						System.out.println(((TestFrame1) jp2).jtxt1.getText());
					}

					pageCounter++;
					decidePage(pageCounter);
					validateButtons(pageCounter);
					wm.show();
				}
			}
		});
		jbPrevious.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getSource() == jbPrevious && previousStatus) {
					pageCounter--;
					decidePage(pageCounter);
					validateButtons(pageCounter);
					wm.show();
				}
			}
		});

	}
	public void show() {
		super.show();
	}
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
	}
	public void setSize(int w, int h) {
		super.setSize(w, h);
	}
	public void setPage(int index, Object page) {
		pages.set(index, page);
	}
	public Object getPage(int index) {
		return pages.get(index);
	}
	public Object firstPage() {
		return pages.firstElement();
	}
	public Object lastPage() {
		return pages.lastElement();
	}
	public Object pageAt(int index) {
		return pages.elementAt(index);
	}
	public void addPages(Object[] pages1) {
		for (int i = 0; i < pages1.length; i++) {
			pages.addElement(pages1[i]);
		}
	}
	/**
	*	Validating which button to show and which one to disable.
	*
	*/

	public void validateButtons(int pageCounter) {
		if (pageCounter == 0) {
			jbPrevious.setEnabled(false);
			previousStatus = false;
			jbFinish.setEnabled(false);
			finishStatus = false;

		}
		else {
			jbPrevious.setEnabled(true);
			previousStatus = true;
		}

		if (pageCounter == pages.size() - 1) {
			jbNext.setEnabled(false);
			nextStatus = false;
			jbFinish.setEnabled(true);
			finishStatus = true;

		}
		else {

			jbNext.setEnabled(true);
			nextStatus = true;
			jbFinish.setEnabled(false);
			finishStatus = false;

		}

	}
	/**
	*	This is the method that decides which page to show.
	*
	*/

	public void decidePage(int pageCounter) {
		this.getContentPane().remove(1);
		jp2 = (JPanel) pageAt(pageCounter);
		this.getContentPane().add(jp2, BorderLayout.CENTER);
		this.setTitle("Page no:" + (pageCounter + 1));
		this.getContentPane().update(jp2.getGraphics());
	}
}

/**
*	This is a test frame to be enbedded onto the wizard screen.
*
*/

class TestFrame extends JPanel {
	JLabel jlbl;
	String lbl;
	public TestFrame(String title) {
		lbl = title;
		jlbl = new JLabel(title);
		setLayout(new BorderLayout());
		add(jlbl, BorderLayout.SOUTH);
	}
}

/**
*	This is a test frame to be enbedded onto the wizard screen.
*
*/

class TestFrame1 extends JPanel {
	JLabel jlbl;
	public JTextField jtxt;
	public JLabel jlbl1;
	public JTextField jtxt1;
	JLabel jlbl2;
	JTextField jtxt2;

	public TestFrame1() {
		jlbl = new JLabel("Name : ");
		jtxt = new JTextField(10);
		jlbl1 = new JLabel("Address : ");
		jtxt1 = new JTextField(10);
		jlbl2 = new JLabel("Phone : ");
		jtxt2 = new JTextField(10);
		setLayout(new FlowLayout());
		add(jlbl);
		add(jtxt);
		add(jlbl1);
		add(jtxt1);
		add(jlbl2);
		add(jtxt2);
	}
}
