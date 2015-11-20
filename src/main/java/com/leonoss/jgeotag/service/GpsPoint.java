package com.leonoss.jgeotag.service;

import java.math.BigDecimal;

public class GpsPoint {

	protected BigDecimal ele;
	protected BigDecimal lat;
	protected BigDecimal lon;
	protected long dateTime;

	public BigDecimal getEle() {
		return ele;
	}

	public void setEle(BigDecimal ele) {
		this.ele = ele;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

}
