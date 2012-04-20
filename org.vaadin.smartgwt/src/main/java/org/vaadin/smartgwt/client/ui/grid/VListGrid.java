package org.vaadin.smartgwt.client.ui.grid;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VJSObject;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VListGrid extends ListGrid implements Paintable {
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Element element = DOM.createDiv();
	private final ClientSideProxy rpc = new ClientSideProxy("VListGrid", new ClientSideHandlerImpl());
	private HandlerRegistration selectionChangedRegistration;
	private HandlerRegistration selectionUpdatedRegistration;
	private String pid;
	private ApplicationConnection client;

	public VListGrid() {
		propertyUpdater.addPaintableListListener("fields", new PaintableListListener() {
			@Override
			public void onAdd(Paintable[] source, Integer index, Paintable element) {
				setFields(toListGridFieldArray(source));
			}

			@Override
			public void onRemove(Paintable[] source, Integer index, Paintable element) {
				setFields(toListGridFieldArray(source));
			}

			private ListGridField[] toListGridFieldArray(Paintable[] source) {
				final ListGridField[] fields = new ListGridField[source.length];

				for (int i = 0; i < source.length; i++) {
					fields[i] = ((VListGridField) source[i]).getJSObject();
				}

				return fields;
			}
		});
	}

	@Override
	public Element getElement() {
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (uidl.hasAttribute("cached")) {
			return;
		}

		if (this.pid == null) {
			this.pid = uidl.getId();
			this.client = client;
		}

		propertyUpdater.updateFromUIDL(uidl, client);
		rpc.update(this, uidl, client);

		if (uidl.hasAttribute("dataSource")) {
			final Paintable paintable = uidl.getPaintableAttribute("dataSource", client);
			setDataSource(((VJSObject<DataSource>) paintable).getJSObject());
		}

		if (uidl.hasAttribute("*hasSelectionChangedHandlers") && selectionChangedRegistration == null) {
			selectionChangedRegistration = addSelectionChangedHandler(new SelectionChangedHandler() {
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					final JSONObject eventJSO = new JSONObject();
					eventJSO.put("record", new JSONObject(event.getRecord().getJsObj()));
					eventJSO.put("state", JSONBoolean.getInstance(event.getState()));
					final JSONArray selectionJSO = new JSONArray();
					for (int i = 0; event.getSelection().length < i; i++) {
						selectionJSO.set(i, new JSONObject(event.getSelection()[i].getJsObj()));
					}
					eventJSO.put("selection", selectionJSO);
					eventJSO.put("selectedRecord", new JSONObject(event.getSelectedRecord().getJsObj()));
					VListGrid.this.client.updateVariable(pid, "onSelectionChanged.event", eventJSO.toString(), true);
				}
			});
		} else if (!uidl.hasAttribute("hasSelectionChangedHandlers") && selectionChangedRegistration != null) {
			selectionChangedRegistration.removeHandler();
			selectionChangedRegistration = null;
		}

		if (uidl.hasAttribute("*hasSelectionUpdatedHandlers") && selectionUpdatedRegistration == null) {
			selectionUpdatedRegistration = addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
				@Override
				public void onSelectionUpdated(SelectionUpdatedEvent event) {
					VListGrid.this.client.updateVariable(pid, "onSelectionUpdated.event", true, true);
				}
			});
		} else if (!uidl.hasAttribute("*hasSelectionUpdatedHandlers") && selectionUpdatedRegistration != null) {
			selectionUpdatedRegistration.removeHandler();
			selectionUpdatedRegistration = null;
		}

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

	private class ClientSideHandlerImpl implements ClientSideHandler {
		@Override
		public boolean initWidget(Object[] params) {
			return false;
		}

		@Override
		public void handleCallFromServer(String method, Object[] params) {

		}
	}
}