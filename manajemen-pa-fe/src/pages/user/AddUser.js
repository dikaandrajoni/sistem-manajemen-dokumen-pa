import React, {Component} from "react";
import {Link, Redirect} from "react-router-dom";
import Header from "../../elements/header";
import userService from "../../services/UserService";
import MenuSidebar from "../../elements/menu";

export default class AddUser extends Component {
    constructor(props) {
        super(props);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangeName = this.onChangeName.bind(this);
        this.onChangeEmail = this.onChangeEmail.bind(this);
        this.onChangeRole = this.onChangeRole.bind(this);
        this.onChangePosisi = this.onChangePosisi.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.onChangePasswordKonfirmasi = this.onChangePasswordKonfirmasi.bind(this);
        this.saveUser = this.saveUser.bind(this);
        this.newUser = this.newUser.bind(this);

        this.state = {
            id: null,
            username: "",
            name:"",
            email: "",
            posisi: "",
            role: "",
            password:"",
            passwordKonfirmasi: "",
            submitted: false
        };
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    onChangeEmail(e) {
        this.setState({
            email: e.target.value
        });
    }

    onChangeRole(e) {
        this.setState({
            role: e.target.value
        });
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

    onChangeName(e) {
        this.setState({
            name: e.target.value
        });
    }

    onChangePosisi(e) {
        this.setState({
            posisi: e.target.value
        });
    }


    saveUser() {
        var data = {
            username: this.state.username,
            name: this.state.name,
            email: this.state.email,
            posisi: this.state.posisi,
            role: this.state.role,
            password: this.state.password,
        };

        userService.create(data)
            .then(response => {
                this.setState({
                    id: response.data.id,
                    username: response.data.username,
                    name: response.data.name,
                    email: response.data.email,
                    posisi: response.data.posisi,
                    role: this.state.role,
                    submitted: true
                });
            })
            .finally(this.renderRedirect)
            .catch(e => {
                console.log(e);
            });

    }

    newUser() {
        this.setState({
            id: null,
            username: "",
            email: "",
            posisi: "",
            name:"",
            password:"",
            role:"",
            passwordKonfirmasi: null,
            submitted: false
        });
    }
    renderRedirect = () => {
        if (this.state.redirect) {
            return <Redirect to='/users' />
        }
    };

    render() {
        const isLoading = this.state.isLoading;
        if (this.state.toDashboard === true) {
            return <Redirect to='/' />
        }
        return (
            <div>
                <Header/>
                <div id="wrapper">
                    <MenuSidebar/>
                    <div id="content-wrapper">
                        <div className="container-fluid">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={'/users'} >List User</Link>
                                </li>
                                <li className="breadcrumb-item active">Add User</li>
                            </ol>
                        </div>
                        <div className="container-fluid">
                            <div className="card mx-auto">
                                <div className="card-header">Tambah User</div>
                                <div className="card-body">
                                    <form onSubmit={this.saveUser}>
                                        <div className="submit-form">
                                            {this.state.submitted ? (
                                                <div>
                                                    <h4>You submitted successfully!</h4>
                                                    <button className="btn btn-success" onClick={this.newUser}>
                                                        Add
                                                    </button>
                                                </div>
                                            ) : (
                                                <div>
                                                    <div className="form-group">
                                                        <label htmlFor="username">Username</label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            id="username"
                                                            required
                                                            value={this.state.username}
                                                            onChange={this.onChangeUsername}
                                                            name="username"
                                                        />
                                                    </div>

                                                    <div className="form-group">
                                                        <label htmlFor="name">Nama</label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            id="name"
                                                            required
                                                            value={this.state.name}
                                                            onChange={this.onChangeName}
                                                            name="name"
                                                        />
                                                    </div>

                                                    <div className="form-group">
                                                        <label htmlFor="email">Email</label>
                                                        <input
                                                            type="email"
                                                            className="form-control"
                                                            id="email"
                                                            required
                                                            value={this.state.email}
                                                            onChange={this.onChangeEmail}
                                                            name="email"
                                                        />
                                                    </div>
                                                    <div className="form-group">
                                                        <label htmlFor="role">Role</label>
                                                        <select name="role" onChange={this.onChangeRole}
                                                                className="form-control" value={this.state.role} required>
                                                            <option></option>
                                                            <option value="pembimbing">Pembimbing</option>
                                                            <option value="tim kbk">Tim KBK</option>
                                                            <option value="mahasiswa">Mahasiswa</option>
                                                            <option value="admin">Admin</option>
                                                            <option value="kaprodi">Kaprodi</option>
                                                            <option value="penguji">Penguji</option>
                                                        </select>
                                                    </div>
                                                    <div className="form-group">
                                                        <label htmlFor="posisi">Posisi</label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            id="posisi"
                                                            required
                                                            value={this.state.posisi}
                                                            onChange={this.onChangePosisi}
                                                            name="posisi"
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

                                                    <button className="btn btn-primary btn-block" type="submit" disabled={this.state.isLoading ? true : false}>Simpan &nbsp;&nbsp;&nbsp;
                                                        {isLoading ? (
                                                            <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                                                        ) : (
                                                            <span></span>
                                                        )}
                                                    </button>
                                                </div>
                                            )}
                                        </div>
                                    </form>
                                    {this.renderRedirect()}
                                </div>
                            </div>
                        </div>
                        <footer className="sticky-footer">
                            <div className="container my-auto">
                                <div className="copyright text-center my-auto">
                                    <span>Copyright © {(new Date().getFullYear())}</span>
                                </div>
                            </div>
                        </footer>
                    </div>
                </div>
            </div>
        );
    }
}
