package models.calendar;

public enum Day {
	D1(1, "1st"),
	D2(2, "2nd"),
	D3(3, "3rd"),
	D4(4, "4th"),
	D5(5, "5th"),
	D6(6, "6th"),
	D7(7, "7th"),
	D8(8, "8th"),
	D9(9, "9th"),
	D10(10, "10th"),
	D11(11, "11st"),
	D12(12, "12nd"),
	D13(13, "13rd"),
	D14(14, "14th"),
	D15(15, "15th"),
	D16(16, "16th"),
	D17(17, "17th"),
	D18(18, "18th"),
	D19(19, "19th"),
	D20(20, "20th"),
	D21(21, "21st"),
	D22(22, "22nd"),
	D23(23, "23rd"),
	D24(24, "24th"),
	D25(25, "25th"),
	D26(26, "26th"),
	D27(27, "27th"),
	D28(28, "28th"),
	D29(29, "29th"),
	D30(30, "30th"),
	D31(31, "31th");

	private int dayNumber;
	private String day英名;

	Day(int dayNumber, String day英名) {
		this.dayNumber = dayNumber;
		this.day英名 = day英名;
	}

	public int getNumber() {
		return this.dayNumber;
	}

	public String get英名() {
		return this.day英名;
	}

	public static Day get日(int number) {
		switch (number) {
			case 1 : return Day.D1;
			case 2 : return Day.D2;
			case 3 : return Day.D3;
			case 4 : return Day.D4;
			case 5 : return Day.D5;
			case 6 : return Day.D6;
			case 7 : return Day.D7;
			case 8 : return Day.D8;
			case 9 : return Day.D9;
			case 10 : return Day.D10;
			case 11 : return Day.D11;
			case 12: return Day.D12;
			case 13 : return Day.D13;
			case 14 : return Day.D14;
			case 15 : return Day.D15;
			case 16 : return Day.D16;
			case 17 : return Day.D17;
			case 18 : return Day.D18;
			case 19 : return Day.D19;
			case 20 : return Day.D20;
			case 21 : return Day.D21;
			case 22 : return Day.D22;
			case 23 : return Day.D23;
			case 24 : return Day.D24;
			case 25 : return Day.D25;
			case 26 : return Day.D26;
			case 27 : return Day.D27;
			case 28 : return Day.D28;
			case 29 : return Day.D29;
			case 30 : return Day.D30;
			case 31 : return Day.D31;
		}
		return null;
	}

}
