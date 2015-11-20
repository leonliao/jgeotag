package jgeotag;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.leonoss.jgeotag.gpx.GpxType;
import com.leonoss.jgeotag.service.CoordinateService;
import com.leonoss.jgeotag.service.GpsPoint;

public class CoordinateServiceTest {
	GpxType gpx;

	@Before
	public void setUp() throws Exception {
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> jaxbElement = (JAXBElement<GpxType>) JAXBContext
				.newInstance(GpxType.class)
				.createUnmarshaller()
				.unmarshal(
						this.getClass().getClassLoader()
								.getResourceAsStream("sample.gpx"));
		gpx = jaxbElement.getValue();

		assertNotNull(gpx);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Date dateForFirstPointInTheGpx = Date.from(Instant
				.parse("2015-11-15T08:53:36.000Z"));
		Date dateForLastPointInTheGpx = Date.from(Instant
				.parse("2015-11-15T09:57:43.000Z"));

		CoordinateService service = new CoordinateService(Arrays.asList(gpx));
		GpsPoint lastPoint = service
				.queryPointForTime(dateForLastPointInTheGpx);
		GpsPoint firstPoint = service
				.queryPointForTime(dateForFirstPointInTheGpx);

		assertNotNull(lastPoint);
		assertNotNull(firstPoint);

		Date dateLaterThanLastPointInTheGpx = Date.from(Instant
				.parse("2015-11-15T09:57:43.003Z"));
		GpsPoint pointLaterThanLastPoint = service
				.queryPointForTime(dateLaterThanLastPointInTheGpx);
		assertNull(pointLaterThanLastPoint);

		Date dateEarlierThanFirstPointInTheGpx = Date.from(Instant
				.parse("2015-11-15T08:53:35.900Z"));
		GpsPoint pointEarlierThanFirst = service
				.queryPointForTime(dateEarlierThanFirstPointInTheGpx);
		assertNull(pointEarlierThanFirst);

		/*
		 * 
		 * Point is between below range <trkpt lon="113.42565807513893"
		 * lat="23.13504290767014"> <ele>20.0</ele>
		 * <time>2015-11-15T08:55:29.000Z</time> </trkpt> <trkpt
		 * lon="113.4256040956825" lat="23.135111639276147">
		 * <ele>19.200000762939453</ele> <time>2015-11-15T08:55:41.000Z</time>
		 * </trkpt>
		 */
		Date dateInTheMiddle = Date.from(Instant
				.parse("2015-11-15T08:55:36.000Z"));
		GpsPoint pointInTheMiddle = service.queryPointForTime(dateInTheMiddle);
		assertTrue(pointInTheMiddle
				.getLon()
				.subtract(BigDecimal.valueOf(113.42565807513893))
				.multiply(
						pointInTheMiddle.getLon().subtract(
								BigDecimal.valueOf(113.4256040956825)))
				.doubleValue() < 0);
	}

}
