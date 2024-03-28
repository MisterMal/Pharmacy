import "./App.css";
import Login from "./pages/Login";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import PublicNavbar from "./components/PublicNavbar";
import SingUp from "./pages/SignUp";
import AllAccounts from "./pages/AllAccounts";
import SingleAccount from "./pages/SingleAccount";
import EditSingleAccount from "./pages/EditSingleAccount";
import React, { Suspense } from "react";
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import HttpApi from "i18next-http-backend";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.js";
import LinearProgress from "@mui/material/LinearProgress";
import EditChemist from "./pages/EditChemist";
import EditAdmin from "./pages/EditAdmin";
import EditPatient from "./pages/EditPatient";
import ConfirmAccount from "./pages/ConfirmAccount";
import { RoutesComponent } from "./router/RoutesComponent";
import store from "./redux/Store";
import { Provider } from "react-redux";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .use(LanguageDetector)
  .use(HttpApi)
  .init({
    supportedLngs: ["en", "pl", "cs"],
    backend: {
      loadPath: "/assets/locales/{{lng}}/translation.json",
    },
    fallbackLng: "en",
    detection: {
      order: [
        "cookie",
        "htmlTag",
        "localStorage",
        "sessionStorage",
        "navigator",
        "path",
        "subdomain",
      ],
      caches: ["cookie"],
    },
    interpolation: {
      escapeValue: false, // react already safes from xss => https://www.i18next.com/translation-function/interpolation#unescape
    },
  });

const loading = (
  <div>
    <LinearProgress />
  </div>
);

function App() {
  return (
    <Suspense fallback={loading}>
      <Provider store={store}>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <Router>
            <RoutesComponent />
          </Router>
        </LocalizationProvider>
      </Provider>
    </Suspense>
  );
}

export default App;
