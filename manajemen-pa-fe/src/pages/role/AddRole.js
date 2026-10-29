import React, {Component} from "react";
import {Link, Redirect} from "react-router-dom";
import Header from "../../elements/header";
import roleService from "../../services/RoleService";
import Alert from "../../Alert";
import Swal from "sweetalert2";
import MenuSidebar from "../../elements/menu";

export default class AddRole extends Component {
    constructor(props) {
        super(props);
        this.onChangeName = this.onChangeName.bind(this);
        this.onChangeDeskripsi = this.onChangeDeskripsi.bind(this);
        this.saveRole = this.saveRole.bind(this);
        this.newRole = this.newRole.bind(this);

        this.state = {
            id: null,
            name:"",
            deskripsi:"",
            submitted: false,
            redirect: false,
            toList: false
        };
    }

    onChangeName(e) {
        this.setState({
            name: e.target.value
        });
    }

    onChangeDeskripsi(e) {
        this.setState({
            deskripsi: e.target.value
        });
    }

    saveRole() {
        var data = {
            name: this.state.name,
            description: this.state.deskripsi
        };

        roleService.create(data)
            .then(response => {
                if (response.status === 200){
                    Swal.fire(
                        'Success',
                        'Role berhasil dibuat!',
                        'success'
                    );
                }
                this.setState({
                //     id: response.data.id,
                //     name: response.data.name,
                //     deskripsi: response.data.deskripsi,
                //     submitted: true,
                    redirect: true
                });
            })
            .catch(e => {
                Swal.fire(
                    'Ops..',
                    e.response.data.data,
                    'error'
                )
                console.log(e);
            });

    }

    newRole() {
        this.setState({
            id: null,
            name:"",
            deskripsi:"",
            submitted: false
        });
    }
    renderRedirect = () => {
        if (this.state.redirect) {
            return <Redirect to='/roles' />
        }
    };

    render() {
        const isLoading = this.state.isLoading;
        return (
            <div>
                <Header/>
                <div id="wrapper">
                    <MenuSidebar/>
                    <div id="content-wrapper">
                        <div className="container-fluid">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Link to={'/roles'} >List Role</Link>
                                </li>
                                <li className="breadcrumb-item active">Add Role</li>
                            </ol>
                        </div>
                        <div className="container-fluid">
                            <div className="card mx-auto">
                                <div className="card-header">Tambah Data Role</div>
                                <div className="card-body">
                                    <Alert
                                        onConfirmOrDismiss={() => this.setState({submitted: false})}
                                        show={this.state.submitted}
                                        showCancelButton={false}
                                        text={'Berhasil tambah data role!'}
                                        title={'Success'}
                                        type={'success'}
                                    />
                                    <form onSubmit={this.saveRole}>
                                        <div className="submit-form">
                                            {this.state.submitted ? (
                                                <div>
                                                    <h4>You submitted successfully!</h4>
                                                    <button className="btn btn-success" onClick={this.newRole}>
                                                        Add
                                                    </button>
                                                </div>
                                            ) : (
                                                <div>
                                                    <div className="form-group">
                                                        <label htmlFor="name">Nama Role</label>
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
                                                        <label htmlFor="name">Deskripsi</label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            id="deskripsi"
                                                            required
                                                            value={this.state.deskripsi}
                                                            onChange={this.onChangeDeskripsi}
                                                            name="deskripsi"
                                                        />
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
