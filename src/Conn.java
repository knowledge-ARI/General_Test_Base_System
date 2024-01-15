import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conn { // 创建类Conn
    private static Connection con; // 声明Connection对象
    private static ResultSet rs = null;
    private static PreparedStatement ps = null;
    private static String pwd = null;

    private static final String DRIVERCLASS= "org.sqlite.JDBC";
    private static final String projectPath = System.getProperty("user.dir");
    private static final String URL = "jdbc:sqlite:" + projectPath + "/sqllite.db";

    //URL="jdbc:sqlite:D://submit_classroom-master//sqllite.db";

 /*   public Conn getcon(){

        return
    }*/
    /**
     * 数据库连接函数
     * */
    //获取数据库连接对象
    public Connection getConnection() throws ClassNotFoundException,SQLException{
        Class.forName(DRIVERCLASS);//加载驱动程序
        //建立与数据库的连接
        con=DriverManager.getConnection(URL);
        return con;
    }


static{
    Conn conn = new Conn();
    try {
        con = conn.getConnection();
        System.out.println("数据库连接成功-" + con);
    } catch (ClassNotFoundException e) {
        System.out.println("数据库连接失败-" + e.getMessage());
    } catch (SQLException e) {
        System.out.println("数据库连接失败-" + e.getMessage());
    }
}



    /**
     * 数据库连接函数
     *
     */
    /*public Connection getConnection() { // 建立返回值为Connection的方法
        try { // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        user = "tiku123";//数据库登录名
        password = "tiku123";//密码
        try { // 通过访问数据库的URL获取数据库连接对象
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiku?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8", user, password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con; // 按方法要求返回一个Connection对象
        }*/
/*static
{
    try{
        Conn c = new Conn(); // 创建本类对象
        c.getConnection();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}*/



    /**
     *通过题目内容找题号
     * @param keyword 关键词
     * @param table 表名
     */
    public int findIdByTitle(String table, String keyword) {
        reorderNumber(table);
        int number = -1;
        String sql = "SELECT test_id FROM "+ table+ " WHERE question LIKE ? COLLATE BINARY";

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                number = resultSet.getInt("test_id");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return number;
    }

    /**
     * 导出所有题目数据
     * 使用默认表名直接拿取数据
     * */
    public static List<Map<String, Object>> findAllData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<Integer,String> table = new HashMap<>();
        table.put(1,"multiple");
        table.put(2,"multiples");
        table.put(3,"fill");
        table.put(4,"judge");
        table.put(5,"subjective");
        try {
            for(int i = 1;i < 6;i++) {
                String sql = "SELECT test_id, question, answer, kemu FROM " + table.get(i);
                PreparedStatement statement = con.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("table",table.get(i));
                    row.put("test_id", resultSet.getInt("test_id"));
                    row.put("question", resultSet.getString("question"));
                    row.put("answer", resultSet.getString("answer"));
                    row.put("kemu", resultSet.getString("kemu"));
                    data.add(row);
                }
                resultSet.close();
                statement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    /**
     * 表名查找
     * 通过表名查找数据(这里就是题目类型)
     * @param table 表名
     * */
    public static List<Map<String, Object>> findDataByTable(String table) {
        reorderNumber(table);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT test_id, question, answer, kemu FROM " + table;

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("table",table);
                row.put("test_id", resultSet.getInt("test_id"));
                row.put("question", resultSet.getString("question"));
                row.put("answer", resultSet.getString("answer"));
                row.put("kemu", resultSet.getString("kemu"));
                data.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    /**
     * 查找所有表
     * 通过一个字段名内容查找所有表的内容
     * @param field 字段名
     * @param value 字段内容
     * */
    public static List<Map<String, Object>> findDataByField(String field, String value) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<Integer,String> table = new HashMap<>();
        table.put(1,"multiple");
        table.put(2,"multiples");
        table.put(3,"fill");
        table.put(4,"judge");
        table.put(5,"subjective");
        try {
                for(int i = 1;i < 6;i++) {
                    String sql2 = "SELECT test_id, question, answer, kemu FROM " + table.get(i) + " WHERE " + field + " LIKE ?";

                    PreparedStatement preparedStatement = con.prepareStatement(sql2);
                    preparedStatement.setString(1, "%" + value + "%");

                    ResultSet resultSet2 = preparedStatement.executeQuery();

                    while (resultSet2.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("table", table.get(i));
                        row.put("test_id", resultSet2.getInt("test_id"));
                        row.put("question", resultSet2.getString("question"));
                        row.put("answer", resultSet2.getString("answer"));
                        row.put("kemu", resultSet2.getString("kemu"));
                        data.add(row);
                    }
                    resultSet2.close();
                    preparedStatement.close();
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    /**
     * 查找所有表
     * 通过两个字段名内容查找所有表的内容
     * @param column 字段名
     * @param value column字段内容
     * @param value2 默认字段的内容
     * */
    public static List<Map<String, Object>> findDataByFields(String column, String value,String value2) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<Integer,String> table = new HashMap<>();
        table.put(1,"multiple");
        table.put(2,"multiples");
        table.put(3,"fill");
        table.put(4,"judge");
        table.put(5,"subjective");
        try {
            for(int i = 1;i < 6;i++) {
                String sql2 = "SELECT test_id, question, answer, kemu FROM " + table.get(i) + " WHERE " + column + " LIKE ? AND question LIKE ?";

                PreparedStatement preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setString(1, "%" + value + "%");
                preparedStatement.setString(2, "%" + value2 + "%");
                ResultSet resultSet2 = preparedStatement.executeQuery();

                while (resultSet2.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("table", table.get(i));
                    row.put("test_id", resultSet2.getInt("test_id"));
                    row.put("question", resultSet2.getString("question"));
                    row.put("answer", resultSet2.getString("answer"));
                    row.put("kemu", resultSet2.getString("kemu"));
                    data.add(row);
                }
                resultSet2.close();
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }



    /**
     * 查找单个表
     * 根据一个字段内容查找一个表中的内容
     * @param table 表名
     * @param keyword 关键词
     * */
    public static List<Map<String, Object>> findDataByTitle(String table,String column, String keyword) {
        reorderNumber(table);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT test_id, question, answer, kemu FROM " + table + " WHERE "+column+" LIKE ?";

        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("test_id", resultSet.getInt("test_id"));
                row.put("question", resultSet.getString("question"));
                row.put("answer", resultSet.getString("answer"));
                row.put("kemu", resultSet.getString("kemu"));
                data.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    /**
     * 序号(test_id)查找
     * 序号查找题目内容
     * @param table 表名
     * @param number 序号(题号)
     *
     **/
    public List<Map<String, Object>> findDataById(String table, String number){//根据序号查找题目内容以及答案
        String sql = "SELECT question,answer,kemu FROM " + table + " WHERE test_id ="+number;
        List<Map<String, Object>> data = new ArrayList<>();
        //声明stmt对象
        Statement stmt = null;
        //声明rs对象
        ResultSet rs = null;
        try{
            //创建stmt对象
            stmt = con.createStatement();
            //执行查询语句
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("question", rs.getString("question"));
                row.put("answer", rs.getString("answer"));
                row.put("kemu", rs.getString("kemu"));
                data.add(row);
            }
        }catch (SQLException e){
            //打印异常信息
            e.printStackTrace();
            //关闭rs对象
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            //关闭stmt对象
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            //返回空值
            return null;
        }
        return data;

    }

    /**
     * 增添题目
     * @param table 表名
     * @param question 问题字段值
     * @param answer 答案字段值
     * @param kemu 科目字段值
     */
    public static boolean insert(String table, String question, String answer, String kemu) {
        boolean result = false;
        switch (table) {
            case "单选题": table="multiple";break;
            case "多选题": table="multiples";break;
            case "填空题": table="fill";break;
            case "主观题": table="subjective";break;
            case "判断题": table="judge";break;
            default: if(table.startsWith("m")||table.endsWith("e")||table.endsWith("l")) {break;}
                else {return result;}
        }
        reorderNumber(table);
        List<Map<String, Object>> data = Conn.findDataByTitle(table,"question",question);
        if(!data.isEmpty()){return result;}
        //构造SQL语句，插入所有字段内容
        String sql = "INSERT INTO " + table + " (question, answer, kemu) VALUES (?, ?, ?)";
        try {
            //创建一个预编译语句对象
            PreparedStatement statement = con.prepareStatement(sql);
            //设置参数，分别是question, answer, kemu的内容
            statement.setString(1, question);
            statement.setString(2, answer);
            statement.setString(3, kemu);
            //执行SQL语句，返回影响的行数
            int rows = statement.executeUpdate();
            //如果影响的行数大于0，表示插入成功，返回true
            if (rows > 0) {
                result = true;
            }
            //关闭预编译语句对象
            statement.close();
            reorderNumber(table);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //返回结果
        return result;
    }


    /**
     * 修改
     * 修改字符串类型的数据
     * @param table 数据表名
     * @param number 题号
     * @param column 列名
     * @param newValue 新值
     */
    //向表中更新数据，参数为表名、题号、列名、值
    public void update(String table, String number,String column,String newValue){
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE " + table + " SET " + column + " = ? WHERE test_id = "+number);
            ps.setString(1,newValue);
            int rows = ps.executeUpdate();
            if(rows > 0){
                System.out.println("Something have been updated.");
            }
            ps.close();
            reorderNumber(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重排序号
     * @param table 表名
     */

    public static void reorderNumber(String table) {
        try {
            // 创建一个Statement对象，用于执行SQL语句
            Statement stmt = con.createStatement();
            // 创建一个临时表，将原表的数据复制到临时表中，但不包括主键列
            // 假设你的主键列是test_id，其他列是question,answer,kemu
            String sql = "CREATE TEMPORARY TABLE temp_table AS SELECT question,answer,kemu FROM " + table;
            stmt.executeUpdate(sql);
            // 删除原表的数据，重置主键列的自增属性
            // 使用 DELETE FROM 语句代替 TRUNCATE TABLE 语句
            sql = "DELETE FROM " + table;
            stmt.executeUpdate(sql);
            // 使用 UPDATE SQLITE_SEQUENCE 语句来重置主键列的自增属性
            sql = "UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '" + table + "'";
            stmt.executeUpdate(sql);
            // 将临时表的数据插入到原表中，让主键列自动增长
            sql = "INSERT INTO " + table + " (question,answer,kemu) SELECT question,answer,kemu FROM temp_table";
            stmt.executeUpdate(sql);
            // 删除临时表
            sql = "DROP TABLE temp_table";
            stmt.executeUpdate(sql);
            // 关闭Statement对象
            stmt.close();
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        }
    }


/*    public static void reorderNumber(String table) {
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        try {

            // 获取当前最大的 test_id
            String sql1 = "SELECT MAX(test_id) FROM " + table;
            pstmt1 = con.prepareStatement(sql1);
            ResultSet rs = pstmt1.executeQuery();
            int maxTestId = rs.getInt(1);

            // 重排序 test_id
            for (int i = 1; i <= maxTestId; i++) {
                String sql2 = "UPDATE " + table + " SET test_id = ? WHERE test_id = ?";
                pstmt2 = con.prepareStatement(sql2);
                pstmt2.setInt(1, i);
                pstmt2.setInt(2, i + 1);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (pstmt1 != null) {
                    pstmt1.close();
                }
                if (pstmt2 != null) {
                    pstmt2.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }*/

/*    public static void reorderNumber(String table) {
        try {
            String sql = "SET @num := 0;";
            Statement stmt = con.createStatement();
            stmt.execute(sql);

            sql = "UPDATE " + table + " SET test_id = @num := (@num+1);";
            stmt.execute(sql);

            sql = "ALTER TABLE " + table + " AUTO_INCREMENT = 1;";//通过 ALTER TABLE 语句将表的自增起始值重置为 1
            stmt.execute(sql);
            stmt.close();;

            System.out.println("Reordering completed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 删除
     * 根据某一字段的内容删除某表中的符合条件的内容
     * @param table 表名
     * @param column 列名
     * @param value 值
     *
     */
    public static boolean delete(String table, String column,String value){
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM "+table+" WHERE "+column+" = ?");
            ps.setString(1,value);
            int rows = ps.executeUpdate();//用于执行 SQL DELETE 语句，并返回删除的行数。
            if(rows > 0 ){
                System.out.println("Some data have been deleted");
                return true;
            }
            ps.close();
            reorderNumber(table);
        }catch (SQLException ex){
            ex.printStackTrace();

        }
        return false;
    }

    /**
     * 用于修改以及获取私有成员变量(密码)的值
     * @param pwd 密码
     *
     * */
    public static String changePwd(String pwd){
        return Conn.pwd=pwd;
    }
    public static String getPwd(){
        return Conn.pwd;
    }

    /**
     * 增添用户名与密码
     * @param username 用户名
     * @param password 密码
     * */
    public  static boolean insert_user(String username,String password){
        boolean result =false;
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            //创建一个预编译语句对象
            PreparedStatement statement = con.prepareStatement(sql);
            //设置参数，分别是question, answer, kemu的内容
            statement.setString(1, username);
            statement.setString(2, password);
            //执行SQL语句，返回影响的行数
            int rows = statement.executeUpdate();
            //如果影响的行数大于0，表示插入成功，返回true
            if (rows > 0) {
                result = true;
            }
            //关闭预编译语句对象
            statement.close();
            reorderNumber("users");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean update_user(String username,String password){
        boolean result =false;
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1,username);
            statement.setString(2,password);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                result = true;
            }
            statement.close();
            reorderNumber("users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * 查找用户名与密码
     * @param username 用户名
     * */
    public static void queryUser(String username) throws SQLException {
        try {
            ps = con.prepareStatement("select * from users where userName=? ");
            //ps = conn.prepareStatement("select * from user_info where role=? and userName=? ");
            ps.setString(1, username);
            rs = ps.executeQuery();
            // 循环取出
            if (rs.next()) {
                String pwd = Conn.changePwd(rs.getString("password"));
                System.out.println("成功获取到密码和用户名from数据库");
                System.out.println("用户：" + username + "\t 密码：" + pwd + "\t");

            } else {
                JOptionPane.showMessageDialog(null, "没有此用户，请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }finally {
            ps.close();
            rs.close();
        }
    }

/*    public static void main(String[] args) throws Exception {
        Conn conn = new Conn();
        try {
            con = conn.getConnection();
            System.out.println("数据库连接成功-" + con);
        } catch (ClassNotFoundException e) {
            System.out.println("数据库连接失败-" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("数据库连接失败-" + e.getMessage());
        }
*//*        conn.delete("multiple","kemu","数学");
        conn.update("multiple","3","kemu","数学");
        int num = conn.findIdByTitle("multiple","a");
        System.out.println(num);*//*



        *//*Conn.reorderNumber("multiple");*//*
*//*        List<Map<String, Object>> data = Conn.findDataByField("kemu","语文");

//使用for循环或者stream流来遍历这个列表，取出每个Map中的键值对，例如：
        for (Map<String, Object> map : data) {
            String tableName = (String)map.get("table");
            if(tableName.equals("multiple")){
                tableName = "单选题";
            }else if(tableName.equals("multiples")){
                tableName = "多选题";
            }else if(tableName.equals("judge")){
                tableName = "判断题";
            }else if(tableName.equals("fill")){
                tableName = "填空题";
            }else if(tableName.equals("subjective")){
                tableName = "主观题";
            }
            //取出test_id的值，注意要转换成Integer类型
            Integer test_id = (Integer) map.get("test_id");
            //取出question的值，注意要转换成String类型
            String question = (String) map.get("question");
            //取出answer的值，注意要转换成String类型
            String answer = (String) map.get("answer");
            //取出kemu的值，注意要转换成String类型
            String kemu = (String) map.get("kemu");
            //打印或者处理这些值
            System.out.println(tableName+" "+test_id + " " + question + " " + answer + " " + kemu);
        }*//*
        *//*if(Conn.insert("judge","你认为弱国无外交的观点是正确的吗?","对","历史")) System.out.println("插入成功");*//*
    }*/
}


/*    *//*
        static String currentSubject="数学单选";
        static String currentType="题目";*//*
    static int num = 1;*/