import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionDisplayPanel extends JFrame {
    private JTextPane textPane;
    private JToolBar toolbar;
    private JButton importButton;
    private JButton editButton;

    private int count = 0;
    private int all =0;

    public QuestionDisplayPanel(File file) {
        super(file.getName());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // 居中显示窗口
        setLocationRelativeTo(null);

        // 创建一个 JTextPane 对象，赋值给 textPane
        textPane = new JTextPane();
        textPane.setFont(new Font("宋体", Font.PLAIN, 16));
        textPane.setEditable(false);
        if(file.getName().endsWith(".docx")||file.getName().endsWith(".doc")) {
            readDocxFile(file);
        } else if(file.getName().endsWith(".txt")) {
            readTxtFile(file);
        }

        // 创建工具栏
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        toolbar.setBorder(emptyBorder);

        // 创建导入按钮
        importButton = new JButton("导入");
        importButton.setToolTipText("导入到数据库");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importToDatabase();
                if(count == all){
                    String number = String.valueOf(count);
                    String message = "导入题库成功!\n成功导入"+number+"行题目!";
                    JOptionPane.showMessageDialog(null,message,"导入结果",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    String number = String.valueOf(count);
                    String All = String.valueOf(all);
                    String message = "导入失败!\n共有"+All+"行有效题目!\n已成功导入"+number+"行题目!";
                    JOptionPane.showMessageDialog(null,message,"导入结果",JOptionPane.WARNING_MESSAGE);
                }
                // 仅释放当前面板
                dispose();
            }
        });

        // 创建编辑按钮
        editButton = new JButton("编辑");
        editButton.setToolTipText("编辑内容");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPane.setEditable(true);
            }
        });

        // 添加按钮到工具栏
        toolbar.add(importButton);
        toolbar.add(editButton);

        // 添加工具栏到窗口
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);

        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 仅释放当前面板
                dispose();
            }
        });

        setVisible(true);
    }

    private void readDocxFile(File file) {
        XWPFDocument document = null;
        XWPFWordExtractor extractor = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            document = new XWPFDocument(fis);
            extractor = new XWPFWordExtractor(document);
            System.out.println(extractor.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            String text = "";
            for (XWPFParagraph paragraph : paragraphs) {
                text += paragraph.getText() + "\n";
            }
            textPane.setText(text);
        }
    }

    private void readTxtFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            String text = new String(data);
            textPane.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importToDatabase() {
        String[] lines = textPane.getText().split("\n");
        List<Map<String, String>> questionList = new ArrayList<>();
        boolean flags = true;
        for (String line : lines) {
            // 遍历 lines 数组，筛选非空行
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(" ");
                if (parts.length >=4 && parts.length <= 5 && parts[1].endsWith("?")) {
                    Map<String, String> questionMap = new HashMap<>();
                    questionMap.put("table", parts[0]);
                    questionMap.put("question", parts[1]);
                    questionMap.put("answer", parts[2]);
                    questionMap.put("kemu", parts[3]);
                    questionList.add(questionMap);
                } else if(parts.length >=8 && parts.length <= 9 && parts[0].contains("选") && parts[1].endsWith("?")){
                    Map<String, String> questionMap = new HashMap<>();
                    questionMap.put("table",parts[0]);
                    questionMap.put("question",parts[1]+" "+parts[2]+" "+parts[3]+" "+parts[4]+" "+parts[5]);
                    questionMap.put("answer",parts[6]);
                    questionMap.put("kemu",parts[7]);
                    questionList.add(questionMap);
                } else{
                    flags = false;
                }
            }
        }
        all = questionList.size();
        for (Map<String, String> questionMap : questionList) {
            String table = questionMap.get("table");
            String question = questionMap.get("question");
            String answer = questionMap.get("answer");
            String kemu = questionMap.get("kemu");
            if (Conn.insert(table, question, answer, kemu)) {
                count++;
            }
        }
        if(!flags){
            String message = "很抱歉，您的内容格式不符合要求!\n请检查是否存在多余的空格!";
            JOptionPane.showMessageDialog(null, message, "无效格式", JOptionPane.ERROR_MESSAGE);
        }

    }
}
