import http from "../Server";

class RoleService {
    getAll() {
        return http.get("/api/v1/role");
    }
    get(id) {
        return http.get(`/api/v1/role/${id}`);
    }
    create(data) {
        return http.post("/api/v1/role", data);
    }
    update(id, data) {
        return http.post(`/api/v1/role/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/role/${id}`);
    }
}
export default new RoleService();
