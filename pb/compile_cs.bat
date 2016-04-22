start ../Tools/protobuf/protogen.exe  -i:proto/NHDefine.proto    -o:../Client/PB/Src/NHDefine.cs -p:lightFramework=true
start ../Tools/protobuf/protogen.exe  -i:proto/NHMsgBase.proto  -o:../Client/PB/Src/NHMsgBase.cs -p:lightFramework=true
start ../Tools/protobuf/protogen.exe  -i:proto/NHMsgAuth.proto    -o:../Client/PB/Src/NHMsgAuth.cs -p:lightFramework=true
start ../Tools/protobuf/protogen.exe  -i:proto/NHMsgGame.proto    -o:../Client/PB/Src/NHMsgGame.cs -p:lightFramework=true

pause