package models.calendar;

public enum 日 {
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

	日(int dayNumber, String day英名) {
		this.dayNumber = dayNumber;
		this.day英名 = day英名;
	}

	public int getNumber() {
		return this.dayNumber;
	}

	public String get英名() {
		return this.day英名;
	}

	public static 日 get日(int number) {
		switch (number) {
			case 1 : return 日.D1;
			case 2 : return 日.D2;
			case 3 : return 日.D3;
			case 4 : return 日.D4;
			case 5 : return 日.D5;
			case 6 : return 日.D6;
			case 7 : return 日.D7;
			case 8 : return 日.D8;
			case 9 : return 日.D9;
			case 10 : return 日.D10;
			case 11 : return 日.D11;
			case 12: return 日.D12;
			case 13 : return 日.D13;
			case 14 : return 日.D14;
			case 15 : return 日.D15;
			case 16 : return 日.D16;
			case 17 : return 日.D17;
			case 18 : return 日.D18;
			case 19 : return 日.D19;
			case 20 : return 日.D20;
			case 21 : return 日.D21;
			case 22 : return 日.D22;
			case 23 : return 日.D23;
			case 24 : return 日.D24;
			case 25 : return 日.D25;
			case 26 : return 日.D26;
			case 27 : return 日.D27;
			case 28 : return 日.D28;
			case 29 : return 日.D29;
			case 30 : return 日.D30;
			case 31 : return 日.D31;
		}
		return null;
	}

}
