package com.sunilos.p4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.sunilos.p4.bean.ProductBean;
import com.sunilos.p4.exception.ApplicationException;
import com.sunilos.p4.exception.DuplicateRecordException;
import com.sunilos.p4.util.JDBCDataSource;

public class ProductModel extends BaseModel<ProductBean> {

	@Override
	public long add(ProductBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");
		Connection conn = null;
		int pk = 0;

		ProductBean existbean = findByProductName(bean.getProductName());

		if (existbean != null) {
			throw new DuplicateRecordException("productName already exists");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key
			System.out.println(pk + " in ModelJDBC");
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + getTable() + " VALUES(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getProductName());
			pstmt.setString(3, bean.getProductCategory());
			pstmt.setDate(4, new java.sql.Date(bean.getOrderDate().getTime()));
			pstmt.setInt(5, bean.getPrice());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}

	@Override
	public void update(ProductBean bean) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getWhereClause(ProductBean bean) {

		StringBuffer sql = new StringBuffer();

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getProductName() != null && bean.getProductName().length() > 0) {
				sql.append(" AND PRODUCT_NAME like '" + bean.getProductName() + "%'");
			}
		}

		return sql.toString();
	}

	public ProductBean findByProductName(String productName) {
		return findByUniqueColumn("PRODUCT_NAME", productName);
	}

	@Override
	public String getTable() {
		return "st_product";
	}

	@Override
	public ProductBean getBean() {
		return new ProductBean();
	}

}
