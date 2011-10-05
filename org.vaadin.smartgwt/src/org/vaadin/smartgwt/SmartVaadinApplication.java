package org.vaadin.smartgwt;

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.RequestFacade;

import com.smartgwt.client.types.SelectionType;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;

public class SmartVaadinApplication extends Application implements HttpServletRequestListener
{
	public VLayout vlayout;
	public IButton button;
	public static SmartVaadinApplication app;

	public void init()
	{
		Window mainWindow = new Window("SmartVaadin Test");
		mainWindow.setSizeFull();
		mainWindow.setBorder(Window.BORDER_NONE);
		mainWindow.setStyleName(null);

		TabSet tabset = new TabSet();
		tabset.setWidth("100%");
		tabset.setHeight("600 px");

		Tab tab = new Tab("premier");
		Tab tab2 = new Tab("deuxieme");
		Tab tab3 = new Tab("troisième");
		Tab tab4 = new Tab("un autre");

		tab.setPane(createForm(4));
		tab2.setPane(createForm(6));
		tab3.setPane(new IButton("Press me!"));
		tab4.setPane(getSpecial());

		tabset.setTabs(tab, tab2, tab3, tab4);

		mainWindow.setContent(tabset);
		setMainWindow(mainWindow);
	}

	public void init5()
	{
		app = this;

		Window mainWindow = new Window("SmartVaadin Test");
		mainWindow.setSizeFull();
		mainWindow.setBorder(Window.BORDER_NONE);
		mainWindow.setStyleName(null);

		mainWindow.setContent(getSpecial());
		setMainWindow(mainWindow);
	}

	public VLayout getSpecial()
	{
		vlayout = new VLayout();
		vlayout.setWidth("100%");
		vlayout.setHeight("450 px");

		vlayout.addComponent(createForm(4));
		button = new IButton("Click to switch");
		button.setIcon("http://www.mricons.com/store/png/119004_36574_32_top_icon.png");
		button.setWidth(200);
		button.setActionType(SelectionType.CHECKBOX);

		vlayout.addComponent(button);
		vlayout.addComponent(createForm(6));

		return vlayout;
	}

	public DynamicForm createForm(int nbCols)
	{
		DynamicForm form = new DynamicForm();
		form.setWidth("100%");
		form.setNumCols(nbCols);
		int i = 1;

		TextItem ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setValue("value!");
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Autre Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Blah Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setWidth("100%");
		ti.setTitle("Edit Field " + i++);
		form.addComponent(ti);

		ti = new TextItem();
		ti.setWidth("100%");
		ti.setTitle("Edit Field " + i++);
		form.addComponent(ti);

		ti = new TextItem();
		ti.setWidth("100%");
		ti.setTitle("Edit Field " + i++);
		form.addComponent(ti);

		return form;
	}

	public static <T> Iterable<T> iterate(final Enumeration<T> en)
	{
		final Iterator<T> iterator = new Iterator<T>()
			{
				public boolean hasNext()
				{
					return en.hasMoreElements();
				}

				public T next()
				{
					return en.nextElement();
				}

				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		return new Iterable<T>()
			{
				public Iterator<T> iterator()
				{
					return iterator;
				}
			};
	}

	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response)
	{
		if (request.getRequestURI().equals("/SmartVaadin/"))
		{
			PartialPaintChecker.forceFullRepaint();
		}
	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response)
	{
		PartialPaintChecker.resetFullRepaint();
	}

}
