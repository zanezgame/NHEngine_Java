using System;
using System.Collections.Generic;
using nicehu.pb;
using nicehu.common;
using System.Text;


namespace nicehu.clientcore
{
    public class AuthTokenHandler
    {
        private ClientCore clientCore;

        public AuthTokenHandler(ClientCore ClientCore)
        {
            this.clientCore = ClientCore;
        }

        public bool AuthTokenReq(int playerId, string token)
        {
            AuthTokenReq request = new AuthTokenReq();
            request.token = token;

            bool result =  clientCore.Transmitter.SendToGame(request, (int)EGMI.EGMI_AUTH_TOKEN_REQ);
            return result;
        }

        public void OnAuthTokeRes(Message message)
        {
            AuthTokenRes response = (AuthTokenRes)message.GetPb(typeof(AuthTokenRes));

            if (clientCore != null)
            {
                clientCore.onAuthToken(
                  response.result
               );
            }
        }
    }
}

