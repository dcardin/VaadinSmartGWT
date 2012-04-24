package org.vaadin.smartgwt.server.data;

import com.google.inject.AbstractModule;

/**
 * contains default bindings for the 'org.vaadin.smartgwt.server.data' package. 
 */
public class ServerDataModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(RecordJSONUpdater.class);
		bind(RecordFactory.class);
	}
}
