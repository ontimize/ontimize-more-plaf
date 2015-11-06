package com.ontimize.plaf.utils.jar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class JarUtil {
	public static final String TITLE_PROPERTY="Implementation-Title";
	public static final String TITLE_VALUE="OntimizePLAF";

	public static String getManifest(Component d) throws Exception {
		Manifest manifest = retrieveManifest();
		if (manifest != null) {
			try {
				String version = getAttribute("Version-number", manifest).toString();
				String date = getAttribute("Version-date", manifest).toString();
				String ontimize_version = (String)getAttribute("Ontimize-version-number", manifest);
				URL urlHtml = JarUtil.class.getClassLoader().getResource("com/ontimize/plaf/utils/jar/template.html");
				if (urlHtml != null) {
					InputStream iS = urlHtml.openStream();
					BufferedReader bR = new BufferedReader(new InputStreamReader(iS));
					StringBuffer html = new StringBuffer();
					try {
						String str;
						while ((str = bR.readLine()) != null) {
							html.append(str);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					bR.close();
					String sOutput = html.toString();
					sOutput = sOutput.replaceAll("%version%", version);
					sOutput = sOutput.replaceAll("%fecha%", date);
					if(ontimize_version!=null){
						sOutput = sOutput.replaceAll("%ontimize-version%", ontimize_version);
					}else{
						sOutput = sOutput.replaceAll("%ontimize-version%", " -- ");
					}
					return sOutput;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String ontimizeVersion() throws Exception {
		Manifest manifest = retrieveManifest();
		try {
			if (manifest == null) return null;
			String attr = getAttribute("Version-number-ontimize", manifest);
			if (attr == null) return null;
			return attr.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static String getAttribute(Object key, Manifest m) {
		Attributes.Name aN = new Attributes.Name(key.toString());
		Attributes ats = m.getMainAttributes();
		if (ats.containsKey(aN)) {
			return ats.getValue(key.toString());
		}
		return null;
	}

	protected static Manifest retrieveManifest() {
		try {
			URL url = JarUtil.class.getProtectionDomain().getCodeSource().getLocation();
			JarFile file = null;
			Manifest manifest = null;
			if (url != null) {
				file = new JarFile(URLDecoder.decode(url.getFile(), "UTF-8"));
				manifest = file.getManifest();
				return manifest;
			}
		} catch (Exception e) {
		}
		
		try{
			Enumeration enumeration = JarUtil.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while(enumeration.hasMoreElements()){
				URL url = (URL)enumeration.nextElement();
				Manifest manifest = new Manifest(url.openStream());
				String title = getAttribute(TITLE_PROPERTY,manifest);
				if (TITLE_VALUE.equalsIgnoreCase(title)){
					return manifest;
				}
			}
		} catch (Exception e) {
		}
		System.out.println("WARNING: -> Ontimize Manifest can't be retrieved");
		return null;
	}

	public static class InformationDialog extends JFrame {
		protected JLabel lVersion = null;

		protected JLabel lHtml = null;

		protected JLabel iOntimize = null;

		protected JLabel tPanel = null;

		protected boolean hideFrame = false;

		class EAction extends AbstractAction {
			public void actionPerformed(ActionEvent e) {
				if (SwingUtilities.getWindowAncestor((Component) e.getSource()) instanceof InformationDialog) {
					((InformationDialog) SwingUtilities.getWindowAncestor((Component) e.getSource())).processWindowEvent(new WindowEvent(
							((InformationDialog) SwingUtilities.getWindowAncestor((Component) e.getSource())), WindowEvent.WINDOW_CLOSING));
				}
			}
		}

		public InformationDialog(boolean hideFrame) {
			this.hideFrame = hideFrame;
			ActionMap aM = ((JComponent) getContentPane()).getActionMap();
			InputMap inMap = ((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

			aM.put("close", new EAction());
			inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");

			setTitle("Imatia");
			URL url = JarUtil.class.getClassLoader().getResource("com/ontimize/plaf/utils/jar/iconimatia.gif");
			ImageIcon iconImatia =  new ImageIcon(url);
			if (iconImatia != null) {
				setIconImage(iconImatia.getImage());
			}

			((JComponent) this.getContentPane()).setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inMap);
			((JComponent) getContentPane()).setActionMap(aM);

			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if (InformationDialog.this.hideFrame) {
						InformationDialog.this.setVisible(false);
					} else
						System.exit(0);
				}
			});

			setResizable(true);
			getContentPane().setBackground(Color.white);
			url = JarUtil.class.getClassLoader().getResource("com/ontimize/plaf/utils/jar/logoontimize.jpg");
			ImageIcon icon =  new ImageIcon(url);
			if (icon != null) {
				iOntimize = new JLabel(icon);
			}
			String version = null;
			try {
				version = getManifest(this);
			} catch (Exception ex) {
				version = "";
			}
			lHtml = new JLabel("", JLabel.CENTER);
			lHtml.setText(version);
			getContentPane().setLayout(new GridBagLayout());
			getContentPane().add(iOntimize,
					new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			getContentPane().add(lHtml,
					new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			getContentPane().setFocusable(true);
			getContentPane().requestFocus();
			pack();

		}

	}

	public final static void main(String[] arg) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		InformationDialog id = new InformationDialog(false);
		center(id);
		id.setVisible(true);

	}
	
	public static void center(Window window) {
		int x = 0;
		int y = 0;

		Rectangle bounds = null;

		bounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		
		x = bounds.width / 2 - window.getWidth() / 2;
		y = bounds.height / 2 - window.getHeight() / 2;

		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (x > bounds.width) x = 0;
		if (y > bounds.height) y = 0;
		window.setLocation(bounds.x+x, bounds.y+y);
	}

}
