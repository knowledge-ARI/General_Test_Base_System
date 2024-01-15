import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("登录");
        setSize(600, 450); // 增大窗口尺寸
        setResizable(false);             //设置不可调整大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout()){     // 使用GridBagLayout来生成在中间
            @Override
            protected void paintComponent(Graphics g) {     //插入图片
                super.paintComponent(g);
                URL imgUrl = getClass().getResource("/imgs/login.png");
                Image image = new ImageIcon(imgUrl).getImage();
                int x = (this.getWidth() - image.getWidth(null)) / 2;  // 计算图片在面板中的水平居中位置
                int y = (this.getHeight() - image.getHeight(null)) / 2; // 计算图片在面板中的垂直居中位置
                g.drawImage(image, x, y, this);
            }
        };
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setPreferredSize(new Dimension(600, 450));


        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 25)); // 设置微软雅黑字体
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(350,35));
        usernameField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 25));
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 25)); // 设置微软雅黑字体
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(350,35));
        passwordField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 25));
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 25));
        loginButton.addActionListener(this);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.insets = new Insets(10,0,0,0); // 添加空白区域以调整按钮位置
        //放在south

       panel.add(loginButton, constraints);

        add(panel);
        setLocationRelativeTo(null); // 将窗口显示在屏幕中央
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 进行用户名和密码验证逻辑
        if (!username.isEmpty() && !password.isEmpty()) {
            try {
                Conn.queryUser(username);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if(password.equals(Conn.getPwd())) {
                JOptionPane.showMessageDialog(this, "登录成功!");
                System.out.println(password);
                // 启动试题管理界面
                ExamManagementInterface examInterface = new ExamManagementInterface();
                examInterface.setVisible(true);
                this.dispose(); // 关闭登录界面
            }else{
                JOptionPane.showMessageDialog(this, "密码错误!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码不能为空");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
