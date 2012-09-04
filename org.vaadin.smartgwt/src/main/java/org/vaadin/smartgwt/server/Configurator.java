package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.server.events.ClickEvent;
import org.vaadin.smartgwt.server.events.ClickHandler;
import org.vaadin.smartgwt.server.grid.ListGridRecord;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedHandler;
import org.vaadin.smartgwt.server.layout.HLayout;
import org.vaadin.smartgwt.server.layout.MasterContainer;
import org.vaadin.smartgwt.server.layout.VLayout;
import org.vaadin.smartgwt.server.toolbar.ToolStrip;
import org.vaadin.smartgwt.server.toolbar.ToolStripButton;
import org.vaadin.smartgwt.server.tree.TreeGrid;
import org.vaadin.smartgwt.server.types.Overflow;
import org.vaadin.smartgwt.server.types.SelectionType;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.netappsid.configurator.IConfigurator;

public class Configurator extends Window {
	private ConfigPropertyEditor cpe = null;
	private ToolStripButton reset = new ToolStripButton();
	private RendererPanel renderer = new RendererPanel();
	private ToolStripButton okButton;
	private ToolStripButton cancelButton;
	private final String servletContextPath;

	public Configurator(MasterContainer container, String servletContextPath) {
		super(container);
		this.servletContextPath = servletContextPath;
		init();
	}
	
	public HandlerRegistration addOKClickHandler(ClickHandler handler) {
		return okButton.addClickHandler(handler);
	}

	public HandlerRegistration addCancelClickHandler(ClickHandler handler) {
		return cancelButton.addClickHandler(handler);
	}

	private static ToolStripButton newButton(String title, String prompt, ClickHandler handler) {
		final ToolStripButton button = new ToolStripButton(title);
		button.addClickHandler(handler);
		button.setPrompt(prompt);
		return button;
	}

	ToolStrip createTopStrip() {
		ToolStrip strip = new ToolStrip();

		strip.addFill();
		strip.addButton(okButton = newButton("Save", "Save the item", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dispose();
			}
		}));

		strip.addButton(cancelButton = newButton("Cancel", "Ignore all changes", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dispose();
			}
		}));

		return strip;
	}

	private void init() {
		setTitle("Configurator for product PGM");
		setHeight("95%");
		setWidth("95%");
		setIsModal(true);
		setShowModalMask(true);
		setAutoCenter(true);
		setCanDragResize(true);
		setShowCloseButton(false);
		setShowMaximizeButton(false);
		setShowMinimizeButton(false);
		setShowHeader(false);
		setDismissOnEscape(true);

		VLayout vertical = new VLayout();
		vertical.setSizeFull();

		HLayout horizontal = new HLayout();
		horizontal.setSizeFull();

		renderer = new RendererPanel();
		renderer.setHeight("95%");
		renderer.setWidth("60%");
		renderer.setShowResizeBar(true);

		horizontal.addMember(renderer);
		horizontal.addMember(createPropertyPanel());

		vertical.addMember(createTopStrip());
		vertical.addMember(horizontal);

		addItem(vertical);
	}

	private VLayout createPropertyPanel() {
		VLayout panel = new VLayout();
		panel.setHeight("100%");
		panel.setWidth("40%");

		//		panel.addMember(createOverviewGrid());
		panel.addMember(createStrip());

		cpe = new ConfigPropertyEditor(renderer);
		cpe.setHeight("*");
		cpe.setBodyOverflow(Overflow.AUTO);

		cpe.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
			@Override
			public void onSelectionUpdated(SelectionUpdatedEvent event) {
				ListGridRecord[] nodes = cpe.getSelectedRecords();

				if (nodes.length > 0) {
					Double id = nodes[0].getAttributeAsDouble("id");
					Integer iid = (int) id.doubleValue();
					
					reset.setEnabled(cpe.isOverriden(iid.toString()));
					reset.requestRepaint();
				}
				else
				{
					reset.setEnabled(false);
					reset.requestRepaint();
				}
			}
		});

		panel.addMember(cpe);

		return panel;
	}

	public void show(String prd) {
		cpe.init(prd);
		super.show();
	}

	public void show(byte[] configurationBytes) {
		cpe.init(configurationBytes);
		super.show();
	}

	public IConfigurator getConfigurator() {
		return cpe.getConfigurator();
	}

	private ToolStrip createStrip() {
		ToolStrip strip = new ToolStrip();
		strip.setMargin(2);
		strip.setWidth100();
		strip.setAutoHeight();
		strip.setPrompt("Return to default value");

		reset.setEnabled(false);
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] nodes = cpe.getSelectedRecords();

				Double id = nodes[0].getAttributeAsDouble("id");
				Integer iid = (int) id.doubleValue();
				cpe.resetOverride(iid.toString());
			}
		});

		reset.setIconSize(16);
		reset.setIcon(servletContextPath + "/img/last_edit_pos.gif");
		reset.setActionType(SelectionType.BUTTON);
		strip.setWidth100();
		strip.addMember(reset);

		return strip;
	}

	private TreeGrid createOverviewGrid() {
		TreeGrid grid = new OverviewTreeGrid();
		grid.setHeight("70");
		grid.setWidth("100%");

		return grid;
	}
}
