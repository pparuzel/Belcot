package pl.belcot;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;

public class BelcotUI extends JFrame
{
    private final JCheckBox tick;
    private JTextArea inputArea;
    JTextArea outputArea;
    JButton button;
    DefaultListModel<String> model;
    JList<String> displayList;
    String selected;

    public BelcotUI(ClientThread client, String username, DefaultListModel<String> model)
    {
        super(username);
        this.model = model;
        selected = "";
        displayList = new JList<>(model);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        outputArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        outputArea.setEditable(false);
        JScrollPane scrollOutput = new JScrollPane(outputArea);
        scrollOutput.createVerticalScrollBar();
        scrollOutput.setPreferredSize(new Dimension(300, 250));
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        JScrollPane scrollInput = new JScrollPane(inputArea);
        scrollInput.setPreferredSize(new Dimension(270, 100));
        inputArea.addKeyListener(new KeyAdapter()
        {
            String text = "";
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(!tick.isSelected() && e.getKeyChar() == KeyEvent.VK_ENTER && !inputArea.getText().equals("") && !selected.equals(""))
                {
                    Message.Text mesg =
                            new Message.Text(inputArea.getText(), username, selected, Calendar.getInstance().getTime());
                    client.send(mesg);
                    inputArea.setText("");
                    outputArea.append(mesg+"\n");
                }
                else if(e.getKeyChar() == KeyEvent.VK_TAB)
                {
                    text = inputArea.getText();
                }
            }

            @Override
            public void keyTyped(KeyEvent e)
            {
                if(!tick.isSelected() && e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    inputArea.setText("");
                }
                else if(e.getKeyChar() == KeyEvent.VK_TAB)
                {
                    inputArea.setText(text);
                }
            }
        });
        button = new JButton("Send");
        button.addActionListener(e ->
        {
            if(e.getSource().equals(button) && !inputArea.getText().equals("") && !selected.equals(""))
            {
                Message.Text mesg =
                        new Message.Text(inputArea.getText(), username, selected, Calendar.getInstance().getTime());
                client.send(mesg);
                inputArea.setText("");
                outputArea.append(mesg+"\n");
            }
        });

        Font font;
        Font boldFont;

        JLabel friendsLabel = new JLabel("People online");
        friendsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        font = friendsLabel.getFont();
        boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        friendsLabel.setFont(boldFont);

        JLabel titleLabel = new JLabel("BELCOT 1.0");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        font = titleLabel.getFont();
        boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        titleLabel.setFont(boldFont);

        tick = new JCheckBox("use enter as text");
        JScrollPane scrollList = new JScrollPane(displayList);
        scrollList.setPreferredSize(new Dimension(200, 350));
        displayList.setPreferredSize(new Dimension(190, 330));
        button.setPreferredSize(new Dimension(50, 100));
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(friendsLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        jPanel.add(titleLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 3;
        jPanel.add(scrollList, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        jPanel.add(scrollOutput, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 2;
        jPanel.add(scrollInput, c);
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        jPanel.add(button, c);
        c.gridx = 2;
        c.gridy = 3;
        jPanel.add(tick, c);
        displayList.addListSelectionListener(e ->
        {
            selected = displayList.getSelectedValue();
            outputArea.setText("");
            for(ClientThread.UserConv u : client.conversations)
            {
                if(selected.equals(u.username))
                {
                    for(Message.Text mesg : u.conv)
                    {
                        outputArea.append(mesg+"\n");
                    }
                    break;
                }
            }
        });
        this.getRootPane().setDefaultButton(button);

        setContentPane(jPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}