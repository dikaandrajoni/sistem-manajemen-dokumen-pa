import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalHeader} from "reactstrap";
import FormProgramStudi from "./FormProgramStudi";

function ModalProgramStudi(props) {
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
                style={{ float: "left", marginRight: "10px", color:"white" }}
            >
                <i className="fa fa-edit"/>
            </Button>
        );
        title = "Edit Program Studi";
    } else {
        button = (
            <Button
                color="success"
                onClick={toggle}
                style={{ float: "left", marginRight: "10px" }}
            >
                <i className="fas fa-plus"/>&nbsp;{label}
            </Button>
        );
        title = "Tambah Program Studi";
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
                    <FormProgramStudi
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

export default ModalProgramStudi;
