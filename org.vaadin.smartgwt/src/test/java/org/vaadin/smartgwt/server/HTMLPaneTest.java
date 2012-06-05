package org.vaadin.smartgwt.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.types.ContentsType;

import com.vaadin.terminal.Resource;

public class HTMLPaneTest {
	private HTMLPane htmlPane;

	@Before
	public void before() {
		htmlPane = new HTMLPane();
	}

	@Test
	public void test_settingIFrameResourceSetsContentsResource() {
		final Resource resource = mock(Resource.class);

		htmlPane.setIFrameResource(resource);
		assertEquals(resource, htmlPane.getContentsResource());
	}

	@Test
	public void test_settingIFrameResourceSetsContentsPageWhenNotDrawn() {
		htmlPane.setIFrameResource(mock(Resource.class));
		assertEquals(ContentsType.PAGE, htmlPane.getContentsType());
	}
}
