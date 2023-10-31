package database;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void createTable() {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table test1 (INT id, STR name)").getStatus());
        assertSame(Result.Status.OK, database.query("create table test2 (INT id, STR name)").getStatus());

        Result result = database.query("list tables");
        assertSame(Result.Status.OK, result.getStatus());
        assertSame(2, result.getRows().size());
    }

    @Test
    void date() {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table test1 (DATE date, DATE_INV date_inv)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into test1 (date) values(2003-29-03)").getStatus());
        assertSame(Result.Status.FAIL, database.query("insert into test1 (date) values(222)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into test1 (date_inv) values(2003-29-03, 2003-29-10)").getStatus());
        assertSame(Result.Status.FAIL, database.query("insert into test1 (date_inv) values(7777, 8888)").getStatus());
    }

    @Test
    void RenameOperation() throws Exception {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table testTable (INT id, STR name)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into testTable  (id, name) values(1, cat1)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into testTable  (id, name) values(2, cat2)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into testTable  (id, name) values(3, cat3)").getStatus());

        var result = database.query(String.format("ALTER TABLE %s RENAME COLUMN %s TO %s", "testTable", "name", "new_column_mame"));
        assertSame(Result.Status.OK, result.getStatus());

        Column newColumn = new Column(Column.Type.STR, "new_column_mame");

        database.save();
        var newColumnsCollection = database.getTableColumns("testTable");

        boolean isNewColumnExist = false;
        for(Column column : newColumnsCollection)
        {
            System.out.println(column.getName());
            if(Objects.equals(column.getName(), "new_column_mame"))
            {
                isNewColumnExist = true;
            }
        }
        assertTrue(!isNewColumnExist);

    }
}