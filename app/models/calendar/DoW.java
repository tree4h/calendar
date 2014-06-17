package models.calendar;

public enum DoW {
	日(1, "SUN"),
	月(2, "MON"),
	火(3, "TUE"),
	水(4, "WED"),
	木(5, "THU"),
	金(6, "FRI"),
	土(7, "SAT");

	private int dowNumber;
	private String dow英名;

	DoW(int dowNumber, String dow英名) {
		this.dowNumber = dowNumber;
		this.dow英名 = dow英名;
	}

	public String get和名() {
		return this.toString();
	}

	public int getNumber() {
		return this.dowNumber;
	}

	public String get英名() {
		return this.dow英名;
	}

	public static DoW get曜日(int number) {
		switch (number) {
			case 1 : return DoW.日;
			case 2 : return DoW.月;
			case 3 : return DoW.火;
			case 4 : return DoW.水;
			case 5 : return DoW.木;
			case 6 : return DoW.金;
			case 7 : return DoW.土;
		}
		return null;
	}
}
