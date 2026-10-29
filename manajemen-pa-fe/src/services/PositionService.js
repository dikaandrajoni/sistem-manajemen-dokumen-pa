import http from "../Server";

class RoleService {
    getAll() {
        return http.get("/api/v1/position");
    }
    get(id) {
        return http.get(`/api/v1/position/${id}`);
    }
    create(data) {
        return http.post("/api/v1/position", data);
    }
    update(id, data) {
        return http.post(`/api/v1/position/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/position/${id}`);
    }

    getTimKbk(){
        return http.get("/api/v1/position/timkbk");
    }
}
export default new RoleService();
