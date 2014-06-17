package models.calendar;

public enum 月 {
	M1(0, "睦月", "JANUARY"),
	M2(1, "如月", "FEBRUARY"),
	M3(2, "弥生", "MARCH"),
	M4(3, "卯月", "APRIL"),
	M5(4, "皐月", "MAY"),
	M6(5, "水無月", "JUNE"),
	M7(6, "文月", "JULY"),
	M8(7, "葉月", "AUGUST"),
	M9(8, "長月", "SEPTEMBER"),
	M10(9, "神無月", "OCTOBER"),
	M11(10, "霜月", "NOVEMBER"),
	M12(11, "師走", "DECEMBER");

	private int monthNumber;
	private String month和名;
	private String month英名;

	月(int monthNumber, String month和名, String month英名) {
		this.monthNumber = monthNumber;
		this.month和名 = month和名;
		this.month英名 = month英名;
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

	public static 月 get月(int number) {
		switch (number) {
			case 0 : return 月.M1;
			case 1 : return 月.M2;
			case 2 : return 月.M3;
			case 3 : return 月.M4;
			case 4 : return 月.M5;
			case 5 : return 月.M6;
			case 6 : return 月.M7;
			case 7 : return 月.M8;
			case 8 : return 月.M9;
			case 9 : return 月.M10;
			case 10 : return 月.M11;
			case 11 : return 月.M12;
		}
		return null;
	}


}
