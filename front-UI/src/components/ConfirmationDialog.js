import {Dialog, DialogActions, DialogContent, DialogTitle} from "@mui/material";
import Button from '@mui/material/Button';


const ConfirmationDialog = ({open, title, actions, onClose}) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{title}</DialogTitle>
            <DialogActions>
                {actions.map((action) => (
                    <Button color={action.color}
                            key={"confirm-" + action.label}
                            onClick={action.handler}>{action.label}</Button>
                ))}
            </DialogActions>
        </Dialog>
    );
};

export default ConfirmationDialog;