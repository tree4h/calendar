package models.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import play.api.db.DB;
import models.party.Owner;

public class CalendarDomainUtil {

	//TODO singletonクラス化
	/*
	 * 指定された所有者のカレンダーにおいて、指定された日（相対日指定）から次の稼働日を返す
	 * rel 0 = TODAY, 1 = TOMORROW, -1 = YESTERDAY
	 */
	public static Date getNextWorkingDate(String ownerName, int rel) {
		Owner owner = Owner.$find.where().eq("name", ownerName).findUnique();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, rel);
		int yearNumber = cal.get(Calendar.YEAR);
		int monthNumber = cal.get(Calendar.MONTH);
		int dayNumber = cal.get(Calendar.DATE);

		Year year = Year.get年(yearNumber);
		Month month = Month.get月(monthNumber);
		Day day = Day.get日(dayNumber);

		boolean flag = true;
		CalendarDay targetDay = CalendarDay.$find.where().eq("owner", owner)
				.eq("year", year).eq("month", month).eq("day", day).findUnique();
		while(flag) {
			//Next稼働日なし
			//TODO Next稼働日なしのときの処理
			if( targetDay == null ) {
				return null;
			}
			else if( targetDay.isWorkingDay() ) {
				return targetDay.getTime();
			}
			else {
				System.out.println("targetDayTime=" + targetDay.getTime());
				targetDay = targetDay.getNextCalendarDay();
			}
		}
		//TODO Next稼働日なしのときの処理(年跨ぎ対応済)
		return null;
	}

	private static CalendarDay changeCalendarDay(Owner owner, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int yearNumber = cal.get(Calendar.YEAR);
		int monthNumber = cal.get(Calendar.MONTH);
		int dayNumber = cal.get(Calendar.DATE);

		Year year = Year.get年(yearNumber);
		Month month = Month.get月(monthNumber);
		Day day = Day.get日(dayNumber);

		return CalendarDay.$find.where().eq("owner", owner).eq("year", year).eq("month", month).eq("day", day).findUnique();
	}

	public static int howManyDaysBetween(Date start, Date end) {
		Owner owner = Owner.$find.where().eq("name", "ISKEN").findUnique();
		CalendarDay startDay = changeCalendarDay(owner, start);
		CalendarDay endDay = changeCalendarDay(owner, end);

		return endDay._passedDay - startDay._passedDay + 1;
	}

	public static int howManyWorkingDaysBetween(Owner owner, Date start, Date end) {
		CalendarDay startDay = changeCalendarDay(owner, start);
		CalendarDay endDay = changeCalendarDay(owner, end);

//		System.out.println("start:" + startDay.getTime() + "," + startDay._passedWorkingDay);
//		System.out.println("end:" + endDay.getTime() + "," + endDay._passedWorkingDay);
		int ret = endDay._passedWorkingDay - startDay._passedWorkingDay;
		if(startDay._passedWorkingDay == 0) {
			return ret;
		}
		else {
			return ret+1;
		}
	}

	private static final long ONE_DATE_TIME = 86400000;
	/*
	 * 与えられた期間の日数は（日数計算）
	 * 1.最初に 2 つの日付を long 値に変換します。
	 * ※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの経過ミリ秒数となります。
	 * 2.次にその差を求めます。
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで日付の差を求めることができます。
	 * ※1 日 (24 時間) は、86,400,000 ミリ秒です。
	 */
	public static int howManyDaysBetween2(Date start, Date end) {
		long startTime = start.getTime();
		long endTime = end.getTime();
		long diffDays = (endTime - startTime) / ONE_DATE_TIME;
		return (int)diffDays+1;
	}

	/*
	 * 与えられた期間の稼働日数は（日数計算）
	 */
	public static int howManyWorkingDaysBetween3(Owner owner, Date start, Date end) {
		CalendarDay startDay = changeCalendarDay(owner, start);
		CalendarDay endDay = changeCalendarDay(owner, end);

		int ret = 0;
		boolean flag = true;
		CalendarDay currentDay = startDay;
		while(flag) {
			if(currentDay.isWorkingDay) {
				ret++;
			}
			if(currentDay.equals(endDay)) {
				flag = false;
			}
			else {
//				System.out.println("month="+currentDay.month.getNumber() + "day=" + currentDay.day.getNumber() );
				currentDay = currentDay.getNextCalendarDay();
			}
		}
		return ret;
	}
	/*
	 * 与えられた期間の日数は（日数計算）
	 */
	public static int howManyDaysBetween3(Date start, Date end) {
		Owner owner = Owner.$find.where().eq("name", "ISKEN").findUnique();
		CalendarDay startDay = changeCalendarDay(owner, start);
		CalendarDay endDay = changeCalendarDay(owner, end);

		int ret = 0;
		boolean flag = true;
		CalendarDay currentDay = startDay;
		while(flag) {
			ret++;
			if(currentDay.equals(endDay)) {
				flag = false;
			}
			else {
//				System.out.println("month="+currentDay.month.getNumber() + "day=" + currentDay.day.getNumber() );
				currentDay = currentDay.getNextCalendarDay();
			}
		}
		return ret;
	}
}
