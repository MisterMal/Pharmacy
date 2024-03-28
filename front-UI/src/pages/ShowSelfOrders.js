import React, {useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import {Skeleton, useTheme, Box} from "@mui/material";
import {approveOrderById, getSelfOrders, withdrawOrderById} from "../api/moa/orderApi";
import moment from "moment";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import {toast} from "react-toastify";
import ConfirmationDialog from "../components/ConfirmationDialog";
import {useTranslation} from "react-i18next";
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from "@mui/material/IconButton";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function ShowSelfOrders() {

    const [selfOrders, setSelfOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [dialogApproveOpen, setDialogApproveOpen] = useState(false);
    const [approveOrderId, setApproveOrderId] = useState(null);
    const {t} = useTranslation();
    const [deleteOrderId, setDeleteOrderId] = useState(null);
    const [refreshing, setRefreshing] = useState(false);
    const theme = useTheme();

    function whichPriceToShow(order, orderMedication) {
        if (order.orderState === "FINALISED" || order.orderState === "REJECTED_BY_CHEMIST" ||
            order.orderState === "REJECTED_BY_PATIENT" || order.orderState === "APPROVED_BY_PATIENT") {
            return orderMedication.purchasePrice
        } else {
            return orderMedication.medication.currentPrice
        }
    }

    function getPatientOrders() {
        setLoading(true);

        getSelfOrders()
            .then((response) => {
                setSelfOrders(response.data);
                console.log(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.log(error);
                setLoading(false);
            });
    }

    useEffect(() => {
        getPatientOrders();
    }, []);


    const handleRefresh = () => {
        getPatientOrders();
    };

    const formatOrderDate = (dateString) => {
        const trimmedDate = dateString.slice(0, -5);
        const date = moment(trimmedDate);
        return date.format("DD-MM-YYYY HH:mm:ss");
    };

    const handleApproveOrder = (approveOrderId) => {
        setApproveOrderId(approveOrderId);
        setDialogApproveOpen(true);
    };

    const acceptApproveOrder = () => {
        approveOrderById(approveOrderId)
            .then(() => {
                const updatedOrders = selfOrders.filter((order) => order.id !== approveOrderId);
                setSelfOrders(updatedOrders);

                setApproveOrderId(null);
                setDialogApproveOpen(false);

                toast.success(t("order_approved"), {
                    position: "top-center",
                });
            });
    };

    const rejectApproveOrder = () => {
        setApproveOrderId(null);
        setDialogApproveOpen(false);
    };

    const handleDeleteOrder = (deleteOrderId) => {
        setDeleteOrderId(deleteOrderId);
        setDialogOpen(true);
    };

    const acceptDeleteOrder = () => {

        withdrawOrderById(deleteOrderId)
            .then(() => {const updatedOrders = selfOrders.filter((order) => order.id !== deleteOrderId);
                setSelfOrders(updatedOrders);

                //setDeleteOrderId(null);
                setDialogOpen(false);

                toast.success(t("order_rejected"), {
                    position: "top-center",
                })
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
                            <TableCell sx={{color: "white"}}>{t("is_order_in_queue")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_total_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("approve_order")}</TableCell>
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
        <div style={{display: "flex", justifyContent: "center", alignContent: "center", flexDirection: "column"}}>
            <Box sx={{marginBottom: "10px", textAlign: "center"}}>
                <IconButton
                    variant="contained"
                    onClick={handleRefresh}
                    disabled={refreshing}
                >
                    <RefreshIcon/>
                </IconButton>
            </Box>
            <TableContainer sx={{maxWidth: "800px", margin: "auto"}} component={Paper}>
                <Table>
                    <TableHead sx={{backgroundColor: theme.palette.primary.main}}>
                        <TableRow>
                            <TableCell sx={{color: "white"}}>{t("order_state")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_date")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_name")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("medication_quantity")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("order_total_price")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("prescription_number")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("approve_order")}</TableCell>
                            <TableCell sx={{color: "white"}}>{t("delete_order")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {selfOrders.map((order, index) => {
                            const totalPrice = order.orderMedication.reduce((sum, medication) => {
                                return sum + whichPriceToShow(order, medication) * medication.quantity;
                            }, 0);

                            const isOrderToBeApproved = order.orderState === "TO_BE_APPROVED_BY_PATIENT";

                            return order.orderMedication.map((orderMedication, medicationIndex) => (
                                <React.Fragment key={`${index}-${medicationIndex}`}>
                                    {medicationIndex === 0 && (
                                        <TableRow>
                                            <TableCell
                                                rowSpan={order.orderMedication.length}>{t(order.orderState)}</TableCell>
                                            <TableCell
                                                rowSpan={order.orderMedication.length}>{formatOrderDate(order.orderDate)}</TableCell>
                                            <TableCell>{orderMedication.medication.name}</TableCell>
                                            <TableCell>{whichPriceToShow(order, orderMedication)}</TableCell>
                                            <TableCell>{orderMedication.quantity}</TableCell>
                                            {medicationIndex === 0 && (
                                                <TableCell
                                                    rowSpan={order.orderMedication.length}>{totalPrice}</TableCell>
                                            )}
                                            {order.prescription && medicationIndex === 0 ? (
                                                <TableCell
                                                    rowSpan={order.orderMedication.length}>{order.prescription.prescriptionNumber}</TableCell>
                                            ) : (
                                                <TableCell rowSpan={order.orderMedication.length}>-</TableCell>
                                            )}
                                            {medicationIndex === 0 && isOrderToBeApproved && (
                                                <TableCell rowSpan={order.orderMedication.length}>
                                                    <IconButton
                                                        color="success"
                                                        aria-label={t("approve_order")}
                                                        onClick={() => handleApproveOrder(order.id)}
                                                    >
                                                        <CheckCircleIcon/>
                                                    </IconButton>
                                                    <ConfirmationDialog
                                                        open={dialogApproveOpen}
                                                        title={t("confirm_approve_order")}
                                                        actions={[
                                                            {
                                                                label: t("confirm"),
                                                                handler: acceptApproveOrder,
                                                                color: "primary"
                                                            },
                                                            {
                                                                label: t("cancel"),
                                                                handler: rejectApproveOrder,
                                                                color: "secondary"
                                                            },
                                                        ]}
                                                        onClose={() => setDialogApproveOpen(false)}
                                                    />
                                                </TableCell>
                                            )}
                                            {order.orderState === "TO_BE_APPROVED_BY_PATIENT" && (
                                                <TableCell>
                                                    <IconButton color="error" aria-label={t("delete_order")}
                                                                onClick={() => handleDeleteOrder(order.id)}>
                                                        <DeleteIcon/>
                                                    </IconButton>
                                                    <ConfirmationDialog
                                                        open={dialogOpen}
                                                        title={t("confirm_delete_order")}
                                                        actions={[
                                                            {
                                                                label: t("confirm"),
                                                                handler: acceptDeleteOrder,
                                                                color: "primary"
                                                            },
                                                            {
                                                                label: t("cancel"),
                                                                handler: rejectDeleteOrder,
                                                                color: "secondary"
                                                            },
                                                        ]}
                                                        onClose={() => setDialogOpen(false)}
                                                    />
                                                </TableCell>)}
                                        </TableRow>
                                    )}
                                    {medicationIndex > 0 && (
                                        <TableRow>
                                            <TableCell>{orderMedication.medication.name}</TableCell>
                                            <TableCell>{whichPriceToShow(order, orderMedication)}</TableCell>
                                            <TableCell>{orderMedication.quantity}</TableCell>
                                        </TableRow>
                                    )}
                                </React.Fragment>
                            ));
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}