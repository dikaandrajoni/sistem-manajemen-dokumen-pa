import React, {useEffect, useState} from "react";
import {Col, Container, Input, Row, Table} from "reactstrap";
import Header from "../../elements/header";
import {Link} from "react-router-dom";
import pengajuanPAService from "../../services/PengajuanPAService";
import DataTableBimbinganProposal from "./DataTableBimbinganProposal";
import MenuSidebar from "../../elements/menu";
import Swal from "sweetalert2";
import {Spinner} from "../../components/spinner";

function BimbinganProposalProyekAkhir(props) {
    const [fetching, setFetching] = useState(true);
    const [items, setItems] = useState([]);
    const [search, setSearch] = useState("");

    const getItems = async () => {
        if (fetching){
            pengajuanPAService.getTodo()
                .then((res) => {
                    if (res.data.status === 200){
                        setItems(res.data.data.content);
                    }
                })
                .finally(() => setFetching(false))
                .catch((res) => {
                    Swal.fire(
                        'Warning',
                        res.response.data.message,
                        'warning'
                    )
                })
        }
    };

    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        getItems();
    }, []);

    return (
        <div>
            <Header/>
            <div id="wrapper">
                <MenuSidebar/>
                <div id="content-wrapper">
                    <div className="container-fluid">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item">
                                <Link to={'/bimbingan-proposal-proyek-akhir'} >Bimbingan Proposal Proyek Akhir</Link>
                            </li>
                            <li className="breadcrumb-item active">Overview</li>
                        </ol>
                        <Container className="container">
                            <Row>
                                <Col>
                                    <h3 style={{ margin: "20px 0" }}>Review Penelitian</h3>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <div style={{float: "right", position: "relative"}}>
                                        <Input type="text" name="search" id="search" value={search} placeholder="Cari..." onChange={onChangeSearch} className="-search"/>
                                    </div>
                                </Col>
                            </Row>
                            <br/>
                            <Row>
                                <Col>
                                    {
                                        fetching?<>
                                            <Table responsive hover>
                                                <thead>
                                                <tr>
                                                    <th>No</th>
                                                    <th>Name</th>
                                                    <th>No BP</th>
                                                    <th>Email</th>
                                                    <th>Judul</th>
                                                    <th>Program Studi</th>
                                                    <th>Review Tim KBK</th>
                                                    <th>Status</th>
                                                    <th>Aksi</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td colSpan={9}><Spinner/></td>
                                                </tr>
                                                </tbody>
                                            </Table>

                                        </>:<>
                                            <DataTableBimbinganProposal
                                                items={items.filter((item) => {
                                                    if (search !== "") {
                                                        if (item.requestorName.toLowerCase().includes(search.toLowerCase()) || item.judulProyekAkhir.toLowerCase()
                                                            .includes(search.toLowerCase())) {
                                                            return item;
                                                        } else {
                                                            return null;
                                                        }
                                                    } else {
                                                        return item;
                                                    }
                                                })}
                                            />
                                        </>
                                    }

                                </Col>
                            </Row>
                        </Container>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default BimbinganProposalProyekAkhir;
