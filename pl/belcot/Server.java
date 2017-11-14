package pl.belcot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Server
{
    ArrayList<ServerThread> users;

    public Server(ServerSocket serverSocket)
    {
        users = new ArrayList<>();
        boolean done = false;
        while(!done)
        {
            try
            {
                System.out.println("Waiting for a user...");
                Socket socket = serverSocket.accept();
                System.out.println("A user has connected! " + socket.getRemoteSocketAddress());
                ServerThread newUser = new ServerThread(this, socket);
                newUser.start();
            } catch (IOException e)
            {
                System.err.println("Server excep.");
                done = true;
            }
        }
    }

    public static void main(String[] args)
    {
        if(args.length != 1)
            return;
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]), 50);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a\tdd MMM yyyy");
            System.out.println(c.getTimeZone().getID()+"\t"+dateFormat.format(c.getTime()));
            System.out.println( "IP: "+InetAddress.getLocalHost().getCanonicalHostName()+":"+args[0] );
            new Server(serverSocket);
        } catch (IOException e)
        {
            e.printStackTrace();
            if (serverSocket != null)
            {
                try
                {
                    serverSocket.close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void add(ServerThread serverThread, String name)
    {
        users.add(serverThread);
        for(ServerThread user : users)
        {
            user.userConn(name);
        }
    }

    public void remove(ServerThread serverThread, String name)
    {
        users.remove(serverThread);
        for(ServerThread user : users)
        {
            user.userDisconn(name);
        }
    }

    public void findAndSend(Message.Text msg)
    {
        for(ServerThread user : users)
        {
            if( msg.recipient.equals(user.getUsername()) )
            {
                user.receive(msg);
            }
        }
    }
}
