import React, {Component} from 'react';
import {Link, Redirect} from 'react-router-dom';
import userService from "../services/UserService";
import Swal from "sweetalert2";
import bg from "../assets/icon/bg3.jpg";

export default class Register extends Component {

    state = {
        name: '',
        username: '',
        email: '',
        password: '',
        passwordKonfirmasi: "",
        redirect: false,
        authError: false,
        isLoading: false,
        message: ""
    };

    handleEmailChange = event => {
        this.setState({ email: event.target.value });
    };
    handleUsernameChange = event => {
        this.setState({ username: event.target.value });
    };
    handlePwdChange = event => {
        this.setState({ password: event.target.value });
    };
    handleNameChange = event => {
        this.setState({ name: event.target.value });
    };
    onChangePasswordKonfirmasi = event => {
        this.setState({
            passwordKonfirmasi: event.target.value
        });
    }

    handleSubmit = event => {
        event.preventDefault();
        this.setState({isLoading: true});
        const email = this.state.email;
        const username = this.state.username;
        const password = this.state.password;
        const name = this.state.name;
        let user = {
            email: email,
            name: name,
            username: username,
            password: password
        };

        userService.register(user)
            .then(result => {
                this.setState({isLoading: false});
                if (result.data.status === 200) {
                    Swal.fire(
                        'Success',
                        'Berhasil register akun!',
                        'success'
                    );
                    this.setState({redirect: true, authError: false});
                }
            })
            .catch(error => {
                this.setState({ authError: true, isLoading: false, message: error.toString });
                console.log(error.response)
                Swal.fire(
                    'Warning',
                    error.response.data.message,
                    'warning'
                )
            });
    };

    renderRedirect = () => {
        if (this.state.redirect) {
            return <Redirect to="/" />
        }
    };

    render() {
        const isLoading = this.state.isLoading;
        return (
            <div style={{backgroundImage: `url(${bg})`, height:"900px", width:"100%"}}>
            <div className="container" style={{paddingTop:"50px"}}>
                <div className="card card-login mx-auto">
                    <div className="card-header"><b>Register</b></div>
                    <div className="card-body">
                        <form onSubmit={this.handleSubmit}>
                            <div className="form-group">
                                <div className="form-label-group">
                                    <input type="text" id="inputName" className="form-control" placeholder="name"  name="name" onChange={this.handleNameChange} required/>
                                    <label htmlFor="inputName">Nama</label>
                                </div>
                            </div>

                            <div className="form-group">
                                <div className="form-label-group">
                                    <input id="inputEmail" className="form-control" placeholder="Email address" type="text" name="email" onChange={this.handleEmailChange} autoFocus required/>
                                    <label htmlFor="inputEmail">Email address</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="form-label-group">
                                    <input type="text" id="inputUsername" className={"form-control " + (this.state.authError ? 'is-invalid' : '')} placeholder="username"  name="username" onChange={this.handleUsernameChange} autoFocus required/>
                                    <label htmlFor="inputUsername">Username</label>
                                    {/*<div className="invalid-feedback">*/}
                                    {/*    Register gagal! Silahkan cek data kembali.*/}
                                    {/*</div>*/}
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="form-label-group">
                                    <input type="password" className="form-control" id="inputPassword" placeholder="******"  name="password" onChange={this.handlePwdChange} required/>
                                    <label htmlFor="inputPassword">Password</label>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="password">Konfirmasi Password</label>
                                <input
                                    type="password"
                                    id="passwordKonfirmasi"
                                    required
                                    className={"form-control " + (this.state.passwordKonfirmasi !== "" && this.state.password !== this.state.passwordKonfirmasi? 'is-invalid' : '')}
                                    value={this.state.passwordKonfirmasi}
                                    onChange={this.onChangePasswordKonfirmasi}
                                    name="passwordKonfirmasi"
                                />
                                <div className="invalid-feedback">
                                    Password konfirmasi tidak sama.
                                </div>
                            </div>

                            <div className="form-group">
                                <button className="btn btn-primary btn-block" type="submit" disabled={this.state.isLoading ? true : false}>Register &nbsp;&nbsp;&nbsp;
                                    {isLoading ? (
                                        <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                    ) : (
                                        <span></span>
                                    )}
                                </button>
                            </div>
                        </form>
                        <div className="text-center">
                            <Link className="d-block small mt-3" to={'/login'}>Login Your Account</Link>
                            {/*<Link className="d-block small" to={'#'}>Forgot Password?</Link>*/}
                        </div>
                    </div>
                </div>
                {this.renderRedirect()}
            </div>
            </div>
        );
    }
}


