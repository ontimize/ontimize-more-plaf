package com.ontimize.plaf;

import java.lang.reflect.Constructor;
import java.net.URL;

import javax.swing.UIDefaults;

import com.ontimize.plaf.painter.AbstractRegionPainter.PaintContext;
public class OntimizeDefaults {

	public static class LazyPainter implements UIDefaults.LazyValue{

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
				if (table == null || !((cl = table.get("ClassLoader"))
						instanceof ClassLoader)) {
					cl = Thread.currentThread().
					getContextClassLoader();
					if (cl == null) {
						// Fallback to the system class loader.
						cl = ClassLoader.getSystemClassLoader();
					}
				}


				// CALLING THE CLASS BY REFLEXION:
				Constructor constructor = null;

				// to call methods, whose construction has two parameters: an int and an URL:
				if (path!=null && ctx != null){
					c = Class.forName(className, true, (ClassLoader)cl);
					constructor = c.getConstructor(int.class,PaintContext.class,URL.class);
					if (constructor == null) {
						throw new NullPointerException(
								"Failed to find the constructor for the class: " +
								className);
					}
					return constructor.newInstance(which,ctx,getClass().getClassLoader().getResource(this.path));

					
				}else if (path!=null){
					c = Class.forName(className, true, (ClassLoader)cl);
					constructor = c.getConstructor(int.class,URL.class);
					if (constructor == null) {
						throw new NullPointerException(
								"Failed to find the constructor for the class: " +
								className);
					}
					return constructor.newInstance(which,getClass().getClassLoader().getResource(this.path));


					// to call methods, whose construction has two parameters: an int and an PaintContext:
				} else if (ctx!=null){
					c = Class.forName(className, true, (ClassLoader)cl);
					Class paintContextClass = Class.forName("com.ontimize.plaf.painter.AbstractRegionPainter$PaintContext", true, (ClassLoader)cl);
					constructor = c.getConstructor(int.class,paintContextClass);
//							PaintContext.class);
					if (constructor == null) {
						throw new NullPointerException(
								"Failed to find the constructor for the class: " +
								className);
					}
					return constructor.newInstance(which, ctx);


					// to call methods, whose construction has two parameters: an int and an URL:
				}else{

					c = Class.forName(className, true, (ClassLoader)cl);
					constructor = c.getConstructor(int.class);
					if (constructor == null) {
						throw new NullPointerException(
								"Failed to find the constructor for the class: " +
								className);
					}
					return constructor.newInstance(which);
				}



			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}
}
