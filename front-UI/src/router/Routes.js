import { Pathnames } from "./Pathnames";
import Home from "../pages/Home";
import Login from "../pages/Login";
import SignUp from "../pages/SignUp";
import ResetPassword from "../pages/ResetPassword";
import SetResetPassword from "../pages/SetResetPassword";
import SingleAccount from "../pages/SingleAccount";
import Landing from "../pages/Landing";
import EditSingleAccount from "../pages/EditSingleAccount";
import EditChemist from "../pages/EditChemist";
import AllAccounts from "../pages/AllAccounts";
import AccountDetails from "../pages/AccountDetails";
import ConfirmAccount from "../pages/ConfirmAccount";
import RoleSelectionForm from "../pages/RoleSelection";
import AddPatient from "../pages/AddPatient";
import AddChemist from "../pages/AddChemist";
import AddAdministrator from "../pages/AddAdministrator";
import ShowSelfOrders from "../pages/ShowSelfOrders";
import ShowWaitingOrders from "../pages/ShowWaitingOrders";
import Shipment from "../pages/Shipment";
import OrdersToApprove from "../pages/OrdersToApprove";
import AllMedications from "../pages/AllMedications";
import ShowBucket from "../pages/ShowBucket";
import MedicationDetails from "../pages/MedicationDetails";
import AllCategories from "../pages/AllCategories";
import EditCategory from "../pages/EditCategory";
import EditSelfAccountDetails from "../pages/EditSelfAccountDetails";

export const publicRoutes = [
  {
    path: Pathnames.public.home,
    Component: Home,
  },
  {
    path: Pathnames.public.login,
    Component: Login,
  },
  {
    path: Pathnames.public.signup,
    Component: SignUp,
  },
  {
    path: Pathnames.public.resetPassword,
    Component: ResetPassword,
  },
  {
    path: Pathnames.public.setResetPassword,
    Component: SetResetPassword,
  },
  {
    path: Pathnames.public.confirmAccount,
    Component: ConfirmAccount,
  },
];

export const AuthRoutes = [
  {
    path: Pathnames.auth.self,
    Component: AccountDetails,
  },
  {
    path: Pathnames.auth.landing,
    Component: Landing,
  },
  {
    path: Pathnames.auth.editSelf,
    Component: EditSelfAccountDetails,
  },
];

export const PatientRoutes = [
  {
    path: Pathnames.patient.selfOrders,
    Component: ShowSelfOrders,
  },
  {
    path: Pathnames.patient.showBucket,
    Component: ShowBucket,
  },
];

export const AdminRoutes = [
  {
    path: Pathnames.admin.account,
    Component: SingleAccount,
  },
  {
    path: Pathnames.admin.editSingleAccount,
    Component: EditSingleAccount,
  },
  {
    path: Pathnames.admin.editChemist,
    Component: EditChemist,
  },
  {
    path: Pathnames.admin.details,
    Component: SingleAccount,
  },
  {
    path: Pathnames.admin.accounts,
    Component: AllAccounts,
  },
  {
    path: Pathnames.admin.createAccount,
    Component: RoleSelectionForm,
  },
  {
    path: Pathnames.admin.addPatient,
    Component: AddPatient,
  },
  {
    path: Pathnames.admin.addChemist,
    Component: AddChemist,
  },
  {
    path: Pathnames.admin.addAdministrator,
    Component: AddAdministrator,
  },
];

export const ChemistRoutes = [
  {
    path: Pathnames.chemist.shipment,
    Component: Shipment,
  },
  {
    path: Pathnames.chemist.waitingOrders,
    Component: ShowWaitingOrders,
  },
  {
    path: Pathnames.chemist.ordersToApprove,
    Component: OrdersToApprove,
  },
  {
    path: Pathnames.chemist.categories,
    Component: AllCategories,
  },
  {
    path: Pathnames.chemist.editCategory,
    Component: EditCategory,
  },
];

export const PatientChemistRoutes = [
  {
    path: Pathnames.patientChemist.medications,
    Component: AllMedications,
  },
  {
    path: Pathnames.patientChemist.medicationDetails,
    Component: MedicationDetails,
  },
];
