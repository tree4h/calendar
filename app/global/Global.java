package global;

import java.io.IOException;

import models.calendar.CalendarYear;
import models.calendar.Holiday;
import models.calendar.Year;
import models.party.Owner;

import org.apache.commons.io.FileUtils;

import com.avaje.ebean.Ebean;

import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		//super.onStart(app);
		//PostgreSQLを使うと、dropDdlがうまく動作せず、createDdlでエラーとなってしまう
		createDb(app);
		this.InitialDataInsert();
	}

	private void createDb(Application app) {
		// Reading the evolution file
		String evolutionContent = null;
		try {
			evolutionContent = FileUtils.readFileToString(
					app.getWrappedApplication().getFile("conf/evolutions/default/1.sql"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Splitting the String to get Create & Drop DDL
		String[] splittedEvolutionContent = evolutionContent
				.split("# --- !Ups");
		String[] upsDowns = splittedEvolutionContent[1].split("# --- !Downs");
		String createDdl = upsDowns[0];
		String dropDdl = upsDowns[1];

		// Execute
		Ebean.execute(Ebean.createCallableSql(dropDdl));
		Ebean.execute(Ebean.createCallableSql(createDdl));
	}

	private static final String 日本の祝日 = "{\"type\":\"祝日\", \"name\":\"元旦\", \"month\":\"M1\", \"day\":\"D1\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"成人の日\", \"month\":\"M1\", \"day\":\"D8-D14\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"建国記念の日\", \"month\":\"M2\", \"day\":\"D11\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"春分の日\", \"month\":\"M3\", \"day\":\"D21\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"昭和の日\", \"month\":\"M4\", \"day\":\"D29\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"憲法記念日\", \"month\":\"M5\", \"day\":\"D3\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"みどりの日\", \"month\":\"M5\", \"day\":\"D4\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"こどもの日\", \"month\":\"M5\", \"day\":\"D5\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"海の日\", \"month\":\"M7\", \"day\":\"D15-D21\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"敬老の日\", \"month\":\"M9\", \"day\":\"D15-D21\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"秋分の日\", \"month\":\"M9\", \"day\":\"D23\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"体育の日\", \"month\":\"M10\", \"day\":\"D8-D14\", \"dow\":\"月\"},{\"type\":\"祝日\", \"name\":\"文化の日\", \"month\":\"M11\", \"day\":\"D3\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"勤労感謝の日\", \"month\":\"M11\", \"day\":\"D23\", \"dow\":\"*\"},{\"type\":\"祝日\", \"name\":\"天皇誕生日\", \"month\":\"M12\", \"day\":\"D23\", \"dow\":\"*\"}";
	private static final String 定休 = "{\"type\":\"不稼働\", \"name\":\"定休\", \"month\":\"*\", \"day\":\"*\", \"dow\":\"日,土\"}";
	private static final String ISKEN = "{\"type\":\"不稼働\", \"name\":\"三が日\", \"month\":\"M1\", \"day\":\"D1-D3\", \"dow\":\"*\"},{\"type\":\"不稼働\", \"name\":\"年末\", \"month\":\"M12\", \"day\":\"D29-D31\", \"dow\":\"*\"},{\"type\":\"不稼働\", \"name\":\"定例会\", \"month\":\"*\", \"day\":\"D22-D28\", \"dow\":\"金\"},{\"type\":\"不稼働\", \"name\":\"創立記念日\", \"month\":\"M11\", \"day\":\"D1\", \"dow\":\"*\"},{\"type\":\"休出\", \"name\":\"成人の日\", \"month\":\"M1\", \"day\":\"D8-D14\", \"dow\":\"月\"}";

	private void InitialDataInsert() {
		Owner japan1 = new Owner("日本の祝日");
		japan1.save();
		Owner japan2 = new Owner("日本の休日");
		japan2.save();
		Owner isken = new Owner("ISKEN");
		isken.save();

		make日本の祝日カレンダ(japan1, Year.Y14);
		make日本のカレンダ(japan2, Year.Y14);

		makeISKENカレンダ(isken, Year.Y14);
		makeISKENカレンダ(isken, Year.Y15);
	}

	public static void make日本の祝日カレンダ(Owner owner, Year year) {
		String json = "[" + 日本の祝日 + "]";
		Holiday holiDay = new Holiday(json);
		holiDay.save();

		CalendarYear calYear = new CalendarYear(owner, year);
		calYear.applyHoliDay(holiDay);
		calYear.saveCalendar();

//		calYear.printCalendar();
	}

	public static void make日本のカレンダ(Owner owner, Year year) {
		String json = "[" + 定休 + "," + 日本の祝日 + "]";
//		String json = "[" + 日本の祝日 + "," + 定休 + "]";
		Holiday holiDay = new Holiday(json);
		holiDay.save();

		CalendarYear calYear = new CalendarYear(owner, year);
		calYear.applyHoliDay(holiDay);
		calYear.saveCalendar();
	}

	public static void makeISKENカレンダ(Owner owner, Year year) {
		String json = "[" + 定休 + "," + 日本の祝日 +  ","  +  ISKEN + "]";
		Holiday holiDay = new Holiday(json);
		holiDay.save();

		CalendarYear calYear = new CalendarYear(owner, year);
		calYear.applyHoliDay(holiDay);
		calYear.saveCalendar();

		calYear.printCalendar();
	}

}
