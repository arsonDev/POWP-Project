using System;
using System.Collections.Generic;
using System.Text;

namespace MessagesServer.Model
{
    public class User
    {
        public string UserName { get; set; }

        public User(string userName)
        {
            this.UserName = userName;
        }

        public int Id { get; set; }

        public override string ToString()
        {
            return UserName;
        }
    }
}
