package cn.movie.robot.utils;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateUtil {

  public static final long ONE_DAY = 24 * 3600 * 1000L;
  public static final int EIGHT_HOUR = 8;
  private static final String yyyy_MM_dd = "yyyy-MM-dd";
  private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
  private static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

  private static String format(Date date, String format) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  private static Date parse(String dateStr, String format) {
    if (dateStr == null) {
      return null;
    }
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      return dateFormat.parse(dateStr);
    } catch (ParseException e) {
      return null;
    }
  }

  public static Date parse_yyyy_MM_dd(String date) {
    return parse(date, yyyy_MM_dd);
  }

  public static String format_yyyy_MM_dd(Date date) {
    return format(date, yyyy_MM_dd);
  }

  public static String format_yyyy_MM_dd_HH_mm(Date date) {
    return format(date, yyyy_MM_dd_HH_mm);
  }

  public static Date parseDateTime(String date) {
    return parse(date, yyyy_MM_dd_HH_mm_ss);
  }

  public static String formatDateTime(Date date) {
    return format(date, yyyy_MM_dd_HH_mm_ss);
  }

  public static Date addEightHours(Date date) {
    return addHourOfDate(date, EIGHT_HOUR);
  }


  public static List<String> getTimeList(String startTime, String endTime) {
    List<String> list = Lists.newArrayList();
    Date date = new Date();

    long start = DateUtil.parse_yyyy_MM_dd(startTime).getTime();
    long end = DateUtil.parse_yyyy_MM_dd(endTime).getTime();

    for (long time = start; time <= end; time += ONE_DAY) {
      date.setTime(time);
      list.add(format_yyyy_MM_dd(date));
    }
    return list;
  }

  public static List<List<String>> getStartAndEndTimeList(String startTime, String endTime) {
    List<List<String>> list = Lists.newArrayList();
    Date date = new Date();

    long start = DateUtil.parse_yyyy_MM_dd(startTime).getTime();
    long end = DateUtil.parse_yyyy_MM_dd(endTime).getTime();

    for (long time = start; time <= end; time += ONE_DAY) {
      List<String> times = Lists.newArrayList();

      date.setTime(time);
      times.add(format_yyyy_MM_dd(date));

      date.setTime(time + ONE_DAY);
      times.add(format_yyyy_MM_dd(date));

      list.add(times);
    }
    return list;
  }

  public static Date addHourOfDate(Date date, int hour) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.HOUR_OF_DAY, hour);
    Date newDate = c.getTime();
    return newDate;
  }


  public static List<String> getStartAndEndDay(String startDay, String endDay, int intervalDays) {
    LocalDate nowDay = LocalDate.now();
    LocalDate maxDay = nowDay.plusDays(intervalDays - 1);

    LocalDate startDate = StringUtils.isBlank(startDay) ? nowDay : LocalDate.parse(startDay).isBefore(nowDay.plusDays(-1))
        ? nowDay.plusDays(-1) : LocalDate.parse(startDay);

    LocalDate endDate = StringUtils.isBlank(endDay) || LocalDate.parse(endDay).isAfter(maxDay)
        ? maxDay : LocalDate.parse(endDay);

    if (startDate.isAfter(maxDay) || endDate.isBefore(nowDay.plusDays(-1))) {
      return null;
    }
    return Lists.newArrayList(startDate.toString(), endDate.toString());
  }

}
