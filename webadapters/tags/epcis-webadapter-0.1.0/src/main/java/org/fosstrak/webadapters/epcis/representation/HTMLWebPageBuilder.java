/*
 * Copyright (C) 2010 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org) and
 * was developed as part of the webofthings.com initiative.
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */
package org.fosstrak.webadapters.epcis.representation;

import org.fosstrak.webadapters.epcis.model.EPCISResource;
import org.fosstrak.webadapters.epcis.model.TreeMenu;

/**
 * Class to build the EPCIS Webadapter Web Page
 *
 * @author Mathias Mueller mathias.mueller(at)unifr.ch, <a href="http://www.guinard.org">Dominique Guinard</a>
 *
 */
public class HTMLWebPageBuilder extends HTMLBuilder {

    /**
     * Builds the HTML representation of a resource
     *
     *
     * @param resource
     *
     * @return
     */
    @Override
    public String buildRepresentation(EPCISResource resource) {
        StringBuilder res = new StringBuilder();

        res.append(HTMLStringBuilder.beginHTML());
        res.append(HTMLStringBuilder.buildHeader());
        res.append(buildContainerBody(resource.getName(), buildTreeMenu(resource), super.buildRepresentation(resource), resource.getUri()));
        res.append(HTMLStringBuilder.endHTML());

        return res.toString();
    }

    private String buildTreeMenu(EPCISResource resource) {
        String   res;
        TreeMenu treeMenu = resource.getTreeMenu();

        if (treeMenu == null) {
            res = "";
        } else {
            res = HTMLStringBuilder.buildTreeMenu(resource);
        }

        return res;
    }

    private String buildContainerBody(String title, String treeMenu, String content, String uri) {
        String res = "";

        if (title == null) {
            title = "";
        }

        res = HTMLStringBuilder.buildContainerBody(title, treeMenu, content, uri);

        return res;
    }
}
