import React from 'react';
import {NavLink} from "react-router-dom"
import img from "../../assets/icon/about.svg";

const Common = ({
    name,
    imgsrc,
    isCompName,
    compName,
    visit,
    btnname
}) => {
    return (
        <>
            <section id="header" className="d-flex align-items-center">
            <div className="container-fluid">
                <div className="row">
                    <div className="col-10 mx-auto">
                        {/*<img src={mahasiswa} className="mahasiswa" width="120px" height="120px"/>*/}
                        <div className="row card card-body shadow">
                            <div className="col-md-6 pt-5 pt-lg-0 order-2 order-lg-1 d-flex justify-content-center flex-column">
                                <h3 className="my-2">Welcome, </h3>
                                <h1>
                                    <strong className="brand-name">{name}</strong>
                                     &nbsp;{isCompName ? compName : ""}
                                </h1>
                                <h2 className="my-3">
                                    Mengelola dokumen penelitian mahasiswa semester akhir dan
                                    memudahkan dalam memahami alur pelaksanaan penelitian di jurusan teknologi informasi
                                </h2>
                                <div className="mt-3">
                                    <NavLink to={visit} className="btn-get-started ">
                                        {btnname} <i className="fa fa-sign-in-alt"/>
                                    </NavLink>
                                </div>
                            </div>

                            <div className="col-lg-6 order-1 order-lg-2 header-image  app-name">
                                <img src={img} className="img-fluid animated" alt="SMDPA"/>
                                {/*<img src={mahasiswa} className="img-fluid animated" alt="SMDPA"/>*/}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </section>
        </>
    )
}

export default Common;
