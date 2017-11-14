package pl.belcot;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame
{
    final JTextField loginField;
    final JTextField ipField;
    final JButton button;

    public LoginWindow()
    {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        pane.setPreferredSize(new Dimension(170, 160)); // zrobic marginesy
        setResizable(false);

        JLabel ipLabel = new JLabel("IP address: ");
        ipField = new JTextField();
        ipField.setHorizontalAlignment(SwingConstants.CENTER);
        ipField.setPreferredSize(new Dimension(170, 30));
        pane.add(ipLabel);
        pane.add(ipField);

        JLabel loginLabel = new JLabel("Identyfikator: ");
        loginField = new JTextField();
        button = new JButton("Zaloguj");
        this.getRootPane().setDefaultButton(button);
        loginField.setPreferredSize(new Dimension(100, 30));
        pane.add(loginLabel);
        pane.add(loginField);
        pane.add(button);

        setContentPane(pane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}