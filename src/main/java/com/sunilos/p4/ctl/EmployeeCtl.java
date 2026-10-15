package com.sunilos.p4.ctl;

import com.sunilos.p4.bean.EmployeeBean;
import com.sunilos.p4.model.EmployeeModel;

import jakarta.servlet.http.HttpServletRequest;

public class EmployeeCtl extends BaseCtl<EmployeeBean, EmployeeModel> {
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return super.validate(request);
	}
	
	@Override
	protected EmployeeBean populateBean(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return super.populateBean(request);
	}

	@Override
	protected String getView() {
		return null;
	}

	@Override
	protected String getView(String op) {

		return null;
	}

	@Override
	protected EmployeeModel getModel() {
		return new EmployeeModel();
	}

}
