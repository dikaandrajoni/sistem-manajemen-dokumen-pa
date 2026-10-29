import React, {useEffect, useState} from "react";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import Swal from "sweetalert2";
import dosenService from "../../services/DosenService";
import programStudiService from "../../services/ProgramStudiService";

function FormProgramStudi(props) {
    const [form, setValues] = useState({
        id: "",
        name: "",
        diploma: "",
        kaprodiId: "",
        kaprodiName: "",
        kaprodiEmail:"",
        kaprodiNip: ""
    });

    const [loading, setLoading] = useState(true);
    const [listDosen, setListDosen] = useState([]);

    const onChange = (e) => {
        setValues({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const getListDosen = () => {
        dosenService.getAll()
            .then(response => {
                setListDosen(response.data.data.content);
            })
            .finally(() =>
            setLoading(false))
            .catch(e => {
                Swal.fire(
                    'Warning',
                    "Gagal load data dosen!",
                    'warning'
                );
            });
    }

    const diplomaOptions = [
        {
            label: "D3",
            value: "D3",
        },
        {
            label: "D4",
            value: "D4",
        }
    ]

    const submitFormAdd = () => {

        console.log(form);

        programStudiService.create(form)
            .then(res =>{
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Berhasil tambah program studi!",
                        'success'
                    ).finally(() => window.location.reload());
                }
        })
            .catch(res => {
            Swal.fire(
                'Warning',
                "Gagal tambah data program studi!",
                'warning'
            );
        });
        props.toggle();
    };


    const submitFormEdit = () => {
        programStudiService.update(form.id, form)
            .then(res => {
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        "Data program studi berhasil diupdate!",
                        'success'
                    ).finally(() => window.location.reload());
                }
            })
            .catch(res => {
                Swal.fire(
                    'Warning',
                    "Gagal update data!",
                    'warning'
                );
            });
        props.toggle();
    };

    useEffect(() => {
        if (props.item) {
            const { id, name, diploma, kaprodiId, kaprodiName, kaprodiEmail, kaprodiNip } = props.item;
            setValues({ id, name, diploma, kaprodiId, kaprodiName, kaprodiEmail, kaprodiNip });
        }
        getListDosen()
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
                <Label for="diploma">Diploma</Label>
                <br/>
                <select name="diploma" onChange={onChange} value={form.diploma} className="custom-select">
                    <option></option>
                    {
                        diplomaOptions.map(value =>
                            <option key={value.value} value={value.value}>{value.label}</option>
                        )
                    }
                </select>
            </FormGroup>
            <FormGroup>
                <Label for="kaprodi">Koordinator</Label>
                <br/>
                <select required name="kaprodiId" value={form.kaprodiId}
                        onChange={onChange} className="custom-select">
                    {
                        props.item? <option key={props.item.kaprodiId} value={props.item.kaprodiId}>{props.item.kaprodiName}</option>:
                        listDosen.length > 0 && listDosen.map((data) =>
                            <option key={data.id} value={data.id}>{data.name}</option>
                        )
                    }
                </select>
            </FormGroup>
            <Button onClick={props.item?submitFormEdit:submitFormAdd}>Simpan</Button>
        </Form>
    );
}

export default FormProgramStudi;
