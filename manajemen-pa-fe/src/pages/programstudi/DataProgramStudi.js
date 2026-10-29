import React, {useEffect, useState} from "react";
import {Col, Container, Input, Row} from "reactstrap";
import Header from "../../elements/header";
import {Link} from "react-router-dom";
import DataTableMahasiswa from "./DataTableProgramStudi";
import ModalProgramStudi from "./ModalProgramStudi";
import jsPDF from "jspdf";
import "jspdf-autotable";
import programStudiService from "../../services/ProgramStudiService";
import MenuSidebar from "../../elements/menu";


function DataProgramStudi(props) {
    const [role, setRole] = useState(null);
    const [items, setItems] = useState([]);
    const [search, setSearch] = useState("")
    const [page, setPage] = React.useState({
        skip: 0,
        take: 10,
    });
    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
    }, []);

    const getItems = () => {
        programStudiService.getAll()
            .then((response) => {
                setItems(response.data.data.content)
            })
            .catch((e) => console.log(e));
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

    const filter = (d) => {
        const filter = items.filter((item) => {
            if (d === "" || d===null) {
                return item
            }
            else if (item.name.toLowerCase().match(d.toLocaleLowerCase())) {
                return item
            }
        } );
        setItems(filter);
    }

    useEffect(() => {
        getItems();
    }, []);

    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

    const exportPDF = () => {
        const unit = "pt";
        const size = "A4"; // Use A1, A2, A3 or A4
        const orientation = "portrait"; // portrait or landscape

        const marginLeft = 40;
        const doc = new jsPDF(orientation, unit, size);

        doc.setFontSize(15);

        const title = "Data Program Studi";
        const headers = [["No BP", "Nama", "Email", "Program Studi"]];

        const data = items.map(d=> [d.nim, d.name, d.email, d.programStudi.name]);

        let content = {
            startY: 50,
            head: headers,
            body: data
        };

        doc.text(title, marginLeft, 40);
        doc.autoTable(content);
        doc.save("DataProgramStudi.pdf")
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
                        <div className="card mb-3">
                        <Container className="container">
                            <Row>
                                <Col>
                                    <h3 style={{ margin: "20px 0" }}>List Program Studi</h3>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    { role === "mahasiswa"?<></>:
                                        <ModalProgramStudi buttonLabel="Tambah Program Studi" addItemToState={addItemToState} />
                                    }
                                    {/*<Button color="primary" onClick={exportPDF}><i className="fas fa-file-pdf"/>&nbsp;Export PDF</Button>*/}
                                    {/*<Button color="primary" onClick={""}><i className="fas fa-file-excel"/>&nbsp;Export Excel</Button>*/}
                                    <div style={{float: "right", position: "relative"}}>
                                        <Input type="text" name="search" id="search" value={search} placeholder="Cari nama..." onChange={onChangeSearch} className="-search"/>
                                    </div>
                                </Col>
                            </Row>
                            <br/>
                            <Row>
                                <Col>
                                    <DataTableMahasiswa
                                        items={items.filter((item) => {
                                            if (search !== "") {
                                                if (item.name.toLowerCase().includes(search.toLowerCase())) {
                                                    return item;
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
                                </Col>
                            </Row>
                        </Container><br/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default DataProgramStudi;
