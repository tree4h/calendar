package models.calendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "year_id", "month", "day" }) })
public class カレンダ日インデックス extends Model {

	@Id
	public Long id;

	@NotNull
	@ManyToOne
	public カレンダ年 year;
	@NotNull
	public int month;
	@NotNull
	public int day;
	@NotNull
	public int index;

	public static Finder<Long, カレンダ日インデックス> $find = new Finder<Long, カレンダ日インデックス>(
			Long.class, カレンダ日インデックス.class);

	public カレンダ日インデックス(カレンダ年 year, int month, int day, int index) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.index = index;
	}


}
