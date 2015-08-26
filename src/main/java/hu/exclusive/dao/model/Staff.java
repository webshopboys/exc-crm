package hu.exclusive.dao.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "T_STAFF")

@NamedQueries({
		@NamedQuery(name = "Staff.getStaffByName", query = "SELECT s FROM Staff s WHERE upper(s.fullName) like :personName") })

public class Staff extends StaffBase {

	private static final long serialVersionUID = 3945633186968471433L;

}
