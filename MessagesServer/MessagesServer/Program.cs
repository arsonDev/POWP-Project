using System;
using System.Net.Sockets;
using System.Threading;

namespace MessagesServer
{
    class Program
    {
        private Thread thread;
        private TcpListener tcpListener;
 
        static void Main(string[] args)
        {
            new MessageServer();
        }
    }
}
