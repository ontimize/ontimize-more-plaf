package com.ontimize.plaf.painter;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.SynthContext;

public class RootPaneImagePainter {

	public static final String UI_KEY = "Synthetica.RootPanePainter";
	 
   protected RootPaneImagePainter(){
   }

   public static RootPaneImagePainter getInstance(){
       return getInstance(null);
   }

   public static RootPaneImagePainter getInstance(SynthContext synthcontext){
   	return new RootPaneImagePainter();
   }

   public void paintRootPaneBorder(JRootPane rootPane, int state, Graphics graphics, int x, int y, int width, int height){
       Window window = getWindow(rootPane);
       boolean flag = isMaximized(window);
       
       boolean flag1 = false;
//      	 SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.opaque", window, true);
       
       if(flag && flag1)
           return;
//       boolean flag2 = OntimizeLookAndFeel.getBoolean("Synthetica.rootPane.border.respectFill", window);
       boolean flag2 = false;
       
       String s = "com/ontimize/plaf/images/rootPaneBorder.png"; 
//       		"Synthetica.rootPane.border";
//       if(window.isActive())
//           s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//       s = OntimizeLookAndFeel.getString(s, window);
       
//       Insets insets = (Insets)OntimizeLookAndFeel.getInsets("Synthetica.rootPane.border.insets", window).clone();
       	Insets insets = new Insets(10, 5, 5, 5);
//       if(OntimizeLookAndFeel.getBoolean("Synthetica.rootPane.respectHeaderHeight", window)){
//      	 int i1 = ((OntimizeRootPaneUI)rootPane.getUI()).getTitlePane().getHeight();
//      	 i1 += rootPane.getJMenuBar() != null ? rootPane.getJMenuBar().getHeight() : 0;
//      	 //           i1 += SyntheticaLookAndFeel.get("Synthetica.rootPane.border.size", window) != null ? SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", window).top : 4;
//      	 i1+=4;
//      	 insets.top = i1;
//       }
       
       Insets insets1 = insets;
       int j1 = height;
//       if(flag){
//           if(!flag2)
//               j1 = insets.top;
//           insets = OntimizeLookAndFeel.getInsets("Synthetica.rootPane.border.size", window);
//           insets1 = new Insets(0, 0, 0, 0);
//       }
       
//       int l1 = 0;
//       if(OntimizeLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.horizontalTiled", window))
//           l1 = 1;
//       int i2 = 0;
//       if(OntimizeLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.verticalTiled", window))
//           i2 = 1;
//       if(OntimizeLookAndFeel.isEvalCopy() && !flag)
//           j1 -= 16;
//       if(OS.getCurrentOS() == OS.Mac && (!OntimizeLookAndFeel.isWindowOpacityEnabled(window) || OntimizeLookAndFeel.isWindowShapeEnabled(window)))
//           g.clearRect(i, j, k, j1);
       
//       ImagePainter imagepainter = new ImagePainter(rootPane, graphics, x, y, k, j1, s, insets, insets1, l1, i2);
//       if(flag)
//           imagepainter.drawCenter();
//       else
//       if(flag2)
//           imagepainter.draw();
//       else
//           imagepainter.drawBorder();
//       s = "Synthetica.rootPane.border.light";
//       if(window.isActive())
//           s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//       s = OntimizeLookAndFeel.getString(s, window);
//       if(s != null){
//           ImagePainter imagepainter1 = new ImagePainter(rootPane, graphics, i, j, k, j1, s, insets, insets1, 0, 1);
//           if(flag)
//               imagepainter1.drawCenter();
//           else
//           if(flag2)
//               imagepainter1.draw();
//           else
//               imagepainter1.drawBorder();
//       }
       
       	ImagePainter painter = new ImagePainter(0,true,true,new Insets(20,5,5,5),null,getClass().getClassLoader().getResource(s),true){
				@Override
				protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
					
				}
       	};
       
       	painter.paint(rootPane, graphics, x, y, width, height);
//       if(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.paintStatusBar", window) && rootPane.getClientProperty("Synthetica.statusBar") != null)
//       {
//           int k1 = l;
//           if(SyntheticaRootPaneUI.isEvalCopy() && !flag)
//               k1 -= 16;
//           int j2 = ((JComponent)rootPane.getClientProperty("Synthetica.statusBar")).getHeight();
//           j2 += SyntheticaLookAndFeel.get("Synthetica.rootPane.border.size", window) != null ? SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", window).top : 4;
//           SyntheticaAddonsPainter.getInstance("Synthetica.StatusBarPainter").paintStatusBarBackground(rootPane, new SyntheticaState(), graphics, 0, k1 - j2, k, j2);
//       }
//       s = "Synthetica.rootPane.titlePane.background";
//       s = SyntheticaLookAndFeel.getString(s, window);
//       boolean flag3 = s != null && flag1;
//       if(!flag3 && rootPane.getClientProperty("Synthetica.logoRenderer") != null)
//           renderLogo(rootPane, window.isActive(), flag, true, graphics, k, l);
//       if(SyntheticaRootPaneUI.isEvalCopy())
//       {
//           graphics.setColor(UIManager.getColor("Panel.background"));
//           graphics.fillRect(i, l - 16, k, 16);
//           graphics.setColor(new Color(0xcc0000));
//           graphics.setFont(graphics.getFont().deriveFont(10F));
//           graphics.drawString("Synthetica - Unregistered Evaluation Copy!", 4, (l - 16) + graphics.getFontMetrics().getAscent());
//       }
   }

   public void paintRootPaneBorder(SynthContext synthcontext, Graphics g, int x, int y, int width, int height){
       JRootPane jrootpane = (JRootPane)synthcontext.getComponent();
       paintRootPaneBorder(jrootpane,  synthcontext.getComponentState(), g, x, y, width, height);
   }

   public void paintContentBackground(JRootPane jrootpane, int state, Graphics g, int i, int j, int k, int l){
//   	Window window = SwingUtilities.getWindowAncestor(jrootpane);
//   	String s = "Synthetica.rootPane.border";
//   	if(window.isActive())
//   		s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//   	s = SyntheticaLookAndFeel.getString(s, window);
//   	Insets insets = SyntheticaLookAndFeel.get("Synthetica.rootPane.border.size", window) != null ? SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", window) : new Insets(4, 4, 4, 4);
//   	Insets insets1 = (Insets)insets.clone();
//   	insets1.top += ((SyntheticaRootPaneUI)jrootpane.getUI()).getTitlePane().getHeight();
//   	Insets insets2 = new Insets(0, 0, 0, 0);
//   	int i1 = 0;
//   	if(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.horizontalTiled", window))
//   		i1 = 1;
//   	int j1 = 0;
//   	if(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.verticalTiled", window))
//   		j1 = 1;
//   	ImagePainter imagepainter = new ImagePainter(g, i, j, k, l, s, insets1, insets2, i1, j1);
//   	imagepainter.drawCenter();
//   	s = "Synthetica.rootPane.border.light";
//   	if(window.isActive())
//   		s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//   	s = SyntheticaLookAndFeel.getString(s, window);
//   	if(s != null)
//   	{
//   		ImagePainter imagepainter1 = new ImagePainter(g, i, j, k, l, s, insets1, insets2, 0, 1);
//   		imagepainter1.drawCenter();
//   	}
//   	MenuPainter.getInstance().paintMenuBarBackground(jrootpane, new SyntheticaState(), g, i, j, k, l);
   }

//   protected void renderLogo(JRootPane jrootpane, boolean flag, boolean flag1, boolean flag2, Graphics g, int i, int j)
//   {
//       SyntheticaLogoRenderer syntheticalogorenderer = (SyntheticaLogoRenderer)jrootpane.getClientProperty("Synthetica.logoRenderer");
//       JComponent jcomponent = syntheticalogorenderer.getRendererComponent(jrootpane, flag);
//       Window window = getWindow(jrootpane);
//       Insets insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.size", window);
//       if(insets == null)
//           insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.border.insets", window);
//       int k = insets.top;
//       k = !flag1 && flag2 ? k : 0;
//       k += ((SyntheticaRootPaneUI)jrootpane.getUI()).getTitlePane().getHeight();
//       JMenuBar jmenubar = (JMenuBar)SyntheticaLookAndFeel.findComponent(javax/swing/JMenuBar, jrootpane);
//       boolean flag3 = jrootpane.getClientProperty("Synthetica.logoRenderer.titlePaneOnly") != null ? ((Boolean)jrootpane.getClientProperty("Synthetica.logoRenderer.titlePaneOnly")).booleanValue() : false;
//       if(jmenubar != null && SyntheticaLookAndFeel.get("Synthetica.menuBar.background.active", jmenubar) == null && !flag3)
//           k += jmenubar.getHeight();
//       boolean flag4 = jrootpane.getClientProperty("Synthetica.logoRenderer.respectButtons") != null ? ((Boolean)jrootpane.getClientProperty("Synthetica.logoRenderer.respectButtons")).booleanValue() : false;
//       if(flag4)
//       {
//           Rectangle rectangle = ((SyntheticaTitlePane)((SyntheticaRootPaneUI)jrootpane.getUI()).getTitlePane()).getMenuButtonBounds();
//           Rectangle rectangle1 = ((SyntheticaTitlePane)((SyntheticaRootPaneUI)jrootpane.getUI()).getTitlePane()).getControlButtonsBounds();
//           int l = Math.min(rectangle.x + rectangle.width, rectangle1.x + rectangle1.width);
//           int i1 = Math.max(rectangle.x, rectangle1.x) - l;
//           l += !flag1 && flag2 ? insets.left : 0;
//           jcomponent.setSize(new Dimension(i1, k));
//           g.translate(l, 0);
//           jcomponent.paint(g);
//           g.translate(-l, 0);
//       } else
//       {
//           jcomponent.setSize(new Dimension(i, k));
//           jcomponent.paint(g);
//       }
//   }

//   public void paintTitlePaneBackground(JRootPane jrootpane, SyntheticaState syntheticastate, Graphics g, int i, int j, int k, int l)
//   {
//       Window window = getWindow(jrootpane);
//       boolean flag = SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.opaque", window, true);
//       String s = "Synthetica.rootPane.titlePane.background";
//       if(syntheticastate.isSet(de.javasoft.plaf.synthetica.SyntheticaState.State.SELECTED))
//           s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//       s = SyntheticaLookAndFeel.getString(s, window);
//       Insets insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.background.insets", window);
//       int i1 = 0;
//       if(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.horizontalTiled", window))
//           i1 = 1;
//       int j1 = 0;
//       if(SyntheticaLookAndFeel.getBoolean("Synthetica.rootPane.titlePane.background.verticalTiled", window))
//           j1 = 1;
//       Object obj = null;
//       boolean flag1 = s != null && flag;
//       if(flag1)
//       {
//           ImagePainter imagepainter = new ImagePainter(g, i, j, k, l, s, insets, insets, i1, j1);
//           imagepainter.draw();
//       }
//       s = "Synthetica.rootPane.titlePane.background.light";
//       if(syntheticastate.isSet(de.javasoft.plaf.synthetica.SyntheticaState.State.SELECTED))
//           s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//       s = SyntheticaLookAndFeel.getString(s, window);
//       if(s != null && flag)
//       {
//           ImagePainter imagepainter1 = new ImagePainter(g, i, j, k, l, s, insets, insets, 0, j1);
//           imagepainter1.draw();
//       }
//       if(flag1 && jrootpane.getClientProperty("Synthetica.logoRenderer") != null)
//           renderLogo(jrootpane, window.isActive(), isMaximized(window), false, g, k, l);
//   }
//
//   public void paintTitlePaneBackground(SynthContext synthcontext, Graphics g, int i, int j, int k, int l){
//       JRootPane jrootpane = (JRootPane)synthcontext.getComponent();
//       paintTitlePaneBackground(jrootpane, new SyntheticaState(synthcontext.getComponentState()), g, i, j, k, l);
//   }

   protected boolean isMaximized(Window window){
       return (window instanceof Frame) && (((Frame)window).getExtendedState() & 6) == 6;
   }

//   public void paintButtonAreaBackground(SynthContext synthcontext, Graphics g, int i, int j, int k, int l)
//   {
//       JRootPane jrootpane = (JRootPane)synthcontext.getComponent();
//       Window window = getWindow(jrootpane);
//       boolean flag = (synthcontext.getComponentState() & 0x200) > 0;
//       Insets insets = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.buttonArea.insets", window);
//       if(insets != null){
//           i -= insets.left;
//           j -= insets.top;
//           k += insets.left + insets.right;
//           l += insets.top + insets.bottom;
//           String s = "Synthetica.rootPane.titlePane.buttonArea.background";
//           if(flag && SyntheticaLookAndFeel.getString("Synthetica.rootPane.titlePane.buttonArea.background.selected", window) != null)
//               s = (new StringBuilder(String.valueOf(s))).append(".selected").toString();
//           s = SyntheticaLookAndFeel.getString(s, window);
//           Insets insets1 = SyntheticaLookAndFeel.getInsets("Synthetica.rootPane.titlePane.buttonArea.background.insets", window);
//           ImagePainter imagepainter = new ImagePainter(g, i, j, k, l, s, insets1, insets1, 0, 0);
//           imagepainter.draw();
//       }
//   }

   protected Window getWindow(JRootPane jrootpane){
       java.awt.Container container = jrootpane.getParent();
       return (container instanceof Window) ? (Window)container : SwingUtilities.getWindowAncestor(container);
   }

//   public Cacheable.ScaleType getCacheScaleType(String s)
//   {
//       if(s.equals("paintRootPaneBorder") || s.equals("paintRootPaneBackground"))
//           return Cacheable.ScaleType.NINE_SQUARE;
//       else
//           return super.getCacheScaleType(s);
//   }

  
}
