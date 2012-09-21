package org.vaadin.smartgwt.server.tree;

import org.vaadin.smartgwt.client.ui.tree.VTree;
import org.vaadin.smartgwt.server.core.BaseClass;
import org.vaadin.smartgwt.server.types.TreeModelType;
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
 * A Tree is a data model representing a set of objects linked into a hierarchy. <P> A Tree has no visual presentation, it
 * is displayed by a {@link com.smartgwt.client.widgets.tree.TreeGrid} or {@link
 * com.smartgwt.client.widgets.grid.ColumnTree} when supplied as {@link com.smartgwt.client.widgets.tree.TreeGrid#getData
 * data} or {@link com.smartgwt.client.widgets.grid.ColumnTree#getData data}. <P> A Tree can be constructed out of a List
 * of objects interlinked by IDs or via explicitly specified Arrays of child objects.  See {@link
 * com.smartgwt.client.widgets.tree.Tree#getModelType modelType} for an explanation of how to pass data to a Tree. <P>
 * Typical usage is to call {@link com.smartgwt.client.widgets.tree.TreeGrid#fetchData TreeGrid.fetchData} to cause
 * automatic creation of a  ResultTree, which is a type of Tree that automatically handles loading data on  demand.  For
 * information on DataBinding Trees, see {@link com.smartgwt.client.docs.TreeDataBinding}.
 */
// @formatter:off
@com.vaadin.ui.ClientWidget(VTree.class)
public class Tree extends BaseClass  { 
    /**
     * If true, the root node is automatically opened when the tree is created or {@link
     * com.smartgwt.client.widgets.tree.Tree#setRoot Tree.setRoot} is called.
     *
     * @param autoOpenRoot autoOpenRoot Default value is true
     */
    public void setAutoOpenRoot(Boolean autoOpenRoot) {
        setAttribute("autoOpenRoot", autoOpenRoot, true);
    }

    /**
     * If true, the root node is automatically opened when the tree is created or {@link
     * com.smartgwt.client.widgets.tree.Tree#setRoot Tree.setRoot} is called.
     *
     *
     * @return Boolean
     */
    public Boolean getAutoOpenRoot()  {
        return getAttributeAsBoolean("autoOpenRoot");
    }

    /**
     * For trees with the modelType "children", this property specifies the name of the property that contains the list of
     * children for a node.
     *
     * @param childrenProperty childrenProperty Default value is "children"
     * @see com.smartgwt.client.widgets.tree.Tree#setModelType
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_children_arrays" target="examples">Children Arrays Example</a>
     */
    public void setChildrenProperty(String childrenProperty) {
        setAttribute("childrenProperty", childrenProperty, true);
    }

    /**
     * For trees with the modelType "children", this property specifies the name of the property that contains the list of
     * children for a node.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.Tree#getModelType
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_children_arrays" target="examples">Children Arrays Example</a>
     */
    public String getChildrenProperty()  {
        return getAttributeAsString("childrenProperty");
    }

    /**
     * Controls whether nodes are assumed to be folders or leaves by default. <P> Nodes that have children or have the {@link
     * com.smartgwt.client.widgets.tree.Tree#getIsFolderProperty isFolderProperty} set to true will be considered folders by
     * default.  Other nodes will be considered folders or leaves by default according to this setting.
     *
     * @param defaultIsFolder defaultIsFolder Default value is null
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     * @see com.smartgwt.client.widgets.tree.TreeGrid#setLoadDataOnDemand
     */
    public void setDefaultIsFolder(Boolean defaultIsFolder)  throws IllegalStateException {
        setAttribute("defaultIsFolder", defaultIsFolder, false);
    }

    /**
     * Controls whether nodes are assumed to be folders or leaves by default. <P> Nodes that have children or have the {@link
     * com.smartgwt.client.widgets.tree.Tree#getIsFolderProperty isFolderProperty} set to true will be considered folders by
     * default.  Other nodes will be considered folders or leaves by default according to this setting.
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.TreeGrid#getLoadDataOnDemand
     */
    public Boolean getDefaultIsFolder()  {
        return getAttributeAsBoolean("defaultIsFolder");
    }

    /**
     * Title assigned to nodes without a {@link com.smartgwt.client.widgets.tree.Tree#getTitleProperty titleProperty} value or
     * a {@link com.smartgwt.client.widgets.tree.Tree#getNameProperty nameProperty} value.
     *
     * @param defaultNodeTitle defaultNodeTitle Default value is "Untitled"
     */
    public void setDefaultNodeTitle(String defaultNodeTitle) {
        setAttribute("defaultNodeTitle", defaultNodeTitle, true);
    }

    /**
     * Title assigned to nodes without a {@link com.smartgwt.client.widgets.tree.Tree#getTitleProperty titleProperty} value or
     * a {@link com.smartgwt.client.widgets.tree.Tree#getNameProperty nameProperty} value.
     *
     *
     * @return String
     */
    public String getDefaultNodeTitle()  {
        return getAttributeAsString("defaultNodeTitle");
    }

    /**
     * If this tree has {@link com.smartgwt.client.widgets.tree.Tree#getModelType modelType:"parent"}, should nodes in the data
     * array for the tree be dropped if they have an explicitly specified value for the {@link
     * com.smartgwt.client.widgets.tree.Tree#getParentIdField parentIdField} which doesn't match any other nodes in the tree.
     * If set to false these nodes will be added as children of the root node.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param discardParentlessNodes discardParentlessNodes Default value is false
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     */
    public void setDiscardParentlessNodes(Boolean discardParentlessNodes)  throws IllegalStateException {
        setAttribute("discardParentlessNodes", discardParentlessNodes, false);
    }

    /**
     * If this tree has {@link com.smartgwt.client.widgets.tree.Tree#getModelType modelType:"parent"}, should nodes in the data
     * array for the tree be dropped if they have an explicitly specified value for the {@link
     * com.smartgwt.client.widgets.tree.Tree#getParentIdField parentIdField} which doesn't match any other nodes in the tree.
     * If set to false these nodes will be added as children of the root node.
     *
     *
     * @return Boolean
     */
    public Boolean getDiscardParentlessNodes()  {
        return getAttributeAsBoolean("discardParentlessNodes");
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds an id for the node which is
     * unique across the entire Tree.  Required for all nodes for trees with modelType "parent". Default value is "id".  See
     * {@link com.smartgwt.client.widgets.tree.TreeNode#getId id} for usage.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param idField idField Default value is "id"
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     * @see com.smartgwt.client.widgets.tree.TreeNode#setId
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setIdField(String idField)  throws IllegalStateException {
        setAttribute("idField", idField, false);
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds an id for the node which is
     * unique across the entire Tree.  Required for all nodes for trees with modelType "parent". Default value is "id".  See
     * {@link com.smartgwt.client.widgets.tree.TreeNode#getId id} for usage.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeNode#getId
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public String getIdField()  {
        return getAttributeAsString("idField");
    }

    /**
     * Name of property that defines whether a node is a folder.  By default this is set to {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getIsFolder isFolder}.
     *
     * @param isFolderProperty isFolderProperty Default value is "isFolder"
     * @see com.smartgwt.client.widgets.tree.TreeNode#setIsFolder
     */
    public void setIsFolderProperty(String isFolderProperty) {
        setAttribute("isFolderProperty", isFolderProperty, true);
    }

    /**
     * Name of property that defines whether a node is a folder.  By default this is set to {@link
     * com.smartgwt.client.widgets.tree.TreeNode#getIsFolder isFolder}.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeNode#getIsFolder
     */
    public String getIsFolderProperty()  {
        return getAttributeAsString("isFolderProperty");
    }

    /**
     * Selects the model used to construct the tree representation.  See {@link com.smartgwt.client.types.TreeModelType} for
     * the available options and their implications. <P> If the "parent" modelType is used, you can provide the initial
     * parent-linked data set to the tree via the {@link com.smartgwt.client.widgets.tree.Tree#getData data} attribute.  If the
     * "children" modelType is used, you can provide the initial tree structure to the Tree via the {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot root} attribute.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param modelType modelType Default value is "children"
     * @see com.smartgwt.client.widgets.tree.Tree#setData
     * @see com.smartgwt.client.widgets.tree.Tree#setRoot
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setModelType(TreeModelType modelType) {
        setAttribute("modelType", modelType.getValue(), true);
    }

    /**
     * Selects the model used to construct the tree representation.  See {@link com.smartgwt.client.types.TreeModelType} for
     * the available options and their implications. <P> If the "parent" modelType is used, you can provide the initial
     * parent-linked data set to the tree via the {@link com.smartgwt.client.widgets.tree.Tree#getData data} attribute.  If the
     * "children" modelType is used, you can provide the initial tree structure to the Tree via the {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot root} attribute.
     *
     *
     * @return TreeModelType
     * @see com.smartgwt.client.widgets.tree.Tree#getData
     * @see com.smartgwt.client.widgets.tree.Tree#getRoot
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public TreeModelType getModelType()  {
        return EnumUtil.getEnum(TreeModelType.values(), getAttribute("modelType"));
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds a name for the node that is
     * unique among it's immediate siblings, thus allowing a unique path to be used to identify the node, similar to a file
     * system.  Default value is "name".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getName name} for usage.
     *
     * @param nameProperty nameProperty Default value is "name"
     * @see com.smartgwt.client.widgets.tree.TreeNode#setName
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setNameProperty(String nameProperty) {
        setAttribute("nameProperty", nameProperty, true);
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds a name for the node that is
     * unique among it's immediate siblings, thus allowing a unique path to be used to identify the node, similar to a file
     * system.  Default value is "name".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getName name} for usage.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeNode#getName
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public String getNameProperty()  {
        return getAttributeAsString("nameProperty");
    }

    /**
     * The property consulted by the default implementation of {@link com.smartgwt.client.widgets.tree.Tree#isOpen Tree.isOpen}
     * to determine if the node is open or not.  By default, this property is auto-generated for you, but you can set it to a
     * custom value if you want to declaratively specify this state, but be careful - if you display this Tree in multiple
     * TreeGrids at the same time, the open state will not be tracked independently - see {@link
     * com.smartgwt.client.docs.SharingNodes} for more info on this.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param openProperty openProperty Default value is null
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_init_ondemand" target="examples">Initial Data & Load on Demand Example</a>
     */
    public void setOpenProperty(String openProperty) {
        setAttribute("openProperty", openProperty, true);
    }

    /**
     * The property consulted by the default implementation of {@link com.smartgwt.client.widgets.tree.Tree#isOpen Tree.isOpen}
     * to determine if the node is open or not.  By default, this property is auto-generated for you, but you can set it to a
     * custom value if you want to declaratively specify this state, but be careful - if you display this Tree in multiple
     * TreeGrids at the same time, the open state will not be tracked independently - see {@link
     * com.smartgwt.client.docs.SharingNodes} for more info on this.
     *
     *
     * @return String
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_init_ondemand" target="examples">Initial Data & Load on Demand Example</a>
     */
    public String getOpenProperty()  {
        return getAttributeAsString("openProperty");
    }

    /**
     * For trees with modelType "parent", this property specifies the name of the property that contains the unique parent ID
     * of a node.  Default value is "parentId".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getParentId parentId} for
     * usage.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param parentIdField parentIdField Default value is "parentId"
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     * @see com.smartgwt.client.widgets.tree.TreeNode#setParentId
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public void setParentIdField(String parentIdField)  throws IllegalStateException {
        setAttribute("parentIdField", parentIdField, false);
    }

    /**
     * For trees with modelType "parent", this property specifies the name of the property that contains the unique parent ID
     * of a node.  Default value is "parentId".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getParentId parentId} for
     * usage.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.TreeNode#getParentId
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_appearance_node_titles" target="examples">Node Titles Example</a>
     */
    public String getParentIdField()  {
        return getAttributeAsString("parentIdField");
    }

    /**
     * Specifies the delimiter between node names.  The pathDelim is used to construct a unique
     *  path to each node. A path can be obtained for any node by calling
     * {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} and can be used to find any node in the tree by
     * calling
     * {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find}.  Note that you can also hand-construct a path - in other
     * words
     *  you are not required to call {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} in order to later use
     *  {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find} to retrieve it.
     *  <br><br>
     *  The pathDelim can be any character or sequence of characters, but must be a unique string
     * with respect to the text that can appear in the {@link com.smartgwt.client.widgets.tree.Tree#getNameProperty
     * nameProperty} that's used
     *  for naming the nodes.  So for example, if you have the following tree:
     *  <pre>
     *  one
     *    two
     *      three/four
     *  </pre>
     *  Then you will be unable to find the <code>three/four</code> node using
     *  {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find} if your tree is using the default pathDelim of /.
     *  In such a case, you can use a different pathDelim for the tree.  For example if you used |
     *  for the path delim, then you can find the <code>three/four</code> node in the tree above by
     *  calling <code>tree.find("one|two|three/four")</code>.
     *  <br><br>
     * The pathDelim is used only by {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} and {@link
     * com.smartgwt.client.widgets.tree.Tree#find Tree.find} and
     *  does not affect any aspect of the tree structure or other forms of tree navigation (such as
     *  via {@link com.smartgwt.client.widgets.tree.Tree#getChildren Tree.getChildren}).
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param pathDelim pathDelim Default value is "/"
     * @see com.smartgwt.client.widgets.tree.Tree#setNameProperty
     * @see com.smartgwt.client.widgets.tree.Tree#find
     */
    public void setPathDelim(String pathDelim) {
        setAttribute("pathDelim", pathDelim, true);
    }

    /**
     * Specifies the delimiter between node names.  The pathDelim is used to construct a unique
     *  path to each node. A path can be obtained for any node by calling
     * {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} and can be used to find any node in the tree by
     * calling
     * {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find}.  Note that you can also hand-construct a path - in other
     * words
     *  you are not required to call {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} in order to later use
     *  {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find} to retrieve it.
     *  <br><br>
     *  The pathDelim can be any character or sequence of characters, but must be a unique string
     * with respect to the text that can appear in the {@link com.smartgwt.client.widgets.tree.Tree#getNameProperty
     * nameProperty} that's used
     *  for naming the nodes.  So for example, if you have the following tree:
     *  <pre>
     *  one
     *    two
     *      three/four
     *  </pre>
     *  Then you will be unable to find the <code>three/four</code> node using
     *  {@link com.smartgwt.client.widgets.tree.Tree#find Tree.find} if your tree is using the default pathDelim of /.
     *  In such a case, you can use a different pathDelim for the tree.  For example if you used |
     *  for the path delim, then you can find the <code>three/four</code> node in the tree above by
     *  calling <code>tree.find("one|two|three/four")</code>.
     *  <br><br>
     * The pathDelim is used only by {@link com.smartgwt.client.widgets.tree.Tree#getPath Tree.getPath} and {@link
     * com.smartgwt.client.widgets.tree.Tree#find Tree.find} and
     *  does not affect any aspect of the tree structure or other forms of tree navigation (such as
     *  via {@link com.smartgwt.client.widgets.tree.Tree#getChildren Tree.getChildren}).
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tree.Tree#getNameProperty
     * @see com.smartgwt.client.widgets.tree.Tree#find
     */
    public String getPathDelim()  {
        return getAttributeAsString("pathDelim");
    }

    /**
     * If new nodes are added to a tree with modelType:"parent" which have the same {@link
     * com.smartgwt.client.widgets.tree.Tree#getIdField id field value} as existing nodes, the existing nodes are removed when
     * the new nodes are added. <P> If reportCollisions is true, the Tree will log a warning in the developer console about
     * this. <P> Note that if an id collision occurs between a new node and its ancestor, the ancestor will be removed and the
     * new node will not be added to the tree.
     *
     * @param reportCollisions reportCollisions Default value is true
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     */
    public void setReportCollisions(Boolean reportCollisions)  throws IllegalStateException {
        setAttribute("reportCollisions", reportCollisions, false);
    }

    /**
     * If new nodes are added to a tree with modelType:"parent" which have the same {@link
     * com.smartgwt.client.widgets.tree.Tree#getIdField id field value} as existing nodes, the existing nodes are removed when
     * the new nodes are added. <P> If reportCollisions is true, the Tree will log a warning in the developer console about
     * this. <P> Note that if an id collision occurs between a new node and its ancestor, the ancestor will be removed and the
     * new node will not be added to the tree.
     *
     *
     * @return Boolean
     */
    public Boolean getReportCollisions()  {
        return getAttributeAsBoolean("reportCollisions");
    }

    /**
     * If you're using the "parent" modelType, you can provide the root node configuration via this
     *  property.  If you don't provide it, one will be auto-created for you with an empty name.
     *  Read on for a description of what omitting the name property on the root node means for path
     *  derivation.
     *  <p>
     *  If you're using the "children" modelType, you can provide the initial tree data via this
     *  property.  So, for example, to construct the following tree:
     *  <pre>
     *  foo
     *    bar
     *  zoo
     *  </pre>
     *  You would initialize the tree as follows: 
     *  <pre>
     *  Tree.create({
     *      root: { name:"root", children: [
     *          { name:"foo", children: [
     *              { name: "bar" }
     *          ]},
     *          { name: "zoo" }
     *      ]}
     *  });
     *  </pre>
     *  Note that if you provide a <code>name</code> property for the root node, then the path to
     *  any node underneath it will start with that name.  So in the example above, the path to the
     *  <code>bar</code> node would be <code>root/foo/bar</code> (assuming you're using the default
     * {@link com.smartgwt.client.widgets.tree.Tree#getPathDelim pathDelim}.  If you omit the name attribute on the root node,
     * then it's name
     * is automatically set to the {@link com.smartgwt.client.widgets.tree.Tree#getPathDelim pathDelim} value.  So in the
     * example above, if
     *  you omitted <code>name:"root"</code>, then the path to the <code>bar</code> node would be
     *  <code>/foo/bar</code>.
     *  <br><br>
     *  Note: if you initialize a Tree with no <code>root</code> value, a root node will be
     * auto-created for you.  You can then call {@link com.smartgwt.client.widgets.tree.Tree#add Tree.add} to construct the
     * tree.
     *
     * <br><br>If this method is called after the component has been drawn/initialized:
     * Set the root node of the tree.
     *
     * @param root new root node. Default value is null
     * @see com.smartgwt.client.widgets.tree.Tree#setModelType
     * @see com.smartgwt.client.widgets.tree.Tree#setRoot
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#tree_databinding_children_arrays" target="examples">Children Arrays Example</a>
     */
    public void setRoot(TreeNode root) {
        //setAttribute("root", root.getJsObj(), true);
        setAttribute("root", root, true);
    }

    /**
     * Should folders be sorted separately from leaves or should nodes be ordered according to their sort field value
     * regardless of whether the node is a leaf or folder?
     *
     * @param separateFolders separateFolders Default value is false
     * @see com.smartgwt.client.widgets.tree.Tree#setSortFoldersBeforeLeaves
     */
    public void setSeparateFolders(Boolean separateFolders) {
        setAttribute("separateFolders", separateFolders, true);
    }

    /**
     * Should folders be sorted separately from leaves or should nodes be ordered according to their sort field value
     * regardless of whether the node is a leaf or folder?
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tree.Tree#getSortFoldersBeforeLeaves
     */
    public Boolean getSeparateFolders()  {
        return getAttributeAsBoolean("separateFolders");
    }

    /**
     * Controls whether a call to {@link com.smartgwt.client.widgets.tree.Tree#getOpenList Tree.getOpenList} includes the root
     * node.  Since view components such as {@link com.smartgwt.client.widgets.tree.TreeGrid} use <code>getOpenList()</code> to
     * display the currently visible tree, <code>showRoot</code> controls whether the root node is shown to the user. <P> All
     * Trees must have a single, logical root, however, most applications want to show multiple nodes at the top level. 
     * <code>showRoot:false</code>, the default setting, prevents the logical root from being shown, so that the displayed tree
     * begins with the children of root. <P> You can set <code>showRoot:true</code> to show the single, logical root node as
     * the only top-level node.  This property is only meaningful for Trees where you supplied a value for {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot root}, otherwise, you will see an automatically generated root node that
     * is meaningless to the user.
     *
     * @param showRoot showRoot Default value is false
     */
    public void setShowRoot(Boolean showRoot) {
        setAttribute("showRoot", showRoot, true);
    }

    /**
     * Controls whether a call to {@link com.smartgwt.client.widgets.tree.Tree#getOpenList Tree.getOpenList} includes the root
     * node.  Since view components such as {@link com.smartgwt.client.widgets.tree.TreeGrid} use <code>getOpenList()</code> to
     * display the currently visible tree, <code>showRoot</code> controls whether the root node is shown to the user. <P> All
     * Trees must have a single, logical root, however, most applications want to show multiple nodes at the top level. 
     * <code>showRoot:false</code>, the default setting, prevents the logical root from being shown, so that the displayed tree
     * begins with the children of root. <P> You can set <code>showRoot:true</code> to show the single, logical root node as
     * the only top-level node.  This property is only meaningful for Trees where you supplied a value for {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot root}, otherwise, you will see an automatically generated root node that
     * is meaningless to the user.
     *
     *
     * @return Boolean
     */
    public Boolean getShowRoot()  {
        return getAttributeAsBoolean("showRoot");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.Tree#getSeparateFolders separateFolders} is true, should folders be displayed
     * above or below leaves? When set to <code>true</code> folders will appear above leaves when the
     * <code>sortDirection</code> applied to the tree is  ASCENDING
     *
     * @param sortFoldersBeforeLeaves sortFoldersBeforeLeaves Default value is true
     */
    public void setSortFoldersBeforeLeaves(Boolean sortFoldersBeforeLeaves) {
        setAttribute("sortFoldersBeforeLeaves", sortFoldersBeforeLeaves, true);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tree.Tree#getSeparateFolders separateFolders} is true, should folders be displayed
     * above or below leaves? When set to <code>true</code> folders will appear above leaves when the
     * <code>sortDirection</code> applied to the tree is  ASCENDING
     *
     *
     * @return Boolean
     */
    public Boolean getSortFoldersBeforeLeaves()  {
        return getAttributeAsBoolean("sortFoldersBeforeLeaves");
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds the title of the node as it
     * should be shown to the user.  Default value is "title".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getTitle
     * title} for usage.
     *
     * @param titleProperty titleProperty Default value is "title"
     */
    public void setTitleProperty(String titleProperty) {
        setAttribute("titleProperty", titleProperty, true);
    }

    /**
     * Name of the property on a {@link com.smartgwt.client.widgets.tree.TreeNode} that holds the title of the node as it
     * should be shown to the user.  Default value is "title".  See {@link com.smartgwt.client.widgets.tree.TreeNode#getTitle
     * title} for usage.
     *
     *
     * @return String
     */
    public String getTitleProperty()  {
        return getAttributeAsString("titleProperty");
    }

    // ********************* Static Methods ***********************

    /**
     * Optional initial data for the tree. How this data is interpreted depends on this tree's {@link
     * com.smartgwt.client.widgets.tree.Tree#getModelType modelType}. <P> If <code>modelType</code> is
     * <code>"parent"</code>, the list that you provide will be passed  to {@link com.smartgwt.client.widgets.tree.Tree#linkNodes},
     * integrating the nodes into the tree. <p> In this case the root node must be supplied separately via {@link
     * com.smartgwt.client.widgets.tree.Tree#getRoot root}, or you may instead provide the <code>id</code> of the root
     * node via {@link com.smartgwt.client.widgets.tree.Tree#getRootValue rootValue}.  So for example, to create this
     * tree: <pre> foo   bar zoo </pre> with modelType:"parent", you can do this: <pre> Tree.create({   root: {id:
     * "root"},   data: [     {name: "foo", id: "foo", parentId: "root"},     {name: "bar", id: "bar", parentId:
     * "foo"},
     * {name: "zoo", id: "zoo", parentId: "root"} }); </pre> Or this: <pre> Tree.create({   rootValue: "root",
     * data: [     {name: "foo", id: "foo", parentId: "root"},     {name: "bar", id: "bar", parentId: "foo"},     {name:
     * "zoo", id: "zoo", parentId: "root"} }); </pre> Specifying the root node explicitly allows you to give it a name,
     * changing the way path derivation works (see {@link com.smartgwt.client.widgets.tree.Tree#getRoot root} for more
     * on naming the root node). <P> For <code>modelType:"children"</code> trees, the data passed in will be assumed to
     * be an  array of children the tree's root node.
     *
     * @param data data Default value is null
     * @throws IllegalStateException this property cannot be changed after the underlying component has been created
     */
    public void setData(TreeNode[] nodes) {
        setAttribute("data", nodes, false);
    }
}



