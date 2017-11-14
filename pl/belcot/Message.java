package pl.belcot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable
{
    Text msg;
    String disconnectedUser;
    String connectedUser;
    String[] list;

    public Message()
    {
        msg = null;
        disconnectedUser = null;
        connectedUser = null;
        list = null;
    }

    public static class TextMessage extends Message
    {
        public TextMessage(Text txt)
        {
            super();
            super.msg = txt;
        }
    }

    public static class List extends Message
    {
        public List(String[] list)
        {
            super();
            this.list = list;
        }
    }

    public static class Disconnected extends Message
    {
        public Disconnected(String username)
        {
            super();
            this.disconnectedUser = username;
        }
    }

    public static class Connected extends Message
    {
        public Connected(String user)
        {
            super();
            super.connectedUser = user;
        }
    }

    public static class Text implements Serializable
    {
        String txt;
        String sender;
        String recipient;
        Date time;

        Text(String txt, String from, String to, Date time)
        {
            this.txt = txt;
            this.sender = from;
            this.recipient = to;
            this.time = time;
        }

        @Override
        public String toString()
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            return dateFormat.format(Calendar.getInstance().getTime())+"\t"+sender+" writes:\n>"+txt;
        }
    }
}