import React from 'react';
import PublicNavbar from "../components/PublicNavbar";

function PublicLayout({children}) {
    return (
        <>
            <PublicNavbar/>
            {children}
        </>
    );
}

export default PublicLayout;