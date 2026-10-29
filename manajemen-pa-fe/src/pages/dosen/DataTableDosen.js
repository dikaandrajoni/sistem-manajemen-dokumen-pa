import React, {useEffect, useState} from 'react'
import {Button, Table} from 'reactstrap';
import ModalDosen from "./ModalDosen";
import Swal from "sweetalert2";
import dosenService from "../../services/DosenService";
import {PaginatedList} from "react-paginated-list";

function DataTable(props){
    const [role, setRole] = useState(null);

    useEffect(() => {
        setRole(window.localStorage.getItem("role"));
    }, []);

    const deleteItem = id => {
        let confirmDelete = window.confirm('Delete item forever?')
        if(confirmDelete){
            dosenService.delete(id)
                .then(res => {
                    if (res.data.status === 200){
                        Swal.fire(
                            'Success',
                            'Data berhasil dihapus',
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
    }

    const generateAkun = (id) => {
        dosenService.createAkun(id)
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
                    data.map((data, idx) =>
                        <tr key={data.id}>
                            <td>{idx+1}</td>
                            <td>{data.nip}</td>
                            <td>{data.name}</td>
                            <td>{data.email}</td>
                            {/*<td>{data.positionName}</td>*/}
                            {/*<td>{data.programStudi===null?"":data.programStudi.name}</td>*/}
                            { role === 'admin' &&
                                <td>
                                    <div style={{width:"110px"}}>
                                        <ModalDosen buttonLabel="Edit" item={data} updateState={props.updateState}/>
                                        {' '}
                                        <Button color="danger" onClick={() => deleteItem(data.id)}> <i className="fas fa-trash"/></Button>
                                        {
                                            role === "admin" &&
                                            <Button color="primary" onClick={() => generateAkun(data.id)}><i className="fa fa-user-plus"/> </Button>
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
                            <th>Nip</th>
                            <th>Nama</th>
                            <th>Email</th>
                            {/*<th>Position</th>*/}
                            {/*<th>Program Studi</th>*/}
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
