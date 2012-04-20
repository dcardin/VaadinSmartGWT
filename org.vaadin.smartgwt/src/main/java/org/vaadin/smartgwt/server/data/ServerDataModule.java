package org.vaadin.smartgwt.server.data;

import com.google.inject.AbstractModule;

public class ServerDataModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(RecordJSONUpdater.class);
		bind(RecordFactory.class);
	}
}
