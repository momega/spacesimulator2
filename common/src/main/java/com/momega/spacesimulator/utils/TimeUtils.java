package com.momega.spacesimulator.utils;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Set of the function working with time
 * Created by martin on 6/7/14.
 */
public class TimeUtils {

    /**
     * Julian Day 2000 timestamp instance
     */
    public static final Timestamp JD2000 = fromJulianDay(2000.0);

    public static DateTimeFormatter UTC_FORMATTER = ISODateTimeFormat.dateTime();
    
    private static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
		    .appendHours()
		    .appendSuffix(" h")
		    .appendSeparator(" ")
		    .appendMinutes()
		    .appendSuffix(" m")
		    .appendSeparator(" ")
		    .appendSeconds()
		    .appendSuffix(" s")
		    .toFormatter();

    /**
     * Creates the time from julian day
     * @param julianDay julian day as double value
     * @return new instance of the time
     */
    public static Timestamp fromJulianDay(double julianDay) {
        Timestamp time = new Timestamp();
        time.setValue(DateTimeUtils.fromJulianDay(julianDay) / DateTimeConstants.MILLIS_PER_SECOND);
        return time;
    }
    
    /**
     * Creates the timestamp
     * @param seconds
     * @return new instance of the timestamp
     */
    public static Timestamp fromSeconds(double seconds) {
    	Timestamp time = new Timestamp();
    	time.setValue(seconds);
    	return time;
    }

    /**
     * Creates the timestamp object based on the calendar instance
     * @param calendar the JDK calendar instance
     * @return the new timestamp instance
     */
    public static Timestamp fromCalendar(Calendar calendar) {
        Assert.notNull(calendar);
        long t = calendar.getTimeInMillis();
        return fromSeconds( t / DateTimeConstants.MILLIS_PER_SECOND);
    }

    /**
     * Creates the time
     * @param datetime datetime
     * @return new instance of the time
     */
    public static Timestamp fromDateTime(DateTime datetime) {
        Timestamp time = new Timestamp();
        time.setValue(datetime.getMillis() / DateTimeConstants.MILLIS_PER_SECOND);
        return time;
    }

    /**
     * Converts the timestamp to JODA date time
     * @param timestamp the timestamp instance
     * @return the instance of the JODA time
     */
    public static DateTime toDateTime(Timestamp timestamp) {
        return new DateTime(toLinuxTime(timestamp));
    }

    public static long toLinuxTime(Timestamp timestamp) {
        long t = Double.valueOf(timestamp.getValue() * DateTimeConstants.MILLIS_PER_SECOND).longValue();
        return t;
    }

    public static Calendar toCalendar(Timestamp timestamp) {
        long t = toLinuxTime(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t);
        return calendar;
    }

    public static double getDuration(Timestamp startTime, Timestamp endTime) {
        return endTime.subtract(startTime);
    }

    public static double getDuration(TimeInterval timeInterval) {
        return getDuration(timeInterval.getEndTime(), timeInterval.getEndTime());
    }

    public static TimeInterval createInterval(Timestamp timestamp, double duration) {
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(timestamp.add(duration));
        return timeInterval;
    }

    public static boolean isTimestampInInterval(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getStartTime());
        Assert.notNull(interval.getEndTime());

        return (interval.getStartTime().compareTo(timestamp) <=0) &&
                (interval.getEndTime().compareTo(timestamp) >= 0);
    }

    public static boolean isIntervalInFuture(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getStartTime());

        return interval.getStartTime().compareTo(timestamp) > 0;
    }

    public static boolean isIntervalInPast(Timestamp timestamp, TimeInterval interval) {
        Assert.notNull(timestamp);
        Assert.notNull(interval);
        Assert.notNull(interval.getEndTime());

        return interval.getEndTime().compareTo(timestamp) < 0;
    }

    public static String timeAsString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return UTC_FORMATTER.print(TimeUtils.toDateTime(timestamp));
    }

    public static String durationAsString(double duration) {
        long d = Double.valueOf(duration * DateTimeConstants.MILLIS_PER_SECOND).longValue();
        long days = d / (DateTimeConstants.SECONDS_PER_DAY * DateTimeConstants.MILLIS_PER_SECOND);
        long secs = d % (DateTimeConstants.SECONDS_PER_DAY * DateTimeConstants.MILLIS_PER_SECOND);
        Period p = new Period(secs);
        return "" + days + " d " + periodFormatter.print(p);
    }

//    public static String periodAsString(PositionProvider positionProvider, Timestamp time) {
//        double period = getETA(positionProvider, time);
//        if (period>DateTimeConstants.SECONDS_PER_DAY) {
//            return timeAsString(positionProvider.getTimestamp());
//        }
//        }
//
//    /**
//     * Returns ETA time in seconds between current time and planned time of the orbital point
//     * @return the ETA in seconds
//     * @param positionProvider position provider
//     * @param time the current time
//     */
//    private static double getETA(PositionProvider positionProvider, Timestamp time) {
//        Timestamp future = positionProvider.getTimestamp();
//        return future.subtract(time);
//    }

}
