using System;
using System.Collections.Generic;

using System.Text;
using KODGames.Common;
using com.kodgames.corgi.protocol;

namespace KODGames.ClientHelper
{
    class AddPlainEmailHandler
    {
        private IClientHelper clientHelper;
        private Transmitter transmitter;
        public AddPlainEmailHandler(IClientHelper clientHelper, Transmitter transmitter)
        {
            this.clientHelper = clientHelper;
            this.transmitter = transmitter;
        }

        public bool AddPlainEmail(String emailTitle,String emailBody,String sender,int receiverType,long sendTime,
	List<KODGames.ClientClass.EmailItem> items,int callback)
        {
            AddPlainEmailReq request = new AddPlainEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.sendTime = sendTime;
            foreach (KODGames.ClientClass.EmailItem item in items)
                request.items.Add(item.ToProtoBufClass());
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_PLAIN_EMAIL_REQ);
        }
        public bool AddPlainEmail(String emailTitle, String emailBody, String sender, int receiverType, int receiverID, long sendTime,
    List<KODGames.ClientClass.EmailItem> items, int callback)
        {
            AddPlainEmailReq request = new AddPlainEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.receiverID = receiverID;
            request.sendTime = sendTime;
            foreach (KODGames.ClientClass.EmailItem item in items)
                request.items.Add(item.ToProtoBufClass());
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_PLAIN_EMAIL_REQ);
        }

        public HandlerAction OnAddPlainEmail(Message message)
        {
            AddPlainEmailRes response = (AddPlainEmailRes)message.Protocol;
            Guid guid = new Guid();
            if(response.guid == null || response.guid.Length == 0)
            guid = new Guid(BigEndianTransfer.GUIDToBytes(response.guid));
            clientHelper.OnAddPlainEmail(response.result, guid, response.callback);

            return HandlerAction.TERMINAL;
        }
    }
}

