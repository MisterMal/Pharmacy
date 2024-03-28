import React, {useCallback, useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {
    Box,
    Button,
    IconButton, useTheme, Skeleton,
} from "@mui/material";
import {useTranslation} from "react-i18next";
import {toast, ToastContainer} from "react-toastify";
import RefreshIcon from "@mui/icons-material/Refresh";
import {approveOrder, cancelOrder, getOrdersToApprove} from "../api/moa/orderApi";
import ConfirmationDialog from "../components/ConfirmationDialog";

export default function OrdersToApprove() {
    const [orders, setOrders] = useState([]);
    const theme = useTheme();
    const [loading, setLoading] = useState(false);
    const [dialogRejectOpen, setDialogRejectOpen] = useState(false);
    const [dialogApproveOpen, setDialogApproveOpen] = useState(false);
    const [id, setId] = useState(false);

    const {t} = useTranslation();

    const findOrders = useCallback(async () => {
        setLoading(true)
        getOrdersToApprove().then((response) => {
            setLoading(false)
            setOrders(response.data);
        }).catch((error) => {
            setLoading(false)
            toast.error(t(error.response.data.message), {position: "top-center"});
        });
    }, []);

    useEffect(() => {
        findOrders();
    }, []);

    const confirmApprove = function() {
        doHandleApprove()
        setDialogApproveOpen(false)
    }

    const handleOrderApprove = function (idToSet) {
        setId(idToSet);
        setDialogApproveOpen(true)
    }

    const doHandleApprove = function () {
        approveOrder(id).then((response) => {
            toast.success(t("approved_successfully"), {position: "top-center"});
            findOrders();
        }).catch((error) => {
            toast.error(t(error.response.data.message), {position: "top-center"});
        })
    }

    const confirmReject = function() {
        doHandleReject();
        setDialogRejectOpen(false);
    }

    const handleOrderReject = function(idToSet) {
        setId(idToSet);
        setDialogRejectOpen(true)
    };

    const doHandleReject = function() {
        cancelOrder(id).then((response) => {
            toast.success(t("order_rejected"), {position: "top-center"});
            findOrders();
        }).catch((error) => {
            toast.error(t(error.response.data.message), {position: "top-center"});
        })
    }



    if (loading) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("patient_name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("medications")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("approve")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("reject")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                            <TableCell>
                                <Skeleton/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        );
    }

    return (
        <div style={{
            display: "flex",
            justifyContent: "center",
            alignContent: "center",
            flexDirection: "column"
        }}>
            <Box sx={{marginBottom: "10px", textAlign: "center"}}>
                <IconButton
                    variant="contained"
                    onClick={findOrders}
                    disabled={loading}
                >
                    <RefreshIcon/>
                </IconButton>
            </Box>
            <TableContainer sx={{maxWidth: "800px", margin: "auto"}} component={Paper}>
                <Table aria-label="simple table">
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow sx={{color: "white"}}>
                            <TableCell sx={{color: "white"}}>{t("patient_name")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("medications")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("approve")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("reject")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {orders.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{"&:last-child td, &:last-child th": {border: 0}}}
                            >
                                <TableCell component="th" scope="row">
                                    {row.patientData.firstName} {row.patientData.lastName}
                                </TableCell>
                                <TableCell>
                                    {row.orderMedication.map((om) => (
                                        <Box key={om.medication.name} sx={{m: 1}}>
                                            {om.medication.name}
                                        </Box>
                                    ))}
                                </TableCell>
                                <TableCell align="right">{row.prescription.prescriptionNumber}</TableCell>
                                <TableCell align="right">
                                    <Button variant="outlined" color={"success"}
                                            onClick={() => handleOrderApprove(row.id)}>
                                        {t("approve")}
                                    </Button>
                                </TableCell>
                                <TableCell align="right">
                                    <Button variant="outlined" color={"error"}
                                            onClick={() => handleOrderReject(row.id)}>
                                        {t("reject")}
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <ToastContainer/>
            </TableContainer>
            <ConfirmationDialog
                open={dialogApproveOpen}
                title={t("confirm_approve_order")}
                actions={[
                    { label: t("confirm"), handler: confirmApprove, color: "primary" },
                    { label: t("cancel"), handler: () => setDialogApproveOpen(false), color: "secondary" },
                ]}
                onClose={() => setDialogApproveOpen(false)}
            />
            <ConfirmationDialog
                open={dialogRejectOpen}
                title={t("confirm_reject_order")}
                actions={[
                    { label: t("confirm"), handler: confirmReject, color: "primary" },
                    { label: t("cancel"), handler: () => setDialogRejectOpen(false), color: "secondary" },
                ]}
                onClose={() => setDialogRejectOpen(false)}
            />
        </div>
    );
}
