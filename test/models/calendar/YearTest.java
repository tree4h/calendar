package models.calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class YearTest {

	@Test
	public void testGet平成元号名() {
		Year s64 = Year.Y1989;
		Year h25 = Year.Y2013;
		Month m1 = Month.M1;
		Month m2 = Month.M2;
		Day d1 = Day.D1;
		Day d7 = Day.D7;
		Day d8 = Day.D8;


		String ret = s64.get平成元号名(m1, d1);
		assertThat(ret, is("昭和64年"));

		ret = s64.get平成元号名(m1, d7);
		assertThat(ret, is("昭和64年"));

		ret = s64.get平成元号名(m1, d8);
		assertThat(ret, is("平成1年"));

		ret = s64.get平成元号名(m2, d8);
		assertThat(ret, is("平成1年"));

		ret = h25.get平成元号名(m1, d1);
		assertThat(ret, is("平成25年"));

		ret = h25.get平成元号名(m2, d8);
		assertThat(ret, is("平成25年"));

	}

}
