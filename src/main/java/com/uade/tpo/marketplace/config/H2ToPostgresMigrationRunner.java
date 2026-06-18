package com.uade.tpo.marketplace.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "app.migration.h2-to-postgres.enabled", havingValue = "true")
public class H2ToPostgresMigrationRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2ToPostgresMigrationRunner.class);

    private static final List<String> TABLES = Arrays.asList(
            "roles",
            "generos",
            "talles",
            "tipos_camiseta",
            "paises",
            "usuarios",
            "camisetas",
            "carritos",
            "pedidos",
            "camiseta_talles",
            "descuentos",
            "items_carrito",
            "detalles_pedido"
    );

    private final DataSource targetDataSource;
    private final String sourceUrl;
    private final String sourceUsername;
    private final String sourcePassword;

    public H2ToPostgresMigrationRunner(
            DataSource targetDataSource,
            @Value("${app.migration.h2-to-postgres.source-url}") String sourceUrl,
            @Value("${app.migration.h2-to-postgres.source-username}") String sourceUsername,
            @Value("${app.migration.h2-to-postgres.source-password}") String sourcePassword) {
        this.targetDataSource = targetDataSource;
        this.sourceUrl = sourceUrl;
        this.sourceUsername = sourceUsername;
        this.sourcePassword = sourcePassword;
    }

    @Override
    public void run(String... args) throws Exception {
        Class.forName("org.h2.Driver");

        try (Connection source = DriverManager.getConnection(sourceUrl, sourceUsername, sourcePassword);
             Connection target = targetDataSource.getConnection()) {
            assertPostgres(target);
            assertTargetIsEmpty(target);

            target.setAutoCommit(false);
            try {
                for (String table : TABLES) {
                    int copiedRows = copyTable(source, target, table);
                    LOGGER.info("Migrated {} rows from H2 table {}", copiedRows, table);
                }

                resetIdentitySequences(target);
                target.commit();
                LOGGER.info("H2 to PostgreSQL migration completed successfully");
            } catch (Exception exception) {
                target.rollback();
                throw exception;
            }
        }
    }

    private void assertPostgres(Connection target) throws SQLException {
        String databaseName = target.getMetaData().getDatabaseProductName();
        if (!databaseName.toLowerCase(Locale.ROOT).contains("postgresql")) {
            throw new IllegalStateException(
                    "H2 migration requires PostgreSQL as target, but found " + databaseName
            );
        }
    }

    private void assertTargetIsEmpty(Connection target) throws SQLException {
        for (String table : TABLES) {
            try (Statement statement = target.createStatement();
                 ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
                result.next();
                if (result.getLong(1) > 0) {
                    throw new IllegalStateException(
                            "PostgreSQL target is not empty. Migration stopped at table " + table
                    );
                }
            }
        }
    }

    private int copyTable(Connection source, Connection target, String table) throws SQLException {
        try (Statement sourceStatement = source.createStatement();
             ResultSet rows = sourceStatement.executeQuery("SELECT * FROM " + table)) {
            ResultSetMetaData metadata = rows.getMetaData();
            int columnCount = metadata.getColumnCount();
            String insertSql = buildInsertSql(table, metadata);
            int copiedRows = 0;

            try (PreparedStatement insert = target.prepareStatement(insertSql)) {
                while (rows.next()) {
                    for (int column = 1; column <= columnCount; column++) {
                        insert.setObject(column, rows.getObject(column));
                    }
                    insert.addBatch();
                    copiedRows++;
                }

                if (copiedRows > 0) {
                    insert.executeBatch();
                }
            }

            return copiedRows;
        }
    }

    private String buildInsertSql(String table, ResultSetMetaData metadata) throws SQLException {
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner parameters = new StringJoiner(", ");

        for (int column = 1; column <= metadata.getColumnCount(); column++) {
            columns.add(metadata.getColumnName(column).toLowerCase(Locale.ROOT));
            parameters.add("?");
        }

        return "INSERT INTO " + table + " (" + columns + ") VALUES (" + parameters + ")";
    }

    private void resetIdentitySequences(Connection target) throws SQLException {
        for (String table : TABLES) {
            String sql = "SELECT setval(" +
                    "pg_get_serial_sequence('" + table + "', 'id'), " +
                    "COALESCE(MAX(id), 1), COUNT(*) > 0) FROM " + table;
            try (Statement statement = target.createStatement()) {
                statement.execute(sql);
            }
        }
    }
}
