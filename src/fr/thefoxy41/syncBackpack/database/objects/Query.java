package fr.thefoxy41.syncBackpack.database.objects;

import fr.thefoxy41.syncBackpack.database.DatabaseManager;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseConnectionException;
import fr.thefoxy41.syncBackpack.database.exceptions.DatabaseQueryException;
import fr.thefoxy41.syncBackpack.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Query {
    private final Connection connection;

    private final Map<String, String[]> join = new LinkedHashMap<>();
    private final List<String> params = new ArrayList<>();
    private final List<String> where = new ArrayList<>();
    private final List<String> order = new ArrayList<>();
    private final List<String> insert = new ArrayList<>();

    private String table;
    private String select;
    private String limit;

    private boolean insertOrUpdate = false;
    private boolean delete = false;
    private boolean updatable = false;

    /**
     * Create query from default connection
     *
     * @throws DatabaseConnectionException connection exception
     */
    public Query() throws DatabaseConnectionException {
        try {
            this.connection = DatabaseManager.getDataBase().getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("An error occurred while connecting to main database");
        }
    }

    /**
     * Create query from existing connection
     *
     * @param connection Connection
     * @throws DatabaseConnectionException connection exception
     * @noinspection unused
     */
    public Query(Connection connection) throws DatabaseConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new DatabaseConnectionException("An error occurred while connecting to database: connection is null or closed");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("An error occurred while connecting to main database");
        }

        this.connection = connection;
    }

    /**
     * Select query table
     *
     * @param table String
     * @return Query
     */
    public Query from(String table) {
        this.table = table;
        return this;
    }

    /**
     * Add select * argument to query
     *
     * @return Query
     */
    public Query select() {
        this.select = "*";
        return this;
    }

    /**
     * Add select argument to query
     *
     * @param item String elements to select
     * @return Query
     */
    public Query select(String item) {
        this.select = item;
        return this;
    }

    /**
     * Select columns fort insert query
     *
     * @param items String[] selected columns for insert
     * @return Query
     */
    public Query insert(String... items) {
        this.insert.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * Select delete query type
     *
     * @return Query
     */
    public Query delete() {
        this.delete = true;
        return this;
    }

    /**
     * Insert element or update if duplicate key sql error
     *
     * @param items String[] selected columns for insert
     * @return Query
     */
    public Query insertOrUpdate(String... items) {
        this.insert.addAll(Arrays.asList(items));
        this.insertOrUpdate = true;
        return this;
    }

    /**
     * Add where condition to query
     *
     * @param key   String first element to compare
     * @param value String element to compare to first
     * @return Query
     */
    public Query where(String key, String value) {
        this.where.add(key + " = '" + value + "'");
        return this;
    }

    /**
     * Add order condition to query
     *
     * @param string String[]
     * @return Query
     * @noinspection unused
     */
    public Query order(String... string) {
        this.order.addAll(Arrays.asList(string));
        return this;
    }

    /**
     * Add limit argument to query
     *
     * @param string String
     * @return Query
     * @noinspection unused
     */
    public Query limit(String string) {
        this.limit = string;
        return this;
    }

    /**
     * Add join argument to query
     *
     * @param table     String
     * @param condition String
     * @return Query
     */
    public Query join(String table, String condition) {
        return join(table, condition, "left");
    }

    /**
     * Add join argument to query
     *
     * @param table     String
     * @param condition String
     * @param type      String
     * @return Query
     * @noinspection WeakerAccess
     */
    public Query join(String table, String condition, String type) {
        String[] args = {condition, type};
        this.join.put(table, args);
        return this;
    }

    /**
     * Bind params for insert, insertOrUpdate query
     *
     * @param string String[] params
     * @return Query
     */
    public Query params(String... string) {
        this.params.addAll(Arrays.asList(string));
        return this;
    }

    /**
     * Make ResultSet of query updatable
     *
     * @return Query
     */
    public Query updatable() {
        this.updatable = true;
        return this;
    }

    /**
     * Execute query
     *
     * @return ResultSet
     * @throws DatabaseQueryException query exception
     */
    public ResultSet execute() throws DatabaseQueryException {
        List<String> parts = new ArrayList<>();

        // if query is select
        if (this.select != null) {
            // bind select
            parts.add("SELECT");
            parts.add(this.select);

            // bind table
            parts.add("FROM");
            parts.add(this.table);

            // bind join
            if (!this.join.isEmpty()) {
                for (String table : this.join.keySet()) {
                    String[] args = this.join.get(table);
                    parts.add(args[1].toUpperCase());
                    parts.add("JOIN");
                    parts.add(table);
                    parts.add("ON");
                    parts.add(args[0]);
                }
            }

            // bind where
            if (!this.where.isEmpty()) {
                parts.add("WHERE");
                parts.add("(" + StringUtils.join(") AND (", this.where) + ")");
            }

            // bind order
            if (!this.order.isEmpty()) {
                parts.add("ORDER BY");
                parts.add(StringUtils.join(", ", this.order));
            }

            // bind limit
            if (this.limit != null) {
                parts.add("LIMIT");
                parts.add(this.limit);
            }
            // if query is insert
        } else if (!this.insert.isEmpty()) {
            // bind insert
            parts.add("INSERT INTO");
            parts.add(this.table);

            // bind columns
            parts.add("(");
            parts.add(StringUtils.join(", ", this.insert));
            parts.add(")");

            // bind values
            parts.add("VALUES");
            parts.add("(");
            parts.add("'" + StringUtils.join("', '", this.params) + "'");
            parts.add(")");

            // bind duplicate keys case (if insertOrUpdate)
            if (this.insertOrUpdate) {
                parts.add("ON DUPLICATE KEY UPDATE");
                parts.add(StringUtils.join("', ", " = '", this.insert, this.params) + "'");
            }

            // if query is delete
        } else if (this.delete) {
            parts.add("DELETE FROM");
            parts.add(this.table);
            if (!this.where.isEmpty()) {
                parts.add("WHERE");
                parts.add("(" + StringUtils.join(") AND (", this.where) + ")");
            }
        } else {
            throw new DatabaseQueryException("You must use insert or select into your query");
        }

        // execute query
        PreparedStatement preparedStatement = null;
        try {
            if (this.updatable) {
                preparedStatement = this.connection.prepareStatement(
                        StringUtils.join(" ", parts),
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE
                );
            } else {
                preparedStatement = this.connection.prepareStatement(StringUtils.join(" ", parts));
            }

            // if query has result
            if (this.select != null) {
                return preparedStatement.executeQuery();
            }

            preparedStatement.execute();
            return null;
        } catch (SQLException e) {
            throw new DatabaseQueryException("You have an error in your sql syntax", StringUtils.join(" ", parts));
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (this.connection != null) this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
