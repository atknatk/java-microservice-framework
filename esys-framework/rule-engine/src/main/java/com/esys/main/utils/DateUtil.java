package com.esys.main.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author selcukme
 *
 */

//@Configuration
//@PropertySource("classpath:dateutil.properties")
//@ConfigurationProperties(prefix = "date.format")
public class DateUtil {
	
	
	public static String DATE_FORMAT_DD_MM_YYYY = "dd.MM.yyyy";
	public static String DATE_FORMAT_YYYY_MM_DD="yyyy.mm.dd";

	public static long MILLIS_IN_DAY;
	public static int DAYS_IN_YEAR;
	public static int DAYS_IN_LEAPYEAR;
	public static final BigDecimal YEAR_DURATION = BigDecimal.valueOf(365);
	public static final BigDecimal LEAPYEAR_DURATION = BigDecimal.valueOf(366);

	private static Locale defaultLocale = new Locale("tr", "TR");

	
	
	public static Calendar convertDatetoCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static java.sql.Date convertToSQLDate(Date date) {
		if (date != null) {
			return new java.sql.Date(date.getTime());
		}
		return null;
	}

	public static java.sql.Timestamp convertToSQLTimestamp(Date date) {
		if (date != null) {
			return new java.sql.Timestamp(date.getTime());
		}
		return null;
	}

	public static DateFormat getDateFormatter(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
				defaultLocale);
		return formatter;
	}

	public static DateFormat getDefaultDateFormatter() {
		return getDateFormatter(DATE_FORMAT_DD_MM_YYYY);
	}

//	public static DateFormat getSlashDateFormatter() {
//		return getDateFormatter(CommonConstants.DATE_FORMAT_SLASH);
//	}
//
//	public static DateFormat getMonthYearDateFormatter() {
//		return getDateFormatter(CommonConstants.MONTH_YEAR_DATE_FORMAT);
//	}
//
//	public static DateFormat getLowDashedDateFormatter() {
//		return getDateFormatter(CommonConstants.DATE_FORMAT_LOWDASHED);
//	}
//
//	public static DateFormat getDashedDateFormatter() {
//		return getDateFormatter(CommonConstants.DATE_FORMAT_DASHED);
//	}
//
//	public static DateFormat getDefaultDateTimeFormatter() {
//		return getDateFormatter(CommonConstants.DATETIME_FORMAT);
//	}
//
//	public static DateFormat getDottedDateTimeFormatter() {
//		return getDateFormatter(CommonConstants.DATETIME_FORMAT_DOTTED);
//	}
//
//	public static DateFormat getLowDashedDateTimeFormatter() {
//		return getDateFormatter(CommonConstants.DATETIME_FORMAT_LOWDASHED);
//	}
//
//	public static DateFormat getISODateFormatter() {
//		return getDateFormatter(CommonConstants.ISO_DATE_FORMAT);
//	}
//
//	public static DateFormat getISODateTimeFormatter() {
//		return getDateFormatter(CommonConstants.ISO_DATETIME_FORMAT);
//	}

	public static String getTimeStringOfDate(Date date) {
		return DateFormat.getTimeInstance(DateFormat.SHORT, defaultLocale)
				.format(date);
	}

	public static Long findNumberOfDays(Date firstDate, Date secondDate) {
		return (secondDate.getTime() - firstDate.getTime())
				/ (1000 * 60 * 60 * 24);
	}

	public static boolean isLeapYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		return isLeapYear(year);
	}

	public static boolean isLeapYear(int year) {
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gonderilen Tarihin Saatini dondurur
	 */
	public static int getHours(Date myDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		return hours;
	}
	
	/**
	 * Gonderilen Tarihin Dakikasini dondurur
	 */
	public static int getMinutes(Date myDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		int minute = c.get(Calendar.MINUTE);
		return minute;
	}

	/**
	 * Gonderilen Tarihin yilini dondurur
	 */
	public static int getYear(Date myDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * Gonderilen Tarihin ayini dondurur
	 */
	public static int getMonth(Date myDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		int month = c.get(Calendar.MONTH);
		if (month >= 0) // ilk ay bu fonksiyondan 0(sifir) larak gelmekte , yani

			// gercek ay bilgisi icin bir arttirilmali
			month++;
		return month;
	}
	public static int getDay(Date myDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	/**
	 * Saati 23.59.59'a cevirir ve doner.
	 */
	public static Date getDateWithLastTime(Date date) {
		if (date == null)
			return null;
		return parseDateWithTimeByMaxMilliSeconds(DateUtil.parseYear(date),
				DateUtil.parseMonth(date), DateUtil.parseDay(date), 23, 59, 59);
	}

	/**
	 * Saati 00.00.00'a cevirir ve doner.
	 */
	public static Date getDateWithFirstTime(Date date) {
		if (date == null)
			return null;
		return parseDateWithTimeByMinMilliSeconds(DateUtil.parseYear(date),
				DateUtil.parseMonth(date), DateUtil.parseDay(date), 00, 00, 00);
	}

	public static Date add(Date myDate, int day, int month, int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		c.add(Calendar.YEAR, year);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	public static Date addTime(Date myDate, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		c.add(Calendar.HOUR, hour);
		c.add(Calendar.MINUTE, minute);
		c.add(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Date addCurrentTime(Date myDate) {
		Calendar c = Calendar.getInstance();
		int hour, minute, second = 0;
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		second = c.get(Calendar.SECOND);

		c.setTime(myDate);
		c.add(Calendar.HOUR, hour);
		c.add(Calendar.MINUTE, minute);
		c.add(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Date findMaxDate(List<Date> dates) {
		Date maxDate = null;
		long maxTimeInMillis = 0;
		for (int i = 0; i < dates.size(); i++) {
			Date date = dates.get(i);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			long timeInMillis = c.getTimeInMillis();
			if (timeInMillis > maxTimeInMillis) {
				maxDate = date;
				maxTimeInMillis = timeInMillis;
			}
		}
		return maxDate;
	}

	/**
	 *
	 * -1 karsilastirdigim tarih kucuk (-1: comparableDate is smaller than date
	 * ) 1 karsilastirdigim tarih buyuk ( 1: comparableDate is bigger than date
	 * ) 0 karsilastirdigim tarih esit. ( 0: comparableDate is equal to date )
	 */
	public static int compareDate(Date comparableDate, Date date) {
		if (comparableDate == null && date == null) {
			return 0;
		} else if (comparableDate != null && date == null) {
			return 1;
		} else if (comparableDate == null && date != null) {
			return -1;
		}

		Calendar dateCalendar, comparableDateCalendar;
		dateCalendar = Calendar.getInstance();
		comparableDateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		comparableDateCalendar.setTime(comparableDate);

		if (dateCalendar.get(Calendar.YEAR) == comparableDateCalendar
				.get(Calendar.YEAR)
				&& dateCalendar.get(Calendar.MONTH) == comparableDateCalendar
						.get(Calendar.MONTH)
				&& dateCalendar.get(Calendar.DATE) == comparableDateCalendar
						.get(Calendar.DATE))
			return 0;

		else if (comparableDateCalendar.before(dateCalendar))
			return -1;
		else if (comparableDateCalendar.after(dateCalendar))
			return 1;

		return 85;
	}

	public static Date parseDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (day > maxDay) {
			c.set(Calendar.DAY_OF_MONTH, maxDay);
		} else {
			c.set(Calendar.DAY_OF_MONTH, day);
		}

		return c.getTime();
	}

	public static Calendar wrapAsCalendar(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance(defaultLocale);
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal;
		}
		return null;
	}

	public static Date strToDate(String dateStr) {
		return parseDateWithDefaultFormat(dateStr);
	}
//
//	public static Date isoStrToDate(String dateStr) {
//		return parseDateWithISOFormat(dateStr);
//	}

	public static Date parseDateWithDefaultFormat(String dateStr) {
		Date retval = null;
		try {
			retval = getDefaultDateFormatter().parse(dateStr);
		} catch (ParseException pe) {
			retval = null;
		}
		return retval;
	}

//	public static Date parseDateWithISOFormat(String dateStr) {
//		Date retval = null;
//		try {
//			retval = getISODateFormatter().parse(dateStr);
//		} catch (ParseException pe) {
//			retval = null;
//		}
//		return retval;
//	}

	public static Date parseDateWithCurrentTime(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return c.getTime();
	}

	public static Date parseDateWithTime(int year, int month, int day,
			int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, minute, second);
		return c.getTime();
	}

	public static Date parseDateWithTimeByMinMilliSeconds(int year, int month,
			int day, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, minute, second);
		c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
		return c.getTime();
	}

	public static Date parseDateWithTimeByMaxMilliSeconds(int year, int month,
			int day, int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, minute, second);
		c.set(Calendar.MILLISECOND, c.getActualMaximum(Calendar.MILLISECOND));
		return c.getTime();
	}

	public static Date generateNewDate() {
		return new Date();
	}

	public static Date generateNewDate(int day, int month, int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, 0, 0, 0);
		return c.getTime();
	}

	public static Date generateNewDateWithoutTime(Date date) {
		if (date == null)
			return null;
		return parseDate(parseYear(date), parseMonth(date), parseDay(date));
	}

	public static Date generateNewDateWithoutTime() {
		Date d = new Date();
		return generateNewDateWithoutTime(d);
	}

	public static int parseYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static int parseMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static int parseDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	public static int parseHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int parseMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	public static int parseSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	/**
	 * baslangic tarihi < bitis tarihi seklinde farzeder.Girilen 2 tarih
	 * araliklarinin iakiiip iakiimadiiini kontrol eder
	 *
	 * @param startDateToCompared
	 * @param endDateToCompared
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean areDateConflicts(Date startDateToCompared,
			Date endDateToCompared, Date startDate, Date endDate) {
		if (endDate == null && endDateToCompared != null) {
			if ((endDateToCompared.compareTo(startDate) == -1))
				return false;
			else
				return true;
		}

		if (endDate == null && endDateToCompared == null) {
			return true;
		}

		if (endDate != null && endDateToCompared == null) {
			if ((startDateToCompared.compareTo(startDate) == 1))
				return false;
			else
				return true;
		}

		return true;
	}

	/**
	 * finds day of the year of the parameter date
	 *
	 * @param date
	 * @return day of the year of the parameter date
	 */
	public static int parseDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * finds day difference between two dates. result int day = myDate -
	 * anotherDate
	 *
	 * */
	public static int getDifference(Date myDate, Date anotherDate) {
		long deltaDate = (myDate.getTime() - anotherDate.getTime())
				/ MILLIS_IN_DAY;
		return (int) deltaDate;
	}

	public static Date subtractMonthFromDate(Date date, int numOfMonth) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		cal1.add(Calendar.MONTH, -numOfMonth);
		return cal1.getTime();
	}

	public static Date subtractDayFromDate(Date date, int numOfDay) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		cal1.add(Calendar.DATE, -numOfDay);
		return cal1.getTime();
	}

	/**
	 * @author sorhan
	 * @return month difference
	 */
	public static int monthDifference(Date firstDate, Date secondDate) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(firstDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(secondDate);
		int montDifference = cal2.get(Calendar.MONTH)
				- cal1.get(Calendar.MONTH);
		int yearDifferecence = cal2.get(Calendar.YEAR)
				- cal1.get(Calendar.YEAR);
		return yearDifferecence * 12 + montDifference;
	}

//	public static String formatYYYY_MM_DD_HH_MI_SN(Date d) {
//		if (d == null)
//			return null;
//		return getDottedDateTimeFormatter().format(d);
//	}
//
//	public static String formatDD_MM_YYYY_HH_MI_SN(Date d) {
//		if (d == null)
//			return null;
//		return getLowDashedDateTimeFormatter().format(d);
//	}
//
//	public static String formatDD_MM_YYYY(Date d) {
//		if (d == null)
//			return null;
//		return getLowDashedDateFormatter().format(d);
//	}
//
//	public static String formatYYYYDDMMwithDash(Date d) {
//		if (d == null)
//			return null;
//		return getISODateFormatter().format(d);
//	}
//
//	public static String formatYYYYMMDD_HHMMSS(Date d) {
//		if (d == null)
//			return null;
//
//		String dateStr = getDottedDateTimeFormatter().format(d);
//		dateStr = dateStr.replaceAll("\\.", "").replaceAll(":", "");
//		return dateStr;
//	}

	public static String formatYYYYMMDD(Date d) {
		if (d == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd"); // Set your date format
		String currentData = sdf.format(d);
		return currentData;
	}

//	public static String formatTodayYYYYMMDD_HHMMSS() {
//		return formatYYYYMMDD_HHMMSS(new Date());
//	}
//
//	public static String formatTodayYYYY_MM_DD_HH_MI_SN() {
//		return DateUtil.formatYYYY_MM_DD_HH_MI_SN(new Date());
//	}

	public static String formatDDMMYYYY(Date d) {
		return getDefaultDateFormatter().format(d);
	}

//	public static String formatDDMMYYYYWithSlash(Date d) {
//		return getSlashDateFormatter().format(d);
//	}
//
//	public static String formatMMYYYY(Date d) {
//		return getMonthYearDateFormatter().format(d);
//	}
//
//	public static String formatDDMMYYYYWithDash(Date d) {
//		return getDashedDateFormatter().format(d);
//	}
//
//	public static String formatDateWithoutTime(Date d, String dateFormat) {
//		return getDateFormatter(dateFormat).format(d);
//	}
//
//	public static String formatDateWithoutTime(Date d, String dateFormat,
//			String seperator) {
//		if (d == null)
//			return null;
//		String[] dateSeq = new String[3];
//		StringTokenizer tokenizer = new StringTokenizer(dateFormat, seperator);
//		int i = 0;
//		while (tokenizer.hasMoreTokens()) {
//			dateSeq[i] = tokenizer.nextToken();
//			i++;
//		}
//		String dateStr = null;
//		String day = String.valueOf(DateUtil.parseDay(d));
//		if (day.length() == 1) {
//			day = "0" + day;
//		}
//		String month = String.valueOf(DateUtil.parseMonth(d));
//		if (month.length() == 1) {
//			month = "0" + month;
//		}
//		if (dateSeq[0].equals(CommonConstants_1.DAY)) {
//			dateStr = day + seperator;
//		} else if (dateSeq[0].equals(CommonConstants_1.MONTH)) {
//			dateStr = month + seperator;
//		} else if (dateSeq[0].equals(CommonConstants_1.YEAR)) {
//			dateStr = DateUtil.parseYear(d) + seperator;
//		}
//		if (dateSeq[1].equals(CommonConstants_1.DAY)) {
//			dateStr += day + seperator;
//		} else if (dateSeq[1].equals(CommonConstants_1.MONTH)) {
//			dateStr += month + seperator;
//		} else if (dateSeq[1].equals(CommonConstants_1.YEAR)) {
//			dateStr += DateUtil.parseYear(d) + seperator;
//		}
//
//		if (dateSeq[2].equals(CommonConstants_1.DAY)) {
//			dateStr += day;
//		} else if (dateSeq[2].equals(CommonConstants_1.MONTH)) {
//			dateStr += month;
//		} else if (dateSeq[2].equals(CommonConstants_1.YEAR)) {
//			dateStr += String.valueOf(DateUtil.parseYear(d));
//		}
//
//		return dateStr;
//	}

	public static String formatDate(Date date, String format) {
		DateFormat formatter = getDateFormatter(format);
		return formatter.format(date);

	}

	public static Date parseDate(String string, String pattern) {
		if (string == null)
			return null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			return formatter.parse(string);
		} catch (java.text.ParseException e) {
			return null;
		}
	}

	public static List<Date> getDaysOfMonths(Date d1, Date d2, int dayInDate) {
		List<Date> daysOfMonthsList = new ArrayList<Date>();
		int d1Day = DateUtil.parseDay(d1);
		int d1Month = DateUtil.parseMonth(d1);
		int d1Year = DateUtil.parseYear(d1);
		int d2Day = DateUtil.parseDay(d2);
		int d2Month = DateUtil.parseMonth(d2);
		int d2Year = DateUtil.parseYear(d2);

		int startMonth = 0;
		int endMonth = 0;
		if (d1Year == d2Year) { // Baslangic ve bitis tarihleri ayni yilda ise;
			if (d1Day <= dayInDate)
				startMonth = d1Month;
			else
				startMonth = d1Month + 1;

			if (d2Day >= dayInDate)
				endMonth = d2Month + 1;
			else
				endMonth = d2Month;
			for (int i = startMonth; i < endMonth; i++) {
				Date d = new Date();

				d = DateUtil.parseDate(d1Year, i, dayInDate);
				daysOfMonthsList.add(d);
			}
		} else { // Baslangic ve bitis tarihleri ayni yilda degilse;
			// 1.Baslangic tarihinin yilinin son ayina kadar hesaplanir
			if (d1Day <= dayInDate)
				startMonth = d1Month;
			else
				startMonth = d1Month + 1;

			// endMonth = 13;

			for (int i = startMonth; i < 13; i++) {
				Date d = new Date();

				d = DateUtil.parseDate(d1Year, i, dayInDate);
				daysOfMonthsList.add(d);
			}
			// 2. Arada 1 yil varsa;

			startMonth = 1;
			if (d2Day >= dayInDate)
				endMonth = d2Month;
			else
				endMonth = d2Month - 1;

			for (int i = startMonth; i < endMonth + 1; i++) {
				Date d = new Date();

				d = DateUtil.parseDate(d2Year, i, dayInDate);
				daysOfMonthsList.add(d);
			}

			// arada 1 den fazla yil varsa;
			for (int j = 1; j < d2Year - d1Year; j++) {
				for (int i = 1; i < 13; i++) {
					Date d = new Date();

					d = DateUtil.parseDate(d1Year + j, i, dayInDate);
					daysOfMonthsList.add(d);
				}
			}
		}
		List<Date> removableItems = new ArrayList<Date>();
		for (Date d : daysOfMonthsList) {
			if (parseDay(d) != dayInDate) {
				removableItems.add(d);
			}
		}
		daysOfMonthsList.removeAll(removableItems);

		return daysOfMonthsList;
	}

	public static List<Date> getDaysOfWeeks(Date d1, Date d2, int dayInWeek) {
		List<Date> daysOfWeeksList = new ArrayList<Date>();
		Date startDate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(d1);

		if (dayInWeek < c.get(Calendar.DAY_OF_WEEK)) {
			startDate = add(d1, 7 - c.get(Calendar.DAY_OF_WEEK) + dayInWeek, 0,
					0);
		} else if (dayInWeek == c.get(Calendar.DAY_OF_WEEK))
			startDate = d1;
		else if (dayInWeek > c.get(Calendar.DAY_OF_WEEK))
			startDate = add(d1, dayInWeek - c.get(Calendar.DAY_OF_WEEK), 0, 0);

		for (Date tempDate = startDate; compareDate(tempDate, add(d2, 1, 0, 0)) < 0; tempDate = add(
				tempDate, 7, 0, 0))
			daysOfWeeksList.add(tempDate);

		if (d1.equals(d2)) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			if (c1.get(Calendar.DAY_OF_WEEK) == dayInWeek) {
				daysOfWeeksList.add(d1);
			}

		}

		return daysOfWeeksList;
	}

	public static List<Date> getDatesBetweenTwoDates(Date d1, Date d2) {
		List<Date> datesList = new ArrayList<Date>();
		for (Date tempDate = d1; compareDate(tempDate, add(d2, 1, 0, 0)) < 0; tempDate = add(
				tempDate, 1, 0, 0)) {
			datesList.add(tempDate);
		}

		return datesList;
	}

	/*
	 * Author:cdurgun 20.11.2007
	 */

	public static Date getLastDateOfMonth(Date d1) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d1);
		int lastMonthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int year = parseYear(d1);
		int month = parseMonth(d1);
		Date lastDate = DateUtil.parseDate(year, month, lastMonthDay);
		return lastDate;
	}

	public static Date getFirstDateOfWeek(Date d1) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d1);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date firstDateOfWeek = calendar.getTime();
		return firstDateOfWeek;
	}

	public static List<Date> getLastDaysOfMonths(Date d1, Date d2) {
		List<Date> datesList = new ArrayList<Date>();
		for (Date tempDate = d1; compareDate(tempDate, d2) < 0; tempDate = add(
				tempDate, 0, 1, 0)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(tempDate);
			int lastday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

			Date d = new Date();
			int year = parseYear(tempDate);
			int month = parseMonth(tempDate);
			d = DateUtil.parseDate(year, month, lastday);
			datesList.add(d);
		}
		return datesList;
	}

	public static boolean isWorkingDay(Date date, List<Date> holidayList) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 7
				|| calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			return false;
		} else if (holidayList.contains(date)) {
			return false;
		} else
			return true;

	}

	public static Date previousWorkingDay(Date date, List<Date> holidayList) {
		while (!isWorkingDay(date, holidayList)) {
			date = add(date, -1, 0, 0);
		}
		return date;
	}

	public static Date nextWorkingDay(Date date, List<Date> holidayList) {
		while (!isWorkingDay(date, holidayList)) {
			date = add(date, 1, 0, 0);
		}
		return date;
	}

	public static Date addDay(Date myDate, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}

	public static Date addMonth(Date myDate, int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(myDate);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}

	public static Date truncateDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static boolean isValidDate(String date, String pattern) {
		if (StringUtil.isNotNullAndNotEmpty(date)) {
			DateFormat df = new SimpleDateFormat(pattern);
			try {
				Date d = df.parse(date);
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * <p>
	 * Checks if two calendars represent the same day ignoring time.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal2
				.get(Calendar.DAY_OF_YEAR));
	}
	/**
	 * verilen iki string gun ve saati merge eder
	 * Önr:"01.01.2018" ve "15:16:56" string degerleri "01.01.2018 15:47" olur
	 * @param day
	 * @param hours
	 * @return
	 */
	public static Date mergeDateAndTime(String dateStr,String timeStr,String pattern){
		Date dayDate = DateUtil.parseDate(dateStr, pattern);
		return mergeDateAndTime(dayDate, timeStr, pattern);
		
	}
	
	public static Date mergeDateAndTime(Date date,String timeStr,String pattern){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String[] timeParts = timeStr.split(":");
		String timePart1 = timeParts[0];
		String timePart2 = timeParts[1];
		cal.set(Calendar.HOUR_OF_DAY, new Integer(timePart1));
		cal.set(Calendar.MINUTE, new Integer(timePart2));		
		return cal.getTime();
	}
	
	public static String milisecondsConvertToHHMMSSFormat(Long millis) {
		 String hms = (String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
		return hms;
		} 

}
