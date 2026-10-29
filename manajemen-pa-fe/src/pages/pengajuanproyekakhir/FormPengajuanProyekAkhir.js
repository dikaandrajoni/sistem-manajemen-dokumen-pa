import React, {useEffect, useState} from "react";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import programStudiService from "../../services/ProgramStudiService";
import pengajuanPAService from "../../services/PengajuanPAService";
import Swal from "sweetalert2";
import dosenService from "../../services/DosenService";
import {saveAsDraftBpmn, startBpmn, updateBpmnTask} from "../../api";
import {Spinner} from "../../components/spinner";

import Select from 'react-select'

function FormPengajuanProyekAkhir(props) {
    const name = window.localStorage.getItem('user');
    const email = window.localStorage.getItem('email');
    const kode = window.localStorage.getItem('username');
    const role = window.localStorage.getItem('role');
    const [file, setFile] = useState(null);
    const [fileLaporan, setFileLaporan] = useState(null);
    const [fileName, setFileName] = useState("");
    const [fileNameLaporan, setFileNameLaporan] = useState("");
    const [mbkm, setMbkm] = useState(false);
    const [form, setValues] = useState({
        id: "",
        requestorCode: kode,
        requestorName: name,
        requestorEmail: email,
        tahun:"",
        judulProyekAkhir: "",
        deskripsi: "",
        isMbkm: false,
        bidangTopikPA: "",
        proposalFileName: "",
        proposalMinioId: "",
        proposalMinioPath: "",
        pembimbing1Id: "",
        pembimbing1: {
            id:"",
            name:""
        },
        pembimbing2Id: "",
        pembimbing2: {
            id:"",
            name:""
        },
        pengujiId: "",
        penguji: {
            id:"",
            name:""
        },
        programStudiId:"",
        programStudi: {
            id:"",
            name:""
        },
        status:""
    });
    const [isLoading, setIsLoading] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [optionDosen, setOptionDosen] = useState([]);
    const [optionProdi, setOptionProdi] = useState([]);
    const [selectedProdi, setSelectedProdi] = useState(null);
    const [selectedDosen, setSelectedDosen] = useState(null);
    const [selectedBidang, setSelectedBidang] = useState(null);
    var prodi = [];
    var pembimbing = [];
    var bidang = [];
    const optionBidang = [
        {
            label: "Mobile Programming",
            value: "Mobile Programming"
        },
        {
            label: "Sistem Informasi",
            value: "Sistem Informasi"
        },
        {
            label: "Jaringan",
            value: "Jaringan"
        },
        {
            label: "Multimedia",
            value: "Multimedia",
        },
        {
            label: "IoT",
            value: "IoT",
        },
        {
            label: "Cloud Computing",
            value: "Cloud Computing",
        },
        {
            label: "Cyber Security",
            value: "Cyber Security",
        },
        {
            label: "AR/VR",
            value: "AR/VR",
        },
        {
            label: "Penerapan Algoritma",
            value: "Penerapan Algoritma",
        },
        {
            label: "Kecerdasan Buatan",
            value: "Kecerdasan Buatan",
        },
        {
            label: "Sistem Pakar",
            value: "Sistem Pakar",
        },
        {
            label: "Sistem Pendukung Keputusan",
            value: "Sistem Pendukung Keputusan",
        },
        {
            label: "Lain-lain",
            value: "Lain-lain",
        }
    ];

    useEffect(() => {
        programStudiService.getAll()
            .then(res => {
                setOptionProdi(
                    res.data.data.content? res.data.data.content.map((data) => {
                        return {
                            label: data.name,
                            value: data.id
                        }
                    }):[]
                );
            })
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data program studi!",
                    'warning'
                );
            });
    }, []);

    useEffect(() => {
        setIsLoading(true);
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
            })
            .finally(() => {setIsLoading(false);})
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data program studi!",
                    'warning'
                );
            });
    }, []);

    const onChange = (e) => {
        setValues({
            ...form,
            [e.target.name]: e.target.value
        });
    };

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

    useEffect(() => {
        if (props.item){
            var tempProdi = props.item.programStudi;
            var tempPembimbing = props.item.pembimbing1;
            var tempTopik = props.item.bidangTopikPA;
            if (tempProdi !== null){
                prodi.push({label: tempProdi.name, value: tempProdi.id});
            }
            if (tempPembimbing !== null){
                pembimbing.push({label: tempPembimbing.name, value: tempPembimbing.id});
            }
            if (tempTopik !== null){
                bidang.push({label: tempTopik, value: tempTopik});
            }
        }
    }, []);

    const createPayload = {
        requestorCode: form.requestorCode,
        requestorName: form.requestorName,
        requestorEmail: form.requestorEmail,
        tahun: new Date().getFullYear(),
        judulProyekAkhir: form.judulProyekAkhir,
        deskripsi: form.deskripsi,
        isMbkm: mbkm,
        bidangTopikPA: selectedBidang === null?form.bidangTopikPA:selectedBidang.value,
        programStudi: {
            id: selectedProdi === null?form.programStudiId:selectedProdi.value,
            name: selectedProdi === null?form.programStudi.name:selectedProdi.label
        },
        pembimbing1:{
            id: selectedDosen === null?form.pembimbing1Id:selectedDosen.value,
        }
    }

    const submit = (id, task, payload) => {
        updateBpmnTask(id, task, payload, "submit")
            .finally(() => {
                if (file !== null){
                    setUploading(true)
                    const formData = new FormData();
                    formData.append("file", file, fileName)
                    pengajuanPAService.uploadProposal(id, formData)
                        .finally(() => {
                            setUploading(false);
                            Swal.fire(
                                        'Success',
                                        'Data berhasil disubmit!',
                                        'success'
                                    ).finally(() => window.location.reload());
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
                } else if (fileLaporan != null){
                    setUploading(true)
                    const formData = new FormData();
                    formData.append("file", fileLaporan, fileNameLaporan)
                    pengajuanPAService.uploadLaporan(id, formData)
                        .finally(() => {
                            setUploading(false);
                            Swal.fire(
                                'Success',
                                'Data berhasil disubmit!',
                                'success'
                            ).finally(() => window.location.reload());
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
                        'Success',
                        'Data berhasil disubmit!',
                        'success'
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
    }

    const startSubmit = (e) => {
        const payload = createPayload;
        startBpmn(payload, true)
            .then(r => {
                if (r.data.status === 200){
                    var id = r.data.data.object.id;
                    var task ;
                    if (form.isMbkm || mbkm){
                        task = "submitLaporanMbkm";
                    } else {
                        task = "submitPraProposal";
                    }
                    submit(id, task, payload);
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
    };


    const uploadFile = (id, data) => {
        setUploading(true);
        const formData = new FormData();
        formData.append("file", data, fileName)
        pengajuanPAService.uploadProposal(id, formData)
            .finally(() => {
                setUploading(false);
                Swal.fire(
                    'Success',
                    'Data berhasil diupdate!',
                    'success'
                ).finally(() => window.location.reload());
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

    const uploadLaporan = (id, file) => {
        if (file !== null){
            const formData = new FormData();
            formData.append("file", file, fileNameLaporan)
            pengajuanPAService.uploadLaporan(id, formData)
                .finally(() => window.location.reload())
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

    const saveAsDraft = (e) => {
        saveAsDraftBpmn(form.id, form.status, createPayload)
            .finally(() => {
                if (file != null){
                    uploadFile(form.id, file);
                } else {
                    Swal.fire(
                        'Success',
                        'Data berhasil diupdate!',
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
    };

    useEffect(() => {
        if (props.item) {
            const { id, requestorCode, requestorName, requestorEmail, judulProyekAkhir, deskripsi, isMbkm, bidangTopikPA, programStudi, pembimbing1, pembimbing2, penguji, status } = props.item;
            setValues({ id, requestorCode, requestorName, requestorEmail, judulProyekAkhir, deskripsi, isMbkm, bidangTopikPA,programStudi, pembimbing1, pembimbing2, penguji, status });
            setMbkm(isMbkm)
        }
    }, [props.item]);

    return (
        <Form>
            <FormGroup>
                <Label for="name">Nama</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    required
                    onChange={onChange}
                    value={form.requestorName}
                />
            </FormGroup>
            <FormGroup>
                <Label for="nim">{role !== null && role === 'user' || role === 'mahasiswa'?"No BP":"NIP"}</Label>
                <Input
                    type="text"
                    name="nim"
                    id="nim"
                    required
                    onChange={onChange}
                    value={form.requestorCode}
                />
            </FormGroup>
            <FormGroup>
                <Label for="email">Email</Label>
                <Input
                    type="email"
                    name="email"
                    id="email"
                    required
                    onChange={onChange}
                    value={form.requestorEmail}
                />
            </FormGroup>
            <FormGroup>
                <Label for="programStudiId">Program Studi</Label>
                <br/>
                { isLoading &&
                    <Spinner/>
                }
                <Select options={optionProdi} onChange={setSelectedProdi} defaultValue={prodi?prodi:selectedProdi} placeholder="Pilih Program Studi" required/>
            </FormGroup>
            <FormGroup>
                <Label for="judulProyekAkhir">Judul</Label>
                <Input
                    type="text"
                    name="judulProyekAkhir"
                    id="judulProyekAkhir"
                    required
                    onChange={onChange}
                    value={form.judulProyekAkhir}
                />
            </FormGroup>
            <FormGroup>
                <Label for="deskripsi">Deskripsi</Label>
                <Input
                    type="textarea"
                    name="deskripsi"
                    id="deskripsi"
                    required
                    onChange={onChange}
                    value={form.deskripsi===null?"":form.deskripsi}
                />
            </FormGroup>
            <FormGroup>
                <Label for="isMbkm">Apakah MBKM?</Label>
                <br/>
                <Label style={{marginLeft: "20px"}}>
                    <Input type="radio" checked={mbkm} onChange={() => setMbkm(true)} name="isMbkm"/>
                    Ya
                </Label>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <Label style={{marginLeft: "20px"}}>
                    <Input type="radio" checked={!mbkm} onChange={() => setMbkm(false)} name="isMbkm"/>
                    Tidak
                </Label>
            </FormGroup>
            <FormGroup>
                <Label for="bidangTopikPA">Bidang Topik Penelitian</Label>
                <br/>
                <Select options={optionBidang} onChange={setSelectedBidang} defaultValue={bidang?bidang:selectedBidang} placeholder="Pilih Bidang Topik PA" required/>
            </FormGroup>

            <FormGroup>
                <Label for="pembimbing1Id">Dosen Pembimbing</Label>
                <Select options={optionDosen} onChange={setSelectedDosen} defaultValue={pembimbing?pembimbing:selectedDosen} placeholder="Pilih Pembimbing" required/>
            </FormGroup>
            {
                form.status !== "submitLaporanMbkm" && !mbkm &&
                <FormGroup>
                    <Label for="file">Upload Proposal</Label>
                    <Input type="file" name="file" onChange={onChangeFile} className="custom-file" required placeholder="Pilih file"
                           accept="application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"/>
                </FormGroup>
            }

            {
                (form.status !== "" && form.status !== "submitPraProposal" && form.status !== "revisiJudul" || mbkm)&&
                <FormGroup>
                    <Label for="file">Upload Laporan</Label>
                    <Input type="file" name="file" onChange={onChangeFileLaporan} className="custom-file" required placeholder="Pilih file" accept="application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"/>
                </FormGroup>
            }
            &nbsp;
            {
                props.item? <>
                <Button onClick={() => submit(form.id, form.status, createPayload)} color="primary" disabled={uploading}>Submit</Button>
                    &nbsp;
                    {
                        uploading? <>
                            <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                        </>:<></>
                    }
                <Button onClick={saveAsDraft} color="info" disabled={isLoading}>Draft</Button>
                </>
                    :
                    <Button onClick={startSubmit} color="primary" disabled={uploading}>Submit &nbsp;
                        {
                            uploading? <>
                                <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                            </>:<></>
                        }
                    </Button>

            }

        </Form>
    );
}

export default FormPengajuanProyekAkhir;
