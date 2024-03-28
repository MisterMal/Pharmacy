import axios from "axios";
import {ACCESS_LEVEL, JWT_TOKEN} from "../constants/Constants";

const BASE_URL = window.location.href.includes("lodz.pl")
    ? "https://team-1.proj-sum.it.p.lodz.pl/api/account"
    : "http://localhost:8080/api/account";

const defaultHeaders = {
    "Content-Type": "application/json",
    Accept: "application/json",
};


const apiWithConfig = axios.create({
    baseURL: BASE_URL,
    headers: defaultHeaders,
});

apiWithConfig.interceptors.request.use((config) => {
    const token = localStorage.getItem(JWT_TOKEN)
    if (token && config.headers) config.headers.Authorization = 'Bearer ' + token.toString().replaceAll('"', '')
    return config
})

apiWithConfig.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.code === "ERR_NETWORK") {
            localStorage.removeItem(JWT_TOKEN)
            localStorage.removeItem(ACCESS_LEVEL)
        }
        return Promise.reject(error)
    },
)

export async function getNoResponse(stringUrl, params) {
    const url = new URL(stringUrl, BASE_URL);
    if (params) {
        url.search = new URLSearchParams(params).toString();
    }
    return await apiWithConfig.get(url);
}

export async function get(stringUrl, params) {
    const url = new URL(stringUrl, BASE_URL);
    if (params) {
        url.search = new URLSearchParams(params).toString();
    }
    return await apiWithConfig.get(url);
}

export async function post(stringUrl, body) {
    const url = new URL(stringUrl, BASE_URL);

    return await apiWithConfig.post(url, body);
}

export async function put(stringUrl, body) {
    const url = new URL(stringUrl, BASE_URL);

    return await apiWithConfig.put(url, body);
}

export async function putWithEtag(stringUrl, body, etag) {
    const url = new URL(stringUrl, BASE_URL);

    return await apiWithConfig.put(url, body, {
        headers: {
            "If-Match": etag,
        },
    });
}

export async function postWithEtag(stringUrl, body, etag) {
    const url = new URL(stringUrl, BASE_URL);

    return await apiWithConfig.post(url, body, {
        headers: {
            "If-Match": etag,
        },
    });
}

export async function del(stringUrl) {
    const url = new URL(stringUrl, BASE_URL);

    return await apiWithConfig.delete(url);
}
