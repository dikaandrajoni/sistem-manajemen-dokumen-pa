import React, {Component} from 'react';
import {Link, Redirect} from 'react-router-dom';
import TitleComponent from "./title";
import loginService from "../services/UserService";
import Swal from "sweetalert2";
import logo from "../assets/logo/logo-circle.png";
import bg from "../assets/icon/bg3.jpg";
import server from "../Server";

export default class Login extends Component {

    state = {
        email: '',
        password: '',
        redirect: false,
        authError: false,
        isLoading: false,
        loggedIn: false,
        location: {},
        show:false,
    };

    handleChange = (e) =>{
        this.setState({
            [e.target.name] : e.target.value
        });
    }

    handleSubmit = event => {
        event.preventDefault();
        this.setState({isLoading: true});
        const username = this.state.email;
        const password = this.state.password;
        let user = {
            username: username,
            password: password
        }

        loginService.login(user)
            .then(result => {
                if (result.data.status === 200) {
                    const token = result.data.data.token;
                    if (token){
                        server.defaults.headers.common["Authorization"] = `Bearer ${token}`;
                    } else {
                        window.location.reload();
                    }
                    window.localStorage.setItem('isLoggedIn', "true");
                    window.localStorage.setItem('token', result.data.data.token);
                    window.localStorage.setItem('user', result.data.data.user);
                    window.localStorage.setItem('email', result.data.data.email);
                    window.localStorage.setItem('username', result.data.data.username);
                    window.localStorage.setItem('role', result.data.data.role);
                    window.localStorage.setItem('posisi', result.data.data.posisi);
                    this.setState({redirect: true, isLoading: false, loggedIn: true});
                    Swal.fire(
                        'Berhasil Login',
                        'Selamat Datang',
                        'success'
                    );
                }
            })
            .catch((res) => {
                if (res.response.data.status !== 500){
                    window.localStorage.setItem('isLoggedIn', "false");
                    Swal.fire(
                        'Warning',
                        res.response.data.message,
                        'warning'
                    )
                } else {
                    window.localStorage.setItem('isLoggedIn', "false");
                    Swal.fire(
                        'Ops..',
                        res.response.data.data,
                        'error'
                    )
                }
                this.setState({show: true})
                this.setState({authError: true, isLoading: false});
            });
    };

    renderRedirect = () => {
        if (this.state.redirect) {
            return <Redirect to='/dashboard'/>
        }
    };

    render() {
        const isLoading = this.state.isLoading;
        return (
            <div style={{height:"900px", backgroundImage: `url(${bg})`}}>
                <div className="container">
                    <TitleComponent title="Login Sistem Manajemen Dokumen "/>
                    <div align="center">
                        <img src={logo} height="180px" style={{overflow:"hidden", boxShadow:"revert"}}/>
                    </div>
                    <div className="card card-login shadow mx-auto mt-4" style={{
                        borderColor:'darkgray', // if you need
                        borderWidth:1,
                        overflow: 'hidden',
                        shadowColor: 'lightBlue',
                        shadowRadius: 10,
                        shadowOpacity: 1,}}>
                        <div className="card-header" style={{textAlign: "center"}}><b>Login</b></div>
                        <div className="card-body ">
                            <form onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <div className="form-label-group">
                                        <input className={"form-control " + (this.state.authError ? 'is-invalid' : '')} id="inputEmail"
                                               placeholder="Username" type="text" name="email" onChange={this.handleChange} autoFocus required/>
                                        <label htmlFor="inputEmail">Username</label>
                                        <div className="invalid-feedback">
                                            Silahkan cek kembali username.
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="form-label-group">
                                        <input type="password" className={"form-control " + (this.state.authError ? 'is-invalid' : '')}
                                               id="inputPassword" placeholder="******" name="password" onChange={this.handleChange} required/>
                                        <label htmlFor="inputPassword">Password</label>
                                        <div className="invalid-feedback">
                                            Silahkan cek kembali password.
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group" align="center">
                                    <button className="btn btn-primary" type="submit" style={{width:"90px", margin:"10px"}} disabled={this.state.isLoading}>Login
                                        {isLoading ? (
                                            <>&nbsp;<span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"/></>
                                        ) : (
                                            <span/>
                                        )}
                                    </button>
                                    <Link className="btn btn-info" style={{width:"90px", margin:"10px"}} to={'register'}>Register</Link>
                                </div>
                            </form>
                        </div>
                    </div>
                    {this.renderRedirect()}
                </div>
            </div>
        );
    }
}


