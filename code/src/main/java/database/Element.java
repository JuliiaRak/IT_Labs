package database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Element implements Serializable {
    private String value;
    private String column;

    Element(String value, String column) {
        this.value = value;
        this.column = column;
    }

    @JsonIgnore
    public Integer getAsInteger() {
        return parseInt(value);
    }

    @JsonIgnore
    public Float getAsFloat() {
        return Float.parseFloat(value);
    }

    @JsonIgnore
    public char getAsCharacter() throws Exception {
        if (value.length() != 1) throw new Exception("Invalid character value");
        return value.charAt(0);
    }

    @JsonIgnore
    public String getAsString() {
        return value;
    }

    @JsonIgnore
    public Date getAsDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(value);
        return date;
    }

    @JsonIgnore
    public List<Date> getAsDateInv() throws ParseException {
        List<Date> dateList = new ArrayList<>();
        String[] dateValues = value.split(",");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (String dateStr : dateValues) {
            dateList.add(sdf.parse(dateStr.trim()));
        }

        return dateList;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String new_column) {column = new_column;}
    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void validate(Table table) throws Exception {
        Column column = table.getColumn(this.column);

        if (value == null) {
            if (column.isNullAllowed()) return;
            throw new Exception("Null value is not allowed");
        }

        try {
            switch (column.getType()) {
                case INT:
                    getAsInteger();
                    break;
                case FLOAT:
                    getAsFloat();
                    break;
                case CHAR:
                    getAsCharacter();
                    break;
                case STR:
                    getAsString();
                    break;
                case DATE:
                    getAsDate();
                    break;
                case DATE_INV:
                    getAsDateInv();
                    break;
            }
        } catch (Exception e) {
            throw new Exception(String.format("Invalid element value '%s': %s", value, e.getMessage()));
        }
    }

    public boolean equals(String other) {
        if (other == null) return value == null;
        if (value == null) return other.equals("null");
        return value.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(value, element.value) &&
                Objects.equals(column, element.column);
    }

}