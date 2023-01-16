package com.wicoder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
public class DBException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    final private static String MSG = "The register with %s = %s not exists in table %s";
    final private static String REQUIRED = "El campo \"%s\" es requerido!...";

    final private Object value;
    final private String tableName;
    final private String columnName;



    public DBException() {

        super("The class could not be implemented");

        this.columnName = null;
        this.tableName = null;
        this.value = null;
    }

    public DBException(String column) {

        super(String.format(REQUIRED, column));

        this.columnName = null;
        this.tableName = null;
        this.value = null;
    }

    public DBException(String table, Object value) {

        super(String.format(MSG, "ID", value, table));

        this.tableName = table;
        this.columnName = "ID";
        this.value = value;
    }

    public DBException(String table, String column, Object value) {

        super(String.format(MSG, column, value, table));

        this.tableName = table;
        this.columnName = column;
        this.value = value;
    }


    public Object getValue() {
        return value;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }
}

