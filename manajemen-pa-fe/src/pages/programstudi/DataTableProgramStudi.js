import React, {useEffect, useState} from 'react'
import {Button, Table} from 'reactstrap';
import ModalProgramStudi from "./ModalProgramStudi";
import Swal from "sweetalert2";
import programStudiService from "../../services/ProgramStudiService";


function DataTable(props){
    const [role, setRole] = useState(null);
    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
    }, []);

    const deleteItem = id => {
        let confirmDelete = window.confirm('Delete item forever?')
        if(confirmDelete){
            programStudiService.delete(id)
                .then(res => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Data berhasil dihapus!",
                            'success'
                        );
                    }
                })
                .finally(() => window.location.reload())
                .catch(res =>{
                    console.log(res);
            });
        }
    }

    const items = props.items.map((item, index) => {
        return (
            <tr key={item.id}>
                <th scope="row">{index+1}</th>
                <td>{item.diploma}</td>
                <td>{item.name}</td>
                <td>{item.kaprodiName}</td>
                <td>{item.kaprodiEmail}</td>
                <td>{item.kaprodiNip}</td>
                {role === 'admin' &&
                    <td>
                        <div style={{width:"130px"}}>
                            <ModalProgramStudi buttonLabel="Edit" item={item} updateState={props.updateState}/>
                            {' '}
                            <Button color="danger" onClick={() => deleteItem(item.id)}><i className="fa fa-trash"/></Button>
                        </div>
                    </td>
                }
            </tr>
        )
    })

    return (
        <Table responsive hover>
            <thead>
            <tr>
                <th>No</th>
                <th>Diploma</th>
                <th>Nama</th>
                <th>Nama Koordinator</th>
                <th>Email Koordinator</th>
                <th>Nip Koordinator</th>
                {role === 'admin' &&
                    <th>Aksi</th>
                }
            </tr>
            </thead>
            <tbody>
            {items}
            </tbody>
        </Table>
    )
}

export default DataTable
