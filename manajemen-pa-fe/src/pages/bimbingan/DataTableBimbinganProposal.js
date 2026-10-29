import React from 'react'
import {Table} from 'reactstrap';
import ModalReviewProyekAkhir from "./ModalReviewProyekAkhir";
import {PaginatedList} from "react-paginated-list";

function DataTableBimbinganProposal(props){

    const getStatus = (tasks) => {
        if (Object.keys(tasks)[0]){
            if (Object.keys(tasks)[0].toString().toLowerCase() ===  "ended"){
                return (tasks[Object.keys(tasks)[0]]);
            } else {
                return (tasks[Object.keys(tasks)[0]].label);
            }
        }
    }

    const generateColor = () => {
        const randomColor = Math.floor(Math.random() * 16777215)
            .toString(16)
            .padStart(6, '0');
        return `#${randomColor}`;
    }

    function getColor(status) {
        if (status === "diterima"){
            return "green";
        } else {
            return "red";
        }
    }

    const items = (data) => {
        return <>
            <tbody>
            {
                data.length === 0 ? <>
                        <tr>
                            <td colSpan={10}>
                                <center>Tidak ada data.</center>
                            </td>
                        </tr>
                    </> :
                    data.map((item, idx) =>
                        <tr key={item.id}>
                            <th scope="row">{idx+1}</th>
                            <td>{item.requestorName}</td>
                            <td>{item.requestorCode}</td>
                            <td>{item.requestorEmail}</td>
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
                                    <ModalReviewProyekAkhir item={item}/>
                                </div>
                            </td>
                        </tr>
                    )
            }
            </tbody>
            </>
    };

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
                <th>Review Kaprodi</th>
                <th>Review Tim KBK</th>
                <th>Status</th>
                <th>Aksi</th>
            </tr>
            </thead>
        </>
    }

    return (
        <PaginatedList
            list={props.items}
            itemsPerPage={10}
            renderList={(list) => (
                <>
                    <Table responsive hover>
                        {header()}
                        {items(list)}
                    </Table>
                </>
            )}
        />
    )
}

export default DataTableBimbinganProposal
