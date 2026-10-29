import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8181",
    headers: {
        'Accept': 'application/json, text/plain, */*',
        "Content-type": "application/json",
        "Access-Control-Allow-Origin": "*",
    }
});
