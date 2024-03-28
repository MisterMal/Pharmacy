import React, {useState} from "react";
import {useForm} from "react-hook-form";
import {toast} from "react-toastify";
import {
    Button,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
} from "@mui/material";
import {useTranslation} from "react-i18next";
import "react-toastify/dist/ReactToastify.css";

import {
    grantAdminRole,
    grantChemistRole,
    grantPatientRole,
} from "../../api/mok/accountApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";
import {
    adminSchema,
    chemistSchema, patientSchema,
} from "../../utils/Validations";
import {yupResolver} from "@hookform/resolvers/yup";
import {ROLES} from "../../constants/Constants";
import {useNavigate} from "react-router-dom";

function AddRoleForm({account, etag, hideChange}) {
    const [dialogOpenPatient, setDialogOpenPatient] = useState(false);
    const [dialogOpenAdmin, setDialogOpenAdmin] = useState(false);
    const [dialogOpenChemist, setDialogOpenChemist] = useState(false);
    const {t} = useTranslation();
    const [loading, setLoading] = useState(false);
    const [selectedRole, setSelectedRole] = useState("");
    const [showAdditionalForm, setShowAdditionalForm] = useState(false);
    const [bodyToSend, setBodyToSend] = useState({});
    const tag = typeof etag === "string" ? etag.split('"').join("") : etag;

    const {
        handleSubmit: handleSubmitPatient,
        register: registerPatient,
        formState: {errors: errorsPatient},
    } = useForm({
        resolver: yupResolver(patientSchema),
    });

    const {
        handleSubmit: handleSubmitAdmin,
        register: registerAdmin,
        formState: {errors: errorsAdmin},
    } = useForm({
        resolver: yupResolver(adminSchema),
    });

    const {
        handleSubmit: handleSubmitChemist,
        register: registerChemist,
        formState: {errors: errorsChemist},
    } = useForm({
        resolver: yupResolver(chemistSchema),
    });

    const onSubmitAdmin = handleSubmitAdmin(({workPhoneNumber}) => {
        const bodyAdmin = {
            login: account.login,
            workPhoneNumber: workPhoneNumber,
            version: account.version,
        };
        setBodyToSend(bodyAdmin);
        setDialogOpenAdmin(true);
    });

    const onSubmitChemist = handleSubmitChemist(({licenseNumber}) => {
        const bodyChemist = {
            login: account.login,
            licenseNumber: licenseNumber,
            version: account.version,
        };
        setBodyToSend(bodyChemist);
        setDialogOpenChemist(true);
    });

    const onSubmitPatient = handleSubmitPatient(
        ({name, lastName, phoneNumber, pesel, nip}) => {
            const bodyPatient = {
                login: account.login,
                firstName: name,
                lastName: lastName,
                phoneNumber: phoneNumber,
                pesel: pesel,
                nip: nip,
                version: account.version,
            };

            setBodyToSend(bodyPatient);
            setDialogOpenPatient(true);
        }
    );

    const onSendAdmin = () => {
        grantAdminRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                toast.success(t("access_level_added_successfully"), {
                    position: toast.POSITION.TOP_CENTER,
                });
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message));
            });
    };

    const onSendChemist = () => {
        grantChemistRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                toast.success(t("access_level_added_successfully"), {
                    position: toast.POSITION.TOP_CENTER,
                });
            })
            .catch((error) => {
                setLoading(false);
                toast.error(t(error.response.data.message));
            });
    };

    const onSendPatient = () => {
        grantPatientRole(account.id, bodyToSend, tag)
            .then((res) => {
                setLoading(false);
                hideChange(false);
                toast.success(t("access_level_added_successfully"), {
                    position: toast.POSITION.TOP_CENTER,
                });
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
                        {Object.keys(ROLES).map(
                            (role) =>
                                account.accessLevels.filter(
                                    (accessLevel) => accessLevel.role === role
                                ).length === 0 && <MenuItem key={role} value={role}>{role}</MenuItem>
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
                            sx={{my: 4}}
                            required
                            error={errorsAdmin.workPhoneNumber}
                            helperText={t(errorsAdmin.workPhoneNumber?.message)}
                        />
                        {
                            loading ? <CircularProgress/> :
                                <Button
                                    fullWidth
                                    sx={{mb: 4}}
                                    onClick={onSubmitAdmin}
                                    color="primary"
                                    type="submit"
                                    variant="contained"
                                >
                                    {t("add_access_level")}
                                </Button>
                        }
                    </>
                )}

                {showAdditionalForm && selectedRole === "CHEMIST" && (
                    <>
                        <TextField
                            {...registerChemist("licenseNumber")}
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{my: 4}}
                            label={t("licenseNumber")}
                            fullWidth
                            required
                            error={errorsChemist.licenseNumber}
                            helperText={t(errorsChemist.licenseNumber?.message)}
                        />
                        {
                            loading ? <CircularProgress/> :
                                <Button
                                    sx={{mb: 4}}
                                    fullWidth
                                    onClick={onSubmitChemist}
                                    color="primary"
                                    type="submit"
                                    variant="contained"
                                >
                                    {t("add_access_level")}
                                </Button>
                        }
                    </>
                )}

                {showAdditionalForm && selectedRole === "PATIENT" && (
                    <>
                        <TextField
                            {...registerPatient("name")}
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{my: 4}}
                            label={t("name")}
                            fullWidth
                            required
                            error={errorsPatient.name}
                            helperText={t(errorsPatient.name?.message)}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            sx={{mb: 4}}
                            label={t("last_name")}
                            fullWidth
                            required
                            error={errorsPatient.lastName}
                            helperText={t(errorsPatient.lastName?.message)}
                            {...registerPatient("lastName")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("phone_number")}
                            required
                            fullWidth
                            error={errorsPatient.phoneNumber}
                            helperText={t(errorsPatient.phoneNumber?.message)}
                            sx={{mb: 4}}
                            {...registerPatient("phoneNumber")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("pesel")}
                            required
                            fullWidth
                            error={errorsPatient.pesel}
                            helperText={t(errorsPatient.pesel?.message)}
                            sx={{mb: 4}}
                            {...registerPatient("pesel")}
                        />
                        <TextField
                            type="text"
                            variant="outlined"
                            color="secondary"
                            label={t("nip")}
                            required
                            fullWidth
                            error={errorsPatient.nip}
                            helperText={t(errorsPatient.nip?.message)}
                            sx={{mb: 4}}
                            {...registerPatient("nip")}
                        />
                        {
                            loading ? <CircularProgress/> :
                                <Button
                                    fullWidth
                                    sx={{mb: 4}}
                                    onClick={onSubmitPatient}
                                    color="primary"
                                    type="submit"
                                    variant="contained"
                                >
                                    {t("add_access_level")}
                                </Button>
                        }
                    </>
                )}
            </form>

            <ConfirmationDialog
                open={dialogOpenPatient}
                title={t("confirm_add_access_level")}
                actions={[
                    {label: t("proceed"), handler: onSendPatient, color: "primary"},
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpenPatient(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpenPatient(false)}
            />
            <ConfirmationDialog
                open={dialogOpenAdmin}
                title={t("confirm_add_access_level")}
                actions={[
                    {label: t("proceed"), handler: onSendAdmin, color: "primary"},
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpenAdmin(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpenAdmin(false)}
            />
            <ConfirmationDialog
                open={dialogOpenChemist}
                title={t("confirm_add_access_level")}
                actions={[
                    {label: t("proceed"), handler: onSendChemist, color: "primary"},
                    {
                        label: t("cancel"),
                        handler: () => setDialogOpenChemist(false),
                        color: "secondary",
                    },
                ]}
                onClose={() => setDialogOpenChemist(false)}
            />
        </div>
    );
}

export default AddRoleForm;
