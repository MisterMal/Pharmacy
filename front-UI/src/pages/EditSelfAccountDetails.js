import { yupResolver } from "@hookform/resolvers/yup";
import { Box, Button, CircularProgress, Paper, TextField } from "@mui/material";
import { default as React, useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import * as Yup from "yup";
import {
  editSelfAdminData,
  editSelfChemistData,
  editSelfPatientData,
  getSelfAccountDetails,
} from "../api/mok/accountApi";
import ConfirmationDialog from "../components/ConfirmationDialog";
import { useSelector } from "react-redux";

const paperStyle = {
  backgroundColor: "rgba(255, 255, 255, 0.75)",
  padding: "20px 20px",
  margin: "0px auto",
  width: 400,
};

const patientSchema = Yup.object().shape({
  name: Yup.string()
    .min(2, "first_name_length_min")
    .max(50, "first_name_length_max")
    .required("first_name_required"),
  lastName: Yup.string()
    .min(2, "last_name_lenght_min")
    .max(50, "last_name_length_max")
    .required("last_name_required"),
  phoneNumber: Yup.string()
    .min(9, "phone_number_length")
    .max(9, "phone_number_length")
    .matches(/^[0-9]+$/, "phone_number_only_digits")
    .required("phone_number_required"),
  pesel: Yup.string()
    .min(11, "pesel_length")
    .max(11, "pesel_length")
    .matches(/^[0-9]+$/, "pesel_only_digits")
    .required("pesel_required"),
  nip: Yup.string()
    .min(10, "nip_length")
    .matches(/^[0-9]+$/, "nip_only_digits")
    .max(10, "nip_length")
    .required("nip_required"),
});

const chemistSchema = Yup.object().shape({
  licenceNumber: Yup.string()
    .matches(/^\d{6}$/, "licence_number_invalid")
    .required("licence_number_required"),
});

const adminSchema = Yup.object().shape({
  workPhoneNumber: Yup.string()
    .matches(/^\d{9}$/, "work_phone_number_invalid")
    .required("work_phone_number_required"),
});
function EditSingleAccount() {
  const [dialogOpen, setDialogOpen] = useState(false);

  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [pesel, setPesel] = useState("");
  const [nip, setNip] = useState("");
  const [workPhoneNumber, setWorkPhoneNumber] = useState("");
  const [licenseNumber, setLicenseNumber] = useState("");
  const [loading, setLoading] = useState(false);
  const [account, setAccount] = useState({});
  const [accessLevels, setAccessLevels] = useState([]);
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [etag, setEtag] = useState("");

  const [patient, setPatient] = useState(false);
  const [chemist, setChemist] = useState(false);
  const [admin, setAdmin] = useState(false);

  const user = useSelector((state) => state.user);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getSelfAccountDetails();
        setAccount(response.data);
        setAccessLevels(response.data.accessLevels);
        setEtag(response.headers["etag"]);
        setLoading(false);
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, []);

  const superAdmin = user.cur === "ADMIN";
  const superChemist = user.cur === "CHEMIST";
  const superPatient = user.cur === "PATIENT";

  const isAdmin =
    accessLevels &&
    accessLevels.some((level) => level.role === "ADMIN") &&
    superAdmin;
  const isChemist =
    accessLevels &&
    accessLevels.some((level) => level.role === "CHEMIST") &&
    superChemist;
  const isPatient =
    accessLevels &&
    accessLevels.some((level) => level.role === "PATIENT") &&
    superPatient;

  let selectedSchema;
  if (isAdmin) {
    selectedSchema = adminSchema;
  } else if (isChemist) {
    selectedSchema = chemistSchema;
  } else {
    selectedSchema = patientSchema;
  }
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(selectedSchema),
  });
  const handleEditPatient = () => {
    setLoading(true);
    setDialogOpen(false);

    if (patient) {
      const body = {
        login: account.login,
        firstName: name,
        lastName: lastName,
        phoneNumber: phoneNumber,
        pesel: pesel,
        nip: nip,
        version: account.version,
      };
      const tag = etag.split('"').join("");
      editSelfPatientData(body, tag)
        .then((res) => {
          setLoading((state) => !state);
          navigate(`/home/self`);
          toast.success(t("success"), {
            position: "top-center",
          });
        })
        .catch((error) => {
          setLoading((state) => !state);

          if (error.response.status === 500) {
            toast.error(t("server_error"), {
              position: "top-center",
            });
          } else {
            toast.error(t(error.response.data.message), {
              position: "top-center",
            });
          }
        });
    } else if (chemist) {
      const body = {
        login: account.login,
        licenseNumber: licenseNumber,
        version: account.version,
      };
      const tag = etag.split('"').join("");
      editSelfChemistData(body, tag)
        .then((res) => {
          setLoading((state) => !state);
          navigate(`/home/self`);
          toast.success(t("success"), {
            position: "top-center",
          });
        })
        .catch((error) => {
          setLoading((state) => !state);

          if (error.response.status === 500) {
            toast.error(t("server_error"), {
              position: "top-center",
            });
          } else {
            toast.error(t(error.response.data.message), {
              position: "top-center",
            });
          }
        });
    } else {
      const body = {
        workPhoneNumber: workPhoneNumber,
        login: account.login,
        version: account.version,
      };
      const tag = etag.split('"').join("");
      editSelfAdminData(body, tag)
        .then((res) => {
          setLoading((state) => !state);
          navigate(`/home/self`);
          toast.success(t("success"), {
            position: "top-center",
          });
        })
        .catch((error) => {
          setLoading((state) => !state);

          if (error.response.status === 500) {
            toast.error(t("server_error"), {
              position: "top-center",
            });
          } else {
            toast.error(t(error.response.data.message), {
              position: "top-center",
            });
          }
        });
    }
  };

  const proceedPatient = (data) => {
    setPatient(true);
    setName(data.name);
    setLastName(data.lastName);
    setPhoneNumber(data.phoneNumber);
    setPesel(data.pesel);
    setNip(data.nip);
    setDialogOpen(true);
  };
  const proceedChemist = (data) => {
    setChemist(true);
    setLicenseNumber(data.licenceNumber);
    setDialogOpen(true);
  };
  const proceedAdmin = (data) => {
    setAdmin(true);
    setWorkPhoneNumber(data.workPhoneNumber);
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
        <h2 style={{ fontFamily: "Lato" }}>{t("edit_account_details")} </h2>

        {isAdmin && (
          <Box sx={{ marginBottom: 4 }}>
            <TextField
              {...register("workPhoneNumber")}
              type="text"
              variant="outlined"
              color="secondary"
              label={t("work_phone_number")}
              fullWidth
              required
              error={errors.workPhoneNumber}
              helperText={t(errors.workPhoneNumber?.message)}
              defaultValue={
                accessLevels.find((level) => level.role === "ADMIN")
                  .workPhoneNumber
              }
            />
            <Box sx={{ marginBottom: 2 }} />

            {loading ? (
              <CircularProgress
                style={{ marginRight: "auto", marginLeft: "auto" }}
              />
            ) : (
              <Button
                fullWidth
                onClick={handleSubmit(proceedAdmin)}
                type="submit"
                variant="contained"
              >
                {t("edit_admin_data")}
              </Button>
            )}
          </Box>
        )}
        {isChemist && (
          <Box sx={{ marginBottom: 4 }}>
            <TextField
              {...register("licenceNumber")}
              type="text"
              variant="outlined"
              color="secondary"
              label={t("licesne_number")}
              fullWidth
              required
              error={errors.licenceNumber}
              helperText={t(errors.licenceNumber?.message)}
              defaultValue={
                accessLevels.find((level) => level.role === "CHEMIST")
                  .licenseNumber
              }
            />
            <Box sx={{ marginBottom: 2 }} />

            {loading ? (
              <CircularProgress
                style={{ marginRight: "auto", marginLeft: "auto" }}
              />
            ) : (
              <Button
                fullWidth
                onClick={handleSubmit(proceedChemist)}
                type="submit"
                variant="contained"
              >
                {t("edit_chemist_data")}
              </Button>
            )}
          </Box>
        )}
        {isPatient && (
          <Box sx={{ marginBottom: 4 }}>
            <form>
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
                defaultValue={
                  accessLevels.find((level) => level.role === "PATIENT")
                    .firstName
                }
              />
              <Box sx={{ marginBottom: 2 }} />
              <TextField
                {...register("lastName")}
                type="text"
                variant="outlined"
                color="secondary"
                label={t("last_name")}
                fullWidth
                required
                error={errors.last_name}
                helperText={t(errors.last_name?.message)}
                defaultValue={
                  accessLevels.find((level) => level.role === "PATIENT")
                    .lastName
                }
              />
              <Box sx={{ marginBottom: 2 }} />
              <TextField
                {...register("phoneNumber")}
                type="text"
                variant="outlined"
                color="secondary"
                label={t("phone_number")}
                fullWidth
                required
                error={errors.phone_number}
                helperText={t(errors.phone_number?.message)}
                defaultValue={
                  accessLevels.find((level) => level.role === "PATIENT")
                    .phoneNumber
                }
              />
              <Box sx={{ marginBottom: 2 }} />
              <TextField
                {...register("pesel")}
                type="text"
                variant="outlined"
                color="secondary"
                label={t("Pesel")}
                fullWidth
                required
                error={errors.pesel}
                helperText={t(errors.pesel?.message)}
                defaultValue={
                  accessLevels.find((level) => level.role === "PATIENT").pesel
                }
              />
              <Box sx={{ marginBottom: 2 }} />
              <TextField
                {...register("nip")}
                type="text"
                variant="outlined"
                color="secondary"
                label={t("NIP")}
                fullWidth
                required
                error={errors.nip}
                helperText={t(errors.pesel?.nip)}
                defaultValue={
                  accessLevels.find((level) => level.role === "PATIENT").nip
                }
              />

              <Box sx={{ marginBottom: 4 }} />

              {loading ? (
                <CircularProgress
                  style={{ marginRight: "auto", marginLeft: "auto" }}
                />
              ) : (
                <Button
                  fullWidth
                  onClick={handleSubmit(proceedPatient)}
                  type="submit"
                  variant="contained"
                >
                  {t("edit_patient_data")}
                </Button>
              )}
            </form>
          </Box>
        )}
        <ConfirmationDialog
          open={dialogOpen}
          title={t("confirm_edit_account_data")}
          actions={[
            {
              label: t("proceed"),
              handler: handleEditPatient,
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

export default EditSingleAccount;
