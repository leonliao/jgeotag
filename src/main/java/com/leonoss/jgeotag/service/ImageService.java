package com.leonoss.jgeotag.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;

import com.leonoss.jgeotag.gpx.GpxType;
import com.leonoss.jgeotag.service.exception.GeoTagServiceException;

public class ImageService {

	public Date readExifDateFromFile(final File file)
			throws GeoTagServiceException {
		try {
			final ImageMetadata metadata = Imaging.getMetadata(file);
			if (metadata instanceof JpegImageMetadata) {
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
				TiffField field = jpegMetadata
						.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

				if (field == null) {
					field = jpegMetadata
							.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_DIGITIZED);
					if (field == null) {
						return null;
					}
				}
				String dateTime = field.getValue().toString();
				SimpleDateFormat sdformat = new SimpleDateFormat(
						"yyyy:MM:dd HH:mm:ss");
				return sdformat.parse(dateTime);
			}

		} catch (ImageReadException | IOException | ParseException e) {
			throw new GeoTagServiceException(
					GeoTagServiceException.CODE_INVALID_IMAGE_PATH,
					"Can not read EXIF info from file.", e);
		}
		return null;
	}

	public TiffImageMetadata.GPSInfo readGpsInfo(final InputStream inputStream,
			final String file) throws GeoTagServiceException {
		try {
			final ImageMetadata metadata = Imaging.getMetadata(inputStream,
					file);
			if (metadata instanceof JpegImageMetadata) {
				final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
				// simple interface to GPS data
				final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
				if (null != exifMetadata) {
					final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata
							.getGPS();
					return gpsInfo;
				}
			}

		} catch (ImageReadException | IOException e) {
			throw new GeoTagServiceException(
					GeoTagServiceException.CODE_INVALID_IMAGE_PATH,
					"Can not read EXIF info from file.", e);
		}
		return null;
	}

	public void setExifGPSTag(final File jpegImageFile,
			final GeoTagOuputStrategy strategy, final GpsPoint point)
			throws IOException, ImageReadException, ImageWriteException {
		OutputStream os = null;
		boolean canThrow = false;
		try {
			TiffOutputSet outputSet = null;

			// note that metadata might be null if no metadata is found.
			final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
			final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			if (null != jpegMetadata) {
				// note that exif might be null if no Exif metadata is found.
				final TiffImageMetadata exif = jpegMetadata.getExif();

				if (null != exif) {
					// TiffImageMetadata class is immutable (read-only).
					// TiffOutputSet class represents the Exif data to write.
					//
					// Usually, we want to update existing Exif metadata by
					// changing
					// the values of a few fields, or adding a field.
					// In these cases, it is easiest to use getOutputSet() to
					// start with a "copy" of the fields read from the image.
					outputSet = exif.getOutputSet();
				}
			}

			// if file does not contain any exif metadata, we create an empty
			// set of exif metadata. Otherwise, we keep all of the other
			// existing tags.
			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}

			final double longitude = point.getLon().doubleValue();
			final double latitude = point.getLat().doubleValue();
			outputSet.setGPSInDegrees(longitude, latitude);
			os = strategy.getOutputStreamForTaggedFile(jpegImageFile);
			new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
					outputSet);
			canThrow = true;
		} finally {
			IoUtils.closeQuietly(canThrow, os);
		}
	}

	public static class TaggingResult {
		public List<File> succeeded = new ArrayList<File>();
		public List<File> failed = new ArrayList<File>();
		public List<Exception> failedExceptions = new ArrayList<Exception>();
		public List<File> notTagged = new ArrayList<File>();
	}

	public TaggingResult geoTagWithGpx(List<String> gpxFiles, String path,
			GeoTagOuputStrategy strategy) throws GeoTagServiceException {
		TaggingResult result = new TaggingResult();
		List<GpxType> tracks = new ArrayList<GpxType>();

		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(GpxType.class)
					.createUnmarshaller();
			for (String gpxPath : gpxFiles) {
				@SuppressWarnings("unchecked")
				JAXBElement<GpxType> gpx = (JAXBElement<GpxType>) unmarshaller
						.unmarshal(new File(gpxPath));
				tracks.add(gpx.getValue());
			}
		} catch (JAXBException e) {
			throw new GeoTagServiceException(
					GeoTagServiceException.CODE_GPX_NOT_FOUND,
					"Can not parse GPX files, file not found or invalid.", e);
		}
		CoordinateService coordinateService = new CoordinateService(tracks);

		File pathFile = new File(path);
		if (pathFile.isDirectory()) {
			File[] allImageFiles = pathFile.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					final String lowerCasePath = pathname.getAbsolutePath()
							.toLowerCase();
					return pathname.isFile()
							&& (lowerCasePath.endsWith("jpg") || lowerCasePath
									.endsWith("jpeg"));
				}
			});

			for (File file : allImageFiles) {
				try {
					if (geoTagSingleFile(coordinateService, file, strategy)) {
						result.succeeded.add(file);
					} else {
						result.notTagged.add(file);
					}
				} catch (Exception e) {
					result.failed.add(file);
					result.failedExceptions.add(e);
					continue;
				}
			}
		} else {
			try {
				if (geoTagSingleFile(coordinateService, pathFile, strategy)) {
					result.succeeded.add(pathFile);
				} else {
					result.notTagged.add(pathFile);
				}
			} catch (Exception e) {
				result.failed.add(pathFile);
				result.failedExceptions.add(e);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param coordinateService
	 * @param path
	 * @param strategy
	 * @return Whether the file is geo tagged
	 * @throws GeoTagServiceException
	 */
	private boolean geoTagSingleFile(CoordinateService coordinateService,
			File path, GeoTagOuputStrategy strategy)
			throws GeoTagServiceException {
		final Date readExifDateFromFile = readExifDateFromFile(path);
		if (readExifDateFromFile != null) {
			GpsPoint point = coordinateService
					.queryPointForTime(readExifDateFromFile);
			if (point != null) {
				try {
					setExifGPSTag(path, strategy, point);
				} catch (ImageReadException | ImageWriteException | IOException e) {
					throw new GeoTagServiceException("Can not geo tag file "
							+ path.getAbsolutePath(), e);
				}

				return true;
			}
		}

		return false;
	}
}
