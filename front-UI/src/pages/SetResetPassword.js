import * as Yup from "yup";
import React, { useState } from "react";
import { Button, CircularProgress, Paper, TextField } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useTranslation } from "react-i18next";
import { setResetPassword } from "../api/mok/accountApi";
import { toast, ToastContainer } from "react-toastify";
import { useParams, useNavigate } from "react-router-dom";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import VisibilityIcon from "@mui/icons-material/Visibility";
import {Pathnames} from "../router/Pathnames";

const setPasswordSchema = Yup.object().shape({
    password: Yup.string()
        .min(8, "password_length_min")
        .max(50, "password_length_max")
        .matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
            "password_invalid"
        )
        .required("password_required"),
    confirmPassword: Yup.string()
        .oneOf([Yup.ref("password"), null], "passwords_not_match")
        .required("confirm_password_required"),
});

function SetResetPassword() {
    const { token } = useParams(); // Parametr z linku resetującego hasło
    const {
        register,
        handleSubmit,
        formState: { errors },
        reset,
    } = useForm({
        resolver: yupResolver(setPasswordSchema),
    });

    const [passwordShown, setPasswordShown] = useState(false);
    const paperStyle = { padding: "30px 20px", margin: "auto", width: 400 };
    const { t } = useTranslation();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();


    const onSubmit = async ({ password }) => {
        setLoading(true);
        setResetPassword(token, password)
            .then((response) => {
                setLoading(false);
                toast.success(t("password_set_success"), {
                    position: "top-center",
                });
                reset(); // Resetowanie formularza
                navigate(Pathnames.public.login);
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t("server_error"), {
                    position: toast.POSITION.TOP_CENTER,
                });
            });
    };

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                marginTop: "3rem",
            }}
        >
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{ fontFamily: "Lato" }}>{t("set_new_password")}</h2>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <TextField
                        type={passwordShown ? "text" : "password"}
                        variant="outlined"
                        color="secondary"
                        label={t("new_password")}
                        fullWidth
                        required
                        sx={{ mb: 4 }}
                        error={errors.password}
                        helperText={t(errors.password?.message)}
                        {...register("password")}
                        InputProps={
                            {
                                endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ?
                                    <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> :
                                    <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>
                            }
                    }
                    />

                    <TextField
                        type={passwordShown ? "text" : "password"}
                        variant="outlined"
                        color="secondary"
                        label={t("confirm_new_password")}
                        fullWidth
                        required
                        sx={{ mb: 4 }}
                        error={errors.confirmPassword}
                        helperText={t(errors.confirmPassword?.message)}
                        {...register("confirmPassword")}
                            InputProps={
                        {
                            endAdornment: <Button onClick={() => setPasswordShown(!passwordShown)}>{passwordShown ?
                            <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> :
                            <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>
                        }
                    }
                    />

                    {loading ? (
                        <CircularProgress />
                    ) : (
                        <Button fullWidth type="submit" variant="contained">
                            {t("submit")}
                        </Button>
                    )}
                </form>
            </Paper>
            <ToastContainer />
        </div>
    );
}

export default SetResetPassword;
