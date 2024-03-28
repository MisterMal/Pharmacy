import { get, post, put, putWithEtag, postWithEtag } from "../api";

export async function getAccounts() {
  return await get("account");
}

export async function getAccount(id) {
  return await get(`account/${id}`);
}

export async function confirmAccount(token) {
  return await post(`account/confirm/${token}`);
}

export async function getSelfAccountDetails() {
  return await get("account/details");
}

export async function getAccountDetails(id) {
  return await get(`account/${id}/details`);
}

export async function signUpAccount(
  name,
  lastName,
  login,
  email,
  password,
  phoneNumber,
  pesel,
  nip
) {
  const body = {
    name: name,
    lastName: lastName,
    login: login,
    email: email,
    password: password,
    phoneNumber: phoneNumber,
    pesel: pesel,
    nip: nip,
  };

  return await post("account/register", body);
}

export async function blockAccount(id) {
  return await put(`account/${id}/block`);
}

export async function unblockAccount(id) {
  return await put(`account/${id}/unblock`);
}

export async function activateAccount(id) {
  return await put(`account/${id}/activate`);
}

export async function changeAccountPassword(id, body, etag) {
  return await putWithEtag(`account/${id}/change-user-password`, body, etag);
}

export async function changeSelfAccountPassword(body, etag) {
  return await putWithEtag("account/change-password", body, etag);
}

export async function changeSelfAccountEmail(body, etag) {
  return await putWithEtag("account", body, etag);
}

export async function changeOtherAccountEmail(id, body, etag) {
  return await putWithEtag(`account/${id}`, body, etag);
}

///implementacja change email

export async function resetPassword(email) {
  const body = {
    email: email,
  };

  return await put("account/reset-password", body);
}

export async function setResetPassword(token, newPassword) {
  const body = {
    token: token,
    newPassword: newPassword,
  };
  return await post("account/new-password", body);
}

export async function setNewPassword(token, password) {
  const body = {
    token: token,
    password: password,
  };

  return await post("account/set-new-password", body);
}

//dodanie pacjenta, aptekarza, admina

export async function addPatient(body) {
  return await post("account/add-patient", body);
}

export async function addChemist(body) {
  return await post("account/add-chemist", body);
}

export async function addAdmin(body) {
  return await post("account/add-admin", body);
}

//nadanie roli pacjenta, aptekarza, admina

export async function grantPatientRole(id, body, etag) {
  return await postWithEtag(`account/${id}/patient`, body, etag);
}

export async function grantChemistRole(id, body, etag) {
  return await postWithEtag(`account/${id}/chemist`, body, etag);
}

export async function grantAdminRole(id, body, etag) {
  return await postWithEtag(`account/${id}/admin`, body, etag);
}

//pobranie danych pacjenta, aptekarza, admina

export async function getPatientData(id) {
  return await get(`account/${id}/patient`);
}

export async function getChemistData(id) {
  return await get(`account/${id}/chemist`);
}

export async function getAdminData(id) {
  return await get(`account/${id}/admin`);
}

//edycja danych pacjenta, aptekarza, admina

export async function editPatientData(id, body) {
  return await put(`account/${id}/patient`, body);
}

export async function editChemistData(id, body) {
  return await put(`account/${id}/chemist`, body);
}

export async function editAdminData(id, body) {
  return await put(`account/${id}/admin`, body);
}

//zarządzanie blokadą ról

export async function blockRoleAdmin(id) {
  return await put(`account/${id}/admin/block`);
}

export async function blockRoleChemist(id) {
  return await put(`account/${id}/chemist/block`);
}

export async function blockRolePatient(id) {
  return await put(`account/${id}/patient/block`);
}

export async function unblockRoleAdmin(id) {
  return await put(`account/${id}/admin/unblock`);
}

export async function unblockRoleChemist(id) {
  return await put(`account/${id}/chemist/unblock`);
}

export async function unblockRolePatient(id) {
  return await put(`account/${id}/patient/unblock`);
}

//login

export async function signInAccount(login, password) {
  const body = {
    login: login,
    password: password,
  };

  return await post("auth/login", body);
}

export async function notifyAccessLevelChange(role) {
  return await post("auth/notify-access-level-change/" + role);
}

//na razie inne register

export async function putPatient(
  id,
  name,
  lastName,
  login,
  email,
  phoneNumber,
  pesel,
  nip
) {
  const body = {
    id: id,
    firstName: name,
    lastName: lastName,
    login: login,
    email: email,
    phoneNumber: phoneNumber,
    pesel: pesel,
    nip: nip,
  };

  return await put("account/{id}/patient", body);
}

export async function changeLanguage(selectedLanguage) {
  return await put("account/change-language?language=" + selectedLanguage);
}

//tymczasowa edycja danych

export async function putChemist(id, licenseNumber) {
  const body = {
    id: id,
    licenseNumber: licenseNumber,
  };

  return await put("account/{id}/chemist", body);
}

export async function putAdmin(id, workPhoneNumber) {
  const body = {
    id: id,
    workPhoneNumber: workPhoneNumber,
  };

  return await put("account/{id}/admin", body);
}

export async function editOtherPatientData(id, body, etag) {
  return await putWithEtag(`account/${id}/patient`, body, etag);
}

export async function editOtherChemistData(id, body, etag) {
  return await putWithEtag(`account/${id}/chemist`, body, etag);
}

export async function editOtherAdminData(id, body, etag) {
  return await putWithEtag(`account/${id}/admin`, body, etag);
}

export async function editSelfPatientData(body, etag) {
  return await putWithEtag("account/patient", body, etag);
}

export async function editSelfChemistData(body, etag) {
  return await putWithEtag("account/chemist", body, etag);
}

export async function editSelfAdminData(body, etag) {
  return await putWithEtag("account/admin", body, etag);
}
