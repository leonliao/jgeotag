package com.leonoss.jgeotag.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface GeoTagOuputStrategy {
	public OutputStream getOutputStreamForTaggedFile(File original)  throws IOException;
}
