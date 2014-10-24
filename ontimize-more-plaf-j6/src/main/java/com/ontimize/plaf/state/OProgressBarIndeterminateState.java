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
 * $Id: OProgressBarIndeterminateState.java,v 1.2 2013/06/25 06:27:06 daniel.grana Exp $
 */
package com.ontimize.plaf.state;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import com.sun.java.swing.plaf.nimbus.State;
/**
 * Is progress bar indeterminate?
 */
public class OProgressBarIndeterminateState extends State {

    /**
     * Creates a new ProgressBarIndeterminateState object.
     */
    public OProgressBarIndeterminateState() {
        super("Indeterminate");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        return ((JProgressBar) c).isIndeterminate();
    }
}
