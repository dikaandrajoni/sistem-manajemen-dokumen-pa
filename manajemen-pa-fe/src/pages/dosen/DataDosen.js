import React, {useEffect, useState} from "react";
import {Col, Container, Input, Row} from "reactstrap";
import ModalDosen from "./ModalDosen";
import Header from "../../elements/header";
import {Link} from "react-router-dom";
import DataTableDosen from "./DataTableDosen";
import dosenService from "../../services/DosenService";
import Swal from "sweetalert2";
import MenuSidebar from "../../elements/menu";

function DataDosen(props) {
    const [role, setRole] = useState(null);
    const [items, setItems] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [search, setSearch] = useState("");
    const [pageConfig, setPageConfig] = useState({
        page: 0,
        perPage: 20,
        limit: 10,
        total: 1
    });

    useEffect(() => {
       setRole(window.localStorage.getItem('role'));
    }, []);

    const getItems = async () => {
        try {
            const res = await dosenService.getAll();
            setItems(res.data.data.content);
            setPageConfig({
                total: res.data.data.totalElements.valueOf(),
                page: pageConfig.page,
                perPage: res.data.data.numberOfElements.valueOf(),
                limit: pageConfig.limit,
            });
        } catch (err) {
            console.log(err.message)
            if (err.response.data.status !== 500){
                Swal.fire(
                    'Warning',
                    err.response.data.message,
                    'warning'
                )
            } else {
                Swal.fire(
                    'Ops..',
                    err.response.data.data,
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

    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        getItems().catch((res) => {
            console.log(res.response);
        })
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
                                <Link to={'/dashboard'} >Dashboard</Link>
                            </li>
                            <li className="breadcrumb-item active">Overview</li>
                        </ol>
                        <Container className="container">
                            <Row>
                                <Col>
                                    <h3 style={{ margin: "20px 0" }}>Data Dosen</h3>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    { role === "mahasiswa"?<></>:
                                        <ModalDosen buttonLabel="Tambah Dosen" addItemToState={addItemToState} />
                                    }
                                    <div style={{float: "right", position: "relative"}}>
                                        <Input type="text" name="search" id="search" value={search} placeholder="Cari..." onChange={onChangeSearch} className="-search"/>
                                    </div>
                                </Col>
                            </Row>
                            <br/>
                            <Row>
                                <Col>
                                    <DataTableDosen
                                        items={items.filter((item) => {
                                            if (search !== "") {
                                                if (item.name.toLowerCase().includes(search.toLowerCase()) || item.nip
                                                    .includes(search.toLowerCase())) {
                                                    return item
                                                } else {
                                                    return null;
                                                }
                                            }
                                            else {
                                                return item;
                                            }
                                        } )}
                                        updateState={updateState}
                                        deleteItemFromState={deleteItemFromState}
                                    />
                                    {/*<Paging page={pageConfig.page}*/}
                                    {/*        perPage={pageConfig.perPage}*/}
                                    {/*        total={pageConfig.total}*/}
                                    {/*/>*/}
                                </Col>
                            </Row>
                        </Container>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DataDosen;
