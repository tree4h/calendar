package models.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;
import models.party.Owner;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "owner_id", "year" }) })
public class CalendarYear extends Model {
	@Id
	public Long id;

	@NotNull
	public Year year;

	@NotNull
	@ManyToOne
	public Owner owner;

	@NotNull
	public int workingDays;

	public List<CalendarDay> dayList = new ArrayList<CalendarDay>();

	public static Finder<Long, CalendarYear> $find = new Finder<Long, CalendarYear>(
			Long.class, CalendarYear.class);

	public CalendarYear(Owner owner, Year year) {
		this.owner = owner;
		this.year = year;
		this.makeClalendar();
	}

	/*
	 * カレンダ日の初期化
	 */
	public void makeClalendar() {
		CalendarYear targetYear = CalendarYear.$find.where().eq("owner", this.owner).eq("year", Year.get年(this.year.getNumber()-1)).findUnique();
		if(targetYear == null) {
			this.workingDays = 0;
		}
		else {
			this.workingDays = targetYear.workingDays;
		}

		Calendar cal = Calendar.getInstance();
		int index = 0;
		for(int i=0; i<=11; i++) {
			cal.clear();
			cal.set(year.getNumber(), i, 1);
			int startDoW = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			int lastDay = cal.get(Calendar.DATE);

//			System.out.println("Month" + (i+1));
//			System.out.println("DoW" + startDoW);
//			System.out.println("lastDay" + lastDay);

			for(int j=1; j<=lastDay; j++) {
				int k = startDoW + j - 1;
				if(k > 7) {
					k = k % 7;
					if(k == 0) {
						k = 7;
					}
				}
				CalendarDay calDay = new CalendarDay(owner, year, Month.get月(i), Day.get日(j), DoW.get曜日(k));
				dayList.add(calDay);

				//nextCalDayの設定
				if( index == 0 ) {
					//前年の12/31に設定する
					CalendarDay targetDay = CalendarDay.$find.where().eq("owner", this.owner).eq("year", Year.get年(this.year.getNumber()-1))
							.eq("month", Month.get月(11)).eq("day", Day.get日(31)).findUnique();
					if(targetDay != null) {
						targetDay.setNextCalendarDay(calDay);
						targetDay.save();
					}
				}
				else {
					CalendarDay targetDay = dayList.get(index-1);
					targetDay.setNextCalendarDay(calDay);
				}
				index++;
			}
		}
	}

	public void applyHoliDay(Holiday holiDay) {
		for(CalendarDay calDay : dayList) {
			calDay.initialize(holiDay);
		}
		this.applyJapaneseHolidayRule3(holiDay);
	}

	public void saveCalendar() {
		for(CalendarDay calDay : dayList) {
			if(calDay.isWorkingDay) {
				this.workingDays++;
			}
			calDay.setPassedWorkingDay(workingDays);
			calDay.save();
		}
		this.save();
	}

	public void printCalendar() {
		System.out.println( "■ " + this.year.getNumber() + "year, owner : " + this.owner.name);
		for(CalendarDay day : dayList) {
			System.out.println((day.month.getNumber()+1) + "month, " + day.day.getNumber() + "day, " + day.dow.get英名() + ", " + day._week + "week");
			System.out.println("passedDays:" + day._passedDay + ", passedWorkingDays:" + day._passedWorkingDay);
		}
		System.out.println( "============================================");
	}

	//TODO 未実装
//	public void applySpecialDay(SpecialHoliday holiDay) {
//		return dayList;
//	}

	/*
	 * 『国民の祝日に関する法律（昭和２３年法律第１７８号）第3条』の適用
	 *「国民の祝日」は、休日とする。
	 *2.「国民の祝日」が日曜日に当たるときは、その日後においてその日に最も近い「国民の祝日」でない日を休日とする。
	 *3.その前日及び翌日が「国民の祝日」である日（「国民の祝日」でない日に限る。）は、休日とする。
	 *年を跨いで、本法律が成立する状況はないことを前提とする
	 *例）12月31日が祝日で日曜日になるパターンはないことが前提
	 *例）12月3０，３１日が祝日で、30日が日曜日になるパターンはないことが前提
	 */
	private void applyJapaneseHolidayRule3(Holiday holiDay) {
		//日本の祝日ルールの適用
		for(int index=0; index<dayList.size()-2; index++) {
			CalendarDay calDay = dayList.get(index);
			if( calDay.type.equals(HolidayType.祝日) ) {
				int tempIndex = index+1;
				//2.「国民の祝日」が日曜日に当たるときは、その日後においてその日に最も近い「国民の祝日」でない日を休日とする。
				if(calDay.dow.equals(DoW.日)) {
					while(tempIndex > index) {
						CalendarDay targetDay = dayList.get(tempIndex);
						if( !(targetDay.type.equals(HolidayType.祝日)) ) {
							//TODO setter作る？
							targetDay.type = HolidayType.休日;
							targetDay.name = "振替休日";
							tempIndex = -1;
						}
						tempIndex++;
					}
				}

				//3.その前日及び翌日が「国民の祝日」である日（「国民の祝日」でない日に限る。）は、休日とする。
				CalendarDay tomorrow = dayList.get(index+1);
				CalendarDay dayAfterTomorrow = dayList.get(index+2);
				if( dayAfterTomorrow.type.equals(HolidayType.祝日) && !(tomorrow.type.equals(HolidayType.祝日)) ) {
					tomorrow.type = HolidayType.休日;
					tomorrow.name = "国民の休日";
				}
			}
		}
	}
}
