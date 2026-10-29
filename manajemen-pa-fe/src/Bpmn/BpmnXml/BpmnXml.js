import React, {useEffect, useState} from 'react';
import {getBpmnList, getBpmnStatistic, getBpmnXML} from "../../api";
import {RenderXml} from "../../utils/Bpmn";
import {Button} from "reactstrap";

const BpmnXml = ({name}) => {
    const [height, setHeight] = useState("500px");
    const [width, setWidth] = useState("100%");

    useEffect(() => {
        const fetchBpmnXml = async () => {
            try {
                const {data} = await getBpmnXML();
                const res = await getBpmnStatistic();
                const list = await getBpmnList();
                return {xml: data, statistic: res.data.data, listTasks: list.data.data.content};
            } catch (e) {
                return e;
            }
        }

        fetchBpmnXml().then(r => RenderXml("#canvasWrapper","#btn-reset", "#btn-zoom-in", "#btn-zoom-out", r.xml, r.statistic, r.listTasks));
    }, []);

    return (
        <div>
            {/*<Button id="btn-zoom-out" variant="primary"><i className="fa fa-search-plus"/></Button>{" "}*/}
            {/*<Button id="btn-zoom-in" variant="secondary"><i class="fa fa-search-minus"/></Button>{" "}*/}
            {/*<Button id="btn-reset" variant="secondary"><i className="fa fa-undo"/></Button>{" "}*/}
            <div id="canvasWrapper" style={{height: height, width: width, margin: "auto", color: "darkgray"}} />
        </div>
    );
};

export default BpmnXml;
