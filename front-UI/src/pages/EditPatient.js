import { useEffect, useState } from "react";
import { EditPatientForm } from "../modules/accounts/EditPatientForm";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { putPatient } from "../api/mok/accountApi";
import { Box } from "@mui/system";
import { Paper } from "@mui/material";
import { getAccount } from "../api/mok/accountApi";

export default function EditPatient() {
  const { id } = useParams();

  const [pesel, setPesel] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [nip, setNip] = useState("");

  const navigate = useNavigate();
  const location = useLocation();

  const paperStyle = {
    padding: "30px 20px",
    width: 300,
    margin: "20px auto",
    justifyContent: "center",
  };

  useEffect(() => {
    getAccount(id).then((account) => {
      setPesel(account.pesel);
      setFirstName(account.firstName);
      setLastName(account.lastName);
      setPhoneNumber(account.phoneNumber);
      setNip(account.nip);
    });
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append("id", id);
    formData.append("pesel", pesel);
    formData.append("firstName", firstName);
    formData.append("lastName", lastName);
    formData.append("phoneNumber", phoneNumber);
    formData.append("nip", nip);
    await putPatient(Object.fromEntries(formData));
    navigate(`/accounts/${id}`);
  };

  return (
    <Paper elevation={20} sx={paperStyle}>
      <Box sx={{ display: "flex", flexDirection: "column", width: "100%" }}>
        <EditPatientForm
          handleSubmit={handleSubmit}
          pesel={pesel}
          setPesel={setPesel}
          firstName={firstName}
          setFirstName={setFirstName}
          lastName={lastName}
          setLastName={setLastName}
          phoneNumber={phoneNumber}
          setPhoneNumber={setPhoneNumber}
          nip={nip}
          setNip={setNip}
        />
      </Box>
    </Paper>
  );
}
