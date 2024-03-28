import React, {useState} from 'react';
import {Box, Button, CircularProgress, Paper, Stack, TextField} from '@mui/material';
import {useTranslation} from "react-i18next";
import {signUpAccount} from "../api/mok/accountApi";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import 'react-toastify/dist/ReactToastify.css';
import {toast, ToastContainer} from 'react-toastify';
import {signUpSchema} from "../utils/Validations";


function SignUp() {

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(signUpSchema),
    });

    const [passwordShown, setPasswordShown] = useState(false);
    const [confirmPasswordShown, setConfirmPasswordShown] = useState(false);
    const {t} = useTranslation();
    const [loading, setLoading] = useState(false)

    const onSubmit = handleSubmit(({name, lastName, login, email, password, phoneNumber, pesel, nip}) => {

        setLoading(true)

        signUpAccount(name, lastName, login, email, password, phoneNumber, pesel, nip).then(
            () => {
                setLoading(false)
                toast.success(t("account_created_check_email"), {
                    position: "top-center",
                })
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
        <div className="wrapper" style={{marginBottom: "3rem"}}>
            <Paper elevation={20} className="paper">
                <h2>
                    {t("sign_up")} </h2>
                <Stack spacing={4}>
                    <Stack spacing={2} direction="row">
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
                    <Box sx={{mb: 2}} display="flex" justifyContent="center" alignItems="center">
                        {loading ? <CircularProgress/> : (
                            <Button fullWidth onClick={handleSubmit(onSubmit)} type='submit' variant='contained'>
                                {t("sign_in")}
                            </Button>
                        )}
                    </Box>
                </Stack>
            </Paper>
            <ToastContainer/>
        </div>
    )
}

export default SignUp;