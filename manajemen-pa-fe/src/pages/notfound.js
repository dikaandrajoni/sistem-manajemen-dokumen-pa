import React, {Component} from 'react';
import Header from "../elements/header";
import {Link, withRouter} from "react-router-dom";
import PropTypes from 'prop-types'
import MenuSidebar from "../elements/menu";
import Navbar from "./home/Navbar";

class NotFound extends Component {
    render() {
        return (
            <div>
                {/*<Navbar/>*/}
                <div id="wrapper" style={{paddingLeft:"100px", paddingTop:"100px"}}>
                    <div id="content-wrapper">
                        <div className="container-fluid">
                            {/*<ol className="breadcrumb">*/}
                            {/*    <li className="breadcrumb-item">*/}
                            {/*        <Link to={'/'}>Home</Link>*/}
                            {/*    </li>*/}
                            {/*    <li className="breadcrumb-item active">404 Error</li>*/}
                            {/*</ol>*/}

                            <h1 className="display-1">404</h1>
                            <p className="lead">Page not found. You can
                                <a href={this.props.history.goBack} onClick={this.props.history.goBack}>go back</a>
                                to the previous page, or &nbsp;
                                <Link to={'/'}>return home</Link>.</p>
                        </div>
                    </div>
                    {/*<footer className="sticky-footer btn-block">*/}
                    {/*    <div className="container my-auto">*/}
                    {/*        <div className="copyright text-center my-auto">*/}
                    {/*            <span>Copyright © {(new Date().getFullYear())}</span>*/}
                    {/*        </div>*/}
                    {/*    </div>*/}
                    {/*</footer>*/}
                </div>
            </div>
        );
    }
}

const {string, object} = PropTypes;
NotFound.propTypes = {
    title: string.isRequired,
    history: object
};

export default withRouter(NotFound)
