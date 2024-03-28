import { useLocation, useNavigate, useParams } from "react-router-dom";
import {Button, CircularProgress, Dialog, DialogActions, DialogTitle, Paper, TextField} from "@mui/material";
import React, {useState} from "react";
import {toast} from "react-toastify";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import {changeSelfAccountEmail} from "../../api/mok/accountApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import {Pathnames} from "../../router/Pathnames";

const changeEmailSchema = Yup.object().shape({
    newEmail: Yup.string()
        .min(5, 'email_length_min')
        .max(50, 'email_length_max')
        .email('email_valid')
        .required('email_required'),
});

function ChangeEmailForm({account, etag, hideChange}) {

    const [dialogOpen, setDialogOpen] = useState(false);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false)
    const {t} = useTranslation()
    const [newEmail, setNewEmail] = useState("")

    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm({
        resolver: yupResolver(changeEmailSchema),
    });

    const handleChange = async () => {
        setLoading((true))
        const tag = etag.split('"').join("")
        const body = {
            login: account.login,
            email: newEmail,
            version: account.version
        };
        changeSelfAccountEmail(body, tag).then((res) => {
            setLoading((state) => !state)
            hideChange((state) => !state)
            navigate(Pathnames.auth.self);
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
    const proceed = ({newEmail}) => {
        setNewEmail(newEmail)
        setDialogOpen(true)
    }

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem'}}>


            <form>
                <TextField  sx={{mb: 4}}
                            {...register("newEmail")}
                            type={"text"}
                            variant='outlined'
                            color='secondary'
                            label={t("new_email")}
                            fullWidth
                            required
                            error={errors.newEmail}
                            helperText={t(errors.newEmail?.message)}
                />
                {
                    loading ? <CircularProgress/> :
                        <Button fullWidth sx={{mb: 2}}
                                onClick={handleSubmit(proceed)} type='submit'
                                variant='contained'>{t("change_email_button")}</Button>
                }
            </form>

            <ConfirmationDialog
                open={dialogOpen}
                title={t("confirm")}
                actions={[
                    {label: t("proceed"), handler:handleChange, color: 'primary'},
                    {label: t("cancel"), handler: () => setDialogOpen(false), color: 'secondary'},
                ]}
                onClose={() => setDialogOpen(false)}
            />

        </div>
    );
}

export default ChangeEmailForm;