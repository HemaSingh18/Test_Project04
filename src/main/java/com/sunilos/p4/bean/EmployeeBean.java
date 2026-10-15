package com.sunilos.p4.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EmployeeBean extends BaseBean {

	private String name;
	private int salary;
	private String company;
	private Date dob;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public void setResultset(ResultSet rs) {
		super.setResultset(rs);
		try {
			this.setName(rs.getString("NAME"));
			this.setSalary(rs.getInt("SALARY"));
			this.setCompany(rs.getString("COMPANY"));
			this.setDob(rs.getDate("DOB"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
