import React, { useCallback, useEffect, useState } from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { getAllCategories } from "../api/moa/categoryApi";
import { Box, Button, IconButton, Skeleton, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { toast, ToastContainer } from "react-toastify";
import RefreshIcon from "@mui/icons-material/Refresh";
import AddCategoryOverlay from "../modules/overlays/AddCategoryOverlay";
import { Close } from "@mui/icons-material";
import AddIcon from "@mui/icons-material/Add";

export default function AllCategories() {
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();
  const theme = useTheme();
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [addCategory, setAddCategory] = useState(false);

  const { t } = useTranslation();

  const findCategories = useCallback(async () => {
    setLoading(true);
    setRefreshing(true);
    await getAllCategories()
      .then((response) => {
        setLoading(false);
        setCategories(response.data);
      })
      .catch((error) => {
        setLoading(false);
        toast.error(t(error.response.data.message), { position: "top-center" });
      });
    setRefreshing(false);
  }, []);

  useEffect(() => {
    findCategories();
  }, [findCategories]);

  const handleRefresh = () => {
    findCategories();
  };

  const handleEditCategory = (id) => {
    const category = categories.find((row) => row.id === id);

    if (category) {
      navigate(`/categories/${id}/edit`, {
        state: {
          name: category.name,
          isOnPrescription: category.isOnPrescription,
        },
      });
    }
  };

  if (loading) {
    return (
      <TableContainer component={Paper}>
        <Table>
          <TableHead sx={{ backgroundColor: theme.palette.primary.main }}>
            <TableRow>
              <TableCell sx={{ color: "white" }}>{t("name")}</TableCell>
              <TableCell sx={{ color: "white" }} align="right">
                {t("is_on_prescription")}
              </TableCell>
              <TableCell sx={{ color: "white" }} align="right">
                {t("edit")}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow>
              <TableCell>
                <Skeleton />
              </TableCell>
              <TableCell>
                <Skeleton />
              </TableCell>
              <TableCell>
                <Skeleton />
              </TableCell>
              <TableCell>
                <Skeleton />
              </TableCell>
              <TableCell>
                <Skeleton />
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    );
  }

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignContent: "center",
        flexDirection: "column",
      }}
    >
      <Box sx={{ marginBottom: "10px", textAlign: "center" }}>
        <IconButton
          variant="contained"
          onClick={handleRefresh}
          disabled={refreshing}
        >
          <RefreshIcon />
        </IconButton>
        <IconButton
          variant="contained"
          onClick={() => setAddCategory(true)}
          disabled={refreshing}
        >
          <AddIcon />
        </IconButton>
      </Box>
      <TableContainer
        sx={{ maxWidth: "800px", margin: "auto" }}
        component={Paper}
      >
        <Table aria-label="simple table">
          <TableHead sx={{ backgroundColor: theme.palette.primary.main }}>
            <TableRow>
              <TableCell sx={{ color: "white" }}>{t("name")}</TableCell>
              <TableCell sx={{ color: "white" }} align="right">
                {t("is_on_prescription")}
              </TableCell>
              <TableCell sx={{ color: "white" }} align="right">
                {t("edit")}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {categories.map((row) => (
              <TableRow
                key={row.id}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.name}
                </TableCell>
                <TableCell align="right">
                  {row.isOnPrescription ? t("yes") : t("no")}
                </TableCell>
                <TableCell align="right">
                  <Button
                    onClick={() =>
                      handleEditCategory(row.id, row.name, row.isOnPrescription)
                    }
                  >
                    {t("edit")}
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <ToastContainer />
      </TableContainer>
      <AddCategoryOverlay
        open={addCategory}
        onClose={() => {
          setAddCategory(false);
          setTimeout(handleRefresh, 100);
        }}
      />
    </div>
  );
}
