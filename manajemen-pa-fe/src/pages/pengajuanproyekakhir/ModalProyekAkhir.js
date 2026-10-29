import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalHeader} from "reactstrap";
import FormPengajuanProyekAkhir from "./FormPengajuanProyekAkhir";

function ModalForm(props) {
    const [modal, setModal] = useState(false);

    const toggle = () => {
        setModal(!modal);
    };

    const closeBtn = (
        <button className="close" onClick={toggle}>
            &times;
        </button>
    );
    const label = props.buttonLabel;

    let button = "";
    let title = "";

    if (label === "Edit") {
        button = (
            <Button
                color="warning"
                onClick={toggle}
                style={{margin: "5px", color: "white", width:"max-content"}}
            >
                <i className="fas fa-edit smaller"/>
            </Button>
        );
        title = "Edit Pengajuan";
    } else {
        button = (
            <Button
                color="success"
                onClick={toggle}
                style={{ float: "left", marginRight: "10px" }}
            >
                <i className="fas fa-plus"/>&nbsp;
                {label}
            </Button>
        );
        title = "Ajukan Judul Penelitian";
    }

    return (
        <div>
            {button}
            <Modal
                isOpen={modal}
                toggle={toggle}
                className={props.className}
                contentClassName="custom-modal-style"
                backdrop={"static"}
                keyboard={false}
            >
                <ModalHeader toggle={toggle} close={closeBtn}>
                    {title}
                </ModalHeader>
                <ModalBody>
                    <FormPengajuanProyekAkhir
                        addItemToState={props.addItemToState}
                        updateState={props.updateState}
                        toggle={toggle}
                        item={props.item}
                    />
                </ModalBody>
            </Modal>
        </div>
    );
}

export default ModalForm;
