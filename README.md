# NiceHuEngine
##### Copyright © http://engine.nicehu.com

#####交流QQ群：528746333

## What is it?

魅狐引擎是用java编写的大型游戏服务器引擎.

## 魅狐引擎使用的技术
  Java 8 语言
  Netty 4.0网络框架
  Memcached缓存,Mysql数据库
  google protobuf网络通信


## 引擎简介
  魅狐引擎共有 五个Server:ManageServer,WorldServer,GameServer,ProxyServer,AuthServer
  其中支持无限数量的GameServer集群来拓展服务器性能.
  
  ManageServer:管理服务器,主要负责管理其它Server注册和状态.
  WorldServer:世界服务器,主要负责无法再单个GameServer上处理的逻辑,比如帮会,跨GameServer聊天.
  GameServer:逻辑服务器,一个区可以配置多个GameServer
  ProxyServer:代理服务器,客户端连接代理服务器,代理服务器负责效验和转发已经连接的状态管理
  AuthServer:验证服务器,负责登录验证

## 通信协议
  底层通信结构: 2byte 消息类型 + 4byte消息长度 + nbyte protobuf字节流.
  所以通信协议,外部再包裹一层BaseMsg,子协议转化成字节流或存储于baseMsg的msgData字段中,这样Proxy统一转发baseMsg即可,无需关心具体子协议.