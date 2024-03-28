import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {red, blue, green, yellow, purple, lightBlue, cyan} from '@mui/material/colors';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from "react-i18next";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import i18n from "i18next";
import LanguageIcon from '@mui/icons-material/Language';
import {Pathnames} from "../router/Pathnames";
import {languages} from "../constants/Constants";


const guestTheme = createTheme({
  palette: {
    primary: blue,
    //admin: blue,
    //pharmacist: green,
    //patient: yellow,
  },
  typography: {
    fontFamily: [
    'Lato',
    'sans-serif',
    ].join(','),
  },
});


export default function PublicNavbar() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const currentLanguage = i18n.language;
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = (event) => {
    event.stopPropagation();
    setAnchorEl(null);
  };

  return (
    <ThemeProvider theme={guestTheme}>
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography onClick={()=>navigate(Pathnames.public.home)}variant="h6" component="div" sx={{ flexGrow: 1, cursor: 'pointer' }}>
            {t('internet_pharmacy')}
          </Typography>
          <Button color="inherit" onClick={() => navigate(Pathnames.public.signup)}>
            {t('sign_up')}
          </Button>
          <Button color="inherit" onClick={() => navigate(Pathnames.public.login)}>
            Login
          </Button>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
            id="basic-button"
            aria-controls={open ? 'basic-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
            onClick={handleClick}
          >
          <LanguageIcon />
          <Menu 
            id="basic-menu"
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            MenuListProps={{
              'aria-labelledby': 'basic-button',
            }}
          >
            {languages.map(({ code, name, country_code }) => (
              <MenuItem disabled={code === currentLanguage} key={country_code} onClick={() => {
                  i18n.changeLanguage(code)
                  setAnchorEl(null);}}>
                {name}
              </MenuItem>
            ))}
          </Menu>
          </IconButton>
        </Toolbar>
      </AppBar>
    </Box>
    </ThemeProvider>
  );
}
