import React, {useEffect, useState} from 'react'
import {Button, Table} from 'reactstrap';
import ModalPA from "./ModalProyekAkhir";
import pengajuanPAService from "../../services/PengajuanPAService";
import Swal from "sweetalert2";
import {format} from "date-fns";
import ModalDetailProyekAkhir from "./ModalDetailProyekAkhir";
import {PaginatedList} from "react-paginated-list";
import {buildStyles, CircularProgressbar} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

function DataTable(props){
    const [role, setRole] = useState(null);

    useEffect(() => {
        setRole(window.localStorage.getItem('role'));
    }, []);

    const deleteItem = id => {
        let confirmDelete = window.confirm('Apakah ingin menghapus data?')
        if(confirmDelete){
            pengajuanPAService.delete(id)
                .then(res => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Data berhasil dihapus!",
                            'success'
                        ).finally(() => window.location.reload());
                    }
                })
                .catch((res) => {
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
    }

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

    const getColorStatus = (status) => {
        let color;
        switch (status) {
            case "submitLaporanMbkm":
            case "submitPraProposal":
                color = '#dc3545';
                break;
            case "reviewKaprodi":
                color = '#e83e8c';
                break;
            case "reviewKoordinatorPA":
            case "submitMbkmKaprodi":
                color = '#e83e8c';
                break;
            case "reviewTimKBK":
                color = '#d39e00';
                break;
            case "submitKoordinatorPA":
                color = '#c69500';
                break;
            case "submitKaprodi":
                color = '#fd7e14';
                break;
            case "revisiJudul":
                color = '#bd2130';
                break;
            case "submitProposal":
            case "revisiProposal":
                color = '#ffe8a1';
                break;
            case "reviewProposal":
                color = '#80bdff';
                break;
            case "daftarSeminar":
                color = '#007bff';
                break;
            case "aturJadwalSeminar":
                color = '#005cbf';
                break;
            case "inputHasilSeminar":
                color = '#10707f';
                break;
            case "submitLaporan":
            case "revisiLaporan":
                color = '#0c5460';
                break;
            case "reviewPembimbing":
                color = '#20c997';
                break;
            case "daftarSidang":
                color = '#1c7430';
                break;
            case "aturJadwalSidang":
                color = '#155724';
                break;
            case "inputHasilSidang":
                color = '#28a745';
                break;
            default:
                color = '#00CD63';
                break;
        }
        return color;

    }

    const getStatus = (tasks) => {
        if (Object.keys(tasks)[0].toString().toLowerCase() ===  "ended"){
            return (tasks[Object.keys(tasks)[0]]);
        } else {
            return (tasks[Object.keys(tasks)[0]].label);
        }
    }

    function getColor(status) {
        if (status === "diterima"){
            return "green";
        } else {
            return "red";
        }
    }

    const progress = (nilai) => {
        return <>
            <CircularProgressbar
                value={nilai}
                text={`${nilai}%`}
                background
                backgroundPadding={6}
                styles={buildStyles({
                    backgroundColor: "#3e98c7",
                    textColor: "#fff",
                    pathColor: "#fff",
                    trailColor: "transparent"
                })}
            />
        </>
    }

    const items = (data) => {
        return <>
            <tbody>
            {
                data.length === 0 ? <>
                        <tr>
                            <td colSpan={12}>
                                <center>Tidak ada data.</center>
                            </td>
                        </tr>
                    </>:
                    data.map((item, idx) =>
                        <tr key={item.id}>
                            <th scope="row">{idx + 1}</th>
                            <td>{format(new Date(item.createdDate), 'dd-MMM-yyyy')}</td>
                            <td>{item.requestorName}</td>
                            <td>{item.requestorCode}</td>
                            <td>{item.requestorEmail}</td>
                            <td>{item.judulProyekAkhir}</td>
                            <td>{item.programStudi === null ? "" : item.programStudi.name}</td>
                            <td>{item.bidangTopikPA}</td>
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
                                <div style={{
                                    backgroundColor: getColorStatus(item.status),
                                    textAlign: "center",
                                    width: "max-content",
                                    padding: "5px",
                                    borderRadius: "5px",
                                    fontSize: 10,
                                    fontFamily: "sans-serif"
                                }}>
                                    {
                                        getStatus(item.tasks)
                                    }
                                </div>
                            </td>

                            <td>{progress(item.progress)}</td>
                            {/*<td style={{color: "white"}}><div style={{backgroundColor:getColorPalate(idx), padding: "5px", borderRadius: "5px"}}>{item.status}</div></td>*/}
                            <td>
                                <div style={{width: "110px"}}>
                                    <ModalDetailProyekAkhir item={item}/>
                                    {' '}
                                    {
                                        item.status === "submitPraProposal" || item.status === "submitLaporanMbkm" || item.status === "revisiJudul" ? <>
                                                <ModalPA buttonLabel="Edit" item={item} updateState={props.updateState}/>
                                                {' '}
                                                <Button color="danger" style={{margin: "5px", width:"max-content"}} onClick={() => deleteItem(item.id)}><i
                                                    className="fas fa-trash smaller"/></Button>
                                            </>
                                            : <></>
                                    }
                                    {' '}
                                    {/*<Button color="primary" onClick={() => pengajuanPAService.downloadFile(item.id)}><i className="fas fa-file-download"/></Button>*/}
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
                <th>Dibuat</th>
                <th>Nama</th>
                <th>Kode</th>
                <th>Email</th>
                <th>Judul</th>
                <th>Program Studi</th>
                <th>Bidang</th>
                <th>Review Kaprodi</th>
                <th>Review Tim KBK</th>
                <th>Status</th>
                <th>Progress</th>
                <th>Aksi</th>
            </tr>
            </thead>
        </>
    }

    return (
        <>
            <PaginatedList
                list={props.items}
                itemsPerPage={10}
                renderList={(list) => (
                    <>
                        <Table responsive hover style={{fontSize: "14px"}}>
                            {header()}
                            {items(list)}
                        </Table>
                    </>
                )}
            />
        </>
    )
}

export default DataTable;
