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
public class カレンダ日 extends Model {

	@Id
	public Long id;

	@NotNull
	@ManyToOne
	public カレンダ年 year;

	@NotNull
	public 月 month;
	@NotNull
	public 日 day;
	public 曜日 dow;

	public 休日タイプ type;
	public String name;
//	public String name英名;

	//派生属性（初期化時に設定）
	public int _week;

	public static Finder<Long, カレンダ日> $find = new Finder<Long, カレンダ日>(
			Long.class, カレンダ日.class);

	public カレンダ日(カレンダ年 year, 月 month, 日 day, 曜日 dow) {
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

	public void initialize(休日 holiDay) {
		休日規則 holiDayRule = holiDay.getHoliDayRule(month, day, dow);
		if(holiDayRule == null) {
			type = 休日タイプ.稼働;
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
