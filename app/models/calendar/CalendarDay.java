package models.calendar;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "year_id", "month", "day" }) })
public class CalendarDay extends Model {

	@Id
	public Long id;

	@NotNull
	@ManyToOne
	public CalendarYear year;

	@NotNull
	public Month month;
	@NotNull
	public Day day;
	public DoW dow;

	public HolidayType type;
	public String name;
//	public String name英名;

	//派生属性（初期化時に設定）
	public int _week;

	public static Finder<Long, CalendarDay> $find = new Finder<Long, CalendarDay>(
			Long.class, CalendarDay.class);

	public CalendarDay(CalendarYear year, Month month, Day day, DoW dow) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.dow = dow;
		this._week = this.setWeek();
	}

	private int setWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(year.year, month.getNumber(), day.getNumber());
		int week = cal.get(Calendar.WEEK_OF_MONTH);
		return week;
	}

	public void initialize(Holiday holiDay) {
		HolidayRule holiDayRule = holiDay.getHoliDayRule(month, day, dow);
		if(holiDayRule == null) {
			type = HolidayType.稼働;
			name = "平日";
		}
		else {
			type = holiDayRule.type;
			name = holiDayRule.name;
		}
	}

	public boolean isHoliday() {
		return this.type.isHoliday();
	}

	public boolean isWorkingDay() {
		return !(this.isHoliday());
	}

	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(year.year, month.getNumber(), day.getNumber());
		return cal.getTime();
	}
}
