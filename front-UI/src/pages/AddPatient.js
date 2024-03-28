import React, {useState} from 'react';
import {Grid, Paper, TextField, Button, CircularProgress} from '@mui/material';
import {useTranslation} from "react-i18next";
import {addPatient} from "../api/mok/accountApi";
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


const addPatientSchema = Yup.object().shape({
    name: Yup.string()
        .min(2, 'first_name_length_min')
        .max(50, 'first_name_length_max')
        .matches(/^[a-zA-Z\s]+$/, "name_invalid")
        .required('first_name_required'),
    lastName: Yup.string()
        .min(2, 'last_name_lenght_min')
        .max(50, 'last_name_length_max')
        .matches(/^[a-zA-Z\s]+$/, "last_name_invalid")
        .required('last_name_required'),
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
    phoneNumber: Yup.string()
        .min(9, 'phone_number_length')
        .max(9, 'phone_number_length')
        .matches(/^[0-9]+$/, "phone_number_only_digits")
        .required('phone_number_required'),
    pesel: Yup.string()
        .min(11, 'pesel_length')
        .max(11, 'pesel_length')
        .matches(/^[0-9]+$/, "pesel_only_digits")
        .required('pesel_required'),
    nip: Yup.string()
        .min(10, 'nip_length')
        .matches(/^[0-9]+$/, "nip_only_digits")
        .max(10, 'nip_length')
        .required('nip_required')
});

function AddPatient() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(addPatientSchema),
    });

    const paperStyle = {padding: '20px 20px', margin: "0px auto", width: 400}
    const [passwordShown, setPasswordShown] = useState(false);
    const [confirmPasswordShown, setConfirmPasswordShown] = useState(false);
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const onSubmit = handleSubmit(({name, lastName, login, email, password, confirmPassword, phoneNumber, pesel, nip}) => {

        setLoading(true)

        const patientData = {
            name: name,
            lastName: lastName,
            login: login,
            email: email,
            password: password,
            confirmPassword: confirmPassword,
            phoneNumber: phoneNumber,
            pesel: pesel,
            nip: nip
        };

        addPatient(patientData).then(
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
                    {t("add_patient")} </h2>
                <form>
                    <Stack spacing={2} direction="row" sx={{marginBottom: 4}}>
                        <TextField
                            {...register("name")}
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("name")}
                            fullWidth
                            required
                            error={errors.name}
                            helperText={t(errors.name?.message)}
                        />
                        <TextField
                            type="text"
                            variant='outlined'
                            color='secondary'
                            label={t("last_name")}
                            fullWidth
                            required
                            error={errors.lastName}
                            helperText={t(errors.lastName?.message)}
                            {...register("lastName")}
                        />
                    </Stack>
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
                        label={t("phone_number")}
                        required
                        fullWidth
                        error={errors.phoneNumber}
                        helperText={t(errors.phoneNumber?.message)}
                        sx={{mb: 4}}
                        {...register("phoneNumber")}
                    />
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("pesel")}
                        required
                        fullWidth
                        error={errors.pesel}
                        helperText={t(errors.pesel?.message)}
                        sx={{mb: 4}}
                        {...register("pesel")}
                    />
                    <TextField
                        type="text"
                        variant='outlined'
                        color='secondary'
                        label={t("nip")}
                        required
                        fullWidth
                        error={errors.nip}
                        helperText={t(errors.nip?.message)}
                        sx={{mb: 4}}
                        {...register("nip")}
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

export default AddPatient;