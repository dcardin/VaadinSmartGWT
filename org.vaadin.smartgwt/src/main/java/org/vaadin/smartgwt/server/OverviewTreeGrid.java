package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.grid.events.SelectionChangedHandler;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;
import org.vaadin.smartgwt.server.tree.Tree;
import org.vaadin.smartgwt.server.tree.TreeGrid;
import org.vaadin.smartgwt.server.tree.TreeGridField;
import org.vaadin.smartgwt.server.tree.TreeNode;
import org.vaadin.smartgwt.server.types.Overflow;
import org.vaadin.smartgwt.server.types.SelectionStyle;
import org.vaadin.smartgwt.server.types.TreeModelType;

public class OverviewTreeGrid extends TreeGrid
{
	private String treeSelection = null;

	public OverviewTreeGrid()
	{
		setShowAllRecords(true);
		setBodyOverflow(Overflow.VISIBLE);
		setOverflow(Overflow.VISIBLE);
		setLeaveScrollbarGap(false);
		setSelectionType(SelectionStyle.SINGLE);
		setShowHeaderContextMenu(false);
		setCanResizeFields(true);
		setCanReorderFields(false);
		setShowHeader(false);
		setAutoSaveEdits(true);

		// create the fields needed for the tree view
		createFields();

		addSelectionChangedHandler(new SelectionChangedHandler()
			{
				@Override
				public void onSelectionChanged(SelectionEvent event)
				{
					getSelectedRecords();
				}
			});
	}

	/**
	 * 
	 */
	private void createFields()
	{
		TreeGridField field = new TreeGridField("name");
		field.setCanEdit(false);
		field.setCanFilter(false);
		field.setCanGroupBy(false);
		field.setCanToggle(false);

		setFields(field);
	}

	/**
	 * Completely remove the usage of icons for tree nodes
	 */
	protected String getIcon(Record node, boolean defaultState)
	{
		return null;
	}

//	/**
//	 * New data has arrived and we need to update the tree view
//	 * 
//	 * @param result
//	 */
//	private void processReturnData(ConfigurationResult result)
//	{
//		if (result == null)
//		{
//			SC.logWarn("Result is null");
//			return;
//		}
//		// Process properties first
//		List<Property> properties = result.getProperties();
//
//		if (properties == null || properties.size() == 0)
//			return;
//
//		// Configurable Info
//		List<ConfigurableInfo> infos = result.getInfos();
//		TreeNode[] list = new TreeNode[properties.size()];
//
//		for (int i = 0; i < infos.size(); i++)
//		{
//			TreeNode record = new TreeNode();
//
//			ConfigPropertyDataSource.copyInfoValues(infos.get(i), record);
//			list[i] = record;
//		}
//
//		Tree t = makeOverviewTree(list);
//		t.openAll();
//		setData(t);
//
//		if (treeSelection != null)
//		{
//			setSelectedPaths(treeSelection);
//		}
//	}

	/**
	 * Actually builds the tree to be displayed
	 * 
	 * @param list
	 * @return
	 */
	private Tree makeOverviewTree(TreeNode[] list)
	{
		Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("name");
		tree.setIdField("id");
		tree.setParentIdField("parent");
		tree.setShowRoot(false);
		tree.setData(list);
		return tree;
	}

}
