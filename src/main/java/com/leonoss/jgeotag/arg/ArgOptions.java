package com.leonoss.jgeotag.arg;

import com.beust.jcommander.Parameter;

public class ArgOptions {

	@Parameter(names = "-gpx", description = "The path of a GPX track file.", required = true)
	private String gpx;

	@Parameter(names = "-path", description = "The path of a JPEG/JPG file or a folder containing JPEG/JPG files.", required = true)
	private String path;

	@Override
	public String toString() {
		return "ArgOptions [gpx=" + gpx + ", path=" + path + "]";
	}

	public String getGpx() {
		return gpx;
	}

	public void setGpx(String gpx) {
		this.gpx = gpx;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
