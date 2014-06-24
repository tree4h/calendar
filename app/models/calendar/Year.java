package models.calendar;

public enum Year {
	Y13(2013, "2013", "平成25年"),
	Y14(2014, "2014", "平成26年"),
	Y15(2015, "2015", "平成27年"),
	Y16(2016, "2016", "平成28年"),
	Y17(2017, "2017", "平成29年"),
	Y18(2018, "2018", "平成30年"),
	Y19(2019, "2019", "平成31年"),
	Y20(2020, "2020", "平成32年");

	private int yearNumber;
	private String year西暦;
	private String year和暦;

	Year(int yearNumber, String year西暦, String year和暦) {
		this.yearNumber = yearNumber;
		this.year西暦 = year西暦;
		this.year和暦 = year和暦;
	}

	public int getNumber() {
		return this.yearNumber;
	}

	public String get和名() {
		return this.year西暦;
	}

	public String get英名() {
		return this.year和暦;
	}

	public static Year get年(int number) {
		switch (number) {
			case 2013 : return Year.Y13;
			case 2014 : return Year.Y14;
			case 2015 : return Year.Y15;
			case 2016 : return Year.Y16;
			case 2017 : return Year.Y17;
			case 2018 : return Year.Y18;
			case 2019 : return Year.Y19;
			case 2020 : return Year.Y20;
		}
		return null;
	}

}
