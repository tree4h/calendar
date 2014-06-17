package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.calendar.カレンダDomainUtil;
import models.calendar.カレンダ年;
import models.calendar.休日;
import models.calendar.月;
import models.party.所有者;
import models.view.MonthView;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.calendar.index;
import views.html.calendar.monthcalendar;
import views.html.calendar.yearcalendar;
import views.html.calendar.inputform;
import views.html.calapi.apiindex;
import views.html.calapi.getNextWorkingDate;

public class CalendarRestFacade extends Controller {

	private static final String 日本の祝日 = "{\"type\":\"祝日\", \"name\":\"元旦\", \"month\":\"M1\", \"day\":\"D1\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"成人の日\", \"month\":\"M1\", \"day\":\"D8-D14\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"建国記念の日\", \"month\":\"M2\", \"day\":\"D11\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"春分の日\", \"month\":\"M3\", \"day\":\"D21\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"昭和の日\", \"month\":\"M4\", \"day\":\"D29\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"憲法記念日\", \"month\":\"M5\", \"day\":\"D3\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"みどりの日\", \"month\":\"M5\", \"day\":\"D4\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"こどもの日\", \"month\":\"M5\", \"day\":\"D5\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"海の日\", \"month\":\"M7\", \"day\":\"D15-D21\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"敬老の日\", \"month\":\"M9\", \"day\":\"D15-D21\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"秋分の日\", \"month\":\"M9\", \"day\":\"D23\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"体育の日\", \"month\":\"M10\", \"day\":\"D8-D14\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"文化の日\", \"month\":\"M11\", \"day\":\"D3\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"勤労感謝の日\", \"month\":\"M11\", \"day\":\"D23\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"天皇誕生日\", \"month\":\"M12\", \"day\":\"D23\", \"dow\":\"*\"}";
	private static final String 定休 = "{\"type\":\"不稼働\", \"name\":\"定休\", \"month\":\"*\", \"day\":\"*\", \"dow\":\"日,土\"}";

	public static Result calendarList() {
		return ok(index.render(カレンダ年.$find.all()));
	}

	public static Result viewInputForm() {
		return ok(inputform.render());
	}

	public static Result calendarAPIList() {
		return ok(apiindex.render());
	}

	public static Result viewAPI1InputForm() {
		return ok(getNextWorkingDate.render());
	}

	public static Result getNextWorkingDate() {
		// GETされたデータのバインド
		DynamicForm input = Form.form().bindFromRequest();
		Map<String, String> param = input.data();
		String ownerName = param.get("ownerName");
		String rel = param.get("rel");

		所有者 owner = 所有者.$find.where().eq("name", ownerName).findUnique();
		Date ret = カレンダDomainUtil.getNextWorkingDate(owner, Integer.parseInt(rel));
		return ok(ret.toString());
	}

	public static Result createCalendar() {
		// POSTされたデータのバインド
		DynamicForm input = Form.form().bindFromRequest();
		Map<String, String> param = input.data();
		String ownerName = param.get("owner");
		String year = param.get("year");
		String holiday = param.get("holiday");
		String ownerRule = param.get("ownerRule");

		System.out.println("checkbox=" + holiday);
		System.out.println("ownerRule=" + ownerRule);

		所有者 owner = new 所有者(ownerName);
		owner.save();

		String json ="";
		if(holiday.equals("on")) {
			if(ownerRule.startsWith("{")) {
				json = "[" + 定休 + "," + 日本の祝日 +  ","  +  ownerRule + "]";
				System.out.println("in2");
			}
			else {
				json = "[" + 定休 + "," + 日本の祝日 + "]";
				System.out.println("in1");
			}
		}
		else {
			json = "[" + ownerRule + "]";
			System.out.println("in3");
		}
		休日 holiDay = new 休日(json);
		holiDay.save();

		カレンダ年 cal = new カレンダ年(owner, Integer.parseInt(year));
		cal.applyHoliDay(holiDay);
		//TODO 特定日ができたら特定日の適用も必要となってくる
		cal.save();
//		cal.printCalendar();

		return redirect(controllers.routes.CalendarRestFacade.calendarList());
	}

	public static Result viewCalendar(Long calId) {
		カレンダ年 target = カレンダ年.$find.byId(calId);
		MonthView m1 = new MonthView(target, 月.M1);
		MonthView m2 = new MonthView(target, 月.M2);
		MonthView m3 = new MonthView(target, 月.M3);
		MonthView m4 = new MonthView(target, 月.M4);
		MonthView m5 = new MonthView(target, 月.M5);
		MonthView m6 = new MonthView(target, 月.M6);
		MonthView m7 = new MonthView(target, 月.M7);
		MonthView m8 = new MonthView(target, 月.M8);
		MonthView m9 = new MonthView(target, 月.M9);
		MonthView m10 = new MonthView(target, 月.M10);
		MonthView m11 = new MonthView(target, 月.M11);
		MonthView m12 = new MonthView(target, 月.M12);
		List<MonthView> yearCalendar = new ArrayList<MonthView>();
		yearCalendar.add(m1);
		yearCalendar.add(m2);
		yearCalendar.add(m3);
		yearCalendar.add(m4);
		yearCalendar.add(m5);
		yearCalendar.add(m6);
		yearCalendar.add(m7);
		yearCalendar.add(m8);
		yearCalendar.add(m9);
		yearCalendar.add(m10);
		yearCalendar.add(m11);
		yearCalendar.add(m12);
		String title = target.owner.name + "_" + target.year;
		return ok(yearcalendar.render(title, yearCalendar));
	}

}
