import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Header from "../../elements/header";
import Swal from "sweetalert2";
import {Button, Table} from "reactstrap";
import MenuSidebar from "../../elements/menu";
import {PaginatedList} from "react-paginated-list";
import ModalDetailProyekAkhir from "../pengajuanproyekakhir/ModalDetailProyekAkhir";
import pengajuanPAService from "../../services/PengajuanPAService";
import jsPDF from "jspdf";

export default class ListHasilReview extends Component {
    constructor(props) {
        super(props);
        this.onChangeSearchName = this.onChangeSearchName.bind(this);
        this.retrieveData = this.retrieveData.bind(this);
        this.refreshList = this.refreshList.bind(this);

        this.state = {
            data: [],
            page:0,
            perPage:10,
            size:0,
            total:0,
            currentUser: null,
            currentIndex: -1,
            searchName: "",
            role:null,
            show:true
        };
    }

    componentDidMount() {
        this.retrieveData();
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

    retrieveData() {
        pengajuanPAService.reviewKaprodi()
            .then(response => {
                this.setState({
                    data: response.data.data.content,
                    total: response.data.data.totalElements
                });
            })
            .catch(e => {
                Swal.fire(
                    'Warning',
                    e.response.data.message,
                    'warning'
                );
            });
    }

    refreshList() {
        this.retrieveData();
        this.setState({
            currentUser: null,
            currentIndex: -1
        });
    }
    render() {
        const generateColor = () => {
            const randomColor = Math.floor(Math.random() * 16777215)
                .toString(16)
                .padStart(6, '0');
            return `#${randomColor}`;
        }

        const getStatus = (tasks) => {
            if (Object.keys(tasks)[0]){
                if (Object.keys(tasks)[0].toString().toLowerCase() ===  "ended"){
                    return (tasks[Object.keys(tasks)[0]]);
                } else {
                    return (tasks[Object.keys(tasks)[0]].label);
                }
            }
        }

        const exportPDF = () => {
            const unit = "pt";
            const size = "A4"; // Use A1, A2, A3 or A4
            const orientation = "landscape"; // portrait or landscape

            const marginLeft = 40;
            const doc = new jsPDF(orientation, unit, size);

            doc.setFontSize(15);

            const title = "Data Hasil Review Penelitian Mahasiswa";
            const headers = [["No BP", "Nama", "Email", "Tahun", "Program Studi", "Judul", "Review Kaprodi", "Review Tim KBK", "Catatan"]];

            const data = this.state.data.filter((item) => {
                return item.hasilReviewKaprodi !== null
            }).map(d=> [d.requestorCode, d.requestorName, d.requestorEmail, d.tahun, d.programStudi.name, d.judulProyekAkhir,
                d.hasilReviewKaprodi, d.hasilReviewKbk, d.catatan]);

            let content = {
                startY: 50,
                head: headers,
                body: data
            };

            doc.text(title, marginLeft, 40);
            doc.autoTable(content);
            doc.save("Data_Hasil_Review_Proyek_Akhir.pdf");
        }

        function getColor(status) {
            if (status === "diterima"){
                return "green";
            } else {
                return "red";
            }
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
                                    <Link to={'/hasil-review'} >List Hasil Review</Link>
                                </li>
                                <li className="breadcrumb-item active">Overview</li>
                            </ol>
                            {/*<Button color="primary" onClick={() => this.setState(this.state.show?false:true)} style={{width: "fit-content"}}>{!this.state.show?"Show BPMN":"Hidden BPMN"}</Button>*/}
                            {/*<div hidden={this.state.show?false:true} style={{marginBottom: "10px"}}>*/}
                            {/*    {*/}
                            {/*        <div style={{marginLeft:"100px"}}> <BpmnXml name="ManajemenDokumenProyekAkhir"/></div>*/}
                            {/*    }*/}
                            {/*</div>*/}
                            <div className="card mb-3">
                                <div className="card-header">
                                    <b>Hasil Review</b>
                                </div>

                                <div className="card-body">
                                    <div className="input-group mb-3">
                                        <Button color="primary" className="btn btn-success" onClick={exportPDF}>
                                            <i className="fas fa-file-pdf"/>&nbsp;Export to PDF</Button>

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
                                            list={this.state.data.filter((item) => {
                                                return item.hasilReviewKaprodi !== null
                                            })}
                                            itemsPerPage={10}
                                            renderList={(list) => (
                                                <>
                                                    <Table responsive hover>
                                                        <thead>
                                                        <tr>
                                                            <th>No</th>
                                                            <th>Name</th>
                                                            <th>No BP</th>
                                                            <th>Email</th>
                                                            <th>Tahun</th>
                                                            <th>Judul</th>
                                                            <th>Program Studi</th>
                                                            <th>Review Kaprodi</th>
                                                            <th>Review Tim KBK</th>
                                                            <th>Status</th>
                                                            <th>Aksi</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        {
                                                            list.length === 0? <>
                                                                    <tr>
                                                                        <td colSpan={10}>
                                                                            <center>Tidak ada data.</center>
                                                                        </td>
                                                                    </tr>
                                                                </>
                                                                :
                                                            list.filter((item) => {
                                                                if (this.state.searchName !== "" ){
                                                                    if (item.judulProyekAkhir.toLowerCase().includes(this.state.searchName.toLowerCase()) ||
                                                                        item.requestorName.toLowerCase().includes(this.state.searchName.toLowerCase()) ||
                                                                        item.tahun.toString().includes(this.state.searchName.toString())) {
                                                                        return item;
                                                                    } else {
                                                                        return null;
                                                                    }
                                                                } else {
                                                                    return item;
                                                                }
                                                            }).map((item, idx) =>
                                                                <tr key={item.id}>
                                                                    <th scope="row">{idx+1}</th>
                                                                    <td>{item.requestorName}</td>
                                                                    <td>{item.requestorCode}</td>
                                                                    <td>{item.requestorEmail}</td>
                                                                    <td>{item.tahun}</td>
                                                                    <td>{item.judulProyekAkhir}</td>
                                                                    <td>{item.programStudi === null?"":item.programStudi.name}</td>
                                                                    <td style={{color: "white"}}>
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
                                                                        <div style={{backgroundColor: generateColor(), textAlign: "center", width: "max-content", padding: "3px",borderRadius: "5px", fontSize:12, fontFamily:"sans-serif"}}>
                                                                            {getStatus(item.tasks)}
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div style={{width:"110px"}}>
                                                                            <ModalDetailProyekAkhir item={item}/>
                                                                        </div>
                                                                    </td>
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
