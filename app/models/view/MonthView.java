package models.view;

import java.util.ArrayList;
import java.util.List;

import models.calendar.CalendarYear;
import models.calendar.CalendarDay;
import models.calendar.HolidayType;
import models.calendar.DoW;
import models.calendar.Month;

public class MonthView {
	public CalendarYear year;
	public Month month;
	public List<DayView> days = new ArrayList<DayView>();

	private final String 日曜日 = "sun";//赤
	private final String 土曜日 = "sat";//青
	private final String 祝日 = "hol";//赤
	private final String 休日 = "japanhol";//橙
	private final String 不稼働 = "comhol";//aqua
	private final String 休出 = "comwork";//黄
	private final String 稼働 = "work";//黒

	public MonthView(CalendarYear calendar, Month month) {
		this.year = calendar;
		this.month = month;
		this.makeDayView();
	}

	public DayView getDayView(int i, int j) {
		int index = 7*(i-1) + (j-1);
		return days.get(index);
	}

	private void  makeDayView() {
		for(int i=1; i<=6; i++) {
			for(int j=1; j<=7; j++) {
				DoW dow = DoW.get曜日(j);
				CalendarDay calDay = CalendarDay.$find.where().eq("year", year).eq("month", month).eq("_week", i).eq("dow", dow).findUnique();
				DayView day;
				if(calDay == null) {
					day = new DayView("", "", "");
				}
				else {
					String className;
					if(calDay.type.isHoliday()) {
						if(calDay.dow.equals(DoW.土)) {
							className = 土曜日;
						}
						else if(calDay.dow.equals(DoW.日)) {
							className = 日曜日;
						}
						else if(calDay.type.equals(HolidayType.祝日)) {
							className = 祝日;
						}
						else if(calDay.type.equals(HolidayType.休日)) {
							className = 休日;
						}
						else {
							className = 不稼働;
						}
					}
					else {
						if(calDay.type.equals(HolidayType.休出)) {
							className = 休出;
						}
						else {
							className = 稼働;
						}
					}
					day = new DayView(Integer.toString(calDay.day.getNumber()), calDay.name, className);
				}
				days.add(day);
			}
		}
	}

}
