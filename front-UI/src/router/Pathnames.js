export const Pathnames = {
  public: {
    login: "/login",
    signup: "/sign-up",
    home: "/",
    confirmAccount: "/confirm-account/:token",
    resetPassword: "/reset-password",
    setResetPassword: "/new-password",
    error: "/error",
  },
  auth: {
    landing: "/home",
    self: "/home/self",
    editSelf: "/home/self/edit",
  },
  admin: {
    accounts: "/accounts",
    editSingleAccount: "/accounts/:id/edit",
    editChemist: "/accounts/:id/edit/chemist",
    details: "/accounts/:id/details",
    account: "/accounts/:id",
    createAccount: "/accounts/create-account",
    addPatient: "/accounts/create-account/add-patient",
    addChemist: "/accounts/create-account/add-chemist",
    addAdministrator: "/accounts/create-account/add-administrator",
  },
  patient: {
    selfOrders: "/orders/self",
    showBucket: "/cart",
  },
  chemist: {
    shipment: "/shipment",
    waitingOrders: "/orders/waiting",
    ordersToApprove: "/orders-to-approve",
    categories: "/categories",
    editCategory: "/categories/:id/edit",
  },
  patientChemist: {
    medications: "/medications",
    medicationDetails: "/medications/:id",
  },
};
