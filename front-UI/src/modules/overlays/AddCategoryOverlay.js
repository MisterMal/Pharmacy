import Overlay from "../../components/Overlay";
import {
  Button,
  TextField,
  Box,
  CircularProgress,
  Stack,
  MenuItem,
} from "@mui/material";
import { Close } from "@mui/icons-material";
import { useTranslation } from "react-i18next";
import React from "react";
import { useState } from "react";
import { toast } from "react-toastify";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { categorySchema } from "../../utils/Validations";
import { createCategory } from "../../api/moa/categoryApi";
import ConfirmationDialog from "../../components/ConfirmationDialog";

function AddCategoryOverlay({ open, onClose }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(categorySchema),
  });

  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);

  const onSubmit = handleSubmit((values) => {
    setData(values);
    setDialogOpen(true);
  });

  const confirm = function() {
    doSubmit(data);
    setDialogOpen(false);
  }

  const doSubmit = function({ name, isOnPrescription }) {
    setLoading(true);

    createCategory(name, isOnPrescription)
        .then(() => {
          setLoading(false);
          toast.success(t("category_created"), {
            position: "top-center",
          });
        })
        .catch((error) => {
          setLoading(false);

          if (error.response.status === 400) {
            toast.error(t("invalid_category_data"), {
              position: "top-center",
            });
          } else if (error.response.status === 409) {
            toast.error(t(error.response.data.message), {
              position: "top-center",
            });
          } else {
            toast.error(t("server_error"), {
              position: "top-center",
            });
          }
        });
    onClose();
  }

  return (
    <Overlay open={open}>
      <Button onClick={onClose}>
        <Close />
        {t("close")}
      </Button>
      <h2>{t("create_category")}</h2>
      <Stack spacing={4}>
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
        />
        <TextField
          select
          {...register("isOnPrescription")}
          variant="outlined"
          color="secondary"
          label={t("is_on_prescription")}
          fullWidth
          required
          error={errors.isOnPrescription}
          helperText={t(errors.isOnPrescription?.message)}
        >
          <MenuItem value={true}>{t("yes")}</MenuItem>
          <MenuItem value={false}>{t("no")}</MenuItem>
        </TextField>
        <Box
          sx={{ mb: 2 }}
          display="flex"
          justifyContent="center"
          alignItems="center"
        >
          {loading ? (
            <CircularProgress />
          ) : (
            <Button
              fullWidth
              onClick={handleSubmit(onSubmit)}
              type="submit"
              variant="contained"
            >
              {t("add_category")}
            </Button>
          )}
        </Box>
      </Stack>
      <ConfirmationDialog
          open={dialogOpen}
          title={t("confirm_reject_order")}
          actions={[
            { label: t("confirm"), handler: confirm, color: "primary" },
            { label: t("cancel"), handler: () => setDialogOpen(false), color: "secondary" },
          ]}
          onClose={() => setDialogOpen(false)}
      />
    </Overlay>
  );
}

export default AddCategoryOverlay;
