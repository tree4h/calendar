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

import models.party.所有者;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "year", "owner_id" }) })
public class カレンダ年 extends Model {
	@Id
	public Long id;

	@NotNull
	public int year;

	@NotNull
	@ManyToOne
	public 所有者 owner;

	//派生属性（初期化時に設定）
	public int _稼働日数;
	public int _休日数;

	@ManyToOne
	public 休日 holiDay;

	@OneToMany(cascade = CascadeType.ALL)
	public List<カレンダ日> dayList = new ArrayList<カレンダ日>();
	@OneToMany(cascade = CascadeType.ALL)
	public List<カレンダ日インデックス> dayIndexList = new ArrayList<カレンダ日インデックス>();

	public static Finder<Long, カレンダ年> $find = new Finder<Long, カレンダ年>(
			Long.class, カレンダ年.class);

	public カレンダ年(所有者 owner, int year) {
		this.owner = owner;
		this.year = year;
	}

	public void applyHoliDay(休日 holiDay) {
		this.holiDay = holiDay;
		this.makeカレンダ日(holiDay);
		this.applyJapaneseHolidayRule3();
		this._稼働日数 = this.get稼働日数();
		this._休日数 = this.get休日数();
	}

	public void printCalendar() {
		for(カレンダ日 day : dayList) {
			System.out.println((day.month.getNumber()+1) + "month, " + day.day.getNumber() + "day, " + day.dow.get英名() + ", " + day._week + "week");
		}
	}

	/*
	 * カレンダ日の初期化
	 * 休日がないカレンダーはあり？
	 */
	private void makeカレンダ日(休日 holiDay) {
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
				カレンダ日 calDay = new カレンダ日(this, 月.get月(i), 日.get日(j), 曜日.get曜日(k));
				calDay.initialize(holiDay);
				dayList.add(calDay);

				//カレンダ日インデックス作成
				カレンダ日インデックス calIndex = new カレンダ日インデックス(this, i, j, index);
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
		for(カレンダ日 calDay : dayList) {
			if( calDay.type.equals(休日タイプ.祝日) ) {
				if(calDay.dow.equals(曜日.日)) {
					月 month = calDay.month;
					日 day = calDay.day;
					int currentIndex = dayList.indexOf(calDay);
					int index = currentIndex+1;
					while(index > currentIndex) {
						カレンダ日 targetDay = dayList.get(index);
						if( !(targetDay.type.equals(休日タイプ.祝日)) ) {
							//TODO setter作る？
							targetDay.type = 休日タイプ.休日;
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
		for(カレンダ日 day : dayList) {
			if( day.isWorkingDay() ) {
				counter++;
			}
		}
		return counter;
	}

	private int get休日数() {
		int counter = 0;
		for(カレンダ日 day : dayList) {
			if( day.isHoliday() ) {
				counter++;
			}
		}
		return counter;
	}

	public Date getNextWorkingDay(int month, int day) {
		カレンダ日インデックス calIndex = カレンダ日インデックス.$find.where().eq("year", this).eq("month", month).eq("day", day).findUnique();
		int startIndex = calIndex.index;

		//TODO kesu
		System.out.println("startIndex" + startIndex);

		dayList = カレンダ日.$find.where().eq("year", this).findList();

		//this.refresh();
		//System.out.println("size" + dayList.size());
		//TODO dayList.size()が機能しない。Playの問題？ 365を暫定的
		for(int index=startIndex; index<=dayList.size(); index++ ) {
			カレンダ日 targetDay = dayList.get(index);

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
