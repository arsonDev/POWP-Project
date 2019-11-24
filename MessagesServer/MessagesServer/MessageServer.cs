using MessagesServer.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;

namespace MessagesServer
{
    class MessageServer
    {
        private TcpListener _tcpListener;
        private Thread _serverThread;
        private Dictionary<User, TcpClient> _clientList;
        private User _user;

        public MessageServer()
        {
            Init();
            _serverThread = new Thread(new ThreadStart(ClientConnection));
            _serverThread.Start();
            _serverThread.Join();
        }

        private void Init()
        {
            _clientList = new Dictionary<User, TcpClient>();
            _tcpListener = new TcpListener(IPAddress.Any, 5555);
        }

        private void ClientConnection()
        {
            try
            {
                _tcpListener.Start();
                Console.WriteLine($"Nasłuchiwanie rozpoczęte.");
            }
            catch (Exception e)
            {
                Console.WriteLine("Server nie wystartował");
            }

            while (true)
            {
                try
                {
                    TcpClient client = _tcpListener.AcceptTcpClient();
                    if (client != null)
                        ThreadPool.QueueUserWorkItem(ThreadProc, client);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Wyjebało server");
                }
            }
        }

        private void ThreadProc(object obj)
        {
            User user = null;
            var client = (TcpClient)obj;
            byte[] messageBytes = new byte[1024];
            try
            {
                user = CreateNewUser(client);
                _clientList.Add(user, client);
                string listOfActiveUser = GetActiveUser();
                messageBytes = Encoding.UTF8.GetBytes($"Zalogowano jako: {user.UserName}\n{listOfActiveUser}");
            }
            catch (Exception e)
            {
                messageBytes = Encoding.UTF8.GetBytes(e.Message);
            }

            client.ReceiveTimeout = 10;
            client.GetStream().Write(messageBytes, 0, messageBytes.Length);
            while (true)
            {
                try
                {
                    if (!client.Connected)
                    {
                        break;
                    }

                    byte[] buffor = new byte[client.Available];
                    int result = client.GetStream().Read(buffor, 0, buffor.Length);
                    string receivedMessage = Encoding.UTF8.GetString(buffor);
                    
                    if (receivedMessage.Equals("/ACTIVE"))
                    {
                        string listOfActiveUser = GetActiveUser();
                        messageBytes = Encoding.UTF8.GetBytes($"Lista użytkowników: \n{listOfActiveUser}");
                        client.ReceiveTimeout = 10;
                        client.GetStream().Write(messageBytes, 0, messageBytes.Length);
                    }

                    User searchedUser = _clientList.Keys.AsEnumerable().Where(x => x.UserName.Equals(Regex.Match(receivedMessage, @"(?<=^)(.*)(?=:)").Value)).Select(x => x).FirstOrDefault();
                    TcpClient tcpClient = _clientList[searchedUser];

                    var messageTosend = Encoding.UTF8.GetBytes($"{user?.UserName ?? "BRAK NAZWY"}:{Regex.Match(receivedMessage, @"(?<=:)(.*)(?=$)").Value}");
                    tcpClient.GetStream().Write(messageTosend, 0, messageTosend.Length);

                }
                catch (Exception e)
                {

                }
            }
        }

        private User CreateNewUser(TcpClient client)
        {
            try
            {
                byte[] buffer = new byte[1024];
                NetworkStream stream = client.GetStream();
                int result = stream.Read(buffer, 0, buffer.Length);
                string userName = Encoding.UTF8.GetString(buffer, 0, result);
                if (_clientList.ContainsKey(new User(userName)))
                    throw new Exception($"Użytkownik {userName} już istnieje lub nie może zostać utworzony");
                return new User(userName);
            }
            catch (Exception e)
            {
                return CreateNewUser(client);
            }
        }

        private string GetActiveUser()
        {
            string result = "Aktywni użytkownicy:\n";
            foreach (User user in _clientList.Keys)
            {
                result += user.ToString() + "\n";
            }
            return result;
        }

    }
}
