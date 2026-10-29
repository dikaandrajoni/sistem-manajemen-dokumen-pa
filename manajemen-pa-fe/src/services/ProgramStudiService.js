import http from "../Server";

class DataJurusanService {
    getAll() {
        return http.get("/api/v1/program-studi");
    }
    get(id) {
        return http.get(`/api/v1/program-studi/${id}`);
    }
    create(data) {
        return http.post("/api/v1/program-studi", data);
    }
    setKaprodi(id, data) {
        return http.post(`/api/v1/program-studi/${id}`, data);
    }
    update(id, data) {
        return http.post(`/api/v1/program-studi/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/program-studi/${id}`);
    }
}
export default new DataJurusanService();
