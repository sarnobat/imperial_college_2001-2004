
package yams;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.*;

import yams.GUI.*;
import yams.assembler.*;
import yams.processor.*;
import yams.parser.*;


public class YAMSGui extends YAMSController {
	
	// window elements
	
	private JFrame mainFrame;
	private FileSelectorPanel files;
	private StatisticsPanel stats;
	private RegistersPanel regs;
	private DataPanel data;
	private DialogPanel dialog;
	private ProgramCodePanel code;
	
	private JMenuBar menuBar;
	// FILE MENU
	private JMenu mnuFile;
	private JMenuItem mnuItemAddFile;
	private JMenuItem mnuItemAddFileList;
	private JMenuItem mnuItemRemoveFile;
	private JMenuItem mnuItemLoadFile;
	private JMenuItem mnuItemExit;
	// PROCESSOR MENU
	private JMenu mnuProcessor;
	private JMenuItem mnuItemReloadFile;
	private JMenuItem mnuItemResetBreakpoints;
	private JMenuItem mnuItemDisplayStats;
	private JCheckBoxMenuItem mnuItemVerbose;
	// HELP MENU
	private JMenu mnuHelp;
	private JMenuItem mnuItemAbout;

	// other useful stuff
	
	protected Assembler asm;
	protected Processor processor;
	protected Parser parser;
	private ProcessorHandler processorHandler;
	private LineList instrList = null;

	
	private boolean fileLoaded = false;
	private File currentFile;
	private boolean running = false;
	
	private String currentDirectory = System.getProperty("user.dir");
	

	public void start(String[] args) {

		LinkedList files = new LinkedList();
		boolean verbose = false;

		// set up GUI
		setUpGui();

		// create processor, depending on verbose option
		if (verbose) {
			processor = new Processor(this, System.in, dialog.getPrintStream(), dialog.getPrintStream());
		} else {
			processor = new Processor(this, System.in, dialog.getPrintStream(), new PrintStream(new NullStream()));
		}
		
		// create assembler
		asm = new Assembler("Instruction_file.xml", processor.memoryManager, processor.statisticsManager, System.out);

		// parse command line arguments
		for (int i = 1; i < args.length; i++) {
			
			if (args[i].equalsIgnoreCase("-if")) {
				// next argument is an input file containing a list of files
				if (i == args.length - 1) {
					// but no more options
					System.err.println("-if needs an argument");
					continue;
				} else {
					i++;
					loadFileList(new File(args[i]));

				}
				
			} else if (args[i].equalsIgnoreCase("-v")) {
				verboseOutput(true);

			} else {
				// assume it's a file
				files.add(new File(args[i]));

			}
		}
		
		// initalise data panel and registers panel
		// (must be done after procesor has been created)
		this.data.init();
		this.regs.init();
		
	}
	
	
	private void setUpGui() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e) {
		}
		
		// create main frame
		mainFrame = new JFrame("Yet Another MIPS Simulator");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add menu bar
		MenuHandler menuHandler = new MenuHandler(this);
		JMenuBar menuBar = new JMenuBar();
		
		// FILE MENU
		mnuFile = new JMenu("File");
		
		mnuItemAddFile = new JMenuItem("Add File...");
		mnuItemAddFile.addActionListener(menuHandler);
		mnuFile.add(mnuItemAddFile);
		
		mnuItemAddFileList = new JMenuItem("Add File List...");
		mnuItemAddFileList.addActionListener(menuHandler);
		mnuFile.add(mnuItemAddFileList);
		
		mnuItemRemoveFile = new JMenuItem("Remove File");
		mnuItemRemoveFile.addActionListener(menuHandler);
		mnuItemRemoveFile.setEnabled(false);
		mnuFile.add(mnuItemRemoveFile);
		
		mnuItemLoadFile = new JMenuItem("Load File");
		mnuItemLoadFile.addActionListener(menuHandler);
		mnuItemLoadFile.setEnabled(false);
		mnuFile.add(mnuItemLoadFile);
		
		mnuFile.addSeparator();
		
		mnuItemExit = new JMenuItem("Exit");
		mnuItemExit.addActionListener(menuHandler);
		mnuFile.add(mnuItemExit);
		
		// PROCESSOR MENU
		mnuProcessor = new JMenu("Processor");
		
		mnuItemReloadFile = new JMenuItem("Reload File");
		mnuItemReloadFile.addActionListener(menuHandler);
		mnuProcessor.add(mnuItemReloadFile);
		
		mnuItemResetBreakpoints = new JMenuItem("Reset Breakpoints");
		mnuItemResetBreakpoints.addActionListener(menuHandler);
		mnuProcessor.add(mnuItemResetBreakpoints);
		
		mnuItemDisplayStats = new JMenuItem("Display Statistics");
		mnuItemDisplayStats.addActionListener(menuHandler);
		mnuProcessor.add(mnuItemDisplayStats);
		
		mnuProcessor.addSeparator();
		
		mnuItemVerbose = new JCheckBoxMenuItem("Verbose Output");
		mnuItemVerbose.addItemListener(menuHandler);
		mnuProcessor.add(mnuItemVerbose);
		
		// HELP MENU
		mnuHelp = new JMenu("Help");
		
		mnuItemAbout = new JMenuItem("About...");
		mnuItemAbout.addActionListener(menuHandler);
		mnuHelp.add(mnuItemAbout);
		
		
		menuBar.add(mnuFile);
		menuBar.add(mnuProcessor);
		menuBar.add(mnuHelp);
		mainFrame.setJMenuBar(menuBar);
		

		// create panels
		files = new FileSelectorPanel(this);
		stats = new StatisticsPanel(this);
		code = new ProgramCodePanel(this);
		data = new DataPanel(this);
		dialog = new DialogPanel(this);
		regs = new RegistersPanel(this);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		// set preferred sizes
		files.setPreferredSize(new Dimension(230, Integer.MAX_VALUE));
		regs.setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
		stats.setPreferredSize(new Dimension(200,150));
		dialog.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
		
		// create main panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel codeData = new JPanel();
		codeData.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		codeData.setLayout(new GridBagLayout());
		c.weightx = 0.5;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 1;
		codeData.add(code,c);
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 2;
		codeData.add(data,c);
		
		JPanel statsDialog = new JPanel();
		statsDialog.setLayout(new GridBagLayout());
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		statsDialog.add(stats,c);
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		statsDialog.add(dialog,c);
		
		mainPanel.add(files, BorderLayout.LINE_START);
		mainPanel.add(codeData, BorderLayout.CENTER);
		mainPanel.add(regs, BorderLayout.LINE_END);
		mainPanel.add(statsDialog, BorderLayout.PAGE_END);

		mainFrame.getContentPane().add(mainPanel);
		
		mainFrame.setSize(800,600);
		mainFrame.show();
	}
	
	
	//
	// Below are all the general GUI controller functions
	//
	
	// Event handlers
	
	/**
	 * Opens dialog to select a file and adds it to the list
	 */
	public void addFile() {
		// open a dialog to add a file to the list
		JFileChooser fc = new JFileChooser(currentDirectory);
		int returnVal = fc.showOpenDialog(mainFrame);
		
		// if they clicked open, add the chosen file to the list
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
			currentDirectory  = fc.getSelectedFile().getPath();
			files.addFile(fc.getSelectedFile());
		}
	}
	
	
	/**
	 * Opens dialog select a filelist file and adds files listed in it
	 */
	public void addFileList() {
		// open a dialog to add a list of files
		JFileChooser fc = new JFileChooser(currentDirectory);
		int returnVal = fc.showOpenDialog(mainFrame);
			
		// if they clicked open, read from the chosen file and add contents to the list
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
			currentDirectory  = fc.getSelectedFile().getPath();
			loadFileList(fc.getSelectedFile());
		}
	}
	
	
	/**
	 * Removes currently selected file from list
	 */
	public void removeFile() {
		// remove file from the list
		int index = files.lstFileList.getSelectedIndex();
		files.fileList.remove(index);
			
		// reset current slection to something
		if ( index == files.fileList.getSize() )
			index--;
		files.lstFileList.setSelectedIndex(index);
		files.lstFileList.ensureIndexIsVisible(index);
			
		// if list is empty, disable button
		if ( files.fileList.getSize() == 0 ) {
			files.butRemFile.setEnabled(false);
		}
	}
	
	
	/**
	 * Loads currently selected file in list 
	 */
	public void loadFile() {
		// if there is a program running, ask whether we should stop it
		if (processorHandler != null) {
			if (processorHandler.isRunning()) {
				int r = JOptionPane.showConfirmDialog(mainFrame, "Cancel running program?", "YAMS", JOptionPane.YES_NO_OPTION);
				if (r == JOptionPane.NO_OPTION) {
					return;
				} else {
					processorHandler.processorStop();
				}
			}
		}
		// load file into parser, assembler and processor
		int index = files.lstFileList.getSelectedIndex();
		loadFile((File)files.fileList.elementAt(index));
	}
	
	
	/**
	 * Exits the program
	 */
	public void exit() {
		System.exit(0);
	}
	
	/**
	 * Reloads the currently loaded file
	 */
	public void reloadFile() {
		// check if a program is running, and if so, prompt to cancel it
		if (processorHandler != null) {
			if (processorHandler.isRunning()) {
				int r = JOptionPane.showConfirmDialog(mainFrame, "Cancel running program?", "YAMS", JOptionPane.YES_NO_OPTION);
				if (r == JOptionPane.NO_OPTION) {
					return;
				} else {
					processorHandler.processorStop();
				}
			}
		}		
		loadFile(currentFile);
	}
	
	
	/**
	 * Removes all breakpoints
	 */
	public void resetBreakpoints() {
		code.resetBreakpoints();
	}
	
	
	/**
	 * Displays graphical statistics window
	 */
	public void displayStats() {
		// if a file has been loaded, then display stats
		if (fileLoaded) {
			stats.displayGraphs(instrList);
		} else {
			JOptionPane.showMessageDialog(mainFrame, "No file loaded." + '\n' + "Cannot draw graphs.", "YAMS Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	/**
	 * Sets verbose mode
	 * @param value true causes verbose output from the processor to be displayed
	 * 				false hides it
	 */
	public void verboseOutput(boolean value) {
		if (value) {
			processor.setVerbose(dialog.getPrintStream());
		} else {
			processor.setVerbose(new PrintStream(new NullStream()));
		}
		mnuItemVerbose.setState(value);
	}
	
	
	/**
	 * Displays 'About' window
	 */
	public void about() {
		// TODO: create about window
	}
	
	
	/**
	 * Start the processor running
	 */
	public void processorStart() {
		processorHandler.processorStart();
	}
	
	
	/**
	 * Stop the processor running
	 */
	public void processorStop() {
		processorHandler.processorStop();
	}
	
	
	/**
	 * Step to the next line
	 */
	public void processorStep() {
		// execute one instruction
		processorHandler.runOne();
	}
	
	
	/**
	 * Skip the next line
	 */
	public void processorSkip() {
		// skip next line
		processorHandler.runSkip();
	}
	
	
	
	// General Methods
	
	// Methods to do with file list
	
	/**
	 * Unloads any currently loaded file, and parses, assembles and sets up the
	 * processor ready to execute the new file
	 * @ param file The file to load
	 */
	public void loadFile(File file) {
		PrintStream out = dialog.getPrintStream();
		boolean error = false;
		// set file loaded to false
		setFileLoaded(false);
		currentFile = null;
		
		// UNLOAD old file
		// reset memory manager, register manager, statistics manager, cycleManager
		//   assembler, programCode panel and data panel
		processor.reset();
		asm.resetAssembler();
		code.reset();
		data.reset();
		
		// destroy old processor handler thread (if it exists)
		if (processorHandler != null) {
			processorHandler.destroy();
		}
		
		// LOAD new file
		parser = new Parser();
		StringBuffer programCode;
		try {
			// load, parse and assemble
			programCode = loadCode(file);
			instrList = parser.parse(programCode);
			asm.assembleCode(instrList);
		}
		catch (FileNotFoundException e) {
			out.println("File Not Found: " + file.getName());
			error = true;
		}
		catch (LexerException e) {
			out.println("Lexer Error: " + e.toString());
			error = true;
		}
		catch (ParseException e) {
			out.println("Parse Error: " + e.toString());
			error = true;
		}
		catch (AssemblerException e) {
			out.println("Assembler Error: " + e.toString());
			error = true;
		}
		
		if (!error) {
			// UPDATE display
			dialog.clearText();
			processor.cycleManager.jump(0x400000);
			setFileLoaded(true);
			currentFile = file;
			processorHandler = new ProcessorHandler(this, processor);
			processorHandler.start();
			code.setSourceCode(instrList);
			updateBreakPointPanel();
		}
	}
	
	
	
	/**
	 * Loads all the valid existing files from the files described in fileName
	 */
	public void loadFileList(File fileName) {
		FileReader file;
		BufferedReader input = null;
		String path = (fileName.getPath()).substring(0, fileName.getPath().length() - fileName.getName().length());
		try {
			file = new FileReader(fileName);
			input = new BufferedReader(file);
		} catch (FileNotFoundException ee){
			System.err.println("Input file list not found: " + fileName.getName());
			return;
		}
		String line = "";
		try {
			File temp;
			while ((line = input.readLine()) != null) {
				temp = new File(path + line);
				if (temp.exists()) {
					files.addFile(temp);
				} else {
					System.err.println("File does not exist: " + temp.getName());
				}
			}
		} catch (IOException ee) {
			// error reading from file
			JOptionPane.showMessageDialog(mainFrame, "Error reading from file: " + line, "YAMS Error", JOptionPane.ERROR);
		}
	}
	
	/**
	 * Sets whether remove/load is allowed (changes enabled state of menu and buttons)
	 */
	public void setRemoveLoadStatus(boolean value) {
		files.butLoadFile.setEnabled(value);
		files.butRemFile.setEnabled(value);
		mnuItemLoadFile.setEnabled(value);
		mnuItemRemoveFile.setEnabled(value);
	}



	
	// Methods to do with processor
	
	/**
	 * Returns a handle to the processor (and hence to the classes within the processor)
	 */
	public Processor getProcessor() {
		return processor;
	}
	
	
	/**
	 * Updates the Processor Status message
	 * @param message The message to display
	 */
	public void updateProcessorStatus(String message) {
		code.setProcessorStatus(message);
	}
	
	
	/**
	 * Set the speed of the processor
	 * @param speed Delay between instructions in milliseconds
	 */
	public void setProcessorSpeed(int speed) {
		// processor handler may not exist yet
		if (processorHandler != null) {
			processorHandler.setDelay(speed);
		}
	}
	
	
	/**
	 * Get the speed of the processor from the processor
	 * @return Returns the delay between instructions in milliseconds
	 */
	public int getProcessorSpeedFromProcessor() {
		return processorHandler.getDelay();
	}
	
	
	/**
	 * Get the speed setting of the processor from the slider in the GUI
	 * @return Returns the current selection of the slider
	 */
	public int getProcessorSpeedFromGUI() {
		return code.getSpeed();
	}
	
	
	
	
	// Methods to do with Program Flow/Breakpoint window
	
	/**
	 * Enable the program execution buttons
	 */
	public void enableProgramCodeButtons() {
		code.butRun.setEnabled(true);
		code.butStep.setEnabled(true);
		code.butSkipNext.setEnabled(true);
		code.butStop.setEnabled(true);
	}
	
	
	/**
	 * disable the program execution buttons (e.g. when the program has finished)
	 */
	public void disableProgramCodeButtons() {
		code.butRun.setEnabled(false);
		code.butStep.setEnabled(false);
		code.butSkipNext.setEnabled(false);
		code.butStop.setEnabled(false);
	}
	
	
	/**
	 * Work out the current line pointed to by the Program Counter
	 * @return Current line, or -1 if in the middle of a pseudo instruction
	 */
	public int getCurrentLine () {
		int regPC = processor.registerManager.getRegOnly("PC");
		if (asm.getGUIMap().containsAddress(regPC)) {
			return asm.getGUIMap().returnLine(regPC);
		} else {
			return -1;
		}
	}
	
	
	/**
	 * Selects the 'currentLine' in the breakpoint window
	 */
	public void updateBreakPointPanel() {
		int currentLine = getCurrentLine();
		if (currentLine != -1) {
			code.setCurrentLine(currentLine);
		}
	}
	
	
	/**
	 * Finds whether the current line has a break point set
	 * @return true if a breakpoint is set, false if not
	 */
	public boolean currentLineHasBreakPoint() {
		int currentLine = getCurrentLine();
		if (currentLine == -1) {
			return false;
		} else {
			return code.getBreakPoint(currentLine);
		}
	}
	
	
	
	
	// Misc update methods
	
	/**
	 * Causes the statistics panel to update
	 */
	public void updateStatistics() {
		stats.update();
	}
	
	
	/**
	 * Updates the approiate register in the GUI
	 */
	public void regChanged(String regID) {
		regs.regChanged(regID);
	}
	
	
	/**
	 * Updates the approiate cells in the data panel
	 */
	public void memoryChanged(int address) {
		// update data panel
		data.memoryChanged(address);
	}
	
	
	/**
	 * Sets status of whether a file is loaded and enabled/disabled buttons accordingly
	 * @param value true if a file is loaded, false otherwise
	 */
	private void setFileLoaded(boolean value) {
		if (value) {
			// set fileLoaded to true and enable execution control buttons
			fileLoaded = true;
			enableProgramCodeButtons();
		} else {
			// set fileLoaded to false and disable execution control buttons
			fileLoaded = false;
			disableProgramCodeButtons();
		}
	}
	
	
	/**
	 * @return Returns whether a file is currently loaded or not
	 */
	public boolean getFileLoaded() {
		return fileLoaded;
	}
	
	
	/**
	 * @return Returns a refrence to the main frame
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
}


/**
 * A null-output stream used for disabling verbose output from the processor
 */
class NullStream extends OutputStream {
	NullStream() {}
	public void write(int b) {}
} 


/**
 * A menu handler for our menu
 */
class MenuHandler implements ActionListener, ItemListener {
	
	YAMSGui controller;
	
	MenuHandler(YAMSGui c) {
		controller = c;
	}
	
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem)e.getSource();
		
		if (source.getText().equals("Add File...")) {
			controller.addFile();
			
		} else if (source.getText().equals("Add File List...")) {
			controller.addFileList();
			
		} else if (source.getText().equals("Remove File")) {
			controller.removeFile();
			
		} else if (source.getText().equals("Load File")) {
			controller.loadFile();
			
		} else if (source.getText().equals("Reload File")) {
			controller.reloadFile();
			
		} else if (source.getText().equals("Exit")) {
			controller.exit();
			
		} else if (source.getText().equals("Reset Breakpoints")) {
			controller.resetBreakpoints();
			
		} else if (source.getText().equals("Display Statistics")) {
			controller.displayStats();
			
		} else if (source.getText().equals("About")) {
			controller.about();
			
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		JCheckBoxMenuItem source = (JCheckBoxMenuItem)e.getSource();
		controller.verboseOutput(source.getState());
	}
	
}
