package com.ontimize.plaf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

public class OntimizeContext extends SynthContext {
	
	/**
     * Used to avoid null painter checks everywhere.
     */
    static SynthPainter EMPTY_PAINTER = new SynthPainter() {};
    
    /** This button *must* not have a UI or we end up throwing an NPE. */
    protected static JButton FAKE_BUTTON   = new JButton() {
        public void updateUI() {
            // Do nothing.
        }
    };
    
    // Fake items are created because there's no public empty constructor for
    // SynthContext and we need a way to determine when our own empty
    // constructor was called. We pass these to SynthContext's constructor and
    // then test for them later.

    protected static JButton fakeComponent = FAKE_BUTTON;
    protected static Region        fakeRegion    = Region.BUTTON;
    protected static SynthStyle    fakeStyle     = new OntimizeStyle(null, null);

	protected static final Map contextMap;

	protected JComponent component;
	protected Region region;
	protected SynthStyle style;
	protected int state;

	static {
		contextMap = new HashMap();
	}
	
	public OntimizeContext() {
        super(fakeComponent, fakeRegion, fakeStyle, 0);
    }
	
	public OntimizeContext(JComponent component, Region region,
			SynthStyle style, int state) {
		super(component, region, style, state);
		
		 if (component == fakeComponent) {
	            this.component = null;
	            this.region    = null;
	            this.style     = null;

	            return;
	        }

	        if (component == null || region == null || style == null) {
	            throw new NullPointerException("You must supply a non-null component, region and style");
	        }

	        reset(component, region, style, state);
	}

	public static OntimizeContext getContext(Class type, JComponent component,
			Region region, SynthStyle style, int state) {
		OntimizeContext context = null;

		synchronized (contextMap) {
			java.util.List instances = (java.util.List) contextMap.get(type);

			if (instances != null) {
				int size = instances.size();

				if (size > 0) {
					context = (OntimizeContext) instances.remove(size - 1);
				}
			}
		}
		if (context == null) {
			try {
				context = (OntimizeContext) type.newInstance();
			} catch (IllegalAccessException iae) {
			} catch (InstantiationException ie) {
			}
		}
		context.reset(component, region, style, state);
		return context;
	}

	/**
	 * Resets the state of the Context.
	 */
	protected void reset(JComponent component, Region region, SynthStyle style,
			int state) {
		this.component = component;
		this.region = region;
		this.style = style;
		this.state = state;
	}

	public void dispose() {
		this.component = null;
		this.style = null;
		releaseContext(this);
	}
	
	static void releaseContext(SynthContext context) {
        synchronized(contextMap) {
            java.util.List instances = (java.util.List)contextMap.get(
                                       context.getClass());

            if (instances == null) {
                instances = new ArrayList(5);
                contextMap.put(context.getClass(), instances);
            }
            instances.add(context);
        }
    }
	
    /**
     * Returns the hosting component containing the region.
     *
     * @return Hosting Component
     */
    public JComponent getComponent() {
        return component;
    }
    
    /**
     * Returns the state of the widget, which is a bitmask of the values defined
     * in <code>SynthConstants</code>. A region will at least be in one of
     * <code>ENABLED</code>, <code>MOUSE_OVER</code>, <code>PRESSED</code> or
     * <code>DISABLED</code>.
     *
     * @see    SynthConstants
     *
     * @return State of Component
     */
    public int getComponentState() {
        return state;
    }

    /**
     * Returns the Region identifying this state.
     *
     * @return Region of the hosting component
     */
    public Region getRegion() {
        return region;
    }

    /**
     * A convenience method for <code>getRegion().isSubregion()</code>.
     *
     * @return {@code true} if the context represents a subregion, {@code false}
     *         otherwise.
     */
    @SuppressWarnings("all")
    public boolean isSubregion() {
        return getRegion().isSubregion();
    }

	/**
	 * Convenience method to get the Painter from the current SynthStyle. This
	 * will NEVER return null.
	 */
	public SynthPainter getPainter() {
		SynthPainter painter = getStyle().getPainter(this);

		if (painter != null) {
			return painter;
		}
		return EMPTY_PAINTER;
	}
	
	 /**
     * Sets the current style for the context.
     *
     * @param style the new style.
     */
    @SuppressWarnings("all")
    public void setStyle(SynthStyle style) {
        this.style = style;
    }
    
    /**
     * Sets the current state for a component/region.
     *
     * @param state the new state.
     */
    @SuppressWarnings("all")
    public void setComponentState(int state) {
        this.state = state;
    }
    
    /**
     * Returns the style associated with this Region.
     *
     * @return SynthStyle associated with the region.
     */
    public SynthStyle getStyle() {
        return style;
    }
    
    public static void clearReferences(){
    	fakeStyle  = null;
    	fakeRegion = null;
    	FAKE_BUTTON = null;
    	fakeComponent = null;
    	contextMap.clear();
    	EMPTY_PAINTER = null;
    }

}
