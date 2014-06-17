package models.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import models.party.Owner;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "year", "owner_id" }) })
public class CalendarYear extends Model {
	@Id
	public Long id;

	@NotNull
	public int year;

	@NotNull
	@ManyToOne
	public Owner owner;

	//派生属性（初期化時に設定）
	public int _稼働日数;
	public int _休日数;

	@ManyToOne
	public Holiday holiDay;

	@OneToMany(cascade = CascadeType.ALL)
	public List<CalendarDay> dayList = new ArrayList<CalendarDay>();
	@OneToMany(cascade = CascadeType.ALL)
	public List<CalendarDayIndex> dayIndexList = new ArrayList<CalendarDayIndex>();

	public static Finder<Long, CalendarYear> $find = new Finder<Long, CalendarYear>(
			Long.class, CalendarYear.class);

	public CalendarYear(Owner owner, int year) {
		this.owner = owner;
		this.year = year;
	}

	public void applyHoliDay(Holiday holiDay) {
		this.holiDay = holiDay;
		this.makeカレンダ日(holiDay);
		this.applyJapaneseHolidayRule3();
		this._稼働日数 = this.get稼働日数();
		this._休日数 = this.get休日数();
	}

	public void printCalendar() {
		for(CalendarDay day : dayList) {
			System.out.println((day.month.getNumber()+1) + "month, " + day.day.getNumber() + "day, " + day.dow.get英名() + ", " + day._week + "week");
		}
	}

	/*
	 * カレンダ日の初期化
	 * 休日がないカレンダーはあり？
	 */
	private void makeカレンダ日(Holiday holiDay) {
		Calendar cal = Calendar.getInstance();
		int index = 0;
		for(int i=0; i<=11; i++) {
			cal.clear();
			cal.set(this.year, i, 1);
			int startDoW = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			int lastDay = cal.get(Calendar.DATE);

			System.out.println("Month" + (i+1));
			System.out.println("DoW" + startDoW);
			System.out.println("lastDay" + lastDay);

			for(int j=1; j<=lastDay; j++) {
				int k = startDoW + j - 1;
				if(k > 7) {
					k = k % 7;
					if(k == 0) {
						k = 7;
					}
				}
				CalendarDay calDay = new CalendarDay(this, Month.get月(i), Day.get日(j), DoW.get曜日(k));
				calDay.initialize(holiDay);
				dayList.add(calDay);

				//カレンダ日インデックス作成
				CalendarDayIndex calIndex = new CalendarDayIndex(this, i, j, index);
				dayIndexList.add(calIndex);
				index++;
			}
		}
	}

	/*
	 * 『国民の祝日に関する法律（昭和２３年法律第１７８号）第3条』の適用
	 *「国民の祝日」は、休日とする。
	 *2.「国民の祝日」が日曜日に当たるときは、その日後においてその日に最も近い「国民の祝日」でない日を休日とする。
	 *3.その前日及び翌日が「国民の祝日」である日（「国民の祝日」でない日に限る。）は、休日とする。
	 *年を跨いで、本法律が成立する状況はないことを前提とする
	 *例）12月31日が祝日で日曜日になるパターンはないことが前提
	 *例）12月3０，３１日が祝日で、30日が日曜日になるパターンはないことが前提
	 */
	private void applyJapaneseHolidayRule3() {
		for(CalendarDay calDay : dayList) {
			if( calDay.type.equals(HolidayType.祝日) ) {
				if(calDay.dow.equals(DoW.日)) {
					Month month = calDay.month;
					Day day = calDay.day;
					int currentIndex = dayList.indexOf(calDay);
					int index = currentIndex+1;
					while(index > currentIndex) {
						CalendarDay targetDay = dayList.get(index);
						if( !(targetDay.type.equals(HolidayType.祝日)) ) {
							//TODO setter作る？
							targetDay.type = HolidayType.休日;
							targetDay.name = "休日";
							index = -1;
						}
						index++;
					}
				}
			}
		}
	}

	private int get稼働日数() {
		int counter = 0;
		for(CalendarDay day : dayList) {
			if( day.isWorkingDay() ) {
				counter++;
			}
		}
		return counter;
	}

	private int get休日数() {
		int counter = 0;
		for(CalendarDay day : dayList) {
			if( day.isHoliday() ) {
				counter++;
			}
		}
		return counter;
	}

	public Date getNextWorkingDay(int month, int day) {
		CalendarDayIndex calIndex = CalendarDayIndex.$find.where().eq("year", this).eq("month", month).eq("day", day).findUnique();
		int startIndex = calIndex.index;

		//TODO kesu
		System.out.println("startIndex" + startIndex);

		dayList = CalendarDay.$find.where().eq("year", this).findList();

		//this.refresh();
		//System.out.println("size" + dayList.size());
		//TODO dayList.size()が機能しない。Playの問題？ 365を暫定的
		for(int index=startIndex; index<=dayList.size(); index++ ) {
			CalendarDay targetDay = dayList.get(index);

			//TODO kesu
			System.out.println("index" + index);
			System.out.println("month" + targetDay.month.getNumber());
			System.out.println("day" + targetDay.day.getNumber());

			if( targetDay.isWorkingDay() ) {
				return targetDay.getTime();
			}
		}
		//Next稼働日なし
		//TODO Next稼働日なしのときの処理
		return null;
	}

	public Date getPrevWorkingDay(int rel) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public Date getLastWorkingDayOfMonth() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public int howManyWorkingDaysBetween(Date start, Date end) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
