import { useEffect, useState } from "react";
import { EditAdminForm } from "../modules/accounts/EditAdminForm";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { putAdmin } from "../api/mok/accountApi";
import { Box } from "@mui/system";
import { Paper } from "@mui/material";

export default function EditAdmin() {
  const { id } = useParams();

  const [workPhoneNumber, setWorkPhoneNumber] = useState("");

  const paperStyle = {
    padding: "30px 20px",
    width: 300,
    margin: "20px auto",
    justifyContent: "center",
  };

  const navigate = useNavigate();
  const location = useLocation();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append("id", id);
    formData.append("workPhoneNumber", workPhoneNumber);
    await putAdmin(Object.fromEntries(formData));
    navigate(`/accounts/${id}`);
  };

  return (
    <Paper elevation={20} sx={paperStyle}>
      <Box sx={{ display: "flex", flexDirection: "column", width: "100%" }}>
        <EditAdminForm
          handleSubmit={handleSubmit}
          setWorkPhoneNumber={setWorkPhoneNumber}
          licenseWorkPhoneNumber={workPhoneNumber}
        />
      </Box>
    </Paper>
  );
}
