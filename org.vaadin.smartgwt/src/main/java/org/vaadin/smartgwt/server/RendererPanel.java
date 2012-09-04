package org.vaadin.smartgwt.server;

import java.util.Map;

import org.vaadin.rpc.server.ServerSideHandler;
import org.vaadin.rpc.server.ServerSideProxy;
import org.vaadin.smartgwt.client.VRendererPanel;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VRendererPanel.class)
public class RendererPanel extends Canvas
{
	private final ServerSideProxy client = new ServerSideProxy(new ServerSideHandlerImpl());

	public void refresh() {
		client.call("refresh");
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		client.paintContent(target);
	}

	@Override
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
			RendererPanel.this.requestRepaint();
		}
	}
}
