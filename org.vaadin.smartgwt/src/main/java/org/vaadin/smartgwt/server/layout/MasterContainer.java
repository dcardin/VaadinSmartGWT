package org.vaadin.smartgwt.server.layout;

import java.util.Collections;
import java.util.Iterator;

import org.vaadin.smartgwt.server.BaseWidget;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.core.ComponentList;
import org.vaadin.smartgwt.server.core.PaintablePropertyPainter;
import org.vaadin.smartgwt.server.core.PaintableReference;
import org.vaadin.smartgwt.server.data.DataSource;
import org.vaadin.smartgwt.server.util.SC;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * The Master Container is the top most container of SmartVaadin applications. Why is it needed? There are objects that exist in a static-like context on the
 * client. DataSources, SC etc. These objects must have a server counterpart and it must be held somewhere.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends BaseWidget implements ComponentContainer {
	private final PaintablePropertyPainter paintablePropertyPainter = new PaintablePropertyPainter();
	private final PaintableReference<SC> sc = paintablePropertyPainter.addProperty("sc");
	private final ComponentList<DataSource> dataSources = paintablePropertyPainter.addPaintableList("dataSources");
	private final PaintableReference<Canvas> pane = paintablePropertyPainter.addProperty("pane");
	private final PaintableReference<Window> window = paintablePropertyPainter.addProperty("window");

	public MasterContainer() {
		sc.set(new SC());
		sc.get().setParent(this);
	}

	public void addDataSource(DataSource dataSource) {
		if (!dataSources.contains(dataSource)) {
			if (dataSource.getParent() instanceof ComponentContainer) {
				((ComponentContainer) dataSource.getParent()).removeComponent(dataSource);
			}

			dataSource.setParent(this);
			dataSources.add(dataSource);
		}
	}

	public Canvas getPane() {
		return pane.get();
	}

	public void setPane(Canvas pane) {
		this.pane.set(pane);
		pane.setParent(this);
	}

	public SC getSC() {
		return sc.get();
	}

	public void showWindow(Window window) {
		this.window.set(window);
		window.setParent(this);
		requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		paintablePropertyPainter.paintContent(target);
		window.set(null);
		super.paintContent(target);
	}

	@Override
	public void addComponent(Component c) {

	}

	@Override
	public void removeComponent(Component c) {

	}

	@Override
	public void removeAllComponents() {

	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {

	}

	@Override
	public Iterator<Component> getComponentIterator() {
		return Collections.<Component> emptyList().iterator();
	}

	@Override
	public void requestRepaintAll() {

	}

	@Override
	public void moveComponentsFrom(ComponentContainer source) {

	}

	@Override
	public void addListener(ComponentAttachListener listener) {

	}

	@Override
	public void removeListener(ComponentAttachListener listener) {

	}

	@Override
	public void addListener(ComponentDetachListener listener) {

	}

	@Override
	public void removeListener(ComponentDetachListener listener) {

	}
}
