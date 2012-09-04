package org.vaadin.smartgwt.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@Singleton
public class ImageServerResourcesServlet extends HttpServlet {
	private Bundle applicationBundle;

	public void activate(BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			if ("org.vaadin.org.vaadin.smartgwt".equals(bundle.getSymbolicName())) {
				this.applicationBundle = bundle;
				break;
			}
		}
	}

	public void deactivate(BundleContext context) {
		applicationBundle = null;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		String resourcePath = "/img" + path;

		URL u = applicationBundle.getResource(resourcePath);

		if (null == u) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		final InputStream in = u.openStream();
		final OutputStream out = resp.getOutputStream();
		final byte[] buffer = new byte[1024];
		int read = 0;

		while (-1 != (read = in.read(buffer))) {
			out.write(buffer, 0, read);
		}
	}
}
