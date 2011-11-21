package org.vaadin.smartgwt.server.grid;


public interface CellFormatter {

    String format(Object value, ListGridRecord record, int rowNum, int colNum);
}
