package org.vaadin.smartgwt.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bsh.Interpreter;

import com.google.common.base.Throwables;
import com.netappsid.utils.StreamUtils;

public class ImageServer extends HttpServlet
{
	private final Interpreter interpreter = new Interpreter();

	public ImageServer()
	{
		try
		{
			interpreter.setClassLoader(ConfigPropertyEditor.getConfiguratorClassLoader());
			interpreter.source("/home/ebelanger/Desktop/webapp/WEB-INF/lib/ImageServer.bsh");
		}
		catch (Exception e)
		{
			Throwables.propagate(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		OutputStream out = null;

		try
		{
			// Select the appropriate content encoding based on the
			// client's Accept-Encoding header. Choose GZIP if the header
			// includes "gzip". Choose ZIP if the header includes "compress".
			// Choose no compression otherwise.
			String encodings = req.getHeader("Accept-Encoding");
			if (encodings != null && encodings.indexOf("gzip") != -1)
			{
				// Go with GZIP
				resp.setHeader("Content-Encoding", "gzip");
				out = new GZIPOutputStream(resp.getOutputStream());
			}
			else if (encodings != null && encodings.indexOf("compress") != -1)
			{
				// Go with ZIP
				resp.setHeader("Content-Encoding", "x-compress");
				out = new ZipOutputStream(resp.getOutputStream());
				((ZipOutputStream) out).putNextEntry(new ZipEntry("dummy name"));
			}
			else
			{
				// No compression
				out = resp.getOutputStream();
			}
			resp.setHeader("Vary", "Accept-Encoding");

			if (req.getParameter("type").equals("ressource"))
			{
				InputStream imageStream = null;

				String resName = req.getParameter("name");
				resp.setContentType("image/jpg");

				imageStream = getConfiguratorImageStream(req, imageStream, resName);

				if (imageStream == null)
				{
					imageStream = getClass().getClassLoader().getResourceAsStream(resName);
				}

//				if (imageStream == null)
//				{
//					imageStream = configurator.getDefaultConfigurable().getObject().getClass().getClassLoader().getResourceAsStream(resName));
//				}

				if (imageStream != null)
				{
					StreamUtils.copyInputStream(imageStream, out);
					imageStream.close();
				}
				else
				{
					log("Cannot find: " + resName);
					resp.sendError(404);
				}

				return;
			}

			int width = Integer.parseInt(req.getParameter("width"));
			int height = Integer.parseInt(req.getParameter("height"));

			Boolean initialized = (Boolean) req.getSession().getAttribute("initialized");

			if (initialized == null | initialized == false)
			{
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = (Graphics2D) image.getGraphics();
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, width, height);
				resp.setContentType("image/jpg");
				ImageIO.write(image, "jpg", out);
			}

			writeImage(req, resp, out, width, height);
		}
		catch (Exception e)
		{
			log("Unexpected error", e);
		}
		finally
		{
			if (out != null)
				out.close();
		}

	}

	private InputStream getConfiguratorImageStream(HttpServletRequest req, InputStream imageStream, String resName)
	{
		try
		{
			return (InputStream) interpreter.getNameSpace().invokeMethod("getConfiguratorImageStream", new Object[] { req, imageStream, resName }, interpreter);
		}
		catch (Exception e)
		{
			throw Throwables.propagate(e);
		}
	}

	private void writeImage(HttpServletRequest req, HttpServletResponse resp, OutputStream out, int width, int height)
	{
		try
		{
			interpreter.getNameSpace().invokeMethod("writeImage", new Object[] { req, resp, out, width, height }, interpreter);
		}
		catch (Exception e)
		{
			throw Throwables.propagate(e);
		}
	}
}
