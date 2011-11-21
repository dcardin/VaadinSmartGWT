package org.vaadin.smartgwt.server.toolbar;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.FormItem;
import org.vaadin.smartgwt.server.layout.Layout;
import org.vaadin.smartgwt.server.layout.LayoutSpacer;

import com.google.gwt.core.client.JavaScriptObject;

public class ToolStrip extends Layout
{

	/**
	 * Customized resizeBar with typical appearance for a ToolStrip
	 * 
	 * @param resizeBarClass
	 *            resizeBarClass Default value is "ToolStripResizer"
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 */
	public void setResizeBarClass(String resizeBarClass) throws IllegalStateException
	{
		setAttribute("resizeBarClass", resizeBarClass, false);
	}

	/**
	 * Customized resizeBar with typical appearance for a ToolStrip
	 * 
	 * 
	 * @return String
	 */
	public String getResizeBarClass()
	{
		return getAttributeAsString("resizeBarClass");
	}

	/**
	 * Thickness of the resizeBars in pixels
	 * <p>
	 * <b>Note : </b> This is an advanced setting
	 * </p>
	 * 
	 * @param resizeBarSize
	 *            resizeBarSize Default value is 14
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 */
	public void setResizeBarSize(int resizeBarSize) throws IllegalStateException
	{
		setAttribute("resizeBarSize", resizeBarSize, false);
	}

	/**
	 * Thickness of the resizeBars in pixels
	 * 
	 * 
	 * @return int
	 */
	public int getResizeBarSize()
	{
		return getAttributeAsInt("resizeBarSize");
	}

	/**
	 * Class to create when the string "separator" appears in {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getMembers members}.
	 * 
	 * @param separatorClass
	 *            separatorClass Default value is "ToolStripSeparator"
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 */
	public void setSeparatorClass(String separatorClass) throws IllegalStateException
	{
		setAttribute("separatorClass", separatorClass, false);
	}

	/**
	 * Class to create when the string "separator" appears in {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getMembers members}.
	 * 
	 * 
	 * @return String
	 */
	public String getSeparatorClass()
	{
		return getAttributeAsString("separatorClass");
	}

	/**
	 * Separator thickness in pixels
	 * 
	 * @param separatorSize
	 *            separatorSize Default value is 8
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 */
	public void setSeparatorSize(int separatorSize) throws IllegalStateException
	{
		setAttribute("separatorSize", separatorSize, false);
	}

	/**
	 * Separator thickness in pixels
	 * 
	 * 
	 * @return int
	 */
	public int getSeparatorSize()
	{
		return getAttributeAsInt("separatorSize");
	}

	/**
	 * Indicates whether the components are drawn horizontally from left to right (false), or vertically from top to bottom (true).
	 * 
	 * @param vertical
	 *            vertical Default value is false
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setVertical(Boolean vertical) throws IllegalStateException
	{
		setAttribute("vertical", vertical, false);
	}

	/**
	 * Indicates whether the components are drawn horizontally from left to right (false), or vertically from top to bottom (true).
	 * 
	 * 
	 * @return Boolean
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public Boolean getVertical()
	{
		return getAttributeAsBoolean("vertical");
	}

	/**
	 * Default stylename to use if {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getVertical this.vertical} is true. If unset, the standard
	 * {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getStyleName styleName} will be used for both vertical and horizontal toolstrips.
	 * <P>
	 * Note that this property only applies to the widget at init time. To modify the styleName after this widget has been initialized, you should simply call
	 * {@link com.smartgwt.client.widgets.Canvas#setStyleName setStyleName()} rather than updating this property.
	 * 
	 * @param verticalStyleName
	 *            verticalStyleName Default value is null
	 * @throws IllegalStateException
	 *             this property cannot be changed after the component has been created
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public void setVerticalStyleName(String verticalStyleName) throws IllegalStateException
	{
		setAttribute("verticalStyleName", verticalStyleName, false);
	}

	/**
	 * Default stylename to use if {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getVertical this.vertical} is true. If unset, the standard
	 * {@link com.smartgwt.client.widgets.toolbar.ToolStrip#getStyleName styleName} will be used for both vertical and horizontal toolstrips.
	 * <P>
	 * Note that this property only applies to the widget at init time. To modify the styleName after this widget has been initialized, you should simply call
	 * {@link com.smartgwt.client.widgets.Canvas#setStyleName setStyleName()} rather than updating this property.
	 * 
	 * 
	 * @return String
	 * @see com.smartgwt.client.docs.Appearance Appearance overview and related methods
	 */
	public String getVerticalStyleName()
	{
		return getAttributeAsString("verticalStyleName");
	}

	// ********************* Methods ***********************

	/**
	 * Add a FormItem to the ToolStrip.
	 * 
	 * @param formItem
	 *            the formItem
	 */
	public void addFormItem(FormItem formItem)
	{
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setCellPadding(3);
		dynamicForm.setMinWidth(50);
		dynamicForm.setNumCols(1);
		dynamicForm.setFields(formItem);
//		applyWidth(dynamicForm.getConfig(), formItem.getJsObj());
		addMember(dynamicForm);
	}

	/**
	 * Add a FormItem to the ToolStrip.
	 * 
	 * @param formItem
	 *            the FormItem
	 * @param position
	 *            the position in the layout to place newMember (starts with 0); if omitted, it will be added at the last position
	 */
	public void addFormItem(FormItem formItem, int position)
	{
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setCellPadding(3);
		dynamicForm.setMinWidth(50);
		dynamicForm.setNumCols(1);
		dynamicForm.setFields(formItem);
//		applyWidth(dynamicForm.getConfig(), formItem.getJsObj());
		addMember(dynamicForm, position);
	}

	// set the width of the form to match that of the form item. using native method because width can be int or String
	private static native void applyWidth(JavaScriptObject formJS, JavaScriptObject itemJS)/*-{
																							formJS.width = itemJS.width;
																							}-*/;

	/**
	 * Add a button to the ToolStrip.
	 * 
	 * @param button
	 *            the toolstrip button
	 */
	public void addButton(ToolStripButton button)
	{
		if (button.getTitle() == null)
		{
			button.setIconSpacing(0);
			button.setLabelHPad(3);
		}
		else if (button.getIcon() == null)
		{
			button.setLabelHPad(7);
		}

		addMember(button);
	}

	/**
	 * Add a button to the ToolStrip.
	 * 
	 * @param button
	 *            the button
	 * @param position
	 *            the position in the layout to place newMember (starts with 0); if omitted, it will be added at the last position
	 */
	public void addButton(ToolStripButton button, int position)
	{
		if (button.getTitle() == null)
		{
			button.setIconSpacing(0);
			button.setLabelHPad(3);
		}
		else if (button.getIcon() == null)
		{
			button.setLabelHPad(7);
		}
		addMember(button, position);
	}

	/**
	 * Add a menu button to the ToolStrip.
	 * 
	 * @param button
	 *            the toolstrip menu button
	 */
	public void addMenuButton(ToolStripMenuButton button)
	{
		if (button.getTitle() == null)
		{
			button.setIconSpacing(0);
			button.setLabelHPad(3);
		}
		else if (button.getIcon() == null)
		{
			button.setLabelHPad(7);
		}
		addMember(button);
	}

	/**
	 * Add a menu button to the ToolStrip.
	 * 
	 * @param button
	 *            the menu button
	 * @param position
	 *            the position in the layout to place newMember (starts with 0); if omitted, it will be added at the last position
	 */
	public void addMenuButton(ToolStripMenuButton button, int position)
	{
		if (button.getTitle() == null)
		{
			button.setIconSpacing(0);
			button.setLabelHPad(3);
		}
		else if (button.getIcon() == null)
		{
			button.setLabelHPad(7);
		}
		addMember(button, position);
	}

	/**
	 * Add an extra space to the right of the previously added ToolStrip element. This is a shortcut for {@link #addSpacer(ToolStripSpacer)}.
	 * 
	 * @param space
	 *            space
	 */
	public void addSpacer(int space)
	{
		addSpacer(new ToolStripSpacer(space));
	}

	/**
	 * Add an extra space to the right of the previously added ToolStrip element.
	 * 
	 * @param toolStripSpacer
	 *            the toolstip spacer.
	 */
	public void addSpacer(ToolStripSpacer toolStripSpacer)
	{
		Canvas canvas = new Canvas();
		canvas.setWidth(1);
		canvas.setHeight(1);
		canvas.setBorder("none");
		canvas.setExtraSpace(toolStripSpacer.getSpace());
		addMember(canvas);
	}

	/**
	 * Adds a LayoutSpacer to the ToolStrip to take up space such like a normal member, without actually drawing anything. This causes the "next" member added
	 * to the toolstip to be right / bottom justified depending on the
	 * {@link com.smartgwt.client.widgets.toolbar.ToolStrip#setAlign(com.smartgwt.client.types.VerticalAlignment) alignment} of the ToolStrip.
	 */
	public void addFill()
	{
		addMember(new LayoutSpacer());
	}

	/**
	 * Add a {@link com.smartgwt.client.widgets.toolbar.ToolStripSeparator separator}.
	 */
	public void addSeparator()
	{
		addMember(new ToolStripSeparator());
	}

	/**
	 * Add a {@link com.smartgwt.client.widgets.toolbar.ToolStripResizer resizer}
	 */
	public void addResizer()
	{
		addMember(new ToolStripResizer());
	}
}
