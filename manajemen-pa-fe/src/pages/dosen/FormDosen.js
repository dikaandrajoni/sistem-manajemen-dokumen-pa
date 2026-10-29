import React, {useEffect, useState} from "react";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import programStudiService from "../../services/ProgramStudiService";
import Swal from "sweetalert2";
import dosenService from "../../services/DosenService";


function FormDosen(props) {
    const [form, setValues] = useState({
        id: "",
        nip: "",
        name:"",
        email: "",
        programStudiId:"",
        programStudi: {
            id:"",
            name:""
        },
        submitted: false
    });

    const [loading, setLoading] = useState(false);
    const [listProdi, setListProdi] = useState([]);

    const onChange = (e) => {
        setValues({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const createPayload = {
        name: form.name,
        nip: form.nip,
        email: form.email,
        programStudi: {
            id:form.programStudiId
        }
    }

    const submitFormAdd = () => {
        var payload = createPayload;
        dosenService.create(payload)
            .then(r =>{
                if (r.data.status === 200){
                    Swal.fire(
                        'Success',
                        'Berhasil tambah data dosen',
                        'success'
                    );
                    const id = r.data.data.id;
                    console.log(id)
                }
            }).catch(e => {
                Swal.fire(
                    'Ops..',
                    e.response.data.data,
                    'error'
                )
        });

        props.addItemToState(form);
        props.toggle();
    };


    const submitFormEdit = () => {
        var payload = createPayload;
        dosenService.update(form.id, payload)
            .then(r => {
            if (r.data.status === 200){
                setValues(r.data.data);
                Swal.fire(
                    'Success',
                    'Data Berhasil Diupdate',
                    'success'
                ).finally(() => window.location.reload());
            }
        })
            .catch(e => {
            Swal.fire(
                'Ops..',
                e.response.data.data,
                'error'
            )
        });
    };

    const getListProdi = () => {
        setLoading(true)
        programStudiService.getAll()
            .then(response => {
                setListProdi(response.data.data.content);
            })
            .finally(() =>
                setLoading(false))
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data program studi!",
                    'warning'
                );
            });
    }

    useEffect(() => {
        if (props.item) {
            const { id, name, nip, email, programStudi } = props.item;
            setValues({ id, name, nip, email, programStudi });
        }
       getListProdi();
    }, [props.item]);

    return (
        <Form onSubmit={props.item? submitFormEdit : submitFormAdd}>
            <FormGroup>
                <Label for="nip">Nip</Label>
                <Input
                    type="text"
                    name="nip"
                    id="nip"
                    required
                    onChange={onChange}
                    value={form.nip}
                />
            </FormGroup>
            <FormGroup>
                <Label for="name">Nama</Label>
                <Input
                    type="text"
                    name="name"
                    id="name"
                    required
                    onChange={onChange}
                    value={form.name}
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
                    value={form.email}
                />
            </FormGroup>

            <FormGroup>
                <Label for="programStudiId">Program Studi</Label>
                <br/>
                <select required value={form.programStudiId} name="programStudiId"
                        onChange={onChange} className="custom-select"
                        required>
                    {
                        props.item? <option key={props.item.programStudi.id} value={props.item.programStudi}>{props.item.programStudi.name}</option>:
                            listProdi.length > 0 && listProdi.map((data) =>
                                <option key={data.id} value={data.id}>{data.name}</option>
                            )
                    }
                </select>
            </FormGroup>
            <Button type="submit" className="btn btn-primary" color="primary">Simpan</Button>
        </Form>
    );
}

export default FormDosen;
