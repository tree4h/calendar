package models.calendar;

public enum Month {
	M1(0, "睦月", "JANUARY", "1月"),
	M2(1, "如月", "FEBRUARY", "2月"),
	M3(2, "弥生", "MARCH", "3月"),
	M4(3, "卯月", "APRIL", "4月"),
	M5(4, "皐月", "MAY", "5月"),
	M6(5, "水無月", "JUNE", "6月"),
	M7(6, "文月", "JULY", "7月"),
	M8(7, "葉月", "AUGUST", "8月"),
	M9(8, "長月", "SEPTEMBER", "9月"),
	M10(9, "神無月", "OCTOBER", "10月"),
	M11(10, "霜月", "NOVEMBER", "11月"),
	M12(11, "師走", "DECEMBER", "12月");

	private int monthNumber;
	private String month和名;
	private String month英名;
	private String month数名;

	Month(int monthNumber, String month和名, String month英名, String month数名) {
		this.monthNumber = monthNumber;
		this.month和名 = month和名;
		this.month英名 = month英名;
		this.month数名 = month数名;
	}

	public int getNumber() {
		return this.monthNumber;
	}

	public String get和名() {
		return this.month和名;
	}

	public String get英名() {
		return this.month英名;
	}

	public String get数名() {
		return this.month数名;
	}

	public static Month get月(int number) {
		String name = "M" + Integer.toString(number+1);
		return Month.valueOf(name);
	}


}
