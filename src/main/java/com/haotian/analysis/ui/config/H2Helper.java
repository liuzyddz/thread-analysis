package com.haotian.analysis.ui.config;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2Helper {
    private static final JdbcConnectionPool H2DB_POOL;
    private static final Config CONFIG = ConfigUtil.getDefaultConfig();
    private static final H2DBConfig H2DB_CONFIG = CONFIG.getH2DBConfig();

    static {
        H2DB_POOL = JdbcConnectionPool.create(H2DB_CONFIG.getJdbcUrl(), H2DB_CONFIG.getJdbcUser(), H2DB_CONFIG.getJdbcPasswd());
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = H2Helper.getConnection();
            String[] initSQL = H2DB_CONFIG.getInitDb();
            for (String sql : initSQL) {
                execDbInit(sql, conn);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("InitDB error", e);
        } finally {
            close(conn);
        }
    }

    private static void execDbInit(String sql, Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.execute();
        } finally {
            close(stmt);
        }
    }

    public static void saveThreadSummary(String threadFile, String threadEncId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(H2DB_CONFIG.getSqlMapping().get("save-thread-summary-sql"));
            stmt.setString(1, threadFile);
            stmt.setString(2, threadEncId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("exec sql error", e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    public static void saveThreadItem(ThreadItem threadItem) {
        if (threadItem == null) {
            return;
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(H2DB_CONFIG.getSqlMapping().get("save-thread-details-sql"));
            stmt.setString(1, threadItem.getThreadEncId());
            stmt.setString(2, threadItem.getThreadName());
            stmt.setString(3, threadItem.getThreadDetails());
            stmt.setString(4, threadItem.getThreadDecId());
            stmt.setString(5, threadItem.getThreadHexId());
            stmt.setString(6, threadItem.getThreadState());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("exec sql error", e);
        } finally {
            close(stmt);
            close(conn);
        }
    }

    public static List<ThreadItem> searchThreadItemByName(String threadEncId, String threadName) {
        return getThreadItemsByDynQuery(threadEncId, threadName, "search-thread-details-byname-sql");
    }

    private static List<ThreadItem> getThreadItemsByDynQuery(String threadEncId, String queryArg, String sqlKey) {
        List<ThreadItem> threadItemList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(H2DB_CONFIG.getSqlMapping().get(sqlKey));
            stmt.setString(1, threadEncId);
            stmt.setString(2, queryArg);
            rs = stmt.executeQuery();
            while (rs.next()) {
                threadItemList.add(extractThreadItem(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("exec sql error", e);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return threadItemList;
    }

    private static ThreadItem extractThreadItem(ResultSet rs) throws SQLException {
        return new ThreadItem(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
    }

    public static List<ThreadItem> searchThreadItemByStack(String threadEncId, String queryArg) {
        return getThreadItemsByDynQuery(threadEncId, queryArg, "search-thread-details-bystack-sql");
    }

    public static List<ThreadItem> searchThreadItemByState(String threadEncId, String queryArg) {
        return getThreadItemsByDynQuery(threadEncId, queryArg, "search-thread-details-bystate-sql");
    }

    public static List<ThreadItem> searchThreadItemByDecId(String threadEncId, String queryArg) {
        return getThreadItemsByDynQuery(threadEncId, queryArg, "search-thread-details-bydec-sql");
    }

    public static List<ThreadItem> searchThreadItemByHexId(String threadEncId, String queryArg) {
        return getThreadItemsByDynQuery(threadEncId, queryArg, "search-thread-details-byhex-sql");
    }

    public static List<ThreadSummary> searchThreadSummary() {
        List<ThreadSummary> threadItemList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(H2DB_CONFIG.getSqlMapping().get("search-thread-summary-sql"));
            rs = stmt.executeQuery();
            while (rs.next()) {
                threadItemList.add(new ThreadSummary(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("exec sql error", e);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return threadItemList;
    }

    public static List<ThreadItem> searchThreadItem(String threadEncId) {
        List<ThreadItem> threadItemList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(H2DB_CONFIG.getSqlMapping().get("search-thread-details-byall-sql"));
            stmt.setString(1, threadEncId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                threadItemList.add(extractThreadItem(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("exec sql error", e);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return threadItemList;
    }

    public static Connection getConnection() throws SQLException {
        return H2DB_POOL.getConnection();
    }

    public static void close(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("close jdbc error", e);
        }
    }

    public static void close(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("close jdbc error", e);
        }
    }

    public static void close(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("close jdbc error", e);
        }
    }
}
