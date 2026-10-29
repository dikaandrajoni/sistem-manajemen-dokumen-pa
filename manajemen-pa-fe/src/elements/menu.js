import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

function MenuSidebar () {

    const [role, setRole] = useState(null);
    const [posisi, setPosisi] = useState(null);

    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
        setPosisi(window.localStorage.getItem("posisi"));
    }, []);

    return (
        <div id="wrapper">
            <ul className="sidebar navbar-nav">
                <h5 className="text-white" style={{margin:"20px"}}>
                    <strong>Menu</strong>
                </h5>
                <div className="dropdown-divider"/>
                <li className="nav-item active">
                    <Link to={'/dashboard'} className="nav-link"><i className="fas fa-fw fa-home"/>
                        <span>&nbsp;Dashboard</span></Link>
                </li>
                {/*<li className="nav-item">*/}
                {/*    <Link to={'/index'} className="nav-link"><i className="fas fa-fw fa-chart-area"></i>*/}
                {/*        <span>&nbsp;CRUD App</span></Link>*/}
                {/*</li>*/}
                {
                    (role === "mahasiswa" || role === "admin") &&
                    <li className="nav-item">
                        <Link to={'/pengajuan-proyek-akhir'} className="nav-link"><i className="fas fa-fw fa-file-alt"/>
                            <span>&nbsp;Pengajuan Penelitian</span></Link>
                    </li>
                }

                {
                    role !== "mahasiswa"?
                        <li className="nav-item">
                            <Link to={'/review-proyek-akhir'} className="nav-link"><i className="fas fa-fw fa-file"/>
                                <span>&nbsp;Review Penelitian</span></Link>
                        </li> : <></>
                }
                {
                    role !== "mahasiswa" && <>
                        <li className="nav-item">
                            <Link to={'/hasil-review'} className="nav-link"><i className="fa fa-book-open"></i>
                                <span>&nbsp;Hasil Review</span></Link>
                        </li>
                    </>
                }

                {/*<li className="nav-item">*/}
                {/*    <Link to={'/revisi'} className="nav-link"><i className="fas fa-fw fa-file-alt"></i>*/}
                {/*        <span>&nbsp;Revisi</span></Link>*/}
                {/*</li>*/}

                {/*<li className="nav-item">*/}
                {/*    <Link to={'/fileupload'} className="nav-link"><i className="fas fa-fw fa-file-archive"></i>*/}
                {/*        <span>&nbsp;File Upload</span></Link>*/}
                {/*</li>*/}
                <li className="nav-item dropdown">
                    <Link className="nav-link dropdown-toggle" to={''}  id="pagesDropdown" role="button"
                          data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i className="fas fa-fw fa-folder"/>
                        <span>&nbsp;Master Data</span>
                    </Link>
                    <div className="dropdown-menu" aria-labelledby="pagesDropdown">
                        { role === "mahasiswa"?<></>:
                            <>
                                <Link to={'/mahasiswa'} className="dropdown-item"><i className="fas fa-fw fa-user-friends"/>
                                    <span>&nbsp;Data Mahasiswa</span></Link>
                                <div className="dropdown-divider"/>
                            </>
                        }

                        <Link to={'/dosen'} className="dropdown-item"><i className="fas fa-fw fa-user-circle"/>
                            <span>&nbsp;Data Dosen</span></Link>
                        <div className="dropdown-divider"/>
                        <Link to={'/program-studi'} className="dropdown-item"><i className="fas fa-fw fa-folder"/>
                            <span>&nbsp;Program Studi</span></Link>
                        <div className="dropdown-divider"/>
                        {/*<Link to={'/signature'} className="dropdown-item"><i className="fas fa-fw fa-signature"></i>*/}
                        {/*    <span>&nbsp;Draw Signature</span></Link>*/}
                        {/*<div className="dropdown-divider"></div>*/}
                        { role === "admin"? <>
                            <Link to={'/users'} className="dropdown-item"><i className="fa fa-users"/>
                                <span>&nbsp;Data Users</span></Link>
                            <div className="dropdown-divider"/>
                            <Link to={'/roles'} className="dropdown-item"><i className="fa fa-user-secret"/>
                                <span>&nbsp;Data Role</span></Link>
                        </>:<></>
                        }

                    </div>
                </li>
                {/*<li className="nav-item">*/}
                {/*    <a className="nav-link" href="tables.html">*/}
                {/*        <i className="fas fa-fw fa-question"></i>*/}
                {/*        <span>&nbsp;Not Found</span></a>*/}
                {/*</li>*/}
            </ul>
        </div>
    );
}


export default MenuSidebar;
