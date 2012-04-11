package org.vaadin.smartgwt;

import static argo.jdom.JsonNodeBuilders.*;
import static argo.jdom.JsonNodeFactories.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.vaadin.smartgwt.server.BaseWidget;
import org.vaadin.smartgwt.server.Button;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.DateItem;
import org.vaadin.smartgwt.server.form.fields.FormItem;
import org.vaadin.smartgwt.server.form.fields.PickerIcon;
import org.vaadin.smartgwt.server.form.fields.SelectItem;
import org.vaadin.smartgwt.server.form.fields.TextItem;
import org.vaadin.smartgwt.server.form.fields.events.FormItemClickHandler;
import org.vaadin.smartgwt.server.form.fields.events.FormItemIconClickEvent;
import org.vaadin.smartgwt.server.grid.ListGrid;
import org.vaadin.smartgwt.server.grid.ListGridField;
import org.vaadin.smartgwt.server.grid.ListGridRecord;
import org.vaadin.smartgwt.server.layout.BorderLayout;
import org.vaadin.smartgwt.server.layout.HLayout;
import org.vaadin.smartgwt.server.layout.HSplitLayout;
import org.vaadin.smartgwt.server.layout.Layout;
import org.vaadin.smartgwt.server.layout.MasterContainer;
import org.vaadin.smartgwt.server.layout.SectionStack;
import org.vaadin.smartgwt.server.layout.SectionStackSection;
import org.vaadin.smartgwt.server.layout.VLayout;
import org.vaadin.smartgwt.server.layout.VSplitLayout;
import org.vaadin.smartgwt.server.tab.Tab;
import org.vaadin.smartgwt.server.tab.TabSet;
import org.vaadin.smartgwt.server.toolbar.ToolStrip;
import org.vaadin.smartgwt.server.toolbar.ToolStripButton;
import org.vaadin.smartgwt.server.types.Alignment;
import org.vaadin.smartgwt.server.types.DragDataAction;
import org.vaadin.smartgwt.server.types.ListGridEditEvent;
import org.vaadin.smartgwt.server.types.ListGridFieldType;
import org.vaadin.smartgwt.server.types.SelectionType;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class SmartGWTApplication extends Application implements MasterContainerHolder {
	private static final long serialVersionUID = 1L;

	private TabSet tabset;
	private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();
	private final MasterContainer masterContainer = new MasterContainer();

	@Override
	public MasterContainer getMasterContainer() {
		return masterContainer;
	}

	@Override
	public void init() {
		final Window mainWindow = new Window("BorderLayout Test 2");
		setMainWindow(mainWindow);
		mainWindow.setStyleName(null);
		mainWindow.setSizeFull();

		CountryXmlDS.reset();

		masterContainer.addDataSource(CountryXmlDS.getInstance());
		masterContainer.setPane(getMainPanel());

		mainWindow.setContent(masterContainer);
	}

	public Canvas getSplitTest() {
		VSplitLayout split = new VSplitLayout(true, true);
		split.setWidth100();
		split.setAutoHeight();

		Label label2 = new Label("right");
		label2.setBackgroundColor("yellow");

		split.setTopCanvas(getListGrid());
		split.setBottomCanvas(label2);

		return split;
	}

	public SectionStack getStackView() {
		SectionStack sectionStack = new SectionStack();
		sectionStack.setWidth(200);
		final ListGrid listGrid = new ListGrid();
		listGrid.setCanEdit(true);
		listGrid.setEditEvent(ListGridEditEvent.CLICK);
		listGrid.setFields(new ListGridField("system", "System"), new ListGridField("monitor", "Monitor"));

		SectionStackSection section1 = new SectionStackSection("Monitors");
		section1.addItem(listGrid);
		section1.setExpanded(true);

		SectionStackSection section2 = new SectionStackSection("Monitors");
		section2.addItem(getVertical());
		section2.setExpanded(true);

		sectionStack.addSection(section1);
		sectionStack.addSection(section2);
		return sectionStack;
	}

	public static String getJsonString(Object object) {
		if (object instanceof Map == false)
			return null;

		JsonObjectNodeBuilder builder = anObjectBuilder();

		Map<String, Object> record = (Map<String, Object>) object;
		JsonObjectNodeBuilder nodeBuilder = anObjectBuilder();

		for (Map.Entry<String, Object> entry : record.entrySet()) {
			nodeBuilder.withField(entry.getKey(), aJsonString(entry.getValue() == null ? "" : entry.getValue().toString()));
		}

		return JSON_FORMATTER.format(builder.build());
	}

	public static String getJsonString2(Record[] records) {
		JsonObjectNodeBuilder builder = anObjectBuilder();
		JsonArrayNodeBuilder recordBuilder = anArrayBuilder();

		for (Record record : records) {
			JsonObjectNodeBuilder nodeBuilder = anObjectBuilder();

			for (int i = 0; i < record.getAttributes().length; i++) {
				final String name = record.getAttributes()[i];
				final Object value = record.getAttributeAsObject(name);
				nodeBuilder.withField(name, aJsonString(value == null ? "" : value.toString()));
			}

			recordBuilder.withElement(nodeBuilder);
		}
		builder.withField("records", recordBuilder);

		JsonRootNode jso = recordBuilder.build();
		return JSON_FORMATTER.format(jso);
	}

	public static String getJsonString(Record[] records) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		StringBuffer buffer = new StringBuffer();

		buffer.append('[');

		for (Record record : records) {
			buffer.append(objectMapper.writeValueAsString(record.getAttributes()));
			buffer.append(',');
		}

		buffer.setLength(buffer.length() - 1);
		buffer.append(']');

		return buffer.toString();
	}

	private Canvas getListGrid() {
		final ListGrid countryGrid = new ListGrid() {
			@Override
			public void selectionChanged(ListGridRecord[] selections) {
				System.out.print("records: ");
				for (ListGridRecord record : selections) {
					System.out.print(record.getAttributeAsString("countryName") + ",");
				}
				System.out.println();
			}
		};

		countryGrid.setWidth(500);
		countryGrid.setHeight(224);
		countryGrid.setShowAllRecords(true);
		countryGrid.setCanDragRecordsOut(true);
		countryGrid.setCanAcceptDroppedRecords(true);
		countryGrid.setCanReorderRecords(true);
		countryGrid.setDragDataAction(DragDataAction.MOVE);

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

		return countryGrid;
	}

	private Canvas getEditableListGrid() {
		VLayout surrounding = new VLayout();
		surrounding.setSizeFull();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();

		ToolStripButton boldButton = new ToolStripButton();
		boldButton.setIcon("[SKIN]/RichTextEditor/text_bold.png");
		boldButton.setActionType(SelectionType.CHECKBOX);
		toolStrip.addButton(boldButton);

		toolStrip.addResizer();
		ToolStripButton italicsButton = new ToolStripButton();
		italicsButton.setIcon("[SKIN]/RichTextEditor/text_italic.png");
		italicsButton.setActionType(SelectionType.CHECKBOX);
		toolStrip.addButton(italicsButton);

		ToolStripButton underlineButton = new ToolStripButton();
		underlineButton.setIcon("[SKIN]/RichTextEditor/text_underline.png");
		underlineButton.setActionType(SelectionType.CHECKBOX);
		toolStrip.addButton(underlineButton);

		toolStrip.addSeparator();

		ToolStripButton alignLeftButton = new ToolStripButton();
		alignLeftButton.setIcon("[SKIN]/RichTextEditor/text_align_left.png");
		alignLeftButton.setActionType(SelectionType.RADIO);
		alignLeftButton.setRadioGroup("textAlign");
		toolStrip.addButton(alignLeftButton);

		ToolStripButton alignRightButton = new ToolStripButton();
		alignRightButton.setIcon("[SKIN]/RichTextEditor/text_align_right.png");
		alignRightButton.setActionType(SelectionType.RADIO);
		alignRightButton.setRadioGroup("textAlign");
		toolStrip.addButton(alignRightButton);

		ToolStripButton alignCenterButton = new ToolStripButton();
		alignCenterButton.setIcon("[SKIN]/RichTextEditor/text_align_center.png");
		alignCenterButton.setActionType(SelectionType.RADIO);
		alignCenterButton.setRadioGroup("textAlign");
		toolStrip.addButton(alignCenterButton);

		toolStrip.addSeparator();

		HLayout layout = new HLayout();
		layout.setSizeFull();

		final ListGrid countryGrid = new ListGrid();
		countryGrid.setMargin(5);
		countryGrid.setWidth(550);
		countryGrid.setHeight(224);
		countryGrid.setShowAllRecords(true);
		countryGrid.setCellHeight(22);
		// use server-side dataSource so edits are retained across page transitions
		countryGrid.setDataSource(CountryXmlDS.getInstance());

		ListGridField countryCodeField = new ListGridField("countryCode", "Flag", 40);
		countryCodeField.setAlign(Alignment.CENTER);
		countryCodeField.setType(ListGridFieldType.IMAGE);
		countryCodeField.setImageURLPrefix("flags/16/");
		countryCodeField.setImageURLSuffix(".png");
		countryCodeField.setCanEdit(false);

		ListGridField nameField = new ListGridField("countryName", "Pays");
		ListGridField continentField = new ListGridField("continent", "Continent");
		ListGridField memberG8Field = new ListGridField("member_g8", "Member G8");
		ListGridField populationField = new ListGridField("population", "Population");
		populationField.setType(ListGridFieldType.INTEGER);
		// populationField.setCellFormatter(new NumericFormatter());
		ListGridField independenceField = new ListGridField("independence", "Independence");
		countryGrid.setFields(countryCodeField, nameField, continentField, memberG8Field, populationField, independenceField);

		countryGrid.setAutoFetchData(true);
		countryGrid.setCanEdit(true);
		countryGrid.setEditEvent(ListGridEditEvent.CLICK);
		countryGrid.setEditByCell(true);

		layout.addMember(countryGrid);
		surrounding.addMember(toolStrip);
		surrounding.addMember(layout);

		return surrounding;
	}

	private Layout paintBorderLayout() {
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

	private Layout complexLayout(boolean subPanel) {
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

		/*
		 * TEMP layout.addMember(north, BorderLayout.Constraint.NORTH);
		 * 
		 * if (subPanel) layout.addMember(getMainPanel(), BorderLayout.Constraint.CENTER); layout.addMember(south, BorderLayout.Constraint.SOUTH);
		 * layout.addMember(west, BorderLayout.Constraint.WEST); layout.addMember(east, BorderLayout.Constraint.EAST);
		 */
		return layout;
	}

	private Layout getEricLayout() {
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

	class BlueBox extends Label {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BlueBox(String contents) {
			super("");
			setAlign(Alignment.CENTER);
			setBorder("1px solid #808080");
			setBackgroundColor("#C3D9FF");
			setContents(contents);
		}

		public BlueBox(Integer width, Integer height, String contents) {
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}

		public BlueBox(Integer width, String height, String contents) {
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}

		public BlueBox(String width, String height, String contents) {
			this(contents);
			if (width != null)
				setWidth(String.valueOf(width));
			if (height != null)
				setHeight(String.valueOf(height));
		}
	}

	private BaseWidget getButtonLayout() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		for (int i = 0; i < 10; i++) {
			Button button = new Button("Button " + i);

			button.setHeight("*");
			button.setWidth("100 px");
			layout.addMember(button);
		}
		return layout;
	}

	private Canvas getMainPanel() {
		final List<Tab> tabs = new ArrayList<Tab>();

		tabset = new TabSet();
		tabset.setSizeFull();

		final Tab tab = new Tab("premier");
		final Tab tab2 = new Tab("deuxieme");
		final Tab tab3 = new Tab("troisième");
		final Tab tab4 = new Tab("un autre");
		final Tab tab5 = new Tab("recursif");
		final Tab tab6 = new Tab("avec event");
		final Tab tab7 = new Tab("Fake border");

		tab.setPane(createForm(4));
		tab2.setPane(getEditableListGrid());
		VLayout vl = new VLayout();
		vl.setMembersMargin(4);
		vl.addMember(new Button("Press me 1!") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);

				tabset.selectTab(1);
			}
		});

		vl.addMember(new Button("Press me 2!") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);

				org.vaadin.smartgwt.server.Window window = new org.vaadin.smartgwt.server.Window(masterContainer);
				window.setTitle("Modal Window");
				window.setWidth(900);
				window.setHeight(700);
				window.setShowMinimizeButton(false);
				window.setShowResizer(true);
				window.setIsModal(true);
				window.setShowModalMask(true);
				window.setAutoCenter(true);
				window.setCanDragReposition(true);
				window.setCanDragResize(true);
				window.addItem(getMainPanel());
				window.show();
			}
		});

		vl.addMember(new Button("Press me 3!") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);
				tabset.removeTab(tab3);
			}
		});

		vl.addMember(new Button("Press me 4!"));

		vl.addMember(new Button("Add Tab") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);
				tabset.addTab(newTab("Title", newLabel("LABEL", "blue")));
			}
		});

		vl.addMember(new Button("Add Tab Index 0") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);
				tabset.addTab(newTab("Title", newLabel("INDEXED", "red")), 0);
			}
		});

		vl.addMember(new Button("Remove Tab") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);
				tabset.removeTab(tabset.getTabs()[tabset.getTabs().length - 1]);
			}
		});

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

		tabs.add(tab);
		tabs.add(tab2);
		tabs.add(tab3);
		tabs.add(tab4);
		tabs.add(tab5);
		tabs.add(tab6);
		tabs.add(tab7);
		tabs.add(newTab("sections", getStackView()));
		tabs.add(newTab("splitLayout", newSplitLayoutPane()));
		tabs.add(newTab("postCreationModification", newPostCreationModifiedLayout()));
		tabset.setTabs(tabs.toArray(new Tab[0]));
		return tabset;
	}

	private Layout getVertical() {
		final VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		for (int i = 1; i <= 10; i++) {
			final Button button = new Button("Button " + i) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void changeVariables(Object source, Map<String, Object> variables) {
					if (new Random().nextBoolean() == true) {
						layout.removeMember(this); // layout.getMembers()[0]);
					} else {
						layout.replaceMember(this, new Button("unclickable button"));
					}
				}
			};
			layout.addMember(button);
		}

		return layout;
	}

	private Tab newTab(String title, Canvas pane) {
		final Tab tab = new Tab(title);
		tab.setPane(pane);
		return tab;
	}

	private Canvas newSplitLayoutPane() {
		final VSplitLayout vLayout = new VSplitLayout();
		final HSplitLayout hLayout = new HSplitLayout();
		hLayout.setLeftCanvas(newLabel("LEFT", "blue"));
		hLayout.setRightCanvas(newLabel("RIGHT", "green"));
		vLayout.setTopCanvas(newLabel("TOP", "red"));
		vLayout.setBottomCanvas(hLayout);
		return vLayout;
	}

	private Label newLabel(String content, String bgColor) {
		final Label label = new Label(content);
		label.setBackgroundColor(bgColor);
		return label;
	}

	private Canvas newPostCreationModifiedLayout() {
		final VLayout layout = new VLayout();
		final Label yellow = newLabel("YELLOW", "yellow");
		layout.addMember(new Button("Load Layout") {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables) {
				super.changeVariables(source, variables);
				layout.removeMember(yellow);
				//				final Label blue = newLabel("BLUE", "blue");
				//				layout.addMember(newLabel("GREEN", "green"));
				//				layout.addMember(blue);
				//				layout.addMember(newLabel("RED", "red"));
				//				layout.removeMember(blue);
			}
		});
		layout.addMember(yellow);
		return layout;
	}

	public VLayout getSpecial() {
		VLayout vlayout = new VLayout();
		vlayout.setMargin(15);
		vlayout.setWidth("100%");
		vlayout.setMembersMargin(30);

		vlayout.addMember(createForm(4));
		Button button = new Button("Click to switch");
		button.setIcon("http://www.mricons.com/store/png/119004_36574_32_top_icon.png");
		button.setWidth(200);
		button.setActionType(SelectionType.CHECKBOX);

		vlayout.addMember(button);
		vlayout.addMember(createForm(6));

		return vlayout;
	}

	public DynamicForm createForm(int nbCols) {
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

		final FormItem tiEvent = new TextItem("blah" + i);
		tiEvent.setTitle("Edit Field " + i++);
		tiEvent.setValue("value!");
		tiEvent.setWidth("100%");

		final PickerIcon icon = new PickerIcon(PickerIcon.CLEAR);
		icon.addFormItemClickHandler(new FormItemClickHandler()
			{
				@Override
				public void onFormItemClick(FormItemIconClickEvent event)
				{
					tiEvent.setValue("");
				}
			});

		tiEvent.setIcons(icon);
		form.addField(tiEvent);

		FormItem ti = new TextItem("blah" + i);
		ti.setTitle("Edit Field " + i++);
		ti.setValue("value!");
		ti.setWidth("100%");
		form.addField(ti);

		ti = new TextItem("blah" + i);
		ti.setTitle("Edit Field " + i++);
		ti.setValue("value!");
		ti.setWidth("100%");
		form.addField(ti);

		SelectItem si = new SelectItem("blah" + i);
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");

		ListGridField countryCodeField = new ListGridField("countryCode", "Flag", 40);
		countryCodeField.setAlign(Alignment.CENTER);
		countryCodeField.setType(ListGridFieldType.IMAGE);
		countryCodeField.setImageURLPrefix("flags/16/");
		countryCodeField.setImageURLSuffix(".png");
		countryCodeField.setCanEdit(false);

		ListGridField nameField = new ListGridField("countryName", "Pays");
		ListGridField continentField = new ListGridField("continent", "Continent");
		ListGridField memberG8Field = new ListGridField("member_g8", "Member G8");
		ListGridField populationField = new ListGridField("population", "Population");
		populationField.setType(ListGridFieldType.INTEGER);
		// populationField.setCellFormatter(new NumericFormatter());
		ListGridField independenceField = new ListGridField("independence", "Independence");
		si.setPickListFields(countryCodeField, nameField, continentField, memberG8Field, populationField, independenceField);
		si.setOptionDataSource(CountryXmlDS.getInstance());
		form.addField(si);

		si = new SelectItem("blah" + i);
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addField(si);

		si = new SelectItem("blah" + i);
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addField(si);

		si = new SelectItem("blah" + i);
		si.setTitle("Autre Field " + i++);
		si.setWidth("100%");
		form.addField(si);

		DateItem di = new DateItem("blah" + i);
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		di.setUseTextField(true);
		form.addField(di);

		di = new DateItem("blah" + i);
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addField(di);

		di = new DateItem("blah" + i);
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		di.setUseTextField(true);
		form.addField(di);

		di = new DateItem("blah" + i);
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addField(di);

		di = new DateItem("blah" + i);
		di.setWidth("100%");
		di.setTitle("Edit Field " + i++);
		form.addField(di);

		ti = new TextItem("blah" + i);
		ti.setWidth("100%");
		ti.setTitle("Edit Field " + i++);
		form.addField(ti);

		ti = new TextItem("blah" + i);
		ti.setWidth("100%");
		ti.setTitle("Edit Field " + i++);
		form.addField(ti);

		return form;
	}

	public static <T> Iterable<T> iterate(final Enumeration<T> en) {
		final Iterator<T> iterator = new Iterator<T>() {
			@Override
			public boolean hasNext() {
				return en.hasMoreElements();
			}

			@Override
			public T next() {
				return en.nextElement();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return iterator;
			}
		};
	}

}
