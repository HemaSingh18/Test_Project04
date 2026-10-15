package com.sunilos.p4.model;

import com.sunilos.p4.bean.EmployeeBean;
import com.sunilos.p4.exception.ApplicationException;
import com.sunilos.p4.exception.DuplicateRecordException;

public class EmployeeModel extends BaseModel<EmployeeBean> {

	@Override
	public long add(EmployeeBean bean) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(EmployeeBean bean) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWhereClause(EmployeeBean bean) {
		StringBuffer sql = new StringBuffer();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			if (bean.getCompany() != null && bean.getCompany().length() > 0) {
				sql.append(" AND COMPANY like '" + bean.getCompany() + "%'");
			}
		}

		return sql.toString();
	}

	@Override
	public String getTable() {
		return "st_employee";
	}

	@Override
	public EmployeeBean getBean() {
		return new EmployeeBean();
	}

}
