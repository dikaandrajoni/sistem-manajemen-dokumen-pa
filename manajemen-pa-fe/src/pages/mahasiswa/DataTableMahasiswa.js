import React, {useEffect, useState} from 'react'
import {Button, Table} from 'reactstrap';
import ModalMahasiswa from "./ModalMahasiswa";
import mahasiswaService from "../../services/MahasiswaService";
import Swal from "sweetalert2";
import {PaginatedList} from "react-paginated-list";

function DataTable(props){

    const [role, setRole] = useState(null);
    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
    }, []);

    const deleteItem = id => {
        let confirmDelete = window.confirm('Delete item forever?')
        if(confirmDelete){
            mahasiswaService.delete(id)
                .then(res => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            "Data berhasil dihapus!",
                            'success'
                        );
                        window.location.reload();
                    }
                })
                .catch(res =>{
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

    const generateAkun = (id) => {
        mahasiswaService.createAkunMahasiswa(id)
            .then(res => {
                if (res.data.status === 200){
                    Swal.fire(
                        'Success',
                        'Akun berhasil dibuat!, Password default (12345678)',
                        'success'
                    );
                }
            })
            .catch(res => {
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

    const items = (data) => {
        return <>
            {
                data.length === 0 ? <>
                    <tr>
                        <td colSpan={10}>
                            <center>Tidak ada data.</center>
                        </td>
                    </tr>
                </> :
                    data.map((item, index) =>
                        <tr key={item.id}>
                            <th scope="row">{index+1}</th>
                            <td>{item.nim}</td>
                            <td>{item.name}</td>
                            <td>{item.email}</td>
                            <td>{item.kelas}</td>
                            <td>{item.programStudi?item.programStudi.name:""}</td>
                            { role === 'admin' &&
                                <td>
                                    <div style={{width:"130px"}}>
                                        <ModalMahasiswa buttonLabel="Edit" item={item} updateState={props.updateState}/>
                                        {' '}
                                        <Button color="danger" onClick={() => deleteItem(item.id)}><i className="fa fa-trash"/></Button>
                                        {
                                            role === "admin"?
                                                <Button color="primary" onClick={() => generateAkun(item.id)}><i className="fa fa-user-plus"/> </Button>
                                                :<></>
                                        }
                                    </div>
                                </td>
                            }
                        </tr>
                    )
            }
        </>
    }

    return (
        <PaginatedList
            list={props.items}
            itemsPerPage={10}
            renderList={(list) => (
                <>
                    <Table responsive hover>
                        <thead>
                        <tr>
                            <th>No</th>
                            <th>No BP</th>
                            <th>Nama</th>
                            <th>Email</th>
                            <th>Kelas</th>
                            <th>Program Studi</th>
                            { role === 'admin' &&
                                <th>Aksi</th>
                            }
                        </tr>
                        </thead>
                        <tbody>
                        {items(list)}
                        </tbody>
                    </Table>
                </>
            )}
        />
    )
}

export default DataTable
