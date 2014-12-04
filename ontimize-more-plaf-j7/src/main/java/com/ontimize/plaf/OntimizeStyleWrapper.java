/*
 * Copyright (c) 2009 Kathryn Huxtable and Kenneth Orr.
 *
 * This file is part of the Ontimize Pluggable Look and Feel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: OntimizeStyleWrapper.java,v 1.2 2012/10/15 09:43:31 daniel.grana Exp $
 */
package com.ontimize.plaf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import com.ontimize.plaf.border.OntimizeBorder;

/**
 * A SynthStyle implementation used by Ontimize. This just wraps a SynthStyle
 * with a Ontimize style in order to get at some SynthStyle methods that are
 * package local.
 *
 * @see javax.swing.plaf.synth.SynthStyle
 */
public class OntimizeStyleWrapper extends SynthStyle implements OntimizeStyle {

	/** Shared SynthGraphics. */
	protected static final SynthGraphicsUtils SYNTH_GRAPHICS = new SynthGraphicsUtils();

	/**
	 * The SynthPainter that will be returned from this OntimizeStyle. The
	 * SynthPainter returned will be a OntimizeSynthPainterImpl, which will in
	 * turn delegate back to this OntimizeStyle for the proper Painter (not
	 * SynthPainter) to use for painting the foreground, background, or border.
	 */
	protected SynthPainter painter;

	protected SynthStyle style;

	/**
	 * Create a new OntimizeStyle. Only the prefix must be supplied. At the
	 * appropriate time, installDefaults will be called. At that point, all of
	 * the state information will be pulled from UIManager and stored locally
	 * within this style.
	 *
	 * @param style
	 *            Something like Button or Slider.Thumb or
	 *            org.jdesktop.swingx.JXStatusBar or
	 *            ComboBox."ComboBox.arrowButton"
	 */
	OntimizeStyleWrapper(SynthStyle style) {
		this.style = style;
		this.painter = new OntimizeSynthPainterImpl(this);
	}

	/**
	 * Returns the <code>SynthGraphicUtils</code> for the specified context.
	 *
	 * @param context
	 *            SynthContext identifying requester
	 *
	 * @return SynthGraphicsUtils
	 */
	public SynthGraphicsUtils getGraphicsUtils(SynthContext context) {
		return SYNTH_GRAPHICS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void installDefaults(SynthContext ctx) {
		// delegate to the superclass to install defaults such as background,
		// foreground, font, and opaque onto the swing component.
		this.style.installDefaults(ctx);
	}

	/**
	 * Install UI defaults.
	 *
	 * @param context
	 *            the SynthContext describing the component/region, the style,
	 *            and the state.
	 * @param ui
	 *            the UI delegate.
	 */
	public void installDefaults(SynthContext context, SynthUI ui) {
		// Special case the Border as this will likely change when the LAF
		// can have more control over this.
		if (!context.getRegion().isSubregion()) {
			JComponent c = context.getComponent();
			Border border = c.getBorder();

			if (border == null || border instanceof UIResource) {
				if (!(c instanceof JViewport)) {
					c.setBorder(new OntimizeBorder(ui, getInsets(context, null)));
				}
			}
		}

		installDefaults(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Insets getInsets(SynthContext ctx, Insets in) {
		return this.style.getInsets(ctx, in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getColorForState(SynthContext ctx, ColorType type) {
		return this.style.getColor(ctx, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Font getFontForState(SynthContext ctx) {
		Font f = (Font) get(ctx, "font");

		if (f == null)
			f = UIManager.getFont("defaultFont");

		// Account for scale
		// The key "JComponent.sizeVariant" is used to match Apple's LAF
		String scaleKey = (String) ctx.getComponent().getClientProperty("JComponent.sizeVariant");

		if (scaleKey != null) {

			if (LARGE_KEY.equals(scaleKey)) {
				f = f.deriveFont(Math.round(f.getSize2D() * LARGE_SCALE));
			} else if (SMALL_KEY.equals(scaleKey)) {
				f = f.deriveFont(Math.round(f.getSize2D() * SMALL_SCALE));
			} else if (MINI_KEY.equals(scaleKey)) {
				f = f.deriveFont(Math.round(f.getSize2D() * MINI_SCALE));
			}
		}

		return f;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynthPainter getPainter(SynthContext ctx) {
		return painter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque(SynthContext ctx) {
		return this.style.isOpaque(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(SynthContext ctx, Object key) {
		return this.style.get(ctx, key);
	}

	/**
	 * Gets the appropriate background Painter, if there is one, for the state
	 * specified in the given SynthContext. This method does appropriate
	 * fallback searching, as described in #get.
	 *
	 * @param ctx
	 *            The SynthContext. Must not be null.
	 *
	 * @return The background painter associated for the given state, or null if
	 *         none could be found.
	 */
	@SuppressWarnings("unchecked")
	public Painter getBackgroundPainter(SynthContext ctx) {
		if (!(this.style instanceof NimbusStyle)) {
			return null;
		}

		return new PainterWrapper((Painter) ((NimbusStyle) style).getBackgroundPainter(ctx));
	}

	/**
	 * Gets the appropriate foreground Painter, if there is one, for the state
	 * specified in the given SynthContext. This method does appropriate
	 * fallback searching, as described in #get.
	 *
	 * @param ctx
	 *            The SynthContext. Must not be null.
	 *
	 * @return The foreground painter associated for the given state, or null if
	 *         none could be found.
	 */
	@SuppressWarnings("unchecked")
	public Painter getForegroundPainter(SynthContext ctx) {
		if (!(style instanceof NimbusStyle)) {
			return null;
		}

		return new PainterWrapper((Painter) ((NimbusStyle) style).getForegroundPainter(ctx));
	}

	/**
	 * Gets the appropriate border Painter, if there is one, for the state
	 * specified in the given SynthContext. This method does appropriate
	 * fallback searching, as described in #get.
	 *
	 * @param ctx
	 *            The SynthContext. Must not be null.
	 *
	 * @return The border painter associated for the given state, or null if
	 *         none could be found.
	 */
	@SuppressWarnings("unchecked")
	public Painter getBorderPainter(SynthContext ctx) {
		if (!(this.style instanceof NimbusStyle)) {
			return null;
		}

		return new PainterWrapper((Painter) ((NimbusStyle) this.style).getBorderPainter(ctx));
	}

	/**
	 * Wrap the sun Painter class with our own.
	 */
	public class PainterWrapper implements Painter {
		protected Painter painter;

		/**
		 * Creates a new PainterWrapper object.
		 *
		 * @param painter
		 *            the painter to be wrapped.
		 */
		public PainterWrapper(Painter painter) {
			this.painter = painter;
		}

		/**
		 * @see Painter#paint(java.awt.Graphics2D, java.lang.Object, int, int)
		 */
		public void paint(Graphics2D g, Object object, int width, int height) {
			if (painter != null) {
				painter.paint(g, object, width, height);
			}
		}
	}
}
