import React, {Component} from "react";
import {Link, Redirect} from "react-router-dom";
import Header from "../../elements/header";
import userService from "../../services/UserService";
import Swal from "sweetalert2";
import {Button} from "reactstrap";
import MenuSidebar from "../../elements/menu";

export default class UbahPassword extends Component {
    constructor(props) {
        super(props);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.onChangePasswordKonfirmasi = this.onChangePasswordKonfirmasi.bind(this);
        this.updatePassword = this.updatePassword.bind(this);

        this.state = {
            username: "",
            password:"",
            passwordKonfirmasi: "",
            isLoading: false,
            retryLogin:false
        };
    }

    componentDidMount() {
        const user = window.localStorage.getItem("username");
        this.setState({username: user})
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    onChangePasswordKonfirmasi(e) {
        this.setState({
            passwordKonfirmasi: e.target.value
        });
    }

    updatePassword() {
        this.setState({isLoading: true})
        var data = {
            username: this.state.username,
            password: this.state.password,
        };

        userService.ubahPassword(data)
            .then(response => {
                if (response.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Password berhasil diubah, Silahkan login ulang!",
                        'success'
                    );

                    window.localStorage.removeItem('token');
                    window.localStorage.removeItem('user');
                    window.localStorage.removeItem('email');
                    window.localStorage.removeItem('username');
                    window.localStorage.removeItem('role');
                    window.localStorage.setItem('isLoggedIn', "false");
                    this.setState({ retryLogin: true });
                }

            })
            .finally(() => this.setState({isLoading: false}))
            .catch(e => {
                console.log(e);
            });
    }

    render() {
        const isLoading = this.state.isLoading;
        if (this.state.retryLogin === true) {
            return <Redirect to='/' />
        }
        return (
            <div>
                <Header/>
                <div id="wrapper">
                    <MenuSidebar/>
                    <div id="content-wrapper">
                        <div className="container-fluid">
                            <div className="card mx-auto">
                                <div className="card-header">Ubah Password</div>
                                <div className="card-body">
                                    <form>
                                        <div className="submit-form">
                                            <div>
                                                <div className="form-group">
                                                    <label htmlFor="username">username</label>
                                                    <input
                                                        type="text"
                                                        className="form-control"
                                                        id="username"
                                                        required
                                                        value={this.state.username}
                                                        onChange={this.onChangeUsername}
                                                        name="username"
                                                        disabled={true}
                                                    />
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="password">Password</label>
                                                    <input
                                                        type="password"
                                                        className="form-control"
                                                        id="password"
                                                        required
                                                        value={this.state.password}
                                                        onChange={this.onChangePassword}
                                                        name="password"
                                                    />
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
                                                {/*<button className="btn btn-primary btn-block" type="submit" disabled={this.state.password ==="" ? true : false}>Simpan &nbsp;&nbsp;&nbsp;*/}
                                                {/*    {isLoading ? (*/}
                                                {/*        <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>*/}
                                                {/*    ) : (*/}
                                                {/*        <span></span>*/}
                                                {/*    )}*/}
                                                {/*</button>*/}
                                                {/*<span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"/>*/}
                                                {
                                                    this.state.password === "" || this.state.password !== this.state.passwordKonfirmasi?
                                                        (<Button className="btn-primary" disabled={true}>Simpan</Button>):
                                                        <Link to={'/'} onClick={this.updatePassword} className="btn btn-primary">
                                                            {isLoading ? (
                                                                <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"/>
                                                            ) : (
                                                                <span></span>
                                                            )}
                                                            Simpan</Link>
                                                }

                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <footer className="sticky-footer">
                            <div className="container my-auto">
                                <div className="copyright text-center my-auto">
                                    <span>Copyright © SMDPA {(new Date().getFullYear())}</span>
                                </div>
                            </div>
                        </footer>
                    </div>
                </div>
            </div>
        );
    }
}
