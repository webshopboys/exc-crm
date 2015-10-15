package hu.exclusive.crm.businesslogic;

import hu.exclusive.dao.model.StaffBase;

public class DailyWorkplaceRule implements IWorktimeRule {

	@Override
	public void init(StaffBase staffs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean calculateRule(Object... params) {
		return true;
	}

	@Override
	public String infoMessage(String... additionalText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String errorMessage(String... additionalText) {
		// TODO Auto-generated method stub
		return null;
	}

}
