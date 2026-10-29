import React, {useEffect, useState} from "react";
import {Button, Modal, ModalBody, ModalHeader, Table} from "reactstrap";
import "../styleModal.css";
import pengajuanPAService from "../../services/PengajuanPAService";
import Swal from "sweetalert2";
import moment from "moment";
import Loader from "react-loader-spinner";
import {updateBpmnTask, updateSubProcess, updateSubProcessSecondary} from "../../api";
import ModalPreview from "../review/ModalPreview";

function ModalReview(props) {
    const [modal, setModal] = useState(false);
    const [form, setValues] = useState({
        id: "",
        requestorName: "",
        requestorCode: "",
        requestorEmail: "",
        judulProyekAkhir: "",
        deskripsi: "",
        proposalMinioPath: null,
        proposalFileName: "",
        isMbkm: false,
        bidangTopikPA: "",
        programStudi: {
            id:"",
            name:"",
            kaprodiEmail:""
        },
        pembimbing1Id: "",
        pembimbing1: {
            id:"",
            name:"",
            email:"",
        },
        pembimbing2Id: "",
        pembimbing2: {
            id:"",
            name:"",
            email:"",
        },
        pengujiId: "",
        penguji: {
            id:"",
            name:"",
            email:"",
        },
        status: "",
        catatan:"",
        decision:"",
        hasilSeminar: "",
        hasilSidang: "",
        laporanFileName:"",
        laporanMinioPath:null,
        jadwalSeminarProposal:null,
        jadwalSidangProyekAkhir:null,
    });

    const [detail, setDetail] = useState(null);
    const [sendingProposal, setSendingProposal] = useState(false);
    const [sendingLaporan, setSendingLaporan] = useState(false);
    const [file, setFile] = useState(null);
    const [fileLaporan, setFileLaporan] = useState(null);
    const [fileName, setFileName] = useState("");
    const [fileNameLaporan, setFileNameLaporan] = useState("");
    const [role, setRole] = useState(null);
    const [fetching, setFetching] = useState(true);

    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
    }, []);
    const toggle = () => {
        setModal(!modal);
    };

    const closeBtn = (
        <button className="close" onClick={toggle}>
            &times;
        </button>
    );

    const downloadProposal = (id) => {
        if (form.proposalMinioPath){
            return pengajuanPAService.downloadProposal(id)
                .catch((res) => {
                    console.log(res.response)
                    // Swal.fire(
                    //     'Ops..',
                    //     res.response.data.message,
                    //     'error'
                    // )
                });
        } else {
            Swal.fire(
                'Warning',
                "File proposal belum diupload!",
                'info'
            )
        }
    }

    const onChangeFile = (e) => {
        setFile(e.target.files[0])
        const name = e.target.files[0].name;
        setFileName(name)
    }

    const onChangeFileLaporan = (e) => {
        setFileLaporan(e.target.files[0])
        const name = e.target.files[0].name;
        setFileNameLaporan(name)
    }

    const downloadLaporan = (id) => {
        if (form.laporanMinioPath){
            return pengajuanPAService.downloadLaporan(id)
                .catch((res) => {
                    console.log(res.response);
                    // Swal.fire(
                    //     'Ops..',
                    //     res.response.data.message,
                    //     'error'
                    // )
                });
        } else {
            Swal.fire(
                'Warning',
                "File laporan belum diupload!",
                'info'
            )
        }
    }

    useEffect(() => {
        if (fetching){
            pengajuanPAService.get(form.id)
                .then((res) => {
                    if (res.status === 200){
                        return setDetail(res.data.data.object);
                    }
                }).finally(()=>setFetching(false))
                .catch((res) => {
                    Swal.fire(
                        'Ops..',
                        res.response.data.message,
                        'error'
                    )
                });
        }

    }, [detail]);

    const submitSubProcess = () => {
        if (detail.bimbinganProposalProyekAkhir !== null){
            updateSubProcess(detail.bimbinganProposalProyekAkhir.id, form.status, "{}", "submit")
                .then((res) => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Proposal berhasil diupload!",
                            'success'
                        );
                    }
                })
                .finally(() => {
                    setFile(null);
                    setSendingProposal(false);
                    window.location.reload();
                })
                .catch((res) => {
                    Swal.fire(
                        'Ops..',
                        res.response.data.message,
                        'error'
                    )
                });
        }
    }

    const submitSubProcessSecondary = () => {
        if (detail.bimbinganProyekAkhir !== null){
            updateSubProcessSecondary(detail.bimbinganProyekAkhir.id, form.status, "{}", "submit")
                .then((res) => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Laporan berhasil diupload!",
                            'success'
                        );
                    }
                })
                .finally(() => {
                    setFileLaporan(null);
                    setSendingLaporan(false);
                    window.location.reload();
                })
                .catch((res) => {
                    Swal.fire(
                        'Ops..',
                        res.response.data.message,
                        'error'
                    )
                });
        }
    }

    const uploadProposal = (id) => {
        if (file !== null){
            setSendingProposal(true);
            const formData = new FormData();
            formData.append("file", file, fileName)
            pengajuanPAService.uploadProposal(id, formData)
                .finally(() => {
                    submitSubProcess();
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
        } else {
            Swal.fire(
                'Warning',
                "File proposal belum diunggah!",
                'warning'
            )
        }

    }

    const uploadLaporan = (id) => {
        if (fileLaporan !== null){
            setSendingLaporan(true);
            const formData = new FormData();
            formData.append("file", fileLaporan, fileNameLaporan);
            pengajuanPAService.uploadLaporan(id, formData)
                .finally(() => {
                    submitSubProcessSecondary();
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
        } else {
            Swal.fire(
                'Warning',
                "File laporan belum diunggah!",
                'warning'
            );
        }

    }

    let title="Detail Proyek Akhir";
    let button = "";
    button = (
        <Button
            color="info"
            onClick={toggle}
            style={{ float: "left", margin: "3px"}}
        ><i className="fa fa-info fa-1x"/>
        </Button>
    );
    useEffect(() => {
        if (props.item) {
            const { id, requestorName, requestorCode, status, requestorEmail, judulProyekAkhir, deskripsi, programStudi, bidangTopikPA, proposalMinioPath, laporanMinioPath, pembimbing1, pembimbing2, penguji, jadwalSeminarProposal, jadwalSidangProyekAkhir, catatan } = props.item;
            setValues({ id, requestorName, requestorCode, status, requestorEmail, judulProyekAkhir, deskripsi, programStudi, bidangTopikPA, proposalMinioPath, laporanMinioPath, pembimbing1, pembimbing2, penguji, jadwalSeminarProposal, jadwalSidangProyekAkhir , catatan});
        }
    }, [props.item]);

    const submit = (id) => {
        updateBpmnTask(id, form.status, "{}", "submit")
            .then((res)=>{
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Data berhasil disubmit!",
                        'success'
                    ).finally(()=> window.location.reload());
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
            })
    }

    return (
        <div>
            {button}
            <Modal
                isOpen={modal}
                toggle={toggle}
                className={props.className}
                backdrop={"static"}
                keyboard={false}
                contentClassName="custom-modal-style"
            >
                <ModalHeader toggle={toggle} close={closeBtn}>
                    {title}
                </ModalHeader>
                <ModalBody>
                    <div className="card mb-3">
                        <Table>
                            <tbody>
                            <tr>
                                <td style={{border: "none", width:"25%"}}>No BP</td>
                                <td style={{border: "none", width:"5%"}} >:</td>
                                <td style={{border: "none"}}>{form.requestorCode}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Nama</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.requestorName}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Email</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.requestorEmail}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Program Studi</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.programStudi.name}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Judul</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.judulProyekAkhir}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Deskripsi</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.deskripsi}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Bidang Topik</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.bidangTopikPA}</td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Pembimbing 1</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.pembimbing1 !== null?form.pembimbing1.name:""}</td>
                            </tr>
                            {
                                form.pembimbing2 !== null &&
                                <tr>
                                    <td style={{border: "none"}}>Pembimbing 2</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td style={{border: "none"}}>{form.pembimbing2.name}</td>
                                </tr>
                            }
                            {
                                form.penguji !== null &&
                                <tr>
                                    <td style={{border: "none"}}>Penguji</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td style={{border: "none"}}>{form.penguji.name}</td>
                                </tr>
                            }
                            <tr>
                                <td style={{border: "none"}}>Catatan</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>{form.catatan}</td>
                            </tr>

                            {
                                form.jadwalSeminarProposal !== null &&
                                <tr>
                                    <td style={{border: "none"}}>Jadwal Seminar</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td style={{border: "none"}}>{moment.utc(form.jadwalSeminarProposal).format('DD-MMM-YYYY HH:mm')}</td>
                                </tr>
                            }

                            {
                                form.jadwalSidangProyekAkhir !== null &&
                                <tr>
                                    <td style={{border: "none"}}>Jadwal Sidang</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td style={{border: "none"}}>{moment.utc(form.jadwalSidangProyekAkhir).format('DD-MMM-YYYY HH:mm')}</td>
                                </tr>
                            }
                            {
                                role ==="mahasiswa" && (form.status === 'submitProposal' || form.status === 'revisiProposal') &&
                                <tr>
                                    <td style={{border: "none"}}>Upload Proposal</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td style={{border: "none"}}>
                                        <div style={{float: "left"}}>
                                            {
                                                sendingProposal &&
                                                <div className="spinner">
                                                    <Loader type="ThreeDots" color="#2BAD60" height="80" width="80" />
                                                </div>
                                            }
                                            <input type="file" name="file" onChange={onChangeFile} accept="application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"/>
                                            <button className="btn btn-outline-info" onClick={() => uploadProposal(form.id)} disabled={sendingProposal}><i className="fa fa-file-upload"/>&nbsp;Upload Proposal</button>
                                        </div>
                                    </td>
                                </tr>
                            }

                            {
                                role ==="mahasiswa" && (form.status === 'submitLaporan' || form.status === 'revisiLaporan') &&
                                <tr>
                                    <td style={{border: "none"}}>Upload Laporan</td>
                                    <td style={{border: "none"}}>:</td>
                                    <td>
                                        <div style={{float: "left"}}>
                                            {
                                                sendingLaporan &&
                                                <div className="spinner">
                                                    <Loader type="ThreeDots" color="#2BAD60" height="80" width="80" />
                                                </div>
                                            }
                                            <input type="file" name="file" onChange={onChangeFileLaporan} accept="application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"/>
                                            <button className="btn btn-outline-info" onClick={() => uploadLaporan(form.id)} disabled={sendingLaporan}><i className="fa fa-file-upload"/>&nbsp;Upload Laporan</button>
                                        </div>
                                    </td>
                                </tr>
                            }

                            </tbody>
                        </Table>
                    </div>
                    <center>

                        {
                            form.status !== "daftarSeminar" && form.status !== "daftarSidang"?<>
                                <div>
                                    { form.proposalMinioPath !== null &&
                                        <ModalPreview item={props.item} mode="proposal"/>
                                    }
                                    <br/>
                                    { form.laporanMinioPath !== null &&
                                        <ModalPreview item={props.item} mode="laporan"/>
                                    }
                                </div>
                                <br/>
                                <Button color="primary" onClick={() => downloadProposal(form.id)} style={{width: "fit-content"}}><i className="fas fa-file-download"/> Download Proposal</Button>
                                <Button color="success" onClick={() => downloadLaporan(form.id)} style={{width: "fit-content", marginLeft: "20px"}}><i className="fas fa-file-download"/> Download Laporan</Button>
                            </> :
                                <>
                                    <Button onClick={() => submit(form.id)} color="primary"><i className="fa fa-archive"/>&nbsp;{form.status === "daftarSeminar"?"Daftar Sempro":"Daftar Sidang"}</Button>
                                </>
                        }

                    </center>

                    <br/>
                    <br/>
                </ModalBody>

            </Modal>
        </div>
    );
}

export default ModalReview;
