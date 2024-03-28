import {Paper} from "@mui/material";

function Overlay({ open, children }) {
    return (
        <div hidden={!open} style={{
            position: "fixed",
            padding: '20px 20px',
            margin: "0px auto",
            width: "80%",
            zIndex: 2}} >
            <Paper elevation={10} style={{padding: '20px 20px', margin: "0px auto", width: "80%",}}>
                {children}
            </Paper>
        </div>
    );
}

export default Overlay;