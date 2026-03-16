// DatabaseInspector.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInspector {

    // SQLite数据库连接URL - 修改为你的数据库路径
    private static final String DB_URL = "jdbc:sqlite:bookstore.db";

    public static void main(String[] args) {
        try {
            // 1. 加载SQLite JDBC驱动
            Class.forName("org.sqlite.JDBC");

            // 2. 连接到数据库
            try (Connection conn = DriverConnection()) {
                System.out.println("✅ 成功连接到数据库");
                System.out.println("=".repeat(50));

                // 3. 检查所有表结构
                inspectAllTables(conn);

                // 4. 检查表数据量
                inspectTableRowCounts(conn);

                // 5. 检查外键关系
                inspectForeignKeys(conn);

                // 6. 示例：查询前5条书籍数据
                sampleBookData(conn);

            }
        } catch (Exception e) {
            System.err.println("❌ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection DriverConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * 检查所有表的结构
     */
    private static void inspectAllTables(Connection conn) throws SQLException {
        System.out.println("📋 数据库表结构检查：");
        System.out.println("-".repeat(50));

        // 获取所有表名
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(tableName);
            }
        }

        // 检查每个表的结构
        for (String tableName : tables) {
            System.out.println("\n📁 表名: " + tableName);
            System.out.println("字段结构:");

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")")) {

                System.out.println("┌─────┬────────────────┬────────────┬─────────┬───────────────┐");
                System.out.println("│ CID │     字段名      │   类型     │ 允许空值 │    主键       │");
                System.out.println("├─────┼────────────────┼────────────┼─────────┼───────────────┤");

                while (rs.next()) {
                    int cid = rs.getInt("cid");
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int notNull = rs.getInt("notnull");
                    int pk = rs.getInt("pk");
                    String defaultValue = rs.getString("dflt_value");

                    System.out.printf("│ %-3d │ %-14s │ %-10s │   %-3s   │      %-3s      │\n",
                            cid, name, type,
                            notNull == 1 ? "否" : "是",
                            pk == 1 ? "是" : "否");

                    if (defaultValue != null) {
                        System.out.printf("│     │ 默认值: %-8s │            │         │               │\n",
                                defaultValue);
                    }
                }
                System.out.println("└─────┴────────────────┴────────────┴─────────┴───────────────┘");
            }
        }
    }

    /**
     * 检查每个表的数据量
     */
    private static void inspectTableRowCounts(Connection conn) throws SQLException {
        System.out.println("\n📊 表数据统计：");
        System.out.println("-".repeat(50));

        // 获取所有表名
        List<String> tables = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(tableName);
            }
        }

        for (String tableName : tables) {
            String sql = "SELECT COUNT(*) as count FROM " + tableName;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.printf("📈 %-15s: %d 条记录\n", tableName, count);
                }
            }
        }
    }

    /**
     * 检查外键关系
     */
    private static void inspectForeignKeys(Connection conn) throws SQLException {
        System.out.println("\n🔗 外键关系检查：");
        System.out.println("-".repeat(50));

        List<String> tables = new ArrayList<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(tableName);
            }
        }

        boolean hasForeignKeys = false;
        for (String tableName : tables) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("PRAGMA foreign_key_list(" + tableName + ")")) {

                while (rs.next()) {
                    if (!hasForeignKeys) {
                        hasForeignKeys = true;
                    }

                    int id = rs.getInt("id");
                    int seq = rs.getInt("seq");
                    String from = rs.getString("from");
                    String to = rs.getString("to");
                    String table = rs.getString("table");
                    String onUpdate = rs.getString("on_update");
                    String onDelete = rs.getString("on_delete");

                    System.out.printf("表: %s\n", tableName);
                    System.out.printf("  外键: %s → %s.%s\n", from, table, to);
                    System.out.printf("  更新规则: %s, 删除规则: %s\n", onUpdate, onDelete);
                    System.out.println();
                }
            }
        }

        if (!hasForeignKeys) {
            System.out.println("⚠️  未发现外键约束");
        }
    }

    /**
     * 示例：查询书籍数据
     */
    private static void sampleBookData(Connection conn) throws SQLException {
        System.out.println("\n📚 书籍数据示例（前5条）：");
        System.out.println("-".repeat(50));

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book LIMIT 5")) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 打印表头
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("│ %-15s ", metaData.getColumnName(i));
            }
            System.out.println("│");

            // 打印分隔线
            System.out.println("-".repeat(columnCount * 18));

            // 打印数据
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    String displayValue = (value == null) ? "NULL" : value.toString();
                    if (displayValue.length() > 15) {
                        displayValue = displayValue.substring(0, 12) + "...";
                    }
                    System.out.printf("│ %-15s ", displayValue);
                }
                System.out.println("│");
            }
        }

        // 检查是否有 category 表，并查询分类
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='category'")) {

            if (rs.next()) {
                System.out.println("\n🏷️  分类数据：");
                System.out.println("-".repeat(30));

                try (Statement stmt2 = conn.createStatement();
                     ResultSet rs2 = stmt2.executeQuery("SELECT * FROM category")) {

                    System.out.println("┌─────┬───────────────┐");
                    System.out.println("│ ID  │     名称      │");
                    System.out.println("├─────┼───────────────┤");

                    while (rs2.next()) {
                        int id = rs2.getInt("id");
                        String name = rs2.getString("name");
                        System.out.printf("│ %-3d │ %-13s │\n", id, name);
                    }
                    System.out.println("└─────┴───────────────┘");
                }
            }
        }
    }
}