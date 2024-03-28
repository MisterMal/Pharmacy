import React, {useState} from 'react';
import {Grid, Paper, TextField, Button, CircularProgress} from '@mui/material';
import {useTranslation} from "react-i18next";
import {addChemist} from "../api/mok/accountApi";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import Typography from '@mui/material/Typography';
import {Container, Stack} from '@mui/material';
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer, toast} from 'react-toastify';
import {useNavigate} from "react-router-dom";
import {Pathnames} from "../router/Pathnames";


const addChemistSchema = Yup.object().shape({
    login: Yup.string()
        .min(5, 'login_length_min')
        .max(50, 'login_length_max')
        .required('login_required'),
    email: Yup.string()
        .email('email_valid')
        .required('email_required'),
    password: Yup.string()
        .min(8, 'password_length_min')
        .max(50, 'password_length_max')
        .matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
            "password_invalid"
        )
        .required('Password is required'),
    confirmPassword: Yup.string()
        .oneOf([Yup.ref('password'), null], 'passwords_not_match')
        .required('confirm_password_required'),
    licenseNumber: Yup.string()
        .matches(/^[0-9]{6}$/, "license_invalid")
        .required('license_required')
});

function AddChemist() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(addChemistSchema),
    });

    const paperStyle = {padding: '20px 20px', margin: "0px auto", width: 400}
    const headerStyle = {margin: 0}
    const [passwordShown, setPasswordShown] = useState(false);
    const [confirmPasswordShown, setConfirmPasswordShown] = useState(false);
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const onSubmit = handleSubmit(({login, email, password, confirmPassword, licenseNumber}) => {

        setLoading(true)

        const chemistData = {
            login: login,
            email: email,
            password: password,
            confirmPassword: confirmPassword,
            licenseNumber: licenseNumber
        };

        addChemist(chemistData).then(
            () => {
                setLoading(false)
                toast.success(t("account_created"), {
                    position: "top-center",
                })
                navigate(Pathnames.auth.landing);
            }
        ).catch(error => {
            setLoading(false)

            if (error.response.status === 400) {
                toast.error(t("invalid_account_data"), {
                    position: "top-center",
                })
            } else if (error.response.status === 409) {
                toast.error(t(error.response.data.message), {
                    position: "top-center",
                })
            } else {
                toast.error(t("server_error"), {
                    position: "top-center",
                })
            }
        })
    })

    return (
        // <Grid container spacing={2}>
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignContent: 'center',
            marginTop: '3rem'
        }}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: 'Lato'}}>
                    {t("add_chemist")} </h2>
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
                        type="email"
                        variant='outlined'
                        color='secondary'
                        label={t("email")}
                        fullWidth
                        required
                        sx={{mb: 4}}
                        error={errors.email}
                        helperText={t(errors.email?.message)}
                        {...register("email")}
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
                    <TextField
                        type={confirmPasswordShown ? "text" : "password"}
                        variant='outlined'
                        color='secondary'
                        label={t("confirm_password")}
                        required
                        fullWidth
                        error={errors.confirmPassword}
                        helperText={t(errors.confirmPassword?.message)}
                        sx={{mb: 4}}
                        {...register("confirmPassword")}
                        InputProps={
                            {
                                endAdornment: <Button
                                    onClick={() => setConfirmPasswordShown(!confirmPasswordShown)}>{confirmPasswordShown ?
                                    <VisibilityOffIcon fontSize="small" sx={{color: 'black'}}/> :
                                    <VisibilityIcon fontSize="small" sx={{color: 'black'}}/>}</Button>
                            }
                        }
                    />
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("license_number")}
                        fullWidth
                        required
                        error={errors.licenseNumber}
                        helperText={t(errors.licenseNumber?.message)}
                        sx={{ mb: 4 }}
                        {...register("licenseNumber")}
                    />

                    {
                        loading ? <CircularProgress style={{marginRight: "auto", marginLeft: "auto"}}/> :
                            <Button fullWidth
                                    onClick={onSubmit} type='submit' variant='contained'>{t("submit")}</Button>
                    }
                </form>
            </Paper>
            <ToastContainer/>
        </div>
    )
}

export default AddChemist;