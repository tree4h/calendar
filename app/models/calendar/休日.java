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
public class 休日 extends Model {

	@Id
	public Long id;

	@NotNull
	@Column(length = 5120)
	public String ruleJSON;

	@OneToMany(cascade = CascadeType.ALL)
	public List<休日規則> ruleList = new ArrayList<休日規則>();

	public static Finder<Long, 休日> $find = new Finder<Long, 休日>(
			Long.class, 休日.class);

	public 休日(String ruleJSON) {
		this.ruleJSON = ruleJSON;
		this.setHolidayRule();
	}

	/*
	 * 休日規則を順番に適用していき、最後にマッチした
	 * 休日規則を返す
	 */
	public 休日規則 getHoliDayRule(月 targetMonth, 日 targetDay, 曜日 targetDow) {
		休日規則 ret = null;
		for(休日規則 rule :  ruleList) {
			if( rule.isMatch(targetMonth, targetDay, targetDow) ) {
				ret = rule;
			}
		}
		return ret;
	}

	public boolean isHoliday(月 targetMonth, 日 targetDay, 曜日 targetDow) {
		boolean ret = false;
		for(休日規則 rule :  ruleList) {
			if( rule.isMatch(targetMonth, targetDay, targetDow) ) {
				ret = rule.isHoliday();
			}
		}
		return ret;
	}

	private void setHolidayRule() {
		List<Map<String, String>> ruleMapList = (List<Map<String, String>>) JSON.decode(this.ruleJSON);
		for(Map<String, String> m : ruleMapList) {
			休日タイプ type = 休日タイプ.valueOf(m.get("type"));
			String name = m.get("name");
			List<月> monthList = this.makeMonthList(m.get("month"));
			List<日> dayList = this.makeDayList(m.get("day"));
			List<曜日> dowList = this.makeDowList(m.get("dow"));

			休日規則 rule = new 休日規則(type, name, monthList, dayList, dowList);
			ruleList.add(rule);
		}
	}

	private List<月> makeMonthList(String target) {
		List<月> ret = new ArrayList<月>();
		if( target == null || target.equals("*") ) {
			ret.add(月.M1);
			ret.add(月.M2);
			ret.add(月.M3);
			ret.add(月.M4);
			ret.add(月.M5);
			ret.add(月.M6);
			ret.add(月.M7);
			ret.add(月.M8);
			ret.add(月.M9);
			ret.add(月.M10);
			ret.add(月.M11);
			ret.add(月.M12);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(月.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(月.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String m1 = secondDivStrList[0];
					String m2 = secondDivStrList[1];
					int start = Integer.parseInt(m1.substring(1));
					int end = Integer.parseInt(m2.substring(1));
					for(int i=start; i<=end; i++) {
						String str = "M" + Integer.toString(i);
						if( ret.contains(月.valueOf(str)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + str);
						}
						else {
							ret.add(月.valueOf(str));
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

	private List<日> makeDayList(String target) {
		List<日> ret = new ArrayList<日>();
		if( target == null || target.equals("*") ) {
			ret.add(日.D1);
			ret.add(日.D2);
			ret.add(日.D3);
			ret.add(日.D4);
			ret.add(日.D5);
			ret.add(日.D6);
			ret.add(日.D7);
			ret.add(日.D8);
			ret.add(日.D9);
			ret.add(日.D10);
			ret.add(日.D11);
			ret.add(日.D12);
			ret.add(日.D13);
			ret.add(日.D14);
			ret.add(日.D15);
			ret.add(日.D16);
			ret.add(日.D17);
			ret.add(日.D18);
			ret.add(日.D19);
			ret.add(日.D20);
			ret.add(日.D21);
			ret.add(日.D22);
			ret.add(日.D23);
			ret.add(日.D24);
			ret.add(日.D25);
			ret.add(日.D26);
			ret.add(日.D27);
			ret.add(日.D28);
			ret.add(日.D29);
			ret.add(日.D30);
			ret.add(日.D31);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(日.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(日.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String d1 = secondDivStrList[0];
					String d2 = secondDivStrList[1];
					int start = Integer.parseInt(d1.substring(1));
					int end = Integer.parseInt(d2.substring(1));
					for(int i=start; i<=end; i++) {
						String str = "D" + Integer.toString(i);
						if( ret.contains(日.valueOf(str)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + str);
						}
						else {
							ret.add(日.valueOf(str));
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

	private List<曜日> makeDowList(String target) {
		List<曜日> ret = new ArrayList<曜日>();
		if( target == null || target.equals("*") ) {
			ret.add(曜日.日);
			ret.add(曜日.月);
			ret.add(曜日.火);
			ret.add(曜日.水);
			ret.add(曜日.木);
			ret.add(曜日.金);
			ret.add(曜日.土);
			return ret;
		}
		else {
			String[] firstDivStrList = target.split(",");
			for(String firstDivStr : firstDivStrList) {
				String[] secondDivStrList = firstDivStr.split("-");
				if(secondDivStrList.length == 1) {
					String secondDivStr = secondDivStrList[0];
					if( ret.contains(曜日.valueOf(secondDivStr)) ) {
						System.out.println("[構文エラー]:値が重複している -> " + secondDivStr);
					}
					else {
						ret.add(曜日.valueOf(secondDivStrList[0]));
					}
				}
				else if(secondDivStrList.length == 2) {
					String dow1 = secondDivStrList[0];
					String dow2 = secondDivStrList[1];
					int start = 曜日.valueOf(dow1).getNumber();
					int end = 曜日.valueOf(dow2).getNumber();

					for(int i=start; i<=end; i++) {
						if( ret.contains(曜日.get曜日(i)) ) {
							System.out.println("[構文エラー]:値が重複している -> " + 曜日.get曜日(i).toString() );
						}
						else {
							ret.add(曜日.get曜日(i));
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
