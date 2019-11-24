using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace PS_Client
{
    class Program
    {
        private static Socket _socket;

        public static void Main(string[] args)
        {
            while (!(ServerConnect()))
            {
                if (!_socket.Connected)
                {
                    Console.WriteLine("Serwer jest nieaktywny. Próbować połączyć ponownie ? Y/N");
                    char enteredKey = char.ToUpper(Console.ReadKey().KeyChar);
                    Console.WriteLine(string.Empty);
                    if (enteredKey == 'Y' || enteredKey == 'N')
                    {
                        if (enteredKey == 'Y')
                        {
                            ServerConnect();
                        }
                        else
                        {
                            break;
                        }
                    }
                    else
                    {
                        Console.WriteLine("Nieprawidłowa odpowiedź");
                    }
                }
            }

            CreateNewUser();

            Task.Run(() =>
            {
                while (true)
                {
                    try
                    {
                        byte[] buffer = new byte[_socket.Available];
                        int result = _socket.Receive(buffer);
                        string response = Encoding.UTF8.GetString(buffer, 0, result);
                        string fromuser = Regex.Match(response, @"(?<=^)(.*)(?=:)").Value;
                        string message = Regex.Match(response, @"(?<=:)(.*)(?=$)").Value;
                        if (!string.IsNullOrWhiteSpace(fromuser))
                            Console.WriteLine($"Odpowiedź od {fromuser.Trim()} : {message.Trim()}");
                    }
                    catch (Exception e)
                    {

                    }
                }
            });


            while (true)
            {
                try
                {
                    string senetece = Console.ReadLine();
                    _socket.Send(Encoding.UTF8.GetBytes(senetece));
                }
                catch (Exception e)
                {

                }
            }

        }

        private static bool ServerConnect()
        {
            try
            {
                _socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                _socket.Connect(new IPEndPoint(IPAddress.Parse("10.104.28.120"), 5555));
                _socket.ReceiveTimeout = 5;
                _socket.SendTimeout = 5;
                string state = _socket.Connected ? "Aktywne" : "Nieaktywne";
                Console.WriteLine($"Połączenie : {state}");
            }
            catch (Exception e)
            {

            }
            return _socket?.Connected ?? false;
        }

        private static void CreateNewUser()
        {
            try
            {
                if (_socket.Connected)
                {
                    Console.WriteLine("Podaj nazwę użytkownika i wciśniej ENTER");
                    string senetece = Console.ReadLine();

                    _socket.Send(Encoding.UTF8.GetBytes(senetece));

                    byte[] buffer = new byte[1024];

                    int result = _socket.Receive(buffer);
                    Console.WriteLine($"Odpowiedź od serwera:\n{Encoding.UTF8.GetString(buffer, 0, result)}");
                }
                else
                    Console.WriteLine("Brak połaczenia z serwerem. Koniec nasłuchiwania");
            }
            catch (Exception e)
            {
                Console.WriteLine("Założenie użytkownika nie powiodło się");
                CreateNewUser();
            }
        }
    }
}
