package models.calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import global.Global;

import java.util.Calendar;
import java.util.Date;

import apps.FakeApp;

import models.party.Owner;

import org.junit.Test;
import static play.libs.F.*;
import static org.fest.assertions.Assertions.assertThat;

public class CalendarDomainUtilTest extends FakeApp {
//public class CalendarDomainUtilTest {

	//2014/1/1 - 2014/1/1 -> 1day
	private static final Date start1 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end1 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	//2014/1/1 - 2014/1/2 -> 2day
	private static final Date start2 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end2 = CalendarDomainUtilTest.getDate(2014, 0, 2);
	//2014/1/1 - 2014/1/10 -> 10day
	private static final Date start3 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end3 = CalendarDomainUtilTest.getDate(2014, 0, 10);
	//2014/1/1 - 2014/2/19 -> 50day
	private static final Date start4 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end4 = CalendarDomainUtilTest.getDate(2014, 1, 19);
	//2014/1/1 - 2014/4/10 -> 100day
	private static final Date start5 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end5 = CalendarDomainUtilTest.getDate(2014, 3, 10);
	//2014/1/1 - 2014/7/19 -> 200day
	private static final Date start6 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end6 = CalendarDomainUtilTest.getDate(2014, 6, 19);
	//2014/1/1 - 2015/2/4 -> 400day
	private static final Date start7 = CalendarDomainUtilTest.getDate(2014, 0, 1);
	private static final Date end7 = CalendarDomainUtilTest.getDate(2015, 1, 4);

	private static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	@Test
	public void testHowManyDaysBetween() {
//		Owner owner = new Owner("common");
//		owner.save();
//		Global.make日本の祝日カレンダ(owner, Year.Y14);
//		Global.make日本の祝日カレンダ(owner, Year.Y15);

		int ret = CalendarDomainUtil.howManyDaysBetween(start1, end1);
		assertThat(ret, is(1));
	}

	@Test
	public void testHowManyDaysBetween2() {
		int ret1 = CalendarDomainUtil.howManyDaysBetween2(start1, end1);
		assertThat(ret1, is(1));
		int ret2 = CalendarDomainUtil.howManyDaysBetween2(start2, end2);
		assertThat(ret2, is(2));
		int ret3 = CalendarDomainUtil.howManyDaysBetween2(start3, end3);
		assertThat(ret3, is(10));
		int ret4 = CalendarDomainUtil.howManyDaysBetween2(start4, end4);
		assertThat(ret4, is(50));
		int ret5 = CalendarDomainUtil.howManyDaysBetween2(start5, end5);
		assertThat(ret5, is(100));
		int ret6 = CalendarDomainUtil.howManyDaysBetween2(start6, end6);
		assertThat(ret6, is(200));
		int ret7 = CalendarDomainUtil.howManyDaysBetween2(start7, end7);
		assertThat(ret7, is(400));
	}

}
