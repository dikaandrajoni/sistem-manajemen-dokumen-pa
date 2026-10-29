import API from "../Server";

// const token = window.sessionStorage.getItem('token');
// const API = axios.create({
//     baseURL: "http://localhost:8181",
//     headers: {
//         'Accept': 'application/json, text/plain, */*',
//         "Content-type": "application/json",
//         "Access-Control-Allow-Origin": "*",
//         "Authorization":"Bearer "+token
//     }});

// APi CRUD
export const List = (name) => API.get(`/api/v1/${name}`);
export const Detail = (id, name) => API.get(`/api/v1/${name}/${id}`);
export const Create = (name, payload) => API.post(`/api/v1/${name}`, payload);
export const Update = (id, name, payload) => API.post(`/api/v1/${name}/${id}`, payload);
export const Delete = (id, name) => API.delete(`/api/v1/${name}/${id}`);
export const Spec = (name) => API.get(`/api/v1/${name}/spec`);

// API BPMN
export const getBpmnXML = () => API.get(`/bpmn/ManajemenDokumenProyekAkhir/xml`);
export const startBpmn = (payload, params) => API.post(`/bpmn/ManajemenDokumenProyekAkhir?withVariable=${params}`, payload);
export const draftBpmn = (id, payload) => API.post(`/bpmn/ManajemenDokumenProyekAkhir/${id}`, payload);
export const getBpmnList = () => API.get(`/bpmn/ManajemenDokumenProyekAkhir`);
export const getBpmnDetail = (id) => API.get(`/bpmn/ManajemenDokumenProyekAkhir/${id}`);
export const getBpmnSpec = () => API.get(`/bpmn/ManajemenDokumenProyekAkhir/spec`);
export const getBpmnStatistic = () => API.get(`/bpmn/ManajemenDokumenProyekAkhir/statistic`);
export const updateBpmnTask = (id, nameTask, payload, decision) => API.post(`/bpmn/ManajemenDokumenProyekAkhir/${nameTask}/${id}/submit?decision=${decision}`, payload);
export const saveAsDraftBpmn = (id, nameTask, payload) => API.post(`/bpmn/ManajemenDokumenProyekAkhir/${nameTask}/${id}/draft`, payload);
export const updateSubProcess = (id, nameTask, payload, decision) => API.post(`/bpmn/BimbinganProposalProyekAkhir/${nameTask}/${id}/submit?decision=${decision}`, payload);
export const updateSubProcessSecondary = (id, nameTask, payload, decision) => API.post(`/bpmn/BimbinganProyekAkhir/${nameTask}/${id}/submit?decision=${decision}`, payload);
