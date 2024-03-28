import { Box, Button, Grid, Paper, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ToastContainer } from "react-toastify";
import { getSelfAccountDetails } from "../api/mok/accountApi";
import ChangePasswordForm from "../modules/accounts/ChangePasswordForm";
import ChangeEmailForm from "../modules/accounts/ChangeEmailForm";
import { useNavigate } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { useSelector } from "react-redux";

function AccountDetails() {
  const [account, setAccount] = useState({});
  const [accessLevels, setAccessLevels] = useState([]);
  const [etag, setEtag] = useState("");
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const currentRole = user.cur;

  const paperStyle = {
    backgroundColor: "rgba(255, 255, 255, 0.75)",
    padding: "20px 20px",
    margin: "0px auto",
    width: 400,
  };

  const [loading, setLoading] = useState(true);
  const [changePass, setChangePass] = useState(false);
  const [changeEmail, setChangeEmail] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getSelfAccountDetails();
        setAccount(response.data);
        setAccessLevels(response.data.accessLevels);
        setEtag(response.headers["etag"]);
        setLoading(false);
      } catch (error) {
        navigate("/error", { replace: true });
      }
    };
    fetchData();
  }, []);

  const isSuperAdmin = currentRole === "ADMIN";
  const isSuperChemist = currentRole === "CHEMIST";
  const isSuperPatient = currentRole === "PATIENT";


  if (loading) {
    return <p>Loading...</p>;
  }

  const handleChangePassword = () => {
    setChangePass((state) => !state);
  };

  const handleChangeEmail = () => {
    setChangeEmail((state) => !state);
  };

  const handleEditAccountDetails = () => {
    navigate(`/home/self/edit`);
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
        <h2 style={{ fontFamily: "Lato" }}>{t("account_details")} </h2>
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          Email
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {account.email}
        </Typography>
        {changePass ? (
          <>
            <ChangePasswordForm
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
        {changeEmail ? (
          <>
            <ChangeEmailForm
              account={account}
              etag={etag}
              hideChange={setChangeEmail}
            />
            <Grid item xs={6}>
              <Button onClick={handleChangeEmail}>{t("back_button")}</Button>
            </Grid>
          </>
        ) : (
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Button onClick={handleChangeEmail}>
                {t("change_email_button")}
              </Button>
            </Grid>
          </Grid>
        )}
        <Typography
          style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 1, mt: 1 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          Login
        </Typography>
        <Typography
          style={{ fontSize: 16 }}
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, mb: 2 }}
          type="text"
          fullWidth
          InputProps={{
            readOnly: true,
          }}
        >
          {account.login}
        </Typography>
        {isSuperAdmin && (
          <Box>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("work_phone_number")}
            </Typography>
            {accessLevels.map((level) =>
              level.workPhoneNumber ? (
                <Typography
                  key={level.workPhoneNumber}
                  style={{ fontSize: 16 }}
                  variant="h6"
                  component="div"
                  sx={{ flexGrow: 1, mb: 2 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {level.workPhoneNumber}
                </Typography>
              ) : null
            )}
          </Box>
        )}
        {isSuperChemist && (
          <Box>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("licesne_number")}
            </Typography>
            {accessLevels.map((level) =>
              level.licenseNumber ? (
                <Typography
                  key={level.licenseNumber}
                  style={{ fontSize: 16 }}
                  variant="h6"
                  component="div"
                  sx={{ flexGrow: 1, mb: 2 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {level.licenseNumber}
                </Typography>
              ) : null
            )}
          </Box>
        )}
        {isSuperPatient && (
          <Box>
            <Grid container spacing={2}>
              <Grid item xs={6}>
                <Typography
                  style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                  component="div"
                  sx={{ flexGrow: 1, mb: 1 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {t("First name")}
                </Typography>
                {accessLevels.map((level) =>
                  level.firstName ? (
                    <Typography
                      key={level.firstName}
                      style={{ fontSize: 16 }}
                      variant="h6"
                      component="div"
                      sx={{ flexGrow: 1, mb: 2 }}
                      type="text"
                      fullWidth
                      InputProps={{
                        readOnly: true,
                      }}
                    >
                      {level.firstName}
                    </Typography>
                  ) : null
                )}
              </Grid>
              <Grid item xs={6}>
                <Typography
                  style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
                  component="div"
                  sx={{ flexGrow: 1, mb: 1 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {t("last_name")}
                </Typography>
                {accessLevels.map((level) =>
                  level.lastName ? (
                    <Typography
                      style={{ fontSize: 16 }}
                      variant="h6"
                      component="div"
                      sx={{ flexGrow: 1, mb: 2 }}
                      type="text"
                      fullWidth
                      InputProps={{
                        readOnly: true,
                      }}
                    >
                      {level.lastName}
                    </Typography>
                  ) : null
                )}
              </Grid>
            </Grid>
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              {t("phone_number")}
            </Typography>
            {accessLevels.map((level) =>
              level.phoneNumber ? (
                <Typography
                  style={{ fontSize: 16 }}
                  variant="h6"
                  component="div"
                  sx={{ flexGrow: 1, mb: 2 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {level.phoneNumber}
                </Typography>
              ) : null
            )}
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              Pesel
            </Typography>
            {accessLevels.map((level) =>
              level.pesel ? (
                <Typography
                  style={{ fontSize: 16 }}
                  variant="h6"
                  component="div"
                  sx={{ flexGrow: 1, mb: 2 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {level.pesel}
                </Typography>
              ) : null
            )}
            <Typography
              style={{ fontFamily: "Lato", fontSize: 18, fontWeight: 400 }}
              component="div"
              sx={{ flexGrow: 1, mb: 1 }}
              type="text"
              fullWidth
              InputProps={{
                readOnly: true,
              }}
            >
              NIP
            </Typography>
            {accessLevels.map((level) =>
              level.nip ? (
                <Typography
                  style={{ fontSize: 16 }}
                  variant="h6"
                  component="div"
                  sx={{ flexGrow: 1, mb: 2 }}
                  type="text"
                  fullWidth
                  InputProps={{
                    readOnly: true,
                  }}
                >
                  {level.nip}
                </Typography>
              ) : null
            )}
          </Box>
        )}
        <Button onClick={() => handleEditAccountDetails()}>
          {t("edit_account_details")}
        </Button>
      </Paper>
      <ToastContainer />
    </div>
  );
}

export default AccountDetails;
