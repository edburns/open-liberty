/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package web.staticannotationmixed;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;

import web.common.BaseServlet;

//Exact URL pattern for annotations - /StaticAnnotationMixed1
// Because we have conflict with url in web.xml, we ignore the security annotation
@WebServlet(name = "StaticAnnotationMixed1", urlPatterns = { "/StaticAnnotationMixed1" })
@ServletSecurity(@HttpConstraint(rolesAllowed = { "AllAuthenticated" }))
public class StaticAnnotationMixed1 extends BaseServlet {
    private static final long serialVersionUID = 1L;

    public StaticAnnotationMixed1() {
        super("StaticAnnotationMixed1");
    }

}
