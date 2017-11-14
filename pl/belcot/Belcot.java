package pl.belcot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Belcot implements ActionListener
{
    public LoginWindow loginWindow;
    public Client client;
    public String[] IPAdr;

    public Belcot()
    {
        this.IPAdr = null;
        client = null;
        loginWindow = new LoginWindow();
        loginWindow.button.addActionListener(this);
    }

    public Belcot(String[] IPAdr)
    {
        this.IPAdr = IPAdr;
        client = null;
        loginWindow = new LoginWindow();
        loginWindow.button.addActionListener(this);
        loginWindow.ipField.setEditable(false);
        loginWindow.ipField.setText(IPAdr[0]+":"+IPAdr[1]);
        loginWindow.ipField.setEnabled(false);
    }

    public Belcot(String[] IPAdr, String username)
    {
        this.IPAdr = IPAdr;
        loginWindow = null;
        client = new Client(IPAdr[0], Integer.parseInt(IPAdr[1]), username);
    }

    public static void main(String[] args)
    {
        String[] ipAddress;
        if(args.length == 2)
        {
            ipAddress = args[0].split(":", 2);
            new Belcot(ipAddress, args[1]);
        }
        else if(args.length == 1)
        {
            ipAddress = args[0].split(":", 2);
            new Belcot(ipAddress);
        }
        else
        {
            new Belcot();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(loginWindow.button) && !loginWindow.loginField.getText().equals("")
                && !loginWindow.ipField.getText().equals(""))
        {
            loginWindow.dispose();
            String username = loginWindow.loginField.getText();
            String ipaddr = loginWindow.ipField.getText();
            IPAdr = ipaddr.split(":", 2);
            client = new Client(IPAdr[0], Integer.parseInt(IPAdr[1]), username);
        }
    }
}
