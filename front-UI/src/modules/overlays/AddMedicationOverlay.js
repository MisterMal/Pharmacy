import Overlay from "../../components/Overlay";
import {
  Button,
  TextField,
  Box,
  CircularProgress,
  Stack,
  Select,
  MenuItem,
  Autocomplete,
} from "@mui/material";
import { Close } from "@mui/icons-material";
import { useTranslation } from "react-i18next";
import React from "react";
import { useState } from "react";
import { addMedication } from "../../api/moa/medicationApi";
import { toast } from "react-toastify";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { medicationSchema } from "../../utils/Validations";
import { getAllCategories } from "../../api/moa/categoryApi";
import { useEffect } from "react";
import ConfirmationDialog from "../../components/ConfirmationDialog";

function AddMedicationOverlay({ open, onClose }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    watch,
  } = useForm({
    resolver: yupResolver(medicationSchema),
  });

  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dataToSubmit, setDataToSubmit] = useState(true);

  useEffect(() => {
    getAllCategories()
        .then((response) => {
          const categoriesData = response.data;
          setCategories(categoriesData);
        })
        .catch((error) => {
          console.error("Error fetching categories:", error);
        });
  }, []);

  const selectedCategory = watch("categoryName");

  const confirmSubmit = function() {
    doSubmit(dataToSubmit);
    setDialogOpen(false)
  }

  const onSubmit = handleSubmit((data) => {
    setDataToSubmit(data);
    setDialogOpen(true)
  })

  const doSubmit = function({ name, stock, price }) {
    setLoading(true);

    addMedication(name, stock, price, selectedCategory)
        .then(() => {
          setLoading(false);
          toast.success(t("medication_created"), {
            position: "top-center",
          });
        })
        .catch((error) => {
          setLoading(false);

          if (error.response.status === 400) {
            toast.error(t("invalid_medication_data"), {
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
  };

  return (
      <Overlay open={open}>
        <Button onClick={onClose}>
          <Close />
          {t("close")}
        </Button>
        <h2>{t("create_medication")}</h2>
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
          <Stack spacing={2} direction="row">
            <TextField
                type="text"
                variant="outlined"
                color="secondary"
                label={t("stock")}
                fullWidth
                required
                error={errors.stock}
                helperText={t(errors.stock?.message)}
                {...register("stock")}
            />
            <TextField
                type="text"
                variant="outlined"
                color="secondary"
                label={t("price")}
                fullWidth
                required
                sx={{ mb: 4 }}
                error={errors.price}
                helperText={t(errors.price?.message)}
                {...register("price")}
            />
          </Stack>
          <Autocomplete
              options={categories}
              getOptionLabel={(category) => category.name}
              onChange={(event, value) =>
                  setValue("categoryName", value?.name || "")
              }
              renderInput={(params) => (
                  <TextField
                      {...params}
                      label={t("category_name")}
                      variant="outlined"
                      color="secondary"
                      fullWidth
                      required
                      error={errors.categoryName}
                      helperText={t(errors.categoryName?.message)}
                  />
              )}
          />
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
                  {t("add_medication")}
                </Button>
            )}
          </Box>
        </Stack>
        <ConfirmationDialog
            open={dialogOpen}
            title={t("confirm_add_medication")}
            actions={[
              { label: t("confirm"), handler: confirmSubmit, color: "primary" },
              { label: t("cancel"), handler: () => setDialogOpen(false), color: "secondary" },
            ]}
            onClose={() => setDialogOpen(false)}
        />
      </Overlay>
  );
}

export default AddMedicationOverlay;
