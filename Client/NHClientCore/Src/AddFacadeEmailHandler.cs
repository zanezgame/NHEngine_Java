using System;
using System.Collections.Generic;

using System.Text;
using KODGames.Common;
using com.kodgames.corgi.protocol;

namespace KODGames.ClientHelper
{
    class AddFacadeEmailHandler
    {
        private IClientHelper clientHelper;
        private Transmitter transmitter;
        public AddFacadeEmailHandler(IClientHelper clientHelper, Transmitter transmitter)
        {
            this.clientHelper = clientHelper;
            this.transmitter = transmitter;
        }

        public bool AddFacadeEmail(String emailTitle, String emailBody, String sender, int receiverType, int receiverID,long sendTime,
    int facadeTypeBoyID,int facadeTypeGirlID,bool isAssociateToLevel,long timeDuration, int callback)
        {
            AddFacadeEmailReq request = new AddFacadeEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.receiverID = receiverID;
            request.sendTime = sendTime;
            request.facadeTypeBoyID = facadeTypeBoyID;
            request.facadeTypeGirlID = facadeTypeGirlID;
            request.isAssociateToLevel = isAssociateToLevel;
            request.timeDuration = timeDuration;
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_FACADE_EMAIL_REQ);
        }
        public bool AddFacadeEmail(String emailTitle, String emailBody, String sender, int receiverType,long sendTime,
  int facadeTypeBoyID, int facadeTypeGirlID, bool isAssociateToLevel, long timeDuration, int callback)
        {
            AddFacadeEmailReq request = new AddFacadeEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.sendTime = sendTime;
            request.facadeTypeBoyID = facadeTypeBoyID;
            request.facadeTypeGirlID = facadeTypeGirlID;
            request.isAssociateToLevel = isAssociateToLevel;
            request.timeDuration = timeDuration;
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_FACADE_EMAIL_REQ);
        }

        public HandlerAction OnAddFacadeEmail(Message message)
        {
            AddFacadeEmailRes response = (AddFacadeEmailRes)message.Protocol;
            Guid guid = new Guid();
            if (response.guid == null || response.guid.Length == 0)
                guid = new Guid(BigEndianTransfer.GUIDToBytes(response.guid));
            clientHelper.OnAddFacadeEmail(response.result, guid, response.callback);

            return HandlerAction.TERMINAL;
        }
    }
}

