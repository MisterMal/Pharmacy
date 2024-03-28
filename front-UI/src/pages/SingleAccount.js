import {Box, Button, Grid, Paper, Stack, TextField, Typography,} from "@mui/material";
import LinearProgress from "@mui/material/LinearProgress";
import LockOpenIcon from "@mui/icons-material/LockOpen";
import LockIcon from "@mui/icons-material/Lock";
import {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import ConfirmationDialog from "../components/ConfirmationDialog";

import {
    blockRoleAdmin,
    blockRoleChemist,
    blockRolePatient,
    getAccountDetails,
    unblockRoleAdmin,
    unblockRoleChemist,
    unblockRolePatient
} from "../api/mok/accountApi";
import AddRoleForm from "../modules/accounts/AddRoleForm";
import ChangeOtherEmailForm from "../modules/accounts/ChangeOtherEmailForm";
import ChangeOtherPasswordForm from "../modules/accounts/ChangeOtherPasswordForm";
import {toast} from "react-toastify";

function SingleAccount() {
    const {id} = useParams();
    const [account, setAccount] = useState({});
    const [accessLevels, setAccessLevels] = useState([]);
    const [addRole, setAddRole] = useState(false);
    const [editRole, setEditRole] = useState(false);
    const {t} = useTranslation();
    const [changePass, setChangePass] = useState(false);
    const [changeEmail, setChangeEmail] = useState(false);
    const [etag, setEtag] = useState("");
    const [dialogOpen, setDialogOpen] = useState(false);
    const [idToBlockOrUnblok, setIdToBlockOrUnblok] = useState(0);
    const [accessLevelToBlockOrUnblock, setAccessLevelToBlockOrUnblock] = useState("");
    const [blockOrUnblok, setBlockOrUnblock] = useState(false);
    const navigate = useNavigate();
    const paperStyle = {
        backgroundColor: "rgba(255, 255, 255, 0.75)",
        padding: "20px 20px",
        margin: "0px auto",
        width: 400,
    };
    const [loading, setLoading] = useState(true);

    const accept = () => {

        if (blockOrUnblok) {
            if (accessLevelToBlockOrUnblock === "ADMIN") {
                blockRoleAdmin(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_blocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            } else if (accessLevelToBlockOrUnblock === "CHEMIST") {
                blockRoleChemist(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_blocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            } else if (accessLevelToBlockOrUnblock === "PATIENT") {
                blockRolePatient(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_blocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            }

            setBlockOrUnblock((prev) => !prev);

        } else {
            if (accessLevelToBlockOrUnblock === "ADMIN") {
                unblockRoleAdmin(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_unblocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            } else if (accessLevelToBlockOrUnblock === "CHEMIST") {
                unblockRoleChemist(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_unblocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            } else if (accessLevelToBlockOrUnblock === "PATIENT") {
                unblockRolePatient(idToBlockOrUnblok).then((response) => {
                    fetchAccountDetails()
                    toast.success(t("access_level_sucessfully_unblocked"), {position: "top-center"});
                }).catch((error) => {
                    toast.error(t(error.response.data.message), {position: "top-center"});
                })
            }

            setBlockOrUnblock((prev) => !prev);
        }
        setDialogOpen(false)
    };

    const reject = () => {
        setDialogOpen(false);
    };

    const fetchAccountDetails = () => {
        getAccountDetails(id).then(
            (response) => {
                setAccount(response.data);
                setAccessLevels(response.data.accessLevels);
                setEtag(response.headers["etag"]);
                setLoading(false);
            })
            .catch((error) => {
                toast.error(t(error.response.data.message), {position: "top-center"});
            });
    }

    useEffect(() => {

        fetchAccountDetails()
    }, [changePass, addRole, changeEmail]);


    const isAdmin =
        accessLevels && accessLevels.some((level) => level.role === "ADMIN");
    const isChemist =
        accessLevels && accessLevels.some((level) => level.role === "CHEMIST");
    const isPatient =
        accessLevels && accessLevels.some((level) => level.role === "PATIENT");

    if (loading) {
        return (
            <div>
                <LinearProgress/>
            </div>
        );
    }

    const handleChangePassword = () => {
        setChangePass((state) => !state);
    };

    const handleEditAccountDetails = () => {
        navigate(`/accounts/${id}/edit`);
    };

    const handleAddRole = () => {
        setAddRole((state) => !state);
    };

    const handleChangeEmail = () => {
        setChangeEmail((state) => !state);
    };

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignContent: "center",
                marginTop: "3rem",
                marginBottom: "3rem",
            }}
        >
            <ConfirmationDialog
                open={dialogOpen}
                title={blockOrUnblok === true ? t("block_access_level_confirmation") : t("unblock_access_level_confirmation")}
                actions={[
                    {label: t("confirm"), handler: accept, color: "primary"},
                    {label: t("cancel"), handler: reject, color: "secondary"},
                ]}
                onClose={() => setDialogOpen(false)}
            />
            <Paper elevation={20} style={paperStyle}>
                <h2 style={{fontFamily: "Lato"}}>{t("account_details")} </h2>
                <Stack spacing={2} direction="row" sx={{marginBottom: 4}}>
                    <TextField
                        variant="outlined"
                        color="secondary"
                        value={account.confirmed}
                        label={t("confirmed")}
                        
                        
                    />
                    <TextField
                        type="text"
                        variant="outlined"
                        color="secondary"
                        value={account.active}
                        label={t("active")}
                        
                        
                    />
                </Stack>
                <Typography
                    style={{fontFamily: "Lato", fontSize: 18, fontWeight: 400}}
                    variant="h6"
                    component="div"
                    sx={{flexGrow: 1, mb: 1}}
                    type="text"
                    
                    
                >
                    Email
                </Typography>
                <Typography
                    style={{fontSize: 16}}
                    variant="h6"
                    component="div"
                    sx={{flexGrow: 1, mb: 2}}
                    type="text"
                    
                    
                >
                    {account.email}
                </Typography>
                {
                    changeEmail ? (
                        <>
                            <ChangeOtherEmailForm account={account} etag={etag} hideChange={setChangeEmail}/>
                            <Grid item xs={6}>
                                <Button onClick={handleChangeEmail}>{t("back_button")}</Button>
                            </Grid>
                        </>
                    ) : (
                        <Grid container spacing={2}>
                            <Grid item xs={6}>
                                <Button onClick={handleChangeEmail}>{t("change_email")}</Button>
                            </Grid>
                        </Grid>

                    )
                }
                {changePass ? (
                    <>
                        <ChangeOtherPasswordForm
                            account={account}
                            etag={etag}
                            hideChange={setChangePass}
                        />
                        <Grid item xs={6}>
                            <Button onClick={handleChangePassword}>{t("back_button")}</Button>
                        </Grid>
                    </>
                ) : (
                    <Grid container spacing={2}>

                        <Grid item xs={6}>
                            <Button onClick={handleChangePassword}>
                                {t("change_password_button")}
                            </Button>
                        </Grid>
                    </Grid>
                )}
                {
                    accessLevels.length > 2 ? (
                        <></>
                    ) : (
                        <>
                            {addRole ? (
                                <>
                                    <AddRoleForm
                                        account={account}
                                        etag={etag}
                                        hideChange={setAddRole}
                                    />
                                    <Grid item xs={6}>
                                        <Button onClick={handleAddRole}>{t("back_button")}</Button>
                                    </Grid>
                                </>
                            ) : (
                                <Grid container spacing={2}>
                                    <Grid item xs={6}>
                                        <Button onClick={handleAddRole}>{t("add_role")}</Button>
                                    </Grid>
                                </Grid>
                            )}
                        </>
                    )

                }

                <Typography
                    style={{fontFamily: "Lato", fontSize: 18, fontWeight: 400}}
                    variant="h6"
                    component="div"
                    sx={{flexGrow: 1, mb: 1, mt: 1}}
                    type="text"
                >
                    Login
                </Typography>
                <Typography
                    style={{fontSize: 16}}
                    variant="h6"
                    component="div"
                    sx={{flexGrow: 1, mb: 2}}
                    type="text"
                >
                    {account.login}
                </Typography>
                <Typography
                    style={{fontFamily: "Lato", fontSize: 18, fontWeight: 400}}
                    variant="h6"
                    component="div"
                    sx={{flexGrow: 1, mb: 1}}
                    type="text"
                >
                    {t("access_level")}
                </Typography>
                {
                    <div style={{display: 'flex'}}>
                        {accessLevels.map((level) => (
                            <div key={level.role} style={{marginRight: '10px'}}>
                                <h6>{level.role}</h6>
                                <Button onClick={() => {
                                    setIdToBlockOrUnblok(account.id);
                                    setAccessLevelToBlockOrUnblock(level.role)
                                    setDialogOpen(true)
                                    level.active ? setBlockOrUnblock(true) : setBlockOrUnblock(false)
                                }}>
                                    {level.active ? <LockOpenIcon/> : <LockIcon/>}
                                </Button>
                            </div>
                        ))}
                    </div>
                }
                {isAdmin && (
                    <Box>
                        <Typography
                            style={{fontFamily: "Lato", fontSize: 18, fontWeight: 400}}
                            component="div"
                            sx={{flexGrow: 1, mb: 1}}
                            type="text"
                        >
                            {t("work_phone_number")}
                        </Typography>
                        {accessLevels.map((level) =>
                            level.workPhoneNumber ? (
                                <Typography
                                    key={level.workPhoneNumber}
                                    style={{fontSize: 16}}
                                    variant="h6"
                                    component="div"
                                    sx={{flexGrow: 1, mb: 2}}
                                    type="text"
                                >
                                    {String(level.workPhoneNumber)}
                                </Typography>
                            ) : null
                        )}
                    </Box>
                )}
                {isChemist && (
                    <Box>
                        {accessLevels.map((level) =>
                            level.licenseNumber ? (
                                <Box key={String(level.licenseNumber)}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        {t("licesne_number")}
                                    </Typography>

                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.licenseNumber)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}
                    </Box>
                )}
                {isPatient && (
                    <Box>
                        {accessLevels.map((level) =>
                            level.firstName ? (
                                <Box key={level.firstName}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        {t("name")}
                                    </Typography>
                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.firstName)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}

                        {accessLevels.map((level) =>
                            level.lastName ? (
                                <Box key={level.lastName}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        {t("last_name")}
                                    </Typography>
                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.lastName)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}
                        {accessLevels.map((level) =>
                            level.phoneNumber ? (
                                <Box key={level.phoneNumber}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        {t("phone_number")}
                                    </Typography>
                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.phoneNumber)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}
                        {accessLevels.map((level) =>
                            level.pesel ? (
                                <Box key={level.pesel}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        Pesel
                                    </Typography>
                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.pesel)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}
                        {accessLevels.map((level) =>
                            level.NIP ? (
                                <Box key={level.NIP}>
                                    <Typography
                                        style={{
                                            fontFamily: "Lato",
                                            fontSize: 18,
                                            fontWeight: 400,
                                        }}
                                        component="div"
                                        sx={{flexGrow: 1, mb: 1}}
                                        type="text"
                                    >
                                        NIP
                                    </Typography>
                                    <Typography
                                        style={{fontSize: 16}}
                                        variant="h6"
                                        component="div"
                                        sx={{flexGrow: 1, mb: 2}}
                                        type="text"
                                    >
                                        {String(level.NIP)}
                                    </Typography>
                                </Box>
                            ) : null
                        )}
                    </Box>
                )}
                <Button onClick={handleEditAccountDetails}>
                    {t("edit_account_details")}
                </Button>
            </Paper>
        </div>
    );
}

export default SingleAccount;
