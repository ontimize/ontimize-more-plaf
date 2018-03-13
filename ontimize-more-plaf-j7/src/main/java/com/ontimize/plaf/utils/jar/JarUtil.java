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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarUtil {
	private static final Logger logger = LoggerFactory.getLogger(JarUtil.class);

	public static final String TITLE_PROPERTY="Implementation-Title";
	public static final String TITLE_VALUE="OntimizePLAF";

	public static String getManifest(Component d) throws Exception {
		Manifest manifest = JarUtil.retrieveManifest();
		if (manifest != null) {
			try {
				String version = JarUtil.getAttribute("Version-number", manifest).toString();
				String date = JarUtil.getAttribute("Version-date", manifest).toString();
				String ontimize_version = JarUtil.getAttribute("Ontimize-version-number", manifest);
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
						JarUtil.logger.error("", e);
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
				JarUtil.logger.error("", e);
			}
		}
		return null;
	}

	public static String ontimizeVersion() throws Exception {
		Manifest manifest = JarUtil.retrieveManifest();
		try {
			if (manifest == null) {
				return null;
			}
			String attr = JarUtil.getAttribute("Version-number-ontimize", manifest);
			if (attr == null) {
				return null;
			}
			return attr.toString().trim();
		} catch (Exception e) {
			JarUtil.logger.error("", e);
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
				String title = JarUtil.getAttribute(JarUtil.TITLE_PROPERTY,manifest);
				if (JarUtil.TITLE_VALUE.equalsIgnoreCase(title)){
					return manifest;
				}
			}
		} catch (Exception e) {
		}
		JarUtil.logger.warn(" Ontimize Manifest can't be retrieved");
		return null;
	}

	public static class InformationDialog extends JFrame {
		protected JLabel lVersion = null;

		protected JLabel lHtml = null;

		protected JLabel iOntimize = null;

		protected JLabel tPanel = null;

		protected boolean hideFrame = false;

		class EAction extends AbstractAction {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (SwingUtilities.getWindowAncestor((Component) e.getSource()) instanceof InformationDialog) {
					((InformationDialog) SwingUtilities.getWindowAncestor((Component) e.getSource())).processWindowEvent(new WindowEvent(
							(SwingUtilities.getWindowAncestor((Component) e.getSource())), WindowEvent.WINDOW_CLOSING));
				}
			}
		}

		public InformationDialog(boolean hideFrame) {
			this.hideFrame = hideFrame;
			ActionMap aM = ((JComponent) this.getContentPane()).getActionMap();
			InputMap inMap = ((JComponent) this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

			aM.put("close", new EAction());
			inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");

			this.setTitle("Imatia");
			URL url = JarUtil.class.getClassLoader().getResource("com/ontimize/plaf/utils/jar/iconimatia.gif");
			ImageIcon iconImatia =  new ImageIcon(url);
			if (iconImatia != null) {
				this.setIconImage(iconImatia.getImage());
			}

			((JComponent) this.getContentPane()).setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inMap);
			((JComponent) this.getContentPane()).setActionMap(aM);

			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (InformationDialog.this.hideFrame) {
						InformationDialog.this.setVisible(false);
					}
				}
			});

			this.setResizable(true);
			this.getContentPane().setBackground(Color.white);
			url = JarUtil.class.getClassLoader().getResource("com/ontimize/plaf/utils/jar/logoontimize.jpg");
			ImageIcon icon =  new ImageIcon(url);
			if (icon != null) {
				this.iOntimize = new JLabel(icon);
			}
			String version = null;
			try {
				version = JarUtil.getManifest(this);
			} catch (Exception ex) {
				version = "";
			}
			this.lHtml = new JLabel("", SwingConstants.CENTER);
			this.lHtml.setText(version);
			this.getContentPane().setLayout(new GridBagLayout());
			this.getContentPane().add(this.iOntimize,
					new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			this.getContentPane().add(this.lHtml,
					new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			this.getContentPane().setFocusable(true);
			this.getContentPane().requestFocus();
			this.pack();

		}

	}

	public final static void main(String[] arg) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			JarUtil.logger.error("", e);
		} catch (InstantiationException e) {
			JarUtil.logger.error("", e);
		} catch (IllegalAccessException e) {
			JarUtil.logger.error("", e);
		} catch (UnsupportedLookAndFeelException e) {
			JarUtil.logger.error("", e);
		}

		InformationDialog id = new InformationDialog(false);
		JarUtil.center(id);
		id.setVisible(true);

	}

	public static void center(Window window) {
		int x = 0;
		int y = 0;

		Rectangle bounds = null;

		bounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration().getBounds();

		x = (bounds.width / 2) - (window.getWidth() / 2);
		y = (bounds.height / 2) - (window.getHeight() / 2);

		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x > bounds.width) {
			x = 0;
		}
		if (y > bounds.height) {
			y = 0;
		}
		window.setLocation(bounds.x+x, bounds.y+y);
	}

}
