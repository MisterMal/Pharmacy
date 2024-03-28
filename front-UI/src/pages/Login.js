import React, {useState} from "react";
import {Paper, CircularProgress, Link, Dialog, DialogTitle, DialogActions} from "@mui/material";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import {Grid, TextField, Button, Box, Typography} from "@mui/material";
import {useTranslation} from "react-i18next";
import {signInAccount} from "../api/mok/accountApi";
import axios from "axios";
import {changeLevel, login as loginDispatch, logout} from "../redux/UserSlice";
import {useNavigate} from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer, toast} from 'react-toastify';
import jwtDecode from "jwt-decode";
import {useDispatch, useSelector} from "react-redux";
import {Pathnames} from "../router/Pathnames";
import {logInSchema} from "../utils/Validations";
import {JWT_TOKEN} from "../constants/Constants";



const Login = () => {

    const dispatch = useDispatch();

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(logInSchema),
    });

    const [passwordShown, setPasswordShown] = useState(false);
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [dialogOpen, setDialogOpen] = useState(false);
    const userRoles = useSelector((state) => state.user.roles);
    const user = useSelector((state) => state.user);
    const [loading, setLoading] = useState(false)
    const onSubmit = async ({login, password}) => {

        setLoading(true)

        signInAccount(login, password).then((response) => {
            setLoading(false)
            dispatch(logout())
            const jwt = response.data.jwtToken;
            localStorage.setItem(JWT_TOKEN, jwt);
            const decodedJWT = jwtDecode(jwt);
            dispatch(loginDispatch(decodedJWT));
            const roles = decodedJWT.roles;
            if (!roles.includes(",")) {
                navigate(Pathnames.auth.landing)
            } else {
                setDialogOpen(true)
            }
        }).catch((error) => {
            if (error.response.status === 403) {
                toast.error(t(error.response.data.message), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)

            } else if (error.response.status === 401) {
                toast.error(t(error.response.data.message), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            } else {
                toast.error(t("server_error"), {
                    position: toast.POSITION.TOP_CENTER,
                });
                setLoading(false)
            }
        })
    }
    const onResetPassword = async () => {
        navigate(Pathnames.public.resetPassword);
    }


    return (

        <div className="wrapper">
            <Paper elevation={20} className="paper">
                <h2>
                    {t("sign_in")} </h2>
                <form>
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("login")}
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.login}
                        helperText={t(errors.login?.message)}
                        {...register("login")}
                    />
                    <TextField
                        type={passwordShown ? "text" : "password"}
                        variant='outlined'
                        color='secondary'
                        label={t("password")}
                        required
                        fullWidth
                        error={errors.password}
                        helperText={t(errors.password?.message)}
                        sx={{mb: 4}}
                        {...register("password")}
                        InputProps={
                            {
                                endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ?
                                    <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> :
                                    <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>
                            }
                        }
                    />
                    <Box sx={{ mb: 2 }} display="flex" justifyContent="center" alignItems="center">
                        {loading ? <CircularProgress /> : (
                            <Button fullWidth  onClick={handleSubmit(onSubmit)} type='submit' variant='contained'>
                                {t("sign_in")}
                            </Button>
                        )}
                    </Box>
                </form>
                <Button onClick={onResetPassword}>
                    {t("to_reset_password")}
                </Button>
            </Paper>
            <ToastContainer/>
            <Dialog open={dialogOpen} onClose={() => {
                setDialogOpen(false)
            }}>
                <DialogTitle>{t("choose_role")}</DialogTitle>
                <DialogActions>
                    {
                        userRoles.map((role, index) => {
                            return <Button
                                key={index}
                                onClick={() => {
                                dispatch(changeLevel({
                                    sub: user.sub,
                                    roles: user.roles,
                                    index: index,
                                    exp: user.exp,
                                }));
                                navigate(Pathnames.auth.landing);
                                setDialogOpen(false)
                            }}>{t(role)}</Button>
                        })
                    }
                </DialogActions>
            </Dialog>
        </div>

    );
};

export default Login;