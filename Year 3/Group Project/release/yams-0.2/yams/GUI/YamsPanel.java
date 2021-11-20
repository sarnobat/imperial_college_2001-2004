package yams.GUI;

import yams.*;

import javax.swing.*;

public class YamsPanel extends< Panel {
//TODO Shouldn't this be abstract?
	protected YAMSGui controller;

	public YamsPanel(YAMSGui controller) {
		this.controller = controller;
	}
	
	protected void setTitle(String title) {
		this.setBorder(BorderFactory.createTitledBorder(title));
	}
}
