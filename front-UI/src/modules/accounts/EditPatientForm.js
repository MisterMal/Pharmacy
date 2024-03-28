import React from "react";
import { Box, TextField } from "@mui/material";
import { Button } from "@mui/material";

export function EditPatientForm({
  handleSubmit,
  pesel,
  setPesel,
  firstName,
  setFirstName,
  lastName,
  setLastName,
  phoneNumber,
  setPhoneNumber,
  nip,
  setNip,
}) {
  return (
    <form className="form-container">
      <TextField
        label="Pesel"
        defaultValue={pesel}
        onChange={(e) => {
          setPesel(e.target.value);
        }}
      />
      <TextField
        label="First Name"
        defaultValue={firstName}
        onChange={(e) => {
          setFirstName(e.target.value);
        }}
      />
      <TextField
        label="Last Name"
        defaultValue={lastName}
        onChange={(e) => {
          setLastName(e.target.value);
        }}
      />
      <TextField
        label="Phone Number"
        defaultValue={phoneNumber}
        onChange={(e) => {
          setPhoneNumber(e.target.value);
        }}
      />
      <TextField
        label="Nip"
        defaultValue={nip}
        onChange={(e) => {
          setNip(e.target.value);
        }}
      />
      <Button
        sx={{ width: "40" }}
        variant="contained"
        onClick={handleSubmit}
        className="search-btn"
      >
        Submit
      </Button>
    </form>
  );
}
