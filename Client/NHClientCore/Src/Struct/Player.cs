using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace nicehu.clientcore
{
    public class Player
    {
        private int playerId;
        public int PlayerId
        {
            get { return playerId; }
            set { playerId = value; }
        }

        private string name;
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        private LevelAttrib levelAttrib = new LevelAttrib();
        public LevelAttrib LevelAttrib
        {
            get { return levelAttrib; }
            set { levelAttrib = value; }
        }



        private long money;
        public long Money
        {
            get { return money; }
            set { money = value; }
        }

        private long diamond;
        public long Diamond
        {
            get { return diamond; }
            set { diamond = value; }
        }

        private int packageSize;
        public int PackageSize
        {
            get { return packageSize; }
            set { packageSize = value; }
        }

        private List<Item> items;
        public List<Item> Items
        {
            get { return items; }
            set { items = value; }
        }
        private long loginTime;
        public long LoginTime
        {
            get { return loginTime; }
            set { loginTime = value; }
        }

        private int timeZone;
        public int TimeZone
        {
            get { return timeZone; }
            set { timeZone = value; }
        }

        public Player FromProto(nicehu.pb.Player player)
        {
            this.levelAttrib = new LevelAttrib();

            this.Items = new List<Item>();

            this.playerId = player.playerId;
            this.Name = player.name;
            this.Money = player.money;
            this.Diamond = player.diamond;



            foreach (nicehu.pb.Item item in player.items)
            {
                nicehu.clientcore.Item _item = new nicehu.clientcore.Item();
                _item.FromProto(item);
                this.items.Add(_item);
            }

            return this;
        }



    }
}