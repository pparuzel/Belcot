package pl.belcot;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread
{
    class UserConv
    {
        String username;
        ArrayList<Message.Text> conv;
        public UserConv(String username)
        {
            this.username = username;
            this.conv = new ArrayList<>();
        }
    }

    private final String user;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    DefaultListModel<String> model;
    ArrayList<UserConv> conversations;
    BelcotUI bui;

    public ClientThread(Socket socket, String username)
    {
        this.user = username;
        this.conversations = new ArrayList<>();
        model = new DefaultListModel<>();
        bui = new BelcotUI(this, username, model);
        try
        {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            output.writeObject(new Message.Connected(username));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    output.writeObject(new Message.Disconnected(user));
                } catch (IOException e)
                {
                    System.out.println("Could not connect to server.");
                }
            }
        });
        boolean done = false;
        while(!done)
        {
            try
            {
                Message m = (Message) input.readObject();
                if(m != null)
                {
                    if(m instanceof Message.TextMessage)
                    {
                        bui.outputArea.append(m.msg+"\n");
                        for(UserConv u : conversations)
                        {
                            if(u.username.equals(m.msg.sender))
                            {
                                u.conv.add(m.msg);
                                break;
                            }
                        }
                    }
                    if(m instanceof Message.Connected)
                    {
                        model.addElement(m.connectedUser);
                        conversations.add(new UserConv(m.connectedUser));
                    }
                    if(m instanceof Message.Disconnected)
                    {
                        System.out.println(m.disconnectedUser+" has disconnected.");
                        bui.displayList.setSelectedIndex(0);
                        model.removeElement(m.disconnectedUser);
                    }
                    if(m instanceof Message.List)
                    {
                        for(String username : m.list)
                        {
                            if(!model.contains(username))
                            {
                                model.addElement(username);
                                conversations.add(new UserConv(username));
                            }
                        }
                    }
                }
            } catch (IOException e)
            {
                done = true;
            } catch (ClassNotFoundException e)
            {
                System.err.println("WRONG INPUT");
            }
        }
    }

    public void send(Message.Text mesg)
    {
        try
        {
            for(ClientThread.UserConv u : conversations)
            {
                if(mesg.recipient.equals(u.username))
                {
                    u.conv.add(mesg);
                    break;
                }
            }
            output.writeObject(new Message.TextMessage(mesg));
        } catch (IOException e)
        {
            System.exit(0);
        }
    }
}
