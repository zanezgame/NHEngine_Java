using System;
using System.Collections.Generic;
using System.Text;

namespace nicehu.clientcore
{
	public class Item
	{
        public Item()
        {
        }
        public Item(int id, int count)
        {
            this.Id = id;
            this.Count = count;
        }
		private int id;
		public int Id
		{
			get { return id; }
			set { id = value; }
		}

		private int count;
		public int Count
		{
            get { return count; }
            set { count = value; }
		}

		public Item FromProto(nicehu.pb.Item pb)
		{
			this.Id = pb.id;
            this.Count = pb.count;
			return this;
		}

        public nicehu.pb.Item ToProto()
		{
            var tiemPb = new nicehu.pb.Item();
			tiemPb.id = this.Id;
            tiemPb.count = this.Count;
			return tiemPb;
		}
	}
}