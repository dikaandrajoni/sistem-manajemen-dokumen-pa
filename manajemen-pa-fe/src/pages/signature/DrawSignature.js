import React, {Component} from 'react';
import Header from "../../elements/header";
import "../../sigCanvas.css"
import Signature from "./Signature";
import MenuSidebar from "../../elements/menu";

export default class DrawSignature extends Component {

    render() {
        return (
            <div>
                <Header/>
                <div id="wrapper">
                    <MenuSidebar/>
                    <div id="content-wrapper">
                        <div className="container-fluid">
                            <div className="card mb-3">
                               <Signature/>
                            </div>
                        </div>
                        <footer className="sticky-footer">
                            <div className="container my-auto">
                                <div className="copyright text-center my-auto">
                                    <span>Copyright © {(new Date().getFullYear())}</span>
                                </div>
                            </div>
                        </footer>
                    </div>
                </div>
            </div>

        );
    }
}
