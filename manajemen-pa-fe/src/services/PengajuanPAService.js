import http from "../Server";

class DataDosenService {
    getAll(params) {
        return http.get("/bpmn/ManajemenDokumenProyekAkhir", {
            params: params
        });
    }
    getTodo() {
        return http.get("/bpmn/ManajemenDokumenProyekAkhir/todo");
    }
    getDashboard(param) {
        return http.get("/bpmn/ManajemenDokumenProyekAkhir/dashboard", {
            params: param
        });
    }
    getDashboardCount() {
        return http.get("/bpmn/ManajemenDokumenProyekAkhir/dashboard-count");
    }
    get(id) {
        return http.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}`);
    }
    create(data) {
        return http.post("/bpmn/ManajemenDokumenProyekAkhir?withVariable=true", data);
    }
    update(id, data) {
        return http.post(`/bpmn/ManajemenDokumenProyekAkhir/${id}`, data);
    }
    delete(id) {
        return http.delete(`/bpmn/ManajemenDokumenProyekAkhir/${id}`);
    }
    uploadProposal(id, file) {
        return http.post(`/bpmn/ManajemenDokumenProyekAkhir/${id}/uploadProposal`, file, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }});
    }

    downloadProposal(id) {
        return http.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}/downloadProposal`, {
            responseType: 'blob',
        });
    }

    uploadTemplate(){
        return http.post("/api/v1/filedocument/uploadTemplate", {
            headers: {
                'Content-Type': 'multipart/form-data'
            }});
    }

    downloadTemplate(){
        return http.get("/api/v1/filedocument/downloadTemplate", {
            responseType: 'blob',
        });
    }

    getFileUrl(id) {
        return http.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}/getLinkProposal`);
    }

    getLaporanUrl(id) {
        return http.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}/getLinkLaporan`);
    }

    uploadLaporan(id, file) {
        return http.post(`/bpmn/ManajemenDokumenProyekAkhir/${id}/uploadLaporan`, file, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }});
    }

    downloadLaporan(id) {
        return http.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}/downloadLaporan`, {
            responseType: 'blob',
        });
    }

    bpmn(){
        return http.get("/bpmn/ManajemenDokumenProyekAkhir/xml")
    }

    reviewKaprodi(){
        return http.get("/bpmn/ManajemenDokumenProyekAkhir/hasilReviewKaprodi");
    }

    bimbinganProposal(){
        return http.get("/bpmn/BimbinganProposalProyekAkhir/todo");
    }

    bimbinganLaporan(){
        return http.get("/bpmn/BimbinganProyekAkhir/todo");
    }

}
export default new DataDosenService();
