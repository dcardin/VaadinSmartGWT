package org.vaadin.smartgwt;

import java.util.Enumeration;
import java.util.Iterator;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionType;
import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

public class SmartGWTApplication extends Application
{
	public void init()
	{
		Window mainWindow = new Window("BorderLayout Test");
		mainWindow.setSizeFull();
		mainWindow.setBorder(Window.BORDER_NONE);
		mainWindow.setStyleName(null);

		BorderLayout layout = new BorderLayout();
		layout.setSizeFull();
		
		mainWindow.setContent(layout);
		setMainWindow(mainWindow);
	}
	
	private ComponentContainer getMainPanel()
	{
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
		return tabset;
		
	}

	private ComponentContainer getMainPanel2()
	{
		BorderLayout layout = new BorderLayout();
		return layout;
	}
	
	private ComponentContainer getVertical()
	{
		VLayout layout = new VLayout();
		layout.setWidth("150 px");
		layout.setHeight100();
		layout.setBackgroundColor("green");
		
		for (int i=0; i < 10; i++) 
		{
			IButton button = new IButton("Button " + i);
			layout.addComponent(button);
		}
		
		return layout;
	}
	

	public VLayout getSpecial()
	{
		VLayout vlayout = new VLayout();
		vlayout.setWidth("100%");
		vlayout.setHeight("450 px");

		vlayout.addComponent(createForm(4));
		IButton button = new IButton("Click to switch");
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

	public static <T> Iterable<T> iterate2(final Enumeration<T> en)
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

}
