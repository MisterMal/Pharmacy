import { Stack, TextField, Paper, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { toast, ToastContainer } from "react-toastify";
import { getMedicationById } from "../api/moa/medicationApi";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { LinearProgress } from "@mui/material";
import { useParams } from "react-router-dom";
import { CenterFocusStrongSharp } from "@mui/icons-material";

function MedicationDetails() {
  const [medication, setMedication] = useState({});
  const [etag, setEtag] = useState("");
  const { id } = useParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const currentRole = user.cur;
  console.log(user);

  const paperStyle = {
    backgroundColor: "rgba(255, 255, 255, 0.75)",
    padding: "20px 20px",
    margin: "0px auto",
    width: 400,
  };

  const [loading, setLoading] = useState(true);
  useEffect(() => {
    getMedicationById(id)
      .then((response) => {
        setMedication(response.data);
        setEtag(response.headers["etag"]);
        setLoading(false);
        console.log(response.data);
      })
      .catch((error) => {
        toast.error(t(error.response.data.message), { position: "top-center" });
      });
  }, []);

  if (loading) {
    return (
      <div>
        <LinearProgress />
      </div>
    );
  }

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
        <h2 style={{ fontFamily: "Lato" }}>{medication.name} </h2>

        <Stack
          direction={{ xs: "column", sm: "row" }}
          spacing={{ xs: 1, sm: 2, md: 4 }}
        >
          <Stack>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 1, mt: 1 }}
              type="text"
              InputProps={{
                readOnly: true,
              }}
            >
              {t("price")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              InputProps={{
                readOnly: true,
              }}
            >
              {medication.currentPrice}
            </Typography>
          </Stack>
          <Stack>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 1, mt: 1 }}
              type="text"
              InputProps={{
                readOnly: true,
              }}
            >
              {t("stock")}
            </Typography>
            <Typography
              style={{ fontSize: 16 }}
              variant="h6"
              component="div"
              sx={{ flexGrow: 1, mb: 2 }}
              type="text"
              InputProps={{
                readOnly: true,
              }}
            >
              {medication.stock}
            </Typography>
          </Stack>
        </Stack>

        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1, mt: 1 }}
          type="text"
          InputProps={{
            readOnly: true,
          }}
        >
          {t("category_name")}
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          InputProps={{
            readOnly: true,
          }}
        >
          {medication.categoryDTO.name}
        </Typography>
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1, mt: 1 }}
          type="text"
          InputProps={{
            readOnly: true,
          }}
        >
          {t("on_prescription")}
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          InputProps={{
            readOnly: true,
          }}
        >
          {medication.categoryDTO.isOnPrescription ? t("yes") : t("no")}
        </Typography>
      </Paper>
      <ToastContainer />
    </div>
  );
}

export default MedicationDetails;
