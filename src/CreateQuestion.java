import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

public class CreateQuestion extends JFrame {
    //创建一个能够获取到所选按钮的类
    private static AbstractButton getSelectedRadioButton(ButtonGroup group) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button;
            }
        }
        return null;
    }
    public CreateQuestion() {
        setTitle("新建题目");
        setSize(500, 350);
        setResizable(false);             //设置不可调整大小
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout()); // 使用主面板
        JPanel heads = new JPanel(new FlowLayout(FlowLayout.LEFT));  //上边的面板

        panel.setPreferredSize(new Dimension(600, 400));
        // 创建科目名称标签
        JLabel questionNameLabel = new JLabel("科目名称: ");
        // 创建下拉选择框，并添加科目选项
        String[] subjects = {"语文", "数学", "英语","物理","化学","政治","历史"};
        JComboBox<String> subjectComboBox = new JComboBox<>(subjects);
        // 将标签和下拉选择框添加到面板中
        heads.add(questionNameLabel);
        heads.add(subjectComboBox);

        //添加题目类型选择
        JLabel questiontypeLabel = new JLabel("题目类型");
        String[] types = {"单选题","多选题","判断题","填空题","主观题"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        heads.add(questiontypeLabel);
        heads.add(typeComboBox);
        //添加题目名称
        JLabel questionLabel = new JLabel("题名");
        JTextField quesname = new JTextField(25);
        heads.add(questionLabel);
        heads.add(quesname);
        // 添加确认按钮
        JButton confirmButton = new JButton("确认");
        //创建一个放置按钮的面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirmButton);

        //创建中央的卡片布局
        JPanel Center = new JPanel();
        CardLayout cardLayout = new CardLayout();
        Center.setLayout(cardLayout);

        //添加不同类型题目对应的内容到面板
        //单选题面板
        JPanel singleChoicePanel = new JPanel(new GridLayout(4, 2)); // 使用4行2列的网格布局

        ButtonGroup optionGroup = new ButtonGroup();
// 创建单选按钮和文本框
        JRadioButton optionA = new JRadioButton("A");
        JTextField textFieldA = new JTextField(15); // 设置文本框宽度为20列
        JRadioButton optionB = new JRadioButton("B");
        JTextField textFieldB = new JTextField(15);
        JRadioButton optionC = new JRadioButton("C");
        JTextField textFieldC = new JTextField(15);
        JRadioButton optionD = new JRadioButton("D");
        JTextField textFieldD = new JTextField(15);

// 将单选按钮添加到按钮组
        optionGroup.add(optionA);
        optionGroup.add(optionB);
        optionGroup.add(optionC);
        optionGroup.add(optionD);

// 添加单选按钮和文本框到面板
        singleChoicePanel.add(optionA);
        singleChoicePanel.add(textFieldA);
        singleChoicePanel.add(optionB);
        singleChoicePanel.add(textFieldB);
        singleChoicePanel.add(optionC);
        singleChoicePanel.add(textFieldC);
        singleChoicePanel.add(optionD);
        singleChoicePanel.add(textFieldD);

        Center.add(singleChoicePanel, "单选题");

        //多选题面板
        JPanel multipleChoicePanel = new JPanel(new GridLayout(4,2));

// 创建按钮和文本框
        JCheckBox A = new JCheckBox("A");
        JTextField textFieldA1 = new JTextField(15); // 设置文本框宽度为20列
        JCheckBox B = new JCheckBox("B");
        JTextField textFieldB1 = new JTextField(15);
        JCheckBox C = new JCheckBox("C");
        JTextField textFieldC1 = new JTextField(15);
        JCheckBox D = new JCheckBox("D");
        JTextField textFieldD1 = new JTextField(15);
        //创建多选按钮数组加到其中
        JCheckBox[] boxs = {A,B,C,D};
        // 添加多选按钮和文本框到面板
        multipleChoicePanel.add(A);
        multipleChoicePanel.add(textFieldA1);
        multipleChoicePanel.add(B);
        multipleChoicePanel.add(textFieldB1);
        multipleChoicePanel.add(C);
        multipleChoicePanel.add(textFieldC1);
        multipleChoicePanel.add(D);
        multipleChoicePanel.add(textFieldD1);

        Center.add(multipleChoicePanel, "多选题");
        //判断题面板
        JPanel judge = new JPanel();
        ButtonGroup optionjudge = new ButtonGroup();
        //创建对错按钮
        JRadioButton dui = new JRadioButton("对");
        JRadioButton cuo = new JRadioButton("错");
        //加进按钮组
        optionjudge.add(dui);
        optionjudge.add(cuo);
        //放进面板
        judge.add(dui);
        judge.add(cuo);
        Center.add(judge,"判断题");

        //填空题面板
        JPanel tiankongtiPanel = new JPanel(new GridLayout(6, 2)); // 创建一个4行2列的网格布局，用于排列八个文本框

        JLabel label1 = new JLabel("答案1:");
        JTextField textField1 = new JTextField(10);
        JLabel label2 = new JLabel("答案2:");
        JTextField textField2 = new JTextField(10);
        JLabel label3 = new JLabel("答案3:");
        JTextField textField3 = new JTextField(10);
        JLabel label4 = new JLabel("答案4:");
        JTextField textField4 = new JTextField(10);
        JLabel label5 = new JLabel("答案5:");
        JTextField textField5 = new JTextField(10);
        JLabel label6 = new JLabel("答案6:");
        JTextField textField6 = new JTextField(10);
        //将Textfield放在数组中
        JTextField[] fields = {textField1,textField2,textField3,textField4,textField5,textField6};
        //创建的文本框放入面板
        tiankongtiPanel.add(label1);
        tiankongtiPanel.add(textField1);
        tiankongtiPanel.add(label2);
        tiankongtiPanel.add(textField2);
        tiankongtiPanel.add(label3);
        tiankongtiPanel.add(textField3);
        tiankongtiPanel.add(label4);
        tiankongtiPanel.add(textField4);
        tiankongtiPanel.add(label5);
        tiankongtiPanel.add(textField5);
        tiankongtiPanel.add(label6);
        tiankongtiPanel.add(textField6);


        Center.add(tiankongtiPanel,"填空题");

        //主观题面板
        JPanel zhuguantiPanel = new JPanel();
        JTextArea textArea = new JTextArea(125,55);//创建一个巨大的文本框
        textArea.setLineWrap(true); // 设置自动换行
        JScrollPane scrollPane = new JScrollPane(textArea); // 将文本区域添加到滚动窗格中
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 始终显示垂直滚动条
        zhuguantiPanel.add(scrollPane);
        Center.add(zhuguantiPanel,"主观题");

        //将三个部分进行整体上的组合
        panel.add(heads,BorderLayout.NORTH);
        panel.add(Center,BorderLayout.CENTER);
        panel.add(buttonPanel,BorderLayout.SOUTH);

        this.add(panel);
        this.setLocationRelativeTo(null);
        this.pack();

        this.setVisible(true);

        //选择题目的事件监听，动态选择面板
        typeComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem(); // 获取用户选择的题目类型
                cardLayout.show(Center,selectedType);
            }
        });

        //点击确认后的事件监听
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String questionName = (String) subjectComboBox.getSelectedItem();   //获取科目名称
                String questiontype = (String) typeComboBox.getSelectedItem();      //获取题目类型
                String realquesname = quesname.getText();                           //获取题名

                System.out.println(questionName);
                System.out.println(questiontype);
                System.out.println(realquesname);
                //新建一个问题字符串
                String question = new String();
                String answer =new String();

                if(questiontype.equals("单选题")){
                    AbstractButton selectedButton = getSelectedRadioButton(optionGroup);

                    question =  quesname.getText() + "? "
                                + optionA.getText() + ":" + textFieldA.getText() + " "
                                + optionB.getText() + ":" + textFieldB.getText() + " "
                                + optionC.getText() + ":" + textFieldC.getText() + " "
                                + optionD.getText() + ":" + textFieldD.getText();
                    answer =(selectedButton.getText());
                }
                else if (questiontype.equals("多选题")){
                    question =  quesname.getText() + "? "
                            + A.getText() + ":" + textFieldA1.getText() + " "
                            + B.getText() + ":" + textFieldB1.getText() + " "
                            + C.getText() + ":" + textFieldC1.getText() + " "
                            + D.getText() + ":" + textFieldD1.getText();
                    for (int i = 0; i < boxs.length; i++){
                        if(boxs[i].isSelected()){
                            answer += boxs[i].getText();
                        }
                    }
                }
                else if (questiontype.equals("判断题")){
                    AbstractButton selectedButton = getSelectedRadioButton(optionjudge);
                    question = quesname.getText() + "? ";
                    answer = selectedButton.getText();
                }
                else if (questiontype.equals("填空题")){
                    question =  quesname.getText() + "? ";
                    for (int i = 0; i < fields.length; i++){
                        answer += (fields[i].getText() + "_");
                    }
                }
                else if (questiontype.equals("主观题")){
                    question =  quesname.getText() + "? ";
                    answer = textArea.getText();
                }
                else{
                    System.out.println("出错啦！");
                }

                questiontype = ChineseToEnglishConverter.convertToEnglish(questiontype);    //将题目类型转成中文


                // 在此处添加保存题目逻辑
               if(Conn.insert(questiontype,question,answer,questionName)){
                    System.out.println(questiontype);
                    System.out.println(question);
                    System.out.println(answer);
                    System.out.println(questionName);

                    JOptionPane.showMessageDialog(null, "保存题目：" + questiontype + " 成功");
                }
                else{
                   JOptionPane.showMessageDialog(null, "保存题目：" + questiontype + " 失败");
               }
            }
        });
    }
}
