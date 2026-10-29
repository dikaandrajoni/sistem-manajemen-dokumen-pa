import React from 'react';
import Common from './Common';
import Navbar from './Navbar';
import './home.css';

const Home = () => {
    return (
        <>
            <div>
                <body className="body">
                <Navbar/>
                <Common
                    name='Sistem Manajemen Dokumen'
                    isCompName={true}
                    compName="Penelitian Mahasiswa"
                    visit='/login'
                    btnname="Login"
                />
                </body>
            </div>
        </>
    )
}

export default Home;
