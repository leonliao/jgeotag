package com.leonoss.jgeotag.service.exception;

public class GeoTagServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4109746773428567891L;
	private int code;
	
	public GeoTagServiceException() {
	}

	public GeoTagServiceException(String message) {
		super(message);
	}

	public GeoTagServiceException(Throwable cause) {
		super(cause);
	}

	public GeoTagServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GeoTagServiceException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}


	public GeoTagServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	public static int CODE_GPX_NOT_FOUND = 1;
	public static int CODE_INVALID_IMAGE_PATH = 2;
	
}
