package database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mentaregex.Regex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jsoup.helper.StringUtil.isNumeric;

public class Database {

    private String filePath;

    private HashMap<String, Table> tables;

    public Database(String filePath) {
        this.filePath = filePath;
        tables = new HashMap<>();
    }

    public void setFilePath(String value) {
        filePath = value;
    }

    public Collection<Column> getTableColumns(String name) throws Exception {
        if (!tables.containsKey(name))
            throw new Exception(String.format("A table with the name '%s' doesn't exist", name));
        return tables.get(name).getColumns();
    }

    public void createTable(String name, Collection<Column> columns) throws Exception {
        if (tables.containsKey(name))
            throw new Exception(String.format("A table with the name '%s' already exists", name));
        tables.put(name, new Table(name, columns));
    }

    public void removeTable(String name) throws Exception {
        if (!tables.containsKey(name))
            throw new Exception(String.format("A table with the name '%s' doesn't exist", name));
        tables.remove(name);
    }

    public Collection<String> getTableNames() {
        return new ArrayList<>(tables.keySet());
    }

    public Result query(String queryMessage) {
        try {
            for (QueryRegex regex : QueryRegex.values()) {
                String[] found = Regex.match(queryMessage, regex.getRegex());
                if (found == null) continue;

                ArrayList<String> matches = Arrays.stream(found).filter(Predicate.isEqual(null).negate()).map(String::trim).collect(Collectors.toCollection(ArrayList::new));

                Result result = new Result(Result.Status.OK);
                boolean modified = false;

                switch (regex) {
                    case INSERT_ROW: {
                        Table table = tables.get(matches.get(0));

                        ArrayList<Column> columns = toColumns(parseValues(matches.get(1)), table);
                        ArrayList<String> values = parseValues(matches.get(2));

                        table.insert(IntStream.range(0, columns.size()).boxed().collect(Collectors.toMap(columns::get, values::get)));
                        modified = true;
                        break;
                    }
                    case DELETE_ROWS: {
                        tables.get(matches.get(0)).delete(parsePredicate(matches.get(1)));
                        modified = true;
                        break;
                    }
                    case UPDATE_ROWS: {
                        Table table = tables.get(matches.get(0));

                        ArrayList<String> assignmentExprs = parseValues(matches.get(1));
                        Map<Column, String> assignments = new HashMap<>();

                        for (String assignmentExpr : assignmentExprs) {
                            String[] pair = parseEqual(assignmentExpr);
                            assignments.put(table.getColumn(pair[0]), pair[1]);
                        }

                        table.update(assignments, parsePredicate(matches.get(2)));
                        modified = true;
                        break;
                    }
                    case SELECT_ROWS: {
                        Table table = tables.get(matches.get(1));
                        String predicateExpr = matches.size() > 2 ? matches.get(2) : null;
                        Collection<Column> columns = matches.get(0).equals("*") ? table.getColumns() : toColumns(parseValues(matches.get(0)), table);

                        result.setRows(table.select(columns, parsePredicate(predicateExpr)));
                        break;
                    }
                    case CREATE_TABLE: {
                        String[] columnProps = matches.get(1).split(",");

                        ArrayList<Column> columns = new ArrayList<>();
                        for (String prop : columnProps) {
                            String[] tokens = prop.trim().split("\\s+");

                            Column column = new Column(Column.Type.valueOf(tokens[0]), tokens[1]);
                            //TODO
                            columns.add(column);
                        }

                        createTable(matches.get(0), columns);
                        modified = true;
                        break;
                    }
                    case REMOVE_TABLE: {
                        System.out.print("HYI");
                        removeTable(matches.get(0));
                        modified = true;
                        break;
                    }
                    case LIST_TABLES: {
                        result.setRows(getTableNames().stream().map(tableName -> new Row(Collections.singletonList(new Element(tableName, "table_name")))).collect(Collectors.toCollection(ArrayList::new)));
                        break;
                    }
                    case SUBTRACT: {
                        Table leftTable = tables.get(matches.get(0));
                        Table rightTable = tables.get(matches.get(1));
                        result.setRows(leftTable.subtract(rightTable));
                        break;
                    }
                    case SORT_BY_COLUMN: {
                        String tableName = matches.get(0);
                        String columnName = matches.get(1);
                        String order = (matches.size() > 2) ? matches.get(2) : "ASC"; // Порядок сортування (за замовчуванням - ASC)

                        Table tableToSort = tables.get(tableName);

//                        if (tableToSort == null) {
//                            result.setError("Таблиця " + tableName + " не існує.");
//                            break;
//                        }
//
//                        if (!tableToSort.columnExists(columnName)) {
//                            result.setError("Стовпець " + columnName + " не існує в таблиці " + tableName + ".");
//                            break;
//                        }

                        List<Row> sortedRows = tableToSort.getRows().stream()
                                .sorted((row1, row2) -> {
                                    Element elem1 = row1.getElement(columnName);
                                    Element elem2 = row2.getElement(columnName);

                                    // Якщо елементи можна порівняти як цілі числа:
                                    if (isNumeric(elem1.getValue()) && isNumeric(elem2.getValue())) {
                                        int comparison = Integer.compare(Integer.parseInt(elem1.getValue()), Integer.parseInt(elem2.getValue()));
                                        return "DESC".equalsIgnoreCase(order) ? -comparison : comparison;
                                    }

                                    // Якщо елементи порівнюються як рядки:
                                    int comparison = elem1.getValue().compareToIgnoreCase(elem2.getValue());
                                    return "DESC".equalsIgnoreCase(order) ? -comparison : comparison;
                                })
                                .collect(Collectors.toList());

                        result.setRows(sortedRows);
                        break;
                    }

                    case ALTER_TABLE_RENAME_COLUMN: {

                        String tableName = matches.get(0);
                        String currentColumnName = matches.get(1);
                        String newColumnName = matches.get(2);

                        // Перевірка наявності таблиці
                        Table tableToAlter = tables.get(tableName);
//                        if (tableToAlter == null) {
//                            result.setError("Таблиця " + tableName + " не існує.");
//                            break;
//                        }
//
//                        // Перевірка наявності стовпчика
//                        if (!tableToAlter.columnExists(currentColumnName)) {
//                            result.setError("Стовпець " + currentColumnName + " не існує в таблиці " + tableName + ".");
//                            break;
//                        }

                        // Зміна імені стовпчика
                        tableToAlter.renameColumn(currentColumnName, newColumnName);
                        modified = true;
                        // Встановлення результату
//                        result.setSuccess("Стовпець " + currentColumnName + " було успішно перейменовано на " + newColumnName + " в таблиці " + tableName + ".");
                        break;
                    }
                }

                if (modified) save();

                return result;
            }
        } catch (Exception e) {
            return new Result(Result.Status.FAIL).setReport(e.getMessage());
        }

        return new Result(Result.Status.FAIL).setReport("Invalid query syntax");
    }

    private Predicate<Row> parsePredicate(String expr) throws Exception {
        Predicate<Row> result = row -> true;

        if (expr == null) return result;

        for (String equalExpr : parseValues(expr)) {
            result = result.and(toEqualPredicate(parseEqual(equalExpr)));
        }

        return result;
    }

    private String[] parseEqual(String expr) throws Exception {
        String[] pair = expr.split("=");
        if (pair.length != 2) throw new Exception("Invalid equal expression");
        return pair;
    }

    private Predicate<Row> toEqualPredicate(String[] pair) {
        return row -> {
            Element element = row.getElement(pair[0]);
            if (element == null) return false;
            return element.equals(pair[1]);
        };
    }

    private ArrayList<String> parseValues(String expr) throws Exception {
        return Arrays.stream(expr.split(",")).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Column> toColumns(Collection<String> names, Table table) throws Exception {
        ArrayList<Column> result = new ArrayList<>();
        for (String colName : names) result.add(table.getColumn(colName));
        return result;
    }

    public void save() throws IOException {
        if (filePath == null) return;

        FileWriter writer = new FileWriter(filePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(this, writer);
        writer.close();
    }

    public void rearrangeColumn(String tableName, String columnName, String newPosition) throws Exception {
//        Table table = tables.get(tableName);
//        if (table == null) {
//            throw new Exception("Table not found");
//        }
//
//        // 1. Rearrange columns in the table structure
//        LinkedHashMap<String, Column> newColumnsOrder = new LinkedHashMap<>();
//        int currentIndex = 0;
//        boolean added = false;
//
//        for (Map.Entry<String, Column> entry : table.getColumns().entrySet()) {
//            if (currentIndex == newPosition) {
//                newColumnsOrder.put(columnName, table.getColumn(columnName));
//                added = true;
//            }
//
//            // Skip the original position of the moving column
//            if (!entry.getKey().equals(columnName)) {
//                newColumnsOrder.put(entry.getKey(), entry.getValue());
//                currentIndex++;
//            }
//        }
//
//        // If column was not added in the loop (means it should be the last column)
//        if (!added) {
//            newColumnsOrder.put(columnName, table.getColumn(columnName));
//        }
//
//        // Set the rearranged columns back to the table
//        table.setColumns(newColumnsOrder);
//
//        // 2. Rearrange elements in each row according to the new columns order
//        for (Row row : table.getRows()) {
//            LinkedHashMap<String, Element> rearrangedRow = new LinkedHashMap<>();
//            for (String colName : newColumnsOrder.keySet()) {
//                rearrangedRow.put(colName, row.getElement(colName));
//            }
//            row.setElements(rearrangedRow);
//        }
    }

}
