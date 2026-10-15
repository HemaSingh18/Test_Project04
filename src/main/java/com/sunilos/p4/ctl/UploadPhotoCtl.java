package com.sunilos.p4.ctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.sunilos.p4.bean.UserBean;
import com.sunilos.p4.exception.ApplicationException;
import com.sunilos.p4.model.RoleModel;
import com.sunilos.p4.model.UserModel;
import com.sunilos.p4.util.DataUtility;
import com.sunilos.p4.util.DataValidator;
import com.sunilos.p4.util.PropertyReader;
import com.sunilos.p4.util.ServletUtility;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/ctl/uploadphoto")
@MultipartConfig
public class UploadPhotoCtl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RoleModel model = new RoleModel();
		try {
			List l = model.list();
			request.setAttribute("roleList", l);
		} catch (ApplicationException e) {
		}

		super.service(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id = DataUtility.getLong(request.getParameter("id"));

		UserModel model = new UserModel();
		UserBean bean = null;

		try {
			bean = model.findByPK(id);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		String basePath = PropertyReader.getValue("photoPath");

		File photoFile = null;

		// Check whether user has uploaded a photo
		if (bean != null && DataValidator.isNotNull(bean.getPhoto())) {

			photoFile = new File(basePath, bean.getPhoto());

			if (photoFile.exists()) {

				String contentType = getServletContext().getMimeType(photoFile.getName());

				if (contentType == null) {
					contentType = "application/octet-stream";
				}

				response.setContentType(contentType);

				FileInputStream fis = new FileInputStream(photoFile);
				OutputStream os = response.getOutputStream();

				byte[] buffer = new byte[4096];
				int bytes;

				while ((bytes = fis.read(buffer)) != -1) {
					os.write(buffer, 0, bytes);
				}

				fis.close();
				os.close();
				return;
			}
		}

		// If no uploaded photo exists, show the default logo from WAR
		InputStream is = getServletContext().getResourceAsStream("/img/logo.png");

		if (is == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType("image/png");

		OutputStream os = response.getOutputStream();

		byte[] buffer = new byte[4096];
		int bytes;

		while ((bytes = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}

		is.close();
		os.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long id = DataUtility.getLong(request.getParameter("id"));
		UserModel model = new UserModel();
		UserBean bean = model.findByPK(id);
		ServletUtility.setBean(bean, request);
		String view = request.getParameter("view");

		Part part = request.getPart("photo");

		if (part == null || part.getSize() == 0) {
			ServletUtility.setErrorMessage("Photo is required", request);
			ServletUtility.forwardPage(getView(), request, response);
			return;
		}

		System.out.println("part ==== : " + part.getName());

		// Original file name
		String fileName = part.getSubmittedFileName(); // get original file name

		// Folder path from system.properties
		String basePath = PropertyReader.getValue("photoPath");

		File folder = new File(basePath);

		System.out.println("base path of image folder: " + folder.getName());

		if (!folder.exists()) {
			folder.mkdirs();
		}

		File destFile = new File(folder, fileName);

		// Save file
		InputStream input = part.getInputStream();
		FileOutputStream output = new FileOutputStream(destFile);

		byte[] buffer = new byte[4096];
		int bytesRead;

		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}

		input.close();
		output.close();

		try {

			// Update photo name in database
			model.updatePhoto(id, fileName);

			System.out.println("image successfully uploaded");

			HttpSession session = request.getSession(false);

			if (session != null) {
				UserBean user = (UserBean) session.getAttribute("user");

				if (user != null && user.getId() == id) {
					session.setAttribute("user", bean);
				}
			}

		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		// Redirect
		if ("profile".equals(view)) {
			response.sendRedirect("MyProfileCtl");
		} else {
			response.sendRedirect("UserCtl?id=" + id);
		}
	}

	public String getView() {
		return ORSView.USER_VIEW;
	}

}