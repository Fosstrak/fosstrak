package org.accada.epcis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 * <p>
 * Java class for AttributeType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AttributeType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute name=&quot;id&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeType", namespace = "urn:epcglobal:epcis-masterdata:xsd:1", propOrder = { "content" })
public class AttributeType {

    @XmlMixed
    @XmlAnyElement
    protected List<Object> content;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the content property.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the content property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getContent().add(newItem);
     * </pre>
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     * {@link Element }
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed
     * property on this class.
     * <p>
     * the map is keyed by the name of the attribute and the value is the string
     * value of the attribute. the map returned by this method is live, and you
     * can add new attribute by updating the map directly. Because of this
     * design, there's no setter.
     * 
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
