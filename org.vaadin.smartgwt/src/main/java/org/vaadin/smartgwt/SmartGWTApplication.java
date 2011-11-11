package org.vaadin.smartgwt;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.smartgwt.client.types.SelectionType;
import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

public class SmartGWTApplication extends Application 
{
	public void init()
	{
//		FormContainer fc = new FormContainer("daniel");
//		try {
//			fc.addStream(SmartGWTApplication.class.getResourceAsStream("/daniel.xml"), "daniel.xml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		DefaultFormBuilder fb = new DefaultFormBuilder(new ComponentFactoryImpl(), new LayoutFactoryImpl(), new BorderFactoryImpl(), fc, new LocalizedResourceBundle("temp"), null, 
//				newElementStack(null));
//		
//		Container container = AdapterHelper.unwrap(fb.getDocumentForm());
		Class<? extends Object> a = String.class;
		
		Window mainWindow = new Window("BorderLayout Test");
		mainWindow.setSizeFull();
		mainWindow.setBorder(Window.BORDER_NONE);
		mainWindow.setStyleName(null);
		
		mainWindow.setContent((ComponentContainer) getMainPanel());
		setMainWindow(mainWindow);
	}
	
//	private static SecuredElementStack newElementStack(String formSecurityKey)
//	{
//		final PermissionManagerFactory permissionManagerFactory = new PermissionManagerFactory();
//		final PathBuilder securityKeyBuilder = new PathBuilder(formSecurityKey);
//		final PermissionManager permissionManager;
//
//		if (formSecurityKey != null)
//		{
//			permissionManager = permissionManagerFactory.getPermissionManager(formSecurityKey);
//		}
//		else
//		{
//			permissionManager = permissionManagerFactory.getDefaultPermissionManager();
//		}
//
//		return new SecuredElementStackImpl(securityKeyBuilder, permissionManager);
//	}
	
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
		east.setHeight("50");
	
		layout.addComponent(north, BorderLayout.Constraint.NORTH);
		if (subPanel)
			layout.addComponent(getMainPanel(), BorderLayout.Constraint.CENTER);
		layout.addComponent(south, BorderLayout.Constraint.SOUTH);
		layout.addComponent(west, BorderLayout.Constraint.WEST);
		layout.addComponent(east, BorderLayout.Constraint.EAST);
		
		return layout;
	}
	private BaseWidget getButtonLayout()
	{
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setBackgroundColor("green");
		
		for (int i=0; i < 10; i++) 
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
		tabset.setWidth("100%");
		tabset.setHeight("600 px");

		Tab tab = new Tab("premier");
		Tab tab2 = new Tab("deuxieme");
		Tab tab3 = new Tab("troisième");
		Tab tab4 = new Tab("un autre");
		Tab tab5 = new Tab("recursif");

		tab.setPane(createForm(4));
		tab2.setPane(createForm(6));
		VLayout vl = new VLayout();
		vl.addMember(new Button("Press me 1!"));
		vl.addMember(new Button("Press me 2!"));
		vl.addMember(new Button("Press me 3!"));
		vl.addMember(new Button("Press me 4!"));
		Label filler = new Label("");
		filler.setHeight("*");
		filler.setWidth100();
		vl.addMember(filler);
		tab5.setPane(complexLayout(false));
		
		
		tab3.setPane(vl);
		tab4.setPane(getSpecial());

		tabset.setTabs(tab, tab2, tab3, tab4, tab5);
		return tabset;
		
	}

	private ComponentContainer getMainPanel2()
	{
		BorderLayout layout = new BorderLayout();
		return layout;
	}
	
	private ComponentContainer getVertical()
	{
		final HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		
		for (int i=0; i < 10; i++) 
		{
			Button button = new Button("Button " + i)
			{
				@Override
				public void changeVariables(Object source, Map<String, Object> variables)
				{
					if (new Random().nextBoolean() == true)
					{
						layout.removeMember(layout.getMembers()[0]);
						layout.requestRepaint();
					}
					else
					{
						layout.replaceComponent(this, new Button("unclickable button"));
						layout.requestRepaint();
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
		vlayout.setWidth("100%");
		vlayout.setHeight("450 px");

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
		int i = 1;

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
