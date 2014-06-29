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
		String name = "D" + Integer.toString(number);
		return Day.valueOf(name);
	}

}
