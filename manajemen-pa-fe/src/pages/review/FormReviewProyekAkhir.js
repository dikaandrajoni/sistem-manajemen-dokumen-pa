import React, {useEffect, useState} from "react";
import {Button, Input, Label, Table} from "reactstrap";
import Swal from "sweetalert2";
import ModalPreview from "./ModalPreview";
import dosenService from "../../services/DosenService";
import positionService from "../../services/PositionService";
import TextField from '@mui/material/TextField';
import {AdapterDateFns} from '@mui/x-date-pickers/AdapterDateFns';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {DateTimePicker} from '@mui/x-date-pickers/DateTimePicker';
import Select from "react-select";
import moment from "moment";
import {updateBpmnTask, updateSubProcess, updateSubProcessSecondary} from "../../api";
import pengajuanPAService from "../../services/PengajuanPAService";


function FormReviewProyekAkhir(props) {
    const [user, setUser] = useState(null);
    const [form, setValues] = useState({
        id: "",
        requestorName: "",
        requestorCode: "",
        requestorEmail: "",
        judulProyekAkhir: "",
        proposalMinioPath: "",
        proposalFileName: "",
        isMbkm: false,
        deskripsi: "",
        bidangTopikPA: "",
        programStudi: {
            id:"",
            name:"",
            kaprodiEmail:""
        },
        pembimbing1: {
            id:"",
            name:"",
            email:"",
        },
        pembimbing2: {
            id:"",
            name:"",
            email:"",
        },
        penguji: {
            id:"",
            name:"",
            email:"",
        },
        status: "",
        catatan:"",
        decision:"",
        jadwalSeminarProposal:null,
        jadwalSidangProyekAkhir:null,
        hasilSeminar: "",
        hasilSidang: "",
        bimbinganProposalProyekAkhir:null,
        bimbinganProyekAkhir:null,

    });

    const [detail, setDetail] = useState(null);
    const [remarks, setRemarks] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [optionDosen, setOptionDosen] = useState([]);
    const [selectedP1, setSelectedP1] = useState(null);
    const [selectedP2, setSelectedP2] = useState(null);
    const [selectedPenguji, setSelectedPenguji] = useState(null);
    const [timKbk, setTimKbk] = useState([]);
    const [isTimKbk, setIsTimKbk] = useState(false);
    const [jadwalSeminar, setJadwalSeminar] = useState(null);
    const [tempJadwalSeminar, setTempJadwalSeminar] = useState(null);
    const [jadwalSidang, setJadwalSidang] = useState(null);
    const [tempJadwalSidang, setTempJadwalSidang] = useState(null);
    const [fetching, setFetching] = useState(true);
    const [fetchingDetail, setFetchingDetail] = useState(true);

    var pembimbing1 = [];
    var pembimbing2 = [];
    var penguji = [];

    useEffect(() => {
        setUser(window.localStorage.getItem("email"));
    }, []);

    useEffect(() => {
        if (fetching){
            positionService.getTimKbk()
                .then((res) => {
                    if (res.data.status === 200){
                        setTimKbk(res.data.data);
                    }
                }).finally(()=>setFetching(false))


            timKbk.map((data) => {
                if (data.emailUser === user){
                    setIsTimKbk(true);
                }
            })
        }
    })

    const onChange = (e) => {
        setValues({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    useEffect(() => {
        setIsLoading(true)
        dosenService.getAll()
            .then(res => {
                setOptionDosen(
                    res.data.data.content? res.data.data.content.map((data) => {
                        return {
                            label: data.name,
                            value: data.id
                        }
                    }):[]
                );
            }).finally(() =>
            setIsLoading(false))
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data dosen!",
                    'warning'
                );
            });
    }, []);

    useEffect(() => {
        if (props.item){
            var tempPembimbing1 = props.item.pembimbing1;
            var tempPembimbing2 = props.item.pembimbing2;
            var tempPenguji = props.item.penguji;

            if (tempPembimbing1 !== null){
                pembimbing1.push({label: tempPembimbing1.name, value: tempPembimbing1.id});
            }
            if (tempPembimbing2 !== null){
                pembimbing2.push({label: tempPembimbing2.name, value: tempPembimbing2.id});
            }
            if (tempPenguji !== null){
                penguji.push({label: tempPenguji.name, value: tempPenguji.id});
            }
        }
    }, []);

    useEffect(() => {
        if (fetchingDetail){
            pengajuanPAService.get(form.id)
                .then((res) => {
                    if (res.status === 200){
                        return setDetail(res.data.data.object);
                    }
                })
                .finally(() => setFetchingDetail(false))
                .catch((res) => {
                    if (res.response.data.status !== 500){
                        Swal.fire(
                            'Warning',
                            res.response.data.data,
                            'warning'
                        )
                    } else {
                        Swal.fire(
                            'Ops..',
                            res.response.data.message,
                            'error'
                        )
                    }
                });
        }

    }, [detail]);

    const setData = () => {
        setRemarks(form.catatan)
    }

    const createPayload = {
        requestorCode: form.requestorCode,
        requestorName: form.requestorName,
        requestorEmail: form.requestorEmail,
        judulProyekAkhir: form.judulProyekAkhir,
        deskripsi: form.deskripsi,
        isMbkm: form.isMbkm,
        bidangTopikPA: form.bidangTopikPA,
        programStudi: form.programStudi,
        pembimbing1:{
            id: selectedP1 !== null?selectedP1.value:null,
        },
        pembimbing2:{
            id: selectedP2 !== null?selectedP2.value:null,
        },
        penguji:{
            id: selectedPenguji !== null?selectedPenguji.value:null,
        },
        jadwalSeminarProposal: tempJadwalSeminar!==null?tempJadwalSeminar.replace(" ","T"):null,
        jadwalSidangProyekAkhir: tempJadwalSidang!==null?tempJadwalSidang.replace(" ", "T"):null,
        catatan: form.catatan,
        hasilSeminar: form.hasilSeminar!==""?form.hasilSeminar:null,
        hasilSidang: form.hasilSidang!==""?form.hasilSidang:null
    }

    const submitReview = (decision) => {
        setData();
        const payload = createPayload;
        if (form.status === "reviewProposal"){
            const id = detail.bimbinganProposalProyekAkhir.id;
            updateSubProcess(id, form.status, {
                catatan: form.catatan
            }, decision)
                .then((res) => {
                    if (res.data.status === 200 && res.data.data === true){
                        Swal.fire(
                            'Success',
                            "Berhasil submit data!",
                            'success'
                        ).finally(() => window.location.reload());
                    } else {
                        Swal.fire(
                            'Warning',
                            "Tidak dapat melakukan submit!",
                            'warning'
                        ).finally(() => window.location.reload());
                    }
                })
                .catch((res) => {
                    if (res.response.data.status !== 500){
                        Swal.fire(
                            'Warning',
                            res.response.data.data,
                            'warning'
                        )
                    } else {
                        Swal.fire(
                            'Ops..',
                            res.response.data.message,
                            'error'
                        )
                    }
            });

        } else if(form.status ==="reviewPembimbing") {
            const id = detail.bimbinganProyekAkhir.id;
            updateSubProcessSecondary(id, form.status, {
                catatan: form.catatan
            }, decision)
                .then((res) => {
                    if (res.data.status === 200 && res.data.data === true){
                        Swal.fire(
                            'Success',
                            "Berhasil submit data!",
                            'success'
                        ).finally(() => window.location.reload());
                    } else {
                        Swal.fire(
                            'Warning',
                            "Tidak dapat melakukan submit!",
                            'warning'
                        ).finally(() => window.location.reload());
                    }
                })
                .catch((res) => {
                    if (res.response.data.status !== 500){
                        Swal.fire(
                            'Warning',
                            res.response.data.data,
                            'warning'
                        )
                    } else {
                        Swal.fire(
                            'Ops..',
                            res.response.data.message,
                            'error'
                        )
                    }
                });
        } else {
            console.log(payload)
            updateBpmnTask(form.id, form.status, payload, decision)
                .then((res) => {
                    if (res.data.status === 200 && res.data.data === true){
                        Swal.fire(
                            'Success',
                            "Berhasil submit data!",
                            'success'
                        ).finally(() => window.location.reload());
                    } else {
                        Swal.fire(
                            'Warning',
                            "Tidak dapat melakukan submit!",
                            'warning'
                        ).finally(() => window.location.reload());
                    }
                })
                .catch((res) => {
                    if (res.response.status === 500){
                        Swal.fire(
                            'Ops...',
                            res.response.data.message,
                            'error'
                        )
                    } else {
                        Swal.fire(
                            'Warning',
                            res.response.data.message,
                            'warning'
                        )
                    }

                });
        }

    };

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

    const downloadProposal = (id) => {
        if (form.proposalMinioPath){
            return pengajuanPAService.downloadProposal(id)
                .then((res) => {
                    const file = new Blob([res.data], { type: "application/pdf" });
                    //Build a URL from the file
                    const fileURL = URL.createObjectURL(file);
                    //Open the URL on new Window
                    const pdfWindow = window.open();
                    pdfWindow.location.href = fileURL;
                })
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

    useEffect(() => {
        if (props.item) {
            const { id, requestorName, requestorCode, status, requestorEmail, judulProyekAkhir, programStudi, bidangTopikPA,
                proposalMinioPath, pembimbing1, pembimbing2, penguji, isMbkm, deskripsi, bimbinganProposalProyekAkhir, bimbinganProyekAkhir } = props.item;
            setValues({ id, requestorName, requestorCode, status, requestorEmail, judulProyekAkhir, programStudi, bidangTopikPA,
                proposalMinioPath, pembimbing1, pembimbing2, penguji, isMbkm, deskripsi, bimbinganProposalProyekAkhir, bimbinganProyekAkhir });
        }
    }, [props.item]);

    return (
        <div>
            <div className="card mb-3">
                <Table style={{fontSize: 16}}>
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
                        <td style={{border: "none"}}>Topik</td>
                        <td style={{border: "none"}}>:</td>
                        <td style={{border: "none"}}>{form.bidangTopikPA}</td>
                    </tr>
                    {
                        form.status === "reviewProposal" &&
                        <tr>
                            <td style={{border: "none"}}>Review Proposal Ke</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>{form.bimbinganProposalProyekAkhir.bimbinganKe}</td>
                        </tr>
                    }
                    {
                        form.status === "reviewPembimbing" &&
                        <tr>
                            <td style={{border: "none"}}>Review Laporan Ke</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>{form.bimbinganProyekAkhir.bimbinganKe}</td>
                        </tr>
                    }
                    {
                        form.pembimbing1 !== null &&
                        <tr>
                            <td style={{border: "none"}}>Pembimbing 1</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>{form.pembimbing1.name}</td>
                        </tr>
                    }

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

                    {
                        form.status === 'submitMbkmKaprodi' || form.status === 'submitKaprodi' &&
                        <>
                            <tr>
                                <td style={{border: "none"}}>Pembimbing 1</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>
                                    <Select options={optionDosen} onChange={setSelectedP1} defaultValue={pembimbing1?pembimbing1:selectedP1} placeholder="Pilih Pembimbing 1" required/>
                                </td>
                            </tr>
                            <tr>
                                <td style={{border: "none"}}>Pembimbing 2</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>
                                    <Select options={optionDosen} onChange={setSelectedP2} defaultValue={pembimbing2?pembimbing2:selectedP2} placeholder="Pilih Pembimbing 2" required/>
                                </td>
                            </tr>
                        </>
                    }

                    {
                        form.status === 'aturJadwalSeminar' &&
                        <>
                            <tr>
                                <td style={{border: "none"}}>Jadwal Seminar</td>
                                <td style={{border: "none"}}>:</td>
                                <td style={{border: "none"}}>
                                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                                        <DateTimePicker
                                            renderInput={(props) => <TextField {...props} required/>}
                                            label="Pilih Jadwal"
                                            value={jadwalSeminar}
                                            disableMaskedInput
                                            inputFormat="dd MMM yyyy HH:mm"
                                            onChange={(tgl) => {
                                                setJadwalSeminar(moment(tgl).format());
                                                setTempJadwalSeminar(moment(tgl).format("yyyy-MM-DD HH:mm"));
                                            }}
                                        />
                                    </LocalizationProvider>
                                </td>
                            </tr>

                        </>
                    }

                    {
                        form.status === 'aturJadwalSidang' &&
                        <tr>
                            <td style={{border: "none"}}>Jadwal Sidang</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>
                                <LocalizationProvider dateAdapter={AdapterDateFns}>
                                    <DateTimePicker
                                        renderInput={(props) => <TextField {...props} required/>}
                                        label="Pilih Jadwal"
                                        disableMaskedInput
                                        value={jadwalSidang}
                                        inputFormat="dd MMM yyyy HH:mm"
                                        onChange={(tgl) => {
                                            setJadwalSidang(moment(tgl).format());
                                            setTempJadwalSidang(moment(tgl).format("yyyy-MM-DD HH:mm"));
                                        }}
                                    />
                                </LocalizationProvider>
                            </td>
                        </tr>
                    }

                    {
                        (form.status === "aturJadwalSeminar" || form.status === "aturJadwalSidang") &&
                        <tr>
                            <td style={{border: "none"}}>Atur Penguji</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>
                                <Select options={optionDosen} onChange={setSelectedPenguji} defaultValue={penguji?penguji:selectedPenguji} placeholder="Pilih Penguji" required/>
                            </td>
                        </tr>
                    }

                    {
                        form.status === "inputHasilSeminar"?<>
                        <tr>
                            <td style={{border: "none"}}>Hasil Seminar</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>
                                <Label style={{marginLeft: "20px"}}>
                                    <Input type="radio" onChange={onChange} checked={form.hasilSeminar==="lulus"?true:false} name="hasilSeminar" value="lulus"/>
                                    &nbsp;Lulus
                                </Label><br/>
                                <Label style={{marginLeft: "20px"}}>
                                    <Input type="radio" onChange={onChange} checked={form.hasilSeminar==="tidak_lulus"?true:false} name="hasilSeminar" value="tidak_lulus"/>
                                    &nbsp;Tidak Lulus
                                </Label>
                            </td>
                        </tr></>:<></>
                    }

                    {
                        form.status === "inputHasilSidang"?<>
                        <tr>
                            <td style={{border: "none"}}>Hasil Sidang</td>
                            <td style={{border: "none"}}>:</td>
                            <td style={{border: "none"}}>
                                <Label style={{marginLeft: "20px"}}>
                                    <Input type="radio" onChange={onChange} checked={form.hasilSidang==="lulus"?true:false} name="hasilSidang" value="lulus"/>
                                    &nbsp;Lulus
                                </Label><br/>
                                <Label style={{marginLeft: "20px"}}>
                                    <Input type="radio" onChange={onChange} checked={form.hasilSidang==="tidak_lulus"?true:false} name="hasilSidang" value="tidak_lulus"/>
                                    &nbsp;Tidak Lulus
                                </Label>
                            </td>
                        </tr>
                    </>:<></>
                    }
                    <tr>
                        <td style={{border: "none"}}>Catatan</td>
                        <td style={{border: "none"}}>:</td>
                        <td style={{border: "none"}}>
                            <Input type="textarea" onChange={onChange} name="catatan" placeholder="Tambahkan catatan...."/>
                        </td>
                    </tr>
                    </tbody>
                </Table>
            </div>
            <br/>
            <center>
                { form.status === "reviewProposal" &&
                    <ModalPreview item={props.item} mode="proposal"/>
                }
                { form.status === "reviewPembimbing" &&
                    <ModalPreview item={props.item} mode="laporan"/>
                }
                <br/>
                <Button onClick={() => submitReview("diterima")} className="btn-success" style={{width:"120px", margin:"10px"}}><i className="fa fa-check"/>&nbsp;
                    {form.status === "aturJadwalSeminar" || form.status === "aturJadwalSidang" || form.status ==="reviewKoordinatorPA" ||
                        form.status === "submitKoordinatorPA" || form.status === "inputHasilSeminar" || form.status==="inputHasilSidang"?"Submit":"Terima"}</Button>
                &nbsp;
                {
                    form.status === "reviewTimKBK" || form.status === "reviewProposal" || form.status ==="reviewPembimbing" || form.status === "reviewKaprodi" ||
                    form.status === "submitKaprodi"?<>
                        <Button onClick={() => submitReview("ditolak")} className="btn-danger" style={{margin: "10px", color:"white", width:"120px"}}>
                            <i className="fa fa-times-circle"/>&nbsp;{
                            form.status === "reviewProposal" || form.status === "reviewPembimbing"?"Revisi": "Tolak"}</Button>
                    </>:<></>
                }
            </center>

        </div>
    );
}

export default FormReviewProyekAkhir;
