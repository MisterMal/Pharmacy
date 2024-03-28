import React, {useCallback,useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {Box,Skeleton, useTheme,} from "@mui/material";
import {getWaitingOrders, deleteWaitingOrdersById} from "../api/moa/orderApi";
import moment from "moment";
import {useTranslation} from "react-i18next";
import IconButton from "@mui/material/IconButton";
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from "@mui/icons-material/Refresh";
import {toast, ToastContainer} from "react-toastify";
import ConfirmationDialog from "../components/ConfirmationDialog";


export default function ShowWaitingOrders() {

    const [waitingOrders, setWaitingOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [deleteOrderId, setDeleteOrderId] = useState(null);
    const {t} = useTranslation();
    const theme = useTheme();

    const findOrders = useCallback(async () => {
        setLoading(true)
        getWaitingOrders().then((response) => {
            setLoading(false)
            setWaitingOrders(response.data);

        }).catch((error) => {
            setLoading(false)
            toast.error(t(error.response.data.message), {position: "top-center"});
        });
    }, []);

    useEffect(() => {
        findOrders();
    }, [findOrders]);


    const formatOrderDate = (dateString) => {
        const trimmedDate = dateString.slice(0, -5);
        const date = moment(trimmedDate);
        return date.format('DD-MM-YYYY HH:mm:ss');
    };

    const handleDeleteOrder = (deleteOrderId) => {
        setDeleteOrderId(deleteOrderId);
        setDialogOpen(true);
    };

    const acceptDeleteOrder = () => {

        deleteWaitingOrdersById(deleteOrderId)
            .then(() => {
                const updatedOrders = waitingOrders.filter((order) => order.id !== deleteOrderId);
                setWaitingOrders(updatedOrders);

                setDeleteOrderId(null);
                setDialogOpen(false);
                toast.success(t("order_deleted_successfully"), { position: "top-center" });
            })
    };
    const rejectDeleteOrder = () => {
        setDeleteOrderId(null);
        setDialogOpen(false);
    };


    if (loading) {
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("patient_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("medications")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("delete_order")}</TableCell>
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
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("patient_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("medications")}</TableCell>
                            <TableCell sx={{color: "white"}} align="right">{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("delete_order")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>

                        {waitingOrders.map((row) => (
                            <TableRow key={row.id} sx={{"&:last-child td, &:last-child th": {border: 0}}}>
                                <TableCell component="th" scope="row">
                                    {row.patientData.firstName} {row.patientData.lastName}
                                </TableCell>
                                <TableCell>{formatOrderDate(row.orderDate)}</TableCell>
                                <TableCell>
                                    {row.orderMedication.map((om) => (
                                        <Box key={om.medication.name} sx={{ m: 1 }}>
                                            {om.medication.name}
                                        </Box>
                                    ))}
                                </TableCell>
                                <TableCell align="right">{row.prescription ? row.prescription.prescriptionNumber : t("none")}</TableCell>
                                <TableCell>
                                    <IconButton color="error" aria-label={t("delete_order")} onClick={() => handleDeleteOrder(row.id)}>
                                        <DeleteIcon />
                                    </IconButton>
                                    <ConfirmationDialog
                                        open={dialogOpen}
                                        title={t("confirm_delete_order")}
                                        actions={[
                                            { label: t("confirm"), handler: acceptDeleteOrder, color: "primary" },
                                            { label: t("cancel"), handler: rejectDeleteOrder, color: "secondary" },
                                        ]}
                                        onClose={() => setDialogOpen(false)}
                                    />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <ToastContainer/>
            </TableContainer>
        </div>
    );
}

