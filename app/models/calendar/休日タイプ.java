package models.calendar;

public enum 休日タイプ {
	休日 {
		@Override
		public boolean isHoliday() {
			return true;
		}
	},
	祝日 {
		@Override
		public boolean isHoliday() {
			return true;
		}
	},
	休診 {
		@Override
		public boolean isHoliday() {
			return true;
		}
	},
	不稼働 {
		@Override
		public boolean isHoliday() {
			return true;
		}
	},
	稼働 {
		@Override
		public boolean isHoliday() {
			return false;
		}
	},
	休出 {
		@Override
		public boolean isHoliday() {
			return false;
		}
	};

	abstract public boolean isHoliday();
}
