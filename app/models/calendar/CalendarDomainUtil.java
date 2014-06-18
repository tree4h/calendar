package models.calendar;

import java.util.Calendar;
import java.util.Date;

import models.party.Owner;

public class CalendarDomainUtil {

	//TODO singletonクラス化
	/*
	 * 指定された所有者のカレンダーにおいて、指定された日（相対日指定）から次の稼働日を返す
	 * rel 0 = TODAY, 1 = TOMORROW, -1 = YESTERDAY
	 */
	public static Date getNextWorkingDate(Owner owner, int rel) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, rel);
		int year = cal.get(Calendar.YEAR);
		int monthNumber = cal.get(Calendar.MONTH);
		int dayNumber = cal.get(Calendar.DATE);

		CalendarYear targetCal = CalendarYear.$find.where().eq("year", year).eq("owner", owner).findUnique();

		System.out.println("param: month=" + (monthNumber+1) + ", day=" + dayNumber);

		Month month = Month.get月(monthNumber);
		Day day = Day.get日(dayNumber);

		boolean flag = true;
		CalendarDay targetDay = CalendarDay.$find.where().eq("year", targetCal).eq("month", month).eq("day", day).findUnique();
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
				cal.add(Calendar.DATE, 1);
				monthNumber = cal.get(Calendar.MONTH);
				dayNumber = cal.get(Calendar.DATE);
				month = Month.get月(monthNumber);
				day = Day.get日(dayNumber);
				targetDay = CalendarDay.$find.where().eq("year", targetCal).eq("month", month).eq("day", day).findUnique();
			}
		}
		//TODO Next稼働日なしのときの処理
		return null;
	}
}
