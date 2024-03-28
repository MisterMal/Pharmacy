import { get, del, put, postWithEtag, post } from "../api";

export async function getSelfOrders() {
  return await get("order/self");
}

export async function getWaitingOrders() {
  return await get("order/waiting");
}

export async function getOrdersToApprove() {
  return await get("order/to-approve");
}

export async function approveOrder(id) {
  return await put(`order/${id}/approve`);
}

export async function createOrder(to_send) {
  return await post("order/submit", to_send);
}

export async function deleteWaitingOrdersById(id) {
  return await del(`order/${id}/waiting`);
}

export async function approveOrderById(id) {
  return await put(`order/${id}/patient-approve`);
}

export async function updateQueue() {
  return await put("order/update-queue");
}

export async function withdrawOrderById(id) {
  return await put(`order/${id}/withdraw`);
}

export async function cancelOrder(id) {
  return await put(`order/${id}/cancel`);
}

// export async function submitOrder(orderData) {
//   return await postWithEtag("order/update-queue", orderData);
// }