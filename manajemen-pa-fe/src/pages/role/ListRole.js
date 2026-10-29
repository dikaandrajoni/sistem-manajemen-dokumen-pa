import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Header from "../../elements/header";
import roleService from "../../services/RoleService";
import {format} from "date-fns";
import MenuSidebar from "../../elements/menu";


export default class ListRole extends Component {
    constructor(props) {
        super(props);
        this.onChangeSearchName = this.onChangeSearchName.bind(this);
        this.retrieveRoles = this.retrieveRoles.bind(this);
        this.refreshList = this.refreshList.bind(this);

        this.state = {
            roles: [],
            page:0,
            perPage:10,
            size:0,
            total:0,
            currentUser: null,
            currentIndex: -1,
            searchName: ""
        };
    }

    componentDidMount() {
        this.retrieveRoles();
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


    retrieveRoles() {
        roleService.getAll()
            .then(response => {
                this.setState({
                    roles: response.data.data.content,
                    total: response.data.data.totalElements
                });
            })
            .catch(e => {
                console.log(e);
            });
    }

    refreshList() {
        this.retrieveRoles();
        this.setState({
            currentUser: null,
            currentIndex: -1
        });
    }
    render() {
        const handleDelete = (e) =>{
            const id = e.target.value;
            roleService.delete(id)
                .then(response => {
                    this.setState({deleted: true})
                    this.refreshList();
                })
                .catch(e => {
                    console.log(e)
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
                                    <Link to={'/roles'} >List Role</Link>
                                </li>
                                <li className="breadcrumb-item active">Overview</li>
                            </ol>
                            <div className="card mb-3">
                                <div className="card-header">
                                    <i className="fas fa-table"/>
                                    &nbsp;Data Role
                                </div>
                                <div className="card-body">
                                    <div className="input-group mb-3">
                                        <Link className="btn btn-success" to="/add-role">
                                            <span className="float-right"><i className="fas fa-plus"/> Add Role</span>
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
                                        <table className="table table-bordered" id="dataTable" width="100%"
                                               cellSpacing="0">
                                            <thead>
                                            <tr>
                                                <th>No</th>
                                                <th>Created Date</th>
                                                <th>Name</th>
                                                <th>Deskripsi</th>
                                                <th>Aksi</th>

                                            </tr>
                                            </thead>
                                            <tbody>
                                            {
                                                this.state.roles.map((data, index) =>
                                                    <tr key={data.id}>
                                                        <td>{index+1}</td>
                                                        <td>{format(new Date(data.createdDate), 'dd-MM-yyyy')}</td>
                                                        <td>{data.name}</td>
                                                        <td>{data.description}</td>
                                                        <td>
                                                            {/*<Link*/}
                                                            {/*    to={`edit-role/${data.id}`}*/}
                                                            {/*    className="btn btn-info is-small mr-1"*/}
                                                            {/*>*/}
                                                            {/*    Edit*/}
                                                            {/*</Link> &nbsp;*/}
                                                            <button className="btn btn-danger" onClick={handleDelete} value={data.id}>Delete</button>
                                                        </td>
                                                    </tr>
                                                )
                                            }
                                            </tbody>
                                        </table>
                                        <div>Total data : {this.state.total}</div>
                                        {/*<TablePagination*/}
                                        {/*    rowsPerPageOptions={[5, 10, 25]}*/}
                                        {/*    component="div"*/}
                                        {/*    count={dosen.length}*/}
                                        {/*    rowsPerPage={this.state.perPage}*/}
                                        {/*    page={page}*/}
                                        {/*    // onPageChange={handleChangePage}*/}
                                        {/*    // onRowsPerPageChange={handleChangeRowsPerPage}*/}
                                        {/*/>*/}
                                    </div>
                                </div>
                                {/*<div className="card-footer small text-muted">Updated yesterday at 11:59 PM</div>*/}
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
