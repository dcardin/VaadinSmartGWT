package org.vaadin.smartgwt;

import java.util.Map;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.ConfigPropertyEditor;
import org.vaadin.smartgwt.server.IButton;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.layout.HSplitLayout;
import org.vaadin.smartgwt.server.layout.MasterContainer;
import org.vaadin.smartgwt.server.layout.VLayout;
import org.vaadin.smartgwt.server.toolbar.ToolStrip;
import org.vaadin.smartgwt.server.tree.TreeGrid;

public class Configurator extends VLayout
{
	private ConfigPropertyEditor cpe = null;

	public Configurator(MasterContainer container)
	{
		//super(container);
		setSizeFull();
		
		HSplitLayout main = new HSplitLayout();
		main.setSizeFull();
		main.setProportions(0.7d, 0.3d);
		
		RendererPanel renderer = new RendererPanel();
		renderer.setHeight100();
		
		main.setLeftCanvas(renderer);
		main.setRightCanvas(createPropertyPanel());
		
		//addItem(main);
		addMember(main);
	}
	
	private VLayout createPropertyPanel()
	{
		VLayout panel = new VLayout();
		panel.setHeight100();
		
		panel.addMember(createOverviewGrid());
		panel.addMember(createStrip());

		cpe = new ConfigPropertyEditor();
		cpe.setHeight("*");
		panel.addMember(cpe);
		
		return panel;
	}
	
	public void show(String prd)
	{
		cpe.init(prd);
		//super.show();
	}

	public Canvas getPropertyGrid()
	{
		VLayout layout = new VLayout();

		cpe.setWidth("600");
		cpe.setHeight100();

		layout.addMember(cpe);
		return layout;
	}

	private ToolStrip createStrip()
	{
		ToolStrip strip = new ToolStrip();
		strip.setMargin(2);
		strip.setWidth100();
		
		IButton reset = new IButton() {
			@Override
			public void changeVariables(Object source, Map<String, Object> variables)
			{
				super.changeVariables(source, variables);
			}
		};
		
		reset.setIconSize(16);
		reset.setShowRollOver(false);
		reset.setIcon("img/last_edit_pos.gif");

		strip.addMember(reset);

		return strip;
	}

	private TreeGrid createOverviewGrid()
	{
		TreeGrid grid = new OverviewTreeGrid();
		grid.setHeight("15%");
		return grid;
	}

}
