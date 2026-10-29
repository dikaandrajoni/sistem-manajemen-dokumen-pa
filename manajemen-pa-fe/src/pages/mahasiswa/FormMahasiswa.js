import React, {useEffect, useState} from "react";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import programStudiService from "../../services/ProgramStudiService";
import Swal from "sweetalert2";
import mahasiswaService from "../../services/MahasiswaService";

function FormMahasiswa(props) {
    const [form, setValues] = useState({
        id: "",
        name: "",
        nim: "",
        email: "",
        jenisKelamin: "",
        programStudiId:"",
        programStudi: {
            id:"",
            name:""
        },
        kelas:""
    });

    const [listProdi, setListProdi] = useState([]);

    const onChange = (e) => {
        setValues({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const getListProdi = () => {
        programStudiService.getAll()
            .then(response => {
                setListProdi(response.data.data.content);
            })
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data program studi!",
                    'warning'
                );
            });
    }

    const createPayload = {
        name: form.name,
        nim: form.nim,
        email: form.email,
        jenisKelamin: form.jenisKelamin,
        programStudi: {
            id:form.programStudiId
        },
        kelas:form.kelas
    }

    const submitFormAdd = () => {
        var payload = createPayload;

        mahasiswaService.create(payload)
            .then(res =>{
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Data mahasiswa berhasil ditambah!",
                        'success'
                    ).finally(() => window.location.reload());
                }
        })
            .catch(res => {
            Swal.fire(
                'Warning',
                "Gagal tambah data mahasiswa!",
                'warning'
            );
        });
        props.toggle();
    };


    const submitFormEdit = () => {
        var payload = createPayload;
        mahasiswaService.update(form.id, payload)
            .then(res => {
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Data mahasiswa berhasil diupdate!",
                        'success'
                    ).finally(() => window.location.reload());
                }
            })
            .catch(res => {
                Swal.fire(
                    'Warning',
                    res.response.data.data,
                    'warning'
                );
            });
        props.toggle();
    };

    useEffect(() => {
        if (props.item) {
            const { id, name, nim, email, kelas, programStudi, jenisKelamin } = props.item;
            setValues({ id, name, nim, email, kelas, programStudi, jenisKelamin });
        }
        getListProdi()
    }, [props.item]);


    return (
        // <Form onSubmit={props.item? submitFormEdit : submitFormAdd}>
        <Form>
            <FormGroup>
                <Label for="name">Nama</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    onChange={onChange}
                    value={form.name}
                />
            </FormGroup>
            <FormGroup>
                <Label for="nim">No BP</Label>
                <Input
                    type="text"
                    name="nim"
                    id="nim"
                    onChange={onChange}
                    value={form.nim}
                />
            </FormGroup>
            <FormGroup>
                <Label for="email">Email</Label>
                <Input
                    type="email"
                    name="email"
                    id="email"
                    onChange={onChange}
                    value={form.email}
                />
            </FormGroup>
            <FormGroup>
                <Label for="jenisKelamin">Jenis Kelamin</Label>
                <br/>
                <select required value={form.jenisKelamin}
                        onChange={onChange} name="jenisKelamin" id="jenisKelamin" className="custom-select">
                    <option value=""></option>
                    <option value="laki-laki">Laki-laki</option>
                    <option value="perempuan">Perempuan</option>
                </select>
            </FormGroup>
            <FormGroup>
                <Label for="programStudiId">Program Studi</Label>
                <br/>
                <select required name="programStudiId" value={form.programStudiId}
                        onChange={onChange} className="custom-select">
                    <option value=""></option>
                    {
                        props.item? <option key={props.item.programStudi.id} value={props.item.programStudi}>{props.item.programStudi.name}</option>:
                        listProdi.length > 0 && listProdi.map((data) =>
                            <option key={data.id} value={data.id}>{data.name}</option>
                        )
                    }
                </select>
            </FormGroup>
            <FormGroup>
                <Label for="kelas">Kelas</Label>
                <Input
                    type="text"
                    name="kelas"
                    id="kelas"
                    onChange={onChange}
                    value={form.kelas}
                />
            </FormGroup>
            {/*<FormGroup>*/}
            {/*    <Label for="date">Tanggal</Label>*/}
            {/*    <Input type="date" data-date="" data-date-format="DD MMMM YYYY" value={form.date} id="date" name="date"/>*/}
            {/*</FormGroup>*/}
            <Button onClick={props.item?submitFormEdit:submitFormAdd}>Simpan</Button>
        </Form>
    );
}

export default FormMahasiswa;
