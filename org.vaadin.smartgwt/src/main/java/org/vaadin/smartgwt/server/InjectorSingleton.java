package org.vaadin.smartgwt.server;

import java.util.Set;

import org.vaadin.smartgwt.server.data.ServerDataModule;
import org.vaadin.smartgwt.server.grid.ServerGridModule;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Contains a static reference to the library dependency injector.  It is possible to change the injector reference if different implementation injection is
 * required.  To do so, a call to {@link #set(Injector)} must be done before a call to {@link #get()} is done. Once the injector has been set, it cannot be
 * changed.
 */
public class InjectorSingleton {
	private static Injector injector;

	/**
	 * Returns the library injector.  If it is not already set, it will create a default injector. 
	 */
	public static Injector get() {
		return injector == null ? injector = Guice.createInjector(getDefaultModules()) : injector;
	}

	/**
	 * Sets the library injector.  It cannot be changed once set.
	 * 
	 *  @throws RuntimeException if the injector was already set.
	 */
	public static void set(Injector injector) {
		if (InjectorSingleton.injector == null) {
			InjectorSingleton.injector = injector;
		} else {
			throw new RuntimeException("once set, the injector cannot be changed.");
		}
	}

	private static Set<Module> getDefaultModules() {
		final Set<Module> modules = Sets.newHashSet();
		modules.add(new ServerDataModule());
		modules.add(new ServerGridModule());
		return modules;
	}
}
