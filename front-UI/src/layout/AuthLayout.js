import React, {useEffect, useState} from 'react';
import AuthNavbar from "../components/AuthNavbar";
import {useLocation, useNavigate} from "react-router-dom";
import jwtDecode from "jwt-decode";
import {useTranslation} from "react-i18next";
import {useDispatch, useSelector} from "react-redux";
import {toast, ToastContainer} from "react-toastify";
import {Pathnames} from "../router/Pathnames";
import {logout} from "../redux/UserSlice";
import {login as loginDispatch} from "../redux/UserSlice";
import {ACCESS_LEVEL, JWT_TOKEN, ROLES} from "../constants/Constants";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {Box} from "@mui/system";
import AuthSidebar from "../components/AuthSidebar";
import {blue, pink, purple} from "@mui/material/colors";
import {colors} from "@mui/material";

const guestTheme = createTheme({
    palette: {
        primary: blue,
    },
    typography: {
        fontFamily: ["Lato", "sans-serif"].join(","),
    },
    components: {
        MuiTableCell: {
            styleOverrides: {
                root: {
                    textAlign: 'center',
                },
            },
        },
    },

});

const adminTheme = createTheme({
    palette: {
        primary: {
            main: colors.red[800],
        }
    },
    typography: {
        fontFamily: ["Lato", "sans-serif"].join(","),
    },
    components: {
        MuiTableCell: {
            styleOverrides: {
                root: {
                    textAlign: 'center',
                },
            },
        },
    },
});

const chemistTheme = createTheme({
    palette: {
        primary: purple,
    },
    typography: {
        fontFamily: ["Lato", "sans-serif"].join(","),
    },
    components: {
        MuiTableCell: {
            styleOverrides: {
                root: {
                    textAlign: 'center',
                },
            },
        },
    },
});

const patientTheme = createTheme({
    palette: {
        primary: pink,
    },
    typography: {
        fontFamily: ["Lato", "sans-serif"].join(","),
    },
    components: {
        MuiTableCell: {
            styleOverrides: {
                root: {
                    textAlign: 'center',
                },
            },
        },
    },
});


function AuthLayout({children}) {

    const location = useLocation();
    const userRole = useSelector((state) => state.user.cur);
    const token = localStorage.getItem(JWT_TOKEN);
    const navigate = useNavigate();
    const {t} = useTranslation();
    const dispatch = useDispatch();
    const [currentTheme, setCurrentTheme] = useState(guestTheme);

    useEffect(() => {
        if (userRole === ROLES.ADMIN) {
            setCurrentTheme(adminTheme);
        } else if (userRole === ROLES.CHEMIST) {
            setCurrentTheme(chemistTheme);
        } else if (userRole === ROLES.PATIENT) {
            setCurrentTheme(patientTheme);
        }
    }, [userRole]);

    useEffect(() => {
        try {
            setTimeout(() => {

                if (!localStorage.getItem(JWT_TOKEN) && !localStorage.getItem(ACCESS_LEVEL)) {
                    navigate(Pathnames.public.error);
                } else {
                    const decoded_token = jwtDecode(token);
                    if (decoded_token.exp < Date.now() / 1000) {
                        toast.error(t("session_expired_login_again"))
                        dispatch(logout());
                        navigate(Pathnames.public.login)
                    } else {
                        dispatch(loginDispatch(decoded_token));
                    }
                }

            }, 50);

        } catch (error) {
            console.error(error);
        }

    }, [location.pathname])

    return (
        <ThemeProvider theme={currentTheme}>
            <Box sx={{display: 'flex', position: 'relative', alignItems: 'flex-start'}}>
                <AuthSidebar/>
                <Box sx={{height: '100%', width: '100%'}}>
                    <AuthNavbar/>
                    {children}
                </Box>
                <ToastContainer
                    position="top-right"
                    autoClose={1500}
                    hideProgressBar={false}
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                    theme="light"/>
            </Box>
        </ThemeProvider>
    );
}

export default AuthLayout;