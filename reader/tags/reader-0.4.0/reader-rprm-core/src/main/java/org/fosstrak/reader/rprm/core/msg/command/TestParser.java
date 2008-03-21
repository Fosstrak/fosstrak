/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.reader.rprm.core.msg.command;

import java.io.StringReader;

import antlr.CommonAST;

class TestParser {
    public static void main(String[] args) {
        try {
        	String cmd = "!123rd.getName param,param,{liste1,liste2},param \n";
        	StringReader sr = new StringReader(cmd);
            TextLexer lexer = new TextLexer(sr);
            TextCommandParser parser = new TextCommandParser(lexer);
            // Parse the input expression
            TextCommandParserHelper helper = parser.command_line();
            CommonAST t = (CommonAST)parser.getAST();
            // Print the resulting tree out in LISP notation
            System.out.println(t.toStringList());
            System.out.println("CommandID is " + helper.getId());
            Command command = helper.buildCommandTree();
            System.out.println(command.getId());
            if (command.getReaderDevice().getGetName() != null) {
            	System.out.println("ReaderDevice.getName() Aufruf gefunden.");
            }
        } catch(Exception e) {
            System.err.println("exception: "+e);
        }
    }
}
