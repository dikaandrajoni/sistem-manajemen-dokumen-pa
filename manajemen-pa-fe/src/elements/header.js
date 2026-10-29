import React, {Component} from 'react';
import {Link, Redirect} from "react-router-dom";
import TitleComponent from "../pages/title";
import {setAuthToken} from "../services/AuthToken";
import logo from "../assets/logo/new-white.png";

export default class Header extends Component {

    constructor(props) {
        super(props);
        this.handleClickLogout = this.handleClickLogout.bind(this)
    }

    state = {
        logout: false,
        posisi: "User"
    };

    componentDidMount() {
        const token = window.localStorage.getItem('token');
        setAuthToken(token);
        const pos = window.localStorage.getItem('posisi');
        if (pos !== "null"){
            this.setState({posisi:pos})
        }
    }

    handleClickLogout(){
        window.localStorage.removeItem('token');
        window.localStorage.removeItem('user');
        window.localStorage.removeItem('email');
        window.localStorage.removeItem('username');
        window.localStorage.removeItem('role');
        window.localStorage.removeItem('posisi');
        window.localStorage.setItem('isLoggedIn', "false");
        this.setState({ logout: true });
    }

    render() {
        const isLoggedIn = window.localStorage.getItem("isLoggedIn");

        if (this.state.logout === true || isLoggedIn === "false" || isLoggedIn === null) {
            return <Redirect to='/' />
        }

        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark static-top">
                <TitleComponent title="Sistem Manajemen Dokumen Penelitian Mahasiswa"></TitleComponent>
                {/*<Link to={'/'} className="navbar-brand mr-1">*/}
                {/*    <img src="../assets/icon/cropped-logo-ti-Baru-32x32.png" alt=" Manajemen Dokumen Proyek Akhir"/>*/}
                {/*    </Link>*/}

                <button className="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle">
                    {/*<i className="fas fa-bars"></i>*/}
                </button>
                <div className="navbar-brand mr-2">
                    {/*<Link to={'/dashboard'}>*/}
                        <img src={logo} alt="" height="70"/>
                    {/*</Link>*/}
                </div>
                {/*<div className="navbar nav-item text-light text-white">*/}
                {/*    <center><b>Sistem Manajemen<br/>Dokumen Proyek Akhir</b></center>*/}
                {/*</div>*/}

                <form className="d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">
                    {/*<div className="input-group">*/}
                    {/*    <input type="text" className="form-control" placeholder="Search for..." aria-label="Search"*/}
                    {/*           aria-describedby="basic-addon2" />*/}
                    {/*        <div className="input-group-append">*/}
                    {/*            <button className="btn btn-primary" type="button">*/}
                    {/*                <i className="fas fa-search"></i>*/}
                    {/*            </button>*/}
                    {/*        </div>*/}
                    {/*</div>*/}
                </form>
                <span style={{fontSize: "18px", color: "white"}}>Login as &nbsp;{this.state.posisi}</span>
                <ul className="navbar-nav ml-auto ml-md-0">
                    <li className="nav-item dropdown no-arrow">
                        <Link to={'#'} className="nav-link dropdown-toggle" id="userDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i className="fas fa-user-circle fa-fw"></i>
                        </Link>
                        <div className="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                            {/*<Link to={'#'} className="dropdown-item">Activity Log</Link>*/}
                            <div className="dropdown-item">Hai, {window.localStorage.getItem('user')}</div>
                            <br/>
                            <Link to={'/ubah-password'} className="dropdown-item">Ubah Password</Link>
                            <div className="dropdown-divider"></div>
                            <Link to={'/'} onClick={this.handleClickLogout} className="dropdown-item" data-toggle="modal" data-target="#logoutModal">Logout</Link>
                        </div>
                    </li>
                </ul>
            </nav>
        );
    }
}
