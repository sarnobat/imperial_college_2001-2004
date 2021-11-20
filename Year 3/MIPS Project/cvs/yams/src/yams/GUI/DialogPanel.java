package yams.GUI;

import java.awt.*;
import javax.swing.*;
import java.io.*;

import yams.*;


public class DialogPanel extends YamsPanel {

	// window elements
	JTextArea textDisplay;
	JScrollPane scrollPane;
	
	private OutputStream out = new ConsoleStream(this);
	private PrintStream pout = new PrintStream(out,true);
	
	public DialogPanel(YAMSGui controller) {
		super(controller);
		setTitle("Console I/O");
		
		this.setLayout(new GridLayout(1,1));
		
		textDisplay = new JTextArea();
		textDisplay.setVisible(true);
		textDisplay.setEditable(false);
		textDisplay.setLineWrap(false);
		textDisplay.setWrapStyleWord(true);
		textDisplay.setFont(new Font("Monospaced", Font.PLAIN, 10));
		
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(textDisplay);
		
		this.add(scrollPane);
		
	}
	
	/**
	 * Returns the output stream so that external
	 * objects can write to the console
	 * @return output print stream for the console text box
	 */
	public PrintStream getPrintStream() {
		return pout;
	}
	
	/**
	 * Appends the given text to the text in the console window
	 * @param text text to add to console output window
	 */
	private void addText(String text) {
		// Adds text to the JTextArea
		textDisplay.setText(textDisplay.getText() + text);
		scrollPane.getViewport().scrollRectToVisible(new Rectangle(0, textDisplay.getHeight(), 1, 0));
	}
	
	/**
	 * Erases all text in the console window
	 *
	 */
	public void clearText() {
		textDisplay.setText("");
	}
	 

	/**
	 * Output stream from which to create a print stream for console output
	 */
	class ConsoleStream extends OutputStream {
		
		private DialogPanel d;
		private StringBuffer buffer = new StringBuffer();
		
		ConsoleStream(DialogPanel d) {
			this.d = d;
		}
		
		public void write(int b) {
			buffer.append((char)b);
		}
		
		public void flush() {
			d.addText(buffer.toString());
			buffer = new StringBuffer();
		}
	} 

}
