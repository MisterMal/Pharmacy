import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import {
    Button,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import "react-toastify/dist/ReactToastify.css";

import {
    grantAdminRole,
    grantChemistRole,
    grantPatientRole,
} from "../../api/mok/accountApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import { useNavigate } from "react-router-dom";
import {
    adminSchema,
    chemistSchema,
    signUpSchema,
} from "../../utils/Validations";
import { yupResolver } from "@hookform/resolvers/yup";
import {Pathnames} from "../../router/Pathnames";

function AddRoleForm({ account, etag, hideChange }) {
    const [dialogOpen, setDialogOpen] = useState(false);
    const [dialogOpen2, setDialogOpen2] = useState(false);
    const [dialogOpen3, setDialogOpen3] = useState(false);
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [loading, setLoading] = useState(false);
    const [selectedRole, setSelectedRole] = useState("");
    const [showAdditionalForm, setShowAdditionalForm] = useState(false);
    const [bodyToSend, setBodyToSend] = useState({});
    const [usedRole, setUsedRole] = useState(["ADMIN", "CHEMIST", "PATIENT"]);

    const {
        handleSubmit,
        register,
        formState: { errors },
    } = useForm({
        resolver: yupResolver(signUpSchema),
    });

    const {
        handleSubmit: handleSubmitAdmin,
        register: registerAdmin,
        formState: { errors: errorsAdmin },
    } = useForm({
        resolver: yupResolver(adminSchema),
    });

    const {
        handleSubmit: handleSubmitChemist,
        register: registerChemist,
        formState: { errors: errorsChemist },
    } = useForm({
        resolver: yupResolver(chemistSchema),
    });

    const onSubmitAdmin = handleSubmitAdmin(({ workPhoneNumber }) => {
        const bodyAdmin = {
            login: account.login,
            workPhoneNumber: workPhoneNumber,
            version: account.version,
        };
        setBodyToSend(bodyAdmin);
        setDialogOpen2(true);
    });

    const onSubmitChemist = handleSubmitChemist(({ licenseNumber }) => {
        const bodyChemist = {
            login: account.login,
            licenseNumber: licenseNumber,
            version: account.version,
        };
        setBodyToSend(bodyChemist);
        setDialogOpen3(true);
    });

    const onSubmitPatient = handleSubmit(
        ({ name, lastName, phoneNumber, pesel, nip }) => {
            const bodyPatient = {
                login: account.login,
                name: name,
                lastName: lastName,
                phoneNumber: phoneNumber,
                pesel: pesel,
                NIP: nip,
                version: account.version,
            };

            setBodyToSend(bodyPatient);
            setDialogOpen(true);
        }
    );

    const onSendAdmin = () => {
        const tag = typeof etag === "string" ? etag.split('"').join("") : etag;

        console.log(bodyToSend);
        grantAdminRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                navigate(Pathnames.admin.accounts);
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message));
            });
    };

    const onSendChemist = () => {
        const tag = typeof etag === "string" ? etag.split('"').join("") : etag;

        console.log(bodyToSend);
        grantChemistRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                navigate(Pathnames.admin.accounts);
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message));
            });
    };

    const onSendPatient = () => {
        const tag = typeof etag === "string" ? etag.split('"').join("") : etag;

        console.log(bodyToSend);
        grantPatientRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                navigate(Pathnames.admin.accounts);
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message));
            });
    };

    const handleRoleChange = (event) => {
        setSelectedRole(event.target.value);
        setShowAdditionalForm(true);
    };

    return (
        <div>
            <form>
                <FormControl variant="outlined" fullWidth>
                    <InputLabel>Select Role</InputLabel>
                    <Select
                        value={selectedRole}
                        onChange={handleRoleChange}
                        label="Select Role"
                    >
                        {usedRole.map(
                            (role) =>
                                account.accessLevels.filter(
                                    (accessLevel) => accessLevel.role === role
                                ).length === 0 && <MenuItem value={role}>{role}</MenuItem>
                        )}
                    </Select>
                </FormControl>

                {showAdditionalForm && selectedRole === "ADMIN" && (
                    <>
                        <TextField
                            {...registerAdmin("workPhoneNumber")}
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("workPhoneNumber")}
                            fullWidth
                            sx={{ my: 4 }}
                            required
                            error={errorsAdmin.workPhoneNumber}
                            helperText={t(errorsAdmin.workPhoneNumber?.message)}
                        />
                        <Button
                            fullWidth
                            sx={{ mb: 4 }}
                            onClick={onSubmitAdmin}
                            color="primary"
                            type="submit"
                            variant="contained"
                        >
                            {t("add_access_level")}
                        </Button>
                    </>
                )}

                {showAdditionalForm && selectedRole === "CHEMIST" && (
                    <>
                        <TextField
                            {...registerChemist("licenseNumber")}
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{ my: 4 }}
                            label={t("licenseNumber")}
                            fullWidth
                            required
                            error={errorsChemist.licenseNumber}
                            helperText={t(errorsChemist.licenseNumber?.message)}
                        />
                        <Button
                            sx={{ mb: 4 }}
                            fullWidth
                            onClick={onSubmitChemist}
                            color="primary"
                            type="submit"
                            variant="contained"
                        >
                            {t("add_access_level")}
                        </Button>
                    </>
                )}

                {showAdditionalForm && selectedRole === "PATIENT" && (
                    <>
                        <TextField
                            {...register("name")}
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{ my: 4 }}
                            label={t("name")}
                            fullWidth
                            required
                            error={errors.name}
                            helperText={t(errors.name?.message)}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{ mb: 4 }}
                            label={t("last_name")}
                            fullWidth
                            required
                            error={errors.lastName}
                            helperText={t(errors.lastName?.message)}
                            {...register("lastName")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("phone_number")}
                            required
                            fullWidth
                            error={errors.phoneNumber}
                            helperText={t(errors.phoneNumber?.message)}
                            sx={{ mb: 4 }}
                            {...register("phoneNumber")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("pesel")}
                            required
                            fullWidth
                            error={errors.pesel}
                            helperText={t(errors.pesel?.message)}
                            sx={{ mb: 4 }}
                            {...register("pesel")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("nip")}
                            required
                            fullWidth
                            error={errors.nip}
                            helperText={t(errors.nip?.message)}
                            sx={{ mb: 4 }}
                            {...register("nip")}
                        />
                        <Button
                            fullWidth
                            sx={{ mb: 4 }}
                            onClick={onSubmitPatient}
                            color="primary"
                            type="submit"
                            variant="contained"
                        >
                            {t("add_access_level")}
                        </Button>
                    </>
                )}
            </form>

            <ConfirmationDialog
                open={dialogOpen}
                title={t("confirm_add_access_level")}
                actions={[
                    { label: t("proceed"), handler: onSendPatient, color: "primary" },
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpen(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpen(false)}
            />
            <ConfirmationDialog
                open={dialogOpen2}
                title={t("confirm_add_access_level")}
                actions={[
                    { label: t("proceed"), handler: onSendAdmin, color: "primary" },
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpen2(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpen2(false)}
            />
            <ConfirmationDialog
                open={dialogOpen3}
                title={t("confirm_add_access_level")}
                actions={[
                    { label: t("proceed"), handler: onSendChemist, color: "primary" },
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpen3(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpen3(false)}
            />
        </div>
    );
}

export default AddRoleForm;
