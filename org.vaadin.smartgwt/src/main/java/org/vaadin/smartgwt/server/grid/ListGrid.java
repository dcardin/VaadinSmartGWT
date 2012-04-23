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
package org.vaadin.smartgwt.server.grid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vaadin.smartgwt.client.ui.grid.VListGrid;
import org.vaadin.smartgwt.server.Button;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.InjectorSingleton;
import org.vaadin.smartgwt.server.core.ComponentList;
import org.vaadin.smartgwt.server.core.ComponentPropertyPainter;
import org.vaadin.smartgwt.server.data.DataSource;
import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.grid.events.HasSelectionChangedHandlers;
import org.vaadin.smartgwt.server.grid.events.HasSelectionUpdatedHandlers;
import org.vaadin.smartgwt.server.grid.events.SelectionChangedHandler;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedHandler;
import org.vaadin.smartgwt.server.types.AnimationAcceleration;
import org.vaadin.smartgwt.server.types.AutoFitEvent;
import org.vaadin.smartgwt.server.types.AutoFitIconFieldType;
import org.vaadin.smartgwt.server.types.AutoFitWidthApproach;
import org.vaadin.smartgwt.server.types.Autofit;
import org.vaadin.smartgwt.server.types.ChartType;
import org.vaadin.smartgwt.server.types.DateDisplayFormat;
import org.vaadin.smartgwt.server.types.DragDataAction;
import org.vaadin.smartgwt.server.types.DragTrackerMode;
import org.vaadin.smartgwt.server.types.EmbeddedPosition;
import org.vaadin.smartgwt.server.types.EnterKeyEditAction;
import org.vaadin.smartgwt.server.types.EscapeKeyEditAction;
import org.vaadin.smartgwt.server.types.ExpansionMode;
import org.vaadin.smartgwt.server.types.FetchMode;
import org.vaadin.smartgwt.server.types.GroupStartOpen;
import org.vaadin.smartgwt.server.types.HoverMode;
import org.vaadin.smartgwt.server.types.ListGridEditEvent;
import org.vaadin.smartgwt.server.types.Overflow;
import org.vaadin.smartgwt.server.types.RecordComponentPoolingMode;
import org.vaadin.smartgwt.server.types.RowEndEditAction;
import org.vaadin.smartgwt.server.types.SelectionAppearance;
import org.vaadin.smartgwt.server.types.SelectionStyle;
import org.vaadin.smartgwt.server.types.SortArrow;
import org.vaadin.smartgwt.server.types.TextMatchStyle;
import org.vaadin.smartgwt.server.util.EnumUtil;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

/**
 * A ListGrid is a {@link com.smartgwt.client.widgets.DataBoundComponent} that displays a list of objects in a grid, where
 * each row represents one object and each cell in the row represents one property.
 */
@com.vaadin.ui.ClientWidget(VListGrid.class)
public class ListGrid extends Canvas implements HasSelectionChangedHandlers, HasSelectionUpdatedHandlers {
	private final ComponentPropertyPainter propertyPainter = new ComponentPropertyPainter(this);
	private final ComponentList<ListGridField> fields = propertyPainter.addComponentList("fields");
	private final Set<SelectionChangedHandler> selectionChangedHandlers = Sets.newHashSet();
	private final Set<SelectionUpdatedHandler> selectionUpdatedHandlers = Sets.newHashSet();
	private DataSource dataSource;
	private ListGridRecord[] selectedRecords;
	private SelectionEventFactory selectionEventFactory;


	public ListGrid() {
		setModalEditing(true);
		scClassName = "ListGrid";
	}

	/**
	 * For use with {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor}:true, allows simple
	 * search expressions to be entered into filter fields, as though {@link
	 * com.smartgwt.client.widgets.form.DynamicForm#getAllowExpressions allowExpressions} were true. <P> Can also be enabled or
	 * disabled on a field-by-field basis via {@link com.smartgwt.client.widgets.grid.ListGridField#getFilterEditorProperties
	 * filterEditorProperties}.
	 *
	 * @param allowFilterExpressions allowFilterExpressions Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAllowFilterExpressions(Boolean allowFilterExpressions) throws IllegalStateException {
		setAttribute("allowFilterExpressions", allowFilterExpressions, false);
	}

	/**
	 * For use with {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor}:true, allows simple
	 * search expressions to be entered into filter fields, as though {@link
	 * com.smartgwt.client.widgets.form.DynamicForm#getAllowExpressions allowExpressions} were true. <P> Can also be enabled or
	 * disabled on a field-by-field basis via {@link com.smartgwt.client.widgets.grid.ListGridField#getFilterEditorProperties
	 * filterEditorProperties}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAllowFilterExpressions() {
		return getAttributeAsBoolean("allowFilterExpressions");
	}

	/**
	 * Optional css style to apply to the body if {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateRecordStyles
	 * alternateRecordStyles} is true  for this grid. If unset {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBodyStyleName bodyStyleName} will be used to style the body regardless of
	 * the {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateRecordStyles alternateRecordStyles} setting.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Update the {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateBodyStyleName alternateBodyStyleName} for this listGrid.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param alternateBodyStyleName new body style name when showing alternateRecordStyles. Default value is null
	 */
	public void setAlternateBodyStyleName(String alternateBodyStyleName) {
		setAttribute("alternateBodyStyleName", alternateBodyStyleName, true);
	}

	/**
	 * Optional css style to apply to the body if {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateRecordStyles
	 * alternateRecordStyles} is true  for this grid. If unset {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBodyStyleName bodyStyleName} will be used to style the body regardless of
	 * the {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateRecordStyles alternateRecordStyles} setting.
	 *
	 *
	 * @return String
	 */
	public String getAlternateBodyStyleName() {
		return getAttributeAsString("alternateBodyStyleName");
	}

	/**
	 * The number of consecutive rows to draw in the same style before alternating, when alternateRowStyles is true.
	 *
	 * @param alternateRecordFrequency alternateRecordFrequency Default value is 1
	 */
	public void setAlternateRecordFrequency(int alternateRecordFrequency) {
		setAttribute("alternateRecordFrequency", alternateRecordFrequency, true);
	}

	/**
	 * The number of consecutive rows to draw in the same style before alternating, when alternateRowStyles is true.
	 *
	 *
	 * @return int
	 */
	public int getAlternateRecordFrequency() {
		return getAttributeAsInt("alternateRecordFrequency");
	}

	/**
	 * Whether alternating rows should be drawn in alternating styles, in order to create a "ledger" effect for easier reading.
	 *  If enabled, the cell style for alternate rows will have "Dark" appended to it.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAlternateRecordStyles alternateRecordStyles}
	 *
	 * @param alternateRecordStyles New value for <code>this.alternateRecordStyles</code>. Default value is false
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_lf_grid_cells" target="examples">Grid cells Example</a>
	 */
	public void setAlternateRecordStyles(Boolean alternateRecordStyles) {
		setAttribute("alternateRecordStyles", alternateRecordStyles, true);
	}

	/**
	 * Whether alternating rows should be drawn in alternating styles, in order to create a "ledger" effect for easier reading.
	 *  If enabled, the cell style for alternate rows will have "Dark" appended to it.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_lf_grid_cells" target="examples">Grid cells Example</a>
	 */
	public Boolean getAlternateRecordStyles() {
		return getAttributeAsBoolean("alternateRecordStyles");
	}

	/**
	 * When this attribute is set, editors will be rendered into every row of the grid rather than showing up in a single
	 * record at a time. This attribute is only valid when {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell
	 * editByCell} is false
	 *
	 * @param alwaysShowEditors alwaysShowEditors Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setAlwaysShowEditors(Boolean alwaysShowEditors) throws IllegalStateException {
		setAttribute("alwaysShowEditors", alwaysShowEditors, false);
	}

	/**
	 * When this attribute is set, editors will be rendered into every row of the grid rather than showing up in a single
	 * record at a time. This attribute is only valid when {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell
	 * editByCell} is false
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getAlwaysShowEditors() {
		return getAttributeAsBoolean("alwaysShowEditors");
	}

	/**
	 * When animating folder opening / closing, this property can be set to apply an animated acceleration effect. This allows
	 * the animation speed to be "weighted", for example expanding or collapsing at a faster rate toward the beginning of the
	 * animation than at the end. <P> For a ListGrid, this property applies when {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 * @param animateFolderEffect animateFolderEffect Default value is null
	 */
	public void setAnimateFolderEffect(AnimationAcceleration animateFolderEffect) {
		setAttribute("animateFolderEffect", animateFolderEffect.getValue(), true);
	}

	/**
	 * When animating folder opening / closing, this property can be set to apply an animated acceleration effect. This allows
	 * the animation speed to be "weighted", for example expanding or collapsing at a faster rate toward the beginning of the
	 * animation than at the end. <P> For a ListGrid, this property applies when {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 *
	 * @return AnimationAcceleration
	 */
	public AnimationAcceleration getAnimateFolderEffect() {
		return EnumUtil.getEnum(AnimationAcceleration.values(), getAttribute("animateFolderEffect"));
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateFolders animateFolders} is true for this grid, this number
	 * can be set to designate the maximum number of rows to animate at a time when opening / closing a folder. <P> For a
	 * ListGrid, this property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is
	 * enabled.
	 *
	 * @param animateFolderMaxRows animateFolderMaxRows Default value is null
	 */
	public void setAnimateFolderMaxRows(Integer animateFolderMaxRows) {
		setAttribute("animateFolderMaxRows", animateFolderMaxRows, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateFolders animateFolders} is true for this grid, this number
	 * can be set to designate the maximum number of rows to animate at a time when opening / closing a folder. <P> For a
	 * ListGrid, this property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is
	 * enabled.
	 *
	 *
	 * @return Integer
	 */
	public Integer getAnimateFolderMaxRows() {
		return getAttributeAsInt("animateFolderMaxRows");
	}

	/**
	 * If true, when folders are opened / closed children will be animated into view. <P> For a ListGrid, this property applies
	 * when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 * @param animateFolders animateFolders Default value is true
	 */
	public void setAnimateFolders(Boolean animateFolders) {
		setAttribute("animateFolders", animateFolders, true);
	}

	/**
	 * If true, when folders are opened / closed children will be animated into view. <P> For a ListGrid, this property applies
	 * when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateFolders() {
		return getAttributeAsBoolean("animateFolders");
	}

	/**
	 * When animating folder opening / closing, this property designates the speed of the animation in pixels shown (or hidden)
	 * per second. Takes precedence over the  {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderTime
	 * animateFolderTime} property, which allows the developer to specify a duration for the animation rather than a speed. <P>
	 * For a ListGrid, this property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is
	 * enabled.
	 *
	 * @param animateFolderSpeed animateFolderSpeed Default value is 3000
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setAnimateFolderTime
	 */
	public void setAnimateFolderSpeed(int animateFolderSpeed) {
		setAttribute("animateFolderSpeed", animateFolderSpeed, true);
	}

	/**
	 * When animating folder opening / closing, this property designates the speed of the animation in pixels shown (or hidden)
	 * per second. Takes precedence over the  {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderTime
	 * animateFolderTime} property, which allows the developer to specify a duration for the animation rather than a speed. <P>
	 * For a ListGrid, this property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is
	 * enabled.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getAnimateFolderTime
	 */
	public int getAnimateFolderSpeed() {
		return getAttributeAsInt("animateFolderSpeed");
	}

	/**
	 * When animating folder opening / closing, if {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderSpeed
	 * animateFolderSpeed} is not set, this property designates the duration of the animation in ms. <P> For a ListGrid, this
	 * property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 * @param animateFolderTime animateFolderTime Default value is 100
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setAnimateFolderSpeed
	 */
	public void setAnimateFolderTime(int animateFolderTime) {
		setAttribute("animateFolderTime", animateFolderTime, true);
	}

	/**
	 * When animating folder opening / closing, if {@link com.smartgwt.client.widgets.tree.TreeGrid#getAnimateFolderSpeed
	 * animateFolderSpeed} is not set, this property designates the duration of the animation in ms. <P> For a ListGrid, this
	 * property applies when {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping} is enabled.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getAnimateFolderSpeed
	 */
	public int getAnimateFolderTime() {
		return getAttributeAsInt("animateFolderTime");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} is enabled, should records
	 * be animated out of view when they are removed by the user?
	 *
	 * @param animateRemoveRecord animateRemoveRecord Default value is true
	 */
	public void setAnimateRemoveRecord(Boolean animateRemoveRecord) {
		setAttribute("animateRemoveRecord", animateRemoveRecord, true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} is enabled, should records
	 * be animated out of view when they are removed by the user?
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateRemoveRecord() {
		return getAttributeAsBoolean("animateRemoveRecord");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord animating record removal}, this property 
	 * designates the speed of the animation in pixels per second. Takes precedence over the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveTime animateRemoveTime} property, which allows the developer
	 * to specify a duration for the animation rather than a speed.
	 *
	 * @param animateRemoveSpeed animateRemoveSpeed Default value is 200
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setAnimateRemoveRecord
	 */
	public void setAnimateRemoveSpeed(int animateRemoveSpeed) {
		setAttribute("animateRemoveSpeed", animateRemoveSpeed, true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord animating record removal}, this property 
	 * designates the speed of the animation in pixels per second. Takes precedence over the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveTime animateRemoveTime} property, which allows the developer
	 * to specify a duration for the animation rather than a speed.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord
	 */
	public int getAnimateRemoveSpeed() {
		return getAttributeAsInt("animateRemoveSpeed");
	}

	/**
	 * When animating record removal  {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord (see
	 * animateRemoveRecord)}, if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveSpeed animateRemoveSpeed} is
	 * not set, this property designates the duration of the animation in ms.
	 *
	 * @param animateRemoveTime animateRemoveTime Default value is 100
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setAnimateRemoveRecord
	 */
	public void setAnimateRemoveTime(int animateRemoveTime) {
		setAttribute("animateRemoveTime", animateRemoveTime, true);
	}

	/**
	 * When animating record removal  {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord (see
	 * animateRemoveRecord)}, if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveSpeed animateRemoveSpeed} is
	 * not set, this property designates the duration of the animation in ms.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord
	 */
	public int getAnimateRemoveTime() {
		return getAttributeAsInt("animateRemoveTime");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOverCanvas showRollOverCanvas} is <code>true</code>
	 * setting this property to true ensures that when the rollOver canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be customized via
	 * the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param animateRollOver animateRollOver Default value is false
	 */
	public void setAnimateRollOver(Boolean animateRollOver) {
		setAttribute("animateRollOver", animateRollOver, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOverCanvas showRollOverCanvas} is <code>true</code>
	 * setting this property to true ensures that when the rollOver canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be customized via
	 * the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateRollOver() {
		return getAttributeAsBoolean("animateRollOver");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOverCanvas showRollOverCanvas} is <code>true</code>
	 * setting this property to true ensures that when the rollUnder canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be customized via
	 * the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param animateRollUnder animateRollUnder Default value is false
	 */
	public void setAnimateRollUnder(Boolean animateRollUnder) {
		setAttribute("animateRollUnder", animateRollUnder, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOverCanvas showRollOverCanvas} is <code>true</code>
	 * setting this property to true ensures that when the rollUnder canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be customized via
	 * the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateRollUnder() {
		return getAttributeAsBoolean("animateRollUnder");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowSelectionCanvas showSelectionCanvas} is <code>true</code>
	 * setting this property to true ensures that when the selection canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}.  Note that the animation effect may be customized
	 * via the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param animateSelection animateSelection Default value is false
	 */
	public void setAnimateSelection(Boolean animateSelection) {
		setAttribute("animateSelection", animateSelection, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowSelectionCanvas showSelectionCanvas} is <code>true</code>
	 * setting this property to true ensures that when the selection canvas is displayed it animates into view via an {@link
	 * com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}.  Note that the animation effect may be customized
	 * via the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateSelection() {
		return getAttributeAsBoolean("animateSelection");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowSelectionCanvas showSelectionCanvas} is <code>true</code>
	 * setting this property to true ensures that when the selectionUnder canvas is displayed it animates into view via an
	 * {@link com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be
	 * customized via the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param animateSelectionUnder animateSelectionUnder Default value is false
	 */
	public void setAnimateSelectionUnder(Boolean animateSelectionUnder) {
		setAttribute("animateSelectionUnder", animateSelectionUnder, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowSelectionCanvas showSelectionCanvas} is <code>true</code>
	 * setting this property to true ensures that when the selectionUnder canvas is displayed it animates into view via an
	 * {@link com.smartgwt.client.widgets.Canvas#animateShow Canvas.animateShow}. Note that the animation effect may be
	 * customized via the standard {@link com.smartgwt.client.widgets.Canvas#getAnimateShowEffect animateShowEffect}, {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowTime animateShowTime} and  {@link
	 * com.smartgwt.client.widgets.Canvas#getAnimateShowAcceleration animateShowAcceleration}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAnimateSelectionUnder() {
		return getAttributeAsBoolean("animateSelectionUnder");
	}

	/**
	 * Action to perform when the listGrid has keyboard focus (but not editing focus) and a user presses the up or down arrow
	 * key. Possible values are: <ul> <li><code>select</code> : select the next row in the list (calls <code>recordClick</code>
	 * handler)</li> <li><code>focus</code> : move focus to the next row in the list without changing the selection</li>
	 * <li><code>activate</code> : select and activate the next row in the list (calls  <code>recordDoubleClick</code>
	 * handler)</li> <li><code>none</code> : no action</li> </ul>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param arrowKeyAction arrowKeyAction Default value is "select"
	 */
	public void setArrowKeyAction(String arrowKeyAction) {
		setAttribute("arrowKeyAction", arrowKeyAction, true);
	}

	/**
	 * Action to perform when the listGrid has keyboard focus (but not editing focus) and a user presses the up or down arrow
	 * key. Possible values are: <ul> <li><code>select</code> : select the next row in the list (calls <code>recordClick</code>
	 * handler)</li> <li><code>focus</code> : move focus to the next row in the list without changing the selection</li>
	 * <li><code>activate</code> : select and activate the next row in the list (calls  <code>recordDoubleClick</code>
	 * handler)</li> <li><code>none</code> : no action</li> </ul>
	 *
	 *
	 * @return String
	 */
	public String getArrowKeyAction() {
		return getAttributeAsString("arrowKeyAction");
	}

	/**
	 * If true, for fields where {@link com.smartgwt.client.widgets.grid.ListGridField#getOptionDataSource optionDataSource} is
	 * specified, a valueMap will be automatically created by making a {@link com.smartgwt.client.data.DataSource#fetchData
	 * DataSource.fetchData} call against the specified dataSource and extracting a valueMap from the returned records based on
	 * the displayField and valueField. <P> If set to false, valueMaps will not be automatically fetched.  In this case,
	 * setting field.optionDataSource is effectively a shortcut for setting optionDataSource on the editor via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getEditorProperties editorProperties}. <P> Can also be disabled on a
	 * per-field basis with {@link com.smartgwt.client.widgets.grid.ListGridField#getAutoFetchDisplayMap autoFetchDisplayMap}.
	 *
	 * @param autoFetchDisplayMap autoFetchDisplayMap Default value is true
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setAutoFetchDisplayMap
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setOptionDataSource
	 */
	public void setAutoFetchDisplayMap(Boolean autoFetchDisplayMap) {
		setAttribute("autoFetchDisplayMap", autoFetchDisplayMap, true);
	}

	/**
	 * If true, for fields where {@link com.smartgwt.client.widgets.grid.ListGridField#getOptionDataSource optionDataSource} is
	 * specified, a valueMap will be automatically created by making a {@link com.smartgwt.client.data.DataSource#fetchData
	 * DataSource.fetchData} call against the specified dataSource and extracting a valueMap from the returned records based on
	 * the displayField and valueField. <P> If set to false, valueMaps will not be automatically fetched.  In this case,
	 * setting field.optionDataSource is effectively a shortcut for setting optionDataSource on the editor via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getEditorProperties editorProperties}. <P> Can also be disabled on a
	 * per-field basis with {@link com.smartgwt.client.widgets.grid.ListGridField#getAutoFetchDisplayMap autoFetchDisplayMap}.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getAutoFetchDisplayMap
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getOptionDataSource
	 */
	public Boolean getAutoFetchDisplayMap() {
		return getAttributeAsBoolean("autoFetchDisplayMap");
	}

	/**
	 * When this grid is initially filtered via {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchData
	 * autoFetchData}, or filtered by the user  via the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor
	 * filterEditor}, this attribute can be used to set the <code>textMatchStyle</code> on the dsRequest passed to
	 * <code>fetchData()</code>.
	 *
	 * @param autoFetchTextMatchStyle autoFetchTextMatchStyle Default value is "substring"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAutoFetchTextMatchStyle(TextMatchStyle autoFetchTextMatchStyle) throws IllegalStateException {
		setAttribute("autoFetchTextMatchStyle", autoFetchTextMatchStyle.getValue(), false);
	}

	/**
	 * When this grid is initially filtered via {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchData
	 * autoFetchData}, or filtered by the user  via the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor
	 * filterEditor}, this attribute can be used to set the <code>textMatchStyle</code> on the dsRequest passed to
	 * <code>fetchData()</code>.
	 *
	 *
	 * @return TextMatchStyle
	 */
	public TextMatchStyle getAutoFetchTextMatchStyle() {
		return EnumUtil.getEnum(TextMatchStyle.values(), getAttribute("autoFetchTextMatchStyle"));
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields canAutoFitFields} is true, this
	 * attribute will be shown as the menu  item title for an item to perform a one-time autoFit of all visible fields via the
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField} method.
	 *
	 * @param autoFitAllText autoFitAllText Default value is "Auto Fit All Columns"
	 */
	public void setAutoFitAllText(String autoFitAllText) {
		setAttribute("autoFitAllText", autoFitAllText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields canAutoFitFields} is true, this
	 * attribute will be shown as the menu  item title for an item to perform a one-time autoFit of all visible fields via the
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField} method.
	 *
	 *
	 * @return String
	 */
	public String getAutoFitAllText() {
		return getAttributeAsString("autoFitAllText");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} is enabled and the
	 * calculated field sizes are wide enough that horizontal scrolling would be introduced, this attribute may be set to an
	 * array of fieldNames, causing those fields to be clipped rather than forcing horizontal scrollbars to appear. <P> Note:
	 * If any {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen columns} are included in this list they
	 * will not be clipped.
	 *
	 * @param autoFitClipFields autoFitClipFields Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAutoFitClipFields(String... autoFitClipFields) throws IllegalStateException {
		setAttribute("autoFitClipFields", autoFitClipFields, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} is enabled and the
	 * calculated field sizes are wide enough that horizontal scrolling would be introduced, this attribute may be set to an
	 * array of fieldNames, causing those fields to be clipped rather than forcing horizontal scrollbars to appear. <P> Note:
	 * If any {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen columns} are included in this list they
	 * will not be clipped.
	 *
	 *
	 * @return String
	 */
	public String[] getAutoFitClipFields() {
		return getAttributeAsStringArray("autoFitClipFields");
	}

	/**
	 * The field to expand if {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} and 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldsFillViewport autoFitFieldsFillViewport} are enabled and
	 * auto-fitting will not fill all available horizontal space. <P> If unset, will default to the text field with the longest
	 * {@link com.smartgwt.client.data.DataSourceField#getLength length} if length is set, otherwise, the first text field with
	 * no width specified. <P> Note that expanding {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen
	 * columns} is not supported.
	 *
	 * @param autoFitExpandField autoFitExpandField Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAutoFitExpandField(String autoFitExpandField) throws IllegalStateException {
		setAttribute("autoFitExpandField", autoFitExpandField, false);
	}

	/**
	 * The field to expand if {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} and 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldsFillViewport autoFitFieldsFillViewport} are enabled and
	 * auto-fitting will not fill all available horizontal space. <P> If unset, will default to the text field with the longest
	 * {@link com.smartgwt.client.data.DataSourceField#getLength length} if length is set, otherwise, the first text field with
	 * no width specified. <P> Note that expanding {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen
	 * columns} is not supported.
	 *
	 *
	 * @return String
	 */
	public String getAutoFitExpandField() {
		return getAttributeAsString("autoFitExpandField");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code>  this property specifies the number of additional records for which the grid  will expand. If more
	 * records are present, scrolling will be introduced to reach them as normal.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitExtraRecords autoFitExtraRecords}.
	 *
	 * @param autoFitExtraRecords Number of extra rows beyond the data-size we'll expand to  accommodate if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData auto fit} is enabled vertically.. Default value is null
	 */
	public void setAutoFitExtraRecords(Integer autoFitExtraRecords) {
		setAttribute("autoFitExtraRecords", autoFitExtraRecords, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code>  this property specifies the number of additional records for which the grid  will expand. If more
	 * records are present, scrolling will be introduced to reach them as normal.
	 *
	 *
	 * @return Integer
	 */
	public Integer getAutoFitExtraRecords() {
		return getAttributeAsInt("autoFitExtraRecords");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} is enabled, and
	 * auto-fitting all field widths will not take up all the horizontal space available in the viewport, should a field be
	 * expanded wider than it's calculated auto-fit-width to fill the available space and avoid leaving a gap. <P> If true, the
	 * field to expand may be specified via {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitExpandField
	 * autoFitExpandField} <P> Note this logic will not expand a {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen column}.
	 *
	 * @param autoFitFieldsFillViewport autoFitFieldsFillViewport Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAutoFitFieldsFillViewport(Boolean autoFitFieldsFillViewport) throws IllegalStateException {
		setAttribute("autoFitFieldsFillViewport", autoFitFieldsFillViewport, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths} is enabled, and
	 * auto-fitting all field widths will not take up all the horizontal space available in the viewport, should a field be
	 * expanded wider than it's calculated auto-fit-width to fill the available space and avoid leaving a gap. <P> If true, the
	 * field to expand may be specified via {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitExpandField
	 * autoFitExpandField} <P> Note this logic will not expand a {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen column}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAutoFitFieldsFillViewport() {
		return getAttributeAsBoolean("autoFitFieldsFillViewport");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields canAutoFitFields} is true, this
	 * attribute will be shown as the menu  item title for an item to perform a one-time autoFit of the field to its title or
	 * content via a call to {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField}.
	 *
	 * @param autoFitFieldText autoFitFieldText Default value is "Auto Fit"
	 */
	public void setAutoFitFieldText(String autoFitFieldText) {
		setAttribute("autoFitFieldText", autoFitFieldText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields canAutoFitFields} is true, this
	 * attribute will be shown as the menu  item title for an item to perform a one-time autoFit of the field to its title or
	 * content via a call to {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField}.
	 *
	 *
	 * @return String
	 */
	public String getAutoFitFieldText() {
		return getAttributeAsString("autoFitFieldText");
	}

	/**
	 * Enables autofitting of fields to values or titles. This property may be overridden on a per-field basis via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth}.<br> The {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitWidthApproach autoFitWidthApproach} controls whether fitting is to
	 * values, titles  or both. This property may also be overridden on a per field basis. <P> If  width is also set on the
	 * field, it will be taken as a minimum width. {@link com.smartgwt.client.widgets.grid.ListGrid#getMinFieldWith
	 * minFieldWith} will also be respected. <P> Autofitting will be performed: <ul>  <li> whenever the dataset is completely
	 * changed or rows are added or removed  <li> whenever a field which is autofitting is changed  <li> on a manual call to
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField} or       {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#autoFitFields ListGrid.autoFitFields} </ul> Autofitting behavior continues
	 * until the user resizes the field manually, at which point it stops. The user can also perform a one-time auto-fit of
	 * fields via the header context menu if {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields
	 * canAutoFitFields} is enabled. <P> When autofitting to column values, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} will be called to
	 * determine the space required for a field's values. This method uses values from the rendered set of rows to calculate
	 * the required column width. The values used not match the complete set of data for the grid when rendering rows
	 * incrementally. See {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllRecords showAllRecords} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio}) to control incremental rendering of rows.
	 * <P> Note that for <code>icon</code> type fields, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitIconFields autoFitIconFields}  property setting may turn on
	 * auto-fit-width behavior for specific fields by default, even if <code>autoFitFieldWidths</code> is false for the grid as
	 * a whole. <P> Using this feature has a performance penalty roughly comparable to always rendering  one additional field
	 * per field where autofitting is enabled.  Specifically, enabling it for all fields would be comparable to <i>both</i>
	 * doubling the number of fields <i>and</i> disabling {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllColumns
	 * horizontal incremental rendering}. In a grid where only half the fields are normally visible and hence only half are
	 * normally rendered, this would be roughly 4 times slower overall.<br> This performance penalty is a result of {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} having to render out the
	 * data set offscreen and measure the rendered content - it does not apply for cases where this method can return a simple
	 * fixed values (as with icon fields). <P> Which fields are currently autofitting is saved as part of the  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getViewState view state} of the ListGrid.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths autoFitFieldWidths}. Modifies the default auto-fit-width behavior for fields in this grid. Note that this may be overridden at the field level via  {@link com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth}.
	 *
	 * @param autoFitFieldWidths New value for autoFitFieldWidths. Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setAutoFitFieldWidths(Boolean autoFitFieldWidths) throws IllegalStateException {
		setAttribute("autoFitFieldWidths", autoFitFieldWidths, false);
	}

	/**
	 * Enables autofitting of fields to values or titles. This property may be overridden on a per-field basis via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth}.<br> The {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitWidthApproach autoFitWidthApproach} controls whether fitting is to
	 * values, titles  or both. This property may also be overridden on a per field basis. <P> If  width is also set on the
	 * field, it will be taken as a minimum width. {@link com.smartgwt.client.widgets.grid.ListGrid#getMinFieldWith
	 * minFieldWith} will also be respected. <P> Autofitting will be performed: <ul>  <li> whenever the dataset is completely
	 * changed or rows are added or removed  <li> whenever a field which is autofitting is changed  <li> on a manual call to
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#autoFitField ListGrid.autoFitField} or       {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#autoFitFields ListGrid.autoFitFields} </ul> Autofitting behavior continues
	 * until the user resizes the field manually, at which point it stops. The user can also perform a one-time auto-fit of
	 * fields via the header context menu if {@link com.smartgwt.client.widgets.grid.ListGrid#getCanAutoFitFields
	 * canAutoFitFields} is enabled. <P> When autofitting to column values, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} will be called to
	 * determine the space required for a field's values. This method uses values from the rendered set of rows to calculate
	 * the required column width. The values used not match the complete set of data for the grid when rendering rows
	 * incrementally. See {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllRecords showAllRecords} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio}) to control incremental rendering of rows.
	 * <P> Note that for <code>icon</code> type fields, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitIconFields autoFitIconFields}  property setting may turn on
	 * auto-fit-width behavior for specific fields by default, even if <code>autoFitFieldWidths</code> is false for the grid as
	 * a whole. <P> Using this feature has a performance penalty roughly comparable to always rendering  one additional field
	 * per field where autofitting is enabled.  Specifically, enabling it for all fields would be comparable to <i>both</i>
	 * doubling the number of fields <i>and</i> disabling {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllColumns
	 * horizontal incremental rendering}. In a grid where only half the fields are normally visible and hence only half are
	 * normally rendered, this would be roughly 4 times slower overall.<br> This performance penalty is a result of {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} having to render out the
	 * data set offscreen and measure the rendered content - it does not apply for cases where this method can return a simple
	 * fixed values (as with icon fields). <P> Which fields are currently autofitting is saved as part of the  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getViewState view state} of the ListGrid.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getAutoFitFieldWidths() {
		return getAttributeAsBoolean("autoFitFieldWidths");
	}

	/**
	 * Smart GWT listGrids have special logic to automatically size fields that are displayed as an icon - that is fields with 
	 * {@link com.smartgwt.client.types.ListGridFieldType type:"icon"}, fields displaying only {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getShowValueIconOnly value icons}, and boolean fields (which are rendered
	 * as a checkmark type icon by default. <P> This attribute controls this behavior - governing whether icon fields should be
	 * sized to fit their content (icon), title, or whether to disable this  behavior. Setting this value to
	 * <code>"title"</code> or <code>"iconWidth"</code> will cause {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth} to be enabled by default for all  icon
	 * fields with the {@link com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidthApproach autoFitWidthApproach} set
	 * to  <code>"value"</code> or <code>"both"</code> as appropriate. Note that the width required for the icons is calculated
	 * by {@link com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} which performs a
	 * simple calculation based on the specified icon width for these types of fields. <P> This setting governs default
	 * behavior for icon fields - for specific fields within a grid, this default behavior can be overridden by setting an
	 * explicit {@link com.smartgwt.client.widgets.grid.ListGridField#getWidth width} or explicitly enabling {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth} and setting {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidthApproach autoFitWidthApproach} on the field in question.
	 *
	 * @param autoFitIconFields autoFitIconFields Default value is "title"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setAutoFitFieldWidths
	 */
	public void setAutoFitIconFields(AutoFitIconFieldType autoFitIconFields) {
		setAttribute("autoFitIconFields", autoFitIconFields.getValue(), true);
	}

	/**
	 * Smart GWT listGrids have special logic to automatically size fields that are displayed as an icon - that is fields with 
	 * {@link com.smartgwt.client.types.ListGridFieldType type:"icon"}, fields displaying only {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getShowValueIconOnly value icons}, and boolean fields (which are rendered
	 * as a checkmark type icon by default. <P> This attribute controls this behavior - governing whether icon fields should be
	 * sized to fit their content (icon), title, or whether to disable this  behavior. Setting this value to
	 * <code>"title"</code> or <code>"iconWidth"</code> will cause {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth} to be enabled by default for all  icon
	 * fields with the {@link com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidthApproach autoFitWidthApproach} set
	 * to  <code>"value"</code> or <code>"both"</code> as appropriate. Note that the width required for the icons is calculated
	 * by {@link com.smartgwt.client.widgets.grid.ListGrid#getDefaultFieldWidth ListGrid.getDefaultFieldWidth} which performs a
	 * simple calculation based on the specified icon width for these types of fields. <P> This setting governs default
	 * behavior for icon fields - for specific fields within a grid, this default behavior can be overridden by setting an
	 * explicit {@link com.smartgwt.client.widgets.grid.ListGridField#getWidth width} or explicitly enabling {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidth autoFitWidth} and setting {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getAutoFitWidthApproach autoFitWidthApproach} on the field in question.
	 *
	 *
	 * @return AutoFitIconFieldType
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getAutoFitFieldWidths
	 */
	public AutoFitIconFieldType getAutoFitIconFields() {
		return EnumUtil.getEnum(AutoFitIconFieldType.values(), getAttribute("autoFitIconFields"));
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"horizontal"</code> or
	 * <code>"both"</code> this property provides the maximum number of columns for which the ListGrid will expand. If more
	 * columns are present, scrolling will be introduced to reach them as normal. If unset the ListGrid will expand to
	 * accommodate as many columns as are defined for the grid.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxColumns autoFitMaxColumns}.
	 *
	 * @param autoFitMaxColumns Maximum number of fields we'll expand to accommodate if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData
	 * auto fit} is enabled horizontally.. Default value is 50
	 */
	public void setAutoFitMaxColumns(int autoFitMaxColumns) {
		setAttribute("autoFitMaxColumns", autoFitMaxColumns, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"horizontal"</code> or
	 * <code>"both"</code> this property provides the maximum number of columns for which the ListGrid will expand. If more
	 * columns are present, scrolling will be introduced to reach them as normal. If unset the ListGrid will expand to
	 * accommodate as many columns as are defined for the grid.
	 *
	 *
	 * @return int
	 */
	public int getAutoFitMaxColumns() {
		return getAttributeAsInt("autoFitMaxColumns");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code> this property provides an upper limit on how far the ListGrid will expand vertically to accommodate
	 * its content. If content exceeds this height, scrollbars will be introduced as usual.   In addition to this property,
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxRecords autoFitMaxRecords} allows you to limit vertical
	 * expansion based on the number of rows to be rendered.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxHeight autoFitMaxHeight}.
	 *
	 * @param autoFitMaxHeight Maximum height in px we'll expand to accommodate if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData
	 * auto fit} is enabled vertically.. Default value is null
	 */
	public void setAutoFitMaxHeight(Integer autoFitMaxHeight) {
		setAttribute("autoFitMaxHeight", autoFitMaxHeight, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code> this property provides an upper limit on how far the ListGrid will expand vertically to accommodate
	 * its content. If content exceeds this height, scrollbars will be introduced as usual.   In addition to this property,
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxRecords autoFitMaxRecords} allows you to limit vertical
	 * expansion based on the number of rows to be rendered.
	 *
	 *
	 * @return Integer
	 */
	public Integer getAutoFitMaxHeight() {
		return getAttributeAsInt("autoFitMaxHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code> this property provides the maximum number of records for which the ListGrid will expand. If more
	 * records are present, scrolling will be introduced to reach them as normal. If unset, by default the ListGrid will expand
	 * to accommodate as many records as are present.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxRecords autoFitMaxRecords}.
	 *
	 * @param autoFitMaxRecords Maximum number of rows we'll expand to accommodate if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData
	 * auto fit} is enabled vertically.. Default value is 50
	 */
	public void setAutoFitMaxRecords(int autoFitMaxRecords) {
		setAttribute("autoFitMaxRecords", autoFitMaxRecords, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"vertical"</code> or
	 * <code>"both"</code> this property provides the maximum number of records for which the ListGrid will expand. If more
	 * records are present, scrolling will be introduced to reach them as normal. If unset, by default the ListGrid will expand
	 * to accommodate as many records as are present.
	 *
	 *
	 * @return int
	 */
	public int getAutoFitMaxRecords() {
		return getAttributeAsInt("autoFitMaxRecords");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"horizontal"</code> or
	 * <code>"both"</code> this property provides an upper limit on how far the ListGrid will expand horizontally to
	 * accommodate its content.  If content exceeds this width, scrollbars will be introduced as usual.   In addition to this
	 * property, {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxColumns autoFitMaxColumns} allows you to limit 
	 * horizontal expansion based on the number of columns to be rendered.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxWidth autoFitMaxWidth}.
	 *
	 * @param autoFitMaxWidth Width in px we'll expand to accommodate if  {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData auto fit} is
	 * enabled horizontally.. Default value is null
	 */
	public void setAutoFitMaxWidth(Integer autoFitMaxWidth) {
		setAttribute("autoFitMaxWidth", autoFitMaxWidth, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData} is set to <code>"horizontal"</code> or
	 * <code>"both"</code> this property provides an upper limit on how far the ListGrid will expand horizontally to
	 * accommodate its content.  If content exceeds this width, scrollbars will be introduced as usual.   In addition to this
	 * property, {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxColumns autoFitMaxColumns} allows you to limit 
	 * horizontal expansion based on the number of columns to be rendered.
	 *
	 *
	 * @return Integer
	 */
	public Integer getAutoFitMaxWidth() {
		return getAttributeAsInt("autoFitMaxWidth");
	}

	/**
	 * When a user requests column autofitting via the  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderContextMenuItems header contextmenu} or via a  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderAutoFitEvent mouse gesture}, what autofit approach is used.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for the {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitWidthApproach autoFitWidthApproach}.
	 *
	 * @param autoFitWidthApproach new AutoFitWidth approach. Default value is "value"
	 */
	public void setAutoFitWidthApproach(AutoFitWidthApproach autoFitWidthApproach) {
		setAttribute("autoFitWidthApproach", autoFitWidthApproach.getValue(), true);
	}

	/**
	 * When a user requests column autofitting via the  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderContextMenuItems header contextmenu} or via a  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderAutoFitEvent mouse gesture}, what autofit approach is used.
	 *
	 *
	 * @return AutoFitWidthApproach
	 */
	public AutoFitWidthApproach getAutoFitWidthApproach() {
		return EnumUtil.getEnum(AutoFitWidthApproach.values(), getAttribute("autoFitWidthApproach"));
	}

	/**
	 * If this ListGrid is editable, should edits be saved out when the user finishes editing a row (or a cell if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell saveByCell} is true). <P> The default of <code>true</code>
	 * indicates that edits will be {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell automatically saved} as the
	 * user navigates through the grid and/or ${isc.DocUtils.linkForRef('type:EnterKeyEditAction','hits 'Enter'')} to end
	 * editing.  See the {@link com.smartgwt.client.docs.Editing Grid Editing} overview for details.  <P> Setting
	 * <code>autoSaveEdits</code> false creates a "mass update" / "mass delete" interaction where edits will be retained for
	 * all edited cells (across rows if appropriate) until {@link com.smartgwt.client.widgets.grid.ListGrid#saveEdits
	 * ListGrid.saveEdits} is called to save a particular row, or {@link com.smartgwt.client.widgets.grid.ListGrid#saveAllEdits
	 * ListGrid.saveAllEdits} is called to save all changes in a batch.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param autoSaveEdits autoSaveEdits Default value is true
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setAutoSaveEdits(Boolean autoSaveEdits) {
		setAttribute("autoSaveEdits", autoSaveEdits, true);
	}

	/**
	 * If this ListGrid is editable, should edits be saved out when the user finishes editing a row (or a cell if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell saveByCell} is true). <P> The default of <code>true</code>
	 * indicates that edits will be {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell automatically saved} as the
	 * user navigates through the grid and/or ${isc.DocUtils.linkForRef('type:EnterKeyEditAction','hits 'Enter'')} to end
	 * editing.  See the {@link com.smartgwt.client.docs.Editing Grid Editing} overview for details.  <P> Setting
	 * <code>autoSaveEdits</code> false creates a "mass update" / "mass delete" interaction where edits will be retained for
	 * all edited cells (across rows if appropriate) until {@link com.smartgwt.client.widgets.grid.ListGrid#saveEdits
	 * ListGrid.saveEdits} is called to save a particular row, or {@link com.smartgwt.client.widgets.grid.ListGrid#saveAllEdits
	 * ListGrid.saveAllEdits} is called to save all changes in a batch.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getAutoSaveEdits() {
		return getAttributeAsBoolean("autoSaveEdits");
	}

	/**
	 * {@link com.smartgwt.client.grid.GridRenderer#getBaseStyle base cell style} for this listGrid. If this property is unset,
	 * base style may be derived from {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} or
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} as described in {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle ListGrid.getBaseStyle}.
	 *
	 * @param baseStyle baseStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setBaseStyle(String baseStyle) throws IllegalStateException {
		setAttribute("baseStyle", baseStyle, false);
	}

	/**
	 * {@link com.smartgwt.client.grid.GridRenderer#getBaseStyle base cell style} for this listGrid. If this property is unset,
	 * base style may be derived from {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} or
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} as described in {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle ListGrid.getBaseStyle}.
	 *
	 *
	 * @return Return the base stylename for this cell.  Has the following implementation by default: <ul> <li>If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getEditFailedBaseStyle this.editFailedBaseStyle} is defined, and the     cell
	 * is displaying a validation error return this value.</li> <li>If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getEditPendingBaseStyle this.editFailedPendingStyle} is defined, and     the
	 * cell is displaying an edit value that has not yet been saved (see      {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoSaveEdits autoSaveEdits}) return this value.</li> <li>Otherwise return
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordBaseStyleProperty record[listGrid.recordBaseStyleProperty]},  
	 * if defined, otherwise {@link com.smartgwt.client.widgets.grid.ListGridField#getBaseStyle field.baseStyle}.</li> </ul> If
	 * no custom style is found for the cell as described above, the default baseStyle will be returned. If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle baseStyle} is specified this will be used. Otherwise for grids
	 * showing fixed height rows which match {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalCellHeight
	 * normalCellHeight}  {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} will be used.
	 * For grids with variable, or modified cell heights, {@link com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle
	 * tallBaseStyle} will be used.  <P> Note also that enabling {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFastCellUpdates fastCellUpdates} will cause the <code>tallBaseStyle</code>
	 * to be used rather than {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle}.
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getBaseStyle() {
		return getAttributeAsString("baseStyle");
	}

	/**
	 * Background color applied to the ListGrid body (that is, the area of the grid where data values are rendered).<br> Note
	 * that this will typically not be visible to the user unless there are few enough rows that there is visible space in the
	 * body below the last row. To style data cells, override {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} instead.
	 *
	 * @param bodyBackgroundColor bodyBackgroundColor Default value is "white"
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setBodyBackgroundColor(String bodyBackgroundColor) {
		setAttribute("bodyBackgroundColor", bodyBackgroundColor, true);
	}

	/**
	 * Background color applied to the ListGrid body (that is, the area of the grid where data values are rendered).<br> Note
	 * that this will typically not be visible to the user unless there are few enough rows that there is visible space in the
	 * body below the last row. To style data cells, override {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} instead.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getBodyBackgroundColor() {
		return getAttributeAsString("bodyBackgroundColor");
	}

	/**
	 * Overflow setting for the "body", that is, the area of the grid where data values are rendered. <P> By setting both the
	 * grid itself and the body to overflow:visible, it is possible to "auto-fit" to the rendered height or width of the rows. 
	 * Note that in this case <code>grid.width</code> and <code>grid.height</code> act as minimums.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Update the {@link com.smartgwt.client.widgets.grid.ListGrid#getBodyOverflow bodyOverflow} for this listGrid.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param bodyOverflow new overflow setting for the body. Default value is Canvas.AUTO
	 * @see com.smartgwt.client.docs.Sizing Sizing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public void setBodyOverflow(Overflow bodyOverflow) {
		setAttribute("bodyOverflow", bodyOverflow.getValue(), true);
	}

	/**
	 * Overflow setting for the "body", that is, the area of the grid where data values are rendered. <P> By setting both the
	 * grid itself and the body to overflow:visible, it is possible to "auto-fit" to the rendered height or width of the rows. 
	 * Note that in this case <code>grid.width</code> and <code>grid.height</code> act as minimums.
	 *
	 *
	 * @return Overflow
	 * @see com.smartgwt.client.docs.Sizing Sizing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public Overflow getBodyOverflow() {
		return EnumUtil.getEnum(Overflow.values(), getAttribute("bodyOverflow"));
	}

	/**
	 * CSS style used for the body of this grid.  If applying a background-color to the body via a CSS style applied using this
	 * property, be sure to set  {@link com.smartgwt.client.widgets.grid.ListGrid#getBodyBackgroundColor bodyBackgroundColor}
	 * to <code>null</code>.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Update the {@link com.smartgwt.client.widgets.grid.ListGrid#getBodyStyleName bodyStyleName} for this listGrid.
	 *
	 * @param bodyStyleName new body style name. Default value is null
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setBodyStyleName(String bodyStyleName) {
		setAttribute("bodyStyleName", bodyStyleName, true);
	}

	/**
	 * CSS style used for the body of this grid.  If applying a background-color to the body via a CSS style applied using this
	 * property, be sure to set  {@link com.smartgwt.client.widgets.grid.ListGrid#getBodyBackgroundColor bodyBackgroundColor}
	 * to <code>null</code>.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getBodyStyleName() {
		return getAttributeAsString("bodyStyleName");
	}

	/**
	 * Image to display for a false value in a boolean field. Default <code>null</code> value means no image will be displayed
	 * <P> To turn this off explicitly set {@link com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon
	 * suppressValueIcon} to true <P> If this, {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage
	 * booleanTrueImage} and {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} are
	 * undefined, this will be set to {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getUncheckedImage
	 * uncheckedImage}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param booleanFalseImage booleanFalseImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanPartialImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setBooleanFalseImage(String booleanFalseImage) {
		setAttribute("booleanFalseImage", booleanFalseImage, true);
	}

	/**
	 * Image to display for a false value in a boolean field. Default <code>null</code> value means no image will be displayed
	 * <P> To turn this off explicitly set {@link com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon
	 * suppressValueIcon} to true <P> If this, {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage
	 * booleanTrueImage} and {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} are
	 * undefined, this will be set to {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getUncheckedImage
	 * uncheckedImage}.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public String getBooleanFalseImage() {
		return getAttributeAsString("booleanFalseImage");
	}

	/**
	 * Height for the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage}. Note: If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} is unset, the {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage} will be used to indicate a true value
	 * in a boolean field. In this case this property is ignored in favor of {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getValueIconHeight valueIconHeight}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param booleanImageHeight booleanImageHeight Default value is 16
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setBooleanImageHeight(int booleanImageHeight) {
		setAttribute("booleanImageHeight", booleanImageHeight, true);
	}

	/**
	 * Height for the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage}. Note: If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} is unset, the {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage} will be used to indicate a true value
	 * in a boolean field. In this case this property is ignored in favor of {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getValueIconHeight valueIconHeight}.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getBooleanImageHeight() {
		return getAttributeAsInt("booleanImageHeight");
	}

	/**
	 * Width for the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage}. Note: If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} is unset, the {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage} will be used to indicate a true value
	 * in a boolean field. In this case this property is ignored in favor of {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getValueIconWidth valueIconWidth}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param booleanImageWidth booleanImageWidth Default value is 16
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setBooleanImageWidth(int booleanImageWidth) {
		setAttribute("booleanImageWidth", booleanImageWidth, true);
	}

	/**
	 * Width for the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage}. Note: If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} is unset, the {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage} will be used to indicate a true value
	 * in a boolean field. In this case this property is ignored in favor of {@link
	 * com.smartgwt.client.widgets.form.fields.CheckboxItem#getValueIconWidth valueIconWidth}.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getBooleanImageWidth() {
		return getAttributeAsInt("booleanImageWidth");
	}

	/**
	 * Image to display for a partially true value in a boolean field (typically selection). <P> To turn this off explicitly
	 * set {@link com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon suppressValueIcon} to true. <P> If this,
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} are undefined, this will be set to
	 * {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getPartialSelectedImage partialSelectedImage}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param booleanPartialImage booleanPartialImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanFalseImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setBooleanPartialImage(String booleanPartialImage) {
		setAttribute("booleanPartialImage", booleanPartialImage, true);
	}

	/**
	 * Image to display for a partially true value in a boolean field (typically selection). <P> To turn this off explicitly
	 * set {@link com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon suppressValueIcon} to true. <P> If this,
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} are undefined, this will be set to
	 * {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getPartialSelectedImage partialSelectedImage}.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public String getBooleanPartialImage() {
		return getAttributeAsString("booleanPartialImage");
	}

	/**
	 * Image to display for a true value in a boolean field. <P> To turn this off explicitly set {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon suppressValueIcon} to true. <P> If this, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} are undefined, this will be set to
	 * {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param booleanTrueImage booleanTrueImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBooleanPartialImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setBooleanTrueImage(String booleanTrueImage) {
		setAttribute("booleanTrueImage", booleanTrueImage, true);
	}

	/**
	 * Image to display for a true value in a boolean field. <P> To turn this off explicitly set {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getSuppressValueIcon suppressValueIcon} to true. <P> If this, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} are undefined, this will be set to
	 * {@link com.smartgwt.client.widgets.form.fields.CheckboxItem#getCheckedImage checkedImage}.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public String getBooleanTrueImage() {
		return getAttributeAsString("booleanTrueImage");
	}

	/**
	 * Indicates whether records can be dropped into this listGrid.
	 *
	 * @param canAcceptDroppedRecords canAcceptDroppedRecords Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 */
	public void setCanAcceptDroppedRecords(Boolean canAcceptDroppedRecords) {
		setAttribute("canAcceptDroppedRecords", canAcceptDroppedRecords, true);
	}

	/**
	 * Indicates whether records can be dropped into this listGrid.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 */
	public Boolean getCanAcceptDroppedRecords() {
		return getAttributeAsBoolean("canAcceptDroppedRecords");
	}

	/**
	 * Whether the user able to autofit specific columns to their data and/or title via a context menu item or {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderAutoFitEvent headerAutoFitEvent}. <P> Autofitting can also be
	 * programmatically enabled by setting  autoFitWidth.
	 *
	 * @param canAutoFitFields canAutoFitFields Default value is true
	 */
	public void setCanAutoFitFields(Boolean canAutoFitFields) {
		setAttribute("canAutoFitFields", canAutoFitFields, true);
	}

	/**
	 * Whether the user able to autofit specific columns to their data and/or title via a context menu item or {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderAutoFitEvent headerAutoFitEvent}. <P> Autofitting can also be
	 * programmatically enabled by setting  autoFitWidth.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanAutoFitFields() {
		return getAttributeAsBoolean("canAutoFitFields");
	}

	/**
	 * If this is an editable listGrid, and <code>this.confirmCancelEditing</code> is true this property is used as the message
	 * to display in the confirmation dismissal prompt.
	 *
	 * @param cancelEditingConfirmationMessage cancelEditingConfirmationMessage Default value is Cancelling this edit will discard unsaved changes for this record. Continue?
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setCancelEditingConfirmationMessage(String cancelEditingConfirmationMessage) {
		setAttribute("cancelEditingConfirmationMessage", cancelEditingConfirmationMessage, true);
	}

	/**
	 * If this is an editable listGrid, and <code>this.confirmCancelEditing</code> is true this property is used as the message
	 * to display in the confirmation dismissal prompt.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public String getCancelEditingConfirmationMessage() {
		return getAttributeAsString("cancelEditingConfirmationMessage");
	}

	/**
	 * Can a group be collapsed/expanded? When true a collapse/expand icon is shown ({@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGroupIcon groupIcon}) and clicking the icon or double-clicking the group
	 * title will collapse or expand the group. When false the group icon is not shown and double-clicking on the title does
	 * not change group state. Additionally  groupStartOpen is  initialized to "all".
	 *
	 * @param canCollapseGroup canCollapseGroup Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setCanCollapseGroup(Boolean canCollapseGroup) throws IllegalStateException {
		setAttribute("canCollapseGroup", canCollapseGroup, false);
	}

	/**
	 * Can a group be collapsed/expanded? When true a collapse/expand icon is shown ({@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGroupIcon groupIcon}) and clicking the icon or double-clicking the group
	 * title will collapse or expand the group. When false the group icon is not shown and double-clicking on the title does
	 * not change group state. Additionally  groupStartOpen is  initialized to "all".
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public Boolean getCanCollapseGroup() {
		return getAttributeAsBoolean("canCollapseGroup");
	}

	/**
	 * Indicates whether records can be dragged from this listGrid and dropped elsewhere.
	 *
	 * @param canDragRecordsOut canDragRecordsOut Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 */
	public void setCanDragRecordsOut(Boolean canDragRecordsOut) {
		setAttribute("canDragRecordsOut", canDragRecordsOut, true);
	}

	/**
	 * Indicates whether records can be dragged from this listGrid and dropped elsewhere.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 */
	public Boolean getCanDragRecordsOut() {
		return getAttributeAsBoolean("canDragRecordsOut");
	}

	/**
	 * If this property is true, users can drag the mouse to select several rows or cells.  This is mutually exclusive with
	 * rearranging rows or cells by dragging.
	 *
	 * @param canDragSelect canDragSelect Default value is false
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_select_list" target="examples">Drag list (select) Example</a>
	 */
	public void setCanDragSelect(Boolean canDragSelect) {
		setAttribute("canDragSelect", canDragSelect, true);
	}

	/**
	 * If this property is true, users can drag the mouse to select several rows or cells.  This is mutually exclusive with
	 * rearranging rows or cells by dragging.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_select_list" target="examples">Drag list (select) Example</a>
	 */
	public Boolean getCanDragSelect() {
		return getAttributeAsBoolean("canDragSelect");
	}

	/**
	 * If this property is true, users can drag the mouse to select text within grid rows. This is mutually exclusive with 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCanDragReorder rearranging rows or cells by dragging}, and with 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCanDragSelect drag selection of rows}.
	 *
	 * @param canDragSelectText canDragSelectText Default value is false
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public void setCanDragSelectText(Boolean canDragSelectText) {
		setAttribute("canDragSelectText", canDragSelectText, true);
	}

	/**
	 * If this property is true, users can drag the mouse to select text within grid rows. This is mutually exclusive with 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCanDragReorder rearranging rows or cells by dragging}, and with 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCanDragSelect drag selection of rows}.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public Boolean getCanDragSelectText() {
		return getAttributeAsBoolean("canDragSelectText");
	}

	/**
	 * Can the user edit cells in this listGrid? Can be set for the listGrid, and overridden for       individual fields.<br>  
	 * If 'canEdit' is false at the listGrid level, fields can never be edited - in this case      the canEdit property on
	 * individual fields will be ignored.<br>      If 'canEdit' is set to true at the listGrid level, setting the 'canEdit'
	 * property to      false at the field level will prevent the field from being edited inline.<br>      If 'canEdit' is not
	 * set at the listGrid level, setting 'canEdit' to true at the field       level enables the field to be edited inline.
	 *
	 * @param canEdit canEdit Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#startEditing
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setRecordEditProperty
	 * @see com.smartgwt.client.widgets.grid.ListGrid#canEditCell
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setFields
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_row" target="examples">Edit by row Example</a>
	 */
	public void setCanEdit(Boolean canEdit) {
		setAttribute("canEdit", canEdit, true);
	}

	/**
	 * Can the user edit cells in this listGrid? Can be set for the listGrid, and overridden for       individual fields.<br>  
	 * If 'canEdit' is false at the listGrid level, fields can never be edited - in this case      the canEdit property on
	 * individual fields will be ignored.<br>      If 'canEdit' is set to true at the listGrid level, setting the 'canEdit'
	 * property to      false at the field level will prevent the field from being edited inline.<br>      If 'canEdit' is not
	 * set at the listGrid level, setting 'canEdit' to true at the field       level enables the field to be edited inline.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#startEditing
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getRecordEditProperty
	 * @see com.smartgwt.client.widgets.grid.ListGrid#canEditCell
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getFields
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_row" target="examples">Edit by row Example</a>
	 */
	public Boolean getCanEdit() {
		return getAttributeAsBoolean("canEdit");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true, this property
	 * indicates whether multiple records can be expanded simultaneously.  If set to false, expanding a record will
	 * automatically collapse any record which is already expanded.  The default value is  <code>true</code>.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param canExpandMultipleRecords canExpandMultipleRecords Default value is true
	 */
	public void setCanExpandMultipleRecords(Boolean canExpandMultipleRecords) {
		setAttribute("canExpandMultipleRecords", canExpandMultipleRecords, true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true, this property
	 * indicates whether multiple records can be expanded simultaneously.  If set to false, expanding a record will
	 * automatically collapse any record which is already expanded.  The default value is  <code>true</code>.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanExpandMultipleRecords() {
		return getAttributeAsBoolean("canExpandMultipleRecords");
	}

	/**
	 * Property name on a record that will be checked to determine whether a record can be expanded.
	 *
	 * @param canExpandRecordProperty canExpandRecordProperty Default value is "canExpand"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanExpand
	 */
	public void setCanExpandRecordProperty(String canExpandRecordProperty) throws IllegalStateException {
		setAttribute("canExpandRecordProperty", canExpandRecordProperty, false);
	}

	/**
	 * Property name on a record that will be checked to determine whether a record can be expanded.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanExpand
	 */
	public String getCanExpandRecordProperty() {
		return getAttributeAsString("canExpandRecordProperty");
	}

	/**
	 * When set to true, shows an additional field at the beginning of the field-list  (respecting RTL) to allow users to
	 * expand and collapse individual records. See {@link com.smartgwt.client.widgets.grid.ListGrid#expandRecord
	 * ListGrid.expandRecord} and {@link com.smartgwt.client.widgets.grid.ListGrid#getExpansionMode expansionMode} for details
	 * on record expansion. <P> If expanded records will be variable height, you should switch on {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getVirtualScrolling virtualScrolling}. <P> Note that expanded records are not
	 * currently supported in conjunction  with {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen fields}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param canExpandRecords canExpandRecords Default value is false
	 */
	public void setCanExpandRecords(Boolean canExpandRecords) {
		setAttribute("canExpandRecords", canExpandRecords, true);
	}

	/**
	 * When set to true, shows an additional field at the beginning of the field-list  (respecting RTL) to allow users to
	 * expand and collapse individual records. See {@link com.smartgwt.client.widgets.grid.ListGrid#expandRecord
	 * ListGrid.expandRecord} and {@link com.smartgwt.client.widgets.grid.ListGrid#getExpansionMode expansionMode} for details
	 * on record expansion. <P> If expanded records will be variable height, you should switch on {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getVirtualScrolling virtualScrolling}. <P> Note that expanded records are not
	 * currently supported in conjunction  with {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen fields}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanExpandRecords() {
		return getAttributeAsBoolean("canExpandRecords");
	}

	/**
	 * Whether an interface should be shown to allow user is allowed to dynamically "freeze" or "unfreeze" columns with respect
	 * to horizontally scrolling. If unset, this property defaults to <code>true</code> unless:<ul> <li>{@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights this.fixedRecordHeights} is <code>false</code></li>
	 * <li>{@link com.smartgwt.client.widgets.grid.ListGrid#getBodyOverflow this.bodyOverflow} is <code>"visible"</code></li>
	 * <li>{@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData this.autoFitData} is set to
	 * <code>"horizontal"</code> or  <code>"both"</code></li> <li>Any field has overflow set to
	 * <code>"visible"</code></li></ul> <P> Note that the <code>canFreezeFields</code> setting enables or disables the user
	 * interface for freezing and unfreezing fields only.  Fields can be programmatically frozen via setting {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getFrozen field.frozen} to true when the grid is created, or dynamically
	 * frozen and unfrozen via {@link com.smartgwt.client.widgets.grid.ListGrid#freezeField ListGrid.freezeField} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#unfreezeField ListGrid.unfreezeField}.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter method for {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields canFreezeFields}
	 *
	 * @param canFreezeFields New value for <code>listGrid.canFreezeFields</code>. Default value is null
	 * @see com.smartgwt.client.docs.FrozenFields FrozenFields overview and related methods
	 */
	public void setCanFreezeFields(Boolean canFreezeFields) {
		setAttribute("canFreezeFields", canFreezeFields, true);
	}

	/**
	 * Whether an interface should be shown to allow user is allowed to dynamically "freeze" or "unfreeze" columns with respect
	 * to horizontally scrolling. If unset, this property defaults to <code>true</code> unless:<ul> <li>{@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights this.fixedRecordHeights} is <code>false</code></li>
	 * <li>{@link com.smartgwt.client.widgets.grid.ListGrid#getBodyOverflow this.bodyOverflow} is <code>"visible"</code></li>
	 * <li>{@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData this.autoFitData} is set to
	 * <code>"horizontal"</code> or  <code>"both"</code></li> <li>Any field has overflow set to
	 * <code>"visible"</code></li></ul> <P> Note that the <code>canFreezeFields</code> setting enables or disables the user
	 * interface for freezing and unfreezing fields only.  Fields can be programmatically frozen via setting {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getFrozen field.frozen} to true when the grid is created, or dynamically
	 * frozen and unfrozen via {@link com.smartgwt.client.widgets.grid.ListGrid#freezeField ListGrid.freezeField} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#unfreezeField ListGrid.unfreezeField}.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.FrozenFields FrozenFields overview and related methods
	 */
	public Boolean getCanFreezeFields() {
		return getAttributeAsBoolean("canFreezeFields");
	}

	/**
	 * If false, grouping via context menu will be disabled.
	 *
	 * @param canGroupBy canGroupBy Default value is true
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setCanGroupBy(Boolean canGroupBy) {
		setAttribute("canGroupBy", canGroupBy, true);
	}

	/**
	 * If false, grouping via context menu will be disabled.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public Boolean getCanGroupBy() {
		return getAttributeAsBoolean("canGroupBy");
	}

	/**
	 * If true, cellHover and rowHover events will fire when the user leaves the mouse over a  row / cell.
	 *
	 * @param canHover canHover Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowHover
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setShowHover
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_value_hover" target="examples">Value hover tips Example</a>
	 */
	@Override
	public void setCanHover(Boolean canHover) {
		setAttribute("canHover", canHover, true);
	}

	/**
	 * If true, cellHover and rowHover events will fire when the user leaves the mouse over a  row / cell.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowHover
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getShowHover
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_value_hover" target="examples">Value hover tips Example</a>
	 */
	@Override
	public Boolean getCanHover() {
		return getAttributeAsBoolean("canHover");
	}

	/**
	 * When true, indicates that this ListGrid supports multi-level sorting.
	 *
	 * @param canMultiSort canMultiSort Default value is true
	 */
	public void setCanMultiSort(Boolean canMultiSort) {
		setAttribute("canMultiSort", canMultiSort, true);
	}

	/**
	 * When true, indicates that this ListGrid supports multi-level sorting.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanMultiSort() {
		return getAttributeAsBoolean("canMultiSort");
	}

	/**
	 * Indicates whether the field picker item and submenu should be present in the header context menu. This menu allows the
	 * user to hide visible fields and show hidden fields. By default only fields explicitly included in the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFields fields} array will be available in this menu, unless {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanPickOmittedFields canPickOmittedFields} is set to true for a databound
	 * grid.
	 *
	 * @param canPickFields canPickFields Default value is true
	 */
	public void setCanPickFields(Boolean canPickFields) {
		setAttribute("canPickFields", canPickFields, true);
	}

	/**
	 * Indicates whether the field picker item and submenu should be present in the header context menu. This menu allows the
	 * user to hide visible fields and show hidden fields. By default only fields explicitly included in the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFields fields} array will be available in this menu, unless {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanPickOmittedFields canPickOmittedFields} is set to true for a databound
	 * grid.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanPickFields() {
		return getAttributeAsBoolean("canPickFields");
	}

	/**
	 * If this grid has a specified {@link com.smartgwt.client.widgets.grid.ListGrid#getDataSource dataSource}, and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getUseAllDataSourceFields useAllDataSourceFields} is false, setting this
	 * property to true will cause all dataSource fields not included in the specified set of fields to show up in the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields field picker menu item}. <P> Has no effect if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFields fields} is unset (as in this case all dataSource fields will be
	 * displayed by default), or if {@link com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields canPickFields} is false.
	 *
	 * @param canPickOmittedFields canPickOmittedFields Default value is false
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setCanPickOmittedFields(Boolean canPickOmittedFields) throws IllegalStateException {
		setAttribute("canPickOmittedFields", canPickOmittedFields, false);
	}

	/**
	 * If this grid has a specified {@link com.smartgwt.client.widgets.grid.ListGrid#getDataSource dataSource}, and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getUseAllDataSourceFields useAllDataSourceFields} is false, setting this
	 * property to true will cause all dataSource fields not included in the specified set of fields to show up in the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields field picker menu item}. <P> Has no effect if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFields fields} is unset (as in this case all dataSource fields will be
	 * displayed by default), or if {@link com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields canPickFields} is false.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanPickOmittedFields() {
		return getAttributeAsBoolean("canPickOmittedFields");
	}

	/**
	 * If set, provide UI for the user to remove records from the grid. This is achieved by rendering an additional field in
	 * the listGrid which, when clicked, will remove the record associated with the clicked row via a call to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#removeData ListGrid.removeData}. <P> If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord animateRemoveRecord} is true, the removed record will
	 * appear to shrink out of view when it is removed. <P> By default the field will display the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRemoveIcon removeIcon} next to each record, and will be rendered as the
	 * leftmost column. Two mechanisms exist to further modify this field: <ul> <li>To change the position of the remove-field,
	 * include an explicitly specified field with     the attribute {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getIsRemoveField isRemoveField:true} set. This will then     be used as
	 * the remove field instead of adding a field to the beginning of the set of     columns.</li> <li>Additional direct
	 * configuration of the remove field may be achieved by modifying     {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldProperties removeFieldProperties}.</li> </ul>
	 *
	 * @param canRemoveRecords canRemoveRecords Default value is false
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public void setCanRemoveRecords(Boolean canRemoveRecords) throws IllegalStateException {
		setAttribute("canRemoveRecords", canRemoveRecords, false);
	}

	/**
	 * If set, provide UI for the user to remove records from the grid. This is achieved by rendering an additional field in
	 * the listGrid which, when clicked, will remove the record associated with the clicked row via a call to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#removeData ListGrid.removeData}. <P> If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAnimateRemoveRecord animateRemoveRecord} is true, the removed record will
	 * appear to shrink out of view when it is removed. <P> By default the field will display the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRemoveIcon removeIcon} next to each record, and will be rendered as the
	 * leftmost column. Two mechanisms exist to further modify this field: <ul> <li>To change the position of the remove-field,
	 * include an explicitly specified field with     the attribute {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getIsRemoveField isRemoveField:true} set. This will then     be used as
	 * the remove field instead of adding a field to the beginning of the set of     columns.</li> <li>Additional direct
	 * configuration of the remove field may be achieved by modifying     {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldProperties removeFieldProperties}.</li> </ul>
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public Boolean getCanRemoveRecords() {
		return getAttributeAsBoolean("canRemoveRecords");
	}

	/**
	 * Indicates whether fields in this listGrid can be reordered by dragging and dropping header fields.
	 *
	 * @param canReorderFields canReorderFields Default value is true
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_columnorder" target="examples">Column order Example</a>
	 */
	public void setCanReorderFields(Boolean canReorderFields) {
		setAttribute("canReorderFields", canReorderFields, true);
	}

	/**
	 * Indicates whether fields in this listGrid can be reordered by dragging and dropping header fields.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_columnorder" target="examples">Column order Example</a>
	 */
	public Boolean getCanReorderFields() {
		return getAttributeAsBoolean("canReorderFields");
	}

	/**
	 * Indicates whether records can be reordered by dragging within this listGrid.
	 *
	 * @param canReorderRecords canReorderRecords Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#setCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_drag_reorder" target="examples">Drag reorder Example</a>
	 */
	public void setCanReorderRecords(Boolean canReorderRecords) {
		setAttribute("canReorderRecords", canReorderRecords, true);
	}

	/**
	 * Indicates whether records can be reordered by dragging within this listGrid.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanDrag
	 * @see com.smartgwt.client.widgets.grid.ListGridRecord#getCanAcceptDrop
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#effects_dd_move_list" target="examples">Drag list (move) Example</a>
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_drag_reorder" target="examples">Drag reorder Example</a>
	 */
	public Boolean getCanReorderRecords() {
		return getAttributeAsBoolean("canReorderRecords");
	}

	/**
	 * Indicates whether fields in this listGrid can be resized by dragging header fields.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter method for updating {@link com.smartgwt.client.widgets.grid.ListGrid#getCanResizeFields canResizeFields} at runtime.
	 *
	 * @param canResizeFields new value for this.canResizeFields. Default value is true
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_columnsize" target="examples">Column size Example</a>
	 */
	public void setCanResizeFields(Boolean canResizeFields) {
		setAttribute("canResizeFields", canResizeFields, true);
	}

	/**
	 * Indicates whether fields in this listGrid can be resized by dragging header fields.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_columnsize" target="examples">Column size Example</a>
	 */
	public Boolean getCanResizeFields() {
		return getAttributeAsBoolean("canResizeFields");
	}

	/**
	 * Controls whether a checkbox for selecting all records appears in the header with  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} set to "checkbox"
	 *
	 * @param canSelectAll canSelectAll Default value is null
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public void setCanSelectAll(Boolean canSelectAll) {
		setAttribute("canSelectAll", canSelectAll, true);
	}

	/**
	 * Controls whether a checkbox for selecting all records appears in the header with  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} set to "checkbox"
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public Boolean getCanSelectAll() {
		return getAttributeAsBoolean("canSelectAll");
	}

	/**
	 * Enables or disables interactive sorting behavior for this listGrid. Does not affect sorting by direct calls to the sort
	 * method.
	 *
	 * @param canSort canSort Default value is true
	 */
	public void setCanSort(Boolean canSort) {
		setAttribute("canSort", canSort, true);
	}

	/**
	 * Enables or disables interactive sorting behavior for this listGrid. Does not affect sorting by direct calls to the sort
	 * method.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getCanSort() {
		return getAttributeAsBoolean("canSort");
	}

	/**
	 * Should the header be included in the tab-order for the page? If not explicitly specified, the header will be included in
	 * the tab order for the page if  setScreenReaderMode is called.
	 *
	 * @param canTabToHeader canTabToHeader Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Accessibility Accessibility overview and related methods
	 */
	public void setCanTabToHeader(Boolean canTabToHeader) throws IllegalStateException {
		setAttribute("canTabToHeader", canTabToHeader, false);
	}

	/**
	 * Should the header be included in the tab-order for the page? If not explicitly specified, the header will be included in
	 * the tab order for the page if  setScreenReaderMode is called.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Accessibility Accessibility overview and related methods
	 */
	public Boolean getCanTabToHeader() {
		return getAttributeAsBoolean("canTabToHeader");
	}

	/**
	 * The default height of each row in pixels.
	 *
	 * @param cellHeight cellHeight Default value is 20
	 * @see com.smartgwt.client.grid.GridRenderer#getRowHeight
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_multiline" target="examples">Multiline values Example</a>
	 */
	public void setCellHeight(int cellHeight) {
		setAttribute("cellHeight", cellHeight, true);
	}

	/**
	 * The default height of each row in pixels.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.grid.GridRenderer#getRowHeight
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_multiline" target="examples">Multiline values Example</a>
	 */
	public int getCellHeight() {
		return getAttributeAsInt("cellHeight");
	}

	/**
	 * The amount of empty space, in pixels, surrounding each value in its cell.
	 *
	 * @param cellPadding cellPadding Default value is 2
	 */
	public void setCellPadding(int cellPadding) {
		setAttribute("cellPadding", cellPadding, true);
	}

	/**
	 * The amount of empty space, in pixels, surrounding each value in its cell.
	 *
	 *
	 * @return int
	 */
	public int getCellPadding() {
		return getAttributeAsInt("cellPadding");
	}

	/**
	 * Name of the Smart GWT Class to be used when creating charts.  Must support the Chart interface.
	 *
	 * @param chartConstructor chartConstructor Default value is "FacetChart"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setChartConstructor(String chartConstructor) throws IllegalStateException {
		setAttribute("chartConstructor", chartConstructor, false);
	}

	/**
	 * Name of the Smart GWT Class to be used when creating charts.  Must support the Chart interface.
	 *
	 *
	 * @return String
	 */
	public String getChartConstructor() {
		return getAttributeAsString("chartConstructor");
	}

	/**
	 * Default type of chart to plot.
	 *
	 * @param chartType chartType Default value is "Column"
	 */
	public void setChartType(ChartType chartType) {
		setAttribute("chartType", chartType.getValue(), true);
	}

	/**
	 * Default type of chart to plot.
	 *
	 *
	 * @return ChartType
	 */
	public ChartType getChartType() {
		return EnumUtil.getEnum(ChartType.values(), getAttribute("chartType"));
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for an unselected row. If
	 * unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} will be used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param checkboxFieldFalseImage checkboxFieldFalseImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageHeight
	 */
	public void setCheckboxFieldFalseImage(String checkboxFieldFalseImage) {
		setAttribute("checkboxFieldFalseImage", checkboxFieldFalseImage, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for an unselected row. If
	 * unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} will be used.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageHeight
	 */
	public String getCheckboxFieldFalseImage() {
		return getAttributeAsString("checkboxFieldFalseImage");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property may be set to govern the height of the checkbox image displayed to indicate
	 * whether a row is selected. If unset, the checkboxField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageHeight booleanImageHeight} for this grid.
	 *
	 * @param checkboxFieldImageHeight checkboxFieldImageHeight Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setCheckboxFieldImageHeight(Integer checkboxFieldImageHeight) throws IllegalStateException {
		setAttribute("checkboxFieldImageHeight", checkboxFieldImageHeight, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property may be set to govern the height of the checkbox image displayed to indicate
	 * whether a row is selected. If unset, the checkboxField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageHeight booleanImageHeight} for this grid.
	 *
	 *
	 * @return Integer
	 */
	public Integer getCheckboxFieldImageHeight() {
		return getAttributeAsInt("checkboxFieldImageHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property may be set to govern the width of the checkbox image displayed to indicate whether
	 * a row is selected. If unset, the checkboxField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageWidth booleanImageWidth} for this grid.
	 *
	 * @param checkboxFieldImageWidth checkboxFieldImageWidth Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setCheckboxFieldImageWidth(Integer checkboxFieldImageWidth) throws IllegalStateException {
		setAttribute("checkboxFieldImageWidth", checkboxFieldImageWidth, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property may be set to govern the width of the checkbox image displayed to indicate whether
	 * a row is selected. If unset, the checkboxField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageWidth booleanImageWidth} for this grid.
	 *
	 *
	 * @return Integer
	 */
	public Integer getCheckboxFieldImageWidth() {
		return getAttributeAsInt("checkboxFieldImageWidth");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for a partially selected
	 * row. If unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} will be
	 * used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param checkboxFieldPartialImage checkboxFieldPartialImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageHeight
	 */
	public void setCheckboxFieldPartialImage(String checkboxFieldPartialImage) {
		setAttribute("checkboxFieldPartialImage", checkboxFieldPartialImage, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for a partially selected
	 * row. If unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanPartialImage booleanPartialImage} will be
	 * used.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageHeight
	 */
	public String getCheckboxFieldPartialImage() {
		return getAttributeAsString("checkboxFieldPartialImage");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for a selected row. If
	 * unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} will be used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param checkboxFieldTrueImage checkboxFieldTrueImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCheckboxFieldImageHeight
	 */
	public void setCheckboxFieldTrueImage(String checkboxFieldTrueImage) {
		setAttribute("checkboxFieldTrueImage", checkboxFieldTrueImage, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance} is set to
	 * <code>"checkbox"</code> this property determines the image to display in the checkbox field for a selected row. If
	 * unset, the {@link com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} will be used.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCheckboxFieldImageHeight
	 */
	public String getCheckboxFieldTrueImage() {
		return getAttributeAsString("checkboxFieldTrueImage");
	}

	/**
	 * For {@link com.smartgwt.client.types.ExpansionMode expansionModes} that show another grid or tree, what the  child's
	 * expansionMode should be. <P>Default value <code>null</code> means no further expansion.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param childExpansionMode childExpansionMode Default value is null
	 */
	public void setChildExpansionMode(ExpansionMode childExpansionMode) {
		setAttribute("childExpansionMode", childExpansionMode.getValue(), true);
	}

	/**
	 * For {@link com.smartgwt.client.types.ExpansionMode expansionModes} that show another grid or tree, what the  child's
	 * expansionMode should be. <P>Default value <code>null</code> means no further expansion.
	 *
	 *
	 * @return ExpansionMode
	 */
	public ExpansionMode getChildExpansionMode() {
		return EnumUtil.getEnum(ExpansionMode.values(), getAttribute("childExpansionMode"));
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to clear any existing sort on all fields.  This menu-item is
	 * displayed only in the context menu for the sorter button.
	 *
	 * @param clearAllSortingText clearAllSortingText Default value is "Clear All Sorting"
	 */
	public void setClearAllSortingText(String clearAllSortingText) {
		setAttribute("clearAllSortingText", clearAllSortingText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to clear any existing sort on all fields.  This menu-item is
	 * displayed only in the context menu for the sorter button.
	 *
	 *
	 * @return String
	 */
	public String getClearAllSortingText() {
		return getAttributeAsString("clearAllSortingText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor filter-editor} is visible, this
	 * attribute will be shown as the menu item title to clear any existing filter.  This menu-item is displayed only in the
	 * context menu for the sorter button.
	 *
	 * @param clearFilterText clearFilterText Default value is "Clear Filter"
	 */
	public void setClearFilterText(String clearFilterText) {
		setAttribute("clearFilterText", clearFilterText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor filter-editor} is visible, this
	 * attribute will be shown as the menu item title to clear any existing filter.  This menu-item is displayed only in the
	 * context menu for the sorter button.
	 *
	 *
	 * @return String
	 */
	public String getClearFilterText() {
		return getAttributeAsString("clearFilterText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to clear an existing sort on this field.
	 *
	 * @param clearSortFieldText clearSortFieldText Default value is "Clear Sort"
	 */
	public void setClearSortFieldText(String clearSortFieldText) {
		setAttribute("clearSortFieldText", clearSortFieldText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to clear an existing sort on this field.
	 *
	 *
	 * @return String
	 */
	public String getClearSortFieldText() {
		return getAttributeAsString("clearSortFieldText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and multi-sorting is enabled, this attribute will be shown as the menu item title to show a  MultiSortDialog to
	 * configure the sort-specification for this grid.  This menu-item is displayed only in the context menu for the sorter
	 * button.
	 *
	 * @param configureSortText configureSortText Default value is "Configure Sort"
	 */
	public void setConfigureSortText(String configureSortText) {
		setAttribute("configureSortText", configureSortText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid,  and multi-sorting is enabled, this attribute will be shown as the menu item title to show a  MultiSortDialog to
	 * configure the sort-specification for this grid.  This menu-item is displayed only in the context menu for the sorter
	 * button.
	 *
	 *
	 * @return String
	 */
	public String getConfigureSortText() {
		return getAttributeAsString("configureSortText");
	}

	/**
	 * If this is an editable listGrid, when the user attempts to cancel an edit, should we display a confirmation prompt
	 * before discarding the edited values for the record?
	 *
	 * @param confirmCancelEditing confirmCancelEditing Default value is false
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setConfirmCancelEditing(Boolean confirmCancelEditing) {
		setAttribute("confirmCancelEditing", confirmCancelEditing, true);
	}

	/**
	 * If this is an editable listGrid, when the user attempts to cancel an edit, should we display a confirmation prompt
	 * before discarding the edited values for the record?
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getConfirmCancelEditing() {
		return getAttributeAsBoolean("confirmCancelEditing");
	}

	/**
	 * For editable listGrids, outstanding unsaved edits when the user performs a new filter or sort will be discarded. This
	 * flag determines whether we should display a confirmation dialog with options to save or discard the edits, or cancel the
	 * action in this case.
	 *
	 * @param confirmDiscardEdits confirmDiscardEdits Default value is true
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setConfirmDiscardEdits(Boolean confirmDiscardEdits) {
		setAttribute("confirmDiscardEdits", confirmDiscardEdits, true);
	}

	/**
	 * For editable listGrids, outstanding unsaved edits when the user performs a new filter or sort will be discarded. This
	 * flag determines whether we should display a confirmation dialog with options to save or discard the edits, or cancel the
	 * action in this case.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getConfirmDiscardEdits() {
		return getAttributeAsBoolean("confirmDiscardEdits");
	}

	/**
	 * If <code>this.confirmDiscardEdits</code> is true, this property can be used to customize the error message string
	 * displayed to the user in a dialog with options to  cancel the action, or save or discard pending edits in response to
	 * sort/filter actions that would otherwise drop unsaved edit values.
	 *
	 * @param confirmDiscardEditsMessage confirmDiscardEditsMessage Default value is "This action will discard unsaved changes for this list."
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setConfirmDiscardEditsMessage(String confirmDiscardEditsMessage) {
		setAttribute("confirmDiscardEditsMessage", confirmDiscardEditsMessage, true);
	}

	/**
	 * If <code>this.confirmDiscardEdits</code> is true, this property can be used to customize the error message string
	 * displayed to the user in a dialog with options to  cancel the action, or save or discard pending edits in response to
	 * sort/filter actions that would otherwise drop unsaved edit values.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public String getConfirmDiscardEditsMessage() {
		return getAttributeAsString("confirmDiscardEditsMessage");
	}

	/**
	 * How should Date type values be displayed in this ListGrid by default? <P> This property specifies the default
	 * DateDisplayFormat to apply to Date values displayed in this grid for all fields except those of {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getType type "time"} (See also {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getTimeFormatter timeFormatter}).<br> If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDatetimeFormatter datetimeFormatter} is specified, that will be applied by
	 * default to fields of type <code>"datetime"</code>. <P> Note that if {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getDateFormatter dateFormatter} or {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getTimeFormatter timeFormatter} are specified those properties will take
	 * precedence over the component level settings. <P> If unset, date values will be formatted according to the system wide  
	 * short display format or   short datetime display format for datetime type fields. <P> If this field is editable the
	 * dateFormatter will also be passed to the editor created to edit this field as {@link
	 * com.smartgwt.client.widgets.form.fields.DateItem#getDateFormatter dateFormatter}. In this case you may also need to set
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getDateInputFormat dateInputFormat}.
	 *
	 * @param dateFormatter dateFormatter Default value is null
	 */
	public void setDateFormatter(DateDisplayFormat dateFormatter) {
		setAttribute("dateFormatter", dateFormatter.getValue(), true);
	}

	/**
	 * How should Date type values be displayed in this ListGrid by default? <P> This property specifies the default
	 * DateDisplayFormat to apply to Date values displayed in this grid for all fields except those of {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getType type "time"} (See also {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getTimeFormatter timeFormatter}).<br> If {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDatetimeFormatter datetimeFormatter} is specified, that will be applied by
	 * default to fields of type <code>"datetime"</code>. <P> Note that if {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getDateFormatter dateFormatter} or {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getTimeFormatter timeFormatter} are specified those properties will take
	 * precedence over the component level settings. <P> If unset, date values will be formatted according to the system wide  
	 * short display format or   short datetime display format for datetime type fields. <P> If this field is editable the
	 * dateFormatter will also be passed to the editor created to edit this field as {@link
	 * com.smartgwt.client.widgets.form.fields.DateItem#getDateFormatter dateFormatter}. In this case you may also need to set
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getDateInputFormat dateInputFormat}.
	 *
	 *
	 * @return DateDisplayFormat
	 */
	public DateDisplayFormat getDateFormatter() {
		return EnumUtil.getEnum(DateDisplayFormat.values(), getAttribute("dateFormatter"));
	}

	/**
	 * Display format to use for fields specified as type 'datetime'.  Default is to use the system-wide default date time
	 * format, configured via  Date.setShortDatetimeDisplayFormat.  Specify any valid {@link
	 * com.smartgwt.client.types.DateDisplayFormat} to change the display format for datetimes used by this grid.  <P> May also
	 * be specified at the field level via {@link com.smartgwt.client.widgets.grid.ListGridField#getDateFormatter
	 * dateFormatter} <P> If this field is editable the dateFormatter will also be passed to the editor created to edit this
	 * field as {@link com.smartgwt.client.widgets.form.fields.DateItem#getDateFormatter dateFormatter}. In this case you may
	 * also need to set {@link com.smartgwt.client.widgets.grid.ListGrid#getDateInputFormat dateInputFormat}.
	 *
	 * @param datetimeFormatter datetimeFormatter Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setDateFormatter
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setDatetimeFormatter(DateDisplayFormat datetimeFormatter) {
		setAttribute("datetimeFormatter", datetimeFormatter.getValue(), true);
	}

	/**
	 * Display format to use for fields specified as type 'datetime'.  Default is to use the system-wide default date time
	 * format, configured via  Date.setShortDatetimeDisplayFormat.  Specify any valid {@link
	 * com.smartgwt.client.types.DateDisplayFormat} to change the display format for datetimes used by this grid.  <P> May also
	 * be specified at the field level via {@link com.smartgwt.client.widgets.grid.ListGridField#getDateFormatter
	 * dateFormatter} <P> If this field is editable the dateFormatter will also be passed to the editor created to edit this
	 * field as {@link com.smartgwt.client.widgets.form.fields.DateItem#getDateFormatter dateFormatter}. In this case you may
	 * also need to set {@link com.smartgwt.client.widgets.grid.ListGrid#getDateInputFormat dateInputFormat}.
	 *
	 *
	 * @return DateDisplayFormat
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getDateFormatter
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public DateDisplayFormat getDatetimeFormatter() {
		return EnumUtil.getEnum(DateDisplayFormat.values(), getAttribute("datetimeFormatter"));
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true and {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is <code>"related"</code>, this property specifies the
	 * dataSource for the  related records grid to be shown embedded in expanded records. <P> This property may also be
	 * specified on a per-record basis - see  {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordDetailDSProperty
	 * recordDetailDSProperty}
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param detailDS detailDS Default value is null
	 */
	public void setDetailDS(String detailDS) {
		setAttribute("detailDS", detailDS, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true and {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is <code>"related"</code>, this property specifies the
	 * dataSource for the  related records grid to be shown embedded in expanded records. <P> This property may also be
	 * specified on a per-record basis - see  {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordDetailDSProperty
	 * recordDetailDSProperty}
	 *
	 *
	 * @return String
	 */
	public String getDetailDS() {
		return getAttributeAsString("detailDS");
	}

	/**
	 * The field whose contents to show in the expanded portion of a record when  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is <code>true</code> and  {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is <code>detailField</code>.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param detailField detailField Default value is null
	 */
	public void setDetailField(String detailField) {
		setAttribute("detailField", detailField, true);
	}

	/**
	 * The field whose contents to show in the expanded portion of a record when  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is <code>true</code> and  {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is <code>detailField</code>.
	 *
	 *
	 * @return String
	 */
	public String getDetailField() {
		return getAttributeAsString("detailField");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getConfirmDiscardEdits confirmDiscardEdits} is true this is the
	 * title for the save button appearing in the lost edits confirmation dialog. Override this for localization if necessary.
	 *
	 * @param discardEditsSaveButtonTitle discardEditsSaveButtonTitle Default value is "Save"
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setDiscardEditsSaveButtonTitle(String discardEditsSaveButtonTitle) {
		setAttribute("discardEditsSaveButtonTitle", discardEditsSaveButtonTitle, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getConfirmDiscardEdits confirmDiscardEdits} is true this is the
	 * title for the save button appearing in the lost edits confirmation dialog. Override this for localization if necessary.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public String getDiscardEditsSaveButtonTitle() {
		return getAttributeAsString("discardEditsSaveButtonTitle");
	}

	/**
	 * When records are being dragged from within a ListGrid, what sort of drag-tracker should be displayed?<br> Note that if
	 * multiple records are being dragged the displayed tracker will be based on the first selected record.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param dragTrackerMode dragTrackerMode Default value is "icon"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setDragTrackerMode(DragTrackerMode dragTrackerMode) throws IllegalStateException {
		setAttribute("dragTrackerMode", dragTrackerMode.getValue(), false);
	}

	/**
	 * When records are being dragged from within a ListGrid, what sort of drag-tracker should be displayed?<br> Note that if
	 * multiple records are being dragged the displayed tracker will be based on the first selected record.
	 *
	 *
	 * @return DragTrackerMode
	 */
	public DragTrackerMode getDragTrackerMode() {
		return EnumUtil.getEnum(DragTrackerMode.values(), getAttribute("dragTrackerMode"));
	}

	/**
	 * How far should we render rows ahead of the currently visible area?  This is expressed as a ratio from viewport size to
	 * rendered area size. <P> Tweaking drawAheadRatio allows you to make tradeoffs between continuous scrolling speed vs
	 * initial render time and render time when scrolling by large amounts. <P> NOTE: Only applies when showAllRows is false.
	 *
	 * @param drawAheadRatio drawAheadRatio Default value is 1.3
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_dataoperations_fetch" target="examples">Databound fetch Example</a>
	 */
	public void setDrawAheadRatio(float drawAheadRatio) {
		setAttribute("drawAheadRatio", drawAheadRatio, true);
	}

	/**
	 * How far should we render rows ahead of the currently visible area?  This is expressed as a ratio from viewport size to
	 * rendered area size. <P> Tweaking drawAheadRatio allows you to make tradeoffs between continuous scrolling speed vs
	 * initial render time and render time when scrolling by large amounts. <P> NOTE: Only applies when showAllRows is false.
	 *
	 *
	 * @return float
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_dataoperations_fetch" target="examples">Databound fetch Example</a>
	 */
	public float getDrawAheadRatio() {
		return getAttributeAsFloat("drawAheadRatio");
	}

	/**
	 * If drawing all rows would cause less than <code>drawAllMaxCells</code> cells to be rendered, the full dataset will
	 * instead be drawn even if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllRecords showAllRecords} is false and
	 * the viewport size and {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} setting would
	 * normally have caused incremental rendering to be used. <P> The <code>drawAllMaxCells</code> setting prevents incremental
	 * rendering from being used in situations where it's really unnecessary, such as a 40 row, 5 column dataset (only 200
	 * cells) which happens to be in a grid with a viewport showing only 20 or so rows. Incremental rendering causes a brief
	 * "flash" during scrolling as the visible portion of the dataset is redrawn, and a better scrolling experience can be
	 * obtained in this situation by drawing the entire dataset up front, which in this example would have negligible effect on
	 * initial draw time. <P> <code>drawAllMaxCells:0</code> disables this features.  You may want to disable this feature if
	 * performance is an issue and: <ul> <li> you are very frequently redraw a grid <li> you do a lot of computation when
	 * rendering each cell (eg formulas) <li> you are showing many grids on one screen and the user won't scroll most of them
	 * </ul>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param drawAllMaxCells drawAllMaxCells Default value is 250
	 */
	public void setDrawAllMaxCells(int drawAllMaxCells) {
		setAttribute("drawAllMaxCells", drawAllMaxCells, true);
	}

	/**
	 * If drawing all rows would cause less than <code>drawAllMaxCells</code> cells to be rendered, the full dataset will
	 * instead be drawn even if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowAllRecords showAllRecords} is false and
	 * the viewport size and {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} setting would
	 * normally have caused incremental rendering to be used. <P> The <code>drawAllMaxCells</code> setting prevents incremental
	 * rendering from being used in situations where it's really unnecessary, such as a 40 row, 5 column dataset (only 200
	 * cells) which happens to be in a grid with a viewport showing only 20 or so rows. Incremental rendering causes a brief
	 * "flash" during scrolling as the visible portion of the dataset is redrawn, and a better scrolling experience can be
	 * obtained in this situation by drawing the entire dataset up front, which in this example would have negligible effect on
	 * initial draw time. <P> <code>drawAllMaxCells:0</code> disables this features.  You may want to disable this feature if
	 * performance is an issue and: <ul> <li> you are very frequently redraw a grid <li> you do a lot of computation when
	 * rendering each cell (eg formulas) <li> you are showing many grids on one screen and the user won't scroll most of them
	 * </ul>
	 *
	 *
	 * @return int
	 */
	public int getDrawAllMaxCells() {
		return getAttributeAsInt("drawAllMaxCells");
	}

	/**
	 * Determines whether when the user edits a cell in this listGrid the entire row becomes editable, or just the cell that
	 * received the edit event. <P> No effect if this.canEdit is false or null.
	 *
	 * @param editByCell editByCell Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCanEdit
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_cell" target="examples">Edit by cell Example</a>
	 */
	public void setEditByCell(Boolean editByCell) {
		setAttribute("editByCell", editByCell, true);
	}

	/**
	 * Determines whether when the user edits a cell in this listGrid the entire row becomes editable, or just the cell that
	 * received the edit event. <P> No effect if this.canEdit is false or null.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCanEdit
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_cell" target="examples">Edit by cell Example</a>
	 */
	public Boolean getEditByCell() {
		return getAttributeAsBoolean("editByCell");
	}

	/**
	 * Event that will trigger inline editing, see {@link com.smartgwt.client.types.ListGridEditEvent} for options. <P> Note
	 * this setting has no effect unless {@link com.smartgwt.client.widgets.grid.ListGrid#getCanEdit canEdit} has been set to
	 * enable editing. <P> See also {@link com.smartgwt.client.widgets.grid.ListGrid#getEditOnFocus editOnFocus} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#startEditing ListGrid.startEditing}.
	 *
	 * @param editEvent editEvent Default value is "doubleClick"
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_row" target="examples">Edit by row Example</a>
	 */
	public void setEditEvent(ListGridEditEvent editEvent) {
		setAttribute("editEvent", editEvent.getValue(), true);
	}

	/**
	 * Event that will trigger inline editing, see {@link com.smartgwt.client.types.ListGridEditEvent} for options. <P> Note
	 * this setting has no effect unless {@link com.smartgwt.client.widgets.grid.ListGrid#getCanEdit canEdit} has been set to
	 * enable editing. <P> See also {@link com.smartgwt.client.widgets.grid.ListGrid#getEditOnFocus editOnFocus} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#startEditing ListGrid.startEditing}.
	 *
	 *
	 * @return ListGridEditEvent
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_row" target="examples">Edit by row Example</a>
	 */
	public ListGridEditEvent getEditEvent() {
		return EnumUtil.getEnum(ListGridEditEvent.values(), getAttribute("editEvent"));
	}

	/**
	 * A base name for the CSS class applied to cells when editing has failed.<br>  If this listGrid is editable, this style
	 * will be applied to any edited cells for which  validation failed.<br>  As with the default 'baseStyle' property, this
	 * style will have "Dark", "Over", "Selected",   or "Disabled" appended to it according to the state of the cell.<br> If
	 * null, cells for which editing has failed will be rendered using the normal base style classNames, but with custom
	 * CSSText applied as derived from <code>this.editFailedCSSText</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param editFailedBaseStyle editFailedBaseStyle Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEditFailedCSSText
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setEditFailedBaseStyle(String editFailedBaseStyle) {
		setAttribute("editFailedBaseStyle", editFailedBaseStyle, true);
	}

	/**
	 * A base name for the CSS class applied to cells when editing has failed.<br>  If this listGrid is editable, this style
	 * will be applied to any edited cells for which  validation failed.<br>  As with the default 'baseStyle' property, this
	 * style will have "Dark", "Over", "Selected",   or "Disabled" appended to it according to the state of the cell.<br> If
	 * null, cells for which editing has failed will be rendered using the normal base style classNames, but with custom
	 * CSSText applied as derived from <code>this.editFailedCSSText</code>
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEditFailedCSSText
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getEditFailedBaseStyle() {
		return getAttributeAsString("editFailedBaseStyle");
	}

	/**
	 * Custom CSS text to be applied to cells when editing has failed.<br>  If this listGrid is editable, this css text will be
	 * applied to any edited cells for which  validation failed, on top of the base style for the cell.<br> For further
	 * customization of styling for cells that failed editing validation, use <code>this.editFailedBaseStyle</code> instead.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param editFailedCSSText editFailedCSSText Default value is "color:red;border:1px solid red;"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEditFailedBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setEditFailedCSSText(String editFailedCSSText) {
		setAttribute("editFailedCSSText", editFailedCSSText, true);
	}

	/**
	 * Custom CSS text to be applied to cells when editing has failed.<br>  If this listGrid is editable, this css text will be
	 * applied to any edited cells for which  validation failed, on top of the base style for the cell.<br> For further
	 * customization of styling for cells that failed editing validation, use <code>this.editFailedBaseStyle</code> instead.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEditFailedBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getEditFailedCSSText() {
		return getAttributeAsString("editFailedCSSText");
	}

	/**
	 * Should we start editing when the widget has focus and the user presses the "f2" key (if this ListGrid supports editing)?
	 * <P> Note that if {@link com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is set to <code>"click"</code>
	 * or <code>"doubleClick"</code>, the <code>Space</code> or <code>Enter</code> key may also be used to start editing,
	 * depending on the value for {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateClickOnSpace
	 * generateClickOnSpace}, {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateDoubleClickOnSpace
	 * generateDoubleClickOnSpace}, {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateClickOnEnter
	 * generateClickOnEnter} and  {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateDoubleClickOnEnter
	 * generateDoubleClickOnEnter}. <P> If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanEdit canEdit} is false, or
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is set to "none" this property has no effect.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param editOnF2Keypress editOnF2Keypress Default value is true
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setEditOnF2Keypress(Boolean editOnF2Keypress) {
		setAttribute("editOnF2Keypress", editOnF2Keypress, true);
	}

	/**
	 * Should we start editing when the widget has focus and the user presses the "f2" key (if this ListGrid supports editing)?
	 * <P> Note that if {@link com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is set to <code>"click"</code>
	 * or <code>"doubleClick"</code>, the <code>Space</code> or <code>Enter</code> key may also be used to start editing,
	 * depending on the value for {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateClickOnSpace
	 * generateClickOnSpace}, {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateDoubleClickOnSpace
	 * generateDoubleClickOnSpace}, {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateClickOnEnter
	 * generateClickOnEnter} and  {@link com.smartgwt.client.widgets.grid.ListGrid#getGenerateDoubleClickOnEnter
	 * generateDoubleClickOnEnter}. <P> If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanEdit canEdit} is false, or
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is set to "none" this property has no effect.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getEditOnF2Keypress() {
		return getAttributeAsBoolean("editOnF2Keypress");
	}

	/**
	 * Should we start editing when this widget receives focus (if this ListGrid supports editing)? <P> Note that this property
	 * being set to true will cause editing to occur on a single click, even if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is <code>"doubleClick"</code>, because single clicking
	 * the grid will place keyboard focus there automatically.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param editOnFocus editOnFocus Default value is null
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setEditOnFocus(Boolean editOnFocus) {
		setAttribute("editOnFocus", editOnFocus, true);
	}

	/**
	 * Should we start editing when this widget receives focus (if this ListGrid supports editing)? <P> Note that this property
	 * being set to true will cause editing to occur on a single click, even if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getEditEvent editEvent} is <code>"doubleClick"</code>, because single clicking
	 * the grid will place keyboard focus there automatically.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getEditOnFocus() {
		return getAttributeAsBoolean("editOnFocus");
	}

	/**
	 * A base name for the CSS class applied to cells containing pending (unsaved) edits<br>  As with the default 'baseStyle'
	 * property, this style will have "Dark", "Over", "Selected",   or "Disabled" appended to it according to the state of the
	 * cell.  If this property is null, cells with pending edits will pick up custom css text to  be applied on top of the
	 * normal base style from <code>this.editPendingCSSText</code>
	 *
	 * <b>Note :</b> This method should be called only after the widget has been rendered.
	 *
	 * @return String
	 * @throws IllegalStateException if widget has not yet been rendered.
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getEditPendingBaseStyle() throws IllegalStateException {
		errorIfNotCreated("editPendingBaseStyle");
		return getAttributeAsString("editPendingBaseStyle");
	}

	/**
	 * Custom CSS text to be applied to cells with pending edits that have not yet been  submitted.<br> For further
	 * customization of styling for cells with pending edits use <code>this.editPendingBaseStyle</code> instead.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param editPendingCSSText editPendingCSSText Default value is "color:#0066CC;"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEditFailedBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setEditPendingCSSText(String editPendingCSSText) {
		setAttribute("editPendingCSSText", editPendingCSSText, true);
	}

	/**
	 * Custom CSS text to be applied to cells with pending edits that have not yet been  submitted.<br> For further
	 * customization of styling for cells with pending edits use <code>this.editPendingBaseStyle</code> instead.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEditFailedBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getEditPendingCSSText() {
		return getAttributeAsString("editPendingCSSText");
	}

	/**
	 * The value to display for cells whose value is null or the empty string after applying formatCellValue and valueMap (if
	 * any). <p> This is the grid-wide attribute.  You may also set the emptyCellValue on a per-field basis.
	 *
	 * @param emptyCellValue emptyCellValue Default value is "&nbsp;"
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setEmptyCellValue
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_values" target="examples">Empty values Example</a>
	 */
	public void setEmptyCellValue(String emptyCellValue) {
		setAttribute("emptyCellValue", emptyCellValue, true);
	}

	/**
	 * The value to display for cells whose value is null or the empty string after applying formatCellValue and valueMap (if
	 * any). <p> This is the grid-wide attribute.  You may also set the emptyCellValue on a per-field basis.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getEmptyCellValue
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_values" target="examples">Empty values Example</a>
	 */
	public String getEmptyCellValue() {
		return getAttributeAsString("emptyCellValue");
	}

	/**
	 * The string to display in the body of a listGrid with an empty data array, if showEmptyMessage is true.
	 *
	 * @param emptyMessage emptyMessage Default value is "No items to show."
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowEmptyMessage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEmptyMessageStyle
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_grid" target="examples">Empty grid Example</a>
	 */
	public void setEmptyMessage(String emptyMessage) {
		setAttribute("emptyMessage", emptyMessage, true);
	}

	/**
	 * The string to display in the body of a listGrid with an empty data array, if showEmptyMessage is true.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowEmptyMessage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEmptyMessageStyle
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_grid" target="examples">Empty grid Example</a>
	 */
	public String getEmptyMessage() {
		return getAttributeAsString("emptyMessage");
	}

	/**
	 * The CSS style name applied to the {@link com.smartgwt.client.widgets.grid.ListGrid#getEmptyMessage emptyMessage} if
	 * displayed.
	 *
	 * @param emptyMessageStyle emptyMessageStyle Default value is "emptyMessage"
	 */
	public void setEmptyMessageStyle(String emptyMessageStyle) {
		setAttribute("emptyMessageStyle", emptyMessageStyle, true);
	}

	/**
	 * The CSS style name applied to the {@link com.smartgwt.client.widgets.grid.ListGrid#getEmptyMessage emptyMessage} if
	 * displayed.
	 *
	 *
	 * @return String
	 */
	public String getEmptyMessageStyle() {
		return getAttributeAsString("emptyMessageStyle");
	}

	/**
	 * For performance reasons, even when {@link com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights
	 * fixedRecordHeights} is set, vertical clipping is not enforced by default for some kinds of content (such as images) on
	 * all browsers. Set {@link com.smartgwt.client.widgets.grid.ListGrid#getEnforceVClipping enforceVClipping:true} to enforce
	 * clipping for all types of content on all browsers. <P> This additional setting is likely to be phased out as browsers
	 * improve.
	 *
	 * @param enforceVClipping enforceVClipping Default value is false
	 */
	public void setEnforceVClipping(Boolean enforceVClipping) {
		setAttribute("enforceVClipping", enforceVClipping, true);
	}

	/**
	 * For performance reasons, even when {@link com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights
	 * fixedRecordHeights} is set, vertical clipping is not enforced by default for some kinds of content (such as images) on
	 * all browsers. Set {@link com.smartgwt.client.widgets.grid.ListGrid#getEnforceVClipping enforceVClipping:true} to enforce
	 * clipping for all types of content on all browsers. <P> This additional setting is likely to be phased out as browsers
	 * improve.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getEnforceVClipping() {
		return getAttributeAsBoolean("enforceVClipping");
	}

	/**
	 * What to do when a user hits enter while editing a cell: <ul> <li>"nextCell": start editing the next editable cell in
	 * this record (or the first     editable cell in the next record if focus is in the last editable cell in the row)
	 * <li>"nextRow": start editing the same field in the next row (skipping any rows where      that would be a non-editable
	 * cell. <li>"nextRowStart": start editing the first editable cell in the next row. <li>"done": hide the editor (editing is
	 * complete) </ul> Note that if this.autoSaveEdits is true, this may cause a save of the current edit values
	 *
	 * @param enterKeyEditAction enterKeyEditAction Default value is "done"
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setEnterKeyEditAction(EnterKeyEditAction enterKeyEditAction) {
		setAttribute("enterKeyEditAction", enterKeyEditAction.getValue(), true);
	}

	/**
	 * What to do when a user hits enter while editing a cell: <ul> <li>"nextCell": start editing the next editable cell in
	 * this record (or the first     editable cell in the next record if focus is in the last editable cell in the row)
	 * <li>"nextRow": start editing the same field in the next row (skipping any rows where      that would be a non-editable
	 * cell. <li>"nextRowStart": start editing the first editable cell in the next row. <li>"done": hide the editor (editing is
	 * complete) </ul> Note that if this.autoSaveEdits is true, this may cause a save of the current edit values
	 *
	 *
	 * @return EnterKeyEditAction
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public EnterKeyEditAction getEnterKeyEditAction() {
		return EnumUtil.getEnum(EnterKeyEditAction.values(), getAttribute("enterKeyEditAction"));
	}

	/**
	 * In a ListGrid that has a DataSource and has filter criteria that include values for fields declared as {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDSFieldType type "enum"} in the DataSource, by default a newly edited row
	 * will use those filter criteria as initial values. <P> For example, if a ListGrid is showing all Accounts that have
	 * status:"Active" and a new row is created, the new row will default to status:"Active" unless this flag is set to false.
	 *
	 * @param enumCriteriaAsInitialValues enumCriteriaAsInitialValues Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setEnumCriteriaAsInitialValues(Boolean enumCriteriaAsInitialValues) throws IllegalStateException {
		setAttribute("enumCriteriaAsInitialValues", enumCriteriaAsInitialValues, false);
	}

	/**
	 * In a ListGrid that has a DataSource and has filter criteria that include values for fields declared as {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDSFieldType type "enum"} in the DataSource, by default a newly edited row
	 * will use those filter criteria as initial values. <P> For example, if a ListGrid is showing all Accounts that have
	 * status:"Active" and a new row is created, the new row will default to status:"Active" unless this flag is set to false.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getEnumCriteriaAsInitialValues() {
		return getAttributeAsBoolean("enumCriteriaAsInitialValues");
	}

	/**
	 * What to do when a user hits escape while editing a cell:<ul> <li>"cancel": close the editor and discard the current set
	 * of edit values <li>"done": just close the editor (the edit is complete, but the edited values are retained). </ul> Note
	 * that if {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoSaveEdits autoSaveEdits} is true, this may cause a save
	 * of the current edit values
	 *
	 * @param escapeKeyEditAction escapeKeyEditAction Default value is "cancel"
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setEscapeKeyEditAction(EscapeKeyEditAction escapeKeyEditAction) {
		setAttribute("escapeKeyEditAction", escapeKeyEditAction.getValue(), true);
	}

	/**
	 * What to do when a user hits escape while editing a cell:<ul> <li>"cancel": close the editor and discard the current set
	 * of edit values <li>"done": just close the editor (the edit is complete, but the edited values are retained). </ul> Note
	 * that if {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoSaveEdits autoSaveEdits} is true, this may cause a save
	 * of the current edit values
	 *
	 *
	 * @return EscapeKeyEditAction
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public EscapeKeyEditAction getEscapeKeyEditAction() {
		return EnumUtil.getEnum(EscapeKeyEditAction.values(), getAttribute("escapeKeyEditAction"));
	}

	/**
	 * For {@link com.smartgwt.client.types.ExpansionMode expansionModes} that show another grid or tree, is that  component
	 * editable? <P>The default value for this property is <code>false</code>.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param expansionCanEdit expansionCanEdit Default value is false
	 */
	public void setExpansionCanEdit(Boolean expansionCanEdit) {
		setAttribute("expansionCanEdit", expansionCanEdit, true);
	}

	/**
	 * For {@link com.smartgwt.client.types.ExpansionMode expansionModes} that show another grid or tree, is that  component
	 * editable? <P>The default value for this property is <code>false</code>.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getExpansionCanEdit() {
		return getAttributeAsBoolean("expansionCanEdit");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getExpansionMode expansionMode} is <i>editor</i>, the prompt to display when
	 * an expanded row is collapsed while it's nested editor has changed values.
	 *
	 * @param expansionEditorSaveDialogPrompt expansionEditorSaveDialogPrompt Default value is "You have unsaved changes - do you want to save them now?"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setExpansionEditorSaveDialogPrompt(String expansionEditorSaveDialogPrompt) throws IllegalStateException {
		setAttribute("expansionEditorSaveDialogPrompt", expansionEditorSaveDialogPrompt, false);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getExpansionMode expansionMode} is <i>editor</i>, the prompt to display when
	 * an expanded row is collapsed while it's nested editor has changed values.
	 *
	 *
	 * @return String
	 */
	public String getExpansionEditorSaveDialogPrompt() {
		return getAttributeAsString("expansionEditorSaveDialogPrompt");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property determines the image to display in the expansion field for collapsed rows. If unset, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} will be used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param expansionFieldFalseImage expansionFieldFalseImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldImageHeight
	 */
	public void setExpansionFieldFalseImage(String expansionFieldFalseImage) {
		setAttribute("expansionFieldFalseImage", expansionFieldFalseImage, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property determines the image to display in the expansion field for collapsed rows. If unset, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanFalseImage booleanFalseImage} will be used.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldTrueImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldImageHeight
	 */
	public String getExpansionFieldFalseImage() {
		return getAttributeAsString("expansionFieldFalseImage");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property may be set to govern the height of the expansion image displayed to indicate whether a  row is expanded.
	 * If unset, the expansionField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageHeight booleanImageHeight} for this grid.
	 *
	 * @param expansionFieldImageHeight expansionFieldImageHeight Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setExpansionFieldImageHeight(Integer expansionFieldImageHeight) throws IllegalStateException {
		setAttribute("expansionFieldImageHeight", expansionFieldImageHeight, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property may be set to govern the height of the expansion image displayed to indicate whether a  row is expanded.
	 * If unset, the expansionField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageHeight booleanImageHeight} for this grid.
	 *
	 *
	 * @return Integer
	 */
	public Integer getExpansionFieldImageHeight() {
		return getAttributeAsInt("expansionFieldImageHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property may be set to govern the width of the expansion image displayed to indicate whether a row  is expanded. If
	 * unset, the expansionField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageWidth booleanImageWidth} for this grid.
	 *
	 * @param expansionFieldImageWidth expansionFieldImageWidth Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setExpansionFieldImageWidth(Integer expansionFieldImageWidth) throws IllegalStateException {
		setAttribute("expansionFieldImageWidth", expansionFieldImageWidth, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property may be set to govern the width of the expansion image displayed to indicate whether a row  is expanded. If
	 * unset, the expansionField image will be sized to match the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanImageWidth booleanImageWidth} for this grid.
	 *
	 *
	 * @return Integer
	 */
	public Integer getExpansionFieldImageWidth() {
		return getAttributeAsInt("expansionFieldImageWidth");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property determines the image to display in the expansion field for expanded rows. If unset, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} will be used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param expansionFieldTrueImage expansionFieldTrueImage Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setExpansionFieldImageHeight
	 */
	public void setExpansionFieldTrueImage(String expansionFieldTrueImage) {
		setAttribute("expansionFieldTrueImage", expansionFieldTrueImage, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is set to <code>true</code>,
	 * this property determines the image to display in the expansion field for expanded rows. If unset, the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBooleanTrueImage booleanTrueImage} will be used.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldFalseImage
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldImageWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getExpansionFieldImageHeight
	 */
	public String getExpansionFieldTrueImage() {
		return getAttributeAsString("expansionFieldTrueImage");
	}

	/**
	 * The {@link com.smartgwt.client.types.ExpansionMode} for records in this grid. Default <code>null</code> value means no
	 * expansion.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param expansionMode expansionMode Default value is null
	 */
	public void setExpansionMode(ExpansionMode expansionMode) {
		setAttribute("expansionMode", expansionMode.getValue(), true);
	}

	/**
	 * The {@link com.smartgwt.client.types.ExpansionMode} for records in this grid. Default <code>null</code> value means no
	 * expansion.
	 *
	 *
	 * @return ExpansionMode
	 */
	public ExpansionMode getExpansionMode() {
		return EnumUtil.getEnum(ExpansionMode.values(), getAttribute("expansionMode"));
	}

	/**
	 * Dictates whether the data in this grid should be exported raw by  {@link
	 * com.smartgwt.client.widgets.DataBoundComponent#exportClientData exportClientData()}.  If set to true,   data will not be
	 * processed by field-formatters during exports. Decreases the time taken for large exports.  This property can also be set
	 * at the {@link com.smartgwt.client.widgets.grid.ListGridField#getExportRawValues field level}.
	 *
	 * @param exportRawValues exportRawValues Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setExportRawValues(Boolean exportRawValues) throws IllegalStateException {
		setAttribute("exportRawValues", exportRawValues, false);
	}

	/**
	 * Dictates whether the data in this grid should be exported raw by  {@link
	 * com.smartgwt.client.widgets.DataBoundComponent#exportClientData exportClientData()}.  If set to true,   data will not be
	 * processed by field-formatters during exports. Decreases the time taken for large exports.  This property can also be set
	 * at the {@link com.smartgwt.client.widgets.grid.ListGridField#getExportRawValues field level}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getExportRawValues() {
		return getAttributeAsBoolean("exportRawValues");
	}

	/**
	 * <b>Note: This property only has an effect in Internet Explorer</b> <P> Advanced property to improve performance for
	 * dynamic styling of gridRenderer cells in Internet Explorer, at the expense of slightly slower initial drawing, and some 
	 * limitations on supported styling options. <P> <code>fastCellUpdates</code> speeds up the dynamic styling system used by
	 * rollovers, selections, and custom styling that calls {@link com.smartgwt.client.grid.GridRenderer#refreshCellStyle
	 * GridRenderer.refreshCellStyle}, at the cost of slightly slower draw() and redraw() times. <P> Notes: <ul> <li>When this
	 * property is set, ListGrid cells may be styled using the      {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle}. See {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle ListGrid.getBaseStyle} for      more information.</li> <li>If any
	 * cell styles specify a a background image URL, the URL will be resolved relative     to the page location rather than the
	 * location of the CSS stylesheet. This means cell     styles with a background URL should either supply a fully qualified
	 * path, or the     background image media should be made available at a second location for IE.</li> <li>fastCellUpdates
	 * will not work if the styles involved are in an external stylesheet loaded     from a remote host. Either the stylesheet
	 * containing cell styles needs to be loaded     from the same host as the main page, or the cell styles need to be inlined
	 * in the html      of the bootstrap page.</li> <li>fastCellUpdates will not work if the css styles for cells are defined
	 * in     a <code>.css</code> file loaded via <code>@import</code>. Instead the <code>.css</code>     file should be loaded
	 * via a <code>&lt;link ...&gt;</code> tag.</li> </ul>
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.grid.GridRenderer#getFastCellUpdates fastCellUpdates}. Has no effect in browsers other than Internet Explorer.
	 *
	 * @param fastCellUpdates whether to enable fastCellUpdates.. Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setFastCellUpdates(Boolean fastCellUpdates) throws IllegalStateException {
		setAttribute("fastCellUpdates", fastCellUpdates, false);
	}

	/**
	 * If we're showing the filterEditor ({@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor
	 * showFilterEditor} is true), and we're re-filtering on every keypress ({@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFilterOnKeypress filterOnKeypress} is true), this  property is the delay in
	 * milliseconds between the user changing the filter and the  filter request being kicked off. If multiple changes are made
	 * to the filter  within this fetch delay, only the most recent will actually cause a re-filter
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param fetchDelay fetchDelay Default value is 300
	 */
	public void setFetchDelay(int fetchDelay) {
		setAttribute("fetchDelay", fetchDelay, true);
	}

	/**
	 * If we're showing the filterEditor ({@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor
	 * showFilterEditor} is true), and we're re-filtering on every keypress ({@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFilterOnKeypress filterOnKeypress} is true), this  property is the delay in
	 * milliseconds between the user changing the filter and the  filter request being kicked off. If multiple changes are made
	 * to the filter  within this fetch delay, only the most recent will actually cause a re-filter
	 *
	 *
	 * @return int
	 */
	public int getFetchDelay() {
		return getAttributeAsInt("fetchDelay");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields this.canPickFields} is true, this attribute
	 * will be shown as the title for the menu item which contains a submenu with items  allowing the user to show and hide
	 * fields in the grid.
	 *
	 * @param fieldVisibilitySubmenuTitle fieldVisibilitySubmenuTitle Default value is "Columns"
	 */
	public void setFieldVisibilitySubmenuTitle(String fieldVisibilitySubmenuTitle) {
		setAttribute("fieldVisibilitySubmenuTitle", fieldVisibilitySubmenuTitle, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanPickFields this.canPickFields} is true, this attribute
	 * will be shown as the title for the menu item which contains a submenu with items  allowing the user to show and hide
	 * fields in the grid.
	 *
	 *
	 * @return String
	 */
	public String getFieldVisibilitySubmenuTitle() {
		return getAttributeAsString("fieldVisibilitySubmenuTitle");
	}

	/**
	 * The prompt to show when the mouse hovers over the Filter button in the FilterEditor.
	 *
	 * @param filterButtonPrompt filterButtonPrompt Default value is "Filter"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setFilterButtonPrompt(String filterButtonPrompt) throws IllegalStateException {
		setAttribute("filterButtonPrompt", filterButtonPrompt, false);
	}

	/**
	 * The prompt to show when the mouse hovers over the Filter button in the FilterEditor.
	 *
	 *
	 * @return String
	 */
	public String getFilterButtonPrompt() {
		return getAttributeAsString("filterButtonPrompt");
	}

	/**
	 * If we're showing the filterEditor (this.showFilterEditor is true), this property  determines whether this list should be
	 * filtered every time the user puts focus in a different field in the filter editor.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param filterByCell filterByCell Default value is null
	 */
	public void setFilterByCell(Boolean filterByCell) {
		setAttribute("filterByCell", filterByCell, true);
	}

	/**
	 * If we're showing the filterEditor (this.showFilterEditor is true), this property  determines whether this list should be
	 * filtered every time the user puts focus in a different field in the filter editor.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getFilterByCell() {
		return getAttributeAsBoolean("filterByCell");
	}

	/**
	 * Height for the filterEditor, if shown.
	 *
	 * @param filterEditorHeight filterEditorHeight Default value is 22
	 */
	public void setFilterEditorHeight(int filterEditorHeight) {
		setAttribute("filterEditorHeight", filterEditorHeight, true);
	}

	/**
	 * Height for the filterEditor, if shown.
	 *
	 *
	 * @return int
	 */
	public int getFilterEditorHeight() {
		return getAttributeAsInt("filterEditorHeight");
	}

	/**
	 * If we're showing the filterEditor (this.showFilterEditor is true), this property  determines whether this list should be
	 * filtered every time the user modifies the value in a field of the filter-editor. Can also be set at the field level.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param filterOnKeypress filterOnKeypress Default value is null
	 */
	public void setFilterOnKeypress(Boolean filterOnKeypress) {
		setAttribute("filterOnKeypress", filterOnKeypress, true);
	}

	/**
	 * If we're showing the filterEditor (this.showFilterEditor is true), this property  determines whether this list should be
	 * filtered every time the user modifies the value in a field of the filter-editor. Can also be set at the field level.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getFilterOnKeypress() {
		return getAttributeAsBoolean("filterOnKeypress");
	}

	/**
	 * Should we horizontally clip cell contents, or allow columns to expand horizontally to show all contents? <P> If we allow
	 * columns to expand, the column width is treated as a minimum. <P> NOTE: the header does not automatically respond to
	 * expanded field widths
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param fixedFieldWidths fixedFieldWidths Default value is true
	 */
	public void setFixedFieldWidths(Boolean fixedFieldWidths) {
		setAttribute("fixedFieldWidths", fixedFieldWidths, true);
	}

	/**
	 * Should we horizontally clip cell contents, or allow columns to expand horizontally to show all contents? <P> If we allow
	 * columns to expand, the column width is treated as a minimum. <P> NOTE: the header does not automatically respond to
	 * expanded field widths
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getFixedFieldWidths() {
		return getAttributeAsBoolean("fixedFieldWidths");
	}

	/**
	 * Should we vertically clip cell contents, or allow rows to expand vertically to show all contents? <P> If we allow rows
	 * to expand, the row height as derived from {@link com.smartgwt.client.grid.GridRenderer#getRowHeight getRowHeight()} or
	 * the default {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight} is treated as a minimum. <P>
	 * <b>NOTE:</b> by default, for performance reasons, clipping is not enforced for some kinds of content (such as images) on
	 * all browsers.  Set {@link com.smartgwt.client.widgets.grid.ListGrid#getEnforceVClipping enforceVClipping:true} to
	 * enforce clipping for all types of content on all browsers.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param fixedRecordHeights fixedRecordHeights Default value is true
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_values" target="examples">Cell Values Example</a>
	 */
	public void setFixedRecordHeights(Boolean fixedRecordHeights) {
		setAttribute("fixedRecordHeights", fixedRecordHeights, true);
	}

	/**
	 * Should we vertically clip cell contents, or allow rows to expand vertically to show all contents? <P> If we allow rows
	 * to expand, the row height as derived from {@link com.smartgwt.client.grid.GridRenderer#getRowHeight getRowHeight()} or
	 * the default {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight} is treated as a minimum. <P>
	 * <b>NOTE:</b> by default, for performance reasons, clipping is not enforced for some kinds of content (such as images) on
	 * all browsers.  Set {@link com.smartgwt.client.widgets.grid.ListGrid#getEnforceVClipping enforceVClipping:true} to
	 * enforce clipping for all types of content on all browsers.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_values" target="examples">Cell Values Example</a>
	 */
	public Boolean getFixedRecordHeights() {
		return getAttributeAsBoolean("fixedRecordHeights");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze a currently unfrozen field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Freeze " + the field's summary title.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param freezeFieldText freezeFieldText Default value is "Freeze \${title}"
	 */
	public void setFreezeFieldText(String freezeFieldText) {
		setAttribute("freezeFieldText", freezeFieldText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze a currently unfrozen field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Freeze " + the field's summary title.
	 *
	 *
	 * @return String
	 */
	public String getFreezeFieldText() {
		return getAttributeAsString("freezeFieldText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze fields on the left of the scrollable body.
	 *
	 * @param freezeOnLeftText freezeOnLeftText Default value is "Freeze on left"
	 */
	public void setFreezeOnLeftText(String freezeOnLeftText) {
		setAttribute("freezeOnLeftText", freezeOnLeftText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze fields on the left of the scrollable body.
	 *
	 *
	 * @return String
	 */
	public String getFreezeOnLeftText() {
		return getAttributeAsString("freezeOnLeftText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze fields on the right of the scrollable body.
	 *
	 * @param freezeOnRightText freezeOnRightText Default value is "Freeze on right"
	 */
	public void setFreezeOnRightText(String freezeOnRightText) {
		setAttribute("freezeOnRightText", freezeOnRightText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to freeze fields on the right of the scrollable body.
	 *
	 *
	 * @return String
	 */
	public String getFreezeOnRightText() {
		return getAttributeAsString("freezeOnRightText");
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom baseStyle to all cells in those
	 * frozen fields. If unset, the standard base style will be used for both frozen and unfrozen cells.
	 *
	 * @param frozenBaseStyle frozenBaseStyle Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setFrozen
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setFrozenBaseStyle(String frozenBaseStyle) {
		setAttribute("frozenBaseStyle", frozenBaseStyle, true);
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom baseStyle to all cells in those
	 * frozen fields. If unset, the standard base style will be used for both frozen and unfrozen cells.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getFrozen
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getFrozenBaseStyle() {
		return getAttributeAsString("frozenBaseStyle");
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom headerBaseStyle to the frozen
	 * set of fields. If unset, the standard headerBaseStyle will be used for both frozen and unfrozen cells.
	 *
	 * @param frozenHeaderBaseStyle frozenHeaderBaseStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setHeaderBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setFrozen
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setFrozenHeaderBaseStyle(String frozenHeaderBaseStyle) throws IllegalStateException {
		setAttribute("frozenHeaderBaseStyle", frozenHeaderBaseStyle, false);
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom headerBaseStyle to the frozen
	 * set of fields. If unset, the standard headerBaseStyle will be used for both frozen and unfrozen cells.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getHeaderBaseStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getFrozen
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getFrozenHeaderBaseStyle() {
		return getAttributeAsString("frozenHeaderBaseStyle");
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom headerTitleStyle to the frozen
	 * set of fields. If unset, the standard headerTitleStyle will be used for both frozen and unfrozen cells.
	 *
	 * @param frozenHeaderTitleStyle frozenHeaderTitleStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setHeaderTitleStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setFrozen
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setFrozenHeaderTitleStyle(String frozenHeaderTitleStyle) throws IllegalStateException {
		setAttribute("frozenHeaderTitleStyle", frozenHeaderTitleStyle, false);
	}

	/**
	 * If this listGrid contains any frozen fields, this property can be used to apply a custom headerTitleStyle to the frozen
	 * set of fields. If unset, the standard headerTitleStyle will be used for both frozen and unfrozen cells.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getHeaderTitleStyle
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getFrozen
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getFrozenHeaderTitleStyle() {
		return getAttributeAsString("frozenHeaderTitleStyle");
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Enter,  the cell will respond to a click event.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param generateClickOnEnter generateClickOnEnter Default value is false
	 */
	public void setGenerateClickOnEnter(Boolean generateClickOnEnter) {
		setAttribute("generateClickOnEnter", generateClickOnEnter, true);
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Enter,  the cell will respond to a click event.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getGenerateClickOnEnter() {
		return getAttributeAsBoolean("generateClickOnEnter");
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits space,  the cell will respond to a click event.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param generateClickOnSpace generateClickOnSpace Default value is true
	 */
	public void setGenerateClickOnSpace(Boolean generateClickOnSpace) {
		setAttribute("generateClickOnSpace", generateClickOnSpace, true);
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits space,  the cell will respond to a click event.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getGenerateClickOnSpace() {
		return getAttributeAsBoolean("generateClickOnSpace");
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Enter,  the cell will respond to a double click
	 * event.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param generateDoubleClickOnEnter generateDoubleClickOnEnter Default value is true
	 */
	public void setGenerateDoubleClickOnEnter(Boolean generateDoubleClickOnEnter) {
		setAttribute("generateDoubleClickOnEnter", generateDoubleClickOnEnter, true);
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Enter,  the cell will respond to a double click
	 * event.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getGenerateDoubleClickOnEnter() {
		return getAttributeAsBoolean("generateDoubleClickOnEnter");
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Space,  the cell will respond to a double click
	 * event.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param generateDoubleClickOnSpace generateDoubleClickOnSpace Default value is false
	 */
	public void setGenerateDoubleClickOnSpace(Boolean generateDoubleClickOnSpace) {
		setAttribute("generateDoubleClickOnSpace", generateDoubleClickOnSpace, true);
	}

	/**
	 * If true, when the user navigates to a cell using arrow keys and hits Space,  the cell will respond to a double click
	 * event.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getGenerateDoubleClickOnSpace() {
		return getAttributeAsBoolean("generateDoubleClickOnSpace");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary} is true, this attribute will be
	 * set to true on the record object representing the grid summary row.
	 *
	 * @param gridSummaryRecordProperty gridSummaryRecordProperty Default value is "isGridSummary"
	 */
	public void setGridSummaryRecordProperty(String gridSummaryRecordProperty) {
		setAttribute("gridSummaryRecordProperty", gridSummaryRecordProperty, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary} is true, this attribute will be
	 * set to true on the record object representing the grid summary row.
	 *
	 *
	 * @return String
	 */
	public String getGridSummaryRecordProperty() {
		return getAttributeAsString("gridSummaryRecordProperty");
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupByField grouped}, and  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, this attribute may be set to an
	 * array of groupBy field names for which group summaries should appear. <P> This is particularly useful for listGrids
	 * grouped by more than one field as it allows developers to display the group summary for a particular nested group
	 * without showing a summary for every level of the tree.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param groupByFieldSummaries groupByFieldSummaries Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowGroupSummary
	 */
	public void setGroupByFieldSummaries(String... groupByFieldSummaries) {
		setAttribute("groupByFieldSummaries", groupByFieldSummaries, true);
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupByField grouped}, and  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, this attribute may be set to an
	 * array of groupBy field names for which group summaries should appear. <P> This is particularly useful for listGrids
	 * grouped by more than one field as it allows developers to display the group summary for a particular nested group
	 * without showing a summary for every level of the tree.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary
	 */
	public String[] getGroupByFieldSummaries() {
		return getAttributeAsStringArray("groupByFieldSummaries");
	}

	/**
	 * Maximum number of records to which a groupBy can be applied. If there are more records, grouping will not be available
	 * via the default header context menu, and calls to  {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * ListGrid.groupBy} will be ignored.
	 *
	 * @param groupByMaxRecords groupByMaxRecords Default value is 1000
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setGroupByMaxRecords(int groupByMaxRecords) {
		setAttribute("groupByMaxRecords", groupByMaxRecords, true);
	}

	/**
	 * Maximum number of records to which a groupBy can be applied. If there are more records, grouping will not be available
	 * via the default header context menu, and calls to  {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * ListGrid.groupBy} will be ignored.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public int getGroupByMaxRecords() {
		return getAttributeAsInt("groupByMaxRecords");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy this.canGroupBy} is true, this string will be
	 * shown as the title for the menu item to toggle the group by setting for a field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Group by " + the field's summary title.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param groupByText groupByText Default value is "Group by \${title}"
	 */
	public void setGroupByText(String groupByText) {
		setAttribute("groupByText", groupByText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy this.canGroupBy} is true, this string will be
	 * shown as the title for the menu item to toggle the group by setting for a field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Group by " + the field's summary title.
	 *
	 *
	 * @return If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy this.canGroupBy} is true, this string will be
	 * shown as the title for the menu item to toggle the group by setting for a field.<br> Default implementation evaluates
	 * and returns the dynamic {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupByText groupByText} string.
	 */
	public String getGroupByText() {
		return getAttributeAsString("groupByText");
	}

	/**
	 * The URL of the base icon for the group icons in this treegrid.
	 *
	 * @param groupIcon groupIcon Default value is "[SKINIMG]/TreeGrid/opener.gif"
	 */
	public void setGroupIcon(String groupIcon) {
		setAttribute("groupIcon", groupIcon, true);
	}

	/**
	 * The URL of the base icon for the group icons in this treegrid.
	 *
	 *
	 * @return String
	 */
	public String getGroupIcon() {
		return getAttributeAsString("groupIcon");
	}

	/**
	 * Default width and height of group icons for this ListGrid.
	 *
	 * @param groupIconSize groupIconSize Default value is 16
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setGroupIconSize(int groupIconSize) {
		setAttribute("groupIconSize", groupIconSize, true);
	}

	/**
	 * Default width and height of group icons for this ListGrid.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public int getGroupIconSize() {
		return getAttributeAsInt("groupIconSize");
	}

	/**
	 * Default number of pixels by which to indent subgroups relative to parent group.
	 *
	 * @param groupIndentSize groupIndentSize Default value is 20
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getGroupNodeHTML
	 */
	public void setGroupIndentSize(int groupIndentSize) {
		setAttribute("groupIndentSize", groupIndentSize, true);
	}

	/**
	 * Default number of pixels by which to indent subgroups relative to parent group.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getGroupNodeHTML
	 */
	public int getGroupIndentSize() {
		return getAttributeAsInt("groupIndentSize");
	}

	/**
	 * Default number of pixels by which to indent all groups.
	 *
	 * @param groupLeadingIndent groupLeadingIndent Default value is 10
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getGroupNodeHTML
	 */
	public void setGroupLeadingIndent(int groupLeadingIndent) {
		setAttribute("groupLeadingIndent", groupLeadingIndent, true);
	}

	/**
	 * Default number of pixels by which to indent all groups.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getGroupNodeHTML
	 */
	public int getGroupLeadingIndent() {
		return getAttributeAsInt("groupLeadingIndent");
	}

	/**
	 * The CSS style that group rows will have
	 *
	 * @param groupNodeStyle groupNodeStyle Default value is "groupNode"
	 */
	public void setGroupNodeStyle(String groupNodeStyle) {
		setAttribute("groupNodeStyle", groupNodeStyle, true);
	}

	/**
	 * The CSS style that group rows will have
	 *
	 *
	 * @return String
	 */
	public String getGroupNodeStyle() {
		return getAttributeAsString("groupNodeStyle");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, this attribute will
	 * be set to true on each record object representing a group-level summary row.
	 *
	 * @param groupSummaryRecordProperty groupSummaryRecordProperty Default value is "isGroupSummary"
	 */
	public void setGroupSummaryRecordProperty(String groupSummaryRecordProperty) {
		setAttribute("groupSummaryRecordProperty", groupSummaryRecordProperty, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, this attribute will
	 * be set to true on each record object representing a group-level summary row.
	 *
	 *
	 * @return String
	 */
	public String getGroupSummaryRecordProperty() {
		return getAttributeAsString("groupSummaryRecordProperty");
	}

	/**
	 * {@link com.smartgwt.client.widgets.grid.ListGridRecord#getCustomStyle customStyle} for the group-level summary row
	 * displayed when  {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true.
	 *
	 * @param groupSummaryStyle groupSummaryStyle Default value is "gridSummaryCell"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setGroupSummaryStyle(String groupSummaryStyle) throws IllegalStateException {
		setAttribute("groupSummaryStyle", groupSummaryStyle, false);
	}

	/**
	 * {@link com.smartgwt.client.widgets.grid.ListGridRecord#getCustomStyle customStyle} for the group-level summary row
	 * displayed when  {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true.
	 *
	 *
	 * @return String
	 */
	public String getGroupSummaryStyle() {
		return getAttributeAsString("groupSummaryStyle");
	}

	/**
	 * When a list grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped}, each group shows under an auto
	 * generated header node. By default the title of the group will be shown, with a hanging indent in this node, and will
	 * span all columns in the grid. Setting this property causes the titles of auto-generated group nodes to appear as though
	 * they were values of the designated field instead of spanning all columns and record values in the designated
	 * groupTitleField will appear indented under  the group title in a manner similar to how a TreeGrid shows a Tree. <P> Note
	 * if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummaryInHeader showGroupSummaryInHeader} is true, the
	 * header nodes will not show a single spanning title value by default - instead they will show the summary values for each
	 * field. In this case, if groupTitleField is unset, a  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupTitleColumn groupTitleColumn} can be automatically generated to
	 * show the title for each group.
	 *
	 * @param groupTitleField groupTitleField Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setGroupTitleField(String groupTitleField) throws IllegalStateException {
		setAttribute("groupTitleField", groupTitleField, false);
	}

	/**
	 * When a list grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped}, each group shows under an auto
	 * generated header node. By default the title of the group will be shown, with a hanging indent in this node, and will
	 * span all columns in the grid. Setting this property causes the titles of auto-generated group nodes to appear as though
	 * they were values of the designated field instead of spanning all columns and record values in the designated
	 * groupTitleField will appear indented under  the group title in a manner similar to how a TreeGrid shows a Tree. <P> Note
	 * if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummaryInHeader showGroupSummaryInHeader} is true, the
	 * header nodes will not show a single spanning title value by default - instead they will show the summary values for each
	 * field. In this case, if groupTitleField is unset, a  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupTitleColumn groupTitleColumn} can be automatically generated to
	 * show the title for each group.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public String getGroupTitleField() {
		return getAttributeAsString("groupTitleField");
	}

	/**
	 * Event on a ListGrid header that triggers auto fitting to data and/or title. <P> Note that if sorting is enabled for the
	 * field and the headerAutoFitEvent is "click", both sorting and autofit occur on a click.
	 *
	 * @param headerAutoFitEvent headerAutoFitEvent Default value is "doubleClick"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderAutoFitEvent(AutoFitEvent headerAutoFitEvent) throws IllegalStateException {
		setAttribute("headerAutoFitEvent", headerAutoFitEvent.getValue(), false);
	}

	/**
	 * Event on a ListGrid header that triggers auto fitting to data and/or title. <P> Note that if sorting is enabled for the
	 * field and the headerAutoFitEvent is "click", both sorting and autofit occur on a click.
	 *
	 *
	 * @return AutoFitEvent
	 */
	public AutoFitEvent getHeaderAutoFitEvent() {
		return EnumUtil.getEnum(AutoFitEvent.values(), getAttribute("headerAutoFitEvent"));
	}

	/**
	 * BackgroundColor for the header toolbar. Typically this is set to match the color of the header buttons.
	 *
	 * @param headerBackgroundColor headerBackgroundColor Default value is "#CCCCCC"
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setHeaderBackgroundColor(String headerBackgroundColor) {
		setAttribute("headerBackgroundColor", headerBackgroundColor, true);
	}

	/**
	 * BackgroundColor for the header toolbar. Typically this is set to match the color of the header buttons.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getHeaderBackgroundColor() {
		return getAttributeAsString("headerBackgroundColor");
	}

	/**
	 * Set the CSS style used for the header as a whole.
	 *
	 * @param headerBarStyle headerBarStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setHeaderBarStyle(String headerBarStyle) throws IllegalStateException {
		setAttribute("headerBarStyle", headerBarStyle, false);
	}

	/**
	 * Set the CSS style used for the header as a whole.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getHeaderBarStyle() {
		return getAttributeAsString("headerBarStyle");
	}

	/**
	 * {@link com.smartgwt.client.widgets.Button#getBaseStyle baseStyle} to apply to the buttons in the header, and the sorter,
	 * for  this ListGrid. Note that, depending on the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderButtonConstructor Class} of the header buttons, you may also need to
	 * set {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderTitleStyle headerTitleStyle}.
	 *
	 * @param headerBaseStyle headerBaseStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setHeaderBaseStyle(String headerBaseStyle) throws IllegalStateException {
		setAttribute("headerBaseStyle", headerBaseStyle, false);
	}

	/**
	 * {@link com.smartgwt.client.widgets.Button#getBaseStyle baseStyle} to apply to the buttons in the header, and the sorter,
	 * for  this ListGrid. Note that, depending on the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderButtonConstructor Class} of the header buttons, you may also need to
	 * set {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderTitleStyle headerTitleStyle}.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getHeaderBaseStyle() {
		return getAttributeAsString("headerBaseStyle");
	}

	/**
	 * The height of this listGrid's header, in pixels.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Modify the height of a listGrid. To hide the header set height to zero.
	 *
	 * @param headerHeight new height for the header. Default value is 22
	 * @see com.smartgwt.client.docs.Sizing Sizing overview and related methods
	 */
	public void setHeaderHeight(int headerHeight) {
		setAttribute("headerHeight", headerHeight, true);
	}

	/**
	 * The height of this listGrid's header, in pixels.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.Sizing Sizing overview and related methods
	 */
	public int getHeaderHeight() {
		return getAttributeAsInt("headerHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the height of the  auto-generated <code>headerMenuButton</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param headerMenuButtonHeight headerMenuButtonHeight Default value is "100%"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderMenuButtonHeight(int headerMenuButtonHeight) throws IllegalStateException {
		setAttribute("headerMenuButtonHeight", headerMenuButtonHeight, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the height of the  auto-generated <code>headerMenuButton</code>
	 *
	 *
	 * @return int
	 */
	public int getHeaderMenuButtonHeight() {
		return getAttributeAsInt("headerMenuButtonHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the icon shown on the auto-generated <code>headerMenuButton</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param headerMenuButtonIcon headerMenuButtonIcon Default value is "[SKIN]/ListGrid/sort_descending.gif"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderMenuButtonIcon(String headerMenuButtonIcon) throws IllegalStateException {
		setAttribute("headerMenuButtonIcon", headerMenuButtonIcon, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the icon shown on the auto-generated <code>headerMenuButton</code>
	 *
	 *
	 * @return String
	 */
	public String getHeaderMenuButtonIcon() {
		return getAttributeAsString("headerMenuButtonIcon");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the height of the icon shown on the auto-generated <code>headerMenuButton</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param headerMenuButtonIconHeight headerMenuButtonIconHeight Default value is 7
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderMenuButtonIconHeight(int headerMenuButtonIconHeight) throws IllegalStateException {
		setAttribute("headerMenuButtonIconHeight", headerMenuButtonIconHeight, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the height of the icon shown on the auto-generated <code>headerMenuButton</code>
	 *
	 *
	 * @return int
	 */
	public int getHeaderMenuButtonIconHeight() {
		return getAttributeAsInt("headerMenuButtonIconHeight");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the width of the icon shown on the auto-generated <code>headerMenuButton</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param headerMenuButtonIconWidth headerMenuButtonIconWidth Default value is 7
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderMenuButtonIconWidth(int headerMenuButtonIconWidth) throws IllegalStateException {
		setAttribute("headerMenuButtonIconWidth", headerMenuButtonIconWidth, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the width of the icon shown on the auto-generated <code>headerMenuButton</code>
	 *
	 *
	 * @return int
	 */
	public int getHeaderMenuButtonIconWidth() {
		return getAttributeAsInt("headerMenuButtonIconWidth");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the width of the  auto-generated <code>headerMenuButton</code>
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param headerMenuButtonWidth headerMenuButtonWidth Default value is 16
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderMenuButtonWidth(int headerMenuButtonWidth) throws IllegalStateException {
		setAttribute("headerMenuButtonWidth", headerMenuButtonWidth, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderMenuButton showHeaderMenuButton} is true, this property
	 * governs the width of the  auto-generated <code>headerMenuButton</code>
	 *
	 *
	 * @return int
	 */
	public int getHeaderMenuButtonWidth() {
		return getAttributeAsInt("headerMenuButtonWidth");
	}

	/**
	 * Default height for a {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderSpans headerSpan} with no height
	 * specified. <P> If <code>headerSpanHeight</code> is not specified (the default), headerSpans will be 1/2 of {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderHeight headerHeight}.
	 *
	 * @param headerSpanHeight headerSpanHeight Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setHeaderSpanHeight(Integer headerSpanHeight) throws IllegalStateException {
		setAttribute("headerSpanHeight", headerSpanHeight, false);
	}

	/**
	 * Default height for a {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderSpans headerSpan} with no height
	 * specified. <P> If <code>headerSpanHeight</code> is not specified (the default), headerSpans will be 1/2 of {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderHeight headerHeight}.
	 *
	 *
	 * @return Integer
	 */
	public Integer getHeaderSpanHeight() {
		return getAttributeAsInt("headerSpanHeight");
	}

	/**
	 * {@link com.smartgwt.client.widgets.StretchImgButton#getTitleStyle titleStyle} to apply to the buttons in the header, and
	 * the sorter, for this ListGrid. Note that this will typically only have an effect if  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderButtonConstructor headerButtonConstructor} is set to {@link
	 * com.smartgwt.client.widgets.StretchImgButton} or a subclass  thereof.
	 *
	 * @param headerTitleStyle headerTitleStyle Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setHeaderTitleStyle(String headerTitleStyle) throws IllegalStateException {
		setAttribute("headerTitleStyle", headerTitleStyle, false);
	}

	/**
	 * {@link com.smartgwt.client.widgets.StretchImgButton#getTitleStyle titleStyle} to apply to the buttons in the header, and
	 * the sorter, for this ListGrid. Note that this will typically only have an effect if  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderButtonConstructor headerButtonConstructor} is set to {@link
	 * com.smartgwt.client.widgets.StretchImgButton} or a subclass  thereof.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public String getHeaderTitleStyle() {
		return getAttributeAsString("headerTitleStyle");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHoverComponents showHoverComponents()} is true, the builtin
	 * mode to use when automatically creating a hover component for rows in this grid. <P> A number of builtin modes are
	 * provided - see {@link com.smartgwt.client.types.HoverMode}.  You can also override {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCellHoverComponent getCellHoverComponent()} to provide a custom hover
	 * widget - in that case, this attribute is ignored.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param hoverMode hoverMode Default value is null
	 */
	public void setHoverMode(HoverMode hoverMode) {
		setAttribute("hoverMode", hoverMode.getValue(), true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHoverComponents showHoverComponents()} is true, the builtin
	 * mode to use when automatically creating a hover component for rows in this grid. <P> A number of builtin modes are
	 * provided - see {@link com.smartgwt.client.types.HoverMode}.  You can also override {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCellHoverComponent getCellHoverComponent()} to provide a custom hover
	 * widget - in that case, this attribute is ignored.
	 *
	 *
	 * @return HoverMode
	 */
	public HoverMode getHoverMode() {
		return EnumUtil.getEnum(HoverMode.values(), getAttribute("hoverMode"));
	}

	/**
	 * Style to apply to hovers shown over this grid.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param hoverStyle hoverStyle Default value is "gridHover"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowHover
	 */
	@Override
	public void setHoverStyle(String hoverStyle) {
		setAttribute("hoverStyle", hoverStyle, true);
	}

	/**
	 * Style to apply to hovers shown over this grid.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowHover
	 */
	@Override
	public String getHoverStyle() {
		return getAttributeAsString("hoverStyle");
	}

	/**
	 * Default size of thumbnails shown for fieldTypes image and imageFile.  Overrideable on a per-field basis via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageSize imageSize} or {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageWidth imageWidth}/{@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageHeight imageHeight}
	 *
	 * @param imageSize imageSize Default value is 16
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setImageSize(int imageSize) {
		setAttribute("imageSize", imageSize, true);
	}

	/**
	 * Default size of thumbnails shown for fieldTypes image and imageFile.  Overrideable on a per-field basis via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageSize imageSize} or {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageWidth imageWidth}/{@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getImageHeight imageHeight}
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getImageSize() {
		return getAttributeAsInt("imageSize");
	}

	/**
	 * Property name on a record that will be checked to determine whether a record should be included when calculating totals
	 * for the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary grid summary}.
	 *
	 * @param includeInSummaryProperty includeInSummaryProperty Default value is "includeInSummary"
	 */
	public void setIncludeInSummaryProperty(String includeInSummaryProperty) {
		setAttribute("includeInSummaryProperty", includeInSummaryProperty, true);
	}

	/**
	 * Property name on a record that will be checked to determine whether a record should be included when calculating totals
	 * for the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary grid summary}.
	 *
	 *
	 * @return String
	 */
	public String getIncludeInSummaryProperty() {
		return getAttributeAsString("includeInSummaryProperty");
	}

	/**
	 * If true, if the user clicks on the scroll buttons at the end of the track or clicks once on the scroll track, there will
	 * be an instant redraw of the grid content so that the user doesn't see any blank space.  For drag scrolling or other
	 * types of scrolling, the +link{scrollRedrawDelay applies}.
	 *
	 * @param instantScrollTrackRedraw instantScrollTrackRedraw Default value is true
	 */
	public void setInstantScrollTrackRedraw(Boolean instantScrollTrackRedraw) {
		setAttribute("instantScrollTrackRedraw", instantScrollTrackRedraw, true);
	}

	/**
	 * If true, if the user clicks on the scroll buttons at the end of the track or clicks once on the scroll track, there will
	 * be an instant redraw of the grid content so that the user doesn't see any blank space.  For drag scrolling or other
	 * types of scrolling, the +link{scrollRedrawDelay applies}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getInstantScrollTrackRedraw() {
		return getAttributeAsBoolean("instantScrollTrackRedraw");
	}

	/**
	 * Value to display to the user if showing summary values (through {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} or {@link
	 * com.smartgwt.client.types.ListGridFieldType listGridFieldType:"summary"}), and the summary function returns
	 * <code>"null"</code> (implying it was unable to calculate a valid summary value).
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param invalidSummaryValue invalidSummaryValue Default value is "&amp;nbsp;"
	 */
	public void setInvalidSummaryValue(String invalidSummaryValue) {
		setAttribute("invalidSummaryValue", invalidSummaryValue, true);
	}

	/**
	 * Value to display to the user if showing summary values (through {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} or {@link
	 * com.smartgwt.client.types.ListGridFieldType listGridFieldType:"summary"}), and the summary function returns
	 * <code>"null"</code> (implying it was unable to calculate a valid summary value).
	 *
	 *
	 * @return String
	 */
	public String getInvalidSummaryValue() {
		return getAttributeAsString("invalidSummaryValue");
	}

	/**
	 * True if this listgrid is grouped, false otherwise
	 *
	 * <b>Note :</b> This method should be called only after the widget has been rendered.
	 *
	 * @return Boolean
	 * @throws IllegalStateException if widget has not yet been rendered.
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public Boolean getIsGrouped() throws IllegalStateException {
		errorIfNotCreated("isGrouped");
		return getAttributeAsBoolean("isGrouped");
	}

	/**
	 * If <code>record[this.isSeparatorProperty]</code> is set for some record, the  record will be displayed as a simple
	 * separator row.
	 *
	 * @param isSeparatorProperty isSeparatorProperty Default value is "isSeparator"
	 */
	public void setIsSeparatorProperty(String isSeparatorProperty) {
		setAttribute("isSeparatorProperty", isSeparatorProperty, true);
	}

	/**
	 * If <code>record[this.isSeparatorProperty]</code> is set for some record, the  record will be displayed as a simple
	 * separator row.
	 *
	 *
	 * @return String
	 */
	public String getIsSeparatorProperty() {
		return getAttributeAsString("isSeparatorProperty");
	}

	/**
	 * Whether to leave a gap for the vertical scrollbar, even when it's not present. <P> Note that if leaveScrollbarGap is
	 * false and vertical scrolling is introduced, fields will be resized to fit the smaller body area if possible, in order to
	 * avoid horizontal scrolling also being required.
	 *
	 * @param leaveScrollbarGap leaveScrollbarGap Default value is true
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public void setLeaveScrollbarGap(Boolean leaveScrollbarGap) {
		setAttribute("leaveScrollbarGap", leaveScrollbarGap, true);
	}

	/**
	 * Whether to leave a gap for the vertical scrollbar, even when it's not present. <P> Note that if leaveScrollbarGap is
	 * false and vertical scrolling is introduced, fields will be resized to fit the smaller body area if possible, in order to
	 * avoid horizontal scrolling also being required.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public Boolean getLeaveScrollbarGap() {
		return getAttributeAsBoolean("leaveScrollbarGap");
	}

	/**
	 * Property name on a record that will hold the link text for that record. <P> This property is configurable to avoid
	 * possible collision with data values in the record. <P> Use {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getLinkTextProperty linkTextProperty} if you have more than one link
	 * field and
	 *
	 * @param linkTextProperty linkTextProperty Default value is "linkText"
	 * @see com.smartgwt.client.types.ListGridFieldType
	 * @see com.smartgwt.client.types.FieldType
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setLinkText
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setLinkTextProperty
	 */
	public void setLinkTextProperty(String linkTextProperty) {
		setAttribute("linkTextProperty", linkTextProperty, true);
	}

	/**
	 * Property name on a record that will hold the link text for that record. <P> This property is configurable to avoid
	 * possible collision with data values in the record. <P> Use {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getLinkTextProperty linkTextProperty} if you have more than one link
	 * field and
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.types.ListGridFieldType
	 * @see com.smartgwt.client.types.FieldType
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getLinkText
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getLinkTextProperty
	 */
	public String getLinkTextProperty() {
		return getAttributeAsString("linkTextProperty");
	}

	/**
	 * If the user is editing the last record in this listGrid, and attempts to navigate  beyond the last row either by tabbing
	 * off the last editable field, or using the down arrow key, this property determines what action to take:<ul> <li>"next":
	 * start editing a new record at the end of the list. <li>"done": hide the editor <li>"stop": leave focus in the cell being
	 * edited <li>"none": no action </ul>
	 *
	 * @param listEndEditAction listEndEditAction Default value is null
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_new_row" target="examples">Enter new rows Example</a>
	 */
	public void setListEndEditAction(RowEndEditAction listEndEditAction) {
		setAttribute("listEndEditAction", listEndEditAction.getValue(), true);
	}

	/**
	 * If the user is editing the last record in this listGrid, and attempts to navigate  beyond the last row either by tabbing
	 * off the last editable field, or using the down arrow key, this property determines what action to take:<ul> <li>"next":
	 * start editing a new record at the end of the list. <li>"done": hide the editor <li>"stop": leave focus in the cell being
	 * edited <li>"none": no action </ul>
	 *
	 *
	 * @return RowEndEditAction
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_new_row" target="examples">Enter new rows Example</a>
	 */
	public RowEndEditAction getListEndEditAction() {
		return EnumUtil.getEnum(RowEndEditAction.values(), getAttribute("listEndEditAction"));
	}

	/**
	 * The string to display in the body of a listGrid while data is being loaded. Use <code>"\${loadingImage}"</code> to
	 * include {@link com.smartgwt.client.widgets.Canvas#loadingImageSrc a loading image}.
	 *
	 * @param loadingDataMessage loadingDataMessage Default value is "\${loadingImage}&amp;nbsp;Loading data..."
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setLoadingDataMessageStyle
	 */
	public void setLoadingDataMessage(String loadingDataMessage) {
		setAttribute("loadingDataMessage", loadingDataMessage, true);
	}

	/**
	 * The string to display in the body of a listGrid while data is being loaded. Use <code>"\${loadingImage}"</code> to
	 * include {@link com.smartgwt.client.widgets.Canvas#loadingImageSrc a loading image}.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getLoadingDataMessageStyle
	 */
	public String getLoadingDataMessage() {
		return getAttributeAsString("loadingDataMessage");
	}

	/**
	 * The CSS style name applied to the loadingDataMessage string if displayed.
	 *
	 * @param loadingDataMessageStyle loadingDataMessageStyle Default value is "loadingDataMessage"
	 */
	public void setLoadingDataMessageStyle(String loadingDataMessageStyle) {
		setAttribute("loadingDataMessageStyle", loadingDataMessageStyle, true);
	}

	/**
	 * The CSS style name applied to the loadingDataMessage string if displayed.
	 *
	 *
	 * @return String
	 */
	public String getLoadingDataMessageStyle() {
		return getAttributeAsString("loadingDataMessageStyle");
	}

	/**
	 * If you have a databound listGrid and you scroll out of the currently loaded dataset, by default you will see blank rows
	 * until the server returns the data for those rows.  The loadingMessage attribute allows you to specify arbitrary html
	 * that will be shown in each such "blank" record while the data for that record is loading.
	 *
	 * @param loadingMessage loadingMessage Default value is "&amp;nbsp;"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setLoadingMessage(String loadingMessage) throws IllegalStateException {
		setAttribute("loadingMessage", loadingMessage, false);
	}

	/**
	 * If you have a databound listGrid and you scroll out of the currently loaded dataset, by default you will see blank rows
	 * until the server returns the data for those rows.  The loadingMessage attribute allows you to specify arbitrary html
	 * that will be shown in each such "blank" record while the data for that record is loading.
	 *
	 *
	 * @return String
	 */
	public String getLoadingMessage() {
		return getAttributeAsString("loadingMessage");
	}

	/**
	 * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()} for a cell in
	 * this grid, how should the column be identified? <br> Note that getLocator() will actually store all available
	 * information about the column in the generated string -- this attribute effects how a stored string will be parsed only.
	 * <P> Valid options area: <ul> <li><code>"fieldName"</code> Attempt to identify by fieldName.</li>
	 * <li><code>"index"</code> Attempt to identify by colNum (index in the fields array).</li> </ul> If unset, default
	 * behavior is to identify by fieldName (if available), otherwise by index.
	 *
	 * @param locateColumnsBy locateColumnsBy Default value is null
	 */
	public void setLocateColumnsBy(String locateColumnsBy) {
		setAttribute("locateColumnsBy", locateColumnsBy, true);
	}

	/**
	 * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()} for a cell in
	 * this grid, how should the column be identified? <br> Note that getLocator() will actually store all available
	 * information about the column in the generated string -- this attribute effects how a stored string will be parsed only.
	 * <P> Valid options area: <ul> <li><code>"fieldName"</code> Attempt to identify by fieldName.</li>
	 * <li><code>"index"</code> Attempt to identify by colNum (index in the fields array).</li> </ul> If unset, default
	 * behavior is to identify by fieldName (if available), otherwise by index.
	 *
	 *
	 * @return String
	 */
	public String getLocateColumnsBy() {
		return getAttributeAsString("locateColumnsBy");
	}

	/**
	 * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()} for a cell in
	 * this grid, how should the row be identified? <br> Note that getLocator() will actually store all available information
	 * about the row in the generated string -- this attribute effects how a stored string will be parsed only. <P> Valid
	 * options area: <ul> <li><code>"primaryKey"</code> Only applies to databound grids: If the cell in question has   a
	 * primary key cell value, use it to identify cells in autoTest locator strings.</li> <li><code>"titleField"</code> If the
	 * cell in question has a value for the   {@link com.smartgwt.client.widgets.grid.ListGrid#getTitleField titleField}, use
	 * it to identify cells in autoTest  locator strings</li> <li><code>"targetCellValue"</code> Identify rows by storing the
	 * cell value for the target  row / field in autoTest locator strings</li> <li><code>"index"</code>The rowNum will be used
	 * to identify the row.</li> </ul> If unset, default behavior is to identify by primary key (if available), otherwise by
	 * titleField (if available), otherwise by cell value (if available), and lastly by index.
	 *
	 * @param locateRowsBy locateRowsBy Default value is null
	 */
	public void setLocateRowsBy(String locateRowsBy) {
		setAttribute("locateRowsBy", locateRowsBy, true);
	}

	/**
	 * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()} for a cell in
	 * this grid, how should the row be identified? <br> Note that getLocator() will actually store all available information
	 * about the row in the generated string -- this attribute effects how a stored string will be parsed only. <P> Valid
	 * options area: <ul> <li><code>"primaryKey"</code> Only applies to databound grids: If the cell in question has   a
	 * primary key cell value, use it to identify cells in autoTest locator strings.</li> <li><code>"titleField"</code> If the
	 * cell in question has a value for the   {@link com.smartgwt.client.widgets.grid.ListGrid#getTitleField titleField}, use
	 * it to identify cells in autoTest  locator strings</li> <li><code>"targetCellValue"</code> Identify rows by storing the
	 * cell value for the target  row / field in autoTest locator strings</li> <li><code>"index"</code>The rowNum will be used
	 * to identify the row.</li> </ul> If unset, default behavior is to identify by primary key (if available), otherwise by
	 * titleField (if available), otherwise by cell value (if available), and lastly by index.
	 *
	 *
	 * @return String
	 */
	public String getLocateRowsBy() {
		return getAttributeAsString("locateRowsBy");
	}

	/**
	 * When the length of the field specified by {@link com.smartgwt.client.data.DataSourceField#getLength length} exceeds this
	 * value, the ListGrid shows an edit field of type {@link com.smartgwt.client.widgets.grid.ListGrid#getLongTextEditorType
	 * longTextEditorType} rather than the standard text field when the field enters inline edit mode.
	 *
	 * @param longTextEditorThreshold longTextEditorThreshold Default value is 255
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setLongTextEditorThreshold(int longTextEditorThreshold) {
		setAttribute("longTextEditorThreshold", longTextEditorThreshold, true);
	}

	/**
	 * When the length of the field specified by {@link com.smartgwt.client.data.DataSourceField#getLength length} exceeds this
	 * value, the ListGrid shows an edit field of type {@link com.smartgwt.client.widgets.grid.ListGrid#getLongTextEditorType
	 * longTextEditorType} rather than the standard text field when the field enters inline edit mode.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public int getLongTextEditorThreshold() {
		return getAttributeAsInt("longTextEditorThreshold");
	}

	/**
	 * When the length of the field specified by {@link com.smartgwt.client.data.DataSourceField#getLength length} exceeds 
	 * <code>this.longTextEditorThreshold</code> show an edit field of this type rather than the standard text field when the
	 * field enters inline edit mode.
	 *
	 * @param longTextEditorType longTextEditorType Default value is "PopUpTextAreaItem"
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setLongTextEditorType(String longTextEditorType) {
		setAttribute("longTextEditorType", longTextEditorType, true);
	}

	/**
	 * When the length of the field specified by {@link com.smartgwt.client.data.DataSourceField#getLength length} exceeds 
	 * <code>this.longTextEditorThreshold</code> show an edit field of this type rather than the standard text field when the
	 * field enters inline edit mode.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public String getLongTextEditorType() {
		return getAttributeAsString("longTextEditorType");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanExpandMultipleRecords canExpandMultipleRecords} are both true, this
	 * property dictates the number of records which can be expanded simultaneously.  If the expanded record count hits the
	 * value of this property, further attempts to expand records will result in a popup warning (see {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getMaxExpandedRecordsPrompt maxExpandedRecordsPrompt}) and expansion will be
	 * cancelled. <P> The default value is null, meaning there is no limit on the number of expanded records.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param maxExpandedRecords maxExpandedRecords Default value is null
	 */
	public void setMaxExpandedRecords(Integer maxExpandedRecords) {
		setAttribute("maxExpandedRecords", maxExpandedRecords, true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanExpandMultipleRecords canExpandMultipleRecords} are both true, this
	 * property dictates the number of records which can be expanded simultaneously.  If the expanded record count hits the
	 * value of this property, further attempts to expand records will result in a popup warning (see {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getMaxExpandedRecordsPrompt maxExpandedRecordsPrompt}) and expansion will be
	 * cancelled. <P> The default value is null, meaning there is no limit on the number of expanded records.
	 *
	 *
	 * @return Integer
	 */
	public Integer getMaxExpandedRecords() {
		return getAttributeAsInt("maxExpandedRecords");
	}

	/**
	 * This is a dynamic string - text within <code>\${...}</code> will be evaluated as JS code when the message is displayed.
	 * Note that the local variable <code>count</code> will be available and set to this.maxExpandedRecords. The string will be
	 * executed in the scope of the ListGrid so <code>this</code> may also be used to determine other information about this
	 * grid. <P> Default value returns <P> <code> <i>This grid is limited to <code>[{@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getMaxExpandedRecords maxExpandedRecords}]</code> simultaneously  expanded
	 * records.  Please collapse some expanded records and retry.</i> </code>
	 *
	 * @param maxExpandedRecordsPrompt maxExpandedRecordsPrompt Default value is "This grid is limited to \${count} simultaneously expanded records.  Please collapse some expanded records and retry."
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setMaxExpandedRecordsPrompt(String maxExpandedRecordsPrompt) throws IllegalStateException {
		setAttribute("maxExpandedRecordsPrompt", maxExpandedRecordsPrompt, false);
	}

	/**
	 * This is a dynamic string - text within <code>\${...}</code> will be evaluated as JS code when the message is displayed.
	 * Note that the local variable <code>count</code> will be available and set to this.maxExpandedRecords. The string will be
	 * executed in the scope of the ListGrid so <code>this</code> may also be used to determine other information about this
	 * grid. <P> Default value returns <P> <code> <i>This grid is limited to <code>[{@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getMaxExpandedRecords maxExpandedRecords}]</code> simultaneously  expanded
	 * records.  Please collapse some expanded records and retry.</i> </code>
	 *
	 *
	 * @return String
	 */
	public String getMaxExpandedRecordsPrompt() {
		return getAttributeAsString("maxExpandedRecordsPrompt");
	}

	/**
	 * Minimum size, in pixels, for ListGrid headers.
	 *
	 * @param minFieldWidth minFieldWidth Default value is 15
	 */
	public void setMinFieldWidth(int minFieldWidth) {
		setAttribute("minFieldWidth", minFieldWidth, true);
	}

	/**
	 * Minimum size, in pixels, for ListGrid headers.
	 *
	 *
	 * @return int
	 */
	public int getMinFieldWidth() {
		return getAttributeAsInt("minFieldWidth");
	}

	/**
	 * If this property is true, any mouse click outside of the open cell editors      will end editing mode, hiding the cell
	 * editors and saving any changes to those      cell values.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param modalEditing modalEditing Default value is null
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_modal" target="examples">Modal editing Example</a>
	 */
	public void setModalEditing(Boolean modalEditing) {
		setAttribute("modalEditing", modalEditing, true);
	}

	/**
	 * If this property is true, any mouse click outside of the open cell editors      will end editing mode, hiding the cell
	 * editors and saving any changes to those      cell values.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_editing_modal" target="examples">Modal editing Example</a>
	 */
	public Boolean getModalEditing() {
		return getAttributeAsBoolean("modalEditing");
	}

	/**
	 * If true, validation will not occur as a result of cell editing for this grid.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param neverValidate neverValidate Default value is null
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public void setNeverValidate(Boolean neverValidate) {
		setAttribute("neverValidate", neverValidate, true);
	}

	/**
	 * If true, validation will not occur as a result of cell editing for this grid.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public Boolean getNeverValidate() {
		return getAttributeAsBoolean("neverValidate");
	}

	/**
	 * "Normal" baseStyle for this listGrid. Only applies if {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} is  set to null. <P> If <code>baseStyle</code> is unset, this property will be used as a base cell style if
	 * the grid is showing fixed height rows, and the specified cellHeight matches {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalCellHeight normalCellHeight} (and in Internet Explorer, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFastCellUpdates fastCellUpdates} is false). Otherwise {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} will be used. <P> Having separate styles
	 * defined for fixed vs. variable height rows allows the developer to specify css which is designed to render at a specific
	 * height (typically using background images, which won't scale), without breaking support for styling rows of variable
	 * height.
	 *
	 * @param normalBaseStyle normalBaseStyle Default value is "cell"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 */
	public void setNormalBaseStyle(String normalBaseStyle) throws IllegalStateException {
		setAttribute("normalBaseStyle", normalBaseStyle, false);
	}

	/**
	 * "Normal" baseStyle for this listGrid. Only applies if {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} is  set to null. <P> If <code>baseStyle</code> is unset, this property will be used as a base cell style if
	 * the grid is showing fixed height rows, and the specified cellHeight matches {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalCellHeight normalCellHeight} (and in Internet Explorer, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFastCellUpdates fastCellUpdates} is false). Otherwise {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} will be used. <P> Having separate styles
	 * defined for fixed vs. variable height rows allows the developer to specify css which is designed to render at a specific
	 * height (typically using background images, which won't scale), without breaking support for styling rows of variable
	 * height.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 */
	public String getNormalBaseStyle() {
		return getAttributeAsString("normalBaseStyle");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle baseStyle} is unset, base style will be derived from 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} if this grid has fixed row heights
	 * and  the specified {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight} matches this value.
	 * Otherwise {@link com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} will be used.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param normalCellHeight normalCellHeight Default value is 20
	 */
	public void setNormalCellHeight(int normalCellHeight) {
		setAttribute("normalCellHeight", normalCellHeight, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle baseStyle} is unset, base style will be derived from 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} if this grid has fixed row heights
	 * and  the specified {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight cellHeight} matches this value.
	 * Otherwise {@link com.smartgwt.client.widgets.grid.ListGrid#getTallBaseStyle tallBaseStyle} will be used.
	 *
	 *
	 * @return int
	 */
	public int getNormalCellHeight() {
		return getAttributeAsInt("normalCellHeight");
	}

	/**
	 * Default alias to use for groups with no value
	 *
	 * @param nullGroupTitle nullGroupTitle Default value is '-none-'
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setNullGroupTitle(String nullGroupTitle) {
		setAttribute("nullGroupTitle", nullGroupTitle, true);
	}

	/**
	 * Default alias to use for groups with no value
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public String getNullGroupTitle() {
		return getAttributeAsString("nullGroupTitle");
	}

	/**
	 * The CSS style name applied to the {@link com.smartgwt.client.widgets.grid.ListGrid#getOfflineMessage offlineMessage} if
	 * displayed.
	 *
	 * @param offlineMessageStyle offlineMessageStyle Default value is "offlineMessage"
	 */
	public void setOfflineMessageStyle(String offlineMessageStyle) {
		setAttribute("offlineMessageStyle", offlineMessageStyle, true);
	}

	/**
	 * The CSS style name applied to the {@link com.smartgwt.client.widgets.grid.ListGrid#getOfflineMessage offlineMessage} if
	 * displayed.
	 *
	 *
	 * @return String
	 */
	public String getOfflineMessageStyle() {
		return getAttributeAsString("offlineMessageStyle");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentPoolingMode recordComponentPoolingMode} is
	 * "recycle" and you have components of  different types in different columns, set this property to true to ensure that 
	 * components intended for one column are not recycled for use in another column that  should have a different component.
	 * <P> If no components applicable to a particular column are available in the pool, the system calls {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent createRecordComponent}.
	 *
	 * @param poolComponentsPerColumn poolComponentsPerColumn Default value is null
	 */
	public void setPoolComponentsPerColumn(Boolean poolComponentsPerColumn) {
		setAttribute("poolComponentsPerColumn", poolComponentsPerColumn, true);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentPoolingMode recordComponentPoolingMode} is
	 * "recycle" and you have components of  different types in different columns, set this property to true to ensure that 
	 * components intended for one column are not recycled for use in another column that  should have a different component.
	 * <P> If no components applicable to a particular column are available in the pool, the system calls {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent createRecordComponent}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getPoolComponentsPerColumn() {
		return getAttributeAsBoolean("poolComponentsPerColumn");
	}

	/**
	 * Whether cell contents should wrap during printing.  Equivalent to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFit autoFit}, but specific to printed output.
	 *
	 * @param printAutoFit printAutoFit Default value is true
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public void setPrintAutoFit(Boolean printAutoFit) {
		setAttribute("printAutoFit", printAutoFit, true);
	}

	/**
	 * Whether cell contents should wrap during printing.  Equivalent to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFit autoFit}, but specific to printed output.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public Boolean getPrintAutoFit() {
		return getAttributeAsBoolean("printAutoFit");
	}

	/**
	 * Style for non-header cells in printed output.  Defaults to {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} if null.
	 *
	 * @param printBaseStyle printBaseStyle Default value is null
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public void setPrintBaseStyle(String printBaseStyle) {
		setAttribute("printBaseStyle", printBaseStyle, true);
	}

	/**
	 * Style for non-header cells in printed output.  Defaults to {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} if null.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public String getPrintBaseStyle() {
		return getAttributeAsString("printBaseStyle");
	}

	/**
	 * Style for header cells in printed output.  Defaults to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderBaseStyle headerBaseStyle} if null.
	 *
	 * @param printHeaderStyle printHeaderStyle Default value is "printHeader"
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public void setPrintHeaderStyle(String printHeaderStyle) {
		setAttribute("printHeaderStyle", printHeaderStyle, true);
	}

	/**
	 * Style for header cells in printed output.  Defaults to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getHeaderBaseStyle headerBaseStyle} if null.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public String getPrintHeaderStyle() {
		return getAttributeAsString("printHeaderStyle");
	}

	/**
	 * Advanced property - when generating printHTML for a large ListGrid, rows are printed in batches in order to avoid
	 * triggering a native "script is running slowly" browser dialog. <P> For grids with exceptional numbers of columns or
	 * complex formatting logic, this number might need to be adjusted downward.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param printMaxRows printMaxRows Default value is 100
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public void setPrintMaxRows(int printMaxRows) {
		setAttribute("printMaxRows", printMaxRows, true);
	}

	/**
	 * Advanced property - when generating printHTML for a large ListGrid, rows are printed in batches in order to avoid
	 * triggering a native "script is running slowly" browser dialog. <P> For grids with exceptional numbers of columns or
	 * complex formatting logic, this number might need to be adjusted downward.
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public int getPrintMaxRows() {
		return getAttributeAsInt("printMaxRows");
	}

	/**
	 * Whether cell contents should wrap during printing.  Equivalent to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getWrapCells wrapCells}, but specific to printed output.
	 *
	 * @param printWrapCells printWrapCells Default value is true
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public void setPrintWrapCells(Boolean printWrapCells) {
		setAttribute("printWrapCells", printWrapCells, true);
	}

	/**
	 * Whether cell contents should wrap during printing.  Equivalent to {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getWrapCells wrapCells}, but specific to printed output.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Printing Printing overview and related methods
	 */
	public Boolean getPrintWrapCells() {
		return getAttributeAsBoolean("printWrapCells");
	}

	/**
	 * Alternative to {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio}, to be used when the
	 * user is rapidly changing the grids viewport (for example drag scrolling through the grid). If unspecified {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} will be used in all cases
	 *
	 * @param quickDrawAheadRatio quickDrawAheadRatio Default value is 1.0
	 */
	public void setQuickDrawAheadRatio(float quickDrawAheadRatio) {
		setAttribute("quickDrawAheadRatio", quickDrawAheadRatio, true);
	}

	/**
	 * Alternative to {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio}, to be used when the
	 * user is rapidly changing the grids viewport (for example drag scrolling through the grid). If unspecified {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} will be used in all cases
	 *
	 *
	 * @return float
	 */
	public float getQuickDrawAheadRatio() {
		return getAttributeAsFloat("quickDrawAheadRatio");
	}

	/**
	 * This attribute allows custom base styles to be displayed on a per-record basis. To specify a custom base-style for some
	 * record set  <code>record[listGrid.recordBaseStyleProperty]</code> to the desired base style name -  for example if
	 * <code>recordBaseStyleProperty</code> is <code>"_baseStyle"</code>, set <code>record._baseStyle</code> to the custom base
	 * style name.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordBaseStyleProperty recordBaseStyleProperty Default value is "_baseStyle"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setRecordBaseStyleProperty(String recordBaseStyleProperty) {
		setAttribute("recordBaseStyleProperty", recordBaseStyleProperty, true);
	}

	/**
	 * This attribute allows custom base styles to be displayed on a per-record basis. To specify a custom base-style for some
	 * record set  <code>record[listGrid.recordBaseStyleProperty]</code> to the desired base style name -  for example if
	 * <code>recordBaseStyleProperty</code> is <code>"_baseStyle"</code>, set <code>record._baseStyle</code> to the custom base
	 * style name.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getRecordBaseStyleProperty() {
		return getAttributeAsString("recordBaseStyleProperty");
	}

	/**
	 * If set to false on a record, selection of that record is disallowed.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordCanSelectProperty recordCanSelectProperty Default value is "canSelect"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setRecordCanSelectProperty(String recordCanSelectProperty) throws IllegalStateException {
		setAttribute("recordCanSelectProperty", recordCanSelectProperty, false);
	}

	/**
	 * If set to false on a record, selection of that record is disallowed.
	 *
	 *
	 * @return String
	 */
	public String getRecordCanSelectProperty() {
		return getAttributeAsString("recordCanSelectProperty");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents showRecordComponents} is true, this
	 * attribute may be used to specify a standard height for record components. If specified every row in the grid will be
	 * sized tall enough to accommodate a recordComponent of this size. <P> Note that if this property is unset, the grid will
	 * not be able to know row heights in advance, and {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen freezing
	 * of columns} is not currently supported in this case.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for the {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentHeight recordComponentHeight}
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordComponentHeight recordComponent height. Default value is null
	 */
	public void setRecordComponentHeight(Integer recordComponentHeight) {
		setAttribute("recordComponentHeight", recordComponentHeight, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents showRecordComponents} is true, this
	 * attribute may be used to specify a standard height for record components. If specified every row in the grid will be
	 * sized tall enough to accommodate a recordComponent of this size. <P> Note that if this property is unset, the grid will
	 * not be able to know row heights in advance, and {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen freezing
	 * of columns} is not currently supported in this case.
	 *
	 *
	 * @return Integer
	 */
	public Integer getRecordComponentHeight() {
		return getAttributeAsInt("recordComponentHeight");
	}

	/**
	 * The method of {@link com.smartgwt.client.types.RecordComponentPoolingMode component-pooling} to employ for  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents recordComponents}. <P> The default mode is "viewport",
	 * which means that recordComponents are destroyed as soon  their record leaves the viewport.   <P> For the most efficient
	 * implementation, switch to "recycle" mode, which pools components  when records leave the viewport and re-uses them in
	 * other records.  In this mode, you  should implement {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent updateRecordComponent()} to  apply any changes to make
	 * reused components applicable to the new record they appear in,  if necessary.  If you have components of different types
	 * in different columns and still want to take advantage of component recycling, you can  set {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getPoolComponentsPerColumn poolComponentsPerColumn} to ensure that components
	 * intended for one  column are not recycled for use in another column that should have a different component. <P> Note
	 * that, if different records have distinctly different components embedded  in them, or multiple columns in each record
	 * embed different components, you should  leave the recordComponentPoolingMode at "viewport" if your dataset is very large
	 * or  use "data" otherwise.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordComponentPoolingMode recordComponentPoolingMode Default value is "viewport"
	 */
	public void setRecordComponentPoolingMode(RecordComponentPoolingMode recordComponentPoolingMode) {
		setAttribute("recordComponentPoolingMode", recordComponentPoolingMode.getValue(), true);
	}

	/**
	 * The method of {@link com.smartgwt.client.types.RecordComponentPoolingMode component-pooling} to employ for  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents recordComponents}. <P> The default mode is "viewport",
	 * which means that recordComponents are destroyed as soon  their record leaves the viewport.   <P> For the most efficient
	 * implementation, switch to "recycle" mode, which pools components  when records leave the viewport and re-uses them in
	 * other records.  In this mode, you  should implement {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent updateRecordComponent()} to  apply any changes to make
	 * reused components applicable to the new record they appear in,  if necessary.  If you have components of different types
	 * in different columns and still want to take advantage of component recycling, you can  set {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getPoolComponentsPerColumn poolComponentsPerColumn} to ensure that components
	 * intended for one  column are not recycled for use in another column that should have a different component. <P> Note
	 * that, if different records have distinctly different components embedded  in them, or multiple columns in each record
	 * embed different components, you should  leave the recordComponentPoolingMode at "viewport" if your dataset is very large
	 * or  use "data" otherwise.
	 *
	 *
	 * @return RecordComponentPoolingMode
	 */
	public RecordComponentPoolingMode getRecordComponentPoolingMode() {
		return EnumUtil.getEnum(RecordComponentPoolingMode.values(), getAttribute("recordComponentPoolingMode"));
	}

	/**
	 * if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents showRecordComponents} is true, how should
	 * the component appear within the cell. Valid options are  <ul><li><code>"within"</code>: the component will be rendered
	 * inside the record / cell.  {@link com.smartgwt.client.widgets.Canvas#getSnapTo snapTo} may be set to specify where the
	 * component should render within  the row or cell, and {@link com.smartgwt.client.widgets.Canvas#getSnapOffsetTop
	 * snapOffsetTop} / {@link com.smartgwt.client.widgets.Canvas#getSnapOffsetLeft snapOffsetLeft} may  be set to indent
	 * recordComponents within their parent cells.  Note that if unset, the component will show up at the top/left edge  for
	 * components embedded within an entire row, or for per-cell components, cell  align and valign will be respected.  Note
	 * also that, when rendering components "within"  cells, specified component heights will be respected and will change the
	 * height of the   row.  However, if you want components to completely fill a cell at it's default height,   set height:
	 * "100%" or rows will render at the default height of the component. </li>  <li><code>"expand"</code>: the component will
	 * be written into the cell below the  normal cell content, causing the cell to expand vertically to accommodate it.
	 * <li><code>null</code>: If this attribute is unset, we will default to showing  recordComponents with position
	 * <code>"within"</code> if   {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponentsByCell
	 * showRecordComponentsByCell} is true, otherwise using <code>"expand"</code>  logic. </ul>
	 *
	 * @param recordComponentPosition recordComponentPosition Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowRecordComponents
	 */
	public void setRecordComponentPosition(EmbeddedPosition recordComponentPosition) {
		setAttribute("recordComponentPosition", recordComponentPosition.getValue(), true);
	}

	/**
	 * if {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents showRecordComponents} is true, how should
	 * the component appear within the cell. Valid options are  <ul><li><code>"within"</code>: the component will be rendered
	 * inside the record / cell.  {@link com.smartgwt.client.widgets.Canvas#getSnapTo snapTo} may be set to specify where the
	 * component should render within  the row or cell, and {@link com.smartgwt.client.widgets.Canvas#getSnapOffsetTop
	 * snapOffsetTop} / {@link com.smartgwt.client.widgets.Canvas#getSnapOffsetLeft snapOffsetLeft} may  be set to indent
	 * recordComponents within their parent cells.  Note that if unset, the component will show up at the top/left edge  for
	 * components embedded within an entire row, or for per-cell components, cell  align and valign will be respected.  Note
	 * also that, when rendering components "within"  cells, specified component heights will be respected and will change the
	 * height of the   row.  However, if you want components to completely fill a cell at it's default height,   set height:
	 * "100%" or rows will render at the default height of the component. </li>  <li><code>"expand"</code>: the component will
	 * be written into the cell below the  normal cell content, causing the cell to expand vertically to accommodate it.
	 * <li><code>null</code>: If this attribute is unset, we will default to showing  recordComponents with position
	 * <code>"within"</code> if   {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponentsByCell
	 * showRecordComponentsByCell} is true, otherwise using <code>"expand"</code>  logic. </ul>
	 *
	 *
	 * @return EmbeddedPosition
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents
	 */
	public EmbeddedPosition getRecordComponentPosition() {
		return EnumUtil.getEnum(EmbeddedPosition.values(), getAttribute("recordComponentPosition"));
	}

	/**
	 * The name of the ListGridRecord property that specifies the DataSource to use when  {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is "related".  The default is  {@link
	 * com.smartgwt.client.widgets.grid.ListGridRecord#getDetailDS detailDS}. Note that you can set the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDetailDS detailDS} at the grid level instead if the same dataSource is to
	 * be used for all records.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordDetailDSProperty recordDetailDSProperty Default value is "detailDS"
	 */
	public void setRecordDetailDSProperty(String recordDetailDSProperty) {
		setAttribute("recordDetailDSProperty", recordDetailDSProperty, true);
	}

	/**
	 * The name of the ListGridRecord property that specifies the DataSource to use when  {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is "related".  The default is  {@link
	 * com.smartgwt.client.widgets.grid.ListGridRecord#getDetailDS detailDS}. Note that you can set the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getDetailDS detailDS} at the grid level instead if the same dataSource is to
	 * be used for all records.
	 *
	 *
	 * @return String
	 */
	public String getRecordDetailDSProperty() {
		return getAttributeAsString("recordDetailDSProperty");
	}

	/**
	 * Property name on a record that should be checked to determine whether the record may be edited. <br> This property is
	 * configurable to avoid possible collision with data values in record. With the default setting of "_canEdit", a record
	 * can be set non-editable by ensuring record._canEdit == false. <br> For controlling editability for the entire grid or
	 * for a field, set grid.canEdit or field.canEdit.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordEditProperty recordEditProperty Default value is "_canEdit"
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGrid#canEditCell
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setRecordEditProperty(String recordEditProperty) {
		setAttribute("recordEditProperty", recordEditProperty, true);
	}

	/**
	 * Property name on a record that should be checked to determine whether the record may be edited. <br> This property is
	 * configurable to avoid possible collision with data values in record. With the default setting of "_canEdit", a record
	 * can be set non-editable by ensuring record._canEdit == false. <br> For controlling editability for the entire grid or
	 * for a field, set grid.canEdit or field.canEdit.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getCanEdit
	 * @see com.smartgwt.client.widgets.grid.ListGrid#canEditCell
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public String getRecordEditProperty() {
		return getAttributeAsString("recordEditProperty");
	}

	/**
	 * If showing any record summary fields (IE: fields of {@link com.smartgwt.client.types.ListGridFieldType type:"summary"}),
	 * this attribute specifies a custom base style to apply to cells in the summary field
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param recordSummaryBaseStyle recordSummaryBaseStyle Default value is "recordSummaryCell"
	 */
	public void setRecordSummaryBaseStyle(String recordSummaryBaseStyle) {
		setAttribute("recordSummaryBaseStyle", recordSummaryBaseStyle, true);
	}

	/**
	 * If showing any record summary fields (IE: fields of {@link com.smartgwt.client.types.ListGridFieldType type:"summary"}),
	 * this attribute specifies a custom base style to apply to cells in the summary field
	 *
	 *
	 * @return String
	 */
	public String getRecordSummaryBaseStyle() {
		return getAttributeAsString("recordSummaryBaseStyle");
	}

	/**
	 * The title to use for the {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldDefaults remove field}.  Note
	 * that this text will appear in the column-picker but the not in the field-header because the removeField has {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getShowTitle showTitle} set to false by default.  This can be changed via
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldProperties removeFieldProperties}.
	 *
	 * @param removeFieldTitle removeFieldTitle Default value is "[Remove record]"
	 */
	public void setRemoveFieldTitle(String removeFieldTitle) {
		setAttribute("removeFieldTitle", removeFieldTitle, true);
	}

	/**
	 * The title to use for the {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldDefaults remove field}.  Note
	 * that this text will appear in the column-picker but the not in the field-header because the removeField has {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getShowTitle showTitle} set to false by default.  This can be changed via
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveFieldProperties removeFieldProperties}.
	 *
	 *
	 * @return String
	 */
	public String getRemoveFieldTitle() {
		return getAttributeAsString("removeFieldTitle");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} is enabled, default icon to
	 * show in the auto-generated field that allows removing records.
	 *
	 * @param removeIcon removeIcon Default value is "[SKIN]/actions/remove.png"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setRemoveIcon(String removeIcon) throws IllegalStateException {
		setAttribute("removeIcon", removeIcon, false);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} is enabled, default icon to
	 * show in the auto-generated field that allows removing records.
	 *
	 *
	 * @return String
	 */
	public String getRemoveIcon() {
		return getAttributeAsString("removeIcon");
	}

	/**
	 * Default width and height of {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveIcon remove icons} for this
	 * ListGrid.
	 *
	 * @param removeIconSize removeIconSize Default value is 16
	 */
	public void setRemoveIconSize(int removeIconSize) {
		setAttribute("removeIconSize", removeIconSize, true);
	}

	/**
	 * Default width and height of {@link com.smartgwt.client.widgets.grid.ListGrid#getRemoveIcon remove icons} for this
	 * ListGrid.
	 *
	 *
	 * @return int
	 */
	public int getRemoveIconSize() {
		return getAttributeAsInt("removeIconSize");
	}

	/**
	 * True == we redraw the list viewer in real time as fields are being resized.  This can be slow with a large list and/or
	 * on some platforms.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param resizeFieldsInRealTime resizeFieldsInRealTime Default value is Browser.isIE && isc.Browser.isWin
	 */
	public void setResizeFieldsInRealTime(Boolean resizeFieldsInRealTime) {
		setAttribute("resizeFieldsInRealTime", resizeFieldsInRealTime, true);
	}

	/**
	 * True == we redraw the list viewer in real time as fields are being resized.  This can be slow with a large list and/or
	 * on some platforms.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getResizeFieldsInRealTime() {
		return getAttributeAsBoolean("resizeFieldsInRealTime");
	}

	/**
	 * If the user is editing a record in this listGrid, and attempts to navigate to a field beyond the end of the row, via tab
	 * (or shift-tab off the first editable field), this  property determines what action to take:<ul> <li>"next": start
	 * editing the next (or previous) record in the list <li>"same": put focus back into the first editable field of the same
	 * record. <li>"done": hide the editor <li>"stop": leave focus in the cell being edited <li>"none": no action </ul>
	 *
	 * @param rowEndEditAction rowEndEditAction Default value is null
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setRowEndEditAction(RowEndEditAction rowEndEditAction) {
		setAttribute("rowEndEditAction", rowEndEditAction.getValue(), true);
	}

	/**
	 * If the user is editing a record in this listGrid, and attempts to navigate to a field beyond the end of the row, via tab
	 * (or shift-tab off the first editable field), this  property determines what action to take:<ul> <li>"next": start
	 * editing the next (or previous) record in the list <li>"same": put focus back into the first editable field of the same
	 * record. <li>"done": hide the editor <li>"stop": leave focus in the cell being edited <li>"none": no action </ul>
	 *
	 *
	 * @return RowEndEditAction
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public RowEndEditAction getRowEndEditAction() {
		return EnumUtil.getEnum(RowEndEditAction.values(), getAttribute("rowEndEditAction"));
	}

	/**
	 * The number to start the row-count from - default value is 1.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param rowNumberStart rowNumberStart Default value is 1
	 */
	public void setRowNumberStart(int rowNumberStart) {
		setAttribute("rowNumberStart", rowNumberStart, true);
	}

	/**
	 * The number to start the row-count from - default value is 1.
	 *
	 *
	 * @return int
	 */
	public int getRowNumberStart() {
		return getAttributeAsInt("rowNumberStart");
	}

	/**
	 * The CSS Style name for the {@link com.smartgwt.client.widgets.grid.ListGrid#getRowNumberField rowNumberField}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param rowNumberStyle rowNumberStyle Default value is "cellDark"
	 */
	public void setRowNumberStyle(String rowNumberStyle) {
		setAttribute("rowNumberStyle", rowNumberStyle, true);
	}

	/**
	 * The CSS Style name for the {@link com.smartgwt.client.widgets.grid.ListGrid#getRowNumberField rowNumberField}.
	 *
	 *
	 * @return String
	 */
	public String getRowNumberStyle() {
		return getAttributeAsString("rowNumberStyle");
	}

	/**
	 * Whether edits should be saved whenever the user moves between cells in the current edit row. <P> If unset, defaults to
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell this.editByCell}. <P> To avoid automatic saving entirely,
	 * set {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoSaveEdits autoSaveEdits}:false.
	 *
	 * @param saveByCell saveByCell Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEditByCell
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setSaveByCell(Boolean saveByCell) {
		setAttribute("saveByCell", saveByCell, true);
	}

	/**
	 * Whether edits should be saved whenever the user moves between cells in the current edit row. <P> If unset, defaults to
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell this.editByCell}. <P> To avoid automatic saving entirely,
	 * set {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoSaveEdits autoSaveEdits}:false.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEditByCell
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getSaveByCell() {
		return getAttributeAsBoolean("saveByCell");
	}

	/**
	 * For grids with a specified {@link com.smartgwt.client.widgets.grid.ListGrid#getDataSource dataSource}, this property can
	 * be set to  <code>true</code> to avoid the grid from attempting to save / retrieve data from the server.  In this case
	 * the grid's data should be specified as an array via  the {@link com.smartgwt.client.widgets.grid.ListGrid#getData data}
	 * attribute, and the datasource will simply act as a schema to describe the set of fields visible in the grid.  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanEdit Inline edits}, or removals via the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} mechanism will update the local data
	 * array rather than attempting to perform operations against the dataSource.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param saveLocally saveLocally Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setUseRemoteValidators
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public void setSaveLocally(Boolean saveLocally) throws IllegalStateException {
		setAttribute("saveLocally", saveLocally, false);
	}

	/**
	 * For grids with a specified {@link com.smartgwt.client.widgets.grid.ListGrid#getDataSource dataSource}, this property can
	 * be set to  <code>true</code> to avoid the grid from attempting to save / retrieve data from the server.  In this case
	 * the grid's data should be specified as an array via  the {@link com.smartgwt.client.widgets.grid.ListGrid#getData data}
	 * attribute, and the datasource will simply act as a schema to describe the set of fields visible in the grid.  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanEdit Inline edits}, or removals via the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCanRemoveRecords canRemoveRecords} mechanism will update the local data
	 * array rather than attempting to perform operations against the dataSource.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getUseRemoteValidators
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public Boolean getSaveLocally() {
		return getAttributeAsBoolean("saveLocally");
	}

	/**
	 * While drag scrolling in an incrementally rendered grid, time in milliseconds to wait before redrawing, after the last
	 * mouse movement by the user.  See also {@link com.smartgwt.client.grid.GridRenderer#getInstantScrollTrackRedraw
	 * instantScrollTrackRedraw} for cases where this delay is skipped.
	 *
	 * @param scrollRedrawDelay scrollRedrawDelay Default value is 75
	 */
	public void setScrollRedrawDelay(int scrollRedrawDelay) {
		setAttribute("scrollRedrawDelay", scrollRedrawDelay, true);
	}

	/**
	 * While drag scrolling in an incrementally rendered grid, time in milliseconds to wait before redrawing, after the last
	 * mouse movement by the user.  See also {@link com.smartgwt.client.grid.GridRenderer#getInstantScrollTrackRedraw
	 * instantScrollTrackRedraw} for cases where this delay is skipped.
	 *
	 *
	 * @return int
	 */
	public int getScrollRedrawDelay() {
		return getAttributeAsInt("scrollRedrawDelay");
	}

	/**
	 * How selection of rows should be presented to the user. <P> For <code>selectionAppearance:"checkbox"</code> with multiple
	 * selection allowed, you would typically use {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionType
	 * selectionType}:"simple" (the default).  Because  <code>selectionType</code> and <code>selectionAppearance</code> are
	 * unrelated,  the combination of <code>selectionAppearance:"checkbox"</code> and <code>selectionType:"multiple"</code>
	 * results in a grid where multiple selection can only be achieved via shift-click or ctrl-click.   <P> If using
	 * <code>"checkbox"</code> for a {@link com.smartgwt.client.widgets.grid.ListGrid}, see also {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCheckboxField checkboxField} for customization APIs. <P> If using
	 * <code>"checkbox"</code> for a {@link com.smartgwt.client.widgets.tree.TreeGrid}, an extra icon, {@link
	 * com.smartgwt.client.widgets.tree.TreeGrid#getExtraIcon TreeGrid.getExtraIcon} is not supported. Additionally only {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionType selectionType}:"simple" and "single" are supported. You can
	 * also toggle the display of a disabled checkbox on a treegrid, displayed  when the node can't be selected, via {@link
	 * com.smartgwt.client.widgets.tree.TreeGrid#getShowDisabledSelectionCheckbox showDisabledSelectionCheckbox}.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Changes selectionAppearance on the fly.
	 *
	 * @param selectionAppearance new selection appearance. Default value is "rowStyle"
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public void setSelectionAppearance(SelectionAppearance selectionAppearance) {
		setAttribute("selectionAppearance", selectionAppearance.getValue(), true);
	}

	/**
	 * How selection of rows should be presented to the user. <P> For <code>selectionAppearance:"checkbox"</code> with multiple
	 * selection allowed, you would typically use {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionType
	 * selectionType}:"simple" (the default).  Because  <code>selectionType</code> and <code>selectionAppearance</code> are
	 * unrelated,  the combination of <code>selectionAppearance:"checkbox"</code> and <code>selectionType:"multiple"</code>
	 * results in a grid where multiple selection can only be achieved via shift-click or ctrl-click.   <P> If using
	 * <code>"checkbox"</code> for a {@link com.smartgwt.client.widgets.grid.ListGrid}, see also {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getCheckboxField checkboxField} for customization APIs. <P> If using
	 * <code>"checkbox"</code> for a {@link com.smartgwt.client.widgets.tree.TreeGrid}, an extra icon, {@link
	 * com.smartgwt.client.widgets.tree.TreeGrid#getExtraIcon TreeGrid.getExtraIcon} is not supported. Additionally only {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionType selectionType}:"simple" and "single" are supported. You can
	 * also toggle the display of a disabled checkbox on a treegrid, displayed  when the node can't be selected, via {@link
	 * com.smartgwt.client.widgets.tree.TreeGrid#getShowDisabledSelectionCheckbox showDisabledSelectionCheckbox}.
	 *
	 *
	 * @return SelectionAppearance
	 * @see com.smartgwt.client.docs.Selection Selection overview and related methods
	 */
	public SelectionAppearance getSelectionAppearance() {
		return EnumUtil.getEnum(SelectionAppearance.values(), getAttribute("selectionAppearance"));
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
	public void setSelectionProperty(String selectionProperty) throws IllegalStateException {
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
	public String getSelectionProperty() {
		return getAttributeAsString("selectionProperty");
	}

	/**
	 * Defines a listGrid's clickable-selection behavior.   <P> The default selection appearance is governed by {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance}: if selectionAppearance is
	 * "checkbox", this will be "simple", otherwise, this will be "multiple".
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Changes selectionType on the fly.
	 *
	 * @param selectionType New selection style.. Default value is null
	 * @see com.smartgwt.client.types.SelectionStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_multiselect" target="examples">Multiple select Example</a>
	 */
	public void setSelectionType(SelectionStyle selectionType) {
		setAttribute("selectionType", selectionType.getValue(), true);
	}

	/**
	 * Defines a listGrid's clickable-selection behavior.   <P> The default selection appearance is governed by {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionAppearance selectionAppearance}: if selectionAppearance is
	 * "checkbox", this will be "simple", otherwise, this will be "multiple".
	 *
	 *
	 * @return SelectionStyle
	 * @see com.smartgwt.client.types.SelectionStyle
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_interaction_multiselect" target="examples">Multiple select Example</a>
	 */
	public SelectionStyle getSelectionType() {
		return EnumUtil.getEnum(SelectionStyle.values(), getAttribute("selectionType"));
	}

	/**
	 * When the user starts editing a row, should the row also be selected?  <P>  Note that  when this attribute is set to
	 * <code>true</code>, other all other rows in the grid  will be deselected when a record is selected due to editing.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param selectOnEdit selectOnEdit Default value is true
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setSelectOnEdit(Boolean selectOnEdit) {
		setAttribute("selectOnEdit", selectOnEdit, true);
	}

	/**
	 * When the user starts editing a row, should the row also be selected?  <P>  Note that  when this attribute is set to
	 * <code>true</code>, other all other rows in the grid  will be deselected when a record is selected due to editing.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getSelectOnEdit() {
		return getAttributeAsBoolean("selectOnEdit");
	}

	/**
	 * Whether all columns should be drawn all at once, or only columns visible in the viewport. <P> Drawing all columns causes
	 * longer initial rendering time, but allows smoother horizontal scrolling.  With a very large number of columns,
	 * showAllColumns will become too slow.
	 *
	 * @param showAllColumns showAllColumns Default value is false
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setShowAllColumns(Boolean showAllColumns) throws IllegalStateException {
		setAttribute("showAllColumns", showAllColumns, false);
	}

	/**
	 * Whether all columns should be drawn all at once, or only columns visible in the viewport. <P> Drawing all columns causes
	 * longer initial rendering time, but allows smoother horizontal scrolling.  With a very large number of columns,
	 * showAllColumns will become too slow.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowAllColumns() {
		return getAttributeAsBoolean("showAllColumns");
	}

	/**
	 * Whether all rows should be drawn all at once, or only rows visible in the viewport. <P> Drawing all rows causes longer
	 * initial rendering time, but allows smoother vertical scrolling. With a very large number of rows, showAllRows will
	 * become too slow. <P> See also {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} and
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAllMaxCells drawAllMaxCells}.
	 *
	 * @param showAllRecords showAllRecords Default value is false
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public void setShowAllRecords(Boolean showAllRecords) {
		setAttribute("showAllRecords", showAllRecords, true);
	}

	/**
	 * Whether all rows should be drawn all at once, or only rows visible in the viewport. <P> Drawing all rows causes longer
	 * initial rendering time, but allows smoother vertical scrolling. With a very large number of rows, showAllRows will
	 * become too slow. <P> See also {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAheadRatio drawAheadRatio} and
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getDrawAllMaxCells drawAllMaxCells}.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_rows" target="examples">Rows Example</a>
	 */
	public Boolean getShowAllRecords() {
		return getAttributeAsBoolean("showAllRecords");
	}

	/**
	 * If <code>true</code> this grid will create and show per-row backgroundComponents  as detailed {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBackgroundComponent here}.
	 *
	 * @param showBackgroundComponent showBackgroundComponent Default value is false
	 */
	public void setShowBackgroundComponent(Boolean showBackgroundComponent) {
		setAttribute("showBackgroundComponent", showBackgroundComponent, true);
	}

	/**
	 * If <code>true</code> this grid will create and show per-row backgroundComponents  as detailed {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getBackgroundComponent here}.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowBackgroundComponent() {
		return getAttributeAsBoolean("showBackgroundComponent");
	}

	/**
	 * Whether to show a context menu with standard items for all context clicks on rows in the body.
	 *
	 * @param showCellContextMenus showCellContextMenus Default value is false
	 */
	public void setShowCellContextMenus(Boolean showCellContextMenus) {
		setAttribute("showCellContextMenus", showCellContextMenus, true);
	}

	/**
	 * Whether to show a context menu with standard items for all context clicks on rows in the body.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowCellContextMenus() {
		return getAttributeAsBoolean("showCellContextMenus");
	}

	/**
	 * Indicates whether the text of the emptyMessage property should be displayed if no data is available.
	 *
	 * @param showEmptyMessage showEmptyMessage Default value is true
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setEmptyMessage
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_grid" target="examples">Empty grid Example</a>
	 */
	public void setShowEmptyMessage(Boolean showEmptyMessage) {
		setAttribute("showEmptyMessage", showEmptyMessage, true);
	}

	/**
	 * Indicates whether the text of the emptyMessage property should be displayed if no data is available.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getEmptyMessage
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_appearance_empty_grid" target="examples">Empty grid Example</a>
	 */
	public Boolean getShowEmptyMessage() {
		return getAttributeAsBoolean("showEmptyMessage");
	}

	/**
	 * Should this listGrid display a filter row.  If true, this ListGrid
	 *  will be drawn with a single editable row, (separate from the body) with a filter button.
	 *  <P>
	 *  Values entered into this row are used as filter criteria to filter this List's data on
	 * enter-keypress or filter button click. {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle
	 * autoFetchTextMatchStyle} determines
	 * the textMatchStyle for the request passed to {@link com.smartgwt.client.widgets.grid.ListGrid#fetchData
	 * ListGrid.fetchData}.
	 *  <P>
	 * Note that if {@link com.smartgwt.client.widgets.grid.ListGrid#filterData ListGrid.filterData} or {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#fetchData ListGrid.fetchData} is called directly
	 *  while the filter editor is showing, the filter editor values will be updated to reflect the
	 *  new set of criteria. If you wish to retain the user entered filter criteria and 
	 *  programmatically modify a subset of field values programmatically this can be achieved by
	 *  deriving new criteria by copying the existing set of criteria and adding other changes - 
	 *  something like this:
	 *  <pre><code>
	 *    var newCriteria = myListGrid.getFilterEditorCriteria();
	 *    isc.addProperties(newCriteria, {
	 *       field1:"new value1",
	 *       field2:"new value2"
	 *    });
	 *    myListGrid.setCriteria(newCriteria);
	 *  </code></pre>
	 * In this example code we're using {@link com.smartgwt.client.widgets.grid.ListGrid#getFilterEditorCriteria
	 * ListGrid.getFilterEditorCriteria} rather than 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCriteria ListGrid.getCriteria} - this ensures that if the user has
	 * typed a new value into
	 *  the filter editor, but not yet clicked the filter button, we pick up the value the user
	 *  entered.
	 *  <P>
	 *  If you call <code>filterData()</code> and pass in criteria for dataSource 
	 *  fields that are not present in the ListGrid, these criteria will continue to be applied along
	 *  with the user visible criteria.
	 *  <P>
	 *  <b>filterEditor and advanced criteria</b>: If a developer calls <code>filterData()</code>
	 *  on a ListGrid and passes in {@link com.smartgwt.client.data.AdvancedCriteria}, expected behavior of the filter 
	 *  editor becomes ambiguous, as  AdvancedCriteria supports far more complex filter 
	 *  expressions than the ordinary filterEditor is capable of expressing.
	 *  <br>
	 *  The above example code assumes simple criteria, but if we wanted to apply advanced 
	 * criteria to the grid we could call {@link com.smartgwt.client.data.DataSource#combineCriteria
	 * DataSource.combineCriteria} rather than doing
	 *  a simple addProperties() on the criteria object.
	 *  <P>
	 *  Default behavior for AdvancedCriteria will combine the AdvancedCriteria with the values 
	 *  in the filter editor as follows:
	 *  <ul>
	 *  <li>If the top level criteria has operator of type "and":<br>
	 *   Each field in the top level
	 *   criteria array for which a 'canFilter' true field is shown in the listGrid will show up
	 *   if the specified operator matches the default filter behavior 
	 * (based on the {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle autoFetchTextMatchStyle}).<br>
	 *   If the user enters values in the filter editor, these will be combined with the
	 *   existing AdvancedCriteria by either replacing or adding field level criteria at the top 
	 *   level.</li>
	 *  <li>If the top level criteria is a single field-criteria:<br>
	 *   If the field shows up in the listGrid and is canFilter:true, it will be displayed to
	 *   the user (if the operator matches the default filter behavior for the field).<br>
	 *   If the user enters new filter criteria in the filterEditor, they will be combined with
	 *   this existing criterion via a top level "and" operator, or if the user modifies the
	 *   field for which the criterion already existed, it will be replaced.</li>
	 *  <li>Otherwise, if there are multiple top level criteria combined with an "or" operator,
	 *   these will not be shown in the filter editor. Any filter parameters the user enters will
	 *   be added to the existing criteria via an additional top level "and" operator, meaning
	 *   the user will essentially filter a subset of the existing criteria</li>
	 *  </ul>
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor} property. Allows the filter editor to be shown or hidden at runtime.
	 *
	 * @param showFilterEditor true if the filter editor should be shown, false if it should be hidden. Default value is null
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_filter" target="examples">Filter Example</a>
	 */
	public void setShowFilterEditor(Boolean showFilterEditor) {
		setAttribute("showFilterEditor", showFilterEditor, true);
	}

	/**
	 * Should this listGrid display a filter row.  If true, this ListGrid
	 *  will be drawn with a single editable row, (separate from the body) with a filter button.
	 *  <P>
	 *  Values entered into this row are used as filter criteria to filter this List's data on
	 * enter-keypress or filter button click. {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle
	 * autoFetchTextMatchStyle} determines
	 * the textMatchStyle for the request passed to {@link com.smartgwt.client.widgets.grid.ListGrid#fetchData
	 * ListGrid.fetchData}.
	 *  <P>
	 * Note that if {@link com.smartgwt.client.widgets.grid.ListGrid#filterData ListGrid.filterData} or {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#fetchData ListGrid.fetchData} is called directly
	 *  while the filter editor is showing, the filter editor values will be updated to reflect the
	 *  new set of criteria. If you wish to retain the user entered filter criteria and 
	 *  programmatically modify a subset of field values programmatically this can be achieved by
	 *  deriving new criteria by copying the existing set of criteria and adding other changes - 
	 *  something like this:
	 *  <pre><code>
	 *    var newCriteria = myListGrid.getFilterEditorCriteria();
	 *    isc.addProperties(newCriteria, {
	 *       field1:"new value1",
	 *       field2:"new value2"
	 *    });
	 *    myListGrid.setCriteria(newCriteria);
	 *  </code></pre>
	 * In this example code we're using {@link com.smartgwt.client.widgets.grid.ListGrid#getFilterEditorCriteria
	 * ListGrid.getFilterEditorCriteria} rather than 
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getCriteria ListGrid.getCriteria} - this ensures that if the user has
	 * typed a new value into
	 *  the filter editor, but not yet clicked the filter button, we pick up the value the user
	 *  entered.
	 *  <P>
	 *  If you call <code>filterData()</code> and pass in criteria for dataSource 
	 *  fields that are not present in the ListGrid, these criteria will continue to be applied along
	 *  with the user visible criteria.
	 *  <P>
	 *  <b>filterEditor and advanced criteria</b>: If a developer calls <code>filterData()</code>
	 *  on a ListGrid and passes in {@link com.smartgwt.client.data.AdvancedCriteria}, expected behavior of the filter 
	 *  editor becomes ambiguous, as  AdvancedCriteria supports far more complex filter 
	 *  expressions than the ordinary filterEditor is capable of expressing.
	 *  <br>
	 *  The above example code assumes simple criteria, but if we wanted to apply advanced 
	 * criteria to the grid we could call {@link com.smartgwt.client.data.DataSource#combineCriteria
	 * DataSource.combineCriteria} rather than doing
	 *  a simple addProperties() on the criteria object.
	 *  <P>
	 *  Default behavior for AdvancedCriteria will combine the AdvancedCriteria with the values 
	 *  in the filter editor as follows:
	 *  <ul>
	 *  <li>If the top level criteria has operator of type "and":<br>
	 *   Each field in the top level
	 *   criteria array for which a 'canFilter' true field is shown in the listGrid will show up
	 *   if the specified operator matches the default filter behavior 
	 * (based on the {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFetchTextMatchStyle autoFetchTextMatchStyle}).<br>
	 *   If the user enters values in the filter editor, these will be combined with the
	 *   existing AdvancedCriteria by either replacing or adding field level criteria at the top 
	 *   level.</li>
	 *  <li>If the top level criteria is a single field-criteria:<br>
	 *   If the field shows up in the listGrid and is canFilter:true, it will be displayed to
	 *   the user (if the operator matches the default filter behavior for the field).<br>
	 *   If the user enters new filter criteria in the filterEditor, they will be combined with
	 *   this existing criterion via a top level "and" operator, or if the user modifies the
	 *   field for which the criterion already existed, it will be replaced.</li>
	 *  <li>Otherwise, if there are multiple top level criteria combined with an "or" operator,
	 *   these will not be shown in the filter editor. Any filter parameters the user enters will
	 *   be added to the existing criteria via an additional top level "and" operator, meaning
	 *   the user will essentially filter a subset of the existing criteria</li>
	 *  </ul>
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_filter" target="examples">Filter Example</a>
	 */
	public Boolean getShowFilterEditor() {
		return getAttributeAsBoolean("showFilterEditor");
	}

	/**
	 * For use with {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor}:true and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAllowFilterExpressions allowFilterExpressions}:true, adds a  menu item to
	 * the Filter context menu that shows the supported Expression table in a dialog.
	 *
	 * @param showFilterExpressionLegendMenuItem showFilterExpressionLegendMenuItem Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setShowFilterExpressionLegendMenuItem(Boolean showFilterExpressionLegendMenuItem) throws IllegalStateException {
		setAttribute("showFilterExpressionLegendMenuItem", showFilterExpressionLegendMenuItem, false);
	}

	/**
	 * For use with {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor}:true and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAllowFilterExpressions allowFilterExpressions}:true, adds a  menu item to
	 * the Filter context menu that shows the supported Expression table in a dialog.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowFilterExpressionLegendMenuItem() {
		return getAttributeAsBoolean("showFilterExpressionLegendMenuItem");
	}

	/**
	 * Should this ListGrid show a summary row beneath the last record of the grid. This summary row will contain per-field
	 * summary information. See {@link com.smartgwt.client.widgets.grid.ListGridField#getShowGridSummary showGridSummary} and
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getGridSummaryFunction ListGrid.getGridSummaryFunction} for details on
	 * how the summary value to be displayed for each column will be calculated. <P> Note that the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summaryRow autoChild} will be created to actually display the
	 * summary row.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary} attribute
	 *
	 * @param showGridSummary new value for this.showGridSummary. Default value is false
	 */
	public void setShowGridSummary(Boolean showGridSummary) {
		setAttribute("showGridSummary", showGridSummary, true);
	}

	/**
	 * Should this ListGrid show a summary row beneath the last record of the grid. This summary row will contain per-field
	 * summary information. See {@link com.smartgwt.client.widgets.grid.ListGridField#getShowGridSummary showGridSummary} and
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getGridSummaryFunction ListGrid.getGridSummaryFunction} for details on
	 * how the summary value to be displayed for each column will be calculated. <P> Note that the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summaryRow autoChild} will be created to actually display the
	 * summary row.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowGridSummary() {
		return getAttributeAsBoolean("showGridSummary");
	}

	/**
	 * If this listGrid supports {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping}, setting this
	 * property will cause the grid to render an extra row at the end of each group when grouped, containing summary
	 * information for the fields. Summary information will be calculated by the {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getGroupSummary ListGridField.getGroupSummary} method if specified,
	 * otherwise via the specified {@link com.smartgwt.client.widgets.grid.ListGridField#getSummaryFunction summaryFunction}.
	 *
	 * @param showGroupSummary showGroupSummary Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setGroupByFieldSummaries
	 */
	public void setShowGroupSummary(Boolean showGroupSummary) {
		setAttribute("showGroupSummary", showGroupSummary, true);
	}

	/**
	 * If this listGrid supports {@link com.smartgwt.client.widgets.grid.ListGrid#getCanGroupBy grouping}, setting this
	 * property will cause the grid to render an extra row at the end of each group when grouped, containing summary
	 * information for the fields. Summary information will be calculated by the {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getGroupSummary ListGridField.getGroupSummary} method if specified,
	 * otherwise via the specified {@link com.smartgwt.client.widgets.grid.ListGridField#getSummaryFunction summaryFunction}.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getGroupByFieldSummaries
	 */
	public Boolean getShowGroupSummary() {
		return getAttributeAsBoolean("showGroupSummary");
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped}, and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, setting this property causes
	 * field summary values for each group to be displayed directly in the group header node, rather than showing up at the
	 * bottom of each expanded group. <P> Note that this means the group header node will be showing multiple field values
	 * rather than the default display of a single cell spanning all columns containing the group title. Developers may specify
	 * an explicit {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleField groupTitleField}, or rely on the
	 * automatically generated {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupTitleColumn groupTitleColumn} to
	 * have group titles be visible in as well as the summary values. <P> Also note that multi-line group summaries are not
	 * supported when showing the group summary in the group header. If multiple  {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getSummaryFunction field summary functions} are defined for some field
	 * only the first will be displayed when this property is set to true.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummaryInHeader showGroupSummaryInHeader}
	 *
	 * @param showGroupSummaryInHeader new showGroupSummaryInHeader state. Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public void setShowGroupSummaryInHeader(Boolean showGroupSummaryInHeader) {
		setAttribute("showGroupSummaryInHeader", showGroupSummaryInHeader, true);
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped}, and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummary showGroupSummary} is true, setting this property causes
	 * field summary values for each group to be displayed directly in the group header node, rather than showing up at the
	 * bottom of each expanded group. <P> Note that this means the group header node will be showing multiple field values
	 * rather than the default display of a single cell spanning all columns containing the group title. Developers may specify
	 * an explicit {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleField groupTitleField}, or rely on the
	 * automatically generated {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGroupTitleColumn groupTitleColumn} to
	 * have group titles be visible in as well as the summary values. <P> Also note that multi-line group summaries are not
	 * supported when showing the group summary in the group header. If multiple  {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getSummaryFunction field summary functions} are defined for some field
	 * only the first will be displayed when this property is set to true.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#groupBy
	 */
	public Boolean getShowGroupSummaryInHeader() {
		return getAttributeAsBoolean("showGroupSummaryInHeader");
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummaryInHeader showGroupSummaryInHeader} is true, instead of
	 * group header nodes showing up with a single cell value spanning the full set of columns, summaries for each field will
	 * show up in the appropriate columns of the  header node. <P> In this case there are 2 options for where the group title
	 * will show up. Developers may specify an existing field to put the title values into via {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleField groupTitleField}. If no groupTitleField is specified, this
	 * property may be set to <code>true</code> which causes a <code>groupTitleColumn</code> to be automatically generated.
	 * Each group header will show the group title in this column (records within the group will not show a value for this
	 * column). The column appears in the leftmost position within the grid (unless {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowRowNumbers showRowNumbers} is true, in which case this column shows up
	 * in the second-leftmost position), and by default will auto-fit to its data. <P> To customize this field, developers may
	 * modify  {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleColumnProperties groupTitleColumnProperties}
	 *
	 * @param showGroupTitleColumn showGroupTitleColumn Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setShowGroupTitleColumn(Boolean showGroupTitleColumn) throws IllegalStateException {
		setAttribute("showGroupTitleColumn", showGroupTitleColumn, false);
	}

	/**
	 * If this grid is {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy grouped} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowGroupSummaryInHeader showGroupSummaryInHeader} is true, instead of
	 * group header nodes showing up with a single cell value spanning the full set of columns, summaries for each field will
	 * show up in the appropriate columns of the  header node. <P> In this case there are 2 options for where the group title
	 * will show up. Developers may specify an existing field to put the title values into via {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleField groupTitleField}. If no groupTitleField is specified, this
	 * property may be set to <code>true</code> which causes a <code>groupTitleColumn</code> to be automatically generated.
	 * Each group header will show the group title in this column (records within the group will not show a value for this
	 * column). The column appears in the leftmost position within the grid (unless {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getShowRowNumbers showRowNumbers} is true, in which case this column shows up
	 * in the second-leftmost position), and by default will auto-fit to its data. <P> To customize this field, developers may
	 * modify  {@link com.smartgwt.client.widgets.grid.ListGrid#getGroupTitleColumnProperties groupTitleColumnProperties}
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowGroupTitleColumn() {
		return getAttributeAsBoolean("showGroupTitleColumn");
	}

	/**
	 * Should we show the header for this ListGrid?
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Show or hide the ListGrid header.
	 *
	 * @param showHeader true to show the header, false to hide it.. Default value is true
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setShowHeader(Boolean showHeader) {
		setAttribute("showHeader", showHeader, true);
	}

	/**
	 * Should we show the header for this ListGrid?
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public Boolean getShowHeader() {
		return getAttributeAsBoolean("showHeader");
	}

	/**
	 * Whether to show a context menu on the header with standard items for showing and hiding fields.
	 *
	 * @param showHeaderContextMenu showHeaderContextMenu Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#displayHeaderContextMenu
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getHeaderContextMenuItems
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public void setShowHeaderContextMenu(Boolean showHeaderContextMenu) throws IllegalStateException {
		setAttribute("showHeaderContextMenu", showHeaderContextMenu, false);
	}

	/**
	 * Whether to show a context menu on the header with standard items for showing and hiding fields.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#displayHeaderContextMenu
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getHeaderContextMenuItems
	 * @see com.smartgwt.client.docs.GridHeader GridHeader overview and related methods
	 */
	public Boolean getShowHeaderContextMenu() {
		return getAttributeAsBoolean("showHeaderContextMenu");
	}

	/**
	 * If set to true and {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu showHeaderContextMenu} is
	 * true, the {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderMenuButton headerMenuButton} will be displayed when
	 * the user rolls over the header buttons in this grid.
	 *
	 * @param showHeaderMenuButton showHeaderMenuButton Default value is false
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setShowHeaderMenuButton(Boolean showHeaderMenuButton) throws IllegalStateException {
		setAttribute("showHeaderMenuButton", showHeaderMenuButton, false);
	}

	/**
	 * If set to true and {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu showHeaderContextMenu} is
	 * true, the {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderMenuButton headerMenuButton} will be displayed when
	 * the user rolls over the header buttons in this grid.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowHeaderMenuButton() {
		return getAttributeAsBoolean("showHeaderMenuButton");
	}

	/**
	 * If true, and canHover is also true, when the user hovers over a cell, hover text will pop up next to the mouse.  The
	 * contents of the hover is determined by {@link com.smartgwt.client.widgets.grid.ListGrid#cellHoverHTML
	 * ListGrid.cellHoverHTML}.
	 *
	 * @param showHover showHover Default value is true
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setCanHover
	 * @see com.smartgwt.client.widgets.grid.ListGrid#cellHoverHTML
	 */
	@Override
	public void setShowHover(Boolean showHover) {
		setAttribute("showHover", showHover, true);
	}

	/**
	 * If true, and canHover is also true, when the user hovers over a cell, hover text will pop up next to the mouse.  The
	 * contents of the hover is determined by {@link com.smartgwt.client.widgets.grid.ListGrid#cellHoverHTML
	 * ListGrid.cellHoverHTML}.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getCanHover
	 * @see com.smartgwt.client.widgets.grid.ListGrid#cellHoverHTML
	 */
	@Override
	public Boolean getShowHover() {
		return getAttributeAsBoolean("showHover");
	}

	/**
	 * When set to true and canHover is also true, shows a widget hovering at the mouse point. <P> A number of builtin modes
	 * are provided - see {@link com.smartgwt.client.types.HoverMode}.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showHoverComponents showHoverComponents Default value is false
	 */
	@Override
	public void setShowHoverComponents(Boolean showHoverComponents) {
		setAttribute("showHoverComponents", showHoverComponents, true);
	}

	/**
	 * When set to true and canHover is also true, shows a widget hovering at the mouse point. <P> A number of builtin modes
	 * are provided - see {@link com.smartgwt.client.types.HoverMode}.
	 *
	 *
	 * @return Boolean
	 */
	@Override
	public Boolean getShowHoverComponents() {
		return getAttributeAsBoolean("showHoverComponents");
	}

	/**
	 * If set to true, this listGrid should create and show an embedded component in every row of the grid. <P> Developers
	 * using this feature should implement the  {@link com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent
	 * ListGrid.createRecordComponent} and {@link com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent
	 * ListGrid.updateRecordComponent} methods. <P> createRecordComponent() will be called by the grid as rows are rendered to
	 * create the record components to use per record or per cell.  Your implementation should return a component to embed in
	 * the record passed in. Note that this method should create and return a new component each time it is called. <P> This
	 * feature also supports reusing components in different rows in the grid. If  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentPoolingMode recordComponentPoolingMode} is set to
	 * <code>"recycle"</code>, components created by the <code>createRecordComponent</code> method will become available for
	 * reuse when they are no longer associated with a record. The system will automatically store these in a pool. When a
	 * record with no associated component is rendered, if there are any recordComponents in this pool,  the system will call
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent ListGrid.updateRecordComponent}, and pass in the
	 * component. This allows the developer to apply record-specific attributes to an already created component and render it
	 * out into the new record. This greatly improves performance for  large grids as it allows a small number of components to
	 * be created and reused rather  than maintaining potentially one record component for every cell in the grid. <P> Record
	 * components are refreshed according to the recordComponentPooling mode. If set to  <code>data</code> components will be
	 * maintained as long as their associated record remains present in the data set (but this can require a component to be
	 * created for every record so is not desirable for large data sets). Otherwise record components are refreshed as they are
	 * scrolled into view. Note that you can explicitly refresh record components via {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#invalidateRecordComponents ListGrid.invalidateRecordComponents} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#refreshRecordComponent ListGrid.refreshRecordComponent} <P> NOTE:
	 * recordComponents can have an impact on row height and therefore may require {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getVirtualScrolling virtualScrolling}. This is not supported in conjunction
	 * with {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen fields}. If you are using recordComponents
	 * in a listGrid with frozenFields, you can specify an explicit {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentHeight recordComponentHeight} to ensure every row in the
	 * grid renders tall enough to accommodate the recordComponents, and as such virtual scrolling is not required.
	 *
	 * <br><br>If this method is called after the component has been drawn/initialized:
	 * Setter for the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents showRecordComponents} attribute
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showRecordComponents new value for <code>this.showRecordComponents</code>. Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setRecordComponentPosition
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setShowRecordComponentsByCell
	 * @see com.smartgwt.client.types.RecordComponentPoolingMode
	 * @see com.smartgwt.client.widgets.grid.ListGrid#showRecordComponent
	 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent
	 * @see com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent
	 */
	public void setShowRecordComponents(Boolean showRecordComponents) {
		setAttribute("showRecordComponents", showRecordComponents, true);
	}

	/**
	 * If set to true, this listGrid should create and show an embedded component in every row of the grid. <P> Developers
	 * using this feature should implement the  {@link com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent
	 * ListGrid.createRecordComponent} and {@link com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent
	 * ListGrid.updateRecordComponent} methods. <P> createRecordComponent() will be called by the grid as rows are rendered to
	 * create the record components to use per record or per cell.  Your implementation should return a component to embed in
	 * the record passed in. Note that this method should create and return a new component each time it is called. <P> This
	 * feature also supports reusing components in different rows in the grid. If  {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentPoolingMode recordComponentPoolingMode} is set to
	 * <code>"recycle"</code>, components created by the <code>createRecordComponent</code> method will become available for
	 * reuse when they are no longer associated with a record. The system will automatically store these in a pool. When a
	 * record with no associated component is rendered, if there are any recordComponents in this pool,  the system will call
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent ListGrid.updateRecordComponent}, and pass in the
	 * component. This allows the developer to apply record-specific attributes to an already created component and render it
	 * out into the new record. This greatly improves performance for  large grids as it allows a small number of components to
	 * be created and reused rather  than maintaining potentially one record component for every cell in the grid. <P> Record
	 * components are refreshed according to the recordComponentPooling mode. If set to  <code>data</code> components will be
	 * maintained as long as their associated record remains present in the data set (but this can require a component to be
	 * created for every record so is not desirable for large data sets). Otherwise record components are refreshed as they are
	 * scrolled into view. Note that you can explicitly refresh record components via {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#invalidateRecordComponents ListGrid.invalidateRecordComponents} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#refreshRecordComponent ListGrid.refreshRecordComponent} <P> NOTE:
	 * recordComponents can have an impact on row height and therefore may require {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getVirtualScrolling virtualScrolling}. This is not supported in conjunction
	 * with {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen fields}. If you are using recordComponents
	 * in a listGrid with frozenFields, you can specify an explicit {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentHeight recordComponentHeight} to ensure every row in the
	 * grid renders tall enough to accommodate the recordComponents, and as such virtual scrolling is not required.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getRecordComponentPosition
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponentsByCell
	 * @see com.smartgwt.client.types.RecordComponentPoolingMode
	 * @see com.smartgwt.client.widgets.grid.ListGrid#showRecordComponent
	 * @see com.smartgwt.client.widgets.grid.ListGrid#createRecordComponent
	 * @see com.smartgwt.client.widgets.grid.ListGrid#updateRecordComponent
	 */
	public Boolean getShowRecordComponents() {
		return getAttributeAsBoolean("showRecordComponents");
	}

	/**
	 * Should we show different styling for the cell the mouse is over? <br> If true, the cell style will have the suffix
	 * "Over" appended.
	 *
	 * @param showRollOver showRollOver Default value is true
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setShowRollOver(Boolean showRollOver) {
		setAttribute("showRollOver", showRollOver, true);
	}

	/**
	 * Should we show different styling for the cell the mouse is over? <br> If true, the cell style will have the suffix
	 * "Over" appended.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public Boolean getShowRollOver() {
		return getAttributeAsBoolean("showRollOver");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOver showRollOver} is true, this property provides an
	 * option to show roll over styling with the {@link com.smartgwt.client.widgets.grid.ListGrid#getRollOverCanvas
	 * rollOverCanvas} and {@link com.smartgwt.client.widgets.grid.ListGrid#getRollUnderCanvas rollUnderCanvas} rather than
	 * using css styling.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showRollOverCanvas showRollOverCanvas Default value is null
	 */
	public void setShowRollOverCanvas(Boolean showRollOverCanvas) {
		setAttribute("showRollOverCanvas", showRollOverCanvas, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRollOver showRollOver} is true, this property provides an
	 * option to show roll over styling with the {@link com.smartgwt.client.widgets.grid.ListGrid#getRollOverCanvas
	 * rollOverCanvas} and {@link com.smartgwt.client.widgets.grid.ListGrid#getRollUnderCanvas rollUnderCanvas} rather than
	 * using css styling.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowRollOverCanvas() {
		return getAttributeAsBoolean("showRollOverCanvas");
	}

	/**
	 * When set to true, shows an additional field at the beginning of the field-list  (respecting RTL) that displays the
	 * current rowNum for each record.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showRowNumbers showRowNumbers Default value is null
	 */
	public void setShowRowNumbers(Boolean showRowNumbers) {
		setAttribute("showRowNumbers", showRowNumbers, true);
	}

	/**
	 * When set to true, shows an additional field at the beginning of the field-list  (respecting RTL) that displays the
	 * current rowNum for each record.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowRowNumbers() {
		return getAttributeAsBoolean("showRowNumbers");
	}

	/**
	 * Should the "Selected" style be applied to selected records?
	 *
	 * @param showSelectedStyle showSelectedStyle Default value is true
	 * @see com.smartgwt.client.grid.GridRenderer#getCellStyle
	 */
	public void setShowSelectedStyle(Boolean showSelectedStyle) {
		setAttribute("showSelectedStyle", showSelectedStyle, true);
	}

	/**
	 * Should the "Selected" style be applied to selected records?
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.grid.GridRenderer#getCellStyle
	 */
	public Boolean getShowSelectedStyle() {
		return getAttributeAsBoolean("showSelectedStyle");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionType selectionType} is set to <code>"single"</code>,
	 * setting this property to true means selection will be displayed to the user with the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionCanvas selectionCanvas}  and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionUnderCanvas selectionUnderCanvas} rather than using css styling.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showSelectionCanvas showSelectionCanvas Default value is null
	 */
	public void setShowSelectionCanvas(Boolean showSelectionCanvas) {
		setAttribute("showSelectionCanvas", showSelectionCanvas, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSelectionType selectionType} is set to <code>"single"</code>,
	 * setting this property to true means selection will be displayed to the user with the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionCanvas selectionCanvas}  and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSelectionUnderCanvas selectionUnderCanvas} rather than using css styling.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowSelectionCanvas() {
		return getAttributeAsBoolean("showSelectionCanvas");
	}

	/**
	 * Indicates whether a sorting arrow should appear for the listGrid, and its location. See {@link
	 * com.smartgwt.client.types.SortArrow} for details. <P> Clicking the sort arrow reverses the direction of sorting for the
	 * current sort column (if any), or sorts the listGrid by its first sortable column. The arrow image on the button
	 * indicates the current direction of sorting. If undefined, the sort arrow will show up in the sorted field, and the
	 * corner sort button will be displayed if a vertical scrollbar is being displayed
	 *
	 * @param showSortArrow showSortArrow Default value is null
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setShowSortArrow(SortArrow showSortArrow) {
		setAttribute("showSortArrow", showSortArrow.getValue(), true);
	}

	/**
	 * Indicates whether a sorting arrow should appear for the listGrid, and its location. See {@link
	 * com.smartgwt.client.types.SortArrow} for details. <P> Clicking the sort arrow reverses the direction of sorting for the
	 * current sort column (if any), or sorts the listGrid by its first sortable column. The arrow image on the button
	 * indicates the current direction of sorting. If undefined, the sort arrow will show up in the sorted field, and the
	 * corner sort button will be displayed if a vertical scrollbar is being displayed
	 *
	 *
	 * @return SortArrow
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public SortArrow getShowSortArrow() {
		return EnumUtil.getEnum(SortArrow.values(), getAttribute("showSortArrow"));
	}

	/**
	 * When multiple fields are sorted, set this to false to hide the sort-numeral  displayed by default after the sort-arrows
	 * in the header-buttons of sorted fields.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param showSortNumerals showSortNumerals Default value is null
	 */
	public void setShowSortNumerals(Boolean showSortNumerals) {
		setAttribute("showSortNumerals", showSortNumerals, true);
	}

	/**
	 * When multiple fields are sorted, set this to false to hide the sort-numeral  displayed by default after the sort-arrows
	 * in the header-buttons of sorted fields.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowSortNumerals() {
		return getAttributeAsBoolean("showSortNumerals");
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderSpans headerSpans} are in use, whether to show a
	 * hierarchical column picker that includes both headerSpans and normal headers, with normal headers indented under
	 * headerSpans similarly to how a {@link com.smartgwt.client.widgets.tree.TreeGrid} displays a Tree. <P> If
	 * <code>showTreeColumnPicker</code> is false, no column picker will be shown on the headerSpan itself, and the column
	 * picker for a clicked on a normal field header will include only normal fields.
	 *
	 * @param showTreeColumnPicker showTreeColumnPicker Default value is true
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setShowTreeColumnPicker(Boolean showTreeColumnPicker) throws IllegalStateException {
		setAttribute("showTreeColumnPicker", showTreeColumnPicker, false);
	}

	/**
	 * When {@link com.smartgwt.client.widgets.grid.ListGrid#getHeaderSpans headerSpans} are in use, whether to show a
	 * hierarchical column picker that includes both headerSpans and normal headers, with normal headers indented under
	 * headerSpans similarly to how a {@link com.smartgwt.client.widgets.tree.TreeGrid} displays a Tree. <P> If
	 * <code>showTreeColumnPicker</code> is false, no column picker will be shown on the headerSpan itself, and the column
	 * picker for a clicked on a normal field header will include only normal fields.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getShowTreeColumnPicker() {
		return getAttributeAsBoolean("showTreeColumnPicker");
	}

	/**
	 * If this list grid is showing any {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen} fields, and a
	 * horizontal scrollbar is visible at the bottom of the liquid columns, should an equivalent scrollbar gap be left visible
	 * below the frozen columns?<br> Note that if set to <code>true</code> any backgroundColor or border applied to the
	 * ListGrid will show up below the bottom row of the frozen column(s).
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param shrinkForFreeze shrinkForFreeze Default value is false
	 * @see com.smartgwt.client.docs.FrozenFields FrozenFields overview and related methods
	 */
	public void setShrinkForFreeze(Boolean shrinkForFreeze) {
		setAttribute("shrinkForFreeze", shrinkForFreeze, true);
	}

	/**
	 * If this list grid is showing any {@link com.smartgwt.client.widgets.grid.ListGridField#getFrozen frozen} fields, and a
	 * horizontal scrollbar is visible at the bottom of the liquid columns, should an equivalent scrollbar gap be left visible
	 * below the frozen columns?<br> Note that if set to <code>true</code> any backgroundColor or border applied to the
	 * ListGrid will show up below the bottom row of the frozen column(s).
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.FrozenFields FrozenFields overview and related methods
	 */
	public Boolean getShrinkForFreeze() {
		return getAttributeAsBoolean("shrinkForFreeze");
	}

	/**
	 * If <code>record[this.singleCellValueProperty]</code> is set for some record, the  record will be displayed as a single
	 * cell spanning every column in the grid, with  contents set to the value of
	 * <code>record[this.singleCellValueProperty]</code>.
	 *
	 * @param singleCellValueProperty singleCellValueProperty Default value is "singleCellValue"
	 */
	public void setSingleCellValueProperty(String singleCellValueProperty) {
		setAttribute("singleCellValueProperty", singleCellValueProperty, true);
	}

	/**
	 * If <code>record[this.singleCellValueProperty]</code> is set for some record, the  record will be displayed as a single
	 * cell spanning every column in the grid, with  contents set to the value of
	 * <code>record[this.singleCellValueProperty]</code>.
	 *
	 *
	 * @return String
	 */
	public String getSingleCellValueProperty() {
		return getAttributeAsString("singleCellValueProperty");
	}

	/**
	 * Where do 'skin' images (those provided with the class) live?
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param skinImgDir skinImgDir Default value is "images/ListGrid/"
	 * @see com.smartgwt.client.docs.Images Images overview and related methods
	 */
	@Override
	public void setSkinImgDir(String skinImgDir) {
		setAttribute("skinImgDir", skinImgDir, true);
	}

	/**
	 * Where do 'skin' images (those provided with the class) live?
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Images Images overview and related methods
	 */
	@Override
	public String getSkinImgDir() {
		return getAttributeAsString("skinImgDir");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to sort a field in ascending order.
	 *
	 * @param sortFieldAscendingText sortFieldAscendingText Default value is "Sort Ascending"
	 */
	public void setSortFieldAscendingText(String sortFieldAscendingText) {
		setAttribute("sortFieldAscendingText", sortFieldAscendingText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to sort a field in ascending order.
	 *
	 *
	 * @return String
	 */
	public String getSortFieldAscendingText() {
		return getAttributeAsString("sortFieldAscendingText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to sort a field in descending order.
	 *
	 * @param sortFieldDescendingText sortFieldDescendingText Default value is "Sort Descending"
	 */
	public void setSortFieldDescendingText(String sortFieldDescendingText) {
		setAttribute("sortFieldDescendingText", sortFieldDescendingText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, this attribute will be shown as the menu item title to sort a field in descending order.
	 *
	 *
	 * @return String
	 */
	public String getSortFieldDescendingText() {
		return getAttributeAsString("sortFieldDescendingText");
	}

	/**
	 * When multiple fields are sorted, the Style to apply to the numeral that appears after the  sort-arrows in the
	 * header-buttons of sorted fields.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param sortNumeralStyle sortNumeralStyle Default value is "sortNumeral"
	 */
	public void setSortNumeralStyle(String sortNumeralStyle) {
		setAttribute("sortNumeralStyle", sortNumeralStyle, true);
	}

	/**
	 * When multiple fields are sorted, the Style to apply to the numeral that appears after the  sort-arrows in the
	 * header-buttons of sorted fields.
	 *
	 *
	 * @return String
	 */
	public String getSortNumeralStyle() {
		return getAttributeAsString("sortNumeralStyle");
	}

	/**
	 * If this is an editable listGrid, this property determines how failure to save due to  validation errors should be
	 * displayed to the user. <P> If this property is true, when validation errors occur the errors will be displayed to the
	 * user in an alert, and focus will be returned to the first cell to fail validation. <P> If false, this the cells that
	 * failed validation will be silently styled with the  editFailedBaseStyle.<br> <b>Note:</b> stopOnErrors being set to true
	 * implies that 'waitForSave' is also true. We will not dismiss the editor until save has completed if stopOnErrors is
	 * true.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param stopOnErrors stopOnErrors Default value is false
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setWaitForSave
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setStopOnErrors(Boolean stopOnErrors) {
		setAttribute("stopOnErrors", stopOnErrors, true);
	}

	/**
	 * If this is an editable listGrid, this property determines how failure to save due to  validation errors should be
	 * displayed to the user. <P> If this property is true, when validation errors occur the errors will be displayed to the
	 * user in an alert, and focus will be returned to the first cell to fail validation. <P> If false, this the cells that
	 * failed validation will be silently styled with the  editFailedBaseStyle.<br> <b>Note:</b> stopOnErrors being set to true
	 * implies that 'waitForSave' is also true. We will not dismiss the editor until save has completed if stopOnErrors is
	 * true.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getWaitForSave
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getStopOnErrors() {
		return getAttributeAsBoolean("stopOnErrors");
	}

	/**
	 * Default CSS class
	 *
	 * @param styleName styleName Default value is "listGrid"
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	@Override
	public void setStyleName(String styleName) {
		setAttribute("styleName", styleName, true);
	}

	/**
	 * Default CSS class
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	@Override
	public String getStyleName() {
		return getAttributeAsString("styleName");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowGridSummary showGridSummary} is true, by default summary
	 * values are calculated on the  client based on the current data-set for the grid (see {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGridSummary ListGrid.getGridSummary} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getGridSummaryFunction ListGrid.getGridSummaryFunction}). <P> In some cases
	 * however it may make sense to calculate summary values on the server and retrieve them via a dataSource fetch. If set,
	 * this property specifies a dataSource to fetch against for the summary row. The dataSource should return a single record
	 * with summary data for each field for which summary data should be shown. Note that specifying this property completely
	 * bypasses the standard client-side grid summary calculation logic. <P> The fetch may be further customized via {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRowCriteria summaryRowCriteria} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRowFetchRequestProperties summaryRowFetchRequestProperties}
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param summaryRowDataSource summaryRowDataSource Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setSummaryRowDataSource(DataSource summaryRowDataSource) throws IllegalStateException {
		setAttribute("summaryRowDataSource", summaryRowDataSource == null ? null : summaryRowDataSource.getOrCreateJsObj(), false);
	}

	/**
	 * Default height for the {@link com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summary row autoChild}. Note that
	 * this height is a minumum - the summary row has {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData
	 * autoFitData} set to "vertical" so if multiple rows are visible in the grid summary, the summaryRow component will expand
	 * to accomodate them.
	 *
	 * @param summaryRowHeight summaryRowHeight Default value is 20
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setSummaryRowHeight(int summaryRowHeight) throws IllegalStateException {
		setAttribute("summaryRowHeight", summaryRowHeight, false);
	}

	/**
	 * Default height for the {@link com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summary row autoChild}. Note that
	 * this height is a minumum - the summary row has {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData
	 * autoFitData} set to "vertical" so if multiple rows are visible in the grid summary, the summaryRow component will expand
	 * to accomodate them.
	 *
	 *
	 * @return int
	 */
	public int getSummaryRowHeight() {
		return getAttributeAsInt("summaryRowHeight");
	}

	/**
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle baseStyle} for the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summaryRow}
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param summaryRowStyle summaryRowStyle Default value is "gridSummaryCell"
	 */
	public void setSummaryRowStyle(String summaryRowStyle) {
		setAttribute("summaryRowStyle", summaryRowStyle, true);
	}

	/**
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle baseStyle} for the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getSummaryRow summaryRow}
	 *
	 *
	 * @return String
	 */
	public String getSummaryRowStyle() {
		return getAttributeAsString("summaryRowStyle");
	}

	/**
	 * "Tall" baseStyle for this listGrid. Only applies if {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} is  set to null. <P> If <code>baseStyle</code> is unset, this property will be used as a base cell style
	 * unless the grid is showing fixed height rows with a specified cellHeight that matches {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalCellHeight normalCellHeight}, in which case {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} will be used. Note that in Internet
	 * Explorer if {@link com.smartgwt.client.widgets.grid.ListGrid#getFastCellUpdates fastCellUpdates} is true,
	 * <code>tallBaseStyle</code> will also be used even if the cellHeight matches the specified <code>normalCellHeight</code>
	 * for the grid.
	 *
	 * @param tallBaseStyle tallBaseStyle Default value is "cell"
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 */
	public void setTallBaseStyle(String tallBaseStyle) throws IllegalStateException {
		setAttribute("tallBaseStyle", tallBaseStyle, false);
	}

	/**
	 * "Tall" baseStyle for this listGrid. Only applies if {@link com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 * baseStyle} is  set to null. <P> If <code>baseStyle</code> is unset, this property will be used as a base cell style
	 * unless the grid is showing fixed height rows with a specified cellHeight that matches {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalCellHeight normalCellHeight}, in which case {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNormalBaseStyle normalBaseStyle} will be used. Note that in Internet
	 * Explorer if {@link com.smartgwt.client.widgets.grid.ListGrid#getFastCellUpdates fastCellUpdates} is true,
	 * <code>tallBaseStyle</code> will also be used even if the cellHeight matches the specified <code>normalCellHeight</code>
	 * for the grid.
	 *
	 *
	 * @return String
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getBaseStyle
	 */
	public String getTallBaseStyle() {
		return getAttributeAsString("tallBaseStyle");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to unfreeze a currently frozen field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Unfreeze " + the field's summary title.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param unfreezeFieldText unfreezeFieldText Default value is "Unfreeze \$title}"
	 */
	public void setUnfreezeFieldText(String unfreezeFieldText) {
		setAttribute("unfreezeFieldText", unfreezeFieldText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid and {@link com.smartgwt.client.widgets.grid.ListGrid#getCanFreezeFields this.canFreezeFields} is true, this string
	 * will be shown as the title for the menu item to unfreeze a currently frozen field. <P> This is a dynamic string - text
	 * within <code>\${...}</code> will be evaluated as JS code when the message is displayed, with <code>title</code>
	 * available as a variable containing the field title. <P> Default value returns "Unfreeze " + the field's summary title.
	 *
	 *
	 * @return String
	 */
	public String getUnfreezeFieldText() {
		return getAttributeAsString("unfreezeFieldText");
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, and {@link com.smartgwt.client.widgets.grid.ListGrid#getIsGrouped this.isGrouped} is true, this attribute will be
	 * shown as the title for the menu item to ungroup the grid.
	 *
	 * @param ungroupText ungroupText Default value is "Ungroup"
	 */
	public void setUngroupText(String ungroupText) {
		setAttribute("ungroupText", ungroupText, true);
	}

	/**
	 * If we're showing a {@link com.smartgwt.client.widgets.grid.ListGrid#getShowHeaderContextMenu headerContextMenu} for this
	 * grid, and {@link com.smartgwt.client.widgets.grid.ListGrid#getIsGrouped this.isGrouped} is true, this attribute will be
	 * shown as the title for the menu item to ungroup the grid.
	 *
	 *
	 * @return String
	 */
	public String getUngroupText() {
		return getAttributeAsString("ungroupText");
	}

	/**
	 * Are rollovers cell-level or row-level?
	 *
	 * @param useCellRollOvers useCellRollOvers Default value is false
	 */
	public void setUseCellRollOvers(Boolean useCellRollOvers) {
		setAttribute("useCellRollOvers", useCellRollOvers, true);
	}

	/**
	 * Are rollovers cell-level or row-level?
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getUseCellRollOvers() {
		return getAttributeAsBoolean("useCellRollOvers");
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveLocally saveLocally} is specified, but this grid is bound to
	 * a DataSource which includes remote field validators, by default edits will be saved synchronously and these validators
	 * will not be executed.<br> Set this property to <code>true</code> to ensure these remote validators are called when 
	 * saving edits in saveLocally mode. Note that since these remote validators need to run on  the server, saving with this
	 * property set is asynchronous, even though the data that ultimately gets updated is already present on the client.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param useRemoteValidators useRemoteValidators Default value is null
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public void setUseRemoteValidators(Boolean useRemoteValidators) {
		setAttribute("useRemoteValidators", useRemoteValidators, true);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveLocally saveLocally} is specified, but this grid is bound to
	 * a DataSource which includes remote field validators, by default edits will be saved synchronously and these validators
	 * will not be executed.<br> Set this property to <code>true</code> to ensure these remote validators are called when 
	 * saving edits in saveLocally mode. Note that since these remote validators need to run on  the server, saving with this
	 * property set is asynchronous, even though the data that ultimately gets updated is already present on the client.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public Boolean getUseRemoteValidators() {
		return getAttributeAsBoolean("useRemoteValidators");
	}

	/**
	 * Whether client-side validation checks should be performed when the user moves between cells in the current edit row.  If
	 * unset, defaults to {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell editByCell}. <P> Note that validation
	 * always occurs when a row is to be saved, so setting {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell
	 * saveByCell}:true forces validation on cell transitions.  To completely disable automatic validation, set {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNeverValidate neverValidate}:true.
	 *
	 * @param validateByCell validateByCell Default value is null
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public void setValidateByCell(Boolean validateByCell) {
		setAttribute("validateByCell", validateByCell, true);
	}

	/**
	 * Whether client-side validation checks should be performed when the user moves between cells in the current edit row.  If
	 * unset, defaults to {@link com.smartgwt.client.widgets.grid.ListGrid#getEditByCell editByCell}. <P> Note that validation
	 * always occurs when a row is to be saved, so setting {@link com.smartgwt.client.widgets.grid.ListGrid#getSaveByCell
	 * saveByCell}:true forces validation on cell transitions.  To completely disable automatic validation, set {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getNeverValidate neverValidate}:true.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public Boolean getValidateByCell() {
		return getAttributeAsBoolean("validateByCell");
	}

	/**
	 * If true, validation will be performed on each edited cell when each editor's  "change" handler is fired.
	 *
	 * @param validateOnChange validateOnChange Default value is null
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setValidateOnChange
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public void setValidateOnChange(Boolean validateOnChange) {
		setAttribute("validateOnChange", validateOnChange, true);
	}

	/**
	 * If true, validation will be performed on each edited cell when each editor's  "change" handler is fired.
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getValidateOnChange
	 * @see com.smartgwt.client.docs.GridValidation GridValidation overview and related methods
	 */
	public Boolean getValidateOnChange() {
		return getAttributeAsBoolean("validateOnChange");
	}

	/**
	 * Height for value icons for this listGrid. Overrides {@link com.smartgwt.client.widgets.grid.ListGrid#getValueIconSize
	 * valueIconSize}. Can be overridden at the field level
	 *
	 * @param valueIconHeight valueIconHeight Default value is null
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setValueIconHeight(Integer valueIconHeight) {
		setAttribute("valueIconHeight", valueIconHeight, true);
	}

	/**
	 * Height for value icons for this listGrid. Overrides {@link com.smartgwt.client.widgets.grid.ListGrid#getValueIconSize
	 * valueIconSize}. Can be overridden at the field level
	 *
	 *
	 * @return Integer
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public Integer getValueIconHeight() {
		return getAttributeAsInt("valueIconHeight");
	}

	/**
	 * How much padding should there be on the left of valueIcons by default Can be overridden at the field level
	 *
	 * @param valueIconLeftPadding valueIconLeftPadding Default value is 2
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setValueIcons
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setValueIconLeftPadding(int valueIconLeftPadding) {
		setAttribute("valueIconLeftPadding", valueIconLeftPadding, true);
	}

	/**
	 * How much padding should there be on the left of valueIcons by default Can be overridden at the field level
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getValueIcons
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getValueIconLeftPadding() {
		return getAttributeAsInt("valueIconLeftPadding");
	}

	/**
	 * How much padding should there be on the right of valueIcons by default
	 *
	 * @param valueIconRightPadding valueIconRightPadding Default value is 2
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setValueIcons
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setValueIconRightPadding(int valueIconRightPadding) {
		setAttribute("valueIconRightPadding", valueIconRightPadding, true);
	}

	/**
	 * How much padding should there be on the right of valueIcons by default
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getValueIcons
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getValueIconRightPadding() {
		return getAttributeAsInt("valueIconRightPadding");
	}

	/**
	 * Default width and height of value icons for this ListGrid. Can be overridden at the listGrid level via explicit {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getValueIconWidth valueIconWidth} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getValueIconHeight valueIconHeight}, or at the field level via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getValueIconSize valueIconSize}, {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getValueIconWidth valueIconWidth} and {ListGridField.valueIconHeight}
	 *
	 * @param valueIconSize valueIconSize Default value is 16
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setValueIconWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#setValueIconHeight
	 * @see com.smartgwt.client.widgets.grid.ListGridField#setValueIconSize
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setValueIconSize(int valueIconSize) {
		setAttribute("valueIconSize", valueIconSize, true);
	}

	/**
	 * Default width and height of value icons for this ListGrid. Can be overridden at the listGrid level via explicit {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getValueIconWidth valueIconWidth} and {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getValueIconHeight valueIconHeight}, or at the field level via {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getValueIconSize valueIconSize}, {@link
	 * com.smartgwt.client.widgets.grid.ListGridField#getValueIconWidth valueIconWidth} and {ListGridField.valueIconHeight}
	 *
	 *
	 * @return int
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getValueIconWidth
	 * @see com.smartgwt.client.widgets.grid.ListGrid#getValueIconHeight
	 * @see com.smartgwt.client.widgets.grid.ListGridField#getValueIconSize
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public int getValueIconSize() {
		return getAttributeAsInt("valueIconSize");
	}

	/**
	 * Width for value icons for this listGrid. Overrides {@link com.smartgwt.client.widgets.grid.ListGrid#getValueIconSize
	 * valueIconSize}. Can be overridden at the field level
	 *
	 * @param valueIconWidth valueIconWidth Default value is null
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public void setValueIconWidth(Integer valueIconWidth) {
		setAttribute("valueIconWidth", valueIconWidth, true);
	}

	/**
	 * Width for value icons for this listGrid. Overrides {@link com.smartgwt.client.widgets.grid.ListGrid#getValueIconSize
	 * valueIconSize}. Can be overridden at the field level
	 *
	 *
	 * @return Integer
	 * @see com.smartgwt.client.docs.ImageColumns ImageColumns overview and related methods
	 */
	public Integer getValueIconWidth() {
		return getAttributeAsInt("valueIconWidth");
	}

	/**
	 * When incremental rendering is switched on and there are variable record heights, the virtual scrolling mechanism manages
	 * the differences in scroll height calculations due to the unknown sizes of unrendered rows to make the scrollbar and
	 * viewport appear correctly. <P> virtualScrolling is switched on automatically when fixedRecordHeights is false and when
	 * using the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents recordComponents subsystem}, as
	 * recordComponents expand the rows that contain them. This flag should be manually enabled when calling {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#addEmbeddedComponent ListGrid.addEmbeddedComponent}(...) if embedded
	 * components can cause record sizes to expand beyond specified cellHeight.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param virtualScrolling virtualScrolling Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setVirtualScrolling(Boolean virtualScrolling) throws IllegalStateException {
		setAttribute("virtualScrolling", virtualScrolling, false);
	}

	/**
	 * When incremental rendering is switched on and there are variable record heights, the virtual scrolling mechanism manages
	 * the differences in scroll height calculations due to the unknown sizes of unrendered rows to make the scrollbar and
	 * viewport appear correctly. <P> virtualScrolling is switched on automatically when fixedRecordHeights is false and when
	 * using the {@link com.smartgwt.client.widgets.grid.ListGrid#getShowRecordComponents recordComponents subsystem}, as
	 * recordComponents expand the rows that contain them. This flag should be manually enabled when calling {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#addEmbeddedComponent ListGrid.addEmbeddedComponent}(...) if embedded
	 * components can cause record sizes to expand beyond specified cellHeight.
	 *
	 *
	 * @return Boolean
	 */
	public Boolean getVirtualScrolling() {
		return getAttributeAsBoolean("virtualScrolling");
	}

	/**
	 * If this is an editable listGrid, this property determines whether the user will be able to dismiss the edit form, or
	 * navigate to another cell while the save is in  process (before the asynchronous server response returns).
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param waitForSave waitForSave Default value is false
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public void setWaitForSave(Boolean waitForSave) {
		setAttribute("waitForSave", waitForSave, true);
	}

	/**
	 * If this is an editable listGrid, this property determines whether the user will be able to dismiss the edit form, or
	 * navigate to another cell while the save is in  process (before the asynchronous server response returns).
	 *
	 *
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Editing Editing overview and related methods
	 */
	public Boolean getWaitForSave() {
		return getAttributeAsBoolean("waitForSave");
	}

	/**
	 * Should content within cells be allowed to wrap? <P> Even if content is allowed to wrap, if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights fixedRecordHeights} is set, the content will be clipped
	 * off at the cell boundary.  Either set a larger, fixed {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight
	 * cellHeight} to reveal more content, or set {@link com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights
	 * fixedRecordHeights} to false to allow auto-sizing.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param wrapCells wrapCells Default value is false
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_values" target="examples">Cell Values Example</a>
	 */
	public void setWrapCells(Boolean wrapCells) {
		setAttribute("wrapCells", wrapCells, true);
	}

	/**
	 * Should content within cells be allowed to wrap? <P> Even if content is allowed to wrap, if {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights fixedRecordHeights} is set, the content will be clipped
	 * off at the cell boundary.  Either set a larger, fixed {@link com.smartgwt.client.widgets.grid.ListGrid#getCellHeight
	 * cellHeight} to reveal more content, or set {@link com.smartgwt.client.widgets.grid.ListGrid#getFixedRecordHeights
	 * fixedRecordHeights} to false to allow auto-sizing.
	 *
	 *
	 * @return Boolean
	 * @see <a href="http://www.smartclient.com/smartgwt/showcase/#grid_autofit_values" target="examples">Cell Values Example</a>
	 */
	public Boolean getWrapCells() {
		return getAttributeAsBoolean("wrapCells");
	}

	public Set<SelectionChangedHandler> getSelectionChangedHandlers() {
		return Sets.newHashSet(selectionChangedHandlers);
	}

	@Override
	public HandlerRegistration addSelectionChangedHandler(final SelectionChangedHandler handler) {
		selectionChangedHandlers.add(handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				selectionChangedHandlers.remove(handler);
			}
		};
	}

	public Set<SelectionUpdatedHandler> getSelectionUpdatedHandlers() {
		return Sets.newHashSet(selectionUpdatedHandlers);
	}

	@Override
	public HandlerRegistration addSelectionUpdatedHandler(final SelectionUpdatedHandler handler) {
		selectionUpdatedHandlers.add(handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				selectionUpdatedHandlers.remove(handler);
			}
		};
	}

	/**
	 * Set the properties generated field that displays the current row number when {@link #setShowRowNumbers(Boolean) showRowNumbers} is true.
	 * For example you can change the default width of the row number column if you have data that exceeds 4 digits to accommodate the width of, say, 10000.
	 *
	 * @param rowNumberFieldProperties the row number field properties
	 */
	public void setRowNumberFieldProperties(ListGridField rowNumberFieldProperties) {
		setAttribute("rowNumberFieldProperties", rowNumberFieldProperties, true);
	}

	/**
	 * If this is an editable listGrid, this property will specify the {@link com.smartgwt.client.widgets.form.fields.DateItem#setInputFormat(String) inputFormat}
	 * applied to editors for fields of type "date"
	 *
	 * 3 character string containing the "M", "D" and "Y" characters to indicate the format of strings being parsed into Date instances via Date.parseInput().
	 *
	 * <p>
	 * As an example - an input format of "MDY" would parse "01/02/1999" to Jan 2nd 1999
	 *
	 * @param dateInputFormat the dateInputFormat
	 */
	public void setDateInputFormat(String dateInputFormat) {
		setAttribute("dateInputFormat", dateInputFormat, true);
	}

	/**
	 * A List of ListGridRecord objects, specifying the data to be used to populate the ListGrid.  In ListGrids, the
	 * data array specifies rows. Note that ListGrids automatically observe changes to the data List and redraw
	 * accordingly. <p> This property is settable directly only as part of a {@link
	 * com.smartgwt.client.widgets.grid.ListGrid} constructor.  If you want to change the {@link
	 * com.smartgwt.client.widgets.grid.ListGrid}'s data after initial creation, call {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#setData}. <p> This property will typically not be explicitly specified
	 * for databound ListGrids, where the data is returned from the server via databound component methods such as
	 * {@link com.smartgwt.client.widgets.grid.ListGrid#fetchData}. In this case the data objects will be set to a
	 * {@link com.smartgwt.client.data.ResultSet} rather than a simple array. Initialize the data object with the given
	 * array. Observes methods of the data object so that when the data changes, the listGrid will redraw
	 * automatically.
	 *
	 * @param records data to show in the list. Default value is null
	 */
	public void setData(ListGridRecord[] records) {
		setAttribute("data", records, true);
		requestRepaint();
	}

	/**
	 * An array of Record objects, specifying the data to be used to populate the DataBoundComponent. 
	 *
	 * @param data array of Record objects.
	 * @see #setData(ListGridRecord[])   
	 */
	public void setData(Record[] data) {
		setAttribute("data", data, true);
		requestRepaint();
	}

	/**
	 * Synonym for {@link #setData(ListGridRecord[])}
	 *
	 * @param records the records
	 */
	public void setRecords(ListGridRecord[] records) {
		setAttribute("data", records, true);
	}

	/**
	 * Should this ListGrid automatically expand to accomodate its content? <P> Valid settings are
	 * <ul><li><code>"vertical"</code>: expand vertically to accomodate records.</li>     <li><code>"horizontal"</code>:
	 * expand horizontally to accomodate fields.</li>     <li><code>"both"</code>: expand horizontally and vertically to
	 * accomodate content.</li> </ul> Note that how far the ListGrid will expand may be limited via the following
	 * properties: {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxHeight autoFitMaxHeight}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxRecords autoFitMaxRecords}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxWidth autoFitMaxWidth}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxColumns autoFitMaxColumns}. Setter for {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitData autoFitData}.
	 *
	 * @param autoFitData One of <code>"vertical"</code>, <code>"horizontal"</code>  or <code>"both"</code>. To disable
	 *                    auto fit behavior, pass in <code>null</code>.. Default value is null
	 */
	public void setAutoFitData(Autofit autoFitData) {
		setAttribute("autoFitData", autoFitData.getValue(), true);
	}

	/**
	* Property name on a record that will be checked to determine whether a record is enabled.&#010 <P>&#010 Setting this property on a record will effect the visual style and interactivity of&#010 the record.  If set to <code>false</code> the record (row in a {@link com.smartgwt.client.widgets.grid.ListGrid} or&#010 {@link com.smartgwt.client.widgets.tree.TreeGrid}) will not highlight when the mouse moves over it, nor will it respond to&#010 mouse clicks.
	*
	* @param recordEnabledProperty recordEnabledProperty Default value is "enabled"
	* @throws IllegalStateException this property cannot be changed after the component has been created
	*/
	public void setRecordEnabledProperty(String recordEnabledProperty) throws IllegalStateException {
		setAttribute("recordEnabledProperty", recordEnabledProperty, false);
	}

	/**
	 * Property name on a record that will be checked to determine whether a record is enabled.&#010 <P>&#010 Setting this property on a record will effect the visual style and interactivity of&#010 the record.  If set to <code>false</code> the record (row in a {@link com.smartgwt.client.widgets.grid.ListGrid} or&#010 {@link com.smartgwt.client.widgets.tree.TreeGrid}) will not highlight when the mouse moves over it, nor will it respond to&#010 mouse clicks.
	 *
	 *
	 * @return String
	 *
	 */
	public String getRecordEnabledProperty() {
		return getAttributeAsString("recordEnabledProperty");
	}

	/**
	 * Should this ListGrid automatically expand to accomodate its content? <P> Valid settings are
	 * <ul><li><code>"vertical"</code>: expand vertically to accomodate records.</li>     <li><code>"horizontal"</code>:
	 * expand horizontally to accomodate fields.</li>     <li><code>"both"</code>: expand horizontally and vertically to
	 * accomodate content.</li> </ul> Note that how far the ListGrid will expand may be limited via the following
	 * properties: {@link com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxHeight autoFitMaxHeight}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxRecords autoFitMaxRecords}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxWidth autoFitMaxWidth}, {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#getAutoFitMaxColumns autoFitMaxColumns}.
	 *
	 * @return String
	 */
	public Autofit getAutoFitData() {
		return EnumUtil.getEnum(Autofit.values(), getAttribute("autoFitData"));
	}

	/**
	 * Specifies the field by which this grid should be initially sorted.
	 *
	 * @param fieldName the field Name
	 */
	public void setSortField(String fieldName) {
		setAttribute("sortField", fieldName, true);
	}

	/**
	 * The field by which this grid should be initially sorted.
	 *
	 * @return the sort field
	 */
	public String getSortField() {
		return getAttribute("sortField");
	}

	/**
	 * Specifies the field by which this grid should be initially sorted. Note that if sortField is initally specified
	 * as a number, it will be converted to a string (field name) after list grid initialization.
	 *
	 * @param fieldIndex the field index
	 */
	public void setSortField(int fieldIndex) {
		setAttribute("sortField", fieldIndex, true);
	}

	/**
	 * Field to group grid records by. After initialization, use {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy}  to
	 * update the grouping field list, instead of modifying groupByField directly.
	 *
	 * @param field groupByField Default value is see below
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setGroupByField(String field) throws IllegalStateException {
		setAttribute("groupByField", field, false);
	}

	/**
	 * List of fields to group grid records. If only a single field is used, that field may be specified as a string.
	 * After initialization, use {@link com.smartgwt.client.widgets.grid.ListGrid#groupBy}  to update the grouping field
	 * list, instead of modifying groupByField directly.
	 *
	 * @param field groupByField Default value is see below
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setGroupByField(String... field) throws IllegalStateException {
		setAttribute("groupByField", field, false);
	}

	/**
	 * Describes the default state of ListGrid groups when groupBy is called. Possible values are: <ul> <li>"all": open
	 * all groups </li><li>"first": open the first group </li><li>"none": start with all groups closed </li><li>Array of
	 * values that should be opened </li> </ul>
	 *
	 * @param group the group
	 */
	public void setGroupStartOpen(GroupStartOpen group) {
		setAttribute("groupStartOpen", group, true);
	}

	/**
	 * @param groupValues Array of values that should be opened
	 */
	public void setGroupStartOpen(Object... groupValues) {
		setAttribute("groupStartOpen", groupValues, true);
	}

	/**
	 * Canvas with properties to apply to the auto-generated Selection Canvas properties when
	 * {@link #setShowSelectionCanvas(Boolean) showSelectionCanvas} is true.
	 *
	 * @param selectionUnderCanvasProperties the selection under Canvas properties
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setSelectionCanvasProperties(Canvas selectionCanvasProperties) throws IllegalStateException {
		setAttribute("selectionCanvasProperties", selectionCanvasProperties.getConfig(), false);
	}

	/**
	 * Canvas with properties to apply to the auto-generated Selection Under Canvas properties when
	 * {@link #setShowSelectionCanvas(Boolean) showSelectionCanvas} is true.
	 *
	 * @param selectionUnderCanvasProperties the selection under Canvas properties
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setSelectionUnderCanvasProperties(Canvas selectionUnderCanvasProperties) throws IllegalStateException {
		setAttribute("selectionUnderCanvasProperties", selectionUnderCanvasProperties.getConfig(), false);
	}

	/**
	 * The RollUnder Canvas properties when {@link #setShowRollOver(Boolean) showRollover} is true and
	 * {@link #setShowRollOverCanvas(Boolean) showRollOverCanvas} is true. This canvas will be created and displayed behind the current rollOver 
	 * cell in the page's z-order, meaning it will only be visible if the cell styling is transparent.
	 *
	 * @param rollUnderCanvasProperties the roll under Canvas properties
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setRollUnderCanvasProperties(Canvas rollUnderCanvasProperties) throws IllegalStateException {
		setAttribute("rollUnderCanvasProperties", rollUnderCanvasProperties.getConfig(), false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getShowFilterEditor showFilterEditor} is true, this attribute may be
	 * used to customize the filter button shown to the right of the filterEditor row.
	 *
	 * @param filterButtonProperties filterButtonProperties Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setFilterButtonProperties(Button filterButtonProperties) throws IllegalStateException {
		setAttribute("filterButtonProperties", filterButtonProperties == null ? null : filterButtonProperties.getConfig(), false);
	}

	/**
	 * An array of listGrid field configuration objects.  When a listGrid is initialized, if this property is set and there is
	 * no value for the <code>fields</code> attribute, this.fields will be defaulted to a generated array of field objects
	 * duplicated from this array. <P> This property is useful for cases where a standard set of fields will be displayed in
	 * multiple listGrids - for example a subclass of ListGrid intended to display a particular type of data:<br> In this
	 * example we would not assign a single {@link com.smartgwt.client.widgets.grid.ListGrid#getFields fields} array directly
	 * to the class via <code>addProperties()</code> as every generated instance of this class would then point to the same
	 * fields array object. This would cause unexpected behavior such as changes to the field order in one grid effecting other
	 * grids on the page.<br> Instead we could use <code>addProperties()</code> on our new subclass to set
	 * <code>defaultFields</code> to a standard array of fields to display. Each generated instance of the subclass would then
	 * show up with default fields duplicated from this array.
	 * <p><b>Note : </b> This is an advanced setting</p>
	 *
	 * @param defaultFields defaultFields Default value is null
	 * @throws IllegalStateException this property cannot be changed after the component has been created
	 */
	public void setDefaultFields(ListGridField[] defaultFields) throws IllegalStateException {
		setAttribute("defaultFields", defaultFields, false);
	}

	/**
	 * If {@link com.smartgwt.client.widgets.grid.ListGrid#getCanExpandRecords canExpandRecords} is true and {@link
	 * com.smartgwt.client.types.ExpansionMode listGrid.expansionMode} is <code>"related"</code>, this property specifies the
	 * dataSource for the  related records grid to be shown embedded in expanded records. <P> This property may also be
	 * specified on a per-record basis - see  {@link com.smartgwt.client.widgets.grid.ListGrid#getRecordDetailDSProperty
	 * recordDetailDSProperty}
	 *
	 *
	 * @param detailDS detail datasource
	 */
	public void setDetailDS(DataSource detailDS) {
		setAttribute("detailDS", detailDS == null ? null : detailDS.getOrCreateJsObj(), true);
	}

	/**
	 * How to fetch and manage records retrieve from the server.  See {@link com.smartgwt.client.types.FetchMode}. <P> This
	 * setting only applies to the {@link com.smartgwt.client.data.ResultSet} automatically created by calling {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#fetchData ListGrid.fetchData}.  If a pre-existing ResultSet is passed to
	 * setData() instead, it's existing setting for {@link com.smartgwt.client.data.ResultSet#getFetchMode fetchMode} applies.
	 *
	 * @param dataFetchMode dataFetchMode Default value is "paged"
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public void setDataFetchMode(FetchMode fetchMode) {
		setAttribute("dataFetchMode", fetchMode, true);
	}

	/**
	 * How to fetch and manage records retrieve from the server.  See {@link com.smartgwt.client.types.FetchMode}. <P> This
	 * setting only applies to the {@link com.smartgwt.client.data.ResultSet} automatically created by calling {@link
	 * com.smartgwt.client.widgets.grid.ListGrid#fetchData ListGrid.fetchData}.  If a pre-existing ResultSet is passed to
	 * setData() instead, it's existing setting for {@link com.smartgwt.client.data.ResultSet#getFetchMode fetchMode} applies.
	 *
	 *
	 * @return FetchMode
	 * @see com.smartgwt.client.docs.Databinding Databinding overview and related methods
	 */
	public FetchMode getDataFetchMode() {
		return EnumUtil.getEnum(FetchMode.values(), getAttribute("dataFetchMode"));
	}

	public void setDataPageSize(int dataPageSize) {
		setAttribute("dataPageSize", dataPageSize, true);
	}

	public int getDataPageSize() {
		return getAttributeAsInt("dataPageSize");
	}

	public void setUseAllDataSourceFields(Boolean useAllDataSourceFields) {
		setAttribute("useAllDataSourceFields", useAllDataSourceFields, true);
	}

	public Boolean getUseAllDataSourceFields() {
		return getAttributeAsBoolean("useAllDataSourceFields");
	}

	public void setShowHiddenFields(Boolean showHiddenFields) {
		setAttribute("showHiddenFields", showHiddenFields, true);
	}

	public Boolean getShowHiddenFields() {
		return getAttributeAsBoolean("showHiddenFields");
	}

	public void setShowDetailFields(Boolean showDetailFields) {
		setAttribute("showDetailFields", showDetailFields, true);
	}

	public Boolean getShowDetailFields() {
		return getAttributeAsBoolean("showDetailFields");
	}

	public void setShowComplexFields(Boolean showComplexFields) {
		setAttribute("showComplexFields", showComplexFields, true);
	}

	public Boolean getShowComplexFields() {
		return getAttributeAsBoolean("showComplexFields");
	}

	public void setFetchOperation(String fetchOperation) {
		setAttribute("fetchOperation", fetchOperation, true);
	}

	public String getFetchOperation() {
		return getAttributeAsString("fetchOperation");
	}

	public void setUpdateOperation(String updateOperation) {
		setAttribute("updateOperation", updateOperation, true);
	}

	public String getUpdateOperation() {
		return getAttributeAsString("updateOperation");
	}

	public void setAddOperation(String addOperation) {
		setAttribute("addOperation", addOperation, true);
	}

	public String getAddOperation() {
		return getAttributeAsString("addOperation");
	}

	public void setRemoveOperation(String removeOperation) {
		setAttribute("removeOperation", removeOperation, true);
	}

	public String getRemoveOperation() {
		return getAttributeAsString("removeOperation");
	}

	public void setExportFields(String[] exportFields) {
		setAttribute("exportFields", exportFields, true);
	}

	public String[] getExportFields() {
		return getAttributeAsStringArray("exportFields");
	}

	public void setExportAll(Boolean exportAll) {
		setAttribute("exportAll", exportAll, true);
	}

	public Boolean getExportAll() {
		return getAttributeAsBoolean("exportAll");
	}

	public void setPreventDuplicates(Boolean preventDuplicates) throws IllegalStateException {
		setAttribute("preventDuplicates", preventDuplicates, false);
	}

	public Boolean getPreventDuplicates() {
		return getAttributeAsBoolean("preventDuplicates");
	}

	public void setDuplicateDragMessage(String duplicateDragMessage) throws IllegalStateException {
		setAttribute("duplicateDragMessage", duplicateDragMessage, false);
	}

	public String getDuplicateDragMessage() {
		return getAttributeAsString("duplicateDragMessage");
	}

	public void setAddDropValues(Boolean addDropValues) {
		setAttribute("addDropValues", addDropValues, true);
	}

	public Boolean getAddDropValues() {
		return getAttributeAsBoolean("addDropValues");
	}

	public void setDropValues(Map dropValues) {
		setAttribute("dropValues", dropValues, true);
	}

	public Map getDropValues() {
		return getAttributeAsMap("dropValues");
	}

	public void setUseFlatFields(Boolean useFlatFields) throws IllegalStateException {
		setAttribute("useFlatFields", useFlatFields, false);
	}

	public Boolean getUseFlatFields() {
		return getAttributeAsBoolean("useFlatFields");
	}

	public void setHiliteProperty(String hiliteProperty) {
		setAttribute("hiliteProperty", hiliteProperty, true);
	}

	public String getHiliteProperty() {
		return getAttributeAsString("hiliteProperty");
	}

	public void setDragDataAction(DragDataAction dragDataAction) {
		setAttribute("dragDataAction", dragDataAction.getValue(), true);
	}

	public DragDataAction getDragDataAction() {
		return EnumUtil.getEnum(DragDataAction.values(), getAttribute("dragDataAction"));
	}

	public void setTitleField(String titleField) {
		setAttribute("titleField", titleField, true);
	}

	public String getTitleField() {
		return getAttributeAsString("titleField");
	}

	public void setAutoFetchData(Boolean autoFetchData) throws IllegalStateException {
		setAttribute("autoFetchData", autoFetchData, false);
	}

	public Boolean getAutoFetchData() {
		return getAttributeAsBoolean("autoFetchData");
	}

	public void setAutoFetchAsFilter(Boolean autoFetchAsFilter) throws IllegalStateException {
		setAttribute("autoFetchAsFilter", autoFetchAsFilter, false);
	}

	public Boolean getAutoFetchAsFilter() {
		return getAttributeAsBoolean("autoFetchAsFilter");
	}

	public ListGridField[] getFields() {
		return fields.toArray(new ListGridField[0]);
	}

	public void setFields(ListGridField... fields) {
		this.fields.clear();
		this.fields.addAll(Arrays.asList(fields));
	}

	public ListGridRecord[] getSelectedRecords() {
		return selectedRecords;
	}

	public void selectionChanged(ListGridRecord[] selections) {
	}

	public void setFields(List<ListGridField> fields) {
		ListGridField[] fieldsArr = new ListGridField[0];

		fieldsArr = fields.toArray(fieldsArr);
		setFields(fieldsArr);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SelectionEventFactory getSelectionEventFactory() {
		return selectionEventFactory == null ? selectionEventFactory = InjectorSingleton.get().getInstance(SelectionEventFactory.class) : selectionEventFactory;
	}

	public void setSelectionEventFactory(SelectionEventFactory selectionEventFactory) {
		this.selectionEventFactory = selectionEventFactory;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		propertyPainter.paintContent(target);

		if (dataSource != null) {
			target.addAttribute("dataSource", dataSource);
		}

		if (!selectionChangedHandlers.isEmpty()) {
			target.addAttribute("*hasSelectionChangedHandlers", true);
		}

		if (!selectionUpdatedHandlers.isEmpty()) {
			target.addAttribute("*hasSelectionUpdatedHandlers", true);
		}

		super.paintContent(target);
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		if (variables.containsKey("onSelectionChanged.event")) {
			try {
				final JsonRootNode root = new JdomParser().parse((String) variables.get("onSelectionChanged.event"));
				final SelectionEvent event = getSelectionEventFactory().newSelectionEvent(root);

				for (SelectionChangedHandler handler : selectionChangedHandlers) {
					handler.onSelectionChanged(event);
				}
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		}

		if (variables.containsKey("onSelectionUpdated.event")) {
			final SelectionUpdatedEvent event = new SelectionUpdatedEvent();
			
			for (SelectionUpdatedHandler handler : selectionUpdatedHandlers) {
				handler.onSelectionUpdated(event);
			}
		}

		super.changeVariables(source, variables);
	}
}
