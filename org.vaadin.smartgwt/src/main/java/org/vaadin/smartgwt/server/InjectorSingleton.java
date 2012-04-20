package org.vaadin.smartgwt.server;

import java.util.Set;

import org.vaadin.smartgwt.server.data.ServerDataModule;
import org.vaadin.smartgwt.server.grid.ServerGridModule;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjectorSingleton {
	private static Injector injector;

	public static Injector get() {
		return injector == null ? injector = Guice.createInjector(getDefaultModules()) : injector;
	}

	public static void set(Injector injector) {
		InjectorSingleton.injector = injector;
	}

	private static Set<Module> getDefaultModules() {
		final Set<Module> modules = Sets.newHashSet();
		modules.add(new ServerDataModule());
		modules.add(new ServerGridModule());
		return modules;
	}
}
