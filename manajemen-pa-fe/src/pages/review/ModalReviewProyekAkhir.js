import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalHeader} from "reactstrap";
import FormReviewProyekAkhir from "./FormReviewProyekAkhir";

function ModalReview(props) {
    const [modal, setModal] = useState(false);

    const toggle = () => {
        setModal(!modal);
    };

    const closeBtn = (
        <button className="close" onClick={toggle}>
            &times;
        </button>
    );

    let button = "";
    let title=props.item.status === "aturJadwalSeminar" || props.item.status === "aturJadwalSidang"?"Atur Jadwal":"Review Penelitian";

    button = (
        <Button
            color="primary"
            onClick={toggle}
            style={{ float: "left", margin: "3px"}}
        >
          <i className="fas fa-eye small"/>
        </Button>
    );

    return (
        <div>
            {button}
            <Modal
                isOpen={modal}
                toggle={toggle}
                className={props.className}
                backdrop={"static"}
                keyboard={false}
                contentClassName="custom-modal-style"
            >
                <ModalHeader toggle={toggle} close={closeBtn}>
                    {title}
                </ModalHeader>
                <ModalBody>
                    <FormReviewProyekAkhir
                        review={props.review}
                        toggle={toggle}
                        item={props.item}
                    />
                </ModalBody>

            </Modal>
        </div>
    );
}

export default ModalReview;
