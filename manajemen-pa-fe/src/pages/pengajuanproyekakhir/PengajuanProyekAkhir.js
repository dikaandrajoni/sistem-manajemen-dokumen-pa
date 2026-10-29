import React, {useEffect, useState} from "react";
import {Button, Col, Container, Input, Row} from "reactstrap";
import ModalPA from "./ModalProyekAkhir";
import Header from "../../elements/header";
import {Link} from "react-router-dom";
import BpmnXml from "../../Bpmn/BpmnXml/BpmnXml";
import {getBpmnList} from "../../api";
import DataTablePA from "./DataTableProyekAkhir";
import MenuSidebar from "../../elements/menu";
import {Spinner} from "../../components/spinner";
import Swal from "sweetalert2";

function PengajuanProyekAkhir(props) {
    const [items, setItems] = useState([]);
    const [search, setSearch] = useState("");
    const [show, setShow] = useState(true);
    const [isLoading, setIsLoading] = useState(false);
    const [pageConfig, setPageConfig] = useState({
        page: 0,
        perPage: 20,
        limit: 10,
        total: 0
    });

    const getItems = async () => {
        setIsLoading(true)
        try {
            const res = await getBpmnList().finally(() => setIsLoading(false));
            setItems(res.data.data.content);
            setPageConfig({
                total: res.data.data.totalElements.valueOf(),
                page: pageConfig.page,
                perPage: res.data.data.numberOfElements.valueOf(),
                limit: pageConfig.limit,
                });
        } catch (e) {
            console.log(e.response)
            if (e.response.status === 401){
                Swal.fire(
                    "Error",
                    e.response.data.message,
                    'error'
                ).finally(()=> {
                    window.location.replace("/");
                })
            } else {
                Swal.fire(
                    "Error",
                    "error",
                    'error'
                )
            }

        }
    };

    const addItemToState = (item) => {
        setItems([...items, item]);
    };

    const updateState = (item) => {
        const itemIndex = items.findIndex((data) => data.id === item.id);
        const newArray = [
            ...items.slice(0, itemIndex),
            item,
            ...items.slice(itemIndex + 1)
        ];
        setItems(newArray);
    };

    const deleteItemFromState = (id) => {
        const updatedItems = items.filter((item) => item.id !== id);
        setItems(updatedItems);
    };

    // const downloadTemplate = () => {
    //     return pengajuanPAService.downloadTemplate().catch((res) => {
    //             Swal.fire(
    //                 "Warning",
    //                 "Gagal download file template!",
    //                 'warning'
    //             )
    //     });
    // }

    useEffect(() => {
        getItems().catch((res) => {
            console.log(res.response);
        });
    }, []);

    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

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
                        <Button color="primary" onClick={() => setShow(show?false:true)} style={{width: "fit-content"}}>{!show?"Show BPMN":"Hidden BPMN"}</Button>
                        <div hidden={show?false:true} style={{marginBottom: "10px"}}>
                            {
                               <div style={{marginLeft:"100px"}}> <BpmnXml name="ManajemenDokumenProyekAkhir"/></div>
                            }
                        </div>
                        <div className="card mb-3">
                        <Container className="container">
                            <Row>
                                <Col>
                                    <h3 style={{ margin: "20px 0" }}>Pengajuan Penelitian</h3>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <ModalPA buttonLabel="Ajukan Judul" addItemToState={addItemToState} />
                                    {/*<Button onClick={() => downloadTemplate()}><i className="fa fa-file-download"/>&nbsp;Download Template</Button>*/}
                                    <div style={{float: "right", position: "relative"}}>
                                        <Input type="text" name="search" id="search" value={search} placeholder="Cari..." onChange={onChangeSearch} className="-search"/>
                                    </div>
                                </Col>
                            </Row>
                            <br/>
                            <Row>
                                <Col>
                                    { isLoading &&
                                        <Spinner/>
                                    }
                                    <DataTablePA
                                        items={items.filter((item) => {
                                            if (search !== "") {
                                                if (item.requestorName.toLowerCase().includes(search.toLowerCase()) || item.judulProyekAkhir.toLowerCase()
                                                    .includes(search.toLowerCase())) {
                                                    return item
                                                } else {
                                                    return null;
                                                }
                                            } else {
                                                return item;
                                            }

                                        } )}
                                        updateState={updateState}
                                        deleteItemFromState={deleteItemFromState}
                                    />

                                    {/*<Paging page={pageConfig.page}*/}
                                    {/*        perPage={pageConfig.perPage}*/}
                                    {/*        total={pageConfig.total}*/}
                                    {/*        />*/}
                                </Col>
                            </Row>
                        </Container>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PengajuanProyekAkhir;
