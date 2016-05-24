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
 * $Id: OToolBarNorthState.java,v 1.3 2013/06/25 06:27:06 daniel.grana Exp $
 */
package com.ontimize.plaf.state;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.nimbus.State;

import com.ontimize.gui.field.DataField;

/**
 * Is the toolbar on the (top) north?
 */
public class RequiredState extends State {

	/**
	 * Creates a new RequiredStare object.
	 */
	public RequiredState() {
		super("Required");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInState(JComponent c) {

		Container container = SwingUtilities.getAncestorOfClass(DataField.class, c);
		if (container instanceof DataField) {
			DataField dF = (DataField) container;
			if (dF.isRequired()) {
				return true;
			}
		}

		// if(c.getParent() instanceof DataField && c.isEnabled()){
		// DataField dF = (DataField)c.getParent();
		// if(dF.isRequired()){
		// return true;
		// }
		// } else if(c.getParent() != null && c.getParent().getParent()
		// instanceof DataField && c.isEnabled()){
		// DataField dF = (DataField)c.getParent().getParent();
		// if(dF.isRequired()){
		// return true;
		// }
		// }
		return false;
	}
}
