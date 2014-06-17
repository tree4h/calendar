package models.calendar;

import java.util.Calendar;
import java.util.Date;

import models.party.所有者;

public class カレンダDomainUtil {

	//TODO singletonクラス化
	/*
	 * 指定された所有者のカレンダーにおいて、指定された日（相対日指定）から次の稼働日を返す
	 * rel 0 = TODAY, 1 = TOMORROW, -1 = YESTERDAY
	 */
	public static Date getNextWorkingDate(所有者 owner, int rel) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, rel);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);

		カレンダ年 targetCal = カレンダ年.$find.where().eq("year", year).eq("owner", owner).findUnique();

		System.out.println("param: month=" + (month+1) + ", day=" + day);

		Date ret = targetCal.getNextWorkingDay(month, day);
		if(ret == null) {
			targetCal = カレンダ年.$find.where().eq("year", year+1).eq("owner", owner).findUnique();
			//TODO 年変りが1月1日に依存したコード
			ret = targetCal.getNextWorkingDay(0, 1);
		}
		return ret;
	}
}
