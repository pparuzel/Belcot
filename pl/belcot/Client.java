package pl.belcot;

import java.io.IOException;
import java.net.Socket;

public class Client
{
    public Client(String ip, int port, String username)
    {
        try
        {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection successful, "+username+"!");
            new ClientThread(socket, username).start();
        } catch (IOException e)
        {
            System.out.println("Unable to connect.");
            System.exit(0);
        }
    }
}
