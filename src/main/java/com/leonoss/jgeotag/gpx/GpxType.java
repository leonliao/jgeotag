//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.11.18 时间 11:24:52 PM CST 
//


package com.leonoss.jgeotag.gpx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		GPX documents contain a metadata header, followed by waypoints, routes, and tracks.  You can add your own elements
 * 		to the extensions section of the GPX document.
 * 	  
 * 
 * <p>gpxType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="gpxType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadata" type="{http://www.topografix.com/GPX/1/1}metadataType" minOccurs="0"/>
 *         &lt;element name="wpt" type="{http://www.topografix.com/GPX/1/1}wptType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="rte" type="{http://www.topografix.com/GPX/1/1}rteType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="trk" type="{http://www.topografix.com/GPX/1/1}trkType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="extensions" type="{http://www.topografix.com/GPX/1/1}extensionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.1" />
 *       &lt;attribute name="creator" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gpxType", propOrder = {
    "metadata",
    "wpt",
    "rte",
    "trk",
    "extensions"
})
public class GpxType {

    protected MetadataType metadata;
    protected List<WptType> wpt;
    protected List<RteType> rte;
    protected List<TrkType> trk;
    protected ExtensionsType extensions;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "creator", required = true)
    protected String creator;

    /**
     * 获取metadata属性的值。
     * 
     * @return
     *     possible object is
     *     {@link MetadataType }
     *     
     */
    public MetadataType getMetadata() {
        return metadata;
    }

    /**
     * 设置metadata属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link MetadataType }
     *     
     */
    public void setMetadata(MetadataType value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the wpt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wpt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWpt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WptType }
     * 
     * 
     */
    public List<WptType> getWpt() {
        if (wpt == null) {
            wpt = new ArrayList<WptType>();
        }
        return this.wpt;
    }

    /**
     * Gets the value of the rte property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rte property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRte().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RteType }
     * 
     * 
     */
    public List<RteType> getRte() {
        if (rte == null) {
            rte = new ArrayList<RteType>();
        }
        return this.rte;
    }

    /**
     * Gets the value of the trk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrk().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrkType }
     * 
     * 
     */
    public List<TrkType> getTrk() {
        if (trk == null) {
            trk = new ArrayList<TrkType>();
        }
        return this.trk;
    }

    /**
     * 获取extensions属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsType }
     *     
     */
    public ExtensionsType getExtensions() {
        return extensions;
    }

    /**
     * 设置extensions属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsType }
     *     
     */
    public void setExtensions(ExtensionsType value) {
        this.extensions = value;
    }

    /**
     * 获取version属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.1";
        } else {
            return version;
        }
    }

    /**
     * 设置version属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * 获取creator属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置creator属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

}
