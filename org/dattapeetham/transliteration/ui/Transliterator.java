package org.dattapeetham.transliteration.ui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.dattapeetham.transliteration.FileTransliterator;
import org.dattapeetham.transliteration.ICUHelper;

/**
 * 
 * 
 */
public class Transliterator extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
	JButton openButton, transliterateButton;
	JTextArea log;
	JFileChooser fc;

	JComboBox inputOptions, outputOptions;

	private File inputFile;

	public Transliterator() {
		super(new BorderLayout());

		// Create the log first, because the action listeners
		// need to refer to it.
		log = new JTextArea(10, 80);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create a file chooser
		fc = new JFileChooser();

		// TODO define the input languages and select outputs based on input
		// inputOptions = new
		// JComboBox(ICUHelper.getAvailableSources().toArray());
		// inputOptions.setSelectedItem(ICUHelper.TELUGU);

		// Get all output languages for Telugu and add them to the drop down
		outputOptions = new JComboBox(ICUHelper.getAvailableTargets(
				ICUHelper.TELUGU).toArray());
		outputOptions.setSelectedItem(ICUHelper.DEVANAGARI); // select default
		// target as
		// devanagari
		// Create the open button.
		openButton = new JButton("Select Source File : ");
		openButton.addActionListener(this);

		// Create the Transliterate button.
		transliterateButton = new JButton(" Transliterate ");
		transliterateButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(openButton);
		buttonPanel.add(transliterateButton);
		buttonPanel.add(outputOptions);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(Transliterator.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				inputFile = fc.getSelectedFile();

				log.append("Opening: " + inputFile.getName() + "." + newline);
			} else {
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());

			// Handle save button action.
		} else if (e.getSource() == transliterateButton) {
			if (inputFile == null) {

				log
						.append("Please click Select Source Button to select input file and then click transliterate \n");
				return;
			}

			int returnVal = fc.showSaveDialog(Transliterator.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				long start = System.currentTimeMillis();
				log.append("Transliterating to : " + file.getName() + "."
						+ newline);
				FileTransliterator.transliterateFile(outputOptions
						.getSelectedItem().toString(), inputFile, file);
				log.append("Done transliterating" + " in "
						+ (System.currentTimeMillis() - start) / 1000.0
						+ " seconds, Saved to: " + file.getName() + "."
						+ newline);
				try { // opening the transliterated file...
					Runtime.getRuntime().exec(
							"notepad " + file.getAbsolutePath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				log.append("Save command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Transliterator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setBounds(200, 300, 500, 300);
		// Add content to the window.
		frame.add(new Transliterator());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				createAndShowGUI();
			}
		});
	}
}