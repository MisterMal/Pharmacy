import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { set, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import * as Yup from "yup";
import { getCategory, editCategory } from "../api/moa/categoryApi";
import { useState } from "react";
import { useParams } from "react-router-dom";
import ConfirmationDialog from "../components/ConfirmationDialog";
import { Paper, Box, TextField, Button, CircularProgress } from "@mui/material";
import { MenuItem, Select } from "@mui/material";
import { categorySchema } from "../utils/Validations";
import { useLocation } from "react-router-dom";

const paperStyle = {
  backgroundColor: "rgba(255, 255, 255, 0.75)",
  padding: "20px 20px",
  margin: "0px auto",
  width: 400,
};

export default function EditCategory() {
  const { state } = useLocation();
  const { id } = useParams();
  const { t } = useTranslation();
  const location = useLocation();
  const navigate = useNavigate();
  const [category, setCategory] = useState({});
  const [oldName, setOldName] = useState("");
  const [oldIsOnPrescription, setOldIsOnPrescription] = useState(false);
  const [etag, setEtag] = useState("");
  const [name, setName] = useState("");
  const [isOnPrescription, setIsOnPrescription] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(categorySchema),
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getCategory(id);
        setCategory(response.data);
        setEtag(response.headers["etag"]);
        setName(response.data.name);
        setLoading(false);
      } catch (error) {
        toast.error(t(error.response.data.message), { position: "top-center" });
      }
    };
    fetchData();
  }, []);

  const handleEditCategory = () => {
    setDialogOpen(false);
    setLoading(true);

    const body = {
      id: id,
      name: name,
      isOnPrescription: isOnPrescription,
      version: category.version,
    };
    const tag = etag.split('"').join("");

    editCategory(id, body, tag)
      .then((response) => {
        toast.success(t("category_edit_success"), { position: "top-center" });
        navigate("/categories");
      })
      .catch((error) => {
        if (error.response.status === 409) {
          toast.error(t("category_edit_conflict"), { position: "top-center" });
          navigate("/categories");
        }
        toast.error(t(error.response.data.message), { position: "top-center" });
      });
  };

  const proceedCategory = (data) => {
    setName(data.name);
    setIsOnPrescription(data.isOnPrescription);
    setDialogOpen(true);
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignContent: "center",
        marginTop: "3rem",
      }}
    >
      <Paper elevation={20} style={paperStyle}>
        <h2 style={{ fontFamily: "Lato" }}>{t("edit_category")}</h2>
        <Box sx={{ marginBottom: 4 }}>
          <TextField
            {...register("name")}
            type="text"
            variant="outlined"
            color="secondary"
            label={t("name")}
            fullWidth
            required
            error={errors.name}
            helperText={t(errors.name?.message)}
            defaultValue={location.state.name}
            focused
          />
          <Box sx={{ marginBottom: 2 }} />
          <TextField
            {...register("isOnPrescription")}
            variant="outlined"
            color="secondary"
            label={t("is_on_prescription")}
            fullWidth
            required
            select
            error={errors.isOnPrescription}
            helperText={t(errors.isOnPrescription?.message)}
            defaultValue={location.state.isOnPrescription}
            focused
          >
            <MenuItem value={true}>{t("yes")}</MenuItem>
            <MenuItem value={false}>{t("no")}</MenuItem>
          </TextField>
          <Box sx={{ marginBottom: 2 }} />
          {loading ? (
            <CircularProgress
              style={{ marginRight: "auto", marginLeft: "auto" }}
            />
          ) : (
            <Button
              fullWidth
              onClick={handleSubmit(proceedCategory)}
              type="submit"
              variant="contained"
            >
              {t("edit_category")}
            </Button>
          )}
        </Box>

        <ConfirmationDialog
          open={dialogOpen}
          title={t("confirm_edit_account_data")}
          actions={[
            {
              label: t("proceed"),
              handler: handleEditCategory,
              color: "primary",
            },
            {
              label: t("cancel"),
              handler: () => setDialogOpen(false),
              color: "secondary",
            },
          ]}
          onClose={() => setDialogOpen(false)}
        />
        <ToastContainer />
      </Paper>
    </div>
  );
}
