package models.calendar;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class HolidayRule extends Model {

	@Id
	public Long id;

	public HolidayType type;
	public String name;
	public List<Month> month;
	public List<Day> day;
	public List<DoW> dow;

	@ManyToOne
	public Holiday holiDay;

	public static Finder<Long, HolidayRule> $find = new Finder<Long, HolidayRule>(
			Long.class, HolidayRule.class);

	public HolidayRule(HolidayType type, String name, List<Month> month, List<Day> day, List<DoW> dow) {
		this.type = type;
		this.name = name;
		this.month = month;
		this.day = day;
		this.dow = dow;
	}

	public boolean isHoliday() {
		return this.type.isHoliday();
	}

	public boolean isMatch(Month targetMonth, Day targetDay, DoW targetDow) {
		if( !(month.contains(targetMonth)) ) {
			return false;
		}
		else if( !(day.contains(targetDay)) ) {
			return false;
		}
		else if( !(dow.contains(targetDow)) ) {
			return false;
		}
		return true;
	}
}
