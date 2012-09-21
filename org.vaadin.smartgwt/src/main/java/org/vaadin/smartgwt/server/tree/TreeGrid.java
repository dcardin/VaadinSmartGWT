package org.vaadin.smartgwt.server.tree;

import org.vaadin.smartgwt.client.ui.tree.VTreeGrid;
import org.vaadin.smartgwt.server.core.ComponentReference;
import org.vaadin.smartgwt.server.grid.ListGrid;
import org.vaadin.smartgwt.server.types.AnimationAcceleration;
import org.vaadin.smartgwt.server.types.DisplayNodeType;
import org.vaadin.smartgwt.server.types.FetchMode;
import org.vaadin.smartgwt.server.types.TextMatchStyle;
import org.vaadin.smartgwt.server.util.EnumUtil;

/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

/**
 * The Smart GWT system supports hierarchical data (also referred to as tree data due to its "branching" organization)
 * with: <ul>   <li> the {@link com.smartgwt.client.widgets.tree.Tree} class, which manipulates hierarchical data sets  
 * <li> the TreeGrid widget class, which extends the ListGrid class to visually        present tree data in an
 * expandable/collapsible format. </ul> For information on DataBinding Trees, see {@link
 * com.smartgwt.client.docs.TreeDataBinding}. <p> A TreeGrid works just like a {@link
 * com.smartgwt.client.widgets.grid.ListGrid}, except one column (specified by {@link
 * com.smartgwt.client.widgets.tree.TreeGridField#getTreeField treeField}) shows a hierarchical {@link
 * com.smartgwt.client.widgets.tree.Tree}.  A TreeGrid is not limited to displaying just the {@link
 * com.smartgwt.client.widgets.tree.Tree} column - you can define additional columns (via {@link
 * com.smartgwt.client.widgets.tree.TreeGrid#getFields fields}) which will render just like the columns of a {@link
 * com.smartgwt.client.widgets.grid.ListGrid}, and support all of the functionality of ListGrid columns, such as {@link
 * com.smartgwt.client.widgets.grid.ListGridField#formatCellValue formatters}. <p> Except where explicitly overridden,
 * {@link com.smartgwt.client.widgets.grid.ListGrid} methods, callbacks, and properties apply to TreeGrids as well.  The
 * {@link com.smartgwt.client.widgets.grid.ListGrid} defines some methods as taking/returning {@link
 * com.smartgwt.client.widgets.grid.ListGridField} and {@link com.smartgwt.client.widgets.grid.ListGridRecord}.  When using
 * those methods in a TreeGrid, those types will be {@link com.smartgwt.client.widgets.tree.TreeGridField} and {@link
 * com.smartgwt.client.widgets.tree.TreeNode}, respectively.
 */
// @formatter:off
@com.vaadin.ui.ClientWidget(VTreeGrid.class)
public class TreeGrid extends ListGrid {  

	private ComponentReference<Tree> data = propertyPainter.addProperty("data");

    public TreeGrid(){
        setAnimateFolderSpeed(3000);setAlternateRecordStyles(false);
				scClassName = "TreeGrid";
    }

    // ********************* Properties / Attributes ***********************

    /**
     * When animating folder opening / closing, this property can be set to apply an animated acceleration effect. This allows
     * the animation speed to be "weighted", for example expanding or collapsing at a faster rate toward the beginning of the
     * animation than at the end.
     *
     * @param animateFolderEffect animateFolderEffect Default value is null
     */
    public void setAnimateFolderEffect(AnimationAcceleration animateFolderEffect) {
        setAttribute("animateFolderEffect", animateFolderEffect.getValue(), true);
    }

    /**
     * When animating folder opening / closing, this property can be set to apply an animated acceleration effect. This allows
     * the animation speed to be "weighted", for example expanding or collapsing at a faster rate toward the beginning of the
     * animation than at the end.
     *
     *
     * @return AnimationAcceleration
     */
    public AnimationAcceleration getAnimateFolderEffect()  {
        return EnumUtil.getEnum(AnimationAcceleration.values(), getAttribute("animateFolderEffect"));
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolders animateFolders} is true for this grid, this number
     * can be set to designate the maximum number of rows to animate at a time when opening / closing a folder.
     *
     * @param animateFolderMaxRows animateFolderMaxRows Default value is null
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderMaxRows
     */
    public void setAnimateFolderMaxRows(Integer animateFolderMaxRows) {
        setAttribute("animateFolderMaxRows", animateFolderMaxRows, true);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolders animateFolders} is true for this grid, this number
     * can be set to designate the maximum number of rows to animate at a time when opening / closing a folder.
     *
     *
     * @return If {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolders animateFolders} is true for this treeGrid, this
     * method returns the  the maximum number of rows to animate at a time when opening / closing a folder. This method will
     * return {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderMaxRows animateFolderMaxRows} if set. Otherwise
     * the value will be calculated as 3x the number of rows required to fill a viewport, capped at a maximum value of 75.
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderMaxRows
     */
    public Integer getAnimateFolderMaxRows()  {
        return getAttributeAsInt("animateFolderMaxRows");
    }

    /**
     * If true, when folders are opened / closed children will be animated into view.
     *
     * @param animateFolders animateFolders Default value is true
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_animation_tree" target="examples">Tree Folders Example</a>
     */
    public void setAnimateFolders(Boolean animateFolders) {
        setAttribute("animateFolders", animateFolders, true);
    }

    /**
     * If true, when folders are opened / closed children will be animated into view.
     *
     *
     * @return Boolean
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_animation_tree" target="examples">Tree Folders Example</a>
     */
    public Boolean getAnimateFolders()  {
        return getAttributeAsBoolean("animateFolders");
    }

    /**
     * When animating folder opening / closing, this property designates the speed of the animation in pixels shown (or hidden)
     * per second. Takes precedence over the  {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderTime
     * animateFolderTime} property, which allows the developer to specify a duration for the animation rather than a speed.
     *
     * @param animateFolderSpeed animateFolderSpeed Default value is 3000
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setAnimateFolderTime
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_animation_tree" target="examples">Tree Folders Example</a>
     */
    public void setAnimateFolderSpeed(int animateFolderSpeed) {
        setAttribute("animateFolderSpeed", animateFolderSpeed, true);
    }

    /**
     * When animating folder opening / closing, this property designates the speed of the animation in pixels shown (or hidden)
     * per second. Takes precedence over the  {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderTime
     * animateFolderTime} property, which allows the developer to specify a duration for the animation rather than a speed.
     *
     *
     * @return int
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderTime
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_animation_tree" target="examples">Tree Folders Example</a>
     */
    public int getAnimateFolderSpeed()  {
        return getAttributeAsInt("animateFolderSpeed");
    }

    /**
     * When animating folder opening / closing, if {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderSpeed
     * animateFolderSpeed} is not set, this property designates the duration of the animation in ms.
     *
     * @param animateFolderTime animateFolderTime Default value is 100
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setAnimateFolderSpeed
     */
    public void setAnimateFolderTime(int animateFolderTime) {
        setAttribute("animateFolderTime", animateFolderTime, true);
    }

    /**
     * When animating folder opening / closing, if {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderSpeed
     * animateFolderSpeed} is not set, this property designates the duration of the animation in ms.
     *
     *
     * @return int
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderSpeed
     */
    public int getAnimateFolderTime()  {
        return getAttributeAsInt("animateFolderTime");
    }

    /**
     * With {@link com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand}:true, TreeGrids fetch data
     * by selecting the child nodes of each parent, which should be exact match, so we default to
     * <code>autoFetchTextMatchStyle:"exact"</code>. See {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle autoFetchTextMatchStyle} for details.
     *
     * @param autoFetchTextMatchStyle autoFetchTextMatchStyle Default value is "exact"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setAutoFetchTextMatchStyle(TextMatchStyle autoFetchTextMatchStyle)  throws IllegalStateException {
        setAttribute("autoFetchTextMatchStyle", autoFetchTextMatchStyle.getValue(), false);
    }

    /**
     * With {@link com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand}:true, TreeGrids fetch data
     * by selecting the child nodes of each parent, which should be exact match, so we default to
     * <code>autoFetchTextMatchStyle:"exact"</code>. See {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle autoFetchTextMatchStyle} for details.
     *
     *
     * @return TextMatchStyle
     */
    public TextMatchStyle getAutoFetchTextMatchStyle()  {
        return EnumUtil.getEnum(TextMatchStyle.values(), getAttribute("autoFetchTextMatchStyle"));
    }

    /**
     * Indicates whether records can be dropped into this listGrid.
     *
     * @param canAcceptDroppedRecords canAcceptDroppedRecords Default value is false
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwtee/showcase/#tree_reparent_sql" target="examples">Tree Reparent Example</a>
     */
    public void setCanAcceptDroppedRecords(Boolean canAcceptDroppedRecords) {
        setAttribute("canAcceptDroppedRecords", canAcceptDroppedRecords, true);
    }

    /**
     * Indicates whether records can be dropped into this listGrid.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwtee/showcase/#tree_reparent_sql" target="examples">Tree Reparent Example</a>
     */
    public Boolean getCanAcceptDroppedRecords()  {
        return getAttributeAsBoolean("canAcceptDroppedRecords");
    }

    /**
     * Indicates whether records can be dragged from this listGrid and dropped elsewhere.
     *
     * @param canDragRecordsOut canDragRecordsOut Default value is false
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_interaction_drop_events" target="examples">Drop Events Example</a>
     */
    public void setCanDragRecordsOut(Boolean canDragRecordsOut) {
        setAttribute("canDragRecordsOut", canDragRecordsOut, true);
    }

    /**
     * Indicates whether records can be dragged from this listGrid and dropped elsewhere.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_interaction_drop_events" target="examples">Drop Events Example</a>
     */
    public Boolean getCanDragRecordsOut()  {
        return getAttributeAsBoolean("canDragRecordsOut");
    }

    /**
     * Whether drops are allowed on leaf nodes. <P> Dropping is ordinarily not allowed on leaf nodes unless {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCanReorderRecords canReorderRecords} is set.   <P> The default action for a
     * drop on a leaf node is to place the node in that leaf's parent folder.  This can be customized by overriding {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#folderDrop TreeGrid.folderDrop}. <P> Note that enabling
     * <code>canDropOnLeaves</code> is usually only appropriate where you intend to add a custom {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#folderDrop TreeGrid.folderDrop} implementation that <b>does not</b> add a
     * child node under the leaf.  If you want to add a child nodes to a leaf, instead of enabling canDropOnLeaves, use empty
     * folders instead - see {@link com.smartgwt.client.widgets.tree.Tree#isFolder Tree.isFolder} for how to control whether a
     * node is considered a folder.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param canDropOnLeaves canDropOnLeaves Default value is false
     */
    public void setCanDropOnLeaves(Boolean canDropOnLeaves) {
        setAttribute("canDropOnLeaves", canDropOnLeaves, true);
    }

    /**
     * Whether drops are allowed on leaf nodes. <P> Dropping is ordinarily not allowed on leaf nodes unless {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCanReorderRecords canReorderRecords} is set.   <P> The default action for a
     * drop on a leaf node is to place the node in that leaf's parent folder.  This can be customized by overriding {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#folderDrop TreeGrid.folderDrop}. <P> Note that enabling
     * <code>canDropOnLeaves</code> is usually only appropriate where you intend to add a custom {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#folderDrop TreeGrid.folderDrop} implementation that <b>does not</b> add a
     * child node under the leaf.  If you want to add a child nodes to a leaf, instead of enabling canDropOnLeaves, use empty
     * folders instead - see {@link com.smartgwt.client.widgets.tree.Tree#isFolder Tree.isFolder} for how to control whether a
     * node is considered a folder.
     *
     *
     * @return Boolean
     */
    public Boolean getCanDropOnLeaves()  {
        return getAttributeAsBoolean("canDropOnLeaves");
    }

    /**
     * Indicates whether records can be reordered by dragging within this listGrid.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param canReorderRecords canReorderRecords Default value is false
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwtee/showcase/#tree_reparent_sql" target="examples">Tree Reparent Example</a>
     */
    public void setCanReorderRecords(Boolean canReorderRecords) {
        setAttribute("canReorderRecords", canReorderRecords, true);
    }

    /**
     * Indicates whether records can be reordered by dragging within this listGrid.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwtee/showcase/#tree_reparent_sql" target="examples">Tree Reparent Example</a>
     */
    public Boolean getCanReorderRecords()  {
        return getAttributeAsBoolean("canReorderRecords");
    }

    /**
     * If set this property allows the user to reparent nodes by dragging them from their current folder to a new folder.<br>
     * <b>Backcompat:</b> For backwards compatibility with versions prior to Smart GWT 1.5, if this property is unset, but
     * <code>this.canAcceptDroppedRecords</code> is true, we allow nodes to be dragged to different folders.
     *
     * @param canReparentNodes canReparentNodes Default value is null
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#setCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     */
    public void setCanReparentNodes(Boolean canReparentNodes) {
        setAttribute("canReparentNodes", canReparentNodes, true);
    }

    /**
     * If set this property allows the user to reparent nodes by dragging them from their current folder to a new folder.<br>
     * <b>Backcompat:</b> For backwards compatibility with versions prior to Smart GWT 1.5, if this property is unset, but
     * <code>this.canAcceptDroppedRecords</code> is true, we allow nodes to be dragged to different folders.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanDrag
     * @see com.smartgwt.client.widgets.tree.TreeNode#getCanAcceptDrop
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     */
    public Boolean getCanReparentNodes()  {
        return getAttributeAsBoolean("canReparentNodes");
    }

    /**
     * Message displayed when user attempts to drop a node into a child of itself.
     *
     * @param cantDragIntoChildMessage cantDragIntoChildMessage Default value is "You can't drag an item into one of it's children."
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanReorderRecords
     */
    public void setCantDragIntoChildMessage(String cantDragIntoChildMessage)  throws IllegalStateException {
        setAttribute("cantDragIntoChildMessage", cantDragIntoChildMessage, false);
    }

    /**
     * Message displayed when user attempts to drop a node into a child of itself.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanReorderRecords
     */
    public String getCantDragIntoChildMessage()  {
        return getAttributeAsString("cantDragIntoChildMessage");
    }

    /**
     * Message displayed when user attempts to drop a dragged node onto itself.
     *
     * @param cantDragIntoSelfMessage cantDragIntoSelfMessage Default value is "You can't drag an item into itself."
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanReorderRecords
     */
    public void setCantDragIntoSelfMessage(String cantDragIntoSelfMessage)  throws IllegalStateException {
        setAttribute("cantDragIntoSelfMessage", cantDragIntoSelfMessage, false);
    }

    /**
     * Message displayed when user attempts to drop a dragged node onto itself.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanReorderRecords
     */
    public String getCantDragIntoSelfMessage()  {
        return getAttributeAsString("cantDragIntoSelfMessage");
    }

    /**
     * Should children be selected when parent is selected? And should parent be selected when any child is selected?
     *
     * @param cascadeSelection cascadeSelection Default value is false
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setCascadeSelection(Boolean cascadeSelection)  throws IllegalStateException {
        setAttribute("cascadeSelection", cascadeSelection, false);
    }

    /**
     * Should children be selected when parent is selected? And should parent be selected when any child is selected?
     *
     *
     * @return Boolean
     */
    public Boolean getCascadeSelection()  {
        return getAttributeAsBoolean("cascadeSelection");
    }

    /**
     * This suffix will be appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} for
     * closed folders. If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons} is set to
     * <code>false</code> this suffix will also be appended to open folders' icons.
     *
     * @param closedIconSuffix closedIconSuffix Default value is "closed"
     */
    public void setClosedIconSuffix(String closedIconSuffix) {
        setAttribute("closedIconSuffix", closedIconSuffix, true);
    }

    /**
     * This suffix will be appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} for
     * closed folders. If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons} is set to
     * <code>false</code> this suffix will also be appended to open folders' icons.
     *
     *
     * @return String
     */
    public String getClosedIconSuffix()  {
        return getAttributeAsString("closedIconSuffix");
    }

    /**
     * The base filename for connector icons shown when {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors
     * showConnectors} is true. Connector icons are rendered into the title field of each row and show the dotted hierarchy
     * lines between siblings of the same parent node. For each node, a connector icon may be shown:<ul> <li>As an opener icon
     * for folder nodes, next to the folder icon</li> <li>In place of an opener icon for leaf nodes, next to the leaf icon</li>
     * <li>As a standalone vertical continuation line in the indent to the left of the node, to show     a connection between
     * some ancestor node's siblings (only relevant if     {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} is true).</li> </ul> Note that
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} governs whether connector
     * lines will be displayed for all indent levels, or just for the innermost level of the tree. <P> The filenames for these
     * icons are assembled from this base filename and the state of the node.  Assuming the connectorImage is set to
     * <code>{baseName}.{extension}</code>, the full set of images to be displayed will be: <P>
     * <code>{baseName}_ancestor[_rtl].{extension}</code> if {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors}  is true, this is the URL for the
     * vertical continuation image to be displayed at the  appropriate indent levels for ancestor nodes with subsequent
     * children. <P> For nodes with no children: <ul> <li><code>{baseName}_single[_rtl].{extension}</code>: Shown when there is
     * no connector line  attached to the parent or previous sibling, and no connector line to the next sibling. For  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors:true} trees, there will always be a 
     * connector leading to the parent or previous sibling if its present in the tree so this  icon can only be displayed for
     * the first row.</li> <li><code>{baseName}_start[_rtl].{extension}</code>:  Shown when the there is no connector  line
     * attached to the parent or previous sibling, but there is a connector to the next  sibling. As with <code>_single</code>
     * this will only ever be used for the first row if  {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors
     * showFullConnectors} is true</li> <li><code>{baseName}_end[_rtl].{extension}</code>:  Shown if we are not showing a
     * connector   line attached to the next sibling of this node (but are showing a connection to the previous  sibling or
     * parent).</li> <li><code>{baseName}_middle[_rtl].{extension}</code>:  Shown where the we have a connector  line leading
     * to both the previous sibling (or parent) and the next sibling. </ul> For folders with children. Note that if {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} is false, open folders will never
     * show a connector to subsequent siblings: <ul> <li><code>{baseName}_opened_single[_rtl].{extension}</code> opened folder
     * node with   children when no connector line is shown attaching to either the folder's previous sibling  or parent, or to
     * any subsequent siblings.</li> <li><code>{baseName}_opened_start[_rtl].{extension}</code>:  opened folder with children 
     * when the there is no connector line attached to the parent or previous sibling, but there   is a connector to the next
     * sibling.</li> <li><code>{baseName}_opened_end[_rtl].{extension}</code>:  opened folder with children   if we are not
     * showing a connector line attached to the next sibling of this node (but are  showing a connection to the previous
     * sibling or parent).</li> <li><code>{baseName}_opened_middle[_rtl].{extension}</code>: opened folder with children  
     * where the we have a connector line leading to both the previous sibling (or parent) and the  next sibling. </ul> <ul>
     * <li><code>{baseName}_closed_single[_rtl].{extension}</code> closed folder node with   children when no connector line is
     * shown attaching to either the folder's previous sibling  or parent, or to any subsequent siblings.</li>
     * <li><code>{baseName}_closed_start[_rtl].{extension}</code>:  closed folder with children  when the there is no connector
     * line attached to the parent or previous sibling, but there   is a connector to the next sibling.</li>
     * <li><code>{baseName}_closed_end[_rtl].{extension}</code>:  closed folder with children   if we are not showing a
     * connector line attached to the next sibling of this node (but are  showing a connection to the previous sibling or
     * parent).</li> <li><code>{baseName}_closed_middle[_rtl].{extension}</code>: closed folder with children   where the we
     * have a connector line leading to both the previous sibling (or parent) and the  next sibling. </ul> (Note '[_rtl]' means
     * that "_rtl" will be attached if isRTL() is true for this widget).
     *
     * @param connectorImage connectorImage Default value is "[SKIN]connector.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setConnectorImage(String connectorImage)  throws IllegalStateException {
        setAttribute("connectorImage", connectorImage, false);
    }

    /**
     * The base filename for connector icons shown when {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors
     * showConnectors} is true. Connector icons are rendered into the title field of each row and show the dotted hierarchy
     * lines between siblings of the same parent node. For each node, a connector icon may be shown:<ul> <li>As an opener icon
     * for folder nodes, next to the folder icon</li> <li>In place of an opener icon for leaf nodes, next to the leaf icon</li>
     * <li>As a standalone vertical continuation line in the indent to the left of the node, to show     a connection between
     * some ancestor node's siblings (only relevant if     {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} is true).</li> </ul> Note that
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} governs whether connector
     * lines will be displayed for all indent levels, or just for the innermost level of the tree. <P> The filenames for these
     * icons are assembled from this base filename and the state of the node.  Assuming the connectorImage is set to
     * <code>{baseName}.{extension}</code>, the full set of images to be displayed will be: <P>
     * <code>{baseName}_ancestor[_rtl].{extension}</code> if {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors}  is true, this is the URL for the
     * vertical continuation image to be displayed at the  appropriate indent levels for ancestor nodes with subsequent
     * children. <P> For nodes with no children: <ul> <li><code>{baseName}_single[_rtl].{extension}</code>: Shown when there is
     * no connector line  attached to the parent or previous sibling, and no connector line to the next sibling. For  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors:true} trees, there will always be a 
     * connector leading to the parent or previous sibling if its present in the tree so this  icon can only be displayed for
     * the first row.</li> <li><code>{baseName}_start[_rtl].{extension}</code>:  Shown when the there is no connector  line
     * attached to the parent or previous sibling, but there is a connector to the next  sibling. As with <code>_single</code>
     * this will only ever be used for the first row if  {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors
     * showFullConnectors} is true</li> <li><code>{baseName}_end[_rtl].{extension}</code>:  Shown if we are not showing a
     * connector   line attached to the next sibling of this node (but are showing a connection to the previous  sibling or
     * parent).</li> <li><code>{baseName}_middle[_rtl].{extension}</code>:  Shown where the we have a connector  line leading
     * to both the previous sibling (or parent) and the next sibling. </ul> For folders with children. Note that if {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getShowFullConnectors showFullConnectors} is false, open folders will never
     * show a connector to subsequent siblings: <ul> <li><code>{baseName}_opened_single[_rtl].{extension}</code> opened folder
     * node with   children when no connector line is shown attaching to either the folder's previous sibling  or parent, or to
     * any subsequent siblings.</li> <li><code>{baseName}_opened_start[_rtl].{extension}</code>:  opened folder with children 
     * when the there is no connector line attached to the parent or previous sibling, but there   is a connector to the next
     * sibling.</li> <li><code>{baseName}_opened_end[_rtl].{extension}</code>:  opened folder with children   if we are not
     * showing a connector line attached to the next sibling of this node (but are  showing a connection to the previous
     * sibling or parent).</li> <li><code>{baseName}_opened_middle[_rtl].{extension}</code>: opened folder with children  
     * where the we have a connector line leading to both the previous sibling (or parent) and the  next sibling. </ul> <ul>
     * <li><code>{baseName}_closed_single[_rtl].{extension}</code> closed folder node with   children when no connector line is
     * shown attaching to either the folder's previous sibling  or parent, or to any subsequent siblings.</li>
     * <li><code>{baseName}_closed_start[_rtl].{extension}</code>:  closed folder with children  when the there is no connector
     * line attached to the parent or previous sibling, but there   is a connector to the next sibling.</li>
     * <li><code>{baseName}_closed_end[_rtl].{extension}</code>:  closed folder with children   if we are not showing a
     * connector line attached to the next sibling of this node (but are  showing a connection to the previous sibling or
     * parent).</li> <li><code>{baseName}_closed_middle[_rtl].{extension}</code>: closed folder with children   where the we
     * have a connector line leading to both the previous sibling (or parent) and the  next sibling. </ul> (Note '[_rtl]' means
     * that "_rtl" will be attached if isRTL() is true for this widget).
     *
     *
     * @return String
     */
    public String getConnectorImage()  {
        return getAttributeAsString("connectorImage");
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getShowDropIcon
     * default node.showDropIcon} property.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param customIconDropProperty customIconDropProperty Default value is "showDropIcon"
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCustomIconProperty
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setShowCustomIconDrop
     */
    public void setCustomIconDropProperty(String customIconDropProperty) {
        setAttribute("customIconDropProperty", customIconDropProperty, true);
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getShowDropIcon
     * default node.showDropIcon} property.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getShowCustomIconDrop
     */
    public String getCustomIconDropProperty()  {
        return getAttributeAsString("customIconDropProperty");
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getShowOpenIcon
     * default node.showOpenIcon} property.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param customIconOpenProperty customIconOpenProperty Default value is "showOpenIcon"
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCustomIconProperty
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setShowCustomIconOpen
     */
    public void setCustomIconOpenProperty(String customIconOpenProperty) {
        setAttribute("customIconOpenProperty", customIconOpenProperty, true);
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getShowOpenIcon
     * default node.showOpenIcon} property.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getShowCustomIconOpen
     */
    public String getCustomIconOpenProperty()  {
        return getAttributeAsString("customIconOpenProperty");
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getIcon default
     * node.icon} property.
     *
     * @param customIconProperty customIconProperty Default value is "icon"
     */
    public void setCustomIconProperty(String customIconProperty) {
        setAttribute("customIconProperty", customIconProperty, true);
    }

    /**
     * This property allows the developer to rename the  {@link com.smartgwt.client.widgets.tree.TreeNode#getIcon default
     * node.icon} property.
     *
     *
     * @return String
     */
    public String getCustomIconProperty()  {
        return getAttributeAsString("customIconProperty");
    }

    /**
     * A {@link com.smartgwt.client.widgets.tree.Tree} object containing of nested {@link
     * com.smartgwt.client.widgets.tree.TreeNode}s to  display as rows in this TreeGrid.   The <code>data</code> property will
     * typically not be explicitly specified for  databound TreeGrids, where the data is returned from the server via databound
     * component methods such as <code>fetchData()</code>
     *
     * <br><br>If this method is called after the component has been drawn/initialized:
     * Set the {@link com.smartgwt.client.widgets.tree.Tree} object this TreeGrid will view and manipulate.
     *
     * @param data Tree to show. Default value is null
     */
    public void setData(Tree data) {
    	this.data.set(data);
    }

    /**
     * Mode of fetching records from server. <P> In a ResultTree, "basic" fetchMode implies that if search criteria change, the
     * entire tree will be discarded and re-fetched from the server. {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getOpenState TreeGrid.getOpenState} will be preserved. <P> fetchMode:"local"
     * implies that local filtering will be performed. See {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getKeepParentsOnFilter keepParentsOnFilter} for additional filtering details.
     * <P> fetchMode:"paged" does not apply to ResultTrees. Instead, {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand} is used for folder-by-folder loading of
     * tree data. If enough nodes exist that paging is desirable within a folder, a better UI can be obtained by showing an
     * adjacent ListGrid (similar to Outlook email) to show a large number of child nodes.
     *
     * @param dataFetchMode dataFetchMode Default value is "basic"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setDataFetchMode(FetchMode dataFetchMode)  throws IllegalStateException {
        setAttribute("dataFetchMode", dataFetchMode.getValue(), false);
    }

    /**
     * Mode of fetching records from server. <P> In a ResultTree, "basic" fetchMode implies that if search criteria change, the
     * entire tree will be discarded and re-fetched from the server. {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getOpenState TreeGrid.getOpenState} will be preserved. <P> fetchMode:"local"
     * implies that local filtering will be performed. See {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getKeepParentsOnFilter keepParentsOnFilter} for additional filtering details.
     * <P> fetchMode:"paged" does not apply to ResultTrees. Instead, {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand} is used for folder-by-folder loading of
     * tree data. If enough nodes exist that paging is desirable within a folder, a better UI can be obtained by showing an
     * adjacent ListGrid (similar to Outlook email) to show a large number of child nodes.
     *
     *
     * @return FetchMode
     */
    public FetchMode getDataFetchMode()  {
        return EnumUtil.getEnum(FetchMode.values(), getAttribute("dataFetchMode"));
    }

    /**
     * Specifies the type of nodes displayed in the treeGrid.
     *
     * @param displayNodeType displayNodeType Default value is Tree.FOLDERS_AND_LEAVES
     * @see com.smartgwt.client.types.DisplayNodeType
     */
    public void setDisplayNodeType(DisplayNodeType displayNodeType) {
        setAttribute("displayNodeType", displayNodeType.getValue(), true);
    }

    /**
     * Specifies the type of nodes displayed in the treeGrid.
     *
     *
     * @return DisplayNodeType
     * @see com.smartgwt.client.types.DisplayNodeType
     */
    public DisplayNodeType getDisplayNodeType()  {
        return EnumUtil.getEnum(DisplayNodeType.values(), getAttribute("displayNodeType"));
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowDropIcons showDropIcons} is true, this suffix will be
     * appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} when the user drop-hovers
     * over some folder.
     *
     * @param dropIconSuffix dropIconSuffix Default value is "drop"
     */
    public void setDropIconSuffix(String dropIconSuffix) {
        setAttribute("dropIconSuffix", dropIconSuffix, true);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowDropIcons showDropIcons} is true, this suffix will be
     * appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} when the user drop-hovers
     * over some folder.
     *
     *
     * @return String
     */
    public String getDropIconSuffix()  {
        return getAttributeAsString("dropIconSuffix");
    }

    /**
     * The amount of gap (in pixels) between the extraIcon (see {@link com.smartgwt.client.widgets.tree.TreeGrid#getExtraIcon
     * TreeGrid.getExtraIcon}) or checkbox icon and the {@link com.smartgwt.client.widgets.tree.TreeGrid#getNodeIcon nodeIcon}/
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} or node text.
     *
     * @param extraIconGap extraIconGap Default value is 2
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public void setExtraIconGap(int extraIconGap)  throws IllegalStateException {
        setAttribute("extraIconGap", extraIconGap, false);
    }

    /**
     * The amount of gap (in pixels) between the extraIcon (see {@link com.smartgwt.client.widgets.tree.TreeGrid#getExtraIcon
     * TreeGrid.getExtraIcon}) or checkbox icon and the {@link com.smartgwt.client.widgets.tree.TreeGrid#getNodeIcon nodeIcon}/
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} or node text.
     *
     *
     * @return int
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public int getExtraIconGap()  {
        return getAttributeAsInt("extraIconGap");
    }

    /**
     * The URL of the base icon for all folder nodes in this treeGrid. Note that this URL will have {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix}, {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix} or  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} appended to indicate state changes if
     * appropriate -  see documentation on  {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons}
     * and {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowDropIcons showDropIcons}.
     *
     * @param folderIcon folderIcon Default value is "[SKIN]folder.gif"
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setFolderIcon(String folderIcon) {
        setAttribute("folderIcon", folderIcon, true);
    }

    /**
     * The URL of the base icon for all folder nodes in this treeGrid. Note that this URL will have {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix}, {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix} or  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} appended to indicate state changes if
     * appropriate -  see documentation on  {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons}
     * and {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowDropIcons showDropIcons}.
     *
     *
     * @return String
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public String getFolderIcon()  {
        return getAttributeAsString("folderIcon");
    }

    /**
     * The standard size (same height and width, in pixels) of node icons in this          treeGrid.
     *
     * @param iconSize iconSize Default value is 16
     */
    public void setIconSize(int iconSize) {
        setAttribute("iconSize", iconSize, true);
    }

    /**
     * The standard size (same height and width, in pixels) of node icons in this          treeGrid.
     *
     *
     * @return int
     */
    public int getIconSize()  {
        return getAttributeAsInt("iconSize");
    }

    /**
     * For record components placed "within" the {@link com.smartgwt.client.widgets.tree.TreeGridField#getTreeField treeField}
     * column, should the component be indented to the position where a title would normally show? <P> For more general
     * placement of embedded components, see {@link com.smartgwt.client.widgets.grid.ListGrid#addEmbeddedComponent
     * addEmbeddedComponent}.
     *
     * @param indentRecordComponents indentRecordComponents Default value is true
     */
    public void setIndentRecordComponents(Boolean indentRecordComponents) {
        setAttribute("indentRecordComponents", indentRecordComponents, true);
    }

    /**
     * For record components placed "within" the {@link com.smartgwt.client.widgets.tree.TreeGridField#getTreeField treeField}
     * column, should the component be indented to the position where a title would normally show? <P> For more general
     * placement of embedded components, see {@link com.smartgwt.client.widgets.grid.ListGrid#addEmbeddedComponent
     * addEmbeddedComponent}.
     *
     *
     * @return Boolean
     */
    public Boolean getIndentRecordComponents()  {
        return getAttributeAsBoolean("indentRecordComponents");
    }

    /**
     * The amount of indentation (in pixels) to add to a node's icon/title for each level down in this tree's hierarchy. <p>
     * This value is ignored when {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors} is
     * <code>true</code> because fixed-size images are used to render the connectors.
     *
     * @param indentSize indentSize Default value is 20
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public void setIndentSize(int indentSize) {
        setAttribute("indentSize", indentSize, true);
    }

    /**
     * The amount of indentation (in pixels) to add to a node's icon/title for each level down in this tree's hierarchy. <p>
     * This value is ignored when {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors} is
     * <code>true</code> because fixed-size images are used to render the connectors.
     *
     *
     * @return int
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public int getIndentSize()  {
        return getAttributeAsInt("indentSize");
    }

    /**
     * If set, tree-based filtering is performed such that parent nodes are kept as long as they have children that match the
     * filter criteria, even if the parents themselves do not match the filter criteria. If not set, filtering will exclude
     * parent nodes not matching the criteria and all nodes below it in the tree. <P> When enabled, fetchMode:"local" is
     * automatically enabled so that all filtering behavior shifts to the client-side and full criteria are no longer sent to
     * the server.  Instead, server fetches will always load all nodes, or with {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand}:true, will always load all nodes under a
     * given parent.  This means that the server does not need to implement special tree filtering logic. <P> Optionally, 
     * serverFilterFields can be set to a list of field names that will be sent to the server whenever they are present in the
     * criteria.
     *
     * @param keepParentsOnFilter keepParentsOnFilter Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setKeepParentsOnFilter(Boolean keepParentsOnFilter)  throws IllegalStateException {
        setAttribute("keepParentsOnFilter", keepParentsOnFilter, false);
    }

    /**
     * If set, tree-based filtering is performed such that parent nodes are kept as long as they have children that match the
     * filter criteria, even if the parents themselves do not match the filter criteria. If not set, filtering will exclude
     * parent nodes not matching the criteria and all nodes below it in the tree. <P> When enabled, fetchMode:"local" is
     * automatically enabled so that all filtering behavior shifts to the client-side and full criteria are no longer sent to
     * the server.  Instead, server fetches will always load all nodes, or with {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand loadDataOnDemand}:true, will always load all nodes under a
     * given parent.  This means that the server does not need to implement special tree filtering logic. <P> Optionally, 
     * serverFilterFields can be set to a list of field names that will be sent to the server whenever they are present in the
     * criteria.
     *
     *
     * @return Boolean
     */
    public Boolean getKeepParentsOnFilter()  {
        return getAttributeAsBoolean("keepParentsOnFilter");
    }

    /**
     * For databound treeGrid instances, should the entire tree of data be loaded on initial  fetch, or should folders load
     * their children as they are opened. <P> If unset, calling {@link com.smartgwt.client.widgets.tree.TreeGrid#fetchData
     * TreeGrid.fetchData} will default it to true, otherwise, if a ResultTree is passed to {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#setData TreeGrid.setData}, the  loadDataOnDemand setting is respected. <P>
     * Note that when using <code>loadDataOnDemand</code>, every node returned by the server is assumed be a folder which may
     * load further children.  See  defaultIsFolder for how to control this behavior.
     *
     * @param loadDataOnDemand loadDataOnDemand Default value is null
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setDataFetchMode
     * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_init_ondemand" target="examples">Initial Data & Load on Demand Example</a>
     */
    public void setLoadDataOnDemand(Boolean loadDataOnDemand) {
        setAttribute("loadDataOnDemand", loadDataOnDemand, true);
    }

    /**
     * For databound treeGrid instances, should the entire tree of data be loaded on initial  fetch, or should folders load
     * their children as they are opened. <P> If unset, calling {@link com.smartgwt.client.widgets.tree.TreeGrid#fetchData
     * TreeGrid.fetchData} will default it to true, otherwise, if a ResultTree is passed to {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#setData TreeGrid.setData}, the  loadDataOnDemand setting is respected. <P>
     * Note that when using <code>loadDataOnDemand</code>, every node returned by the server is assumed be a folder which may
     * load further children.  See  defaultIsFolder for how to control this behavior.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getDataFetchMode
     * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_init_ondemand" target="examples">Initial Data & Load on Demand Example</a>
     */
    public Boolean getLoadDataOnDemand()  {
        return getAttributeAsBoolean("loadDataOnDemand");
    }

    /**
     * The filename of the icon displayed use as the default drag tracker when for multiple files and/or folders are being
     * dragged.
     *
     * @param manyItemsImage manyItemsImage Default value is "[SKIN]folder_file.gif"
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     */
    public void setManyItemsImage(String manyItemsImage) {
        setAttribute("manyItemsImage", manyItemsImage, true);
    }

    /**
     * The filename of the icon displayed use as the default drag tracker when for multiple files and/or folders are being
     * dragged.
     *
     *
     * @return String
     * @see com.smartgwt.client.docs.Dragdrop Dragdrop overview and related methods
     */
    public String getManyItemsImage()  {
        return getAttributeAsString("manyItemsImage");
    }

    /**
     * The filename of the default icon for all leaf nodes in this grid. To specify a  custom image for an individual node, set
     * the {@link com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty} directly on the node.
     *
     * <br><br>If this method is called after the component has been drawn/initialized:
     * Set the icon for a particular treenode to a specified URL
     *
     * @param nodeIcon tree node. Default value is "[SKIN]file.gif"
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setNodeIcon(String nodeIcon) {
        setAttribute("nodeIcon", nodeIcon, true);
    }

    /**
     * The filename of the default icon for all leaf nodes in this grid. To specify a  custom image for an individual node, set
     * the {@link com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty} directly on the node.
     *
     *
     * @return String
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public String getNodeIcon()  {
        return getAttributeAsString("nodeIcon");
    }

    /**
     * For TreeGrids with loadDataOnDemand: true, a message to show the user if an attempt is  made to open a folder, and thus
     * load that node's children, while we are offline and  there is no offline cache of that data.  The message will be
     * presented to the user in  in a pop-up dialog box.
     *
     * @param offlineNodeMessage offlineNodeMessage Default value is "This data not available while offline"
     */
    public void setOfflineNodeMessage(String offlineNodeMessage) {
        setAttribute("offlineNodeMessage", offlineNodeMessage, true);
    }

    /**
     * For TreeGrids with loadDataOnDemand: true, a message to show the user if an attempt is  made to open a folder, and thus
     * load that node's children, while we are offline and  there is no offline cache of that data.  The message will be
     * presented to the user in  in a pop-up dialog box.
     *
     *
     * @return String
     */
    public String getOfflineNodeMessage()  {
        return getAttributeAsString("offlineNodeMessage");
    }

    /**
     * Width and height in pixels of the opener icons, that is, the icons which show the open or closed state of the node,
     * typically a [+] or [-] symbol. <P> If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors}
     * is true, the opener icon includes the connector line, and defaults to {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight}. <P> Otherwise, <code>openerIconSize</code> defaults
     * to {@link com.smartgwt.client.widgets.tree.TreeGrid#getIconSize iconSize}.
     *
     * @param openerIconSize openerIconSize Default value is null
     */
    public void setOpenerIconSize(Integer openerIconSize) {
        setAttribute("openerIconSize", openerIconSize, true);
    }

    /**
     * Width and height in pixels of the opener icons, that is, the icons which show the open or closed state of the node,
     * typically a [+] or [-] symbol. <P> If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors}
     * is true, the opener icon includes the connector line, and defaults to {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight}. <P> Otherwise, <code>openerIconSize</code> defaults
     * to {@link com.smartgwt.client.widgets.tree.TreeGrid#getIconSize iconSize}.
     *
     *
     * @return Integer
     */
    public Integer getOpenerIconSize()  {
        return getAttributeAsInt("openerIconSize");
    }

    /**
     * The base filename of the opener icon for the folder node when 'showConnectors' is false for this TreeGrid.<br> The
     * opener icon is displayed beside the folder icon in the Tree column for folder nodes. Clicking on this icon will toggle
     * the open state of the folder.<br> The filenames for these icons are assembled from this base filename and the state of
     * the node, as follows:<br> If the openerImage is set to <code>{baseName}.{extension}</code>, 
     * <code>{baseName}_opened.{extension}</code> will be displayed next to opened folders, and
     * <code>{baseName}_closed.{extension}</code> will be displayed next to closed folders, or if this page is in RTL mode,
     * <code>{baseName}_opened_rtl.{extension}</code> and <code>{baseName}_closed_rtl.{extension}</code> will be used.
     *
     * @param openerImage openerImage Default value is "[SKIN]opener.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setOpenerImage(String openerImage)  throws IllegalStateException {
        setAttribute("openerImage", openerImage, false);
    }

    /**
     * The base filename of the opener icon for the folder node when 'showConnectors' is false for this TreeGrid.<br> The
     * opener icon is displayed beside the folder icon in the Tree column for folder nodes. Clicking on this icon will toggle
     * the open state of the folder.<br> The filenames for these icons are assembled from this base filename and the state of
     * the node, as follows:<br> If the openerImage is set to <code>{baseName}.{extension}</code>, 
     * <code>{baseName}_opened.{extension}</code> will be displayed next to opened folders, and
     * <code>{baseName}_closed.{extension}</code> will be displayed next to closed folders, or if this page is in RTL mode,
     * <code>{baseName}_opened_rtl.{extension}</code> and <code>{baseName}_closed_rtl.{extension}</code> will be used.
     *
     *
     * @return String
     */
    public String getOpenerImage()  {
        return getAttributeAsString("openerImage");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons} is true, this suffix will be
     * appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} for open folders in this
     * grid.
     *
     * @param openIconSuffix openIconSuffix Default value is "open"
     */
    public void setOpenIconSuffix(String openIconSuffix) {
        setAttribute("openIconSuffix", openIconSuffix, true);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowOpenIcons showOpenIcons} is true, this suffix will be
     * appended to the {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} for open folders in this
     * grid.
     *
     *
     * @return String
     */
    public String getOpenIconSuffix()  {
        return getAttributeAsString("openIconSuffix");
    }

    /**
     * Message displayed when user attempts to drag a node into a parent that already contains a child of the same name.
     *
     * @param parentAlreadyContainsChildMessage parentAlreadyContainsChildMessage Default value is "This item already contains a child item with that name."
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setCanReorderRecords
     */
    public void setParentAlreadyContainsChildMessage(String parentAlreadyContainsChildMessage)  throws IllegalStateException {
        setAttribute("parentAlreadyContainsChildMessage", parentAlreadyContainsChildMessage, false);
    }

    /**
     * Message displayed when user attempts to drag a node into a parent that already contains a child of the same name.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanDragRecordsOut
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanAcceptDroppedRecords
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getCanReorderRecords
     */
    public String getParentAlreadyContainsChildMessage()  {
        return getAttributeAsString("parentAlreadyContainsChildMessage");
    }

    /**
     * If specified, the selection object for this list will use this property to mark records as selected.  In other words, if
     * this attribute were set to <code>"isSelected"</code> any records in the listGrid data where <code>"isSelected"</code> is
     * <code>true</code> will show up as selected in the grid. Similarly if records are selected within the grid after the grid
     * has been created, this property will be set to true on the selected records.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param selectionProperty selectionProperty Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public void setSelectionProperty(String selectionProperty)  throws IllegalStateException {
        setAttribute("selectionProperty", selectionProperty, false);
    }

    /**
     * If specified, the selection object for this list will use this property to mark records as selected.  In other words, if
     * this attribute were set to <code>"isSelected"</code> any records in the listGrid data where <code>"isSelected"</code> is
     * <code>true</code> will show up as selected in the grid. Similarly if records are selected within the grid after the grid
     * has been created, this property will be set to true on the selected records.
     *
     *
     * @return String
     * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
     */
    public String getSelectionProperty()  {
        return getAttributeAsString("selectionProperty");
    }

    /**
     * If specified, this attribute will override {@link com.smartgwt.client.widgets.tree.Tree#getSeparateFolders
     * separateFolders} on the data for this treeGrid. <P> Specifies whether folders and leaves should be segregated in the
     * treeGrid display. Use {@link com.smartgwt.client.widgets.tree.TreeGrid#getSortFoldersBeforeLeaves
     * sortFoldersBeforeLeaves} to customize whether folders appear before  or after their sibling leaves. <P> If unset, at the
     * treeGrid level, the property can be set directly on {@link com.smartgwt.client.widgets.tree.TreeGrid#getData the tree
     * data object} or for dataBound TreeGrids on the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDataProperties
     * dataProperties configuration object}.
     *
     * @param separateFolders separateFolders Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setSeparateFolders(Boolean separateFolders)  throws IllegalStateException {
        setAttribute("separateFolders", separateFolders, false);
    }

    /**
     * If specified, this attribute will override {@link com.smartgwt.client.widgets.tree.Tree#getSeparateFolders
     * separateFolders} on the data for this treeGrid. <P> Specifies whether folders and leaves should be segregated in the
     * treeGrid display. Use {@link com.smartgwt.client.widgets.tree.TreeGrid#getSortFoldersBeforeLeaves
     * sortFoldersBeforeLeaves} to customize whether folders appear before  or after their sibling leaves. <P> If unset, at the
     * treeGrid level, the property can be set directly on {@link com.smartgwt.client.widgets.tree.TreeGrid#getData the tree
     * data object} or for dataBound TreeGrids on the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDataProperties
     * dataProperties configuration object}.
     *
     *
     * @return Boolean
     */
    public Boolean getSeparateFolders()  {
        return getAttributeAsBoolean("separateFolders");
    }

    /**
     * When {@link com.smartgwt.client.widgets.tree.TreeGrid#getKeepParentsOnFilter keepParentsOnFilter} is enabled, this
     * property lists field names that will be sent to the server if they are present in criteria.
     *
     * @param serverFilterFields serverFilterFields Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setServerFilterFields(String... serverFilterFields)  throws IllegalStateException {
        setAttribute("serverFilterFields", serverFilterFields, false);
    }

    /**
     * When {@link com.smartgwt.client.widgets.tree.TreeGrid#getKeepParentsOnFilter keepParentsOnFilter} is enabled, this
     * property lists field names that will be sent to the server if they are present in criteria.
     *
     *
     * @return String
     */
    public String[] getServerFilterFields()  {
        return getAttributeAsStringArray("serverFilterFields");
    }

    /**
     * Should this treeGrid show connector lines illustrating the tree's hierarchy? <P> For the set of images used to show
     * connectors, see {@link com.smartgwt.client.widgets.tree.TreeGrid#getConnectorImage connectorImage}. <P> <b>Note</b>: in
     * order for connector images to be perfectly connected, all styles for cells must have no top or bottom border or padding.
     * If you see small gaps in connector lines, check your CSS files.  See the example below for an example of correct
     * configuration, including example CSS.
     *
     * @param showConnectors showConnectors Default value is false
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_connectors" target="examples">Connectors Example</a>
     */
    public void setShowConnectors(Boolean showConnectors) {
        setAttribute("showConnectors", showConnectors, true);
    }

    /**
     * Should this treeGrid show connector lines illustrating the tree's hierarchy? <P> For the set of images used to show
     * connectors, see {@link com.smartgwt.client.widgets.tree.TreeGrid#getConnectorImage connectorImage}. <P> <b>Note</b>: in
     * order for connector images to be perfectly connected, all styles for cells must have no top or bottom border or padding.
     * If you see small gaps in connector lines, check your CSS files.  See the example below for an example of correct
     * configuration, including example CSS.
     *
     *
     * @return Boolean
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_connectors" target="examples">Connectors Example</a>
     */
    public Boolean getShowConnectors()  {
        return getAttributeAsBoolean("showConnectors");
    }

    /**
     * Should folder nodes showing custom icons (set via the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty}, default {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getIcon icon}), show drop state images when the user is drop-hovering over the
     * folder. If true, the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} will be appended
     * to the image URL (so <code>"customFolder.gif"</code> might be replaced with  <code>"customFolder_drop.gif"</code>).<br>
     * Can be overridden at the node level via the default property {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getShowDropIcon showDropIcon} and that property can be renamed via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconDropProperty customIconDropProperty}.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param showCustomIconDrop showCustomIconDrop Default value is false
     */
    public void setShowCustomIconDrop(Boolean showCustomIconDrop) {
        setAttribute("showCustomIconDrop", showCustomIconDrop, true);
    }

    /**
     * Should folder nodes showing custom icons (set via the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty}, default {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getIcon icon}), show drop state images when the user is drop-hovering over the
     * folder. If true, the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} will be appended
     * to the image URL (so <code>"customFolder.gif"</code> might be replaced with  <code>"customFolder_drop.gif"</code>).<br>
     * Can be overridden at the node level via the default property {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getShowDropIcon showDropIcon} and that property can be renamed via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconDropProperty customIconDropProperty}.
     *
     *
     * @return Boolean
     */
    public Boolean getShowCustomIconDrop()  {
        return getAttributeAsBoolean("showCustomIconDrop");
    }

    /**
     * Should folder nodes showing custom icons (set via the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty}), show open state images when the
     * folder is opened. If true, the {@link com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix} will
     * be appended to the image URL (so <code>"customFolder.gif"</code> might be replaced with 
     * <code>"customFolder_open.gif"</code>).<br> <b>Note</b> that the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix} is never appended to custom folder
     * icons.<br> Can be overridden at the node level via the default property {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getShowOpenIcon showOpenIcon} and that property can be renamed via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconOpenProperty customIconOpenProperty}.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param showCustomIconOpen showCustomIconOpen Default value is false
     */
    public void setShowCustomIconOpen(Boolean showCustomIconOpen) {
        setAttribute("showCustomIconOpen", showCustomIconOpen, true);
    }

    /**
     * Should folder nodes showing custom icons (set via the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconProperty customIconProperty}), show open state images when the
     * folder is opened. If true, the {@link com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix} will
     * be appended to the image URL (so <code>"customFolder.gif"</code> might be replaced with 
     * <code>"customFolder_open.gif"</code>).<br> <b>Note</b> that the {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix} is never appended to custom folder
     * icons.<br> Can be overridden at the node level via the default property {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getShowOpenIcon showOpenIcon} and that property can be renamed via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getCustomIconOpenProperty customIconOpenProperty}.
     *
     *
     * @return Boolean
     */
    public Boolean getShowCustomIconOpen()  {
        return getAttributeAsBoolean("showCustomIconOpen");
    }

    /**
     * Should tree nodes show a disabled checkbox instead of a blank space  when {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance}:"checkbox"  is set on the
     * treegrid, and a node can't be selected?
     *
     * @param showDisabledSelectionCheckbox showDisabledSelectionCheckbox Default value is false
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.grid.ListGrid#setRecordCanSelectProperty
     */
    public void setShowDisabledSelectionCheckbox(Boolean showDisabledSelectionCheckbox)  throws IllegalStateException {
        setAttribute("showDisabledSelectionCheckbox", showDisabledSelectionCheckbox, false);
    }

    /**
     * Should tree nodes show a disabled checkbox instead of a blank space  when {@link
     * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance}:"checkbox"  is set on the
     * treegrid, and a node can't be selected?
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.grid.ListGrid#getRecordCanSelectProperty
     */
    public Boolean getShowDisabledSelectionCheckbox()  {
        return getAttributeAsBoolean("showDisabledSelectionCheckbox");
    }

    /**
     * If true, when the user drags a droppable target over a folder in this TreeGrid, show  a different icon folder icon. This
     * is achieved by appending the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} onto the
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} URL (for example
     * <code>"[SKIN]/folder.gif"</code> may be replaced by <code>"[SKIN]/folder_drop.gif"</code>).
     *
     * @param showDropIcons showDropIcons Default value is true
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setShowDropIcons(Boolean showDropIcons) {
        setAttribute("showDropIcons", showDropIcons, true);
    }

    /**
     * If true, when the user drags a droppable target over a folder in this TreeGrid, show  a different icon folder icon. This
     * is achieved by appending the {@link com.smartgwt.client.widgets.tree.TreeGrid#getDropIconSuffix dropIconSuffix} onto the
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} URL (for example
     * <code>"[SKIN]/folder.gif"</code> may be replaced by <code>"[SKIN]/folder_drop.gif"</code>).
     *
     *
     * @return Boolean
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public Boolean getShowDropIcons()  {
        return getAttributeAsBoolean("showDropIcons");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors} is true, this property determines
     * whether we should show vertical continuation lines for each level of indenting within the tree. Setting to false will
     * show only the hierarchy lines are only shown for the most indented path ("sparse" connectors).
     *
     * @param showFullConnectors showFullConnectors Default value is true
     */
    public void setShowFullConnectors(Boolean showFullConnectors) {
        setAttribute("showFullConnectors", showFullConnectors, true);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.TreeGrid#getShowConnectors showConnectors} is true, this property determines
     * whether we should show vertical continuation lines for each level of indenting within the tree. Setting to false will
     * show only the hierarchy lines are only shown for the most indented path ("sparse" connectors).
     *
     *
     * @return Boolean
     */
    public Boolean getShowFullConnectors()  {
        return getAttributeAsBoolean("showFullConnectors");
    }

    /**
     * Should the an opener icon be displayed next to folder nodes?
     *
     * @param showOpener showOpener Default value is true
     */
    public void setShowOpener(Boolean showOpener) {
        setAttribute("showOpener", showOpener, true);
    }

    /**
     * Should the an opener icon be displayed next to folder nodes?
     *
     *
     * @return Boolean
     */
    public Boolean getShowOpener()  {
        return getAttributeAsBoolean("showOpener");
    }

    /**
     * If true, show a different icon for <code>open</code> folders than closed folders. This is achieved by appending the
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix} onto the  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} URL [for example <code>"[SKIN]/folder.gif"</code>
     * might be  replaced by <code>"[SKIN]/folder_open.gif"</code>.<br> <b>Note</b> If this property is set to
     * <code>false</code> the same icon is shown for open folders as for closed folders, unless a custom folder icon was
     * specified. This will be determined by {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} plus
     * the {@link com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix}.
     *
     * @param showOpenIcons showOpenIcons Default value is true
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setShowOpenIcons(Boolean showOpenIcons) {
        setAttribute("showOpenIcons", showOpenIcons, true);
    }

    /**
     * If true, show a different icon for <code>open</code> folders than closed folders. This is achieved by appending the
     * {@link com.smartgwt.client.widgets.tree.TreeGrid#getOpenIconSuffix openIconSuffix} onto the  {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} URL [for example <code>"[SKIN]/folder.gif"</code>
     * might be  replaced by <code>"[SKIN]/folder_open.gif"</code>.<br> <b>Note</b> If this property is set to
     * <code>false</code> the same icon is shown for open folders as for closed folders, unless a custom folder icon was
     * specified. This will be determined by {@link com.smartgwt.client.widgets.tree.TreeGrid#getFolderIcon folderIcon} plus
     * the {@link com.smartgwt.client.widgets.tree.TreeGrid#getClosedIconSuffix closedIconSuffix}.
     *
     *
     * @return Boolean
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public Boolean getShowOpenIcons()  {
        return getAttributeAsBoolean("showOpenIcons");
    }

    /**
     * Should partially selected parents be shown with special icon?
     *
     * @param showPartialSelection showPartialSelection Default value is false
     */
    public void setShowPartialSelection(Boolean showPartialSelection) {
        setAttribute("showPartialSelection", showPartialSelection, true);
    }

    /**
     * Should partially selected parents be shown with special icon?
     *
     *
     * @return Boolean
     */
    public Boolean getShowPartialSelection()  {
        return getAttributeAsBoolean("showPartialSelection");
    }

    /**
     * Specifies whether the root node should be displayed in the treeGrid. <P> This property is only available for "children"
     * modelType trees, hence is not allowed for trees that load data from the server dynamically via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#fetchData TreeGrid.fetchData}.   <P> To get the equivalent of a visible "root"
     * node in a tree that loads data dynamically, add a singular, top-level parent to the data.  However, note that this
     * top-level parent will technically be the only child of root, and the implicit root object will be returned by {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot this.data.getRoot()}.
     *
     * @param showRoot showRoot Default value is false
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setShowRoot(Boolean showRoot)  throws IllegalStateException {
        setAttribute("showRoot", showRoot, false);
    }

    /**
     * Specifies whether the root node should be displayed in the treeGrid. <P> This property is only available for "children"
     * modelType trees, hence is not allowed for trees that load data from the server dynamically via {@link
     * com.smartgwt.client.widgets.tree.TreeGrid#fetchData TreeGrid.fetchData}.   <P> To get the equivalent of a visible "root"
     * node in a tree that loads data dynamically, add a singular, top-level parent to the data.  However, note that this
     * top-level parent will technically be the only child of root, and the implicit root object will be returned by {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot this.data.getRoot()}.
     *
     *
     * @return Boolean
     */
    public Boolean getShowRoot()  {
        return getAttributeAsBoolean("showRoot");
    }

    /**
     * If specified, this attribute will override {@link com.smartgwt.client.widgets.tree.Tree#getSortFoldersBeforeLeaves
     * sortFoldersBeforeLeaves} on the data for this treeGrid. <P> Specifies whether when {@link
     * com.smartgwt.client.widgets.tree.Tree#getSeparateFolders separateFolders} is true, folders should be displayed before or
     * after their sibling leaves in a sorted tree. If set to true, with sortDirection set to Array.ASCENDING, folders are
     * displayed before their sibling leaves and with sort direction set to Array.DESCENDING they are displayed after. To
     * invert this behavior, set this property to false.
     *
     * @param sortFoldersBeforeLeaves sortFoldersBeforeLeaves Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setSeparateFolders
     */
    public void setSortFoldersBeforeLeaves(Boolean sortFoldersBeforeLeaves)  throws IllegalStateException {
        setAttribute("sortFoldersBeforeLeaves", sortFoldersBeforeLeaves, false);
    }

    /**
     * If specified, this attribute will override {@link com.smartgwt.client.widgets.tree.Tree#getSortFoldersBeforeLeaves
     * sortFoldersBeforeLeaves} on the data for this treeGrid. <P> Specifies whether when {@link
     * com.smartgwt.client.widgets.tree.Tree#getSeparateFolders separateFolders} is true, folders should be displayed before or
     * after their sibling leaves in a sorted tree. If set to true, with sortDirection set to Array.ASCENDING, folders are
     * displayed before their sibling leaves and with sort direction set to Array.DESCENDING they are displayed after. To
     * invert this behavior, set this property to false.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getSeparateFolders
     */
    public Boolean getSortFoldersBeforeLeaves()  {
        return getAttributeAsBoolean("sortFoldersBeforeLeaves");
    }

    /**
     * Visible title for the tree column (field).
     *
     * @param treeFieldTitle treeFieldTitle Default value is "Name"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setTreeFieldTitle(String treeFieldTitle)  throws IllegalStateException {
        setAttribute("treeFieldTitle", treeFieldTitle, false);
    }

    /**
     * Visible title for the tree column (field).
     *
     *
     * @return String
     */
    public String getTreeFieldTitle()  {
        return getAttributeAsString("treeFieldTitle");
    }

    /**
     * Overridden to disallow editing of the {@link com.smartgwt.client.widgets.tree.TreeNode#getName name} field of this
     * grid's data tree. Also disallows editing of the auto-generated tree field, which displays the result of {@link
     * com.smartgwt.client.widgets.tree.Tree#getTitle Tree.getTitle} on the node.
     *
     * @return Whether to allow editing this cell
     */
    public native Boolean canEditCell() /*-{
        var self = this.@com.smartgwt.client.widgets.BaseWidget::getOrCreateJsObj()();
        var retVal =self.canEditCell();
        if(retVal == null || retVal === undefined) {
            return null;
        } else {
            return @com.smartgwt.client.util.JSOHelper::toBoolean(Z)(retVal);
        }
    }-*/;

    /**
     * An array of field objects, specifying the order, layout, dynamic calculation, and sorting behavior of each field
     * in the treeGrid object. In TreeGrids, the fields array specifies columns. Each field in the fields array is
     * TreeGridField object. <p> If {@link com.smartgwt.client.widgets.tree.TreeGrid#getDataSource dataSource} is also
     * set, this value acts as a set of overrides as explained in {@link com.smartgwt.client.widgets.DataBoundComponent#getFields
     * fields}.
     *
     * @param fields fields Default value is null
     */
    public void setFields(TreeGridField... fields) {
        super.setFields(fields);
    }
  
    /**
     * You can specify the initial set of data for a databound TreeGrid using this property. The value of this attribute
     * should be a list of <code>parentId</code> linked ${isc.DocUtils.linkForRef('object:TreeNode')}s in a format
     * equivalent to that documented on {@link com.smartgwt.client.widgets.tree.Tree#getData data}.
     * <p/>
     *
     * @param initialData initialData Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setInitialData(TreeNode[] initialData) throws IllegalStateException {
        setAttribute("initialData", initialData, false);
    }

    /**
     * For databound trees, use this attribute to supply a ${isc.DocUtils.linkForRef('ResultTree.rootValue')} for this component's generated data object. <P>  This property allows you to have a particular component navigate a tree starting from any given node as the root.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param treeRootValue treeRootValue Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setTreeRootValue(String treeRootValue) throws IllegalStateException {
        setAttribute("treeRootValue", treeRootValue, false);
    }

    /**
     * For databound trees, use this attribute to supply a ${isc.DocUtils.linkForRef('ResultTree.rootValue')} for this component's generated data object. <P>  This property allows you to have a particular component navigate a tree starting from any given node as the root.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param treeRootValue treeRootValue Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setTreeRootValue(Integer treeRootValue) throws IllegalStateException {
        setAttribute("treeRootValue", treeRootValue, false);
    }

    /**
     * For databound trees, use this attribute to supply a ${isc.DocUtils.linkForRef('ResultTree.rootValue')} for this component's generated data object. <P>  This property allows you to have a particular component navigate a tree starting from any given node as the root.
     *
     * @return tree root value as String
     */
    public String getTreeRootValue() {
        return getAttribute("treeRootValue");
    }

}



