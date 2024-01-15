import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.*;

public class ExamManagementInterface extends JFrame {
    //构建方法来处理数据库返回的值为Object[];
    public List<Object[]> extractData(List<Map<String, Object>> dataList, String name,String choosenway) {
        List<Object[]> result = new ArrayList<>();
        if (choosenway.equals("按题目类型查询")) {
            for (Map<String, Object> row : dataList) {
                Object[] extractedRow = new Object[5];
                extractedRow[0] = ChineseToEnglishConverter.convertToChinese(name);
                extractedRow[1] = row.get("test_id");
                extractedRow[2] = row.get("question");
                extractedRow[3] = row.get("answer");
                extractedRow[4] = row.get("kemu");
                result.add(extractedRow);
            }
        }
        else if (choosenway.equals("按科目查询")){       //
            for (Map<String, Object> row : dataList) {
                Object[] extractedRow = new Object[5];
                extractedRow[0] = ChineseToEnglishConverter.convertToChinese((String)row.get("table"));
                extractedRow[1] = row.get("test_id");
                extractedRow[2] = row.get("question");
                extractedRow[3] = row.get("answer");
                extractedRow[4] =name;
                result.add(extractedRow);
            }
        }
        else{
            for (Map<String, Object> row : dataList) {
                Object[] extractedRow = new Object[5];
                extractedRow[0] = ChineseToEnglishConverter.convertToChinese((String)row.get("table"));
                extractedRow[1] = row.get("test_id");
                extractedRow[2] = row.get("question");
                extractedRow[3] = row.get("answer");
                extractedRow[4] =row.get("kemu");
                result.add(extractedRow);
            }
        }

        return result;
    }
    public ExamManagementInterface() {
        setTitle("试卷管理系统");
        setSize(600, 400);
        setResizable(false);             //设置不可调整大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout()); // 使用生成在左边
        panel.setSize(600, 400);
        JPanel Heads = new JPanel(new BorderLayout());
        JPanel heads = new JPanel(new FlowLayout(FlowLayout.LEFT));     //顶上三个按钮放在左边
        JPanel Hsound = new JPanel();                                   //导入导出放右边
        JPanel center = new JPanel(new BorderLayout());                 //中间放查询文本框
        JPanel chead = new JPanel(new BorderLayout());     //中间顶上放几个查找类
        JPanel chwest = new JPanel(new FlowLayout(FlowLayout.LEFT));    //西边放个选择查询方式
        JPanel chcenter = new JPanel();                                 //中间存个卡片
        JPanel cheast = new JPanel();                                   //东边放个关键字查询
        JPanel ccenter = new JPanel();                                  //中间底下放个表格

        //创建头上五个按钮
        JButton createQuestionButton = new JButton("新建题目");
        createQuestionButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); // 设置微软雅黑字体
        createQuestionButton.setBounds(0,0,10,3);
        JButton editQuestionButton = new JButton("编辑题目");
        editQuestionButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); // 设置微软雅黑字体
        JButton viewQuestionsButton = new JButton("查找题目");
        viewQuestionsButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); // 设置微软雅黑字体
        JButton importQuestionsButton = new JButton("导入题库");
        viewQuestionsButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); // 设置微软雅黑字体
        JButton outputQuestionsButton = new JButton("导出题库");
        viewQuestionsButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14)); // 设置微软雅黑字体

        //设置chwest
        JLabel choosenLabel = new JLabel("选择查询方式");
        String[] choosenway = {"按题目类型查询","按科目查询","按关键字查询"};
        JComboBox<String> chooseComboBox = new JComboBox<>(choosenway);
        chwest.add(choosenLabel);
        chwest.add(chooseComboBox);
        //设置chcenter
        CardLayout cardLayout = new CardLayout();
        chcenter.setLayout(cardLayout);
        //设置查询的几个选择(chcenter中进行)
        //按题目类型查询
        JPanel timuPanel = new JPanel();
        JLabel typeLabel= new JLabel("选择题型");
        String[] types = {"单选题","多选题","判断题","填空题","主观题"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        timuPanel.add(typeLabel);
        timuPanel.add(typeComboBox);
        chcenter.add(timuPanel,"按题目类型查询");
        //按科目查询
        JPanel kemuPanel = new JPanel();
        JLabel kemuLabel = new JLabel("选择科目");
        String[] kemus = {"语文", "数学", "英语","物理","化学","政治","历史"};
        JComboBox<String> kemuComboBox = new JComboBox<>(kemus);
        kemuPanel.add(kemuLabel);
        kemuPanel.add(kemuComboBox);
        chcenter.add(kemuPanel,"按科目查询");
        //按关键字查询
        JPanel keyPanel = new JPanel();
        chcenter.add(keyPanel,"按关键字查询");
        //创建选择查询方式的事件监听
        chooseComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedway = (String) chooseComboBox.getSelectedItem(); // 获取用户选择的查询方式
                System.out.println(selectedway);
                cardLayout.show(chcenter,selectedway);
            }
        });
        //设置cheast
        JLabel keyLabel= new JLabel("请输入关键词");
        JTextField keywordField = new JTextField();
        keywordField.setPreferredSize(new Dimension(150, 25)); // 设置文本输入框的大小
        cheast.add(keyLabel);
        cheast.add(keywordField);
        //设置chead
        chead.add(chwest,BorderLayout.WEST);
        chead.add(chcenter,BorderLayout.CENTER);
        chead.add(cheast,BorderLayout.EAST);

        //设置中间出现的查询表格
        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {    //重写方法设置为不可编辑
                return false;
            }
        };
        JTable table = new JTable(model);
        // 添加表头
        model.addColumn("题目类型");
        model.addColumn("题目编号");
        model.addColumn("题目内容");
        model.addColumn("题目答案");
        model.addColumn("题目科目");


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 270)); // 设置首选大小
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条

        ccenter.add(scrollPane);
        //构建center
        center.add(chead,BorderLayout.NORTH);
        center.add(ccenter,BorderLayout.CENTER);
        //将按钮加入heads
        heads.add(createQuestionButton);
        heads.add(editQuestionButton);
        heads.add(viewQuestionsButton);

        Hsound.add(importQuestionsButton);
        Hsound.add(outputQuestionsButton);

        Heads.add(heads,BorderLayout.WEST);
        Heads.add(Hsound,BorderLayout.EAST);
        //导入面板
        panel.add(Heads,BorderLayout.NORTH);
        panel.add(center,BorderLayout.CENTER);

        add(panel);
        setLocationRelativeTo(null); // 将窗口显示在屏幕中央
        setVisible(true);

        //创建问题的事件监听
        createQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateQuestion createface = new CreateQuestion();        //调用创建问题的类。
                createface.setVisible(true);
            }
        });
        //编辑问题的事件监听
        editQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { // 检查是否有选中的行
                    // 获取选中的题目数据
                    String type = (String) table.getValueAt(selectedRow, 0);
                    String number = table.getValueAt(selectedRow, 1).toString();
                    String content = (String) table.getValueAt(selectedRow, 2);
                    String answer = (String) table.getValueAt(selectedRow, 3);
                    String subject = (String) table.getValueAt(selectedRow, 4);
                    // 在此处添加编辑问题的逻辑，传入选中题目的数据
                    EditQuestion editface = new EditQuestion(type, number, content, answer, subject);
                    editface.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "请选择一个题目");
                }
            }
        });
        //创建一个实例化的row
        Object[] row;
        //查看问题的事件监听
        viewQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //清空model
                model.setRowCount(0);
                // 在此处添加查看问题的逻辑
                String choosenway =(String)chooseComboBox.getSelectedItem();
                if (choosenway.equals("按题目类型查询")) {
                    String selectedType = ChineseToEnglishConverter.convertToEnglish((String) typeComboBox.getSelectedItem()); // 获取用户选择的题目类型
                    String keyword = keywordField.getText();
                    if (keyword == null){      //如果关键字为空，按表格查询
                        List<Map<String, Object>> data = Conn.findDataByTable(selectedType);
                        List<Object[]> extractedData = extractData(data,selectedType,choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for(Object[] row : extractedData){
                            model.addRow(row);
                        }
                    }
                    else{                           //关键字不为空，调用findDataByTitle函数
                        List<Map<String, Object>> data = Conn.findDataByTitle(selectedType, "question",keyword);
                        for (Map<String, Object> map : data) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                System.out.println("Key: " + key + ", Value: " + value);
                            }
                        }
                        List<Object[]> extractedData = extractData(data,selectedType,choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for(Object[] row : extractedData){
                            model.addRow(row);
                        }
                    }
                }
                else if (choosenway.equals("按科目查询")){
                    String keyword = keywordField.getText();
                    String selectedkemu = (String) kemuComboBox.getSelectedItem();//获取用户选择的科目类型
                    if (keyword == null) {                                                          //按科目面板查询

                        List<Map<String, Object>> data = Conn.findDataByField("kemu", selectedkemu);
                        for (Map<String, Object> map : data) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                System.out.println("Key: " + key + ", Value: " + value);
                            }
                        }
                        List<Object[]> extractedData = extractData(data, selectedkemu, choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for (Object[] row : extractedData) {
                            model.addRow(row);
                        }
                        System.out.println(selectedkemu);
                    }
                    else{
                        List<Map<String, Object>> data = Conn.findDataByFields("kemu",selectedkemu,keyword);
                        for (Map<String, Object> map : data) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                System.out.println("Key: " + key + ", Value: " + value);
                            }
                        }
                        List<Object[]> extractedData = extractData(data, selectedkemu, choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for (Object[] row : extractedData) {
                            model.addRow(row);
                        }
                        System.out.println(selectedkemu);
                    }
                }
                else{
                    String keyword = keywordField.getText();

                    if (keyword == null) {              //获取全部的内容
                        List<Map<String, Object>> data = Conn.findAllData();
                        for (Map<String, Object> map : data) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                System.out.println("Key: " + key + ", Value: " + value);
                            }
                        }
                        List<Object[]> extractedData = extractData(data, keyword, choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for (Object[] row : extractedData) {
                            model.addRow(row);
                        }
                        System.out.println(keyword);
                    }
                    else{                           //单独按关键词查询
                        List<Map<String, Object>> data = Conn.findDataByField("question", keyword);
                        for (Map<String, Object> map : data) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                System.out.println("Key: " + key + ", Value: " + value);
                            }
                        }
                        List<Object[]> extractedData = extractData(data, keyword, choosenway);
                        //将转换的数据遍历，添加到显示的面板中
                        for (Object[] row : extractedData) {
                            model.addRow(row);
                        }
                        System.out.println(keyword);
                    }
                }

            }
        });
        //table点击事件监听器
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    // 获取选中行的数据，并进行相应处理
                    String type = (String) target.getValueAt(row, 0);
                    String number =target.getValueAt(row, 1).toString();
                    String content = (String) target.getValueAt(row, 2);
                    String answer = (String) target.getValueAt(row, 3);
                    String subject = (String) target.getValueAt(row, 4);
                    System.out.println("点击的行: " + type + " " + number + " " + content + " " + answer + " " + subject);
                }
            }
        });
        //导入题库按钮逻辑
        importQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 实现打开文件目录并选择文件的逻辑
                JFileChooser fileChooser = new JFileChooser();
                int choice = fileChooser.showOpenDialog(null);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    // 用户选择了文件
                    // 处理选定的文件
                    // 可以使用fileChooser.getSelectedFile()来获取用户选择的文件
                    // 这里可以添加相应的处理逻辑
                    File file = fileChooser.getSelectedFile();
                    String filePath = fileChooser.getSelectedFile().getPath();
                    if (filePath.endsWith(".docx") || filePath.endsWith(".doc")||filePath.endsWith(".txt")) {
                        if(shouldShowDialog()){
                        // 创建提示信息的文字内容
                        String message = "内容全以空格作为分隔符号!\n如果是英文科目,问题内容请以.或者,号作为分隔符!\n题目类型(单选题、多选题、填空题、判断题、主观题)<空格>题目内容?<空格>选项<空格>答案<空格>科目\n" +
                                "中文示例：单选题 新中国于公元多少年建立? A:1949年 B:1840年 C:1900年 D:2000年 A 历史" +
                                "\n英文示例：单选题 How.are.you? A:Thank.you. B:Yes C:No D:I'm.fine,thank.you.";

                        Object[] options = {"下次不再显示", "确定"};

                        int choice1 = JOptionPane.showOptionDialog(null, message, "导入题库内容格式",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                null, options, options[1]);

                        if (choice1 == 0) {
                            // 用户选择了下次不再显示
                            saveConfigToFile();
                        }
                        }
                        new QuestionDisplayPanel(file);
                        System.out.println("是docx文件");
                    } else {
                        // 文件类型不符合，执行以下代码
                        // 弹出提示框，要求用户重新选择文件
                        JOptionPane.showMessageDialog(null, "只允许传入文本文件!\n如:docx、doc、txt的文件", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        //导出题库按钮逻辑
        outputQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                //加入用户可选择保存的文件类型
                FileNameExtensionFilter filterDocx = new FileNameExtensionFilter("docx","docx");
                FileNameExtensionFilter filterDoc = new FileNameExtensionFilter("doc","doc");
                FileNameExtensionFilter filterTxt = new FileNameExtensionFilter("txt","txt");

                fileChooser.setFileFilter(filterDocx);
                fileChooser.addChoosableFileFilter(filterDoc);
                fileChooser.addChoosableFileFilter(filterTxt);

                int choice = fileChooser.showSaveDialog(null);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filename = fileChooser.getSelectedFile().toString();
                        String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];

                        // 如果用户没有输入文件扩展名，则将所选的文件扩展名添加到文件名中
                        if (!filename.toLowerCase().endsWith("." + extension)) {
                            filename += "." + extension;
                        }

                        // 在这里根据用户选择的文件类型来确定生成的文件类型
                        if (extension.equals("docx") || extension.equals("doc")) {
                            // 创建docx或doc文件并写入数据
                            XWPFDocument document = new XWPFDocument();
                            //在这里开始获取数据！
                            List<Map<String, Object>> data = Conn.findAllData();
                            List<Object[]> extractedData = extractData(data," "," ");
                            //将所有数据连接到r
                            for (Object[] rowo : extractedData) {       //一次次地创建一个段来存入数据！

                                XWPFParagraph paragraph = document.createParagraph();
                                XWPFRun run = paragraph.createRun();        //在循环内创建一个新的运行对象
                                ArrayList<Object> datalist = new ArrayList<>(Arrays.asList(rowo));
                                datalist.remove(1);                 //删除题目id
                                rowo = datalist.toArray(new Object[0]);
                                String dataFromDatabase = "";               //设置一个初始的空字符串
                                for (int i = 0; i < rowo.length-1; i++){
                                    dataFromDatabase += rowo[i] + " ";
                                }
                                dataFromDatabase += rowo[rowo.length-1];        //最后一个字符不要加入空格
                                run.setText(dataFromDatabase);      //将每一行的数据存入
                            }
                            //创建新文件
                            System.out.print("First line" + System.lineSeparator() + "Second line");
                            FileOutputStream out = new FileOutputStream(filename);
                            document.write(out);                                            //设置要传入的文件名称
                            out.close();
                            System.out.println("导出成功");
                            JOptionPane.showMessageDialog(null,"导出为doc/docx文档成功");
                        }  else if (extension.equals("txt")) {
                            // 创建txt文件并写入数据
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                            //在这里开始获取数据！
                            List<Map<String, Object>> data = Conn.findAllData();
                            List<Object[]> extractedData = extractData(data," "," ");
                            //将所有数据连接到r
                            for (Object[] rowo : extractedData) {
                                ArrayList<Object> datalist = new ArrayList<>(Arrays.asList(rowo));
                                datalist.remove(1);                 //删除题目id
                                rowo = datalist.toArray(new Object[0]);
                                String dataFromDatabase = "";               //设置一个初始的空字符串
                                for (int i = 0; i < rowo.length-1; i++){
                                    dataFromDatabase += rowo[i] + " ";
                                }
                                dataFromDatabase += rowo[rowo.length-1];        //最后一个字符不要加入空格
                                dataFromDatabase += '\n';                       //加入换行符
                                writer.write(dataFromDatabase);      //将每一行的数据存入
                                writer.flush();
                                System.out.println(dataFromDatabase);
                            }
                            writer.close();
                            System.out.println("导出成功");
                            JOptionPane.showMessageDialog(null,"导出为txt文档成功");
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"尚不支持该类型文档的导出");
                        }

                    } catch (IOException e1) {
                        System.out.println("导出失败：" + e1.getMessage());
                        JOptionPane.showMessageDialog(null, "导出失败！");
                    }
                }
            }
        });
    }
    // 判断是否应该显示弹窗
    public static boolean shouldShowDialog() {
        // 获取当前工作目录
        String workingDir = System.getProperty("user.dir");
        String configFile = workingDir + File.separator + "config.properties";
        // 检查配置文件是否存在，如果不存在则创建
        File file = new File(configFile);
        if (!file.exists()) {
            try {
                // 创建配置文件
                file.createNewFile();

                // 设置默认配置
                PrintWriter writer = new PrintWriter(new FileWriter(file));
                writer.println("nextTimeNotShow=false");
                writer.close();

                System.out.println("配置文件已创建：" + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String nextTimeNotShow = properties.getProperty("nextTimeNotShow");
        return nextTimeNotShow == null || !nextTimeNotShow.equals("true");
    }
    // 保存配置到配置文件
    private static void saveConfigToFile() {
        Properties properties = new Properties();

        // 设置下次不再显示的值为true
        properties.setProperty("nextTimeNotShow", "true");

        try {
            FileOutputStream fos = new FileOutputStream("config.properties");
            properties.store(fos, "Configuration");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ExamManagementInterface examInterface = new ExamManagementInterface();
        examInterface.setVisible(true);
    }
}
