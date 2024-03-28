import * as Yup from "yup";
import React, { useState } from "react";
import { Button, CircularProgress, Paper, Radio, RadioGroup, FormControlLabel } from "@mui/material";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useTranslation } from "react-i18next";
import { ToastContainer } from "react-toastify";
import { useNavigate } from 'react-router-dom';
import {Pathnames} from "../router/Pathnames";


function RoleSelectionForm() {
    const roleSelectionSchema = Yup.object().shape({});

    const {
        handleSubmit,
        formState: { errors },
    } = useForm({
        resolver: yupResolver(roleSelectionSchema),
    });

    const [selectedRole, setSelectedRole] = useState('');
    const navigate = useNavigate();
    const paperStyle = { padding: '30px 20px', margin: "auto", width: 400 }
    const { t } = useTranslation();
    const [loading, setLoading] = useState(false);

    const handleRoleChange = (event) => {
        setSelectedRole(event.target.value);
    };

    const onSubmit = () => {
        //e.preventDefault();
        setLoading(false)

        if (selectedRole === 'patient') {
            navigate(Pathnames.admin.addPatient);
        } else if (selectedRole === 'chemist') {
            navigate(Pathnames.admin.addChemist);
        } else if (selectedRole === 'admin') {
            navigate(Pathnames.admin.addAdministrator);
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignContent: 'center', marginTop: '3rem' }}>
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{ fontFamily: 'Lato' }}>{t("choose_role")}</h2>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <RadioGroup value={selectedRole} onChange={handleRoleChange}>
                        <FormControlLabel value="patient" control={<Radio />} label={t("patient")} />
                        <FormControlLabel value="chemist" control={<Radio />} label={t("chemist")} />
                        <FormControlLabel value="admin" control={<Radio />} label={t("admin")} />
                    </RadioGroup>

                    {loading ? (
                        <CircularProgress />
                    ) : (
                        <Button fullWidth onClick={handleSubmit(onSubmit)} type="submit" variant="contained">
                            {t("submit")}
                        </Button>
                    )}
                </form>
            </Paper>
            <ToastContainer />
        </div>
    );
}

export default RoleSelectionForm;
