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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.07.05 at 05:26:35 PM CEST 
//


package org.accada.reader.rprm.core.msg.reply;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.accada.reader.rprm.core.msg.reply.FieldNameListReturnType;
import org.accada.reader.rprm.core.msg.reply.FieldNameReply;


/**
 *  Field Name enumeration object replies. 
 * 
 * <p>Java class for FieldNameReply complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FieldNameReply">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="getSupportedNames" type="{urn:epcglobal:rp:xsd:1}FieldNameListReturnType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldNameReply", propOrder = {
    "getSupportedNames"
})
public class FieldNameReply {

    protected FieldNameListReturnType getSupportedNames;

    /**
     * Gets the value of the getSupportedNames property.
     * 
     * @return
     *     possible object is
     *     {@link FieldNameListReturnType }
     *     
     */
    public FieldNameListReturnType getGetSupportedNames() {
        return getSupportedNames;
    }

    /**
     * Sets the value of the getSupportedNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldNameListReturnType }
     *     
     */
    public void setGetSupportedNames(FieldNameListReturnType value) {
        this.getSupportedNames = value;
    }

}
