package models.party;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name"}) })
public class 所有者 extends Model {

	@Id
	public Long id;

	@NotNull
	public String name;

	public static Finder<Long, 所有者> $find = new Finder<Long, 所有者>(
			Long.class, 所有者.class);

	public 所有者(String name) {
		this.name = name;
	}

}
