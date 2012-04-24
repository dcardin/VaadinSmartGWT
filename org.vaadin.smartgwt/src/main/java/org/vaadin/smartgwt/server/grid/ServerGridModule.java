package org.vaadin.smartgwt.server.grid;

import org.vaadin.smartgwt.server.data.ServerDataModule;

import com.google.inject.AbstractModule;

/**
 * contains default binding for the 'org.vaadin.smartgwt.server.data' package. 
 */
public class ServerGridModule extends AbstractModule {
	@Override
	protected void configure() {
		install(new ServerDataModule());
		bind(ListGridRecordFactory.class);
	}
}
