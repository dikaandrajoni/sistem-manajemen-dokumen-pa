import http from "../Server";

class DataJurusanService {
    getAll() {
        return http.get("/api/v1/jurusan");
    }
    get(id) {
        return http.get(`/api/v1/jurusan/${id}`);
    }
    create(data) {
        return http.post("/api/v1/jurusan", data);
    }
    update(id, data) {
        return http.post(`/api/v1/jurusan/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/jurusan/${id}`);
    }
}
export default new DataJurusanService();
