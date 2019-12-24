export interface IWebsocketService{

    connect();
    disconnect(cb :() =>void) ;
    isConnected() :boolean;
}