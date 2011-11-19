package org.vaadin.smartgwt;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.vaadin.smartgwt.server.BaseWidget;
import org.vaadin.smartgwt.server.Button;
import org.vaadin.smartgwt.server.IButton;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.data.ListGridField;
import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.DateItem;
import org.vaadin.smartgwt.server.form.fields.FormItem;
import org.vaadin.smartgwt.server.form.fields.SelectItem;
import org.vaadin.smartgwt.server.form.fields.TextItem;
import org.vaadin.smartgwt.server.grid.ListGrid;
import org.vaadin.smartgwt.server.layout.BorderLayout;
import org.vaadin.smartgwt.server.layout.HLayout;
import org.vaadin.smartgwt.server.layout.Layout;
import org.vaadin.smartgwt.server.layout.MasterContainer;
import org.vaadin.smartgwt.server.layout.VLayout;
import org.vaadin.smartgwt.server.tab.Tab;
import org.vaadin.smartgwt.server.tab.TabSet;

import org.vaadin.smartgwt.server.types.Alignment;
import org.vaadin.smartgwt.server.types.SelectionType;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.vaadin.Application;
import com.vaadin.ui.Window;

public class SmartGWTApplication extends Application
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init()
	{
		final Window mainWindow = new Window("BorderLayout Test 2");
		setMainWindow(mainWindow);
		mainWindow.setStyleName(null);
		mainWindow.setSizeFull();

		MasterContainer layout = new MasterContainer();
		layout.addMember(getEricLayout());

		mainWindow.setContent(layout);
	}
	
	public static void main(String[] args)
	{
		new SmartGWTApplication().getListGrid();
	}
	private void getListGrid()
	{
        final ListGrid countryGrid = new ListGrid();  
        countryGrid.setWidth(500);  
        countryGrid.setHeight(224);  
        countryGrid.setShowAllRecords(true);  
  
        ListGridField countryCodeField = new ListGridField("countryCode", "Flag", 40);  
        countryCodeField.setAlign(Alignment.CENTER);  
        countryCodeField.setType(ListGridFieldType.IMAGE);  
        countryCodeField.setImageURLPrefix("flags/16/");  
        countryCodeField.setImageURLSuffix(".png");  
  
        ListGridField nameField = new ListGridField("countryName", "Country");  
        ListGridField capitalField = new ListGridField("capital", "Capital");  
        ListGridField continentField = new ListGridField("continent", "Continent");  
        
        countryGrid.setFields(countryCodeField, nameField, capitalField, continentField);  
        countryGrid.setCanResizeFields(true);  
        countryGrid.setData(CountryData.getRecords());  
	}
	
	private Layout paintBorderLayout()
	{
		final VLayout outerLayout = new VLayout();
		outerLayout.setSizeFull();
		outerLayout.setBackgroundColor("cyan");

		final HLayout northLayout = new HLayout();
		northLayout.setBackgroundColor("blue");
		northLayout.setHeight("25%");

		final HLayout outerCenterLayout = new HLayout();
		outerCenterLayout.setBackgroundColor("green");
		outerCenterLayout.setHeight("*");

		final HLayout southLayout = new HLayout();
		southLayout.setBackgroundColor("red");
		southLayout.setHeight("25%");

		final HLayout westLayout = new HLayout();
		westLayout.setBackgroundColor("orange");
		westLayout.setWidth("25%");

		final HLayout centerLayout = new HLayout();
		centerLayout.setBackgroundColor("white");
		centerLayout.setWidth("*");

		final HLayout eastLayout = new HLayout();
		eastLayout.setBackgroundColor("pink");
		eastLayout.setWidth("25%");

		outerCenterLayout.addMember(westLayout);
		outerCenterLayout.addMember(centerLayout);
		outerCenterLayout.addMember(eastLayout);

		outerLayout.addMember(northLayout);
		outerLayout.addMember(outerCenterLayout);
		outerLayout.addMember(southLayout);

		return outerLayout;
	}

	private BaseWidget complexLayout(boolean subPanel)
	{
		BorderLayout layout = new BorderLayout();
		layout.setSizeFull();

		HLayout north = new HLayout();
		Button button = new Button("north");

		north.addMember(button);
		north.setBackgroundColor("red");
		Label south = new Label("south");
		south.setBackgroundColor("red");
		south.setHeight("50");
		BaseWidget west = getButtonLayout();
		west.setWidth("150");
		Label east = new Label("east");
		east.setBackgroundColor("green");
		east.setHeight100();

		layout.addComponent(north, BorderLayout.Constraint.NORTH);

		if (subPanel)
			layout.addComponent(getMainPanel(), BorderLayout.Constraint.CENTER);
		layout.addComponent(south, BorderLayout.Constraint.SOUTH);
		layout.addComponent(west, BorderLayout.Constraint.WEST);
		layout.addComponent(east, BorderLayout.Constraint.EAST);

		return layout;
	}

	private Layout getEricLayout()
	{
		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(20);

		VLayout vLayout = new VLayout();
		vLayout.setShowEdges(true);
		vLayout.setWidth(150);
		vLayout.setMembersMargin(5);
		vLayout.setLayoutMargin(10);
		vLayout.addMember(new BlueBox(null, 50, "height 50"));
		vLayout.addMember(new BlueBox((String) null, "*", "height *"));
		vLayout.addMember(new BlueBox((String) null, "30%", "height 30%"));
		layout.addMember(vLayout);

		HLayout hLayout = new HLayout();
		hLayout.setShowEdges(true);
		hLayout.setHeight(150);
		hLayout.setMembersMargin(5);
		hLayout.setLayoutMargin(10);
		hLayout.addMember(new BlueBox(50, (Integer) null, "width 50"));
		hLayout.addMember(new BlueBox("*", null, "width *"));
		hLayout.addMember(new BlueBox("30%", null, "width 30%"));
		layout.addMember(hLayout);

		return layout;
	}

	class BlueBox extends Label
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BlueBox(String contents)
		{
			super("");
			setAlign(Alignment.CENTER);
			setBorder("1px solid #808080");
			setBackgroundColor("#C3D9FF");
			setContents(contents);
		}

		public BlueBox(Integer width, Integer height, String contents)
		{
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}

		public BlueBox(Integer width, String height, String contents)
		{
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}

		public BlueBox(String width, String height, String contents)
		{
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}
	}

	private BaseWidget getButtonLayout()
	{
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		for (int i = 0; i < 10; i++)
		{
			Button button = new Button("Button " + i);

			button.setHeight("*");
			button.setWidth("100 px");
			layout.addMember(button);
		}
		return layout;
	}

	private BaseWidget getMainPanel()
	{
		TabSet tabset = new TabSet();
		tabset.setSizeFull();

		Tab tab = new Tab("premier");
		Tab tab2 = new Tab("deuxieme");
		Tab tab3 = new Tab("troisième");
		Tab tab4 = new Tab("un autre");
		Tab tab5 = new Tab("recursif");
		Tab tab6 = new Tab("avec event");
		Tab tab7 = new Tab("Fake border");

		tab.setPane(createForm(4));
		tab2.setPane(createForm(6));
		VLayout vl = new VLayout();
		vl.setMembersMargin(4);
		vl.addMember(new IButton("Press me 1!"));
		vl.addMember(new IButton("Press me 2!"));
		vl.addMember(new IButton("Press me 3!"));
		vl.addMember(new IButton("Press me 4!"));
		Label filler = new Label("");
		filler.setHeight("*");
		filler.setWidth100();
		vl.addMember(filler);
		tab5.setPane(complexLayout(false));
		tab6.setPane(getVertical());

		tab3.setPane(vl);
		tab4.setPane(getSpecial());

		Tab tabEric = new Tab("Éric");
		tabEric.setPane(getEricLayout());

		tab7.setPane(paintBorderLayout());

		tabset.setTabs(tabEric, tab, tab2, tab3, tab4, tab5, tab6, tab7);
		return tabset;

	}

	// private ComponentContainer getMainPanel2()
	// {
	// BorderLayout layout = new BorderLayout();
	// return layout;
	// }

	private Layout getVertical()
	{
		final VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		for (int i = 0; i < 10; i++)
		{
			Button button = new Button("Button " + i)
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void changeVariables(Object source, Map<String, Object> variables)
					{
						if (new Random().nextBoolean() == true)
						{
							layout.removeMember(layout.getMembers()[0]);
						}
						else
						{
							layout.replaceComponent(this, new Button("unclickable button"));
						}
					}
				};
			layout.addComponent(button);
		}

		return layout;
	}

	public VLayout getSpecial()
	{
		VLayout vlayout = new VLayout();
		vlayout.setMargin(10);
		vlayout.setWidth("100%");
		vlayout.setMembersMargin(30);

		vlayout.addComponent(createForm(4));
		Button button = new Button("Click to switch");
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
		form.setBackgroundColor("#F0F0F0");
		form.setShowShadow(true);
		form.setShadowDepth(10);
		form.setBorder("1px solid gray");
		form.setTitle("Un formulaire...");
		form.setPadding(10);

		int i = 1;

		final FormItem tiEvent = new TextItem();
		tiEvent.addChangedHandler(new ChangedHandler()
			{
				@Override
				public void onChanged(ChangedEvent event)
				{
					tiEvent.setValue("c:" + event.getValue());
				}
			});

		tiEvent.setTitle("Edit Field " + i++);
		tiEvent.setValue("value!");
		tiEvent.setWidth("100%");
		form.addComponent(tiEvent);

		FormItem ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setValue("value!");
		ti.setWidth("100%");
		form.addComponent(ti);

		ti = new TextItem();
		ti.setTitle("Edit Field " + i++);
		ti.setValue("value!");
		ti.setWidth("100%");
		form.addComponent(ti);

		SelectItem si = new SelectItem("");
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addComponent(si);

		si = new SelectItem("");
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addComponent(si);

		si = new SelectItem("");
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addComponent(si);

		si = new SelectItem("");
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addComponent(si);

		DateItem di = new DateItem();
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		di.setUseTextField(true);
		form.addComponent(di);

		di = new DateItem();
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addComponent(di);

		di = new DateItem();
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		di.setUseTextField(true);
		form.addComponent(di);

		di = new DateItem();
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addComponent(di);

		di = new DateItem();
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addComponent(di);

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
				@Override
				public boolean hasNext()
				{
					return en.hasMoreElements();
				}

				@Override
				public T next()
				{
					return en.nextElement();
				}

				@Override
				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		return new Iterable<T>()
			{
				@Override
				public Iterator<T> iterator()
				{
					return iterator;
				}
			};
	}

}
