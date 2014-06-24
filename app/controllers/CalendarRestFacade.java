package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.calendar.CalendarDomainUtil;
import models.calendar.CalendarYear;
import models.calendar.Holiday;
import models.calendar.Month;
import models.calendar.Year;
import models.party.Owner;
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
		return ok(index.render(CalendarYear.$find.all()));
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

		Date ret = CalendarDomainUtil.getNextWorkingDate(ownerName, Integer.parseInt(rel));
		return ok(ret.toString());
	}

	public static Result createCalendar() {
		// POSTされたデータのバインド
		DynamicForm input = Form.form().bindFromRequest();
		Map<String, String> param = input.data();
		String ownerName = param.get("owner");
		String yearNumber = param.get("year");
		String holiday = param.get("holiday");
		String ownerRule = param.get("ownerRule");

//		System.out.println("checkbox=" + holiday);
//		System.out.println("ownerRule=" + ownerRule);

		Owner owner = Owner.$find.where().eq("name", ownerName).findUnique();
		if(owner == null) {
			owner = new Owner(ownerName);
			owner.save();
		}

		String json ="";
		if(holiday.equals("on")) {
			if(ownerRule.startsWith("{")) {
				json = "[" + 定休 + "," + 日本の祝日 +  ","  +  ownerRule + "]";
			}
			else {
				json = "[" + 定休 + "," + 日本の祝日 + "]";
			}
		}
		else {
			json = "[" + ownerRule + "]";
		}
		Holiday holiDay = new Holiday(json);
		holiDay.save();

		Year year = Year.get年(Integer.parseInt(yearNumber));
		CalendarYear calYear = new CalendarYear(owner, year);
		calYear.applyHoliDay(holiDay);
		calYear.saveCalendar();

		calYear.printCalendar();

		return redirect(controllers.routes.CalendarRestFacade.calendarList());
	}

	public static Result viewCalendar(Long calId, int mode) {
		CalendarYear target = CalendarYear.$find.byId(calId);
		MonthView m1 = new MonthView(target, Month.M1);
		MonthView m2 = new MonthView(target, Month.M2);
		MonthView m3 = new MonthView(target, Month.M3);
		MonthView m4 = new MonthView(target, Month.M4);
		MonthView m5 = new MonthView(target, Month.M5);
		MonthView m6 = new MonthView(target, Month.M6);
		MonthView m7 = new MonthView(target, Month.M7);
		MonthView m8 = new MonthView(target, Month.M8);
		MonthView m9 = new MonthView(target, Month.M9);
		MonthView m10 = new MonthView(target, Month.M10);
		MonthView m11 = new MonthView(target, Month.M11);
		MonthView m12 = new MonthView(target, Month.M12);
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
		return ok(yearcalendar.render(title, yearCalendar, mode));
	}

	//test
	//2014/1/1 - 2014/1/1 -> 1day
	private static final Date start1 = getDate(2014, 0, 1);
	private static final Date end1 = getDate(2014, 0, 1);
	//2014/1/1 - 2014/1/2 -> 2day
	private static final Date start2 = getDate(2014, 0, 1);
	private static final Date end2 = getDate(2014, 0, 2);
	//2014/1/1 - 2014/1/10 -> 10day
	private static final Date start3 = getDate(2014, 0, 1);
	private static final Date end3 = getDate(2014, 0, 10);
	//2014/1/1 - 2014/2/19 -> 50day
	private static final Date start4 = getDate(2014, 0, 1);
	private static final Date end4 = getDate(2014, 1, 19);
	//2014/1/1 - 2014/4/10 -> 100day
	private static final Date start5 = getDate(2014, 0, 1);
	private static final Date end5 = getDate(2014, 3, 10);
	//2014/1/1 - 2014/7/19 -> 200day
	private static final Date start6 = getDate(2014, 0, 1);
	private static final Date end6 = getDate(2014, 6, 19);
	//2014/1/1 - 2015/2/4 -> 400day
	private static final Date start7 = getDate(2014, 0, 1);
	private static final Date end7 = getDate(2015, 1, 4);

	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	public static Result initialiseCalendar() {
		//性能測定
		System.out.println( "============================================");
		int loopsize = 10000;
		//手法1
		long start = System.currentTimeMillis();
		int ret11 = CalendarDomainUtil.howManyDaysBetween(start1, end1);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyDaysBetween(start1, end1);
		}
		long end = System.currentTimeMillis();
		System.out.println("ret11 = "+ret11);
		System.out.println("ret11.time = " + (end-start) + "ms");

		start = System.currentTimeMillis();
		int ret17 = CalendarDomainUtil.howManyDaysBetween(start7, end7);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyDaysBetween(start7, end7);
		}
		end = System.currentTimeMillis();
		System.out.println("ret17 = "+ret17);
		System.out.println("ret17.time = " + (end-start) + "ms");

		//手法1-稼働日数
		Owner owner = Owner.$find.where().eq("name", "ISKEN").findUnique();
		start = System.currentTimeMillis();
		int retW11 = CalendarDomainUtil.howManyWorkingDaysBetween(owner, start1, end1);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyWorkingDaysBetween(owner, start1, end1);
		}
		end = System.currentTimeMillis();
		System.out.println("retW11 = "+retW11);
		System.out.println("retW11.time = " + (end-start) + "ms");

		start = System.currentTimeMillis();
		int retW17 = CalendarDomainUtil.howManyWorkingDaysBetween(owner, start7, end7);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyWorkingDaysBetween(owner, start7, end7);
		}
		end = System.currentTimeMillis();
		System.out.println("retW17 = "+retW17);
		System.out.println("retW17.time = " + (end-start) + "ms");

		//手法2
		start = System.currentTimeMillis();
		int ret21 = CalendarDomainUtil.howManyDaysBetween2(start1, end1);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyDaysBetween2(start1, end1);
		}
		end = System.currentTimeMillis();
		System.out.println("ret21 = "+ret21);
		System.out.println("ret21.time = " + (end-start) + "ms");

		start = System.currentTimeMillis();
		int ret27 = CalendarDomainUtil.howManyDaysBetween2(start7, end7);
		for(int i=1; i<loopsize; i++) {
			CalendarDomainUtil.howManyDaysBetween2(start7, end7);
		}
		end = System.currentTimeMillis();
		System.out.println("ret27 = "+ret27);
		System.out.println("ret27.time = " + (end-start) + "ms");

		//手法3
		start = System.currentTimeMillis();
		int ret31 = CalendarDomainUtil.howManyDaysBetween3(start1, end1);
		end = System.currentTimeMillis();
		System.out.println("ret31 = "+ret31);
		System.out.println("ret31.time = " + (end-start) + "ms");

		start = System.currentTimeMillis();
		int ret37 = CalendarDomainUtil.howManyDaysBetween3(start7, end7);
		end = System.currentTimeMillis();
		System.out.println("ret37 = "+ret37);
		System.out.println("ret37.time = " + (end-start) + "ms");

		//手法3-稼働日数
		start = System.currentTimeMillis();
		int retW31 = CalendarDomainUtil.howManyWorkingDaysBetween3(owner, start1, end1);
		end = System.currentTimeMillis();
		System.out.println("retW31 = "+retW31);
		System.out.println("retW31.time = " + (end-start) + "ms");

		start = System.currentTimeMillis();
		int retW37 = CalendarDomainUtil.howManyWorkingDaysBetween3(owner, start7, end7);
		end = System.currentTimeMillis();
		System.out.println("retW37 = "+retW37);
		System.out.println("retW37.time = " + (end-start) + "ms");

		return redirect(controllers.routes.CalendarRestFacade.calendarList());
	}

}
