import server from "../Server";

export const setAuthToken = token => {
    if (token) {
        server.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
    else
        delete server.defaults.headers.common["Authorization"];
}
