package org.vaadin.smartgwt.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;

public class ImageServerResourcesServlet extends HttpServlet
{
	private static final String ALIAS = "/img";
	private final HttpService httpService;
	private Bundle applicationBundle;

	public ImageServerResourcesServlet(HttpService httpService, BundleContext context)
	{
		for (Bundle bundle : context.getBundles())
		{
			if ("org.vaadin.org.vaadin.smartgwt".equals(bundle.getSymbolicName()))
			{
				this.applicationBundle = bundle;
				break;
			}
		}

		this.httpService = httpService;
	}

	public void start() throws Exception
	{
		httpService.registerServlet(ALIAS, this, null, null);
	}

	public void stop()
	{
		httpService.unregister(ALIAS);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String path = req.getPathInfo();
		String resourcePath = ALIAS + path;

		URL u = applicationBundle.getResource(resourcePath);

		if (null == u)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		final InputStream in = u.openStream();
		final OutputStream out = resp.getOutputStream();
		final byte[] buffer = new byte[1024];
		int read = 0;

		while (-1 != (read = in.read(buffer)))
		{
			out.write(buffer, 0, read);
		}
	}
}
