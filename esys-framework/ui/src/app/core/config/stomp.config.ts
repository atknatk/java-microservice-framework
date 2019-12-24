export const stompConfig: any = {


    brokerURL: 'ws://localhost:8080/ws/630/025ccuza/websocket', // WebSocketConfig.uri,
    connectHeaders: {
        access_token : "sdfs",
        Authorization: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDQ3MjcxMDgsInVzZXJfbmFtZSI6InRlc3RAdGVzdC5jb20iLCJhdXRob3JpdGllcyI6WyJhdXRob3JpdHkucm9sZS5uZXciLCJhdXRob3JpdHkubG9naW4udXNlciIsImF1dGhvcml0eS5kYXNoYm9hcmQudmlldyIsImF1dGhvcml0eS5yb2xlLmRlbGV0ZSIsImF1dGhvcml0eS5yb2xlLmVkaXQiLCJhdXRob3JpdHkudXNlci5lZGl0Il0sImp0aSI6Ijc3MDlmNTU0LWY3YjEtNGMxOC1hODQ3LTJmYWMyYzU1ZGJmYSIsImNsaWVudF9pZCI6ImNsaWVudGFwcCIsInNjb3BlIjpbIm1vYmlsZWNsaWVudCJdfQ.oWmBhcyHzjS-7t8M1uGQKpwvZ68ho_Gy5Elmzr4kNcw'
    },
//
    //
    // heartbeat_in: 0,
    //
    //
    // heartbeat_out: 20000,
    //
    //
    //reconnect_delay: 30000,

    reconnectDelay: 5000,
    beforeConnect : () => {

    },

    debug: (msg: string): void => {
        console.log(new Date(), msg);
    }

};
