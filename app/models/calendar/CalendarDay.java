package models.calendar;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import models.party.Owner;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "owner_id", "year", "month", "day" }) })
public class CalendarDay extends Model {

	@Id
	public Long id;

	@NotNull
	@ManyToOne
	public Owner owner;

	@NotNull
	public Year year;
	@NotNull
	public Month month;
	@NotNull
	public Day day;
	@NotNull
	public DoW dow;

	@OneToOne(cascade=CascadeType.ALL)
	private CalendarDay nextCalDay;

	public HolidayType type;
	public String name;

	//1970 年 1 月 1 日 00:00:00 GMTからの経過日数
	@NotNull
	public int _passedDay;
	//1970 年 1 月 1 日 00:00:00 GMTからの経過日数(稼働日数)
	@NotNull
	public int _passedWorkingDay;
	@NotNull
	public boolean isHoliday;
	@NotNull
	public boolean isWorkingDay;

	private static final long ONE_DATE_TIME = 86400000;

	//派生属性（初期化時に設定）
	public int _week;

	public static Finder<Long, CalendarDay> $find = new Finder<Long, CalendarDay>(
			Long.class, CalendarDay.class);

	public CalendarDay(Owner owner, Year year, Month month, Day day, DoW dow) {
		this.owner = owner;
		this.year = year;
		this.month = month;
		this.day = day;
		this.dow = dow;
		this._week = this.setWeek();
		this._passedDay = this.setPassedDay();
	}

	private int setPassedDay() {
		Date target = this.getTime();
		long passedTime = target.getTime();
		int ret = (int) (passedTime / ONE_DATE_TIME);
//		System.out.println("date = " + target + ", 経過日数 = " + ret);
		return ret;
	}

	//コンストラクタにより、初期化されない変数はsetメソッドを作らないと、永続化またはインスタンス化（たぶんインスタンス化）がうまくいかない
	public void setPassedWorkingDay(int passedWorkingDay) {
		this._passedWorkingDay = passedWorkingDay;
	}

	public void setNextCalendarDay(CalendarDay nextCalDay) {
		this.nextCalDay = nextCalDay;
	}
	private int setWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(year.getNumber(), month.getNumber(), day.getNumber());
		int week = cal.get(Calendar.WEEK_OF_MONTH);
		return week;
	}

	public CalendarDay getNextCalendarDay() {
		return this.nextCalDay;
	}

	public void initialize(Holiday holiDay) {
		HolidayRule holiDayRule = holiDay.getHoliDayRule(month, day, dow);
		if(holiDayRule == null) {
			type = HolidayType.稼働;
			name = "平日";
			isHoliday = false;
		}
		else {
			type = holiDayRule.type;
			name = holiDayRule.name;
			isHoliday = type.isHoliday();
		}
		isWorkingDay = !(isHoliday);
	}

	public boolean isHoliday() {
		return this.isHoliday();
	}

	public boolean isWorkingDay() {
		return this.isWorkingDay;
	}

	public Date getTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(year.getNumber(), month.getNumber(), day.getNumber());
		return cal.getTime();
	}

}
