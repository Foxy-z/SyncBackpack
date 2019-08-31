package fr.thefoxy41.syncBackpack.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {
    private final DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    DatabaseAccess(DatabaseCredentials credentials) {
        this.credentials = credentials;
    }

    private void setUpHikariCP(Plugin plugin) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(this.credentials.toURI());
        hikariConfig.setUsername(this.credentials.getUser());
        hikariConfig.setPassword(this.credentials.getPass());

        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMaxLifetime(30000L);
        hikariConfig.setIdleTimeout(30000L);
        hikariConfig.setLeakDetectionThreshold(10000L);
        hikariConfig.setConnectionTimeout(10000L);
        hikariConfig.setPoolName(plugin.getName());
        hikariConfig.setMinimumIdle(2);

        hikariConfig.addDataSourceProperty("autoReconnect", Boolean.TRUE);
        hikariConfig.addDataSourceProperty("cachePrepStmts", Boolean.TRUE);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", Boolean.TRUE);
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", Boolean.TRUE);

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initPool(Plugin plugin) {
        setUpHikariCP(plugin);
    }

    public void closePool() {
        this.hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }
}
