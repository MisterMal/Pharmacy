import { get, post } from "../api";

export async function getAllMedications() {
  return await get("medication");
}

export async function getMedicationById(id) {
  return await get(`medication/${id}`);
}

export async function getMedication(id) {
  return await get(`medication/${id}`);
}

export async function addMedication(name, stock, price, categoryName) {
  const body = {
    name: name,
    stock: stock,
    price: price,
    categoryName: categoryName,
  };
  return await post("medication/add-medication", body);
}
