import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import {useNavigate} from "react-router-dom";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import i18n from "i18next";
import LanguageIcon from "@mui/icons-material/Language";
import {
    AccountCircle,
} from "@mui/icons-material";
import {useTranslation} from "react-i18next";
import {useDispatch, useSelector} from "react-redux";
import {changeLevel} from "../redux/UserSlice";
import {useState} from "react";
import {Pathnames} from "../router/Pathnames";
import {ROLES} from "../constants/Constants";
import {useLocation} from "react-router-dom";
import Breadcrumbs from "@mui/material/Breadcrumbs";
import Link from "@mui/material/Link";
import {languages} from "../constants/Constants";
import {changeLanguage} from "../api/mok/accountApi";
import LoopIcon from '@mui/icons-material/Loop';


export default function AuthNavbar() {
    const navigate = useNavigate();
    const {t} = useTranslation();
    const userRole = useSelector((state) => state.user.cur);
    const currentLanguage = i18n.language;
    const user = useSelector((state) => state.user);
    const roles = useSelector((state) => state.user.roles);
    const dispatch = useDispatch();
    const [anchorElRole, setAnchorElRole] = React.useState(null);
    const [anchorElLanguage, setAnchorElLanguage] = React.useState(null);

    const handleClick = (event) => {
        setAnchorElRole(event.currentTarget);
    };

    const handleClose = (event) => {
        event.stopPropagation();
        setAnchorElRole(null);
    };

    const location = useLocation();

    let current = "";

    return (
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <Typography
                            onClick={() => navigate(Pathnames.auth.landing)}
                            variant="h5"
                            component="div"
                            sx={{flexGrow: 1, cursor: "pointer"}}
                        >
                            {t("internet_pharmacy")}
                        </Typography>
                        {
                            roles.length > 1 &&
                            <IconButton color="inherit" onClick={(e) => {
                                setAnchorElRole(e.currentTarget);
                            }}>
                                <LoopIcon/>
                                <Menu
                                    id="simple-menu"
                                    anchorEl={anchorElRole}
                                    open={Boolean(anchorElRole)}
                                    onClose={(e) => {
                                        e.stopPropagation();
                                        setAnchorElRole(null);
                                    }}
                                    MenuListProps={{
                                        "aria-labelledby": "basic-button",
                                    }}>
                                    {roles.map((role, index) => (
                                        <MenuItem
                                            disabled={role === userRole}
                                            key={role}
                                            onClick={(event) => {
                                                dispatch(
                                                    changeLevel({
                                                        sub: user.sub,
                                                        roles: user.roles,
                                                        index: index,
                                                        exp: user.exp,
                                                    })
                                                );
                                                handleClose(event);
                                                navigate(Pathnames.auth.landing);
                                            }}>
                                            {t(role)}
                                        </MenuItem>
                                    ))}
                                </Menu>
                            </IconButton>
                        }
                        <IconButton
                            color="inherit"
                            onClick={() => {
                                navigate(Pathnames.auth.self);
                            }}
                        >
                            <AccountCircle/>
                        </IconButton>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            // sx={{mr: 2}}
                            id="basic-button"
                            aria-controls={Boolean(anchorElRole) ? 'basic-menu' : undefined}
                            aria-haspopup="true"
                            aria-expanded={Boolean(anchorElRole) ? 'true' : undefined}
                            onClick={(e) => {
                                setAnchorElLanguage(e.currentTarget)
                            }}
                        >
                            <LanguageIcon/>
                            <Menu
                                id="basic-menu"
                                anchorEl={anchorElLanguage}
                                open={Boolean(anchorElLanguage)}
                                onClose={(e) => {
                                    e.stopPropagation();
                                    setAnchorElLanguage(null);
                                }}
                                MenuListProps={{
                                    'aria-labelledby': 'basic-button',
                                }}
                            >
                                {languages.map(({code, name, country_code}) => (
                                    <MenuItem disabled={code === currentLanguage} key={country_code} onClick={() => {
                                        changeLanguage(code)
                                        i18n.changeLanguage(code)
                                        setAnchorElLanguage(null);
                                    }}>
                                        {name}
                                    </MenuItem>
                                ))}
                            </Menu>
                        </IconButton>

                    </Toolbar>
                </AppBar>
                <Breadcrumbs sx={{margin: "10px 20px"}} separator=">" aria-label="breadcrumb">
                    {location.pathname
                        .split("/")
                        .filter((crumb) => crumb !== "")
                        .map((crumb) => {
                            if (!isNaN(crumb)) {
                                return null; // Ignore numbers in the URL
                            }
                            current += `/${crumb}`;
                            const capitalizedCrumb =
                                crumb.charAt(0).toUpperCase() + crumb.slice(1);
                            return (
                                <Link
                                    key={"crumb" + current}
                                    underline="hover"
                                    color="inherit"
                                    href={current}
                                    sx={{fontSize: "18px"}}
                                >
                                    {t(capitalizedCrumb)}
                                </Link>
                            );
                        })}
                </Breadcrumbs>
            </Box>
    );
}
