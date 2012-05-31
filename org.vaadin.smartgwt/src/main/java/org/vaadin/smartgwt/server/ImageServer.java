package org.vaadin.smartgwt.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bsh.Interpreter;

import com.google.common.base.Throwables;
import com.netappsid.configurator.IConfigurator;
import com.netappsid.utils.StreamUtils;

public class ImageServer extends HttpServlet {
	private final Interpreter interpreter = new Interpreter();

	public ImageServer() {
		try {
			final InputStream scriptStream = getClass().getClassLoader().getResourceAsStream("/org/vaadin/smartgwt/ImageServer.bsh");
			interpreter.setClassLoader(ConfigPropertyEditor.getConfiguratorClassLoader());
			interpreter.eval(new InputStreamReader(scriptStream));
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		OutputStream out = null;

		try {
			out = resp.getOutputStream();
			resp.setHeader("Vary", "Accept-Encoding");

			if (req.getParameter("type").equals("ressource")) {
				InputStream imageStream = null;

				String resName = req.getParameter("name");
				if (resName.contains(".jpg"))
					resp.setContentType("image/jpg");
				else
					resp.setContentType("image/png");

				long cacheAge = 60*60;
				long expiry = new Date().getTime() + cacheAge * 1000;

				resp.setDateHeader("Expires", expiry);
				resp.setHeader("Cache-Control", "max-age=" + cacheAge);

				IConfigurator configurator = (IConfigurator) req.getSession().getAttribute("configurator");
				imageStream = configurator.getConfigurables().get(0).getClass().getResourceAsStream(resName);

				if (imageStream == null) {
					imageStream = getClass().getClassLoader().getResourceAsStream(resName);
				}

				if (imageStream != null) {
					StreamUtils.copyInputStream(imageStream, out);
					imageStream.close();
				} else {
					log("Cannot find: " + resName);
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				}

				return;
			}

			int width = Integer.parseInt(req.getParameter("width"));
			int height = Integer.parseInt(req.getParameter("height"));

			Boolean initialized = (Boolean) req.getSession().getAttribute("initialized");

			if (initialized == null | initialized == false) {
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) image.getGraphics();
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, width, height);
				resp.setContentType("image/jpg");
				ImageIO.write(image, "jpg", out);
			}
			long cacheAge = 60*60;
			
			long expiry = new Date().getTime() + cacheAge * 1000;

			resp.setDateHeader("Expires", expiry);
			resp.setHeader("Cache-Control", "max-age=" + cacheAge);
			writeImage(req, resp, out, width, height);
		} catch (Exception e) {
			log("Unexpected error", e);
		} finally {
			if (out != null)
				out.close();
		}

	}

	private InputStream getConfiguratorImageStream(HttpServletRequest req, InputStream imageStream, String resName) {
		try {
			return (InputStream) interpreter.getNameSpace().invokeMethod("getConfiguratorImageStream", new Object[] { req, resName }, interpreter);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	private void writeImage(HttpServletRequest req, HttpServletResponse resp, OutputStream out, int width, int height) {
		try {
			interpreter.getNameSpace().invokeMethod("writeImage", new Object[] { req, resp, out, width, height }, interpreter);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
