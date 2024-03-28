import { Button, CircularProgress, TextField } from "@mui/material";
import React, { useState } from 'react';
import { useTranslation } from "react-i18next";

import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer, toast } from 'react-toastify';
import * as Yup from "yup";
import { changeOtherAccountEmail } from "../../api/mok/accountApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";
const ChangeEmailSchema = Yup.object().shape({

    newEmail: Yup.string()
        .email('email_valid')
        .required('email_required'),
    confirmEmail: Yup.string()
        .email('email_valid')
        .required('email_required')
        .oneOf([Yup.ref('newEmail'), null], 'emails_not_match')
        ,
});
function ChangeOtherEmailForm({account, etag, hideChange}) {
    const [dialogOpen, setDialogOpen] = useState(false);
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [newEmail, setNewEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const paperStyle = {padding: '30px 20px', margin: "auto", width: 400};
    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(ChangeEmailSchema),
    });

    const handleReset = async () =>{

        setLoading((true))
        const tag = etag.split('"').join("")
        const body = {
            login: account.login,
            email: newEmail,
            version: account.version
        };
        changeOtherAccountEmail(account.id, body, tag).then((res) =>{
         
            setLoading((state)=> !state)
            setDialogOpen((state)=> !state)
            hideChange((state)=> !state)
            //navigate('/accounts');
            toast.success(t("success"), {
                position: "top-center",
            })
           
        }).catch(error => {
            setLoading((state)=> !state)

            if (error.response.status === 500) {
                toast.error(t("server_error"), {
                    position: "top-center",
                })
            } else   {
                toast.error(t(error.response.data.message), {
                    position: "top-center",
                })
            }
        })

    }
    const proceed = ({newEmail}) =>{

        setNewEmail(newEmail)
        setDialogOpen(true)

    }





    return (
        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>
           
               

            <form>
                <TextField  sx={{mb: 4}}
                            {...register("newEmail")}
                            type="email"
                            variant='outlined'
                            color='secondary'
                            label={t("new_Email")}
                            fullWidth
                            required
                            error={errors.newEmail}
                            helperText={t(errors.newEmail?.message)}

                />
                <TextField  sx={{mb: 4}}
                            {...register("confirmEmail")}
                            type="email"
                            variant='outlined'
                            color='secondary'
                            label={t("confirm_Email")}
                            fullWidth
                            required
                            error={errors.confirmEmail}
                            helperText={t(errors.confirmEmail?.message)}

                />
                {
                    loading ? <CircularProgress/> :
                        <Button fullWidth sx={{mb: 2}}
                                onClick={handleSubmit(proceed)} type='submit'
                                variant='contained'>{t("change_email")}</Button>
                }
            </form>

            <ConfirmationDialog
                open={dialogOpen}
                title={t("confirm_change_email")}
                actions={[
                    {label: t("proceed"), handler:handleReset, color: 'primary'},
                    {label: t("cancel"), handler: () => setDialogOpen(false), color: 'secondary'},
                ]}
                onClose={() => setDialogOpen(false)}
            />
     <ToastContainer/>
        </div>
    );
}

export default ChangeOtherEmailForm