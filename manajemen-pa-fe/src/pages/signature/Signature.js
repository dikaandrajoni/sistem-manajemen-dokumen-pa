import React, {useRef, useState} from "react";
import Popup from "reactjs-popup";
import SignaturePad from "react-signature-canvas";

function Signature() {
    const [imageURL, setImageURL] = useState(null);
    const sigCanvas = useRef({});
    const clear = () => sigCanvas.current.clear();
    const save = () => (
        setImageURL(sigCanvas.current.getTrimmedCanvas().toDataURL("image/png"))
    );

    const save2 = closeFunc => {
        // const sig = new Image();
        // sig.src = sigCanvas.current.getTrimmedCanvas().toDataURL('image/png');
        // console.log(sigCanvas.current.getTrimmedCanvas().toDataURL('image/png'));
        fetch(sigCanvas.current.getTrimmedCanvas().toDataURL("image/png"))
            .then(res => res.blob())
            .then(blob => {
                const file = new File([blob], "signature", { type: "image/png" });
                // setSignatureFile(file);
                console.log(file);
            });
        setImageURL(sigCanvas.current.getTrimmedCanvas().toDataURL("image/png"));
        closeFunc();
    };

    return (
        <div className="container">
            <h1>Draw Signature</h1>
            <SignaturePad
                ref={sigCanvas}
                canvasProps={{
                    className: "signatureCanvas"
                }}
            />
            <Popup
                modal
                trigger={<button className="btn btn-primary" onClick={save} >Save</button>}
                closeOnDocumentClick={false}
                position="center center"
            >
                {close => (
                    <>
                    <div className="card mb-2">
                        {imageURL &&
                            <img
                                src={imageURL}
                                alt="my signature"
                                style={{
                                    display: "block",
                                    margin: "0 auto",
                                    border: "1px solid black",
                                    width: "250px"
                                }}
                            />
                        }
                    </div>
                        <button className="btn btn-primary">Upload </button>
                        &nbsp;
                        <button className="btn btn-warning" onClick={close}> Close</button>
                    </>
                )}
            </Popup>
            &nbsp;
            <button className="btn btn-danger" onClick={clear}> Clear</button>
            <br />
            <br />
        </div>
    );
}

export default Signature;
