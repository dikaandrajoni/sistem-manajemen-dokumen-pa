import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Header from "../../elements/header";
import userService from "../../services/UserService";
import {format} from "date-fns";
import Swal from "sweetalert2";
import {Button, Table} from "reactstrap";
import MenuSidebar from "../../elements/menu";
import {PaginatedList} from "react-paginated-list";

export default class ListUser extends Component {
    constructor(props) {
        super(props);
        this.onChangeSearchName = this.onChangeSearchName.bind(this);
        this.retrieveUser = this.retrieveUser.bind(this);
        this.refreshList = this.refreshList.bind(this);

        this.state = {
            users: [],
            page:0,
            perPage:10,
            size:0,
            total:0,
            currentUser: null,
            currentIndex: -1,
            searchName: "",
            role:null
        };
    }

    componentDidMount() {
        this.retrieveUser();
        const role = window.localStorage.getItem("role");
        this.setState({role: role});
    }

    onChangeSearchName(e) {
        const searchName = e.target.value;

        this.setState({
            searchName: searchName
        });
    }

    searchName() {
        this.setState({
            currentTutorial: null,
            currentIndex: -1
        });
    }

    retrieveUser() {
        userService.getAll()
            .then(response => {
                this.setState({
                    users: response.data.data.content,
                    total: response.data.data.totalElements
                });
            })
            .catch(e => {
                console.log(e);
            });
    }

    refreshList() {
        this.retrieveUser();
        this.setState({
            currentUser: null,
            currentIndex: -1
        });
    }
    render() {
        const handleDelete = (id) =>{
            userService.delete(id)
                .then(res => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Data berhasil dihapus!",
                            'success'
                        ).finally(() =>  window.location.reload());
                    }
                })
                .catch(res => {
                    if (res.response.data.status !== 500){
                        Swal.fire(
                            'Warning',
                            res.response.data.message,
                            'warning'
                        )
                    } else {
                        Swal.fire(
                            'Ops..',
                            res.response.data.data,
                            'error'
                        )
                    }
                });
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
                                <li className="breadcrumb-item active">Overview</li>
                            </ol>
                            <div className="card mb-3">
                                <div className="card-header">
                                    <i className="fas fa-table"/>
                                    &nbsp;Data User
                                </div>
                                <div className="card-body">
                                    <div className="input-group mb-3">
                                        <Link className="btn btn-success" to="/add-user">
                                            <span className="float-right"><i className="fas fa-plus"/> Tambah User</span>
                                        </Link>
                                        <form className="d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0" >
                                            <div className="input-group">
                                                <input type="text" className="form-control" onChange={this.onChangeSearchName} placeholder="Cari..."
                                                       aria-label="Search"
                                                       aria-describedby="basic-addon2" />
                                            </div>
                                        </form>
                                    </div>

                                    <div className="table-responsive">
                                        <PaginatedList
                                            list={this.state.users}
                                            itemsPerPage={10}
                                            renderList={(list) => (
                                                <>
                                                    <Table responsive hover>
                                                        <thead>
                                                        <tr>
                                                            <th>No</th>
                                                            <th>Tanggal Dibuat</th>
                                                            <th>Username</th>
                                                            <th>Name</th>
                                                            <th>Email</th>
                                                            <th>Role</th>
                                                            <th>Posisi</th>
                                                            {this.state.role === 'admin' &&
                                                                <th>Aksi</th>
                                                            }
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        {
                                                            list.length === 0? <>
                                                                    <tr>
                                                                        <td colSpan={8}>
                                                                            <center>Tidak ada data.</center>
                                                                        </td>
                                                                    </tr>
                                                                </>
                                                                :
                                                            list.filter((data) => {
                                                                if (this.state.searchName !== "" ){
                                                                    if (data.name.toLowerCase().includes(this.state.searchName.toLowerCase())) {
                                                                        return data;
                                                                    } else {
                                                                        return null;
                                                                    }
                                                                } else {
                                                                    return data;
                                                                }
                                                            }).map((data, index) =>
                                                                <tr key={data.id}>
                                                                    <td>{index+1}</td>
                                                                    <td>{format(new Date(data.createdDate), 'dd-MM-yyyy')}</td>
                                                                    <td>{data.username}</td>
                                                                    <td>{data.name}</td>
                                                                    <td>{data.email}</td>
                                                                    <td>{data.role}</td>
                                                                    <td>{data.posisi}</td>
                                                                    { this.state.role === 'admin' &&
                                                                        <td>
                                                                            <Link
                                                                                to={`edit-user/${data.id}`}
                                                                                className="btn btn-warning is-small mr-1"
                                                                            >
                                                                                <i className="fa fa-edit" style={{color: "white"}}/>
                                                                            </Link> &nbsp;
                                                                            <Button className="btn btn-danger" onClick={() => handleDelete(data.id)} value={data.id}><i className="fa fa-trash"/></Button>
                                                                        </td>
                                                                    }
                                                                </tr>
                                                            )
                                                        }
                                                        </tbody>
                                                    </Table>
                                                </>
                                            )}
                                        />
                                    </div>
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
