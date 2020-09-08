import java.awt.*;
import java.awt.event.*;

import javax.swing.*;;

/**
 * A menu full of commands that affect the text shown in a DrawPanel.
 */
public class TextMenu extends JMenu {

	final DrawPanel panel; // the panel whose text is controlled by this menu

	private JCheckBoxMenuItem bold; // controls whether the text is bold or not.
	private JCheckBoxMenuItem italic; // controls whether the text is italic or not.
	// add in order to create Radio Buttons in a Submenu
	private JRadioButtonMenuItem justify; // controls justification of text

	/**
	 * Constructor creates all the menu commands and adds them to the menu.
	 * 
	 * @param owner
	 *            the panel whose text will be controlled by this menu.
	 */
	public TextMenu(DrawPanel owner) {
		super("Text");
		this.panel = owner;
		final JMenuItem change = new JMenuItem("Change Text...");
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String currentText = panel.getTextItem().getText();
				String newText = GetTextDialog.showDialog(panel, currentText);
				if (newText != null && newText.trim().length() > 0) {
					panel.getTextItem().setText(newText);
					panel.repaint();
				}
			}
		});
		final JMenuItem size = new JMenuItem("Set Size...");
		size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int currentSize = panel.getTextItem().getFontSize();
				String s = JOptionPane.showInputDialog(panel, "What font size do you want to use?", currentSize);
				if (s != null && s.trim().length() > 0) {
					try {
						int newSize = Integer.parseInt(s.trim()); // can throw NumberFormatException
						panel.getTextItem().setFontSize(newSize); // can throw IllegalArgumentException
						panel.repaint();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(panel,
								s + " is not a legal text size.\n" + "Please enter a positive integer.");
					}
				}
			}
		});
		
		//Add support for changing the value of the lineHeightMultiplier to TextMenu.java. 
		final JMenuItem lineHeight = new JMenuItem("Set Line Height...");
		lineHeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				double currentLineHeight = panel.getTextItem().getLineHeightMultiplier();
				String s = JOptionPane.showInputDialog(panel, "What line height do you want?", currentLineHeight);
				if (s != null && s.trim().length() > 0) {
					try {
						double newHeight = Double.parseDouble(s.trim()); // can throw NumberFormatException
						panel.getTextItem().setLineHeightMultiplier(newHeight); // can throw IllegalArgumentException
						panel.repaint();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(panel,
								s + " is not a legal height line.\n" + "Please enter a positive real number.");
					}
				}
			}
		});
		final JMenuItem color = new JMenuItem("Set Color...");
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Color currentColor = panel.getTextItem().getColor();
				Color newColor = JColorChooser.showDialog(panel, "Select Text Color", currentColor);
				if (newColor != null) {
					panel.getTextItem().setColor(newColor);
					panel.repaint();
				}
			}
		});
		italic = new JCheckBoxMenuItem("Italic");
		italic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setItalic(italic.isSelected());
				panel.repaint();
			}
		});
		bold = new JCheckBoxMenuItem("Bold");
		bold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setBold(bold.isSelected());
				panel.repaint();
			}
		});

		add(change);
		addSeparator();
		add(size);
		add(lineHeight);
		add(color);
		add(italic);
		add(bold);
		// add in order to create Radio Buttons in a Submenu
		add(makeJustifyMenu());
		addSeparator();
		add(makeFontNameSubmenu());

	}

	/**
	 * Reset the state of the menu to reflect the default settings for text in a
	 * DrawPanel. (Sets the italic and bold checkboxes to unselected.) This method
	 * is called by the main program when the user selects the "New" command, to
	 * make sure that the menu state reflects the contents of the panel.
	 */
	public void setDefaults() {
		italic.setSelected(false);
		bold.setSelected(false);
		
		// add in order to create Radio Buttons in a Submenu
		/*
		 * There is one subtle problem here. When the user selects the "New" command, 
		 * a default text item is placed in the picture. This text item has "Left" 
		 * justification, but the radio group might still indicate that the justification 
		 * is "Right" or "Center," from its setting in the previous picture. 
		 * There was a similar problem with the "Bold" and "Italic" checkboxes in the 
		 * text menu. To solve it, a setDefaults() method was added to TextMenu which 
		 * resets the checkboxes to their default state (unselected).
		 */
		justify.setSelected(true);
	}
	
	/*
	 * Create a new JMenu named "Justify." To this menu, you will add three 
	 * JRadioButtonMenuItems named "Left", "Right", and "Center". To make the radio 
	 * buttons into an actual group, create a ButtonGroup object, and add each radio 
	 * button to the button group. Add the JMenu to the menu that is created by the 
	 * constructor of the TextMenu class. You might want to use a makeJustifyMenu method, 
	 * similar to (but simpler than) the makeFontNameSubmenu method that is already 
	 * defined in TextMenu. 
	 * Since the text justification has to change when the user selects one of the buttons, 
	 * you will have to add an ActionListener to each of the buttons. The action listener 
	 * for the "Left" button should do: 
	 * panel.getTextItem().setJustify(TextItem.LEFT);
	 * panel.repaint(); 
	 * With similar code for the other action listeners, using TextItem.RIGHT and 
	 * TextItem.CENTER for the values of the justify property.
	 */

	JMenu makeJustifyMenu() {
		JMenu menu = new JMenu("Justify");
		ActionListener setJustifyAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getActionCommand() == "Left")
					panel.getTextItem().setJustify(TextItem.LEFT);
				else if (evt.getActionCommand() == "Center")
					panel.getTextItem().setJustify(TextItem.CENTER);
				else
					panel.getTextItem().setJustify(TextItem.RIGHT);
				panel.repaint();
			}
		};

		ButtonGroup group = new ButtonGroup();
		String[] basic = { "Left", "Right", "Center" };
		for (String f : basic) {
			JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(f);
			menuItem.addActionListener(setJustifyAction);
			/*
			 * There is one subtle problem here. When the user selects the "New" command, 
			 * a default text item is placed in the picture. This text item has "Left" 
			 * justification, but the radio group might still indicate that the justification 
			 * is "Right" or "Center," from its setting in the previous picture. 
			 * There was a similar problem with the "Bold" and "Italic" checkboxes in the 
			 * text menu. To solve it, a setDefaults() method was added to TextMenu which 
			 * resets the checkboxes to their default state (unselected).
			 */
			if (f.equals("Left")){
				menuItem.setSelected(true);
				this.justify = menuItem;
			}
			
			group.add(menuItem);
			menu.add(menuItem);
		}
		return menu;

	}



	/**
	 * Create a menu containing a list of all available fonts. (It turns out this
	 * can be very messy, at least on Linux, but it does show the use what is
	 * available and lets the user try everything!)
	 */
	JMenu makeFontNameSubmenu() {
		ActionListener setFontAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setFontName(evt.getActionCommand());
				panel.repaint();
			}
		};
		JMenu menu = new JMenu("Font Name");
		String[] basic = { "Serif", "SansSerif", "Monospace" };
		for (String f : basic) {
			JMenuItem m = new JMenuItem(f + " Default");
			m.setActionCommand(f);
			m.addActionListener(setFontAction);
			m.setFont(new Font(f, Font.PLAIN, 12));
			menu.add(m);
		}
		menu.addSeparator();
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		if (fonts.length <= 20) {
			for (String f : fonts) {
				JMenuItem m = new JMenuItem(f);
				m.addActionListener(setFontAction);
				m.setFont(new Font(f, Font.PLAIN, 12));
				menu.add(m);
			}
		} else { // Too many items for one menu; divide them into several sub-sub-menus.
			char ch1 = 'A';
			char ch2 = 'A';
			JMenu m = new JMenu();
			int i = 0;
			while (i < fonts.length) {
				while (i < fonts.length && (Character.toUpperCase(fonts[i].charAt(0)) <= ch2 || ch2 == 'Z')) {
					JMenuItem item = new JMenuItem(fonts[i]);
					item.addActionListener(setFontAction);
					item.setFont(new Font(fonts[i], Font.PLAIN, 12));
					m.add(item);
					i++;
				}
				if (i == fonts.length || (m.getMenuComponentCount() >= 12 && i < fonts.length - 4)) {
					if (ch1 == ch2)
						m.setText("" + ch1);
					else
						m.setText(ch1 + " to " + ch2);
					menu.add(m);
					if (i < fonts.length)
						m = new JMenu();
					ch2++;
					ch1 = ch2;
				} else
					ch2++;
			}
		}
		return menu;
	}

}

