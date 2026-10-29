import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalHeader} from "reactstrap";
import FormDosen from "./FormDosen";

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
                style={{ float: "left", marginRight: "5px", color: "white"}}
            >
                <i className="fas fa-edit"/>
            </Button>
        );
        title = "Edit Data Dosen";
    } else {
        button = (
            <Button
                color="success"
                onClick={toggle}
                style={{ float: "left", marginRight: "10px" }}
            >
                <i className="fas fa-plus"/> &nbsp;{label}
            </Button>
        );
        title = "Tambah Data Dosen";
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
            >
                <ModalHeader toggle={toggle} close={closeBtn}>
                    {title}
                </ModalHeader>
                <ModalBody>
                    <FormDosen
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
