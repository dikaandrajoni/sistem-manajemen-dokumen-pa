import http from "../Server";

class MahasiswaService {
    getAll() {
        return http.get("/api/v1/mahasiswa");
    }
    get(id) {
        return http.get(`/api/v1/mahasiswa/${id}`);
    }
    create(data) {
        return http.post("/api/v1/mahasiswa", data);
    }
    update(id, data) {
        return http.post(`/api/v1/mahasiswa/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/mahasiswa/${id}`);
    }
    deleteAll() {
        return http.delete(`/api/v1/mahasiswaList`);
    }

    createAkunMahasiswa(id) {
        return http.post(`/api/v1/mahasiswa/${id}/createAkun`);
    }
}
export default new MahasiswaService();
