import { useEffect, useState } from "react";
import { EditChemistForm } from "../modules/accounts/EditChemistForm";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { putChemist } from "../api/mok/accountApi";
import { Box } from "@mui/system";
import { Paper } from "@mui/material";

export default function EditChemist() {
  const { id } = useParams();

  const [licenseNumber, setLicenseNumber] = useState("");

  const navigate = useNavigate();
  const location = useLocation();

  const paperStyle = {
    padding: "30px 20px",
    width: 300,
    margin: "20px auto",
    justifyContent: "center",
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append("id", id);
    formData.append("licenseNumber", licenseNumber);
    await putChemist(Object.fromEntries(formData));
    navigate(`/accounts/${id}`);
  };

  return (
    <Paper elevation={20} sx={paperStyle}>
      <Box sx={{ display: "flex", flexDirection: "column", width: "100%" }}>
        <EditChemistForm
          handleSubmit={handleSubmit}
          setLicenseNumber={setLicenseNumber}
          licenseNumber={licenseNumber}
        />
      </Box>
    </Paper>
  );
}
