import http from "../Server";

class UserService {
    login(user) {
        return http.post("/auth/login", user);
    }
    getAll() {
        return http.get("/api/v1/user");
    }
    get(id) {
        return http.get(`/api/v1/user/${id}`);
    }
    create(data) {
        return http.post("/api/v1/user", data);
    }
    update(id, data) {
        return http.post(`/api/v1/user/${id}`, data);
    }
    delete(id) {
        return http.delete(`/api/v1/user/${id}`);
    }
    logOut(data) {
        window.localStorage.removeItem("token");
    }

    ubahPassword(data) {
        return http.post("/api/v1/user/ubah-password", data);
    }

    register(user) {
        return http.post("/auth/register", user);
    }

    getCurrentUser() {
        return JSON.parse(window.localStorage.getItem('token'));;
    }
    uploadSignature(id, file) {
        return http.post(`/api/v1/user/${id}/uploadSignature`, file, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }});
    }
}

export default new UserService();
