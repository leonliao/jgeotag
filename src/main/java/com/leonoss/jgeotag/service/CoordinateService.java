package com.leonoss.jgeotag.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.leonoss.jgeotag.gpx.GpxType;
import com.leonoss.jgeotag.gpx.TrkType;
import com.leonoss.jgeotag.gpx.TrksegType;
import com.leonoss.jgeotag.gpx.WptType;

public class CoordinateService {
	private List<TrkType> tracks = new ArrayList<TrkType>();

	/**
	 * Initiate GPS route info
	 * 
	 * @param gpxInfo
	 */
	public CoordinateService(List<GpxType> gpxInfo) {
		Object[] lastTrackAndSegment = getLastSegment();
		TrksegType lastSegment = (TrksegType) lastTrackAndSegment[1];
		TrkType lastTrack = (TrkType) lastTrackAndSegment[0];

		for (GpxType gpx : gpxInfo) {
			for (TrkType track : gpx.getTrk()) {
				// merge all segments to first segment
				// 将同一个track的所有路段合并在一起，使一条track只有一个segment
				TrksegType firstSegment = track.getTrkseg().get(0);
				for (int i = 1; i < track.getTrkseg().size(); i++) {
					firstSegment.getTrkpt().addAll(
							track.getTrkseg().get(i).getTrkpt());
				}

				track.getTrkseg().clear();
				track.getTrkseg().add(firstSegment);

				// 看看上一个track和最后一个点跟当前track的第一个点是否足够近
				// 如果是，则将当前track的所有点合并到上一条track
				if (lastSegment != null
						&& isCloseEnough(
								lastSegment.getTrkpt().get(
										lastSegment.getTrkpt().size() - 1),
								firstSegment.getTrkpt().get(0))) {
					lastSegment.getTrkpt().addAll(firstSegment.getTrkpt());
				} else {
					tracks.add(track);
				}

				lastTrack = track;
				lastSegment = lastTrack.getTrkseg() == null
						|| lastTrack.getTrkseg().isEmpty() ? null : lastTrack
						.getTrkseg().get(lastTrack.getTrkseg().size() - 1);
			}
		}
	}

	private Object[] getLastSegment() {
		TrksegType lastSegment = null;
		TrkType lastTrack = null;
		if (!tracks.isEmpty()) {
			lastTrack = tracks.get(tracks.size() - 1);
			if (!lastTrack.getTrkseg().isEmpty()) {
				lastSegment = lastTrack.getTrkseg().get(
						lastTrack.getTrkseg().size() - 1);
			}
		}

		return new Object[] { lastTrack, lastSegment };
	}

	public static boolean isCloseEnough(WptType point1, WptType point2) {
		// 假设所有track都不是同一路段的
		return false;
	}

	/**
	 * Return a calculated GPS point for a specific date.
	 * 
	 * @param date
	 * @return
	 */
	public GpsPoint queryPointForTime(Date date) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		WptType searchPoint = new WptType();
		try {
			searchPoint.setTime(DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(c));
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("SHOULD NOT happend", e);
		}

		for (TrkType track : tracks) {
			for (TrksegType segment : track.getTrkseg()) {
				// 根据时间搜索最近的点
				int index = Collections.binarySearch(segment.getTrkpt(),
						searchPoint, new Comparator<WptType>() {
							@Override
							public int compare(WptType o1, WptType o2) {
								return o1.getTime().compare(o2.getTime());
							}
						});
				if (index >= 0) {
					// 刚好记录了对应的时间
					return buildGpsPointFromWptType(segment.getTrkpt().get(
							index));
				} else {
					int insertionIndex = 0 - index - 1;
					if (insertionIndex == 0
							|| insertionIndex > segment.getTrkpt().size() - 1) {
						// 超出track的范围
						continue;
					}
					// 按照时间线性分布去计算经纬度
					// 比如查找时间刚好在两个时间点中间，则经纬度的中点即为经纬度
					WptType startPoint = segment.getTrkpt().get(
							insertionIndex - 1);
					WptType endPoint = segment.getTrkpt().get(insertionIndex);

					long startTimeInMillis = startPoint.getTime()
							.toGregorianCalendar().getTimeInMillis();
					long endTimeInMillis = endPoint.getTime()
							.toGregorianCalendar().getTimeInMillis();
					double deviationFromStart = (date.getTime() - startTimeInMillis)
							/ (double) (endTimeInMillis - startTimeInMillis);

					GpsPoint point = new GpsPoint();
					if (startPoint.getLat() != null
							&& endPoint.getLat() != null
							&& startPoint.getLon() != null
							&& endPoint.getLon() != null) {
						point.setLat(computeValue(startPoint.getLat(),
								endPoint.getLat(), deviationFromStart));
						point.setLon(computeValue(startPoint.getLon(),
								endPoint.getLon(), deviationFromStart));
					} else {
						return null;
					}

					if (startPoint.getEle() != null
							&& endPoint.getEle() != null) {
						point.setEle(computeValue(startPoint.getEle(),
								endPoint.getEle(), deviationFromStart));
					}

					return point;
				}
			}
		}
		return null;
	}

	private BigDecimal computeValue(BigDecimal start, BigDecimal end,
			double deviationFromStart) {
		return start.add(end.subtract(start).multiply(
				BigDecimal.valueOf(deviationFromStart)));
	}

	private GpsPoint buildGpsPointFromWptType(WptType wptType) {
		GpsPoint point = new GpsPoint();
		point.setLat(wptType.getLat());
		point.setLon(wptType.getLon());
		point.setEle(wptType.getEle());
		point.setDateTime(wptType.getTime().toGregorianCalendar()
				.getTimeInMillis());
		return point;
	}
}
