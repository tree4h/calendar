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
public class Owner extends Model {

	@Id
	public Long id;

	@NotNull
	public String name;

	public static Finder<Long, Owner> $find = new Finder<Long, Owner>(
			Long.class, Owner.class);

	public Owner(String name) {
		this.name = name;
	}

}
