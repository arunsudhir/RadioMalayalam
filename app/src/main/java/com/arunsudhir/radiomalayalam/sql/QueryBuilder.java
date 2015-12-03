package com.arunsudhir.radiomalayalam.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Created by ullatil on 12/2/2015.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryBuilder {
    private final String tableName;
    private final List<String> columns = new ArrayList<>();
    private boolean distinct = false;
    private Condition condition;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public static QueryBuilder fromTable(String tableName) {
        return new QueryBuilder(tableName);
    }

    public QueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public QueryBuilder column(String columnName) {
        columns.add(columnName);
        return this;
    }

    public QueryBuilder where(Condition condition) {
        this.condition = condition;
        return this;
    }

    public QueryBuilder groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public QueryBuilder having(String having) {
        this.having = having;
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public QueryBuilder limit(String limit) {
        this.limit = limit;
        return this;
    }

    public Cursor execute(SQLiteOpenHelper instance) {
        String where = condition != null ? condition.getCondition() : null;
        String[] whereArgs = condition != null ? condition.getArguments() : null;
        return instance.getReadableDatabase().query(
                distinct,
                tableName,
                toArray(columns),
                where,
                whereArgs,
                groupBy,
                having,
                orderBy,
                limit);
    }

    private String[] toArray(List<String> list) {
        if (list == null) {
            return null;
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }
}
