using System;
using System.Collections.Generic;

using System.Text;
using KODGames.Common;
using com.kodgames.corgi.protocol;

namespace KODGames.ClientHelper
{
    class AddWeaponOrPetEmailHandler
    {
        private IClientHelper clientHelper;
        private Transmitter transmitter;
        public AddWeaponOrPetEmailHandler(IClientHelper clientHelper, Transmitter transmitter)
        {
            this.clientHelper = clientHelper;
            this.transmitter = transmitter;
        }

        public bool AddWeaponOrPetEmail(String emailTitle, String emailBody, String sender, int receiverType, long sendTime,
    int weaponPetID, int qualityType, int level, List<KODGames.ClientClass.Skill> skillList, int callback)
        {
            AddWeaponOrPetEmailReq request = new AddWeaponOrPetEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.sendTime = sendTime;
            request.weaponPetID = weaponPetID;
            request.qualityType = qualityType;
            request.level = level;
            foreach (KODGames.ClientClass.Skill skill in skillList)
                request.skill.Add(skill.toProtobufClass());
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_WEAPON_OR_PET_EMAIL_REQ);
        }
        public bool AddWeaponOrPetEmail(String emailTitle, String emailBody, String sender, int receiverType, int receiverID,long sendTime,
   int weaponPetID, int qualityType, int level, List<KODGames.ClientClass.Skill> skillList, int callback)
        {
            AddWeaponOrPetEmailReq request = new AddWeaponOrPetEmailReq();
            request.emailTitle = emailTitle;
            request.emailBody = emailBody;
            request.sender = sender;
            request.receiverType = receiverType;
            request.receiverID = receiverID;
            request.sendTime = sendTime;
            request.weaponPetID = weaponPetID;
            request.qualityType = qualityType;
            request.level = level;
            foreach (KODGames.ClientClass.Skill skill in skillList)
                request.skill.Add(skill.toProtobufClass());
            request.callback = callback;
            return transmitter.Send(request, Protocols.P_GAME_ADD_WEAPON_OR_PET_EMAIL_REQ);
        }

        public HandlerAction OnAddWeaponOrPetEmail(Message message)
        {
            AddWeaponOrPetEmailRes response = (AddWeaponOrPetEmailRes)message.Protocol;
            Guid guid = new Guid();
            if (response.guid == null || response.guid.Length == 0)
                guid = new Guid(BigEndianTransfer.GUIDToBytes(response.guid));
            clientHelper.OnAddWeaponOrPetEmail(response.result, guid, response.callback);

            return HandlerAction.TERMINAL;
        }
    }
}

