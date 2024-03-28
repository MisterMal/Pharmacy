import React from "react";
import { Box, TextField } from "@mui/material";
import { Button } from "@mui/material";

export function EditAdminForm({
  handleSubmit,
  workPhoneNumber,
  setWorkPhoneNumber,
}) {
  return (
    <Box
      sx={{ display: "flex", flexDirection: "column", width: "100%" }}
      onSubmit={handleSubmit}
    >
      <form className="form-container">
        <label>License Number</label>
        <TextField
          defaultValue={workPhoneNumber}
          onChange={(e) => {
            setWorkPhoneNumber(e.target.value);
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
    </Box>
  );
}
