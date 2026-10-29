import React, {useState} from "react";
import {Button, Modal, ModalBody, ModalHeader} from "reactstrap";
import pengajuanPAService from "../../services/PengajuanPAService";
import "./preview_style.css"
import Swal from "sweetalert2";
import Loader from "react-loader-spinner";
import {Document, Page, pdfjs} from 'react-pdf/dist/esm/entry.webpack';

pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

function ModalPreview(props) {
    const [isLoading, setIsLoading] = useState(false);
    const [modal, setModal] = useState(false);
    const [fileUrl, setFileUrl] = useState(null);

    const toggle = () => {
        setModal(!modal);
    };

    const closeBtn = (
        <button className="close" onClick={toggle}>
            &times;
        </button>
    );

    let title="Review Document";
    let button = "";

    button = (
        <center>
            <Button
                color={props.mode ==="proposal"?"info":"secondary"}
                onClick={() => {
                    getUrl();
                }}
                style={{ float: "none", position: "relative" ,marginRight: "10px"}}
            >
                <i className="fas fa-book-open"/>
                &nbsp;{props.mode === "proposal"?"Review Proposal":"Review Laporan"}
            </Button>
        </center>
    );

    const getUrl = () => {
        setIsLoading(true);
        if (props.mode === "proposal"){
            pengajuanPAService.getFileUrl(props.item.id)
                .then(res => {
                    setFileUrl(res.data.data);
                    console.log(res.data.data)
                    toggle();
                })
                .finally(() => setIsLoading(false))
                .catch((res) => {
                    Swal.fire(
                        'Warning',
                        res.response.data.message,
                        'warning'
                    )
                });
        } else {
            pengajuanPAService.getLaporanUrl(props.item.id)
                .then(res => {
                    setFileUrl(res.data.data);
                    console.log(res.data.data)
                    toggle();
                })
                .finally(() => setIsLoading(false))
                .catch((res) => {
                    Swal.fire(
                        'Warning',
                        res.response.data.message,
                        'warning'
                    )
                });
        }

    };

    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    const onDocumentLoadSuccess = ({ numPages }) => {
        setNumPages(numPages);
        // setIsLoading(false);
    };

    const goToPrevPage = () =>
        setPageNumber(pageNumber - 1 <= 1 ? 1 : pageNumber - 1);

    const goToNextPage = () =>
        setPageNumber(
            pageNumber + 1 >= numPages ? numPages : pageNumber + 1,
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
                contentClassName={"custom-preview"}
            >
                <ModalHeader toggle={toggle} close={closeBtn}>
                    {title}
                </ModalHeader>
                <ModalBody>
                    <div className="container">
                        {isLoading?
                            <div className="spinner">
                                <Loader type="ThreeDots" color="#2BAD60" height="100" width="100" />
                            </div>
                            :<>
                                <nav>
                                    <button className="btn btn-outline-primary" onClick={goToPrevPage}>Prev</button>&nbsp;
                                    <button className="btn btn-outline-primary" onClick={goToNextPage}>Next</button>
                                    <p>
                                        Page {pageNumber} of {numPages}
                                    </p>
                                </nav>
                                <div className="card mb-3">
                                    <Document
                                        file={fileUrl}
                                        onLoadSuccess={onDocumentLoadSuccess}
                                    >
                                        <Page pageNumber={pageNumber} />
                                    </Document>
                                </div>
                            </>
                        }
                    </div>
                </ModalBody>
            </Modal>
        </div>
    );
}

export default ModalPreview;

