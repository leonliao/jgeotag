package jgeotag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GPSInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.leonoss.jgeotag.service.GeoTagOuputStrategy;
import com.leonoss.jgeotag.service.ImageService;
import com.leonoss.jgeotag.service.ImageService.TaggingResult;

public class ImageServiceTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		File fileWithOriginal = new File(this.getClass().getClassLoader()
				.getResource("img_with_orig.jpg").getPath());

		Date result = (new ImageService())
				.readExifDateFromFile(fileWithOriginal);
		assertNotNull(result);
	}

	@Test
	public void getTag() throws Exception {
		String fileWithOriginal = this.getClass().getClassLoader()
				.getResource("img_with_orig.jpg").getPath();
		String path = this.getClass().getClassLoader()
				.getResource("sample.gpx").getPath();

		final ImageService imageService = new ImageService();
		// final PipedOutputStream pipedOutput = new PipedOutputStream();
		final File taggedFile = new File(fileWithOriginal + ".TAGGED.JPG");
		

		TaggingResult result = imageService.geoTagWithGpx(
				Arrays.asList(path), fileWithOriginal,
				new GeoTagOuputStrategy() {

					@Override
					public OutputStream getOutputStreamForTaggedFile(
							File original) throws IOException {
						if (!taggedFile.exists()) {
							if (!taggedFile.createNewFile()) {
								throw new IllegalStateException("Cannot create test file.");
							}
						}
						return new FileOutputStream(taggedFile);
					}
				});
		// PipedInputStream pipedIn = new PipedInputStream();
		// pipedIn.connect(pipedOutput);
		// pipedOutput.close();

		assertTrue(result.failed.isEmpty());
		assertEquals(1, result.succeeded.size());
		GPSInfo info = imageService.readGpsInfo(
				new FileInputStream(taggedFile), "pipe");
		assertNotNull(info);
		// pipedIn.close();
	}

}
