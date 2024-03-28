import { get, post, putWithEtag } from "../api";

export async function getAllCategories() {
  return await get("category");
}

export async function getCategory(id) {
  return await get(`category/${id}`);
}

export async function createCategory(name, isOnPrescription) {
  const body = {
    name: name,
    isOnPrescription: isOnPrescription,
  };
  return await post("category/add-category", body);
}

export async function editCategory(id, body, etag) {
  return await putWithEtag(`category/${id}/edit-category`, body, etag);
}
