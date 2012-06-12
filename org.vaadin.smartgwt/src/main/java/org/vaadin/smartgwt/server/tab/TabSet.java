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

package org.vaadin.smartgwt.server.tab;

import java.util.Map;

import org.vaadin.rpc.server.ServerSideHandler;
import org.vaadin.rpc.server.ServerSideProxy;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.VaadinIntegration;
import org.vaadin.smartgwt.server.core.ComponentList;
import org.vaadin.smartgwt.server.core.ComponentPropertyPainter;
import org.vaadin.smartgwt.server.types.Side;
import org.vaadin.smartgwt.server.util.EnumUtil;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

/**
 * The TabSet class allows components on several panes to share the same space. The tabs at  the top can be selected by the
 * user to show each pane.  <P> Tabs are configured via the <code>tabs</code> property, each of which has a
 * <code>pane</code> property which will be displayed in the main pane when that tab is selected.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.tab.VTabSet.class)
public class TabSet extends Canvas {
	private final ServerSideProxy client = new ServerSideProxy(new ServerSideHandlerImpl());
	private final ComponentPropertyPainter propertyPainter = new ComponentPropertyPainter(this);
	private final ComponentList<Tab> paintableTabs = propertyPainter.addComponentList("tabs");

	public TabSet() {
		scClassName = "TabSet";
	}

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller showTabScroller} is true, should tabs be scrolled
     * into view via an  animation when the user interacts with the scroller buttons?
     *
     * @param animateTabScrolling animateTabScrolling Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setAnimateTabScrolling(Boolean animateTabScrolling)  throws IllegalStateException {
        setAttribute("animateTabScrolling", animateTabScrolling, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller showTabScroller} is true, should tabs be scrolled
     * into view via an  animation when the user interacts with the scroller buttons?
     *
     *
     * @return Boolean
     */
    public Boolean getAnimateTabScrolling()  {
        return getAttributeAsBoolean("animateTabScrolling");
    }

    /**
     * Should tabs in this tabSet show an icon allowing the user to dismiss the tab by
     *  clicking on it directly. May be overridden for individual tabs by setting 
     *  {@link com.smartgwt.client.widgets.tab.Tab#getCanClose canClose}.
     *  <P>
     * The URL for this icon's image will be derived from  {@link com.smartgwt.client.widgets.tab.TabSet#getCloseTabIcon
     * closeTabIcon} by 
     * default, but may be overridden by explicitly specifying {@link com.smartgwt.client.widgets.tab.Tab#getCloseIcon
     * closeIcon}.
     *  <P>
     *  <b>Note</b>: Currently, tabs can only show a single icon, so a closable tab will show
     * the close icon only even if {@link com.smartgwt.client.widgets.tab.Tab#getIcon icon} is set.  To work around this, add
     * the icon
     *  as an HTML &lt;img&gt; tag to the {@link com.smartgwt.client.widgets.tab.Tab#getTitle title} property, for example:
     *  <pre>
     *     title : "<span>" + isc.Canvas.imgHTML("myIcon.png") + " Tab Title</span>"
     *  </pre>
     *
     * @param canCloseTabs canCloseTabs Default value is null
     * @see com.smartgwt.client.widgets.tab.TabSet#closeClick
     */
    public void setCanCloseTabs(Boolean canCloseTabs) {
        setAttribute("canCloseTabs", canCloseTabs, true);
    }

    /**
     * Should tabs in this tabSet show an icon allowing the user to dismiss the tab by
     *  clicking on it directly. May be overridden for individual tabs by setting 
     *  {@link com.smartgwt.client.widgets.tab.Tab#getCanClose canClose}.
     *  <P>
     * The URL for this icon's image will be derived from  {@link com.smartgwt.client.widgets.tab.TabSet#getCloseTabIcon
     * closeTabIcon} by 
     * default, but may be overridden by explicitly specifying {@link com.smartgwt.client.widgets.tab.Tab#getCloseIcon
     * closeIcon}.
     *  <P>
     *  <b>Note</b>: Currently, tabs can only show a single icon, so a closable tab will show
     * the close icon only even if {@link com.smartgwt.client.widgets.tab.Tab#getIcon icon} is set.  To work around this, add
     * the icon
     *  as an HTML &lt;img&gt; tag to the {@link com.smartgwt.client.widgets.tab.Tab#getTitle title} property, for example:
     *  <pre>
     *     title : "<span>" + isc.Canvas.imgHTML("myIcon.png") + " Tab Title</span>"
     *  </pre>
     *
     *
     * @return Boolean
     * @see com.smartgwt.client.widgets.tab.TabSet#closeClick
     */
    public Boolean getCanCloseTabs()  {
        return getAttributeAsBoolean("canCloseTabs");
    }

    /**
     * If true, users can edit the titles of tabs in this TabSet when the  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTitleEditEvent titleEditEvent} fires.  You can override this behavior per tab 
     * with the {@link com.smartgwt.client.widgets.tab.Tab#getCanEditTitle canEditTitle} property.
     *
     * @param canEditTabTitles canEditTabTitles Default value is false
     */
    public void setCanEditTabTitles(Boolean canEditTabTitles) {
        setAttribute("canEditTabTitles", canEditTabTitles, true);
    }

    /**
     * If true, users can edit the titles of tabs in this TabSet when the  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTitleEditEvent titleEditEvent} fires.  You can override this behavior per tab 
     * with the {@link com.smartgwt.client.widgets.tab.Tab#getCanEditTitle canEditTitle} property.
     *
     *
     * @return Boolean
     */
    public Boolean getCanEditTabTitles()  {
        return getAttributeAsBoolean("canEditTabTitles");
    }

    /**
     * Default src for the close icon for tabs to display if {@link com.smartgwt.client.widgets.tab.TabSet#getCanCloseTabs
     * canCloseTabs} is true.
     *
     * @param closeTabIcon closeTabIcon Default value is [SKIN]/TabSet/close.png
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setCloseTabIcon(String closeTabIcon)  throws IllegalStateException {
        setAttribute("closeTabIcon", closeTabIcon, false);
    }

    /**
     * Default src for the close icon for tabs to display if {@link com.smartgwt.client.widgets.tab.TabSet#getCanCloseTabs
     * canCloseTabs} is true.
     *
     *
     * @return String
     */
    public String getCloseTabIcon()  {
        return getAttributeAsString("closeTabIcon");
    }

    /**
     * Size in pixels of the icon for closing tabs, displayed when {@link
     * com.smartgwt.client.widgets.tab.TabSet#getCanCloseTabs canCloseTabs} is true.
     *
     * @param closeTabIconSize closeTabIconSize Default value is 16
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setCloseTabIconSize(int closeTabIconSize)  throws IllegalStateException {
        setAttribute("closeTabIconSize", closeTabIconSize, false);
    }

    /**
     * Size in pixels of the icon for closing tabs, displayed when {@link
     * com.smartgwt.client.widgets.tab.TabSet#getCanCloseTabs canCloseTabs} is true.
     *
     *
     * @return int
     */
    public int getCloseTabIconSize()  {
        return getAttributeAsInt("closeTabIconSize");
    }

    /**
     * Whether {@link com.smartgwt.client.widgets.Canvas#destroy destroy()} should be called on {@link
     * com.smartgwt.client.widgets.tab.Tab#getPane pane} when it a tab is removed via {@link
     * com.smartgwt.client.widgets.tab.TabSet#removeTab TabSet.removeTab}}.   <P> An application might set this to false in
     * order to re-use panes in different tabs or in different parts of the application.
     *
     * @param destroyPanes destroyPanes Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setDestroyPanes(Boolean destroyPanes)  throws IllegalStateException {
        setAttribute("destroyPanes", destroyPanes, false);
    }

    /**
     * Whether {@link com.smartgwt.client.widgets.Canvas#destroy destroy()} should be called on {@link
     * com.smartgwt.client.widgets.tab.Tab#getPane pane} when it a tab is removed via {@link
     * com.smartgwt.client.widgets.tab.TabSet#removeTab TabSet.removeTab}}.   <P> An application might set this to false in
     * order to re-use panes in different tabs or in different parts of the application.
     *
     *
     * @return Boolean
     */
    public Boolean getDestroyPanes()  {
        return getAttributeAsBoolean("destroyPanes");
    }

    /**
     * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()}, how should tabs
     * within this tabset be identified? By default if tab has a specified {@link com.smartgwt.client.widgets.tab.Tab#getID ID}
     * this will always be used. For tabs with no ID, the following options are available: <ul> <li><code>"title"</code> use
     * the title as an identifier</li> <li><code>"index"</code> use the index of the tab in the tabset as an identifier</li>
     * </ul>  If unset, and the tab has no specified ID, default behavior is to identify by title (if available), otherwise by
     * index.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param locateTabsBy locateTabsBy Default value is null
     */
    public void setLocateTabsBy(String locateTabsBy) {
        setAttribute("locateTabsBy", locateTabsBy, true);
    }

    /**
     * When  AutoTest.getElement is used to parse locator strings generated by link{isc.AutoTest.getLocator()}, how should tabs
     * within this tabset be identified? By default if tab has a specified {@link com.smartgwt.client.widgets.tab.Tab#getID ID}
     * this will always be used. For tabs with no ID, the following options are available: <ul> <li><code>"title"</code> use
     * the title as an identifier</li> <li><code>"index"</code> use the index of the tab in the tabset as an identifier</li>
     * </ul>  If unset, and the tab has no specified ID, default behavior is to identify by title (if available), otherwise by
     * index.
     *
     *
     * @return String
     */
    public String getLocateTabsBy()  {
        return getAttributeAsString("locateTabsBy");
    }

    /**
     * This property defines the number tab buttons that should be shown before automatically adding a "more" button to handle
     * the remaining tabs. This property is only used when {@link com.smartgwt.client.widgets.tab.TabSet#getShowMoreTab
     * showMoreTab} is enabled.
     *
     * @param moreTabCount moreTabCount Default value is 5
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setMoreTabCount(int moreTabCount)  throws IllegalStateException {
        setAttribute("moreTabCount", moreTabCount, false);
    }

    /**
     * This property defines the number tab buttons that should be shown before automatically adding a "more" button to handle
     * the remaining tabs. This property is only used when {@link com.smartgwt.client.widgets.tab.TabSet#getShowMoreTab
     * showMoreTab} is enabled.
     *
     *
     * @return int
     */
    public int getMoreTabCount()  {
        return getAttributeAsInt("moreTabCount");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowMoreTab showMoreTab} is enabled this property determines the
     * image to display on the "More" tab button.
     *
     * @param moreTabImage moreTabImage Default value is "[SKINIMG]/iOS/more.png"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setMoreTabImage(String moreTabImage)  throws IllegalStateException {
        setAttribute("moreTabImage", moreTabImage, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowMoreTab showMoreTab} is enabled this property determines the
     * image to display on the "More" tab button.
     *
     *
     * @return String
     */
    public String getMoreTabImage()  {
        return getAttributeAsString("moreTabImage");
    }

    /**
     * Title for the "More" tab.
     *
     * @param moreTabTitle moreTabTitle Default value is "More"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setMoreTabTitle(String moreTabTitle)  throws IllegalStateException {
        setAttribute("moreTabTitle", moreTabTitle, false);
    }

    /**
     * Title for the "More" tab.
     *
     *
     * @return String
     */
    public String getMoreTabTitle()  {
        return getAttributeAsString("moreTabTitle");
    }

    /**
     * CSS style used for the paneContainer.
     *
     * @param paneContainerClassName paneContainerClassName Default value is null
     */
    public void setPaneContainerClassName(String paneContainerClassName) {
        setAttribute("paneContainerClassName", paneContainerClassName, true);
    }

    /**
     * CSS style used for the paneContainer.
     *
     *
     * @return String
     */
    public String getPaneContainerClassName()  {
        return getAttributeAsString("paneContainerClassName");
    }

    /**
     * Space to leave around the panes in our paneContainer
     *
     * @param paneMargin paneMargin Default value is 0
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setPaneMargin(int paneMargin)  throws IllegalStateException {
        setAttribute("paneMargin", paneMargin, false);
    }

    /**
     * Space to leave around the panes in our paneContainer
     *
     *
     * @return int
     */
    public int getPaneMargin()  {
        return getAttributeAsInt("paneMargin");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton symmetricPickerButton} is  set to true, this property
     * governs the base URL for the picker button image, when displayed in a horizontal tab-bar [IE {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} is set to <code>"top"</code> or
     * <code>"bottom"</code>]. <P> Note that if <code>symmetricPickerButton</code> is false, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property will be used instead. <P> This base
     * URL will have a suffix of <code>"Down"</code> appended when the user holds the mouse down over the button, and
     * <code>"Disabled"</code> if the tabset as a whole is  disabled.
     *
     * @param pickerButtonHSrc pickerButtonHSrc Default value is "[SKIN]hpicker.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricPickerButton
     */
    public void setPickerButtonHSrc(String pickerButtonHSrc)  throws IllegalStateException {
        setAttribute("pickerButtonHSrc", pickerButtonHSrc, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton symmetricPickerButton} is  set to true, this property
     * governs the base URL for the picker button image, when displayed in a horizontal tab-bar [IE {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} is set to <code>"top"</code> or
     * <code>"bottom"</code>]. <P> Note that if <code>symmetricPickerButton</code> is false, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property will be used instead. <P> This base
     * URL will have a suffix of <code>"Down"</code> appended when the user holds the mouse down over the button, and
     * <code>"Disabled"</code> if the tabset as a whole is  disabled.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton
     */
    public String getPickerButtonHSrc()  {
        return getAttributeAsString("pickerButtonHSrc");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, this property governs the size
     * of tab-picker button. Applied as the width of buttons if the tabBar is horizontal, or the height if tabBar is vertical.
     * Note that the other dimension is determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness
     * this.tabBarThickness}
     *
     * @param pickerButtonSize pickerButtonSize Default value is 16
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setPickerButtonSize(int pickerButtonSize)  throws IllegalStateException {
        setAttribute("pickerButtonSize", pickerButtonSize, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, this property governs the size
     * of tab-picker button. Applied as the width of buttons if the tabBar is horizontal, or the height if tabBar is vertical.
     * Note that the other dimension is determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness
     * this.tabBarThickness}
     *
     *
     * @return int
     */
    public int getPickerButtonSize()  {
        return getAttributeAsInt("pickerButtonSize");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, this property governs the base
     * URL for the picker button image, when {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton
     * symmetricPickerButton} is set to false <P> Note that if <code>symmetricPickerButton</code> is true, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonHSrc pickerButtonHSrc}  and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonVSrc pickerButtonVSrc} properties will be used instead. <P> To get
     * the path to the image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>The {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} for this tabSet will be appended.</li> </ul>
     *
     * @param pickerButtonSrc pickerButtonSrc Default value is "[SKIN]/picker.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricPickerButton
     */
    public void setPickerButtonSrc(String pickerButtonSrc)  throws IllegalStateException {
        setAttribute("pickerButtonSrc", pickerButtonSrc, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, this property governs the base
     * URL for the picker button image, when {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton
     * symmetricPickerButton} is set to false <P> Note that if <code>symmetricPickerButton</code> is true, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonHSrc pickerButtonHSrc}  and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonVSrc pickerButtonVSrc} properties will be used instead. <P> To get
     * the path to the image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>The {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} for this tabSet will be appended.</li> </ul>
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton
     */
    public String getPickerButtonSrc()  {
        return getAttributeAsString("pickerButtonSrc");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton symmetricPickerButton} is  set to true, this property
     * governs the base URL for the picker button image, when displayed in a verricaL tab-bar [IE {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} is set to <code>"LEFT"</code> or
     * <code>"right"</code>]. <P> Note that if <code>symmetricPickerButton</code> is false, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property will be used instead. <P> This base
     * URL will have a suffix of <code>"Down"</code> appended when the user holds the mouse down over the button, and
     * <code>"Disabled"</code> if the tabset as a whole is  disabled.
     *
     * @param pickerButtonVSrc pickerButtonVSrc Default value is "[SKIN]vpicker.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricPickerButton
     */
    public void setPickerButtonVSrc(String pickerButtonVSrc)  throws IllegalStateException {
        setAttribute("pickerButtonVSrc", pickerButtonVSrc, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker showTabPicker} is true, and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton symmetricPickerButton} is  set to true, this property
     * governs the base URL for the picker button image, when displayed in a verricaL tab-bar [IE {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} is set to <code>"LEFT"</code> or
     * <code>"right"</code>]. <P> Note that if <code>symmetricPickerButton</code> is false, the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property will be used instead. <P> This base
     * URL will have a suffix of <code>"Down"</code> appended when the user holds the mouse down over the button, and
     * <code>"Disabled"</code> if the tabset as a whole is  disabled.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricPickerButton
     */
    public String getPickerButtonVSrc()  {
        return getAttributeAsString("pickerButtonVSrc");
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller showTabScroller} is true, this property governs the
     * size of scroller buttons. Applied as the width of buttons if the tabBar is horizontal, or the height if tabBar is
     * vertical. Note that the other dimension is determined by  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness this.tabBarThickness}
     *
     * @param scrollerButtonSize scrollerButtonSize Default value is 16
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setScrollerButtonSize(int scrollerButtonSize)  throws IllegalStateException {
        setAttribute("scrollerButtonSize", scrollerButtonSize, false);
    }

    /**
     * If {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller showTabScroller} is true, this property governs the
     * size of scroller buttons. Applied as the width of buttons if the tabBar is horizontal, or the height if tabBar is
     * vertical. Note that the other dimension is determined by  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness this.tabBarThickness}
     *
     *
     * @return int
     */
    public int getScrollerButtonSize()  {
        return getAttributeAsInt("scrollerButtonSize");
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true, this property governs the
     * base URL for the tab bar back and forward scroller button images for horizontal tab bars [IE for tab sets with {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} set to "top" or "bottom"]. <P> Note that if
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>A suffix of <code>"forward"</code> or
     * <code>"back"</code> will be appended for the     forward or backward scrolling button.</li> </ul> For example - if the
     * scrollerHSrc is set to <code>"[SKIN]hscroll.gif"</code>, the image displayed for the back-scroller button on a tabSet
     * with <code>tabBarPosition</code> set to "top" and <code>symmetricScroller</code> set to true would be one of 
     * <code>"[SKIN]hscroll_back.gif"</code>, <code>"[SKIN]hscroll_Down_back.gif"</code>, and
     * <code>"[SKIN]hscroll_Disabled_back.gif"</code>. <P> Note that for best results the media should be sized to match the
     * scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness tabBarThickness}
     * and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     * @param scrollerHSrc scrollerHSrc Default value is "[SKIN]hscroll.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricScroller
     */
    public void setScrollerHSrc(String scrollerHSrc)  throws IllegalStateException {
        setAttribute("scrollerHSrc", scrollerHSrc, false);
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true, this property governs the
     * base URL for the tab bar back and forward scroller button images for horizontal tab bars [IE for tab sets with {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} set to "top" or "bottom"]. <P> Note that if
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>A suffix of <code>"forward"</code> or
     * <code>"back"</code> will be appended for the     forward or backward scrolling button.</li> </ul> For example - if the
     * scrollerHSrc is set to <code>"[SKIN]hscroll.gif"</code>, the image displayed for the back-scroller button on a tabSet
     * with <code>tabBarPosition</code> set to "top" and <code>symmetricScroller</code> set to true would be one of 
     * <code>"[SKIN]hscroll_back.gif"</code>, <code>"[SKIN]hscroll_Down_back.gif"</code>, and
     * <code>"[SKIN]hscroll_Disabled_back.gif"</code>. <P> Note that for best results the media should be sized to match the
     * scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness tabBarThickness}
     * and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller
     */
    public String getScrollerHSrc()  {
        return getAttributeAsString("scrollerHSrc");
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false, this property governs
     * the base URL for the tab bar back and forward scroller button images. <P> Note that if {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerHSrc scrollerHSrc} and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerVSrc scrollerVSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>The {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} for this tabSet will be appended.</li> <li>A
     * suffix of <code>"forward"</code> or <code>"back"</code> will be appended for the     forward or backward scrolling
     * button.</li> </ul> For example - if the scrollerSrc is set to <code>"[SKIN]scroll.gif"</code>, the image displayed for
     * the back-scroller button on a tabSet with <code>tabBarPosition</code> set to "top" and <code>symmetricScroller</code>
     * set to false would be one of  <code>"[SKIN]scroll_top_back.gif"</code>, <code>"[SKIN]scroll_Down_top_back.gif"</code>,
     * and <code>"[SKIN]scroll_Disabled_top_back.gif"</code>. <P> Note that for best results the media should be sized to match
     * the scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness
     * tabBarThickness} and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     * @param scrollerSrc scrollerSrc Default value is "[SKIN]/scroll.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricScroller
     */
    public void setScrollerSrc(String scrollerSrc)  throws IllegalStateException {
        setAttribute("scrollerSrc", scrollerSrc, false);
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false, this property governs
     * the base URL for the tab bar back and forward scroller button images. <P> Note that if {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerHSrc scrollerHSrc} and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerVSrc scrollerVSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>The {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} for this tabSet will be appended.</li> <li>A
     * suffix of <code>"forward"</code> or <code>"back"</code> will be appended for the     forward or backward scrolling
     * button.</li> </ul> For example - if the scrollerSrc is set to <code>"[SKIN]scroll.gif"</code>, the image displayed for
     * the back-scroller button on a tabSet with <code>tabBarPosition</code> set to "top" and <code>symmetricScroller</code>
     * set to false would be one of  <code>"[SKIN]scroll_top_back.gif"</code>, <code>"[SKIN]scroll_Down_top_back.gif"</code>,
     * and <code>"[SKIN]scroll_Disabled_top_back.gif"</code>. <P> Note that for best results the media should be sized to match
     * the scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness
     * tabBarThickness} and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller
     */
    public String getScrollerSrc()  {
        return getAttributeAsString("scrollerSrc");
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true, this property governs the
     * base URL for the tab bar back and forward scroller button images for vertical tab bars [IE for tab sets with {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} set to "left" or "right"]. <P> Note that if
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>A suffix of <code>"forward"</code> or
     * <code>"back"</code> will be appended for the     forward or backward scrolling button.</li> </ul> For example - if the
     * scrollerVSrc is set to <code>"[SKIN]vscroll.gif"</code>, the image displayed for the back-scroller button on a tabSet
     * with <code>tabBarPosition</code> set to "left" and <code>symmetricScroller</code> set to true would be one of 
     * <code>"[SKIN]vscroll_back.gif"</code>, <code>"[SKIN]vscroll_Down_back.gif"</code>, and
     * <code>"[SKIN]vscroll_Disabled_back.gif"</code>. <P> Note that for best results the media should be sized to match the
     * scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness tabBarThickness}
     * and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     * @param scrollerVSrc scrollerVSrc Default value is "[SKIN]vscroll.gif"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.widgets.tab.TabSet#setSymmetricScroller
     */
    public void setScrollerVSrc(String scrollerVSrc)  throws IllegalStateException {
        setAttribute("scrollerVSrc", scrollerVSrc, false);
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, and 
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is true, this property governs the
     * base URL for the tab bar back and forward scroller button images for vertical tab bars [IE for tab sets with {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} set to "left" or "right"]. <P> Note that if
     * {@link com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller symmetricScroller} is false,  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} will be used instead. <P> To get the path to the
     * image to display, this base URL will be modified as follows: <ul> <li>If appropriate a state suffix of
     * <code>"Down"</code> or <code>"Disabled"</code> will be     appended.</li> <li>A suffix of <code>"forward"</code> or
     * <code>"back"</code> will be appended for the     forward or backward scrolling button.</li> </ul> For example - if the
     * scrollerVSrc is set to <code>"[SKIN]vscroll.gif"</code>, the image displayed for the back-scroller button on a tabSet
     * with <code>tabBarPosition</code> set to "left" and <code>symmetricScroller</code> set to true would be one of 
     * <code>"[SKIN]vscroll_back.gif"</code>, <code>"[SKIN]vscroll_Down_back.gif"</code>, and
     * <code>"[SKIN]vscroll_Disabled_back.gif"</code>. <P> Note that for best results the media should be sized to match the
     * scroller button sizes,  determined by {@link com.smartgwt.client.widgets.tab.TabSet#getTabBarThickness tabBarThickness}
     * and {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerButtonSize scrollerButtonSize}.
     *
     *
     * @return String
     * @see com.smartgwt.client.widgets.tab.TabSet#getSymmetricScroller
     */
    public String getScrollerVSrc()  {
        return getAttributeAsString("scrollerVSrc");
    }

    /**
     * Should tabs exceeding {@link com.smartgwt.client.widgets.tab.TabSet#getMoreTabCount moreTabCount} be shown on a "more"
     * tab? <p> This setting is used to emulate an iPhone-style tab bar "more" button.
     *
     * @param showMoreTab showMoreTab Default value is null
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setShowMoreTab(Boolean showMoreTab)  throws IllegalStateException {
        setAttribute("showMoreTab", showMoreTab, false);
    }

    /**
     * Should tabs exceeding {@link com.smartgwt.client.widgets.tab.TabSet#getMoreTabCount moreTabCount} be shown on a "more"
     * tab? <p> This setting is used to emulate an iPhone-style tab bar "more" button.
     *
     *
     * @return Boolean
     */
    public Boolean getShowMoreTab()  {
        return getAttributeAsBoolean("showMoreTab");
    }

    /**
     * Should the paneContainer for this tabset show {@link com.smartgwt.client.widgets.Canvas#getShowEdges edges}.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param showPaneContainerEdges showPaneContainerEdges Default value is null
     */
    public void setShowPaneContainerEdges(Boolean showPaneContainerEdges) {
        setAttribute("showPaneContainerEdges", showPaneContainerEdges, true);
    }

    /**
     * Should the paneContainer for this tabset show {@link com.smartgwt.client.widgets.Canvas#getShowEdges edges}.
     *
     *
     * @return Boolean
     */
    public Boolean getShowPaneContainerEdges()  {
        return getAttributeAsBoolean("showPaneContainerEdges");
    }

    /**
     * If the paneContainer for this tab set is showing {@link com.smartgwt.client.widgets.Canvas#getShowEdges edges}, setting
     * this attribute to <code>true</code> will set the paneContainer to show {@link
     * com.smartgwt.client.widgets.Canvas#getCustomEdges customEdges} for the three sides opposing the tabBarPosition.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param showPartialEdges showPartialEdges Default value is false
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setShowPartialEdges(Boolean showPartialEdges)  throws IllegalStateException {
        setAttribute("showPartialEdges", showPartialEdges, false);
    }

    /**
     * If the paneContainer for this tab set is showing {@link com.smartgwt.client.widgets.Canvas#getShowEdges edges}, setting
     * this attribute to <code>true</code> will set the paneContainer to show {@link
     * com.smartgwt.client.widgets.Canvas#getCustomEdges customEdges} for the three sides opposing the tabBarPosition.
     *
     *
     * @return Boolean
     */
    public Boolean getShowPartialEdges()  {
        return getAttributeAsBoolean("showPartialEdges");
    }

    /**
     * If there is not enough space to display all the tab-buttons in this tabSet, should a drop-down "picker" be displayed to
     * allow selection of tabs that are clipped?
     *
     * @param showTabPicker showTabPicker Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setShowTabPicker(Boolean showTabPicker)  throws IllegalStateException {
        setAttribute("showTabPicker", showTabPicker, false);
    }

    /**
     * If there is not enough space to display all the tab-buttons in this tabSet, should a drop-down "picker" be displayed to
     * allow selection of tabs that are clipped?
     *
     *
     * @return Boolean
     */
    public Boolean getShowTabPicker()  {
        return getAttributeAsBoolean("showTabPicker");
    }

    /**
     * If there is not enough space to display all the tab-buttons in this tabSet, should  scroller buttons be displayed to
     * allow access to tabs that are clipped?
     *
     * @param showTabScroller showTabScroller Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setShowTabScroller(Boolean showTabScroller)  throws IllegalStateException {
        setAttribute("showTabScroller", showTabScroller, false);
    }

    /**
     * If there is not enough space to display all the tab-buttons in this tabSet, should  scroller buttons be displayed to
     * allow access to tabs that are clipped?
     *
     *
     * @return Boolean
     */
    public Boolean getShowTabScroller()  {
        return getAttributeAsBoolean("showTabScroller");
    }

    /**
     * If this.useSimpleTabs is true, simpleTabBaseClass will be the base style used to   determine the css style to apply to
     * the tabs.<br>  This property will be suffixed with the side on which the tab-bar will appear, followed  by with the
     * tab's state (selected, over, etc), resolving to a className like   "tabButtonTopOver"
     *
     * @param simpleTabBaseStyle simpleTabBaseStyle Default value is "tabButton"
     */
    public void setSimpleTabBaseStyle(String simpleTabBaseStyle) {
        setAttribute("simpleTabBaseStyle", simpleTabBaseStyle, true);
    }

    /**
     * If this.useSimpleTabs is true, simpleTabBaseClass will be the base style used to   determine the css style to apply to
     * the tabs.<br>  This property will be suffixed with the side on which the tab-bar will appear, followed  by with the
     * tab's state (selected, over, etc), resolving to a className like   "tabButtonTopOver"
     *
     *
     * @return String
     */
    public String getSimpleTabBaseStyle()  {
        return getAttributeAsString("simpleTabBaseStyle");
    }

    /**
     * Default directory for skin images (those defined by the class), relative to the Page-wide {@link
     * com.smartgwt.client.util.Page#getSkinDir skinDir}.
     *
     * @param skinImgDir skinImgDir Default value is "images/TabSet/"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see com.smartgwt.client.docs.Images Images overview and related methods
     */
    public void setSkinImgDir(String skinImgDir)  throws IllegalStateException {
        setAttribute("skinImgDir", skinImgDir, false);
    }

    /**
     * Default directory for skin images (those defined by the class), relative to the Page-wide {@link
     * com.smartgwt.client.util.Page#getSkinDir skinDir}.
     *
     *
     * @return String
     * @see com.smartgwt.client.docs.Images Images overview and related methods
     */
    public String getSkinImgDir()  {
        return getAttributeAsString("skinImgDir");
    }

    /**
     * If this tabSet will {@link com.smartgwt.client.widgets.tab.TabSet#getShowPaneContainerEdges show edges} for the
     * paneContainer, this property determines whether the same edge media will be used regardless of the tab bar position, or
     * whether different media should be used (necessary if the edge appearance is not symmetrical on all sides). <P> If this
     * property is set to false the paneContainer edge image URLs will be prefixed with the tabBarPosition of the tabSet - for
     * example <code>"[SKIN]edge_top_T.gif"</code> rather than just <code>"[SKIN]edge_T.gif"</code>. <P> When
     * <code>symmetricEdges</code> is false, custom edge sizes for the pane container may be specified via {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTopEdgeSizes topEdgeSizes} et al, and custom edge offsets via  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTopEdgeOffsets topEdgeOffsets} et al.
     *
     * @param symmetricEdges symmetricEdges Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setSymmetricEdges(Boolean symmetricEdges)  throws IllegalStateException {
        setAttribute("symmetricEdges", symmetricEdges, false);
    }

    /**
     * If this tabSet will {@link com.smartgwt.client.widgets.tab.TabSet#getShowPaneContainerEdges show edges} for the
     * paneContainer, this property determines whether the same edge media will be used regardless of the tab bar position, or
     * whether different media should be used (necessary if the edge appearance is not symmetrical on all sides). <P> If this
     * property is set to false the paneContainer edge image URLs will be prefixed with the tabBarPosition of the tabSet - for
     * example <code>"[SKIN]edge_top_T.gif"</code> rather than just <code>"[SKIN]edge_T.gif"</code>. <P> When
     * <code>symmetricEdges</code> is false, custom edge sizes for the pane container may be specified via {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTopEdgeSizes topEdgeSizes} et al, and custom edge offsets via  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTopEdgeOffsets topEdgeOffsets} et al.
     *
     *
     * @return Boolean
     */
    public Boolean getSymmetricEdges()  {
        return getAttributeAsBoolean("symmetricEdges");
    }

    /**
     * If this TabSet is showing a {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker tab picker button}, this
     * property determines whether the {@link com.smartgwt.client.widgets.tab.TabSet#getPickerButtonHSrc pickerButtonHSrc} and
     * {@link com.smartgwt.client.widgets.tab.TabSet#getPickerButtonVSrc pickerButtonVSrc} media will be used for vertical and
     * horizontal tab-bar picker buttons, or whether separate media should be used for each possible  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} based on the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property  for this tabSet.
     *
     * @param symmetricPickerButton symmetricPickerButton Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setSymmetricPickerButton(Boolean symmetricPickerButton)  throws IllegalStateException {
        setAttribute("symmetricPickerButton", symmetricPickerButton, false);
    }

    /**
     * If this TabSet is showing a {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabPicker tab picker button}, this
     * property determines whether the {@link com.smartgwt.client.widgets.tab.TabSet#getPickerButtonHSrc pickerButtonHSrc} and
     * {@link com.smartgwt.client.widgets.tab.TabSet#getPickerButtonVSrc pickerButtonVSrc} media will be used for vertical and
     * horizontal tab-bar picker buttons, or whether separate media should be used for each possible  {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} based on the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getPickerButtonSrc pickerButtonSrc} property  for this tabSet.
     *
     *
     * @return Boolean
     */
    public Boolean getSymmetricPickerButton()  {
        return getAttributeAsBoolean("symmetricPickerButton");
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, this
     * property  determines whether the {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerHSrc scrollerHSrc} and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerVSrc scrollerVSrc} media will be used for vertical and horizontal
     * tab-bar scroller buttons, or whether separate media should be used for each possible {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} based on the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} property for this tabSet.
     *
     * @param symmetricScroller symmetricScroller Default value is true
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setSymmetricScroller(Boolean symmetricScroller)  throws IllegalStateException {
        setAttribute("symmetricScroller", symmetricScroller, false);
    }

    /**
     * If this TabSet is showing {@link com.smartgwt.client.widgets.tab.TabSet#getShowTabScroller tab scroller buttons}, this
     * property  determines whether the {@link com.smartgwt.client.widgets.tab.TabSet#getScrollerHSrc scrollerHSrc} and {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerVSrc scrollerVSrc} media will be used for vertical and horizontal
     * tab-bar scroller buttons, or whether separate media should be used for each possible {@link
     * com.smartgwt.client.widgets.tab.TabSet#getTabBarPosition tabBarPosition} based on the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getScrollerSrc scrollerSrc} property for this tabSet.
     *
     *
     * @return Boolean
     */
    public Boolean getSymmetricScroller()  {
        return getAttributeAsBoolean("symmetricScroller");
    }

    /**
     * Alignment of the tabBar. <P> If the position of the tabBar is "top" or "bottom", then alignment must be "left" or
     * "right" and defaults to "left". <P> If the position of the tabBar is "left" or "right", then the alignment must be "top"
     * or "bottom" and defaults to "top".
     *
     * @param tabBarAlign tabBarAlign Default value is see below
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_tabs_align" target="examples">Align Example</a>
     */
    public void setTabBarAlign(Side tabBarAlign)  throws IllegalStateException {
        setAttribute("tabBarAlign", tabBarAlign.getValue(), false);
    }

    /**
     * Alignment of the tabBar. <P> If the position of the tabBar is "top" or "bottom", then alignment must be "left" or
     * "right" and defaults to "left". <P> If the position of the tabBar is "left" or "right", then the alignment must be "top"
     * or "bottom" and defaults to "top".
     *
     *
     * @return Side
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_tabs_align" target="examples">Align Example</a>
     */
    public Side getTabBarAlign()  {
        return EnumUtil.getEnum(Side.values(), getAttribute("tabBarAlign"));
    }

    /**
     * Which side of the TabSet the TabBar should appear on.
     *
     * @param tabBarPosition tabBarPosition Default value is Canvas.TOP
     * @throws IllegalStateException this property cannot be changed after the component has been created
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_tabs_orientation" target="examples">Orientation Example</a>
     */
    public void setTabBarPosition(Side tabBarPosition)  throws IllegalStateException {
        setAttribute("tabBarPosition", tabBarPosition.getValue(), false);
    }

    /**
     * Which side of the TabSet the TabBar should appear on.
     *
     *
     * @return Side
     * @see <a href="http://www.smartclient.com/smartgwt/showcase/#layout_tabs_orientation" target="examples">Orientation Example</a>
     */
    public Side getTabBarPosition()  {
        return EnumUtil.getEnum(Side.values(), getAttribute("tabBarPosition"));
    }

    /**
     * Thickness of tabBar, applies to either orientation (specifies height for horizontal, width for vertical orientation). 
     * Note that overriding this value for TabSets that are skinned with images generally means providing new media for the
     * borders.
     *
     * @param tabBarThickness tabBarThickness Default value is 21
     */
    public void setTabBarThickness(int tabBarThickness) {
        setAttribute("tabBarThickness", tabBarThickness, true);
    }

    /**
     * Thickness of tabBar, applies to either orientation (specifies height for horizontal, width for vertical orientation). 
     * Note that overriding this value for TabSets that are skinned with images generally means providing new media for the
     * borders.
     *
     *
     * @return int
     */
    public int getTabBarThickness()  {
        return getAttributeAsInt("tabBarThickness");
    }

    /**
     * If set, offsets the tab title editor further in from the left-hand edge of the tab, by the number of pixels set in this
     * property.  Note that the editor is always offset to avoid overlapping the endcaps of the tab; this property is applied
     * on top of that  default offset.
     *
     * @param titleEditorLeftOffset titleEditorLeftOffset Default value is null
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorRightOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorTopOffset
     */
    public void setTitleEditorLeftOffset(Integer titleEditorLeftOffset) {
        setAttribute("titleEditorLeftOffset", titleEditorLeftOffset, true);
    }

    /**
     * If set, offsets the tab title editor further in from the left-hand edge of the tab, by the number of pixels set in this
     * property.  Note that the editor is always offset to avoid overlapping the endcaps of the tab; this property is applied
     * on top of that  default offset.
     *
     *
     * @return Integer
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorRightOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorTopOffset
     */
    public Integer getTitleEditorLeftOffset()  {
        return getAttributeAsInt("titleEditorLeftOffset");
    }

    /**
     * If set, offsets the tab title editor further in from the right-hand edge of the tab, by the number of pixels set in this
     * property.  Note that the editor is always offset to avoid overlapping the endcaps of the tab; this property is applied
     * on top of that  default offset.
     *
     * @param titleEditorRightOffset titleEditorRightOffset Default value is null
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorLeftOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorTopOffset
     */
    public void setTitleEditorRightOffset(Integer titleEditorRightOffset) {
        setAttribute("titleEditorRightOffset", titleEditorRightOffset, true);
    }

    /**
     * If set, offsets the tab title editor further in from the right-hand edge of the tab, by the number of pixels set in this
     * property.  Note that the editor is always offset to avoid overlapping the endcaps of the tab; this property is applied
     * on top of that  default offset.
     *
     *
     * @return Integer
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorLeftOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorTopOffset
     */
    public Integer getTitleEditorRightOffset()  {
        return getAttributeAsInt("titleEditorRightOffset");
    }

    /**
     * If set, offsets the tab title editor further down from the top edge of the tab, by the number of pixels set in this
     * property.  You can use this property, together with the  left and right offset properties, to fine tune positioning of
     * the editor within or  around the tab button.<p> <b>Note:</b> The height of the editor is an attribute of the editor
     * itself, and can be set by specifying a "height" property in {@link com.smartgwt.client.widgets.tab.TabSet#getTitleEditor
     * titleEditorDefaults}.
     *
     * @param titleEditorTopOffset titleEditorTopOffset Default value is null
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorLeftOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#setTitleEditorRightOffset
     */
    public void setTitleEditorTopOffset(Integer titleEditorTopOffset) {
        setAttribute("titleEditorTopOffset", titleEditorTopOffset, true);
    }

    /**
     * If set, offsets the tab title editor further down from the top edge of the tab, by the number of pixels set in this
     * property.  You can use this property, together with the  left and right offset properties, to fine tune positioning of
     * the editor within or  around the tab button.<p> <b>Note:</b> The height of the editor is an attribute of the editor
     * itself, and can be set by specifying a "height" property in {@link com.smartgwt.client.widgets.tab.TabSet#getTitleEditor
     * titleEditorDefaults}.
     *
     *
     * @return Integer
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorLeftOffset
     * @see com.smartgwt.client.widgets.tab.TabSet#getTitleEditorRightOffset
     */
    public Integer getTitleEditorTopOffset()  {
        return getAttributeAsInt("titleEditorTopOffset");
    }

    /**
     * Should we use simple button based tabs styled with CSS rather than image based {@link
     * com.smartgwt.client.widgets.tab.ImgTab} tabs? <P>   If set to true tabs will instances of {@link
     * com.smartgwt.client.widgets.Button}, styled according to the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSimpleTabBaseStyle simpleTabBaseStyle}.
     * <p><b>Note : </b> This is an advanced setting</p>
     *
     * @param useSimpleTabs useSimpleTabs Default value is false
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setUseSimpleTabs(Boolean useSimpleTabs)  throws IllegalStateException {
        setAttribute("useSimpleTabs", useSimpleTabs, false);
    }

    /**
     * Should we use simple button based tabs styled with CSS rather than image based {@link
     * com.smartgwt.client.widgets.tab.ImgTab} tabs? <P>   If set to true tabs will instances of {@link
     * com.smartgwt.client.widgets.Button}, styled according to the {@link
     * com.smartgwt.client.widgets.tab.TabSet#getSimpleTabBaseStyle simpleTabBaseStyle}.
     *
     *
     * @return Boolean
     */
    public Boolean getUseSimpleTabs()  {
        return getAttributeAsBoolean("useSimpleTabs");
    }

    /**
    * Specifies the index of the initially selected tab.
    *
    * @param selectedTab selectedTab Default value is 0
    */
    public void setSelectedTab(int selectedTab) {
        setAttribute("selectedTab", selectedTab, true);
    }

    public void setTabs(Tab... tabs) {
        for (Tab tab : tabs) {
            addTab(tab);
        }
    }

    /**
     * Properties of the container where the component specified by Tab.pane is shown.
     *
     * @param paneContainerProperties the pane container properties
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setPaneContainerProperties(Canvas paneContainerProperties) {
        setAttribute("paneContainerProperties", paneContainerProperties.getConfig(), false);
    }

	/**
	 * Select a tab.
	 *
	 * @param tabIndex the tab index
	 */
	public void selectTab(int tabIndex) {
		client.call("selectTab", tabIndex);
	}

	/**
	 * Remove a tab. <P> The pane associated with the removed tab is automatically destroyed when you call this method.
	 * To avoid this, call {@link com.smartgwt.client.widgets.tab.TabSet#updateTab} with <code>null</code> as the new
	 * pane immediately before removing the tab.
	 *
	 * @param tab the tab
	 */
	public void removeTab(Tab tab) {
		paintableTabs.remove(tab);
		requestRepaint();
	}

	/**
	 * Add a tab
	 *
	 * @param tab new tab
	 */
	public void addTab(Tab tab) {
		tab.setTabSet(this);
		paintableTabs.add(tab);
	}

	/**
	 * Add a tab
	 *
	 * @param tab new tab
	 * @param position the position where tab should be added
	 */
	public void addTab(Tab tab, int position) {
		tab.setTabSet(this);
		paintableTabs.add(position, tab);
	}

	/**
	 * The tabs
	 *
	 * @return the tabs
	 */
	public Tab[] getTabs() {
		return paintableTabs.toArray(new Tab[0]);
	}

	@Override
	@VaadinIntegration
	public void paintContent(PaintTarget target) throws PaintException {
		propertyPainter.paintContent(target);
		super.paintContent(target);
		client.paintContent(target);
	}

	@Override
	@VaadinIntegration
	public void changeVariables(final Object source, final Map variables) {
		super.changeVariables(source, variables);
		client.changeVariables(source, variables);
	}

	private class ServerSideHandlerImpl implements ServerSideHandler {
		@Override
		public Object[] initRequestFromClient() {
			return new Object[0];
		}

		@Override
		public void callFromClient(String method, Object[] params) {

		}

		@Override
		public void requestRepaint() {
			TabSet.this.requestRepaint();
		}
	}
}
