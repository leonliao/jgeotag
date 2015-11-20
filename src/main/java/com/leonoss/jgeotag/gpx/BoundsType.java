//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2015.11.18 时间 11:24:52 PM CST 
//


package com.leonoss.jgeotag.gpx;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 	 Two lat/lon pairs defining the extent of an element.
 *     
 * 
 * <p>boundsType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="boundsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="minlat" use="required" type="{http://www.topografix.com/GPX/1/1}latitudeType" />
 *       &lt;attribute name="minlon" use="required" type="{http://www.topografix.com/GPX/1/1}longitudeType" />
 *       &lt;attribute name="maxlat" use="required" type="{http://www.topografix.com/GPX/1/1}latitudeType" />
 *       &lt;attribute name="maxlon" use="required" type="{http://www.topografix.com/GPX/1/1}longitudeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "boundsType")
public class BoundsType {

    @XmlAttribute(name = "minlat", required = true)
    protected BigDecimal minlat;
    @XmlAttribute(name = "minlon", required = true)
    protected BigDecimal minlon;
    @XmlAttribute(name = "maxlat", required = true)
    protected BigDecimal maxlat;
    @XmlAttribute(name = "maxlon", required = true)
    protected BigDecimal maxlon;

    /**
     * 获取minlat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinlat() {
        return minlat;
    }

    /**
     * 设置minlat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinlat(BigDecimal value) {
        this.minlat = value;
    }

    /**
     * 获取minlon属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinlon() {
        return minlon;
    }

    /**
     * 设置minlon属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinlon(BigDecimal value) {
        this.minlon = value;
    }

    /**
     * 获取maxlat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxlat() {
        return maxlat;
    }

    /**
     * 设置maxlat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxlat(BigDecimal value) {
        this.maxlat = value;
    }

    /**
     * 获取maxlon属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxlon() {
        return maxlon;
    }

    /**
     * 设置maxlon属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxlon(BigDecimal value) {
        this.maxlon = value;
    }

}
