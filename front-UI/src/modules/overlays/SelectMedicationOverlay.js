import Overlay from "../../components/Overlay";
import {Button, Grid, TextField} from "@mui/material";
import {Close} from "@mui/icons-material";
import {useTranslation} from "react-i18next";
import React, {useCallback, useEffect, useState} from "react";

function SelectMedicationOverlay({ open, onClose, medications, append }) {

    const {t} = useTranslation();

    const [filteredMedications, setFilteredMedications] = useState([]);
    const [maxItems, setMaxItems] = useState(2)
    const [phrase, setPhrase] = useState('')

    const filter = useCallback(() => {
        if(Array.isArray(medications)) {
            if(phrase == '') {
                setFilteredMedications(medications.filter(medication =>
                    !medication.chosen).slice(0, maxItems));
            } else {
                setFilteredMedications(medications.filter(medication =>
                    medication.name.toLowerCase().startsWith(phrase.toLowerCase())
                    && !medication.chosen).slice(0, maxItems))
            }
        }
    }, [phrase, medications, open, maxItems]);


    useEffect(() => {
        filter()
    }, [phrase, medications, open, maxItems])

    const select = function(medication) {
        const found = medications.find(med => med.name === medication.name);
        found.chosen = true;
        append({id: medication.id, name: medication.name,
            price: Number(medication.currentPrice).toFixed(2), quantity: ''});
        onClose();
    }

    return (
        <Overlay open={open}>
            <Grid container spacing={1} sx={{mb: 2}}>
                <Grid container spacing={1}>
                    <Grid item xs={10}>
                        <h2>{t("select_medication")}</h2>
                    </Grid>
                    <Grid item xs={2}>
                        <Button onClick={onClose}><Close/>{t("close")}</Button>
                    </Grid>
                </Grid>
                <Grid item xs={12}>
                    <TextField type="text" variant='outlined'
                               color='secondary' label={t("medication_name")}
                               fullWidth onChange={e => setPhrase(e.target.value)}/>
                </Grid>
                {filteredMedications.map((med, i) => (
                    <Grid item xs={12} key={"fm" + i}>
                        <Button fullWidth variant='outlined'
                                onClick={() => select(med)}>{med.name}</Button>
                    </Grid>
                ))}
                <Grid item xs={12}>
                    <Button fullWidth color="info" variant='outlined'
                            onClick={() => setMaxItems(maxItems + 2)}>
                        {t("show_more")}</Button>
                </Grid>
            </Grid>
        </Overlay>
    );
}

export default SelectMedicationOverlay;