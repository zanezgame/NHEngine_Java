using System;
using System.Collections.Generic;
using System.Text;

namespace nicehu.clientcore
{
	public class LevelAttrib
	{
		private int level;
		public int Level
		{
			get { return level; }
			set { level = value; }
		}

        private long exp;
        public long Exp
		{
			get { return exp; }
			set { exp = value; }
		}

		public void Copy(LevelAttrib levelAttrib)
		{
			Level = levelAttrib.Level;
            Exp = levelAttrib.Exp;
		}

		public LevelAttrib FromProto(nicehu.pb.LevelAttrib pb)
		{
			this.level = pb.level;
			this.exp = pb.exp;
			return this;
		}

		public nicehu.pb.LevelAttrib ToProto()
		{
			var levelAttrPb = new nicehu.pb.LevelAttrib();
			levelAttrPb.level = this.level;
			levelAttrPb.exp = this.exp;
			return levelAttrPb;
		}
	}
}