import http from "../Server";
import Swal from "sweetalert2";

class DataDosenService {
    getAll() {
        return http.get("/api/v1/dosen").catch(e =>
            Swal.fire(
            'Ops..',
            e.response.data.data,
            'error'
        ));
    }
    get(id) {
        return http.get(`/api/v1/dosen/${id}`);
    }
    create(data) {
        return http.post("/api/v1/dosen", data);
    }
    update(id, data) {
        return http.post(`/api/v1/dosen/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/dosen/${id}`);
    }

    createAkun(id) {
        return http.post(`/api/v1/dosen/${id}/generateUser`)
    }
}
export default new DataDosenService();
