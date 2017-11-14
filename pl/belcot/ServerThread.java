package pl.belcot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread
{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server server;
    public String username;

    public ServerThread(Server server, Socket socket)
    {
        this.server = server;
        this.socket = socket;
        this.username = "";
        try
        {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            ArrayList<String> list = new ArrayList<>();
            for(ServerThread user : server.users)
            {
                list.add(user.getUsername());
            }
            output.writeObject(new Message.List(list.toArray(new String[0])));
        } catch (IOException e)
        {
            // do nothing
        }
    }

    void userConn(String user)
    {
        try
        {
            output.writeObject(new Message.Connected(user));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void userDisconn(String user)
    {
        try
        {
            output.writeObject(new Message.Disconnected(user));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    String getUsername()
    {
        return username;
    }

    @Override
    public void run()
    {
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
                        server.findAndSend(m.msg);
                    }
                    if(m instanceof Message.Connected)
                    {
                        System.out.println("Username: "+m.connectedUser);
                        username = m.connectedUser;
                        server.add(this, m.connectedUser);
                    }
                    if(m instanceof Message.Disconnected)
                    {
                        System.out.println(m.disconnectedUser+" has disconnected.");
                        server.remove(this, m.disconnectedUser);
                        done = true;
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
        try
        {
            socket.close();
        } catch (IOException e)
        {
//            e.printStackTrace();
        }
    }

    public void receive(Message.Text msg)
    {
        try
        {
            output.writeObject(new Message.TextMessage(msg));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
