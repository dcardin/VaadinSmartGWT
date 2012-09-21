package org.vaadin.smartgwt.client.ui.tree;

import org.vaadin.smartgwt.client.core.JSON;
import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;
import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VJSObject;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTreeGrid extends TreeGrid implements Paintable {
	protected final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Element element = DOM.createDiv();
	protected String pid;
	protected ApplicationConnection client;
	protected ServerSideEventRegistration selectedChangedEventRegistration;
	protected ServerSideEventRegistration selectionUpdatedEventRegistration;

	public VTreeGrid() {
		propertyUpdater.addPaintableReferenceListener("data", new PaintableReferenceListener() {
			@Override
			public void onChange(Paintable paintable) {
				setData(((VBaseClass<Tree>) paintable).getJSObject());
			}
		});

		propertyUpdater.addPaintableListListener("fields", new PaintableListListener() {
			@Override
			public void onAdd(Paintable[] source, Integer index, Paintable element) {
				setFields(toTreeGridFieldArray(source));
			}

			@Override
			public void onRemove(Paintable[] source, Integer index, Paintable element) {
				setFields(toTreeGridFieldArray(source));
			}

			private TreeGridField[] toTreeGridFieldArray(Paintable[] source) {
				final TreeGridField[] fields = new TreeGridField[source.length];

				for (int i = 0; i < source.length; i++) {
					fields[i] = ((VTreeGridField) source[i]).getJSObject();
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

			addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
				@Override
				public void onSelectionUpdated(SelectionUpdatedEvent event) {
					final JavaScriptObject selectedRecordsJSA = toJSOArray(getSelectedRecords());
					JavaScriptObject newExclusionReplacer = JSON.newExclusionReplacer(new String[] { "children", "^_parent_isc_Tree_[0-9]+", "selections",
							"messages" });
					VTreeGrid.this.client.updateVariable(pid, "selectedRecords", JSON.stringify(selectedRecordsJSA, newExclusionReplacer), false);
				}
			});

			selectedChangedEventRegistration = new ServerSideEventRegistration("*hasSelectionChangedHandlers") {
				@Override
				protected HandlerRegistration registerHandler() {
					return addSelectionChangedHandler(new SelectionChangedHandler() {
						@Override
						public void onSelectionChanged(SelectionEvent event) {
							final JavaScriptObject eventJSO = JavaScriptObject.createObject();
							JSOHelper.setAttribute(eventJSO, "record", toJSO(event.getRecord()));
							JSOHelper.setAttribute(eventJSO, "state", event.getState());
							JSOHelper.setAttribute(eventJSO, "selection", toJSOArray(event.getSelection()));
							JSOHelper.setAttribute(eventJSO, "selectedRecord", toJSO(event.getSelectedRecord()));
							VTreeGrid.this.client
									.updateVariable(
											pid,
											"onSelectionChanged.event",
											JSON.stringify(eventJSO, JSON.newExclusionReplacer(new String[] { "children", "^_parent_isc_Tree_[0-9]+",
													"selections", "messages" })), true);
						}
					});
				}
			};

			selectionUpdatedEventRegistration = new ServerSideEventRegistration("*hasSelectionUpdatedHandlers") {
				@Override
				protected HandlerRegistration registerHandler() {
					return addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
						@Override
						public void onSelectionUpdated(SelectionUpdatedEvent event) {
							VTreeGrid.this.client.updateVariable(pid, "onSelectionUpdated.event", true, true);
						}
					});
				}
			};
		}

		selectedChangedEventRegistration.updateFromUIDL(uidl);
		selectionUpdatedEventRegistration.updateFromUIDL(uidl);
		propertyUpdater.updateFromUIDL(uidl, client);

		if (uidl.hasAttribute("dataSource")) {
			final Paintable paintable = uidl.getPaintableAttribute("dataSource", client);
			setDataSource(((VJSObject<DataSource>) paintable).getJSObject());
		}

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

	private static JavaScriptObject toJSOArray(DataClass[] array) {
		final JavaScriptObject arrayJSO = JavaScriptObject.createArray();
		for (int i = 0; i < array.length; i++) {
			JSOHelper.setArrayValue(arrayJSO, i, array[i].getJsObj());
		}
		return arrayJSO;
	}

	private static JavaScriptObject toJSO(DataClass dataClass) {
		return dataClass == null ? null : dataClass.getJsObj();
	}

	private static abstract class ServerSideEventRegistration {
		private final String uidlAttribute;
		private HandlerRegistration registration;

		public ServerSideEventRegistration(String uidlAttribute) {
			this.uidlAttribute = uidlAttribute;
		}

		public void updateFromUIDL(UIDL uidl) {
			if (uidl.hasAttribute(uidlAttribute) && registration == null) {
				registration = registerHandler();
			} else if (!uidl.hasAttribute(uidlAttribute) && registration != null) {
				registration.removeHandler();
				registration = null;
			}
		}

		protected abstract HandlerRegistration registerHandler();
	}
}