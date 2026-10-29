import React, {Component} from 'react';
import Header from "../elements/header";
import {Link} from 'react-router-dom';
import pengajuanPAService from "../services/PengajuanPAService";
import {Table} from "reactstrap";
import Swal from "sweetalert2";
import ModalDetailProyekAkhir from "./pengajuanproyekakhir/ModalDetailProyekAkhir";
import MenuSidebar from "../elements/menu";
import {PaginatedList} from "react-paginated-list";

export default class Dashboard extends Component {

    constructor(props) {
        super(props);
        this.retrieveDashboard = this.retrieveDashboard.bind(this);

        this.state = {
            todo: [],
            onProgress:0,
            terimaKaprodi:0,
            terimaKbk:0,
            tolakKaprodi:0,
            tolakKbk:0,
            bimbingan:0,
            approved:0,
            revisi:0,
            page:0,
            perPage:10,
            size:0,
            total:0,
            currentUser: null,
            currentIndex: -1,
            load:true,
            searchName: ""
        };
    }
    retrieveDashboard() {
        pengajuanPAService.getDashboard()
            .then(response => {
                this.setState({
                    todo: response.data.data.content,
                    total: response.data.data.totalElements
                });
            }).finally(() => this.setState({load:false}))
            .catch(e => {
                console.log(e);
            });

        pengajuanPAService.getDashboardCount().then(res => {
            if (res.data.status === 200){
                this.setState({
                    terimaKaprodi: res.data.data.terimaKaprodi,
                    terimaKbk: res.data.data.terimaKbk,
                    tolakKaprodi: res.data.data.tolakKaprodi,
                    tolakKbk: res.data.data.tolakKbk
                })
            }
        }).catch((e) => {
            Swal.fire(
                'Ops..',
                e.response.data.message,
                'error'
            )
        })
    }

    componentDidMount() {
        this.retrieveDashboard();
    }

    render() {
        const generateColor = (status) => {
            let riskColor;
            switch (true) {
                case status==="ENDED - approved":
                    riskColor = '#00CD63';
                    break;
                case status==="Revisi Proposal" || status==="Revisi Laporan":
                    riskColor = '#F24343';
                    break;
                default:
                    const randomColor = Math.floor(Math.random() * 16777215)
                        .toString(16)
                        .padStart(6, '0');
                    riskColor = `#${randomColor}`;
                    break;
            }
            return (riskColor);
        }

        const getStatus = (tasks) => {
            if (Object.keys(tasks)[0] !== ""){
                if (Object.keys(tasks)[0].toString().toLowerCase() ===  "ended"){
                    return (tasks[Object.keys(tasks)[0]]);
                } else {
                    return (tasks[Object.keys(tasks)[0]].label);
                }
            }
        }

        const header = () => {
            return <>
                <thead>
                <tr>
                    <th>No</th>
                    <th>Name</th>
                    <th>No BP</th>
                    <th>Email</th>
                    <th>Judul</th>
                    <th>Program Studi</th>
                    <th>Bidang</th>
                    <th>Review Kaprodi</th>
                    <th>Review Tim KBK</th>
                    <th>Status</th>
                    <th>Aksi</th>
                </tr>
                </thead>
                </>
        }

        function getColor(status) {
            if (status === "diterima"){
                return "green";
            } else {
                return "red";
            }
        }

        const body = (data) => {
            return <>
                    <tbody>
                    {data.length === 0 ? <>
                        <tr>
                            <td colSpan={9}>
                                <center>Tidak ada data.</center>
                            </td>
                        </tr>

                    </>:
                        data.map((item, idx) =>
                            <tr key={idx}>
                                <th scope="row">{idx+1}</th>
                                <td>{item.requestorName}</td>
                                <td>{item.requestorCode}</td>
                                <td>{item.requestorEmail}</td>
                                <td>{item.judulProyekAkhir}</td>
                                <td>{item.programStudi === null?"":item.programStudi.name}</td>
                                <td>{item.bidangTopikPA}</td><td style={{color: "white"}}>
                                {
                                    item.hasilReviewKaprodi !== null &&
                                    <div style={{backgroundColor: getColor(item.hasilReviewKaprodi), textAlign: "center", width: "max-content", padding: "3px",borderRadius: "5px", fontSize:12, fontFamily:"sans-serif"}}>
                                        {item.hasilReviewKaprodi}
                                    </div>
                                }

                            </td>
                                <td style={{color: "white"}}>
                                    {
                                        item.hasilReviewKbk !== null &&
                                        <div style={{backgroundColor: getColor(item.hasilReviewKbk), textAlign: "center", width: "max-content", padding: "3px",borderRadius: "5px", fontSize:12, fontFamily:"sans-serif"}}>
                                            {item.hasilReviewKbk}
                                        </div>
                                    }

                                </td>
                                <td style={{color: "white"}}>
                                    <div style={{backgroundColor: generateColor(getStatus(item.tasks)), textAlign: "center", width: "max-content", padding: "5px", borderRadius: "5px", fontSize:10, fontFamily:"sans-serif"}}>
                                        {
                                            getStatus(item.tasks)
                                        }
                                    </div>
                                </td>
                                <td>
                                    <div style={{width:"110px"}}>
                                        <ModalDetailProyekAkhir item={item}/>
                                        {' '}
                                        {/*<Button color="primary" onClick={() => pengajuanPAService.downloadProposal(item.id)}><i className="fas fa-file-download"/></Button>*/}

                                    </div>
                                </td>
                            </tr>
                        )
                    }
                    </tbody>
            </>
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
                                    <Link to={'/dashboard'} >Dashboard</Link>
                                </li>
                                <li className="breadcrumb-item active">Overview</li>
                            </ol>

                            <div className="row">
                                {/*<div className="col-xl-3 col-sm-6 mb-3">*/}
                                {/*    <div className="card text-white bg-warning o-hidden h-100">*/}
                                {/*        <div className="card-body">*/}
                                {/*            <div className="card-body-icon">*/}
                                {/*                <i className="fas fa-fw fa-list"/>*/}
                                {/*            </div>*/}
                                {/*            <div className="mr-5">{this.state.bimbingan} Proses Bimbingan!</div>*/}
                                {/*        </div>*/}
                                {/*        /!*<Link className="card-footer text-white clearfix small z-1" to="#">*!/*/}
                                {/*        /!*    <span className="float-left">View Details</span>*!/*/}
                                {/*        /!*    <span className="float-right"><i className="fas fa-angle-right"></i></span>*!/*/}
                                {/*        /!*</Link>*!/*/}
                                {/*    </div>*/}
                                {/*</div>*/}
                                <div className="col-xl-3 col-sm-6 mb-3">
                                    <div className="card text-white bg-success o-hidden h-100">
                                        <div className="card-body">
                                            <div className="card-body-icon">
                                                <i className="fas fa-fw fa-file"/>
                                            </div>
                                            <div className="mr-5">{this.state.terimaKaprodi} Diterima Kaprodi</div>
                                        </div>
                                        {/*<Link className="card-footer text-white clearfix small z-1" to="#">*/}
                                        {/*    <span className="float-left">View Details</span>*/}
                                        {/*    <span className="float-right"><i className="fas fa-angle-right"></i></span>*/}
                                        {/*</Link>*/}
                                    </div>
                                </div>
                                <div className="col-xl-3 col-sm-6 mb-3">
                                    <div className="card text-white bg-danger o-hidden h-100">
                                        <div className="card-body">
                                            <div className="card-body-icon">
                                                <i className="fas fa-fw fa-file"/>
                                            </div>
                                            <div className="mr-5">{this.state.tolakKaprodi} Ditolak Kaprodi</div>
                                        </div>
                                        {/*<Link className="card-footer text-white clearfix small z-1" to="#">*/}
                                        {/*    <span className="float-left">View Details</span>*/}
                                        {/*    <span className="float-right"><i className="fas fa-angle-right"></i></span>*/}
                                        {/*</Link>*/}
                                    </div>
                                </div>
                                <div className="col-xl-3 col-sm-6 mb-3">
                                    <div className="card text-white bg-primary o-hidden h-100">
                                        <div className="card-body">
                                            <div className="card-body-icon">
                                                <i className="fas fa-fw fa-file"/>
                                            </div>
                                            <div className="mr-5">{this.state.terimaKbk} Diterima Tim KBK</div>
                                        </div>
                                        {/*<Link className="card-footer text-white clearfix small z-1" to="#">*/}
                                        {/*    <span className="float-left">View Details</span>*/}
                                        {/*    <span className="float-right"><i className="fas fa-angle-right"></i></span>*/}
                                        {/*</Link>*/}
                                    </div>
                                </div>
                                <div className="col-xl-3 col-sm-6 mb-3">
                                    <div className="card text-white bg-warning o-hidden h-100">
                                        <div className="card-body">
                                            <div className="card-body-icon">
                                                <i className="fas fa-fw fa-file"/>
                                            </div>
                                            <div className="mr-5">{this.state.tolakKbk} Ditolak Tim KBK</div>
                                        </div>
                                        {/*<Link className="card-footer text-white clearfix small z-1" to="#">*/}
                                        {/*    <span className="float-left">View Details</span>*/}
                                        {/*    <span className="float-right"><i className="fas fa-angle-right"></i></span>*/}
                                        {/*</Link>*/}
                                    </div>
                                </div>
                            </div>

                            <div className="card mb-3">
                                <div className="card-header">
                                    <i className="fas fa-table"/>
                                    &nbsp;List Review Penelitian
                                </div>
                                <div className="card-body">
                                    <div className="table-responsive">
                                        <PaginatedList
                                            list={this.state.todo}
                                            itemsPerPage={10}
                                            renderList={(list) => (
                                                <>
                                                <Table responsive hover>
                                                    {header()}
                                                    {body(list)}
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
                                    <span>Copyright SMDPA © {(new Date().getFullYear())}</span>
                                </div>
                            </div>
                        </footer>
                    </div>
                </div>
            </div>

        );
    }
}
