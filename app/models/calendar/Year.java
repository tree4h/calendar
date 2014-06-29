package models.calendar;

public enum Year {
	Y1989(1989, "1989"),
	Y2013(2013, "2013"),
	Y2014(2014, "2014"),
	Y2015(2015, "2015"),
	Y2016(2016, "2016"),
	Y2017(2017, "2017"),
	Y2018(2018, "2018"),
	Y2019(2019, "2019"),
	Y2020(2020, "2020");

	private int yearNumber;
	private String year西暦;
	private static final int 平成改元年 = 1989;
	private static final int 平成改元月 = 0;
	private static final int 平成改元日 = 8;

	Year(int yearNumber, String year西暦) {
		this.yearNumber = yearNumber;
		this.year西暦 = year西暦;
	}

	public int getNumber() {
		return this.yearNumber;
	}

	public String get平成元号名(Month month, Day day) {
		if(this.yearNumber == 平成改元年 && month.getNumber() == 平成改元月 && day.getNumber() < 平成改元日) {
			return "昭和64年";
		}
		else {
			int ret = this.yearNumber - 平成改元年 + 1;
			return "平成" + Integer.toString(ret) + "年";
		}
	}

	public String get英名() {
		return this.year西暦;
	}

	public static Year get年(int number) {
		String name = "Y" + Integer.toString(number);
		return Year.valueOf(name);
	}

}
