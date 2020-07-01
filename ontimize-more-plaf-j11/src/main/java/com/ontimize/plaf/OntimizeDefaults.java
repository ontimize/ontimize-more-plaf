package com.ontimize.plaf;

import java.lang.reflect.Constructor;
import java.net.URL;

import javax.swing.UIDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext;
import com.ontimize.plaf.utils.ReflectionUtils;

public class OntimizeDefaults {

	private static final Logger logger = LoggerFactory.getLogger(OntimizeDefaults.class);

	public static class LazyPainter implements UIDefaults.LazyValue {

		protected int which;
		protected String className;

		protected String path;
		protected PaintContext ctx;

		public LazyPainter(String className, int which) {
			this.className = className;
			this.which = which;
		}

		public LazyPainter(String className, int which, String path) {
			this.className = className;
			this.which = which;
			this.path = path;
		}

		public LazyPainter(String className, int which, PaintContext ctx) {
			this.className = className;
			this.which = which;
			this.ctx = ctx;
		}

		public LazyPainter(String className, int which, PaintContext ctx, String path) {
			this.className = className;
			this.which = which;
			this.ctx = ctx;
			this.path = path;
		}

		@Override
		public Object createValue(UIDefaults table) {
			try {
				Class c;
				Object cl;

				// See if we should use a separate ClassLoader
				if ((table == null) || !((cl = table.get("ClassLoader")) instanceof ClassLoader)) {
					cl = Thread.currentThread().getContextClassLoader();
					if (cl == null) {
						// Fallback to the system class loader.
						cl = ClassLoader.getSystemClassLoader();
					}
				}

				// CALLING THE CLASS BY REFLEXION:
				Constructor constructor = null;

				// to call methods, whose construction has two parameters: an
				// int and an URL:
				if ((this.path != null) && (this.ctx != null)) {
					try {
						return ReflectionUtils.newInstance(this.className, this.which, this.ctx, this.path);
					} catch (Exception ex) {
						OntimizeDefaults.logger.trace("Trying another constructor...", ex);
						return ReflectionUtils.newInstance(this.className, this.which, this.ctx, this.getClass().getClassLoader().getResource(this.path));
					}
				} else if (this.path != null) {
					c = Class.forName(this.className, true, (ClassLoader) cl);
					constructor = c.getConstructor(int.class, URL.class);
					if (constructor == null) {
						throw new NullPointerException("Failed to find the constructor for the class: " + this.className);
					}
					return constructor.newInstance(this.which, this.getClass().getClassLoader().getResource(this.path));

					// to call methods, whose construction has two parameters:
					// an int and an PaintContext:
				} else if (this.ctx != null) {
					c = Class.forName(this.className, true, (ClassLoader) cl);
					Class paintContextClass = Class.forName("com.ontimize.plaf.painter.AbstractRegionPainter$PaintContext", true, (ClassLoader) cl);
					constructor = c.getConstructor(int.class, paintContextClass);
					// PaintContext.class);
					if (constructor == null) {
						throw new NullPointerException("Failed to find the constructor for the class: " + this.className);
					}
					return constructor.newInstance(this.which, this.ctx);

					// to call methods, whose construction has two parameters:
					// an int and an URL:
				} else {

					c = Class.forName(this.className, true, (ClassLoader) cl);
					constructor = c.getConstructor(int.class);
					if (constructor == null) {
						throw new NullPointerException("Failed to find the constructor for the class: " + this.className);
					}
					return constructor.newInstance(this.which);
				}

			} catch (Exception e) {
				OntimizeDefaults.logger.error("", e);
				return null;
			}
		}

	}
}
