package models.calendar;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class 休日規則 extends Model {

	@Id
	public Long id;

	public 休日タイプ type;
	public String name;
	public List<月> month;
	public List<日> day;
	public List<曜日> dow;

	@ManyToOne
	public 休日 holiDay;

	public static Finder<Long, 休日規則> $find = new Finder<Long, 休日規則>(
			Long.class, 休日規則.class);

	public 休日規則(休日タイプ type, String name, List<月> month, List<日> day, List<曜日> dow) {
		this.type = type;
		this.name = name;
		this.month = month;
		this.day = day;
		this.dow = dow;
	}

	public boolean isHoliday() {
		return this.type.isHoliday();
	}

	public boolean isMatch(月 targetMonth, 日 targetDay, 曜日 targetDow) {
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
