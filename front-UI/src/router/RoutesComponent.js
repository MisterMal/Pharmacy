import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import {
  AdminRoutes,
  AuthRoutes,
  ChemistRoutes,
  PatientRoutes,
  publicRoutes,
  PatientChemistRoutes,
} from "./Routes";
import jwtDecode from "jwt-decode";
import { login as loginDispatch, logout } from "../redux/UserSlice";
import Error from "../pages/Error";
import { useDispatch, useSelector } from "react-redux";
import { JWT_TOKEN, ROLES } from "../constants/Constants";
import AuthLayout from "../layout/AuthLayout";
import PublicLayout from "../layout/PublicLayout";

export const RoutesComponent = () => {
  const user = useSelector((state) => state.user);
  const dispatch = useDispatch();
  const token = localStorage.getItem(JWT_TOKEN);
  try {
    if (user.exp === "" && token) {
      const decoded_token = jwtDecode(token);

      if (decoded_token) {
        dispatch(loginDispatch(decoded_token));
      }
    }
  } catch (error) {
    console.error(error);
  }

  return (
    <Routes>
      {publicRoutes.map(({ path, Component }) => (
        <Route
          key={path}
          path={path}
          element={
            <PublicLayout>
              <Component />
            </PublicLayout>
          }
        />
      ))}
      {user.cur === ROLES.PATIENT &&
        PatientRoutes.map(({ path, Component }) => (
          <Route
            key={path}
            path={path}
            element={
              <AuthLayout>
                <Component />
              </AuthLayout>
            }
          />
        ))}
      {user.cur === ROLES.ADMIN &&
        AdminRoutes.map(({ path, Component }) => (
          <Route
            key={path}
            path={path}
            element={
              <AuthLayout>
                <Component />
              </AuthLayout>
            }
          />
        ))}
      {user.cur === ROLES.CHEMIST &&
        ChemistRoutes.map(({ path, Component }) => (
          <Route
            key={path}
            path={path}
            element={
              <AuthLayout>
                <Component />
              </AuthLayout>
            }
          />
        ))}
      {(user.cur === ROLES.CHEMIST || user.cur === ROLES.PATIENT) &&
        PatientChemistRoutes.map(({ path, Component }) => (
          <Route
            key={path}
            path={path}
            element={
              <AuthLayout>
                <Component />
              </AuthLayout>
            }
          />
        ))}
      {(user.cur === ROLES.ADMIN ||
        user.cur === ROLES.PATIENT ||
        user.cur === ROLES.CHEMIST) &&
        AuthRoutes.map(({ path, Component }) => (
          <Route
            key={path}
            path={path}
            element={
              <AuthLayout>
                <Component />
              </AuthLayout>
            }
          />
        ))}

      <Route
        path="*"
        element={
          <PublicLayout>
            <Error />
          </PublicLayout>
        }
      />
    </Routes>
  );
};
