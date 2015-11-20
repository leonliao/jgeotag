package com.leonoss.jgeotag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.beust.jcommander.JCommander;
import com.leonoss.jgeotag.arg.ArgOptions;
import com.leonoss.jgeotag.service.GeoTagOuputStrategy;
import com.leonoss.jgeotag.service.ImageService;
import com.leonoss.jgeotag.service.ImageService.TaggingResult;
import com.leonoss.jgeotag.service.exception.GeoTagServiceException;

public class Main {

	private static ArgOptions parseOptions(String[] args) {
		JCommander jCommander = null;
		try {
			ArgOptions argOptions = new ArgOptions();
			jCommander = new JCommander(argOptions);
			jCommander.setProgramName("java -jar jgeotag-[version].jar ");
			jCommander.parse(args);
			return argOptions;
		} catch (com.beust.jcommander.ParameterException e) {
			System.out.println(e.getMessage());
			jCommander.usage();
			System.exit(2);
		}
		return null;
	}

	public static void main(String[] args) {
		ArgOptions argOptions = parseOptions(args);

		GeoTagOuputStrategy strategy = new GeoTagOuputStrategy() {
			@Override
			public OutputStream getOutputStreamForTaggedFile(File original)
					throws IOException {
				File taggedFile = new File(original.getAbsolutePath()
						+ ".TAGGED.JPG");
				if (!taggedFile.exists() && !taggedFile.createNewFile()) {
					throw new IOException("Can not create the file."
							+ taggedFile.getAbsolutePath());
				}
				return new FileOutputStream(taggedFile);
			}
		};

		final ImageService imageService = new ImageService();
		try {
			TaggingResult result = imageService.geoTagWithGpx(
					Arrays.asList(argOptions.getGpx()), argOptions.getPath(),
					strategy);

			if (!result.succeeded.isEmpty()) {
				System.out.println("Succeeded to Geo Tag below files:");
				for (int i = 0; i < result.succeeded.size(); i++) {
					System.out.println(result.succeeded.get(i)
							.getAbsolutePath());
				}
				System.out.println("--------------------------------");

			}

			if (!result.notTagged.isEmpty()) {
				System.out
						.println("No date or not GPS coordinate found for below files:");
				for (int i = 0; i < result.notTagged.size(); i++) {
					System.out.println(result.notTagged.get(i)
							.getAbsolutePath());
				}
				System.out.println("--------------------------------");
			}

			if (!result.failed.isEmpty()) {
				System.out.println("Failed to Geo Tag below files:");
				for (int i = 0; i < result.failed.size(); i++) {
					Exception exception = result.failedExceptions.get(i);
					String errorMsg = exception.getMessage();
					if (exception instanceof GeoTagServiceException) {
						GeoTagServiceException svcException = (GeoTagServiceException) exception;
						if (svcException.getCode() == GeoTagServiceException.CODE_INVALID_IMAGE_PATH) {
							errorMsg = "File/path is invalid, check whether it is existing or a valid JPEG.";
						}
					}
					System.out.printf("File: %s failed for reason: %s\n",
							result.failed.get(i).getAbsolutePath(), errorMsg);
				}
			}

		} catch (GeoTagServiceException e) {
			if (e.getCode() == GeoTagServiceException.CODE_GPX_NOT_FOUND) {
				System.out
						.println("Failed to access or parse ["
								+ argOptions.getGpx()
								+ "]. Check whether the file is existing or it is a valid gpx file.");
			} else {
				System.out.println("Failed to GeoTag file/directory at "
						+ argOptions.getPath() + " because of "
						+ e.getMessage());
			}
		}
	}

}
