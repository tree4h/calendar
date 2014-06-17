package models.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import net.arnx.jsonic.JSON;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Holiday extends Model {

	@Id
	public Long id;

	@NotNull
	@Column(length = 5120)
	public String ruleJSON;

	@OneToMany(cascade = CascadeType.ALL)
	public List<HolidayRule> ruleList = new ArrayList<HolidayRule>();

	public static Finder<Long, Holiday> $find = new Finder<Long, Holiday>(
			Long.class, Holiday.class);

	public Holiday(String ruleJSON) {
		this.ruleJSON = ruleJSON;
		this.setHolidayRule();
	}

	/*
	 * 休日規則を順番に適用していき、最後にマッチした
	 * 休日規則を返す
	 */
	public HolidayRule getHoliDayRule(Month targetMonth, Day targetDay, DoW targetDow) {
		HolidayRule ret = null;
		for(HolidayRule rule :  ruleList) {
			if( rule.isMatch(targetMonth, targetDay, targetDow) ) {
				ret = rule;
			}
		}
		return ret;
	}

	public boolean isHoliday(Month targetMonth, Day targetDay, DoW targetDow) {
		boolean ret = false;
		for(HolidayRule rule :  ruleList) {
			if( rule.isMatch(targetMonth, targetDay, targetDow) ) {
				ret = rule.isHoliday();
			}
		}
		return ret;
	}

	private void setHolidayRule() {
		List<Map<String, String>> ruleMapList = (List<Map<String, String>>) JSON.decode(this.ruleJSON);
		for(Map<String, String> m : ruleMapList) {
			HolidayType type = HolidayType.valueOf(m.get("type"));
			String name = m.get("name");
			List<Month> monthList = this.makeMonthList(m.get("month"));
			List<Day> dayList = this.makeDayList(m.get("day"));
			List<DoW> dowList = this.makeDowList(m.get("dow"));

			HolidayRule rule = new HolidayRule(type, name, monthList, dayList, dowList);
			ruleList.add(rule);
		}
	}

	private List<Month> makeMonthList(String target) {
		List<Month> ret = new ArrayList<Month>();
		if( target == null || target.equals("*") ) {
			ret.add(Month.M1);
			ret.add(Month.M2);
			ret.add(Month.M3);
			ret.add(Month.M4);
			ret.add(Month.M5);
			ret.add(Month.M6);
			ret.add(Month.M7);
			ret.add(Month.M8);
			ret.add(Month.M9);
			ret.add(Month.M10);
			ret.add(Month.M11);
			ret.add(Month.M12);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(Month.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(Month.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String m1 = secondDivStrList[0];
					String m2 = secondDivStrList[1];
					int start = Integer.parseInt(m1.substring(1));
					int end = Integer.parseInt(m2.substring(1));
					for(int i=start; i<=end; i++) {
						String str = "M" + Integer.toString(i);
						if( ret.contains(Month.valueOf(str)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + str);
						}
						else {
							ret.add(Month.valueOf(str));
						}
					}
				}
				else {
					System.out.println("[構文エラー]: -> " + firstDivStr);
				}
			}
		}
		return ret;
	}

	private List<Day> makeDayList(String target) {
		List<Day> ret = new ArrayList<Day>();
		if( target == null || target.equals("*") ) {
			ret.add(Day.D1);
			ret.add(Day.D2);
			ret.add(Day.D3);
			ret.add(Day.D4);
			ret.add(Day.D5);
			ret.add(Day.D6);
			ret.add(Day.D7);
			ret.add(Day.D8);
			ret.add(Day.D9);
			ret.add(Day.D10);
			ret.add(Day.D11);
			ret.add(Day.D12);
			ret.add(Day.D13);
			ret.add(Day.D14);
			ret.add(Day.D15);
			ret.add(Day.D16);
			ret.add(Day.D17);
			ret.add(Day.D18);
			ret.add(Day.D19);
			ret.add(Day.D20);
			ret.add(Day.D21);
			ret.add(Day.D22);
			ret.add(Day.D23);
			ret.add(Day.D24);
			ret.add(Day.D25);
			ret.add(Day.D26);
			ret.add(Day.D27);
			ret.add(Day.D28);
			ret.add(Day.D29);
			ret.add(Day.D30);
			ret.add(Day.D31);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(Day.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(Day.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String d1 = secondDivStrList[0];
					String d2 = secondDivStrList[1];
					int start = Integer.parseInt(d1.substring(1));
					int end = Integer.parseInt(d2.substring(1));
					for(int i=start; i<=end; i++) {
						String str = "D" + Integer.toString(i);
						if( ret.contains(Day.valueOf(str)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + str);
						}
						else {
							ret.add(Day.valueOf(str));
						}
					}
				}
				else {
					System.out.println("[構文エラー]: -> " + firstDivStr);
				}
			}
		}
		return ret;
	}

	private List<DoW> makeDowList(String target) {
		List<DoW> ret = new ArrayList<DoW>();
		if( target == null || target.equals("*") ) {
			ret.add(DoW.日);
			ret.add(DoW.月);
			ret.add(DoW.火);
			ret.add(DoW.水);
			ret.add(DoW.木);
			ret.add(DoW.金);
			ret.add(DoW.土);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(DoW.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(DoW.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String dow1 = secondDivStrList[0];
					String dow2 = secondDivStrList[1];
					int start = DoW.valueOf(dow1).getNumber();
					int end = DoW.valueOf(dow2).getNumber();

					for(int i=start; i<=end; i++) {
						if( ret.contains(DoW.get曜日(i)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + DoW.get曜日(i).toString() );
						}
						else {
							ret.add(DoW.get曜日(i));
						}
					}
				}
				else {
					System.out.println("[構文エラー]: -> " + firstDivStr);
				}
			}
		}
		return ret;
	}
}
